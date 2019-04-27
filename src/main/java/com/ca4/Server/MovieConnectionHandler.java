package com.ca4.Server;

import com.ca4.Core.MovieServiceDetails;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class MovieConnectionHandler implements Runnable {
    private Socket clientSocket;
    //Crate a list to store the clients that haven't been processed yet
    private static List pool = new LinkedList();

    MovieConnectionHandler() {}

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
                        System.out.println("Waiting for connections");
                        pool.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                //At this point we have the lock on the pool and it is not empty
                System.out.println("Setting up a socket for the client");
                clientSocket = (Socket) pool.remove(0);
            }
            //Interact with the client
            handleConnection();
        }
    }

    private void handleConnection() {
        try {
            OutputStream outputToSocket = clientSocket.getOutputStream();
            InputStream inputFromSocket = clientSocket.getInputStream();

            PrintWriter output = new PrintWriter(outputToSocket, true);
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputFromSocket));

            String line = null;
            while ((line = streamReader.readLine()) != null && !line.equals(MovieServiceDetails.CLOSE_CONNECTION)) {
                String response = processCommand(line);

                output.println(response);
                System.out.println("*****START RESPONSE FOR " + clientSocket.getInetAddress() + "*****");
                System.out.println(response);
                System.out.println("*****END RESPONSE FOR " + clientSocket.getInetAddress() + "*****");
            }

            output.close();
            streamReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            MovieRequestHandler.writeToLogFile(e.getMessage());
            MovieRequestHandler.writeToErrorLogFile(e.getMessage());
        }
    }

    private String processCommand(String commandToProcess) {
        String response = "";

        //While the client doesn't want to end the session
        if (!commandToProcess.equals(MovieServiceDetails.CLOSE_CONNECTION)) {
            MovieRequestHandler.writeToLogFile("Received a message: " + commandToProcess);

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
                        response = MovieRequestHandler.registerUser(components[1], components[2]);
                    } else {
                        response = MovieServiceDetails.FAIL;
                    }
                    break;
                case MovieServiceDetails.SEARCH_MOVIE_TITLE:
                    if (components.length > 1) {
                        response = MovieRequestHandler.searchForMovieByTitle(components[1]);
                    } else {
                        response = MovieServiceDetails.FAIL;
                    }
                    break;
                case MovieServiceDetails.SEARCH_MOVIE_DIRECTOR:
                    if (components.length > 1) {
                        response = MovieRequestHandler.searchForMovieByDirector(components[1]);
                    } else {
                        response = MovieServiceDetails.FAIL;
                    }
                    break;
                case MovieServiceDetails.SEARCH_MOVIE_GENRE:
                    if (components.length > 1) {
                        response = MovieRequestHandler.searchForMovieByGenre(components[1]);
                    } else {
                        response = MovieServiceDetails.FAIL;
                    }
                    break;
                case MovieServiceDetails.ADD_MOVIE:
                    if (components.length > 1) {
                        response = MovieRequestHandler.addMovie(components[1]);
                    } else {
                        response = MovieServiceDetails.FAIL;
                    }
                    break;
                case MovieServiceDetails.REMOVE_MOVIE:
                    if (components.length > 1) {
                        int movieID = Integer.parseInt(components[1]);

                        if (movieID >= 1) {
                            response = MovieRequestHandler.removeMovie(movieID);
                        }
                    } else {
                        response = MovieServiceDetails.FAIL;
                    }
                    break;
                case MovieServiceDetails.UPDATE_MOVIE:
                    if (components.length > 1) {
                        response = MovieRequestHandler.updateMovie(components[1]);
                    } else {
                        response = MovieServiceDetails.FAIL;
                    }
                    break;
                case MovieServiceDetails.WATCH_MOVIE:
                    if (components.length > 2) {
                        int userID = Integer.parseInt(components[1]);
                        int movieID = Integer.parseInt(components[2]);

                        if (userID >= 1 && movieID >= 1) {
                            response = MovieRequestHandler.watchMovie(userID, movieID);
                        }
                    } else {
                        response = MovieServiceDetails.FAIL;
                    }
                    break;
                case MovieServiceDetails.RECOMMEND_MOVIE:
                    if (components.length > 1) {
                        int userID = Integer.parseInt(components[1]);

                        if (userID >= 1) {
                            response = MovieRequestHandler.recommendMovie(userID);
                        }
                    } else {
                        response = MovieServiceDetails.FAIL;
                    }
                    break;
                default:
                    response = MovieServiceDetails.UNRECOGNISED_COMMAND;
                    break;
            }
        }
        return response;
    }
}