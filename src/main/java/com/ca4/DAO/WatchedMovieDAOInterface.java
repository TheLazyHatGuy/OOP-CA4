package com.ca4.DAO;

import com.ca4.DTO.WatchedMovie;
import com.ca4.Exceptions.DAOException;

import java.util.List;

public interface WatchedMovieDAOInterface
{
    List<WatchedMovie> getAllUsersWatchedMovies(int userID) throws DAOException;

    void addWatchedMovie (WatchedMovie watchedMovie) throws DAOException;

    void deleteWatchedMovie (WatchedMovie watchedMovie) throws DAOException;

    void deleteAllUsersWatchedMovies (int userID) throws DAOException;
}
