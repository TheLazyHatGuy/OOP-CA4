package com.ca4.Server;

import com.ca4.Core.MovieServiceDetails;
import com.ca4.DAO.MovieDAOInterface;
import com.ca4.DAO.MySQLMovieDAO;
import com.ca4.DAO.MySQLUserDAO;
import com.ca4.DAO.UserDAOInterface;
import com.ca4.DTO.Movie;
import com.ca4.DTO.User;
import com.ca4.Exceptions.DAOException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

class MovieRequestHandler
{
    static String registerUser(String email, String password)
    {
        UserDAOInterface userDAO = new MySQLUserDAO();
        String hashedPassword = hash(password);
        String response = MovieServiceDetails.FAIL;

        try
        {
            boolean isRegistered = userDAO.registerUser(email, hashedPassword);

            if (isRegistered)
            {
                //TODO - Redesign protocol with better responses
                response = MovieServiceDetails.REGISTER_SUCCESS;
            }
        }
        catch (DAOException e)
        {
            e.printStackTrace();
            writeToLogFile(e.getMessage());
            writeToErrorLogFile(e.getMessage());
        }

        return response;
    }

    static String loginUser(String email, String password)
    {
        UserDAOInterface userDAO = new MySQLUserDAO();
        String response = MovieServiceDetails.FAIL;

        try
        {
            User toLogin = userDAO.loginUser(email);
            boolean isLoggedIn = verifyHash(password, toLogin.getPassword());

            if (isLoggedIn)
            {
                response = MovieServiceDetails.LOGIN_SUCCESS + MovieServiceDetails.BREAKING_CHARACTER + toLogin.getId();
            }
        }
        catch (DAOException e)
        {
            e.printStackTrace();
            writeToLogFile(e.getMessage());
            writeToErrorLogFile(e.getMessage());
        }

        return response;
    }

    static String searchForMovieByTitle(String searchString)
    {
        MovieDAOInterface movieDAO = new MySQLMovieDAO();
        String response = MovieServiceDetails.FAIL;

        try
        {
            Movie movie = movieDAO.getMovieByName(searchString);
            response = movie.toJSONString();
        }
        catch (DAOException e)
        {
            e.printStackTrace();
            writeToLogFile(e.getMessage());
            writeToErrorLogFile(e.getMessage());
        }

        return response;
    }

    static String searchForMovieByDirector(String searchString)
    {
        MovieDAOInterface movieDAO = new MySQLMovieDAO();
        String response = MovieServiceDetails.FAIL;

        try
        {
            ArrayList<Movie> movies = movieDAO.getMoviesByDirector(searchString);

            if (movies.size() >= 1)
            {
                response = buildMovieJSONString(movies);
            }
        }
        catch (DAOException e)
        {
            e.printStackTrace();
            writeToLogFile(e.getMessage());
            writeToErrorLogFile(e.getMessage());
        }

        return response;
    }

    static String searchForMovieByGenre(String searchString)
    {
        MovieDAOInterface movieDAO = new MySQLMovieDAO();
        String response = MovieServiceDetails.FAIL;

        try
        {
            ArrayList<Movie> movies = movieDAO.getMoviesByGenre(searchString);

            if (movies.size() >= 1)
            {
                response = buildMovieJSONString(movies);
            }
        }
        catch (DAOException e)
        {
            e.printStackTrace();
            writeToLogFile(e.getMessage());
            writeToErrorLogFile(e.getMessage());
        }

        return response;
    }

    /**
     * Converts a movie JSON String to the movie class and adds it to the database
     * @param movieJSONString A full movie JSON String
     * @return Server response message
     */
    static String addMovie(String movieJSONString)
    {
        MovieDAOInterface movieDAO = new MySQLMovieDAO();
        String response = MovieServiceDetails.FAIL;

        try
        {
            Movie movieToAdd = convertJSONStringToMovie(movieJSONString);
            boolean isAdded = movieDAO.addMovieToDatabase(movieToAdd);

            if (isAdded)
            {
                response = MovieServiceDetails.ADD_SUCCESS;
            }
        }
        catch (DAOException e)
        {
            e.printStackTrace();
            writeToLogFile(e.getMessage());
            writeToErrorLogFile(e.getMessage());
        }

        return response;
    }

    /**
     * Converts a Movie ArrayList to a JSON string
     * @param movies An ArrayList of movie
     * @return A JSON string
     */
    private static String buildMovieJSONString(ArrayList<Movie> movies)
    {
        // '[' is added to start a JSON array
        StringBuilder movieString = new StringBuilder("[");

        for (Movie movie : movies)
        {
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
     * @param jsonStringToConvert Movie JSON String
     * @return Movie object
     */
    //This is added to avoid the warnings related to similar SQL code
    @SuppressWarnings("Duplicates")
    private static Movie convertJSONStringToMovie(String jsonStringToConvert)
    {
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
        String starring  = movieJSON.getString("staring");
        int copies = movieJSON.getInt("copies");
        String barcode = movieJSON.getString("barcode");
        String userRating = movieJSON.getString("user-rating");

        return new Movie(0, title, genre, director, runtime, plot, location, poster, rating,
                format, year, starring, copies, barcode, userRating);
    }

    /**
     * Taken from - https://www.stubbornjava.com/posts/hashing-passwords-in-java-with-bcrypt
     * @param password Raw password to hash
     * @return Hashed password
     */
    private static String hash(String password)
    {
        //Applies 12 rounds of salting to password
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    /**
     * Taken from - https://www.stubbornjava.com/posts/hashing-passwords-in-java-with-bcrypt
     * @param password Raw password to compare
     * @param hash The hash taken from the database
     * @return True if the hashes match
     */
    private static boolean verifyHash(String password, String hash)
    {
        return BCrypt.checkpw(password, hash);
    }

    static void writeToLogFile(String stringToWrite)
    {
        //Taken from - https://stackoverflow.com/questions/4614227/how-to-add-a-new-line-of-text-to-an-existing-file-in-java
        try
        {
            Date date = new Date();
            BufferedWriter log = new BufferedWriter(new FileWriter("log.txt", true));

            log.write(date + " " + stringToWrite + "\n");
            log.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    static void writeToErrorLogFile(String stringToWrite)
    {
        //Taken from - https://stackoverflow.com/questions/4614227/how-to-add-a-new-line-of-text-to-an-existing-file-in-java
        try
        {
            Date date = new Date();
            BufferedWriter log = new BufferedWriter(new FileWriter("error.txt", true));

            log.write(date + " " + stringToWrite + "\n\n");
            log.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
