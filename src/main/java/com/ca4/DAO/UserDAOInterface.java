package com.ca4.DAO;

import com.ca4.DTO.User;
import com.ca4.Exceptions.DAOException;

public interface UserDAOInterface
{
    boolean registerUser(String email, String password) throws DAOException;
    User loginUser(String email) throws DAOException;
    boolean deleteUser(int user_id) throws DAOException;
    boolean updateUser(int id, String email, String password) throws DAOException;
}
