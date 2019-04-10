package com.ca4.Client;

import java.util.Scanner;

public class ClientInteractor {
    private static Scanner scan = new Scanner(System.in);

    public static String getMenuOption_NotLoggedIn(){
        System.out.println("A) Login");
        System.out.println("B) Register");

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
}
