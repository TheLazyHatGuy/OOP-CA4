package com.ca4.BusinessObjects;

import com.ca4.DAO.MovieDAOInterface;
import com.ca4.DAO.MySQLMovieDAO;
import com.ca4.DTO.Movie;
import com.ca4.Utilities.Asker;

import java.util.ArrayList;

public class MainApp
{
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static MovieDAOInterface movieDAO = new MySQLMovieDAO();

    public static void startApp(Asker asker)
    {
        testMovies();
    }

    private static void testMovies()
    {
        try
        {
            ArrayList<Movie> movies = movieDAO.getAllMovies();

            for (Movie movie : movies)
            {
                System.out.println(movie.toJSONString());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
