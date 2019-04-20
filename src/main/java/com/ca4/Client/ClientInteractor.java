package com.ca4.Client;

import com.ca4.DTO.Movie;

import java.util.Random;
import java.util.Scanner;

/**
 * this class is the class the client class will call to whenever it needs user input
 */
public class ClientInteractor {
    private static Scanner scan = new Scanner(System.in);

    /**
     * this is the first menu a client will interact with giving them the option to register, login or exit
     * @return user's menu option
     */
    public static String getMenuOption_NotLoggedIn(){
        System.out.println("A) Login");
        System.out.println("B) Register");
        System.out.println("C) Exit application");

        String option = getMenuOptionFromUser(3);

        return option;
    }

    /**
     * this is the menu an user will interact with if they are logged in
     * @return
     */
    public static String getMenuOption_LoggedIn(){
        System.out.println("A) Find movie by name");
        System.out.println("B) Find movies by director");
        System.out.println("C) Find movies by genre");
        System.out.println("D) Update a movie");
        System.out.println("E) Add a movie");
        System.out.println("F) Delete a movie");
        System.out.println("G) Logout and exit application");

        String option = getMenuOptionFromUser(7);

        return option;
    }

    /**
     * return which variable about the movie the user would like to update
     * @return user's menu option
     */
    public static String getUpdateOption(){
        System.out.println("A) Update movie name");
        System.out.println("B) Update movie genre list");
        System.out.println("C) Update movie director");
        System.out.println("D) Update movie runtime");
        System.out.println("E) Update movie plot");
        System.out.println("F) Update movie rating");
        System.out.println("G) Update movie format");
        System.out.println("H) Update movie year");
        System.out.println("I) Update movie actor/actress list");
        System.out.println("J) Update number of available copies of the movies");
        System.out.println("K) Run update and exit");
        System.out.println("L) Exit without updating");

        String option = getMenuOptionFromUser(12);

        option.toUpperCase();

        return option;
    }

    /**
     * allows the user to choose what part of the movie the user would like to update
     * keep running until the user is ready to update the movie
     * @param movieToUpdate
     * @return
     */
    public static Movie UpdateMovie(Movie movieToUpdate){
        boolean keepUpdating = true;
        String updatedParam;
        String option;

        Movie updatedMovie = new Movie(movieToUpdate.getId(), movieToUpdate.getTitle(), movieToUpdate.getGenre(),
                movieToUpdate.getDirector(), movieToUpdate.getRuntime(), movieToUpdate.getPlot(), movieToUpdate.getLocation(),
                movieToUpdate.getPoster(), movieToUpdate.getRating(), movieToUpdate.getFormat(), movieToUpdate.getYear(),
                movieToUpdate.getStarring(), movieToUpdate.getCopies(), movieToUpdate.getBarcode(), movieToUpdate.getUserRating());
        while(keepUpdating){
            option = getUpdateOption();

            switch (option) {
                case "A":
                    System.out.print("Please enter the updated name of the movie:");
                    updatedParam = getStringFromUser();
                    updatedMovie.setTitle(updatedParam);
                    break;

                case "B":
                    System.out.print("Please enter the updated genre list of the movie:");
                    updatedParam = getStringFromUser();
                    updatedMovie.setGenre(updatedParam);
                    break;

                case "C":
                    System.out.print("Please enter the updated director name of the movie:");
                    updatedParam = getStringFromUser();
                    updatedMovie.setDirector(updatedParam);
                    break;

                case "D":
                    System.out.print("Please enter the updated runtime of the movie:");
                    updatedParam = getStringFromUser();
                    updatedMovie.setRuntime(updatedParam);
                    break;

                case "E":
                    System.out.print("Please enter the updated plot of the movie:");
                    updatedParam = getStringFromUser();
                    updatedMovie.setPlot(updatedParam);
                    break;

                case "F":
                    System.out.print("Please enter the updated rating of the movie:");
                    updatedParam = getStringFromUser();
                    updatedMovie.setRating(updatedParam);
                    break;

                case "G":
                    System.out.print("Please enter the updated format of the movie:");
                    updatedParam = getStringFromUser();
                    updatedMovie.setFormat(updatedParam);
                    break;

                case "H":
                    System.out.print("Please enter the updated year of release of the movie:");
                    updatedParam = getStringFromUser();
                    updatedMovie.setYear(updatedParam);
                    break;

                case "I":
                    System.out.print("Please enter the updated actor/actress list of the movie:");
                    updatedParam = getStringFromUser();
                    updatedMovie.setStarring(updatedParam);
                    break;

                case "J":
                    System.out.print("Please enter the updated amount of available copies of the movie:");
                    int updatedCopies = scan.nextInt();
                    updatedMovie.setCopies(updatedCopies);
                    break;

                case "K":
                    boolean answer = getYesorNofromuser();

                    if(answer){
                        return updatedMovie;
                    }
                    break;

                case "L":
                    keepUpdating = false;
                    break;
            }
        }

        return movieToUpdate;
    }

    /**
     * allows the user to enter their account details or register for an account
     * @return
     */
    public static String[] loginRegister(){
        System.out.println("Please enter your email: ");
        String email = getEmail();
        System.out.println("Please enter your password: ");
        String password = getPassword();

        String[] userDetails = {email, password};

        return userDetails;
    }

    public static String getMovieName(){
        String movieName = getStringFromUser();

        return movieName;
    }

    public static String getMovieGenre(){
        System.out.println("What movie genre would you like to search by: ");
        String movieGenre = getStringFromUser();

        return movieGenre;
    }

    public static String getMovieDirector(){
        System.out.println("What movie director would you like to search by: ");
        String directorName = getStringFromUser();

        return directorName;
    }

    public static boolean getYesorNofromuser(){
        System.out.print("Would you like to continue with the action on this movie [Y]es or [N]o: ");
        String answer = scan.nextLine();

        answer.toUpperCase();

        if(answer.equals("Y")){
            return true;
        }else if(answer.equals("N")){
            return false;
        }

        return false;
    }

    public static Movie getMovieDetails(){
        System.out.print("PLease enter the name of the movie: ");
        String name = scan.nextLine();
        System.out.print("Please enter the genres of the movie: ");
        String genre = scan.nextLine();
        System.out.print("Please enter the name of the director of the movie: ");
        String director = scan.nextLine();
        System.out.print("Please enter the runtime of the movie: ");
        String runtime = scan.nextLine();
        System.out.print("Please enter the plot of the movie: ");
        String plot = scan.nextLine();
        System.out.print("Please enter the rating of the movie: ");
        String rating = scan.nextLine();
        System.out.print("Please enter the disc format of the movie: ");
        String format = scan.nextLine();
        System.out.print("Please enter the year the movie was released: ");
        String year = scan.nextLine();
        System.out.print("Please enter the actor/actress list of the movie: ");
        String starring = scan.nextLine();
        System.out.print("Please enter the critical rating of the movie: ");
        String userRating = scan.nextLine();
        System.out.print("Please enter the number of available copies of the movie: ");
        int copies = scan.nextInt();

        return new Movie(0, name, genre, director, runtime, plot, "", "", rating,
                format, year, starring, copies, generateRandomBarcode(), userRating);
    }

    public static String getMenuOptionFromUser(int lastCharIntValue){
        int valueOfA = 65;
        String userInput = getStringFromUser();

        userInput.toUpperCase();

        char[] inputtedChar = userInput.toCharArray();

        if(inputtedChar.length > 1){
            return null;
        }

        for(int i = valueOfA; i < valueOfA+lastCharIntValue; ++i){
            if(inputtedChar[0] == (char)i){
                return userInput;
            }
        }

        return null;
    }

    public static String getStringFromUser(){
        String stringFromUser = scan.nextLine();

        return stringFromUser;
    }

    public static String getEmail(){
        String username = scan.nextLine();

        /**
         * regex from:
         * https://howtodoinjava.com/regex/java-regex-validate-email-address/
         * will allow:
         * A-Z, a-z, 0-9, ".", "-", and "_"
         */
        if(!username.matches("^[A-Za-z0-9+_.-]+@(.+)$")){
            return null;
        }

        return username;
    }

    public static String getPassword(){
        String password = scan.nextLine();
        //TODO add input validation and password validation

        return password;
    }

    /**
     * generate a random barcode for the movie object
     * @return barcode
     */
    public static String generateRandomBarcode(){
        Random rand = new Random();

        Long randNum = -1l;

        while(randNum < 0){
            randNum = rand.nextLong();
        }

        String stringNum = String.valueOf(randNum);

        return stringNum;
    }
}
