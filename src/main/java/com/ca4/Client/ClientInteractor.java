package com.ca4.Client;

import java.util.Scanner;

public class ClientInteractor {
    private static Scanner scan = new Scanner(System.in);

    public static String[] Login(){
        System.out.println("Please enter your username: ");
        String username = ClientInteractor.getUsername();
        System.out.println("Please enter your password: ");
        String password = ClientInteractor.getPassword();

        String[] userDetails = {username, password};

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

    public static String getUsername(){
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
