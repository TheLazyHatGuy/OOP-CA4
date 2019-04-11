package com.ca4.DAO;

import com.ca4.DTO.Movie;
import com.ca4.Exceptions.DAOException;

import java.sql.*;
import java.util.ArrayList;

public class MySQLMovieDAO extends MySQLDAO implements MovieDAOInterface{
    public ArrayList<Movie> getAllMovies() throws DAOException{
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Movie> movies = new ArrayList<>();

        try{
            con = this.getConnection();
            String query = "SELECT * FROM movie";
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

    public Movie getMovieByName(String inputTitle) throws DAOException{
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Movie movie = new Movie();

        try {
            con = this.getConnection();
            String query = "SELECT * FROM movie WHERE title like ?";
            ps = con.prepareStatement(query);
            ps.setString(1, inputTitle);
            rs = ps.executeQuery();

            while (rs.next()) {
                int movieId = rs.getInt("ID");
                String title = rs.getString("title");
                String genre = rs.getString("genre");
                String director = rs.getString("director");
                String runtime = rs.getString("runtime");
                String plot = rs.getString("plot");
                String location = rs.getString("location");
                String poster = rs.getString("poster");
                String rating = rs.getString("rating");
                String format = rs.getString("format");
                String year = rs.getString("year");
                String starring = rs.getString("starring");
                int copies = rs.getInt("copies");
                String barcode = rs.getString("barcode");
                String user_rating = rs.getString("user_rating");

                movie = new Movie(movieId, title, genre, director, runtime, plot, location,
                        poster, rating, format, year, starring, copies, barcode, user_rating);
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
            String query = "SELECT * FROM movie WHERE genre like ?";
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
            String query = "SELECT * FROM movie WHERE director like ?";
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

    public void addMovieToDatabase(Movie movieToAdd) throws  DAOException{
        Connection con = null;
        PreparedStatement ps = null;

        try{
            con = this.getConnection();
            String query = "INSERT INTO movie (title, genre, director, runtime, plot, location, poster,"
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
            ps.execute();

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

    public void deleteMovie(int movie_id) throws DAOException{
        Connection con = null;
        PreparedStatement ps = null;

        try{
            con = this.getConnection();
            String query = "DELETE FROM movie WHERE id = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1,movie_id);
            ps.execute();

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
    public void updateMovie(Movie movieToUpdate) throws DAOException{
        Connection con = null;
        PreparedStatement ps = null;

        try{
            con = this.getConnection();
            String query = "UPDATE movie SET title = ?, genre = ?, director = ?, runtime = ?," +
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
            ps.execute();

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
                int movieId = rs.getInt("ID");
                String title = rs.getString("title");
                String genre = rs.getString("genre");
                String director = rs.getString("director");
                String runtime = rs.getString("runtime");
                String plot = rs.getString("plot");
                String location = rs.getString("location");
                String poster = rs.getString("poster");
                String rating = rs.getString("rating");
                String format = rs.getString("format");
                String year = rs.getString("year");
                String starring = rs.getString("starring");
                int copies = rs.getInt("copies");
                String barcode = rs.getString("barcode");
                String user_rating = rs.getString("user_rating");

                Movie m = new Movie(movieId, title, genre, director, runtime, plot, location,
                        poster, rating, format, year, starring, copies, barcode, user_rating);
                ListToReturn.add(m);
            }
        }catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
        return ListToReturn;
    }
}
