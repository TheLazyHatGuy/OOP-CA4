package com.ca4.DAO;

import com.ca4.DTO.User;
import com.ca4.Exceptions.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLUserDAO extends MySQLDAO implements UserDAOInterface
{
    public boolean registerUser(String email, String password) throws DAOException{
        Connection con = null;
        PreparedStatement ps = null;

        try{
            con = this.getConnection();
            String query = "INSERT INTO user (email, password) values(?,?)";
            ps = con.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            return ps.execute();
        }catch (SQLException e){
            throw new DAOException("registerUser() " + e.getMessage());
        }finally{
            try{
                this.closeConnection(con, ps);
            }catch(SQLException e){
                throw new DAOException("registerUser() " + e.getMessage());
            }
        }
    }

    public User loginUser (String email) throws DAOException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;

        try
        {
            con = this.getConnection();
            String query = "SELECT * FROM user WHERE email = ? LIMIT 1";
            ps = con.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();

            while (rs.next())
            {
                int id = rs.getInt("id");
                String password = rs.getString("password");
                user = new User(id, email, password);
            }
        }
        catch (SQLException e)
        {
            throw new DAOException("loginUser() " + e.getMessage());
        }
        finally
        {
            try
            {
                closeConnection(con, rs, ps);
            }
            catch (SQLException e)
            {
                throw new DAOException("loginUser() " + e.getMessage());
            }
        }
        return user;
    }

    public void deleteUser(int user_id) throws DAOException{
        Connection con = null;
        PreparedStatement ps = null;

        try{
            con = this.getConnection();
            String query = "DELETE FROM user WHERE id = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, user_id);
            ps.execute();
        }catch (SQLException e){
            throw new DAOException("deleteUser() " + e.getMessage());
        }finally{
            try{
                this.closeConnection(con, ps);
            }catch(SQLException e){
                throw new DAOException("deleteUser() " + e.getMessage());
            }
        }
    }

    public void updateUser(int id, String email, String password) throws DAOException{
        Connection con = null;
        PreparedStatement ps = null;

        try{
            con = this.getConnection();
            String query = "UPDATE user SET email = ?, password = ? where id = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            ps.setInt(3, id);
            ps.execute();
        }catch (SQLException e){
            throw new DAOException("updateUser() " + e.getMessage());
        }finally{
            try{
                this.closeConnection(con, ps);
            }catch(SQLException e){
                throw new DAOException("updateUser() " + e.getMessage());
            }
        }
    }
}
