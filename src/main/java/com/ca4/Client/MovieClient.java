package com.ca4.Client;

import com.ca4.Core.MovieServiceDetails;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MovieClient
{
    public static void main(String[] args)
    {
        Socket client;
        OutputStream outputToSocket;
        InputStream inputFromSocket;
        PrintWriter streamWriter;
        BufferedReader streamReader;
        boolean continueRunning = true;
        String option;
        boolean loggedIn = false;

        try{
            InetAddress serverIP = InetAddress.getByName(MovieServiceDetails.SERVER_IP);

            client = new Socket(serverIP, MovieServiceDetails.SERVER_PORT);

            while(continueRunning){
                if(!loggedIn){
                    option = ClientInteractor.getMenuOption_NotLoggedIn();
                }else if(loggedIn){
                    option = ClientInteractor.getMenuOption_LoggedIn();

                    switch (option){
                        case "A":
                            ClientInteractor.getMovieName();
                            break;
                        case "B":
                            ClientInteractor.getMovieDirector();
                            break;
                        case "C":
                            ClientInteractor.getMovieGenre();
                            break;
                        case "D":
                            //TODO add update movie logic
                            break;
                        case "E":
                            //TODO add insert movies logic
                            break;
                        case "F":
                            //TODO add delete movie logic
                            break;
                        case "G":
                            //TODO add update user logic
                            break;
                        case "H":
                            //TODO add delete user logic
                            break;
                    }
                }
            }
        }catch (UnknownHostException ue){
            ue.printStackTrace();
        }catch (IOException io){
            io.printStackTrace();
        }
    }
}
