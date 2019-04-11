package com.ca4.Client;

import com.ca4.Core.MovieServiceDetails;
import org.json.JSONObject;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MovieClient
{
    private static Socket client;
    private static OutputStream outputToSocket;
    private static InputStream inputFromSocket;
    private static PrintWriter streamWriter;
    private static BufferedReader streamReader;
    private static Scanner socketInput;
    private static StringBuilder stringBuilder;

    public static void main(String[] args)
    {
        boolean continueRunning = true;
        String option;
        boolean loggedIn = true;

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
                            String movieName = ClientInteractor.getMovieName();
                            sendStringMessage(MovieServiceDetails.SEARCH_MOVIE_TITLE, movieName);
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
        }finally {
            try {
                if (client != null) {
                    client.close();
                }
            }catch (IOException io){
                io.printStackTrace();
            }
        }
    }

    public static void sendStringMessage(String messageCode, String message){
        try{
            inputFromSocket = client.getInputStream();
            //streamReader = new BufferedReader(new InputStreamReader(inputFromSocket));

            message = messageCode + MovieServiceDetails.BREAKING_CHARACTER + message;
            streamWriter = new PrintWriter(client.getOutputStream());
            streamWriter.println(message);
            streamWriter.flush();
            recieveJSONMessage();
        }catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static void recieveJSONMessage(){
            Scanner input = new Scanner(new InputStreamReader(inputFromSocket));

            String currentline = "";
            stringBuilder = new StringBuilder();

            while(!(currentline.contains("}"))){

                currentline += input.nextLine();
                //System.out.println(currentline);
            }

            JSONObject jsObj = new JSONObject(currentline);

            System.out.println(jsObj.toString(4));
        }
}
