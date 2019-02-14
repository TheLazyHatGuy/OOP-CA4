package com.ca4.DAO;

import java.sql.Connection;

public class MySQLDAO
{
    public Connection getConnection() throws DAOException
    {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/oopca4";
        String userName = "root";
        String password = "cQdynq4q8VOP4R9llL2A";

        Connection connection = null;
    }
}
