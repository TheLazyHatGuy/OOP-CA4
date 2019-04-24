package com.ca4.DAO;

import com.ca4.Exceptions.DAOException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class MySQLDAO
{
    private String dbHost;
    private String dbUser;
    private String dbPass;
    private String dbName;

    public Connection getConnection() throws DAOException
    {
        getJSONVariables();

        String url = "jdbc:mysql://" + dbHost + ":3306/" + dbName;
        Connection connection = null;

        try{
            connection = DriverManager.getConnection(url, dbUser, dbPass);
        }
        catch(SQLException ex){
            System.out.println("Connection failed " + ex.getMessage());
            System.exit(1);
        }
        return connection;
    }

    private void getJSONVariables() {
        try (Scanner configFile = new Scanner(new BufferedReader(new FileReader("config.json")))) {
            StringBuilder json = new StringBuilder();

            while (configFile.hasNextLine()) {
                json.append(configFile.nextLine());
            }

            JSONObject config = new JSONObject(json.toString());

            dbHost = config.getString("DATABASE_HOST");
            dbUser = config.getString("DATABASE_USER");
            dbPass = config.getString("DATABASE_PASS");
            dbName = config.getString("DATABASE_NAME");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
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
