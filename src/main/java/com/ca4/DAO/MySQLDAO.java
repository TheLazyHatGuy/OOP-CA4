package com.ca4.DAO;

import com.ca4.Exceptions.DAOException;

import java.sql.*;

public class MySQLDAO
{
    public Connection getConnection() throws DAOException
    {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/oopca4";
        String userName = "root";
        String password = "cQdynq4q8VOP4R9llL2A";

        Connection connection = null;

        try{
            connection = DriverManager.getConnection(url, userName, password);
        }
        catch(SQLException ex){
            System.out.println("Connection failed " + ex.getMessage());
            System.exit(1);
        }
        return connection;
    }

    public void closeConnection(Connection con) throws DAOException{
        try {
            if (con != null) {
                con.close();
                con = null;
            }
        }
        catch(SQLException e){
            System.out.println("Failed to free the connection: " + e.getMessage());
            System.exit(1);
        }
    }

    public void closeConnection(Connection con, PreparedStatement ps) throws DAOException
    {
        try
        {
            if (ps != null)
            {
                ps.close();
            }

            if (con !=  null)
            {
                this.closeConnection(con);
            }
        }
        catch(SQLException e)
        {
            System.out.println("Failed to free the connection: " + e.getMessage());
            System.exit(1);
        }
    }

    public void closeConnection(Connection con, ResultSet rs, PreparedStatement ps) throws DAOException
    {
        try
        {
            if (rs != null)
            {
                rs.close();
            }

            if (ps != null)
            {
                ps.close();
            }

            if (con !=  null)
            {
                this.closeConnection(con);
            }
        }
        catch(SQLException e)
        {
            System.out.println("Failed to free the connection: " + e.getMessage());
            System.exit(1);
        }
    }
}
