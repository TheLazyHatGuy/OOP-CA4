package com.ca4.DAO;

import com.ca4.DTO.Movie;

import java.util.ArrayList;

public interface MovieDAOInterface {
    ArrayList<Movie> getAllMovies();

    Movie getMoviebyName(String name);

    ArrayList<Movie> getMoviesbyGenre(String genre);

    ArrayList<Movie> getMoviesbyDirector(String dirName);

    ArrayList<Movie> addMovieToDatabase(String title, ArrayList<String> genres, String director, String runtime,
                                        String plot, String rating, String format, String Year,
                                        ArrayList<String> starring);
}
