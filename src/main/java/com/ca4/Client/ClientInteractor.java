package com.ca4.Client;

import com.ca4.Core.MovieServiceDetails;
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

        return option.toUpperCase();
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
        System.out.println("G) Get your recommended movies");
        System.out.println("H) Add a movie to your list of watched movies");
        System.out.println("I) Logout and exit application");

        String option = getMenuOptionFromUser(7);

        return option.toUpperCase();
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

        return option.toUpperCase();
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
                case MenuDetails.UPDATETITLE:
                    System.out.print("Please enter the updated name of the movie:");
                    updatedParam = getStringFromUser();
                    updatedMovie.setTitle(updatedParam);
                    break;

                case MenuDetails.UPDATEGENRE:
                    System.out.print("Please enter the updated genre list of the movie:");
                    updatedParam = getStringFromUser();
                    updatedMovie.setGenre(updatedParam);
                    break;

                case MenuDetails.UPDATEDIRECTOR:
                    System.out.print("Please enter the updated director name of the movie:");
                    updatedParam = getStringFromUser();
                    updatedMovie.setDirector(updatedParam);
                    break;

                case MenuDetails.UPDATERUNTIME:
                    System.out.print("Please enter the updated runtime of the movie:");
                    updatedParam = getStringFromUser();
                    updatedMovie.setRuntime(updatedParam);
                    break;

                case MenuDetails.UPDATEPLOT:
                    System.out.print("Please enter the updated plot of the movie:");
                    updatedParam = getStringFromUser();
                    updatedMovie.setPlot(updatedParam);
                    break;

                case MenuDetails.UPDATERATING:
                    System.out.print("Please enter the updated rating of the movie:");
                    updatedParam = getStringFromUser();
                    updatedMovie.setRating(updatedParam);
                    break;

                case MenuDetails.UPDATEFORMAT:
                    System.out.print("Please enter the updated format of the movie:");
                    updatedParam = getStringFromUser();
                    updatedMovie.setFormat(updatedParam);
                    break;

                case MenuDetails.UPDATEYEAR:
                    System.out.print("Please enter the updated year of release of the movie:");
                    updatedParam = getStringFromUser();
                    updatedMovie.setYear(updatedParam);
                    break;

                case MenuDetails.UPDATEACTORS:
                    System.out.print("Please enter the updated actor/actress list of the movie:");
                    updatedParam = getStringFromUser();
                    updatedMovie.setStarring(updatedParam);
                    break;

                case MenuDetails.UPDATECOPIES:
                    System.out.print("Please enter the updated amount of available copies of the movie:");
                    int updatedCopies = scan.nextInt();
                    updatedMovie.setCopies(updatedCopies);
                    break;

                case MenuDetails.UPDATEEXIT:
                    boolean answer = getYesorNofromuser();

                    if(answer){
                        return updatedMovie;
                    }

                    keepUpdating = false;
                    break;

                case MenuDetails.EXITNOUPDATE:
                    keepUpdating = false;
                    break;

                default:
                    System.out.println("Invalid input");
                    break;
            }
        }

        return movieToUpdate;
    }

    /**
     * allows the user to enter their account details or register for an account
     * @return sting array with user login details
     */
    public static String[] loginRegister(){
        boolean iscredentialsGood = false;
        String email = null;
        String password = null;
        String[] userDetails = new String[2];

        while(iscredentialsGood){
            if(email == null){
                System.out.println("Please enter your email: ");
                email = getEmail();
                System.out.println("Please enter your password: ");
                password = getPassword();
            }
            if(password == null){
                System.out.println("Please enter your password: ");
                password = getPassword();
            }
            if(email == null && password == null){
                userDetails[0] = email;
                userDetails[1] = password;
                iscredentialsGood = true;
            }
        }

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

        userInput = userInput.toUpperCase();

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

    /**
     * gets the user's email and checks to see if the email is valid
     * @return null if not valid or the user's email if it is valid
     */
    public static String getEmail(){
        String username = scan.nextLine();

        /**
         * regex from:
         * https://howtodoinjava.com/regex/java-regex-validate-email-address/
         * will allow:
         * A-Z, a-z, 0-9, ".", "-", and "_"
         */
        if(!username.matches("^[A-Za-z0-9+_.-]{7,}+@(.+)$")){
            return null;
        }

        return username;
    }

    /**
     * get the users password and checks if it is valid
     * @return null if not valid or the user's password if valid
     */
    public static String getPassword(){
        String password = scan.nextLine();

        /**
         * regex from:
         * https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
         *
         * modified so special chars are not needed
         */
        if(!password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{10,}$")){
            return null;
        }

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
