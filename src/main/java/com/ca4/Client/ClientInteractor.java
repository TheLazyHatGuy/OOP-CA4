package com.ca4.Client;

import com.ca4.DTO.Movie;

import java.sql.SQLOutput;
import java.util.Random;
import java.util.Scanner;

public class ClientInteractor {
    private static Scanner scan = new Scanner(System.in);

    public static String getMenuOption_NotLoggedIn(){
        System.out.println("A) Login");
        System.out.println("B) Register");
        System.out.println("C) Exit application");

        String option = getStringFromUser();

        return option;
    }

    public static String getMenuOption_LoggedIn(){
        System.out.println("A) Find movie by name");
        System.out.println("B) Find movies by director");
        System.out.println("C) Find movies by genre");
        System.out.println("D) Update a movie");
        System.out.println("E) Add a movie");
        System.out.println("F) Delete a movie");
        System.out.println("G) Update your profile");
        System.out.println("H) Delete your profile");
        System.out.println("I) Logout and exit application");

        String option = ClientInteractor.getStringFromUser();

        return option;
    }

    public static String[] loginRegister(){
        System.out.println("Please enter your email: ");
        String email = ClientInteractor.getEmail();
        System.out.println("Please enter your password: ");
        String password = ClientInteractor.getPassword();

        String[] userDetails = {email, password};

        return userDetails;
    }

    public static String getMovieName(){
        System.out.println("What is the name of the movie you are looking for: ");
        String movieName = ClientInteractor.getStringFromUser();

        return movieName;
    }

    public static String getMovieGenre(){
        System.out.println("What movie genre would you like to search by: ");
        String movieGenre = ClientInteractor.getStringFromUser();

        return movieGenre;
    }

    public static String getMovieDirector(){
        System.out.println("What movie director would you like to search by: ");
        String directorName = ClientInteractor.getStringFromUser();

        return directorName;
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

    public static String getStringFromUser(){
        String stringFromUser = scan.nextLine();
        //TODO add input validation

        return stringFromUser;
    }

    public static String getEmail(){
        String username = scan.nextLine();
        //TODO add input and username validation

        return username;
    }

    public static String getPassword(){
        String password = scan.nextLine();
        //TODO add input validation and password validation

        return password;
    }

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
