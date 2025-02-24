package com.ca4.DAO;

import com.ca4.DTO.WatchedMovie;
import com.ca4.Exceptions.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLWatchedMovieDAO extends MySQLDAO implements WatchedMovieDAOInterface {
    @Override
    public List<WatchedMovie> getAllUsersWatchedMovies(int userID) throws DAOException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<WatchedMovie> watchedMovies = new ArrayList<>();

        try {
            con = this.getConnection();
            String query = "SELECT * FROM watched_movies WHERE userID = ?";
            ps = con.prepareStatement(query);

            ps.setInt(1, userID);

            rs = ps.executeQuery();

            while (rs.next()) {
                int movieID = rs.getInt("movieID");

                watchedMovies.add(new WatchedMovie(userID, movieID));
            }
        } catch (SQLException e) {
            throw new DAOException("getAllUsersWatchedMovies() " + e.getMessage());
        } finally {
            try {
                this.closeConnection(con, rs, ps);
            } catch (SQLException e) {
                throw new DAOException("getAllUsersWatchedMovies().finally " + e.getMessage());
            }
        }

        return watchedMovies;
    }

    @Override
    public boolean addWatchedMovie(WatchedMovie watchedMovie) throws DAOException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = this.getConnection();
            String query = "INSERT INTO watched_movies (userID, movieID) VALUES(? ,?)";
            ps = con.prepareStatement(query);

            ps.setInt(1, watchedMovie.getUserID());
            ps.setInt(2, watchedMovie.getMovieID());

            int rowCount = ps.executeUpdate();
            return rowCount >= 1;
        } catch (SQLException e) {
            throw new DAOException("addWatchedMovie() " + e.getMessage());
        } finally {
            try {
                this.closeConnection(con, ps);
            } catch (SQLException e) {
                throw new DAOException("addWatchedMovie().finally " + e.getMessage());
            }
        }
    }

    @Override
    public boolean deleteWatchedMovie(WatchedMovie watchedMovie) throws DAOException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = this.getConnection();
            String query = "DELETE FROM watched_movies WHERE userID = ? AND movieID = ?";
            ps = con.prepareStatement(query);

            ps.setInt(1, watchedMovie.getUserID());
            ps.setInt(2, watchedMovie.getMovieID());

            int rowCount = ps.executeUpdate();
            return rowCount >= 1;
        } catch (SQLException e) {
            throw new DAOException("deleteWatchedMovie() " + e.getMessage());
        } finally {
            try {
                this.closeConnection(con, ps);
            } catch (SQLException e) {
                throw new DAOException("deleteWatchedMovie().finally " + e.getMessage());
            }
        }
    }

    @Override
    public boolean deleteAllUsersWatchedMovies(int userID) throws DAOException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = this.getConnection();
            String query = "DELETE FROM watched_movies WHERE userID = ?";
            ps = con.prepareStatement(query);

            ps.setInt(1, userID);

            int rowCount = ps.executeUpdate();
            return rowCount >= 1;
        } catch (SQLException e) {
            throw new DAOException("deleteAllUsersWatchedMovies() " + e.getMessage());
        } finally {
            try {
                this.closeConnection(con, ps);
            } catch (SQLException e) {
                throw new DAOException("deleteAllUsersWatchedMovies().finally " + e.getMessage());
            }
        }
    }
}
