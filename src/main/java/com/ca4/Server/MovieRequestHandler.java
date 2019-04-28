package com.ca4.Server;

import com.ca4.Core.MovieServiceDetails;
import com.ca4.DAO.*;
import com.ca4.DTO.Movie;
import com.ca4.DTO.User;
import com.ca4.DTO.WatchedMovie;
import com.ca4.Exceptions.DAOException;
import com.ca4.Server.Cache.Cache;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

class MovieRequestHandler {
    private static Cache cache = new Cache();

    static String registerUser(String email, String password) {
        UserDAOInterface userDAO = new MySQLUserDAO();
        String hashedPassword = hash(password);
        String response = MovieServiceDetails.FAIL;

        try {
            boolean isRegistered = userDAO.isUserAlreadyRegistered(email);

            if (!isRegistered) {
                //The user isn't already registered, attempt to register them
                int userID = userDAO.registerUser(email, hashedPassword);

                response = MovieServiceDetails.REGISTER_SUCCESS + MovieServiceDetails.BREAKING_CHARACTER + userID;
            } else {
                response = MovieServiceDetails.REGISTER_ALREADY_REGISTERED;
            }
        } catch (DAOException e) {
            writeToErrorLogFile(e.getMessage());
        }

        return response;
    }

    static String loginUser(String email, String password) {
        UserDAOInterface userDAO = new MySQLUserDAO();
        String response = MovieServiceDetails.FAIL;

        try {
            boolean isRegistered = userDAO.isUserAlreadyRegistered(email);

            if (isRegistered) {
                User toLogin = userDAO.loginUser(email);
                boolean isLoggedIn = verifyHash(password, toLogin.getPassword());

                if (isLoggedIn) {
                    response = MovieServiceDetails.LOGIN_SUCCESS + MovieServiceDetails.BREAKING_CHARACTER + toLogin.getId();
                } else {
                    response = MovieServiceDetails.LOGIN_WRONG_INFO;
                }
            } else {
                response = MovieServiceDetails.LOGIN_NOT_REGISTERED;
            }
        } catch (DAOException e) {
            writeToErrorLogFile(e.getMessage());
        }

        return response;
    }

    static String deleteUser(int userID) {
        UserDAOInterface userDAO = new MySQLUserDAO();
        String response = MovieServiceDetails.FAIL;

        try {
            boolean isDeleted = userDAO.deleteUser(userID);

            if (isDeleted) {
                response = MovieServiceDetails.DELETE_USER_SUCCESS;
            }
        } catch (DAOException e) {
            writeToErrorLogFile(e.getMessage());
        }

        return response;
    }

    static String searchForMovieByTitle(String searchString) {
        MovieDAOInterface movieDAO = new MySQLMovieDAO();
        String response = MovieServiceDetails.FAIL;

        try {
            String cacheResult = cache.queryMovieTitleCache(searchString);

            if (cacheResult.equals("")) {
                System.out.println("Adding object to cache");

                Movie movie = movieDAO.getMovieByName(searchString);
                response = movie.toJSONString();
                cache.addToMovieTitleCache(searchString, response);
            } else {
                System.out.println("Retrieving object from cache");
                response = cacheResult;
            }
        } catch (DAOException e) {
            writeToErrorLogFile(e.getMessage());
        }

        cache.checkAllCaches();
        return response;
    }

    static String searchForMovieByDirector(String searchString) {
        MovieDAOInterface movieDAO = new MySQLMovieDAO();
        String response = MovieServiceDetails.FAIL;

        try {
            String cacheResult = cache.queryMovieDirectorCache(searchString);

            if (cacheResult.equals("")) {
                ArrayList<Movie> movies = movieDAO.getMoviesByDirector(searchString);

                if (movies.size() >= 1) {
                    System.out.println("Adding object to cache");

                    response = buildMovieJSONString(movies);
                    cache.addToMovieDirectorCache(searchString, response);
                }
            } else {
                System.out.println("Retrieving object from cache");
                response = cacheResult;
            }
        } catch (DAOException e) {
            writeToErrorLogFile(e.getMessage());
        }

        cache.checkAllCaches();
        return response;
    }

    static String searchForMovieByGenre(String searchString) {
        MovieDAOInterface movieDAO = new MySQLMovieDAO();
        String response = MovieServiceDetails.FAIL;

        try {
            String cacheResult = cache.queryMovieGenreCache(searchString);

            if (cacheResult.equals("")) {
                ArrayList<Movie> movies = movieDAO.getMoviesByGenre(searchString);

                if (movies.size() >= 1) {
                    System.out.println("Adding object to cache");

                    response = buildMovieJSONString(movies);
                    cache.addToMovieGenreCache(searchString, response);
                }
            } else {
                System.out.println("Retrieving object from cache");
                response = cacheResult;
            }

        } catch (DAOException e) {
            writeToErrorLogFile(e.getMessage());
        }

        cache.checkAllCaches();
        return response;
    }

    /**
     * Converts a movie JSON String to the movie class and adds it to the database
     *
     * @param movieJSONString A full movie JSON String
     * @return Server response message
     */
    static String addMovie(String movieJSONString) {
        MovieDAOInterface movieDAO = new MySQLMovieDAO();
        String response = MovieServiceDetails.FAIL;

        try {
            Movie movieToAdd = convertJSONStringToMovie(movieJSONString);
            boolean isAdded = movieDAO.addMovieToDatabase(movieToAdd);

            if (isAdded) {
                response = MovieServiceDetails.ADD_SUCCESS;
                //The database has been updated so all the caches need to be rebuilt
                cache.dumpAllCaches();
            }
        } catch (DAOException e) {
            writeToErrorLogFile(e.getMessage());
        }

        return response;
    }

    static String removeMovie(int idOfMovieToRemove) {
        MovieDAOInterface movieDAO = new MySQLMovieDAO();
        String response = MovieServiceDetails.FAIL;

        try {
            boolean isDeleted = movieDAO.deleteMovie(idOfMovieToRemove);

            if (isDeleted) {
                response = MovieServiceDetails.REMOVE_SUCCESS;
                //The database has been updated so all the caches need to be rebuilt
                cache.dumpAllCaches();
            }
        } catch (DAOException e) {
            writeToErrorLogFile(e.getMessage());
        }

        return response;
    }

    /**
     * Converts a movie JSON String to the movie class and updates it in the database
     *
     * @param movieJSONString A full movie JSON String
     * @return Server response message
     */
    static String updateMovie(String movieJSONString) {
        MovieDAOInterface movieDAO = new MySQLMovieDAO();
        String response = MovieServiceDetails.FAIL;

        try {
            Movie movieToUpdate = convertJSONStringToMovie(movieJSONString);
            boolean isUpdated = movieDAO.updateMovie(movieToUpdate);

            if (isUpdated) {
                response = MovieServiceDetails.UPDATE_SUCCESS;
                //The database has been updated so all the caches need to be rebuilt
                cache.dumpAllCaches();
            }
        } catch (DAOException e) {
            writeToErrorLogFile(e.getMessage());
        }

        return response;
    }

    static String watchMovie(int userID, int movieID) {
        WatchedMovieDAOInterface watchedMovieDAO = new MySQLWatchedMovieDAO();
        String response = MovieServiceDetails.FAIL;

        try {
            WatchedMovie watchedMovie = new WatchedMovie(userID, movieID);
            boolean isWatched = watchedMovieDAO.addWatchedMovie(watchedMovie);

            if (isWatched) {
                response = MovieServiceDetails.WATCH_SUCCESS;
            }
        } catch (DAOException e) {
            writeToErrorLogFile(e.getMessage());
        }

        return response;
    }

    static String recommendMovie(int userID) {
        String response = MovieServiceDetails.FAIL;

        try {
            ArrayList<Movie> recommendations = getRandomMovieBasedOnGenre(userID);

            while (recommendations.size() < 3) {
                recommendations.add(getRandomMovie());
            }

            response = buildMovieJSONString(recommendations);
        } catch (DAOException e) {
            writeToErrorLogFile(e.getMessage());
        }

        return response;
    }

    private static ArrayList<Movie> getRandomMovieBasedOnGenre(int userID) throws DAOException {
        MovieDAOInterface movieDAO = new MySQLMovieDAO();
        WatchedMovieDAOInterface watchedMovieDAO = new MySQLWatchedMovieDAO();
        List<WatchedMovie> usersWatchedMovies = watchedMovieDAO.getAllUsersWatchedMovies(userID);
        ArrayList<String> watchedGenres = new ArrayList<>();
        ArrayList<Movie> recommendations = new ArrayList<>();

        //Find all the genres the user has watched
        for (WatchedMovie watchedMovie : usersWatchedMovies) {
            Movie movie = movieDAO.getMovieByID(watchedMovie.getMovieID());

            if (!watchedGenres.contains(movie.getGenre())) {
                watchedGenres.add(movie.getGenre());
            }
        }

        if (watchedGenres.size() > 0) {
            for (String genre : watchedGenres) {
                String cachedResult = cache.queryMovieGenreCache(genre);
                ArrayList<Movie> movies;

                if (cachedResult.equals("")) {
                    //Search for all movies in given genre
                    movies = movieDAO.getMoviesByGenre(genre);

                    if (movies.size() >= 1) {
                        System.out.println("Adding object to cache");
                        cache.addToMovieGenreCache(genre, buildMovieJSONString(movies));
                    }
                } else {
                    //Rebuild the cache object into movies
                    movies = new ArrayList<>();
                    JSONArray cachedGenre = new JSONArray(cachedResult);

                    for (Object o : cachedGenre) {
                        Movie movie = convertJSONStringToMovie(o.toString());
                        movies.add(movie);
                    }
                }

                if (movies.size() >= 1) {
                    //Pick a random movie from the list to recommend
                    int randomMovieID = getRandomInt(0, movies.size());
                    recommendations.add(movies.get(randomMovieID));
                }
            }

            cache.checkAllCaches();
        }

        return recommendations;
    }

    private static Movie getRandomMovie() throws DAOException {
        MovieDAOInterface movieDAO = new MySQLMovieDAO();
        int randomMovieID = getRandomInt(14, 1052);

        return movieDAO.getMovieByID(randomMovieID);
    }

    private static int getRandomInt(int min, int max) {
        Random random = new Random();
        //Taken from - https://stackoverflow.com/questions/5887709/getting-random-numbers-in-java
        return random.nextInt((max - min) + 1) + min;
    }

    /**
     * Converts a Movie ArrayList to a JSON string
     *
     * @param movies An ArrayList of movie
     * @return A JSON string
     */
    private static String buildMovieJSONString(ArrayList<Movie> movies) {
        // '[' is added to start a JSON array
        StringBuilder movieString = new StringBuilder("[");

        for (Movie movie : movies) {
            movieString.append(movie.toJSONString());
            // Comma is added to denote a new object
            movieString.append(",\n");
        }
        // Removes the last comma and new line in order to create a proper JSON Array
        movieString.deleteCharAt(movieString.length() - 1);
        movieString.deleteCharAt(movieString.length() - 1);
        // ']' is added to close the JSON array
        movieString.append("]");

        return movieString.toString();
    }


    /**
     * Converts a movie JSON String back to a Movie object
     *
     * @param jsonStringToConvert Movie JSON String
     * @return Movie object
     */
    //This is added to avoid the warnings related to similar SQL code
    @SuppressWarnings("Duplicates")
    private static Movie convertJSONStringToMovie(String jsonStringToConvert) {
        JSONObject movieJSON = new JSONObject(jsonStringToConvert);

        String title = movieJSON.getString("title");
        String genre = movieJSON.getString("genre");
        String director = movieJSON.getString("director");
        String runtime = movieJSON.getString("runtime");
        String plot = movieJSON.getString("plot");
        String location = movieJSON.getString("location");
        String poster = movieJSON.getString("poster");
        String rating = movieJSON.getString("rating");
        String format = movieJSON.getString("format");
        String year = movieJSON.getString("year");
        String starring = movieJSON.getString("staring");
        int copies = movieJSON.getInt("copies");
        String barcode = movieJSON.getString("barcode");
        String userRating = movieJSON.getString("user-rating");

        return new Movie(0, title, genre, director, runtime, plot, location, poster, rating,
                format, year, starring, copies, barcode, userRating);
    }

    /**
     * Taken from - https://www.stubbornjava.com/posts/hashing-passwords-in-java-with-bcrypt
     *
     * @param password Raw password to hash
     * @return Hashed password
     */
    private static String hash(String password) {
        //Applies 12 rounds of salting to password
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    /**
     * Taken from - https://www.stubbornjava.com/posts/hashing-passwords-in-java-with-bcrypt
     *
     * @param password Raw password to compare
     * @param hash     The hash taken from the database
     * @return True if the hashes match
     */
    private static boolean verifyHash(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }

    static void writeToLogFile(String stringToWrite) {
        //Taken from - https://stackoverflow.com/questions/4614227/how-to-add-a-new-line-of-text-to-an-existing-file-in-java

        System.out.println(stringToWrite);

        try {
            Date date = new Date();
            BufferedWriter log = new BufferedWriter(new FileWriter("log.txt", true));

            log.write(date + " " + stringToWrite + "\n");
            log.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void writeToErrorLogFile(String stringToWrite) {
        //Taken from - https://stackoverflow.com/questions/4614227/how-to-add-a-new-line-of-text-to-an-existing-file-in-java

        writeToLogFile(MovieServiceDetails.ANSI_RED + stringToWrite + MovieServiceDetails.ANSI_RESET);

        try {
            Date date = new Date();
            BufferedWriter log = new BufferedWriter(new FileWriter("error.txt", true));

            log.write(date + " " + stringToWrite + "\n\n");
            log.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
