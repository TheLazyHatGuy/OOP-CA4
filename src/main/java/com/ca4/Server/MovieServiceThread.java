package com.ca4.Server;

import com.ca4.Core.MovieServiceDetails;
import com.ca4.DAO.MySQLUserDAO;
import com.ca4.DAO.UserDAOInterface;
import com.ca4.DTO.User;
import com.ca4.Exceptions.DAOException;
import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.net.Socket;
import java.util.Date;
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
                writeToLogFile(threadNumber + ": Received a message: " + incomingMessage);

                //Tokenize the input
                String[] components = incomingMessage.split(MovieServiceDetails.BREAKING_CHARACTER);

                //Process the information
                if (components[0].equals(MovieServiceDetails.LOGIN))
                {
                    if (components.length > 2)
                    {
                        response = loginUser(components[1], components[2]);
                    }
                    else
                    {
                        response = MovieServiceDetails.FAIL;
                    }
                }
                else if (components[0].equals(MovieServiceDetails.REGISTER))
                {
                    if (components.length > 2)
                    {
                        response = registerUser(components[1], components[2]);
                    }
                    else
                    {
                        response = MovieServiceDetails.FAIL;
                    }
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
                else if (components[0].equals(MovieServiceDetails.CLOSE_CONNECTION))
                {
                    response = MovieServiceDetails.CLOSE_CONNECTION;
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
            BufferedWriter log = new BufferedWriter(new FileWriter("log.txt", true));

            log.write(stringToWrite);
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
