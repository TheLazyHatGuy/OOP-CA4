package com.ca4.Client;

import com.ca4.Core.MovieServiceDetails;
import com.ca4.DTO.Movie;
import com.ca4.DTO.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.net.Socket;

public class MessageSender {
    private static InputStream inputFromSocket;
    private static PrintWriter streamWriter;

    /**
     * sends an array of strings to the server
     *
     * used for search movies methods
     * @param messageCode
     * @param message
     */
    public static void sendStringArray(Socket clientSocket, String messageCode, String[] message){
        try{
            String messageToSend;
            inputFromSocket = clientSocket.getInputStream();

            messageToSend = messageCode;

            for(int i = 0; i < message.length; ++i){
                messageToSend += MovieServiceDetails.BREAKING_CHARACTER + message[i];
            }

            streamWriter = new PrintWriter(clientSocket.getOutputStream());
            streamWriter.println(messageToSend);
            streamWriter.flush();
        }catch (IOException io) {
            System.out.println("Unable to send message to server\n");
        }
    }

    /**
     * creates a new user object based on the id returned by the server and the users inputted details
     * if the user just registered it sends the details again for a log in
     * also sets the state of the user to logged in
     * @param userDetails
     */
    public static boolean createNewUser(String[] userDetails){
        Scanner input = new Scanner(new InputStreamReader(inputFromSocket));

        String currentline = "";

        currentline = input.nextLine();

        String[] serverAnswer = currentline.split(MovieServiceDetails.BREAKING_CHARACTER);

        if(!checkResponseForErrors(currentline) && !checkforLoginRegisterErrors(currentline)){
            MovieClient.localUser = new User(Integer.parseInt(serverAnswer[1]), userDetails[0], userDetails[1]);
            MovieClient.localUser.toString();
            return true;
        }else if(serverAnswer[0] == MovieServiceDetails.FAIL || serverAnswer[0] == MovieServiceDetails.UNRECOGNISED_COMMAND){
            System.out.println(MovieServiceDetails.ANSI_RED + "There was an error with processing that command"
                    + MovieServiceDetails.ANSI_RESET);
        }else if(serverAnswer[0] == MovieServiceDetails.LOGIN_NOT_REGISTERED){
            System.out.println("Sorry that user is not registered");
        }else if(serverAnswer[0] == MovieServiceDetails.LOGIN_WRONG_INFO){
            System.out.println("Email or password are incorrect");
        }else if(serverAnswer[0] == MovieServiceDetails.REGISTER_ALREADY_REGISTERED){
            System.out.println("Sorry that email is already registered");
        }

        return false;
    }

    /**
     * receives the json string from the server and returns it as a json object
     * @return a json object
     */
    public static JSONObject receiveJSONObject(){
        Scanner input = new Scanner(new InputStreamReader(inputFromSocket));

        String currentline = "";

        while(!(currentline.contains(MovieServiceDetails.JSONOBJECT_ENDINGCHAR)) ||
                !(currentline.contains(MovieServiceDetails.FAIL)) ||
                !(currentline.contains(MovieServiceDetails.UNRECOGNISED_COMMAND))){
            currentline += input.nextLine();
        }

        JSONObject jsObj = new JSONObject(currentline);

        return jsObj;
    }

    /**
     * receives a json string and turns it into an json array
     * @return json array
     */
    public static JSONArray receiveJSONArray(){
        Scanner input = new Scanner(new InputStreamReader(inputFromSocket));

        String currentline = "";

        while(!(currentline.contains(MovieServiceDetails.JSONARRAY_ENDINGCHAR)) ||
                !(currentline.contains(MovieServiceDetails.FAIL)) ||
                !(currentline.contains(MovieServiceDetails.UNRECOGNISED_COMMAND))){
            currentline += input.nextLine();
        }
        JSONArray jsArray = new JSONArray(currentline);

        return jsArray;
    }

    /**
     * sends the close connection code to the server
     */
    public static void closeConnectionToServer(Socket clientSocket){
        try{
            inputFromSocket = clientSocket.getInputStream();

            streamWriter = new PrintWriter(clientSocket.getOutputStream());
            streamWriter.println(MovieServiceDetails.CLOSE_CONNECTION);
            streamWriter.flush();
        }catch (IOException io){
            System.out.println("Error on closing application");
        }
    }

    /**
     * receives command codes from the server
     * such as success for add of movie or close connection
     */
    public static String receiveCommandCodeFromServer(){
        Scanner input = new Scanner(new InputStreamReader(inputFromSocket));

        String answer;

        answer = input.nextLine();

        return answer;
    }

    /**
     * sends a movie object as a json string to the server
     * @param movieToSend
     * @param serverCode
     */
    public static void sendMovieJSON(Socket clientSocket, Movie movieToSend, String serverCode){
        try{
            String sendableMovie = serverCode + MovieServiceDetails.BREAKING_CHARACTER + movieToSend.toJSONString();

            streamWriter = new PrintWriter(clientSocket.getOutputStream());
            streamWriter.println(sendableMovie);
            streamWriter.flush();
        }catch (IOException io){
            System.out.println("Unable to send movie data to server\n");
        }
    }

    /**
     * splits the json array into multiple movie objects
     * solution found on stack overflow changed to suit these objects
     * src: https://stackoverflow.com/questions/33754101/split-json-object-from-json-array-in-java
     * @param movieArray
     * @return
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

    /**
     * converts a json movie object into an actual movie object
     * taken from camerons movieservicethread
     * @param jsonStringToConvert
     * @return
     */
    public static Movie convertJSONStringToMovie(String jsonStringToConvert)
    {
        JSONObject movieJSON = new JSONObject(jsonStringToConvert);

        int id = movieJSON.getInt("id");
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

        return new Movie(id, title, genre, director, runtime, plot, location, "", rating,
                format, year, starring, copies, "", "");
    }

    /**
     * sends the id of a movie that the user has requested to be deleted
     * @param id
     */
    public static void sendMovieToDelete(Socket clientSocket, int id){
        String message = MovieServiceDetails.REMOVE_MOVIE + MovieServiceDetails.BREAKING_CHARACTER
                + Integer.toString(id);

        try{
            inputFromSocket = clientSocket.getInputStream();

            streamWriter = new PrintWriter(clientSocket.getOutputStream());
            streamWriter.println(message);
            streamWriter.flush();
        }catch (IOException io){
            System.out.println("Unable to send movie data to server");
            System.out.println("Movie with id: " + id + " was not deleted from database\n");
        }
    }

    /**
     * checks the response string from the server to see if the response code is equal to ERR or ANKN
     * if it is one of these two strings it returns true
     * If the response code is not equals to the strings it returns false
     * @param response string response from server
     * @return true if there are errors or false if there is not errors
     */
    public static boolean checkResponseForErrors(String response){
        String[] splitres = response.split(MovieServiceDetails.BREAKING_CHARACTER);

        if(splitres[0].equals(MovieServiceDetails.FAIL) || splitres[0].equals(MovieServiceDetails.UNRECOGNISED_COMMAND)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean checkforLoginRegisterErrors(String response){
        String[] splitres = response.split(MovieServiceDetails.BREAKING_CHARACTER);

        if(splitres[0].equals(MovieServiceDetails.LOGIN_NOT_REGISTERED) ||
                splitres[0].equals(MovieServiceDetails.LOGIN_WRONG_INFO) ||
                        splitres[0].equals(MovieServiceDetails.REGISTER_ALREADY_REGISTERED)){
            return true;
        }else{
            return false;
        }
    }
}
