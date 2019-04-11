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
import java.net.Socket;
import java.util.*;

public class MovieConnectionHandler implements Runnable
{
    private Socket clientSocket;
    //Crate a list to store the clients that haven't been processed yet
    private static List pool = new LinkedList();

    MovieConnectionHandler() {}

    static void processRequest(Socket incomingClient)
    {
        synchronized (pool)
        {
            pool.add(pool.size(), incomingClient);

            //Notify all waiting threads that there is a new client to be serviced
            pool.notifyAll();
        }
    }

    @Override
    public void run()
    {
        while (true)
        {
            //Lock the pool, if another thread has the lock then we will have to wait
            synchronized (pool)
            {
                //While the pool is empty, wait
                while (pool.isEmpty())
                {
                    try
                    {
                        //Wait until another client is added to the pool
                        pool.wait();
                    }
                    catch (InterruptedException e)
                    {
                        return;
                    }
                }
                //At this point we have the lock on the pool and it is not empty
                clientSocket = (Socket) pool.remove(0);
                //Interact with the client
                handleConnection();
            }
        }
    }

    private void handleConnection()
    {
        try
        {
            OutputStream outputToSocket = clientSocket.getOutputStream();
            InputStream inputFromSocket = clientSocket.getInputStream();

            PrintWriter output = new PrintWriter(outputToSocket, true);
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputFromSocket));

            String line = null;
            while ((line = streamReader.readLine()) != null)
            {
                String response = processCommand(line);

                output.println(response);
                System.out.println("*****START RESPONSE*****");
                System.out.println(response);
                System.out.println("*****END RESPONSE*****");
            }

            output.close();
            streamReader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            writeToLogFile(e.getMessage());
            writeToErrorLogFile(e.getMessage());
        }
    }

    @SuppressWarnings("Duplicates")
    private String processCommand(String commandToProcess)
    {
        String response = "";

        //While the client doesn't want to end the session
        if (!commandToProcess.equals(MovieServiceDetails.CLOSE_CONNECTION))
        {
            System.out.println("Received a message: " + commandToProcess);
            writeToLogFile("Received a message: " + commandToProcess);

            //Tokenize the input
            String[] components = commandToProcess.split(MovieServiceDetails.BREAKING_CHARACTER);

            //Process the information
            switch (components[0])
            {
                case MovieServiceDetails.LOGIN:
                    if (components.length > 2)
                    {
                        response = loginUser(components[1], components[2]);
                    }
                    else
                    {
                        response = MovieServiceDetails.FAIL;
                    }
                    break;
                case MovieServiceDetails.REGISTER:
                    if (components.length > 2)
                    {
                        response = registerUser(components[1], components[2]);
                    }
                    else
                    {
                        response = MovieServiceDetails.FAIL;
                    }
                    break;
                case MovieServiceDetails.SEARCH_MOVIE_TITLE:
                    if (components.length > 1)
                    {
                        response = searchForMovieByTitle(components[1]);
                    }
                    else
                    {
                        response = MovieServiceDetails.FAIL;
                    }
                    break;
                case MovieServiceDetails.SEARCH_MOVIE_DIRECTOR:
                    if (components.length > 1)
                    {
                        response = searchForMovieByDirector(components[1]);
                    }
                    else
                    {
                        response = MovieServiceDetails.FAIL;
                    }
                    break;
                case MovieServiceDetails.SEARCH_MOVIE_GENRE:
                    if (components.length > 1)
                    {
                        response = searchForMovieByGenre(components[1]);
                    }
                    else
                    {
                        response = MovieServiceDetails.FAIL;
                    }
                    break;
                case MovieServiceDetails.ADD_MOVIE:
                    if (components.length > 1)
                    {
                        response = addMovie(components[1]);
                    }
                    else
                    {
                        response = MovieServiceDetails.FAIL;
                    }
                    break;
                case MovieServiceDetails.REMOVE_MOVIE:
                    response = "NOT IMPLEMENTED";
                    break;
                case MovieServiceDetails.UPDATE_MOVIE:
                    response = "NOT IMPLEMENTED";
                    break;
                case MovieServiceDetails.WATCH_MOVIE:
                    response = "NOT IMPLEMENTED";
                    break;
                case MovieServiceDetails.RECOMMEND_MOVIE:
                    response = "NOT IMPLEMENTED";
                    break;
                case MovieServiceDetails.CLOSE_CONNECTION:
                    response = MovieServiceDetails.CLOSE_CONNECTION;
                    break;
                default:
                    response = MovieServiceDetails.UNRECOGNISED_COMMAND;
                    break;
            }
        }
        return response;
    }

    private String registerUser(String email, String password)
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

    private String loginUser(String email, String password)
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

    private String searchForMovieByTitle(String searchString)
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

    private String searchForMovieByDirector(String searchString)
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

    private String searchForMovieByGenre(String searchString)
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
    private String addMovie(String movieJSONString)
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
    private String buildMovieJSONString(ArrayList<Movie> movies)
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
    private Movie convertJSONStringToMovie(String jsonStringToConvert)
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
    private String hash(String password)
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
    private boolean verifyHash(String password, String hash)
    {
        return BCrypt.checkpw(password, hash);
    }

    private static void writeToLogFile(String stringToWrite)
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

    private static void writeToErrorLogFile(String stringToWrite)
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
