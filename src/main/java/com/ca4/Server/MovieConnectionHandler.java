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

public class MovieConnectionHandler implements Runnable {
    private Socket clientSocket;
    //Crate a list to store the clients that haven't been processed yet
    private static List pool = new LinkedList();

    MovieConnectionHandler() {
    }

    static void processRequest(Socket incomingClient) {
        synchronized (pool) {
            pool.add(pool.size(), incomingClient);

            //Notify all waiting threads that there is a new client to be serviced
            pool.notifyAll();
        }
    }

    @Override
    public void run() {
        while (true) {
            //Lock the pool, if another thread has the lock then we will have to wait
            synchronized (pool) {
                //While the pool is empty, wait
                while (pool.isEmpty()) {
                    try {
                        //Wait until another client is added to the pool
                        pool.wait();
                    } catch (InterruptedException e) {
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

    private void handleConnection() {
        try {
            OutputStream outputToSocket = clientSocket.getOutputStream();
            InputStream inputFromSocket = clientSocket.getInputStream();

            PrintWriter output = new PrintWriter(outputToSocket, true);
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputFromSocket));

            String line = null;
            while ((line = streamReader.readLine()) != null) {
                String response = processCommand(line);

                output.println(response);
                System.out.println("*****START RESPONSE*****");
                System.out.println(response);
                System.out.println("*****END RESPONSE*****");
            }

            output.close();
            streamReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            writeToLogFile(e.getMessage());
            writeToErrorLogFile(e.getMessage());
        }
    }

    private String processCommand(String commandToProcess) {
        String response = "";

        //While the client doesn't want to end the session
        if (!commandToProcess.equals(MovieServiceDetails.CLOSE_CONNECTION)) {
            System.out.println("Received a message: " + commandToProcess);
            writeToLogFile("Received a message: " + commandToProcess);

            //Tokenize the input
            String[] components = commandToProcess.split(MovieServiceDetails.BREAKING_CHARACTER);

            //Process the information
            switch (components[0]) {
                case MovieServiceDetails.LOGIN:
                    if (components.length > 2) {
                        response = MovieRequestHandler.loginUser(components[1], components[2]);
                    } else {
                        response = MovieServiceDetails.FAIL;
                    }
                    break;
                case MovieServiceDetails.REGISTER:
                    if (components.length > 2) {
                        response = registerUser(components[1], components[2]);
                    } else {
                        response = MovieServiceDetails.FAIL;
                    }
                    break;
                case MovieServiceDetails.SEARCH_MOVIE_TITLE:
                    if (components.length > 1) {
                        response = searchForMovieByTitle(components[1]);
                    } else {
                        response = MovieServiceDetails.FAIL;
                    }
                    break;
                case MovieServiceDetails.SEARCH_MOVIE_DIRECTOR:
                    if (components.length > 1) {
                        response = searchForMovieByDirector(components[1]);
                    } else {
                        response = MovieServiceDetails.FAIL;
                    }
                    break;
                case MovieServiceDetails.SEARCH_MOVIE_GENRE:
                    if (components.length > 1) {
                        response = searchForMovieByGenre(components[1]);
                    } else {
                        response = MovieServiceDetails.FAIL;
                    }
                    break;
                case MovieServiceDetails.ADD_MOVIE:
                    if (components.length > 1) {
                        response = addMovie(components[1]);
                    } else {
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
}