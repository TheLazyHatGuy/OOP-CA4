package com.ca4.DAO;

import com.ca4.DTO.Movie;
import com.ca4.Exceptions.DAOException;

import java.util.ArrayList;

public interface MovieDAOInterface {
    ArrayList<Movie> getAllMovies() throws DAOException;

    Movie getMoviebyName(String title) throws DAOException;

    ArrayList<Movie> getMoviesbyGenre(String genre) throws DAOException;

    ArrayList<Movie> getMoviesbyDirector(String dirName) throws DAOException;

    void addMovieToDatabase(String title, String genres, String director, String runtime,
                                        String plot, String rating, String format, String Year,
                                        String starring) throws DAOException;

    void deleteMovie(int movie_id) throws DAOException;

    void updateMovie(int id, String title, String genres, String director,
                     String runtime, String plot, String rating, String format,
                     String Year, String starring) throws DAOException;
}
