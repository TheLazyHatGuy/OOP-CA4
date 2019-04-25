package com.ca4.DAO;

import com.ca4.DTO.User;
import com.ca4.Exceptions.DAOException;

import java.sql.*;

public class MySQLUserDAO extends MySQLDAO implements UserDAOInterface
{
    @Override
    public boolean isUserAlreadyRegistered(String email) throws DAOException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = this.getConnection();
            String query = "SELECT * FROM users WHERE email = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, email);

            int rowCount = ps.executeUpdate();
            return rowCount >= 1;
        } catch (SQLException e) {
            throw new DAOException("isUserAlreadyRegistered() " + e.getMessage());
        } finally {
            try {
                this.closeConnection(con, ps);
            } catch (SQLException e) {
                throw new DAOException("isUserAlreadyRegistered().finally " + e.getMessage());
            }
        }
    }

    public int registerUser(String email, String password) throws DAOException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            con = this.getConnection();
            String query = "INSERT INTO users (email, password) values(?,?)";
            ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, email);
            ps.setString(2, password);

            int rowCount = ps.executeUpdate();

            //Taken from - https://stackoverflow.com/questions/1915166/how-to-get-the-insert-id-in-jdbc
            if (rowCount == 0) {
                throw new DAOException("Creating user failed, no rows affected.");
            } else {
                rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new DAOException("Creating user failed, no ID obtained.");
                }
            }
        }catch (SQLException e){
            throw new DAOException("registerUser() " + e.getMessage());
        }finally{
            try{
                this.closeConnection(con, rs, ps);
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
            String query = "SELECT * FROM users WHERE email = ? LIMIT 1";
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

    public boolean deleteUser(int user_id) throws DAOException{
        Connection con = null;
        PreparedStatement ps = null;

        try{
            con = this.getConnection();
            String query = "DELETE FROM users WHERE id = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, user_id);

            int rowCount = ps.executeUpdate();
            return rowCount >= 1;
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

    public boolean updateUser(int id, String email, String password) throws DAOException{
        Connection con = null;
        PreparedStatement ps = null;

        try{
            con = this.getConnection();
            String query = "UPDATE users SET email = ?, password = ? where id = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            ps.setInt(3, id);

            int rowCount = ps.executeUpdate();
            return rowCount >= 1;
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
