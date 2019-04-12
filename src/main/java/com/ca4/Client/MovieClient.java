package com.ca4.Client;

import com.ca4.Core.MovieServiceDetails;
import com.ca4.DTO.Movie;
import com.ca4.DTO.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class MovieClient
{
    private static Socket client;
    private static InputStream inputFromSocket;
    private static PrintWriter streamWriter;
    private static User localUser;
    private static boolean loggedIn = true;

    private static JSONArray jsMovieArray;
    private static ArrayList<Movie> movieArray;
    private static JSONObject jsonMovie;

    public static void main(String[] args)
    {
        boolean continueRunning = true;
        String option;


        try{
            InetAddress serverIP = InetAddress.getByName(MovieServiceDetails.SERVER_IP);

            client = new Socket(serverIP, MovieServiceDetails.SERVER_PORT);

            while(continueRunning){
                if(!loggedIn){
                    option = ClientInteractor.getMenuOption_NotLoggedIn();

                    switch (option){
                        case "A":
                            String[] loginDetails = ClientInteractor.loginRegister();
                            sendUsersDetails(MovieServiceDetails.LOGIN, loginDetails);
                            break;
                        case "B":
                            String[] userDetails = ClientInteractor.loginRegister();
                            sendUsersDetails(MovieServiceDetails.REGISTER, userDetails);
                            break;
                        case "C":
                            closeConnectionToServer();
                            continueRunning = false;
                            break;
                    }


                }else if(loggedIn){
                    option = ClientInteractor.getMenuOption_LoggedIn();

                    switch (option){
                        case "A":
                            System.out.println("What is the name of the movie you are looking for: ");
                            String[] movieName = {ClientInteractor.getMovieName()};
                            sendStringArray(MovieServiceDetails.SEARCH_MOVIE_TITLE, movieName);
                            jsonMovie = receiveJSONObject();
                            Movie movie = convertJSONStringToMovie(jsonMovie.toString());
                            System.out.println(movie.toString());
                            break;
                        case "B":
                            String[] movieDirector = {ClientInteractor.getMovieDirector()};
                            sendStringArray(MovieServiceDetails.SEARCH_MOVIE_DIRECTOR, movieDirector);
                            jsMovieArray = receiveJSONArray();
                            movieArray = splitJSONMovieArray(jsMovieArray);
                            printMovieArray(movieArray);
                            break;
                        case "C":
                            String[] movieGenre = {ClientInteractor.getMovieGenre()};
                            sendStringArray(MovieServiceDetails.SEARCH_MOVIE_GENRE, movieGenre);
                            jsMovieArray = receiveJSONArray();
                            movieArray = splitJSONMovieArray(jsMovieArray);
                            printMovieArray(movieArray);
                            break;
                        case "D":
                            //TODO add update movie logic
                            break;
                        case "E":
                            Movie movieToAdd = ClientInteractor.getMovieDetails();
                            sendMovieJSON(movieToAdd);
                            break;
                        case "F":
                            System.out.println("What is the name of the movie you are looking to delete: ");
                            String[] movieToDelete = {ClientInteractor.getMovieName()};
                            sendStringArray(MovieServiceDetails.SEARCH_MOVIE_TITLE, movieToDelete);
                            jsonMovie = receiveJSONObject();
                            Movie movieForDelete = convertJSONStringToMovie(jsonMovie.toString());
                            if(ClientInteractor.getYesorNofromuser()){
                                sendMovieToDelete(movieForDelete.getId());
                            }
                            break;
                        case "G":
                            //TODO add update user logic
                            break;
                        case "H":
                            //TODO add delete user logic
                            break;
                        case "I":
                            closeConnectionToServer();
                            loggedIn = false;
                            continueRunning = false;
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

    public static void sendUsersDetails(String messageCode, String[] details){
        try{
            String messageToSend;
            inputFromSocket = client.getInputStream();

            messageToSend = messageCode;

            for(int i = 0; i < details.length; ++i){
                messageToSend += MovieServiceDetails.BREAKING_CHARACTER + details[i];
            }

            streamWriter = new PrintWriter(client.getOutputStream());
            streamWriter.println(messageToSend);
            streamWriter.flush();
            createNewUser(details);
        }catch (IOException io){
            io.printStackTrace();
        }
    }

    public static void sendStringArray(String messageCode, String[] message){
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
        }catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static void createNewUser(String[] userDetails){
        Scanner input = new Scanner(new InputStreamReader(inputFromSocket));

        String currentline = "";

        currentline = input.nextLine();

        String[] serverAnswer = currentline.split(MovieServiceDetails.BREAKING_CHARACTER);

        if(serverAnswer[0].equals(MovieServiceDetails.LOGIN_SUCCESS)){
            localUser = new User(Integer.parseInt(serverAnswer[1]), userDetails[0], userDetails[1]);
            localUser.toString();
            loggedIn = true;
        }else if(serverAnswer[0].equals(MovieServiceDetails.REGISTER_SUCCESS)){
            sendUsersDetails(MovieServiceDetails.LOGIN, userDetails);
        }else{
            System.out.println("Cannot find user on server.");
        }
    }

    public static JSONObject receiveJSONObject(){
        Scanner input = new Scanner(new InputStreamReader(inputFromSocket));

        String currentline = "";

        while(!(currentline.contains(MovieServiceDetails.JSONOBJECT_ENDINGCHAR))){
            currentline += input.nextLine();
        }
            JSONObject jsObj = new JSONObject(currentline);

        return jsObj;
    }

    public static JSONArray receiveJSONArray(){
        Scanner input = new Scanner(new InputStreamReader(inputFromSocket));

        String currentline = "";

        while(!(currentline.contains(MovieServiceDetails.JSONARRAY_ENDINGCHAR))){
            currentline += input.nextLine();
        }
        JSONArray jsArray = new JSONArray(currentline);

        return jsArray;
    }

    public static void closeConnectionToServer(){
        try{
            inputFromSocket = client.getInputStream();

            streamWriter = new PrintWriter(client.getOutputStream());
            streamWriter.println(MovieServiceDetails.CLOSE_CONNECTION);
            streamWriter.flush();
            recceiveCommandCodeFromServer();
        }catch (IOException io){
            io.printStackTrace();
        }
    }

    public static void recceiveCommandCodeFromServer(){
        Scanner input = new Scanner(new InputStreamReader(inputFromSocket));

        String answer;

        answer = input.nextLine();

        System.out.println(answer);
    }

    public static void sendMovieJSON(Movie movieToSend){
        try{
            String sendableMovie = MovieServiceDetails.ADD_MOVIE + MovieServiceDetails.BREAKING_CHARACTER
                    + movieToSend.toJSONString();

            streamWriter = new PrintWriter(client.getOutputStream());
            streamWriter.println(sendableMovie);
            streamWriter.flush();
            recceiveCommandCodeFromServer();
        }catch (IOException io){
            io.printStackTrace();
        }
    }

    /*
    src: https://stackoverflow.com/questions/33754101/split-json-object-from-json-array-in-java
     */
    public static ArrayList<Movie> splitJSONMovieArray(JSONArray movieArray){
        ArrayList<Movie> movies = new ArrayList<>();
        JSONObject jsonObj;

        for(int i = 0; i < movieArray.length(); ++i){
            jsonObj = movieArray.getJSONObject(i);
            movies.add(convertJSONStringToMovie(jsonObj.toString()));
        }

        return movies;
    }

    /*
    taken from camerons movieservicethread
     */
    public static Movie convertJSONStringToMovie(String jsonStringToConvert)
    {
        JSONObject movieJSON = new JSONObject(jsonStringToConvert);

        String title = movieJSON.getString("title");
        String genre = movieJSON.getString("genre");
        String director = movieJSON.getString("director");
        String runtime = movieJSON.getString("runtime");
        String plot = movieJSON.getString("plot");
        String location = movieJSON.getString("location");
        //String poster = movieJSON.getString("poster");
        String rating = movieJSON.getString("rating");
        String format = movieJSON.getString("format");
        String year = movieJSON.getString("year");
        String starring  = movieJSON.getString("staring");
        int copies = movieJSON.getInt("copies");
        //String barcode = movieJSON.getString("barcode");
        //String userRating = movieJSON.getString("user-rating");

        return new Movie(0, title, genre, director, runtime, plot, location, "", rating,
                format, year, starring, copies, "", "");
    }

    public static void printMovieArray(ArrayList<Movie> movies){
        for(int i = 0;i < movies.size(); ++i){
            System.out.println(movies.get(i).toString());
        }
    }

    public static void sendMovieToDelete(int id){
        String message = MovieServiceDetails.REMOVE_MOVIE + MovieServiceDetails.BREAKING_CHARACTER
                + Integer.toString(id);

        try{
            inputFromSocket = client.getInputStream();

            streamWriter = new PrintWriter(client.getOutputStream());
            streamWriter.println(message);
            streamWriter.flush();
            recceiveCommandCodeFromServer();
        }catch (IOException io){
            io.printStackTrace();
        }
    }
}
