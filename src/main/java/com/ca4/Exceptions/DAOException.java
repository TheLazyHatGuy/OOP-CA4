package com.ca4.Exceptions;

import java.sql.SQLException;

public class DAOException extends SQLException {
    public DAOException() {
    }

    public DAOException(String eMessage){
        super(eMessage);
    }
}
