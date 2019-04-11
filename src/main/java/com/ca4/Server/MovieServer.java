package com.ca4.Server;

import com.ca4.Core.MovieServiceDetails;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MovieServer
{
    public static void main(String[] args)
    {
        try
        {
            boolean continueRunning = true;
            int threadCount = 0;

            //Create the listening socket
            ServerSocket listenSocket = new ServerSocket(MovieServiceDetails.SERVER_PORT);

            //Create a Thread Group to store all clients together
            ThreadGroup group = new ThreadGroup("Client Threads");
            //Place more emphasis on accepting threads than on processing threads
            group.setMaxPriority(Thread.currentThread().getPriority() - 1);

            System.out.println("Ready to accept connections");

            while (continueRunning)
            {
                //Wait for incoming connections
                Socket dataSocket = listenSocket.accept();
                threadCount ++;

                MovieServiceThread serviceThread = new MovieServiceThread(group, dataSocket.getInetAddress().toString(),
                                                                            dataSocket, threadCount);
                serviceThread.run();
            }
            listenSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
