package com.ca4.DAO;

import com.ca4.Exceptions.DAOException;

public interface UserDAOInterface {
    void registerUser(String email, String password) throws DAOException;

    void deleteUser(int user_id) throws DAOException;

    void updateUser(int id, String email, String password) throws DAOException;
}
