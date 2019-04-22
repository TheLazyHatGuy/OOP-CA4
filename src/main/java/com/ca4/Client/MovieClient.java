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

/**
 * this is the main class the client will run to interact with the server
 */
public class MovieClient
{
    private static Socket client;
    private static boolean loggedIn = false;
    public static User localUser;

    public static void main(String[] args)
    {
        JSONArray jsMovieArray;
        ArrayList<Movie> movieArray;
        JSONObject jsonMovie;

        boolean continueRunning = true;
        String option;


        try{
            InetAddress serverIP = InetAddress.getByName(MovieServiceDetails.SERVER_IP);

            client = new Socket(serverIP, MovieServiceDetails.SERVER_PORT);

            while(continueRunning){
                if(!loggedIn){
                    option = ClientInteractor.getMenuOption_NotLoggedIn();

                    switch (option){
                        case MenuDetails.LOGIN:
                            String[] loginDetails = ClientInteractor.loginRegister();
                            MessageSender.sendStringArray(client, MovieServiceDetails.LOGIN, loginDetails);
                            loggedIn = MessageSender.createNewUser(client, loginDetails);
                            break;

                        case MenuDetails.REGISTER:
                            String[] userDetails = ClientInteractor.loginRegister();
                            MessageSender.sendStringArray(client, MovieServiceDetails.REGISTER, userDetails);
                            loggedIn = MessageSender.createNewUser(client, userDetails);
                            break;

                        case MenuDetails.EXIT:
                            MessageSender.closeConnectionToServer(client);
                            continueRunning = false;
                            break;

                        default:
                            System.out.println("Invalid input");
                            break;
                    }


                }else if(loggedIn){
                    option = ClientInteractor.getMenuOption_LoggedIn();

                    switch (option){
                        case MenuDetails.FINDBYTITLE:
                            System.out.println("What is the name of the movie you are looking for: ");
                            String[] movieName = {ClientInteractor.getMovieName()};
                            MessageSender.sendStringArray(client, MovieServiceDetails.SEARCH_MOVIE_TITLE, movieName);
                            jsonMovie = MessageSender.receiveJSONObject();
                            Movie movie = MessageSender.convertJSONStringToMovie(jsonMovie.toString());

                            printMovie(movie);

                            break;

                        case MenuDetails.FINDBYDIRECTOR:
                            String[] movieDirector = {ClientInteractor.getMovieDirector()};
                            MessageSender.sendStringArray(client, MovieServiceDetails.SEARCH_MOVIE_DIRECTOR, movieDirector);
                            jsMovieArray = MessageSender.receiveJSONArray();
                            movieArray = MessageSender.splitJSONMovieArray(jsMovieArray);

                            printMovieArray(movieArray);

                            break;

                        case MenuDetails.FINDBYGENRE:
                            String[] movieGenre = {ClientInteractor.getMovieGenre()};
                            MessageSender.sendStringArray(client, MovieServiceDetails.SEARCH_MOVIE_GENRE, movieGenre);
                            jsMovieArray = MessageSender.receiveJSONArray();
                            movieArray = MessageSender.splitJSONMovieArray(jsMovieArray);

                            printMovieArray(movieArray);

                            break;

                        case MenuDetails.UPDATEMOVIE:
                            System.out.println("What is the name of the movie you are looking to update: ");
                            String[] movieToUpdate = {ClientInteractor.getMovieName()};
                            MessageSender.sendStringArray(client, MovieServiceDetails.SEARCH_MOVIE_TITLE, movieToUpdate);

                            jsonMovie = MessageSender.receiveJSONObject();
                            Movie movieForUpdate = MessageSender.convertJSONStringToMovie(jsonMovie.toString());

                            printMovie(movieForUpdate);

                            Movie updatedMovie = ClientInteractor.UpdateMovie(movieForUpdate);

                            MessageSender.sendMovieJSON(client, updatedMovie, MovieServiceDetails.UPDATE_MOVIE);

                            break;

                        case MenuDetails.ADDMOVIE:
                            Movie movieToAdd = ClientInteractor.getMovieDetails();
                            MessageSender.sendMovieJSON(client, movieToAdd, MovieServiceDetails.ADD_MOVIE);
                            break;

                        case MenuDetails.DELETEMOVIE:
                            System.out.println("What is the name of the movie you are looking to delete: ");
                            String[] movieToDelete = {ClientInteractor.getMovieName()};

                            MessageSender.sendStringArray(client, MovieServiceDetails.SEARCH_MOVIE_TITLE, movieToDelete);
                            jsonMovie = MessageSender.receiveJSONObject();
                            Movie movieForDelete = MessageSender.convertJSONStringToMovie(jsonMovie.toString());

                            printMovie(movieForDelete);

                            if(ClientInteractor.getYesorNofromuser()){
                                MessageSender.sendMovieToDelete(client, movieForDelete.getId());
                                MessageSender.receiveCommandCodeFromServer();
                            }
                            break;

                        case MenuDetails.RECOMMENDED:
                            String[] userID = {Integer.toString(localUser.getId())};
                            MessageSender.sendStringArray(client, MovieServiceDetails.RECOMMEND_MOVIE, userID);
                            jsonMovie = MessageSender.receiveJSONObject();
                            Movie recommendedMovie = MessageSender.convertJSONStringToMovie(jsonMovie.toString());

                            printMovie(recommendedMovie);

                            break;

                        case MenuDetails.SETWATCHED:
                            System.out.println("What is the name of the movie you have watched: ");
                            String[] watchedMovie = {ClientInteractor.getMovieName()};

                            MessageSender.sendStringArray(client, MovieServiceDetails.SEARCH_MOVIE_TITLE, watchedMovie);
                            jsonMovie = MessageSender.receiveJSONObject();
                            Movie watched = MessageSender.convertJSONStringToMovie(jsonMovie.toString());

                            printMovie(watched);

                            String[] IDofUserandMovie = {Integer.toString(localUser.getId()),
                                    Integer.toString(watched.getId())};

                            if(ClientInteractor.getYesorNofromuser()){
                                MessageSender.sendStringArray(client, MovieServiceDetails.WATCH_MOVIE, IDofUserandMovie);
                                MessageSender.receiveCommandCodeFromServer();
                            }

                        case MenuDetails.LOGOUTEXIT:
                            MessageSender.closeConnectionToServer(client);
                            loggedIn = false;
                            continueRunning = false;
                            break;

                        default:
                            System.out.println("Invalid input");
                    }
                }
            }
        }catch (UnknownHostException ue){
            System.out.println("Server not found on " + MovieServiceDetails.SERVER_IP);
            System.exit(1);
        }catch (IOException io){
            System.out.println("Server not found on " + MovieServiceDetails.SERVER_IP);
            System.exit(1);
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

    /**
     * prints an arraylist of movies into a clean format
     * @param movies arraylist that has been returned from the server per client request
     */
    private static void printMovieArray(ArrayList<Movie> movies){
        int longestID = 5;
        int longestTitle = 10;
        int maxGenrelengthBeforeNewLIne = 50;
        int longestDirector = 10;
        int longestRuntime = 5;
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

    /**
     * prints a single movie in a clean format
     * @param movie movie object that is gotten from the server via client request
     */
    private static void printMovie(Movie movie){
        int longestID = 5;
        int longestTitle = 10;
        int maxGenrelengthBeforeNewLIne = 50;
        int longestDirector = 10;
        int longestRuntime = 5;
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

    /**
     * adds a specific amount of padding between strings
     * @param string string that extra spacing is to be added to
     * @param stringLength length of string that requires spacing
     * @return a padded string
     */
    private static String pad(String string, int stringLength){
        while (string.length() < stringLength){
            string += " ";
        }

        return string;
    }
}
