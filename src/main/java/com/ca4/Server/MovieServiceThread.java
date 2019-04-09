package com.ca4.Server;

import com.ca4.Core.MovieServiceDetails;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MovieServiceThread implements Runnable
{
    private Thread thread;
    private Socket dataSocket;
    private Scanner input;
    private PrintWriter output;
    private int threadNumber;

    public MovieServiceThread(ThreadGroup group, String threadName, Socket dataSocket, int threadNumber)
    {
        thread              = new Thread(group, threadName);
        this.dataSocket     = dataSocket;
        this.threadNumber   = threadNumber;

        try
        {
            input = new Scanner(new InputStreamReader(this.dataSocket.getInputStream()));
            output = new PrintWriter(this.dataSocket.getOutputStream(), true);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        //Setup some variables for communication
        String incomingMessage = "";
        String response;

        try
        {
            //While the client doesn't want to end the session
            while (!incomingMessage.equals(MovieServiceDetails.CLOSE_CONNECTION))
            {
                response = null;
                //Take the input information from the client
                incomingMessage = input.nextLine();
                System.out.println(threadNumber + ": Received a message: " + incomingMessage);

                //Tokenize the input
                String[] components = incomingMessage.split(MovieServiceDetails.BREAKING_CHARACTER);

                //Process the information
                if (components[0].equals(MovieServiceDetails.LOGIN))
                {
                    response = "NOT IMPLEMENTED";
                }
                else if (components[0].equals(MovieServiceDetails.REGISTER))
                {
                    response = "NOT IMPLEMENTED";
                }
                else if (components[0].equals(MovieServiceDetails.SEARCH_MOVIE_TITLE))
                {
                    response = "NOT IMPLEMENTED";
                }
                else if (components[0].equals(MovieServiceDetails.SEARCH_MOVIE_DIRECTOR))
                {
                    response = "NOT IMPLEMENTED";
                }
                else if (components[0].equals(MovieServiceDetails.ADD_MOVIE))
                {
                    response = "NOT IMPLEMENTED";
                }
                else if (components[0].equals(MovieServiceDetails.REMOVE_MOVIE))
                {
                    response = "NOT IMPLEMENTED";
                }
                else if (components[0].equals(MovieServiceDetails.UPDATE_MOVIE))
                {
                    response = "NOT IMPLEMENTED";
                }
                else if (components[0].equals(MovieServiceDetails.WATCH_MOVIE))
                {
                    response = "NOT IMPLEMENTED";
                }
                else if (components[0].equals(MovieServiceDetails.RECOMMEND_MOVIE))
                {
                    response = "NOT IMPLEMENTED";
                }
                else
                {
                    response = MovieServiceDetails.UNRECOGNISED_COMMAND;
                }

                output.println(response);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                dataSocket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
