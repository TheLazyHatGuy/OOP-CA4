package com.ca4.DAO;

import com.ca4.DTO.Movie;
import com.ca4.Exceptions.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySQLMovieDAO extends MySQLDAO implements MovieDAOInterface{
    public ArrayList<Movie> getAllMovies() throws DAOException{
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Movie> movies = new ArrayList<>();

        try{
            con = this.getConnection();
            String query = "SELECT * FROM movies";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            movies = fillArrayList(rs);

        }catch (SQLException e){
            throw new DAOException("getAllMovies() " + e.getMessage());
        }finally {
            try{
                closeConnection(con, rs, ps);
            }catch (SQLException e){
                throw new DAOException("getAllMovies() " + e.getMessage());
            }
        }
        return movies;
    }

    public Movie getMovieByID(int movieID) throws DAOException{
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Movie movie = new Movie();

        try {
            con = this.getConnection();
            String query = "SELECT * FROM movies WHERE id = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, movieID);
            rs = ps.executeQuery();

            while (rs.next()) {
                movie = resultSetToMovie(rs);
            }
        } catch (SQLException e) {
            throw new DAOException("getMovieByName() " + e.getMessage());
        } finally {
            try {
                closeConnection(con, rs, ps);
            } catch (SQLException e) {
                throw new DAOException("getMovieByName() " + e.getMessage());
            }
        }
        return movie;
    }

    public Movie getMovieByName(String inputTitle) throws DAOException{
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Movie movie = new Movie();

        try {
            con = this.getConnection();
            String query = "SELECT * FROM movies WHERE title like ?";
            ps = con.prepareStatement(query);
            ps.setString(1, inputTitle);
            rs = ps.executeQuery();

            while (rs.next()) {
                movie = resultSetToMovie(rs);
            }
        } catch (SQLException e) {
            throw new DAOException("getMovieByName() " + e.getMessage());
        } finally {
            try {
                closeConnection(con, rs, ps);
            } catch (SQLException e) {
                throw new DAOException("getMovieByName() " + e.getMessage());
            }
        }
        return movie;
    }

    public ArrayList<Movie> getMoviesByGenre(String Genre) throws DAOException{
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Movie> movies = new ArrayList<>();

        try{
            con = this.getConnection();
            String query = "SELECT * FROM movies WHERE genre like ?";
            ps = con.prepareStatement(query);
            ps.setString(1, Genre);
            rs = ps.executeQuery();

            movies = fillArrayList(rs);
        }catch (SQLException e) {
            throw new DAOException("getMovieByGenre() " + e.getMessage());
        } finally {
            try {
                closeConnection(con, rs, ps);
            } catch (SQLException e) {
                throw new DAOException("getMovieByGenre() " + e.getMessage());
            }
        }
        return movies;
    }

    public ArrayList<Movie> getMoviesByDirector(String dirname) throws DAOException{
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Movie> movies = new ArrayList<>();

        try{
            con = this.getConnection();
            String query = "SELECT * FROM movies WHERE director like ?";
            ps = con.prepareStatement(query);
            ps.setString(1, dirname);
            rs = ps.executeQuery();

            movies = fillArrayList(rs);
        }catch (SQLException e) {
            throw new DAOException("getMovieByDirector() " + e.getMessage());
        } finally {
            try {
                closeConnection(con, rs, ps);
            } catch (SQLException e) {
                throw new DAOException("getMovieByDirector() " + e.getMessage());
            }
        }
        return movies;
    }

    public boolean addMovieToDatabase(Movie movieToAdd) throws  DAOException{
        Connection con = null;
        PreparedStatement ps = null;

        try{
            con = this.getConnection();
            String query = "INSERT INTO movies (title, genre, director, runtime, plot, location, poster,"
                    + "rating, format, year, starring, copies, barcode, user_rating) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(query);
            ps.setString(1, movieToAdd.getTitle());
            ps.setString(2, movieToAdd.getGenre());
            ps.setString(3, movieToAdd.getDirector());
            ps.setString(4, movieToAdd.getRuntime());
            ps.setString(5, movieToAdd.getPlot());
            ps.setString(6, movieToAdd.getLocation());
            ps.setString(7, movieToAdd.getPoster());
            ps.setString(8, movieToAdd.getRating());
            ps.setString(9, movieToAdd.getFormat());
            ps.setString(10, movieToAdd.getYear());
            ps.setString(11, movieToAdd.getStarring());
            ps.setInt(12, movieToAdd.getCopies());
            ps.setString(13, movieToAdd.getBarcode());
            ps.setString(14, movieToAdd.getUserRating());

            int rowCount = ps.executeUpdate();
            return rowCount >= 1;

        }catch (SQLException e) {
            throw new DAOException("addMovieToDatabase() " + e.getMessage());
        } finally {
            try {
                closeConnection(con, ps);
            } catch (SQLException e) {
                throw new DAOException("addMovieToDatabase() " + e.getMessage());
            }
        }
    }

    public boolean deleteMovie(int movie_id) throws DAOException{
        Connection con = null;
        PreparedStatement ps = null;

        try{
            con = this.getConnection();
            String query = "DELETE FROM movies WHERE id = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1,movie_id);

            int rowCount = ps.executeUpdate();
            return rowCount >= 1;

        }catch (SQLException e) {
            throw new DAOException("DeleteMovieInDatabase() " + e.getMessage());
        } finally {
            try {
                closeConnection(con, ps);
            } catch (SQLException e) {
                throw new DAOException("DeleteMovieInDatabase() " + e.getMessage());
            }
        }
    }

    @SuppressWarnings("Duplicates")
    public boolean updateMovie(Movie movieToUpdate) throws DAOException{
        Connection con = null;
        PreparedStatement ps = null;

        try{
            con = this.getConnection();
            String query = "UPDATE movies SET title = ?, genre = ?, director = ?, runtime = ?," +
                    " plot = ?, location = ?, poster = ?, rating = ?, format = ?, year = ?, " +
                    "starring = ?, copies = ?, barcode = ?, user_rating = ? where id = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, movieToUpdate.getTitle());
            ps.setString(2, movieToUpdate.getGenre());
            ps.setString(3, movieToUpdate.getDirector());
            ps.setString(4, movieToUpdate.getRuntime());
            ps.setString(5, movieToUpdate.getPlot());
            ps.setString(6, movieToUpdate.getLocation());
            ps.setString(7, movieToUpdate.getPoster());
            ps.setString(8, movieToUpdate.getRating());
            ps.setString(9, movieToUpdate.getFormat());
            ps.setString(10, movieToUpdate.getYear());
            ps.setString(11, movieToUpdate.getStarring());
            ps.setInt(12, movieToUpdate.getCopies());
            ps.setString(13, movieToUpdate.getBarcode());
            ps.setString(14, movieToUpdate.getUserRating());
            ps.setInt(15, movieToUpdate.getId());

            int rowCount = ps.executeUpdate();
            return rowCount >= 1;

        }catch (SQLException e) {
            throw new DAOException("addMovieToDatabase() " + e.getMessage());
        } finally {
            try {
                closeConnection(con, ps);
            } catch (SQLException e) {
                throw new DAOException("addMovieToDatabase() " + e.getMessage());
            }
        }

    }

    private ArrayList<Movie> fillArrayList(ResultSet rs) throws DAOException{
        ArrayList<Movie> ListToReturn = new ArrayList<>();
        try {
            while (rs.next()) {
                Movie m = resultSetToMovie(rs);
                ListToReturn.add(m);
            }
        }catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
        return ListToReturn;
    }

    private Movie resultSetToMovie(ResultSet resultSetToConvert) throws SQLException
    {
        int movieId = resultSetToConvert.getInt("ID");
        String title = resultSetToConvert.getString("title");
        String genre = resultSetToConvert.getString("genre");
        String director = resultSetToConvert.getString("director");
        String runtime = resultSetToConvert.getString("runtime");
        String plot = resultSetToConvert.getString("plot");
        String location = resultSetToConvert.getString("location");
        String poster = resultSetToConvert.getString("poster");
        String rating = resultSetToConvert.getString("rating");
        String format = resultSetToConvert.getString("format");
        String year = resultSetToConvert.getString("year");
        String starring = resultSetToConvert.getString("starring");
        int copies = resultSetToConvert.getInt("copies");
        String barcode = resultSetToConvert.getString("barcode");
        String user_rating = resultSetToConvert.getString("user_rating");

        return new Movie(movieId, title, genre, director, runtime, plot, location,
                poster, rating, format, year, starring, copies, barcode, user_rating);
    }
}
