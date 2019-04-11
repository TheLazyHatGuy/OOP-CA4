package com.ca4.Client;

import com.ca4.Core.MovieServiceDetails;
import org.json.JSONArray;
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
                            String[] movieName = {ClientInteractor.getMovieName()};
                            sendStringArray(MovieServiceDetails.SEARCH_MOVIE_TITLE, movieName,
                                    MovieServiceDetails.JSONOBJECT_ENDINGCHAR);
                            break;
                        case "B":
                            String[] movieDirector = {ClientInteractor.getMovieDirector()};
                            sendStringArray(MovieServiceDetails.SEARCH_MOVIE_DIRECTOR, movieDirector,
                                    MovieServiceDetails.JSONARRAY_ENDINGCHAR);
                            break;
                        case "C":
                            String[] movieGenre = {ClientInteractor.getMovieGenre()};
                            //sendStringArray();
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


    public static void sendStringArray(String messageCode, String[] message, CharSequence endingChar){
        try{
            String messageToSend;
            inputFromSocket = client.getInputStream();

            messageToSend = messageCode;

            for(int i = 0; i < message.length; ++i){
                messageToSend += MovieServiceDetails.BREAKING_CHARACTER + message[i];
            }

            streamWriter = new PrintWriter(client.getOutputStream());
            streamWriter.println(messageToSend);
            streamWriter.flush();
            receiveJSONObject(endingChar);
        }catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static void receiveJSONObject(CharSequence endingChar){
            Scanner input = new Scanner(new InputStreamReader(inputFromSocket));

            String currentline = "";
            stringBuilder = new StringBuilder();

            while(!(currentline.contains(endingChar))){
                currentline += input.nextLine();
            }

            if(endingChar.equals(MovieServiceDetails.JSONARRAY_ENDINGCHAR)){
                JSONArray jsArray = new JSONArray(currentline);
                System.out.println(jsArray.toString(4));
            }else if(endingChar.equals(MovieServiceDetails.JSONOBJECT_ENDINGCHAR)){
                JSONObject jsObj = new JSONObject(currentline);
                System.out.println(jsObj.toString(4));
            }
        }
}
