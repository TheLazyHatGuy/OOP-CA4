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

                            printMovie(movie);

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
                            System.out.println("What is the name of the movie you are looking to update: ");
                            String[] movieToUpdate = {ClientInteractor.getMovieName()};
                            sendStringArray(MovieServiceDetails.SEARCH_MOVIE_TITLE, movieToUpdate);

                            jsonMovie = receiveJSONObject();
                            Movie movieForUpdate = convertJSONStringToMovie(jsonMovie.toString());

                            printMovie(movieForUpdate);

                            Movie updatedMovie = ClientInteractor.UpdateMovie(movieForUpdate);

                            sendMovieJSON(updatedMovie, MovieServiceDetails.UPDATE_MOVIE);

                            break;

                        case "E":
                            Movie movieToAdd = ClientInteractor.getMovieDetails();
                            sendMovieJSON(movieToAdd, MovieServiceDetails.ADD_MOVIE);
                            break;

                        case "F":
                            System.out.println("What is the name of the movie you are looking to delete: ");
                            String[] movieToDelete = {ClientInteractor.getMovieName()};

                            sendStringArray(MovieServiceDetails.SEARCH_MOVIE_TITLE, movieToDelete);
                            jsonMovie = receiveJSONObject();
                            Movie movieForDelete = convertJSONStringToMovie(jsonMovie.toString());

                            printMovie(movieForDelete);

                            if(ClientInteractor.getYesorNofromuser()){
                                sendMovieToDelete(movieForDelete.getId());
                            }
                            break;

                        case "G":
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

    public static void sendMovieJSON(Movie movieToSend, String serverCode){
        try{
            String sendableMovie = serverCode + MovieServiceDetails.BREAKING_CHARACTER + movieToSend.toJSONString();

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

    public static void printMovieArray(ArrayList<Movie> movies){
        int longestID = 5;
        int longestTitle = 10;
        int maxGenrelengthBeforeNewLIne = 50;
        int longestDirector = 10;
        int longestRuntime = 5;
        int maxPlotlengthBeforeNewLIne = 50;
        int longestRating = 4;
        int longestFormat = 5;
        int longestYear = 4;
        int maxStarringListlengthBeforeNewLIne = 50;
        int longestCopies = 4;

        for(int i = 0; i < movies.size(); ++i){
            int movieTitleLength = movies.get(i).getTitle().length();
            int movieDirectorLength = movies.get(i).getDirector().length();
            int movieRuntimeLength = movies.get(i).getRuntime().length();
            int movieRatingLength = movies.get(i).getRating().length();
            int movieFormatLength = movies.get(i).getFormat().length();
            int movieYearLength = movies.get(i).getYear().length();

            if(movieTitleLength > longestTitle){
                longestTitle = movieTitleLength;
            }

            if(movieDirectorLength > longestDirector){
                longestDirector = movieDirectorLength;
            }

            if(movieRuntimeLength > longestRuntime){
                longestRuntime = movieRuntimeLength;
            }

            if(movieRatingLength > longestRating){
                longestRating = movieRatingLength;
            }

            if(movieFormatLength > longestFormat){
                longestFormat = movieFormatLength;
            }

            if(movieYearLength > longestYear){
                longestYear = movieYearLength;
            }
        }

        for(Movie movie : movies){
            System.out.println("||" + pad("ID", longestID) + "||" + pad("Title", longestTitle) +
                    "||" + pad("Genre", maxGenrelengthBeforeNewLIne) + "||" + pad("Director", longestDirector) +
                    "||" + pad("RunTime", longestRuntime) + "||" + pad("Rating", longestRating) + "||"
                    + pad("Format", longestFormat) + "||" + pad("Year", longestYear) + "||" +
                    pad("Starring", maxStarringListlengthBeforeNewLIne) + "||" + pad("Copies", longestCopies) + "||");

            System.out.println("||" + pad(Integer.toString(movie.getId()), longestID) + "||" + pad(movie.getTitle(), longestTitle) +
                    "||" + pad(movie.getGenre(), maxGenrelengthBeforeNewLIne) + "||" + pad(movie.getGenre(), longestDirector) +
                    "||" + pad(movie.getRuntime(), longestRuntime) + "||" + pad(movie.getRating(), longestRating) + "||"
                    + pad(movie.getFormat(), longestFormat) + "||" + pad(movie.getYear(), longestYear) + "||" +
                    pad(movie.getStarring(), maxStarringListlengthBeforeNewLIne) + "||" +
                    pad(Integer.toString(movie.getCopies()), longestCopies) + "||");

            System.out.println("\n" + movie.getPlot() + "\n");
        }
    }

    public static void printMovie(Movie movie){
        int longestID = 5;
        int longestTitle = 10;
        int maxGenrelengthBeforeNewLIne = 50;
        int longestDirector = 10;
        int longestRuntime = 5;
        int maxPlotlengthBeforeNewLIne = 50;
        int longestRating = 4;
        int longestFormat = 5;
        int longestYear = 4;
        int maxStarringListlengthBeforeNewLIne = 50;
        int longestCopies = 4;

        int movieTitleLength = movie.getTitle().length();
        int movieDirectorLength = movie.getDirector().length();
        int movieRuntimeLength = movie.getRuntime().length();
        int movieRatingLength = movie.getRating().length();
        int movieFormatLength = movie.getFormat().length();
        int movieYearLength = movie.getYear().length();

        if(movieTitleLength > longestTitle){
            longestTitle = movieTitleLength;
        }

        if(movieDirectorLength > longestDirector){
            longestDirector = movieDirectorLength;
        }

        if(movieRuntimeLength > longestRuntime){
            longestRuntime = movieRuntimeLength;
        }

        if(movieRatingLength > longestRating){
            longestRating = movieRatingLength;
        }

        if(movieFormatLength > longestFormat){
            longestFormat = movieFormatLength;
        }

        if(movieYearLength > longestYear){
            longestYear = movieYearLength;
        }

        System.out.println("||" + pad("ID", longestID) + "||" + pad("Title", longestTitle) +
                "||" + pad("Genre", maxGenrelengthBeforeNewLIne) + "||" + pad("Director", longestDirector) +
                "||" + pad("RunTime", longestRuntime) + "||" + pad("Rating", longestRating) + "||"
                + pad("Format", longestFormat) + "||" + pad("Year", longestYear) + "||" +
                pad("Starring", maxStarringListlengthBeforeNewLIne) + "||" + pad("Copies", longestCopies) + "||");

        System.out.println("||" + pad(Integer.toString(movie.getId()), longestID) + "||" + pad(movie.getTitle(), longestTitle) +
                "||" + pad(movie.getGenre(), maxGenrelengthBeforeNewLIne) + "||" + pad(movie.getGenre(), longestDirector) +
                "||" + pad(movie.getRuntime(), longestRuntime) + "||" + pad(movie.getRating(), longestRating) + "||"
                + pad(movie.getFormat(), longestFormat) + "||" + pad(movie.getYear(), longestYear) + "||" +
                pad(movie.getStarring(), maxStarringListlengthBeforeNewLIne) + "||" +
                pad(Integer.toString(movie.getCopies()), longestCopies) + "||");

        System.out.println("\n" + movie.getPlot() + "\n");
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

    private static String pad(String string, int stringLength){
        while (string.length() < stringLength){
            string += " ";
        }

        return string;
    }
}
