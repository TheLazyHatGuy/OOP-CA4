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
            String query = "SELECT * FROM MOVIES";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            movies = fillArrayList(rs);

        }catch (SQLException e){
            throw new DAOException("getAllMovies() " + e.getMessage());
        }finally {
            try{
                closeConnections(rs, con, ps);
            }catch (SQLException e){
                throw new DAOException("getAllMovies() " + e.getMessage());
            }
        }
        return movies;
    }

    public Movie getMoviebyName(String inputTitle) throws DAOException{
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Movie movie = new Movie();

        try {
            con = this.getConnection();
            String query = "SELECT * FROM MOVIES WHERE title like ?";
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
            throw new DAOException("getMoviebyName() " + e.getMessage());
        } finally {
            try {
                closeConnections(rs, con, ps);
            } catch (SQLException e) {
                throw new DAOException("getMoviebyName() " + e.getMessage());
            }
        }
        return movie;
    }

    public ArrayList<Movie> getMoviesbyGenre(String Genre) throws DAOException{
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Movie> movies = new ArrayList<>();

        try{
            con = this.getConnection();
            String query = "SELECT * FROM MOVIES WHERE genre like ?";
            ps = con.prepareStatement(query);
            ps.setString(1, Genre);
            rs = ps.executeQuery();

            movies = fillArrayList(rs);
        }catch (SQLException e) {
            throw new DAOException("getMoviebyGenre() " + e.getMessage());
        } finally {
            try {
                closeConnections(rs, con, ps);
            } catch (SQLException e) {
                throw new DAOException("getMoviebyGenre() " + e.getMessage());
            }
        }
        return movies;
    }

    public ArrayList<Movie> getMoviesbyDirector(String dirname) throws DAOException{
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Movie> movies = new ArrayList<>();

        try{
            con = this.getConnection();
            String query = "SELECT * FROM MOVIES WHERE director like ?";
            ps = con.prepareStatement(query);
            ps.setString(1, dirname);
            rs = ps.executeQuery();

            movies = fillArrayList(rs);
        }catch (SQLException e) {
            throw new DAOException("getMoviebyDirector() " + e.getMessage());
        } finally {
            try {
                closeConnections(rs, con, ps);
            } catch (SQLException e) {
                throw new DAOException("getMoviebyDirector() " + e.getMessage());
            }
        }
        return movies;
    }

    public void addMovieToDatabase(String title, String genres, String director,
                                               String runtime, String plot, String rating, String format,
                                               String Year, String starring) throws  DAOException{
        Connection con = null;
        PreparedStatement ps = null;

        try{
            con = this.getConnection();
            String query = "INSERT INTO MOVIES (title, genre, director, runtime, plot, rating, " +
                    "format, Year, starring) values (?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(query);
            ps.setString(1, title);
            ps.setString(2, genres);
            ps.setString(3, director);
            ps.setString(4, runtime);
            ps.setString(5, plot);
            ps.setString(6, rating);
            ps.setString(7, format);
            ps.setString(8, Year);
            ps.setString(9, starring);
            ps.execute();

        }catch (SQLException e) {
            throw new DAOException("addMovieToDatabase() " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    this.closeConnection(con);
                }
            } catch (SQLException e) {
                throw new DAOException("addMovieToDatabase() " + e.getMessage());
            }
        }
    }

    public void DeleteMovieInDatabase(int movie_id) throws DAOException{
        Connection con = null;
        PreparedStatement ps = null;

        try{
            con = this.getConnection();
            String query = "DELETE FROM MOVIES WHERE id = ?";
            ps.setInt(1,movie_id);
            ps.execute();

        }catch (SQLException e) {
            throw new DAOException("DeleteMovieInDatabase() " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    this.closeConnection(con);
                }
            } catch (SQLException e) {
                throw new DAOException("DeleteMovieInDatabase() " + e.getMessage());
            }
        }
    }

    public ArrayList<Movie> fillArrayList(ResultSet rs) throws DAOException{
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

    public void closeConnections(ResultSet rs, Connection con, PreparedStatement ps) throws DAOException{
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                this.closeConnection(con);
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }
}
