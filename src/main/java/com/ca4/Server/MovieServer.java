package com.ca4.Server;

import com.ca4.Core.MovieServiceDetails;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MovieServer
{
    private ServerSocket listenSocket;
    private int listenPort;
    private int maxConnections;

    public MovieServer(int listenPort, int maxConnections)
    {
        this.listenPort = listenPort;
        this.maxConnections = maxConnections;
    }

    public static void main(String[] args)
    {
        //Set up the server
        MovieServer server = new MovieServer(MovieServiceDetails.SERVER_PORT, MovieServiceDetails.MAX_CONNECTIONS);

        //Set up the handler threads in a pool
        server.setUpHandlers();
        //Start accepting connections
        server.acceptConnections();
    }

    /*
    Set up the connection pool
    Create all the threads that will be reused during the lifetime of the server
     */
    private void setUpHandlers()
    {
        for (int i = 0; i < this.maxConnections; ++i)
        {
            MovieConnectionHandler currentHandler = new MovieConnectionHandler();
            Thread t = new Thread(currentHandler);
            t.start();
        }
    }

    private void acceptConnections()
    {
        try
        {
            //Open a listening socket
            //Backlog is the length of the queue before it stops listening
            //Allows up to 5 connections to wait for connection
            ServerSocket server = new ServerSocket(this.listenPort, this.maxConnections);
            Socket incomingConnection = null;

            while (true)
            {
                System.out.println("Accepting a connection....");
                //Accept the next client
                incomingConnection = server.accept();
                /*
                Handle the client
                Either with one of our service threads or else out in a queue
                 */
                handleConnection(incomingConnection);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void handleConnection(Socket connectionToHandle)
    {
        //Process the client's request to connect to the server
        MovieConnectionHandler.processRequest(connectionToHandle);
    }
}
