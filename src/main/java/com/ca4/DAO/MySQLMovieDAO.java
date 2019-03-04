package com.ca4.DAO;

import com.ca4.DTO.Movie;
import com.ca4.Exceptions.DAOException;

import java.sql.*;
import java.util.ArrayList;

public class MySQLMovieDAO extends MySQLDAO implements MovieDAOInterface{
    public ArrayList<Movie> getAllMovies(){
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Movie> movies = new ArrayList<>();

        try{
            con = this.getConnection();
            String query = "SELECT * FROM MOVIES";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while(rs.next()){
                int movieId = rs.getInt("ID");
                String title = rs.getString("title");
                String genre = rs.getString("genre");
                String director = rs.getString("director");
                String runtime = rs.getString("plot");
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
                movies.add(m);
            }
        }catch (SQLException e){
            throw new DAOException("getAllMovies() " + e.getMessage());
        }finally {
            try{
                if(rs != null){
                    rs.close();
                }
                if(ps != null){
                    ps.close();
                }
                if(con != null){
                    this.closeConnection(con);
                }
            }catch (SQLException e){
                throw new DAOException("getAllMovies() " + e.getMessage());
            }
        }
        return movies;
    }
}
