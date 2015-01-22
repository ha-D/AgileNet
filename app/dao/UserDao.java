package dao;

import models.User;

import java.util.List;

public interface UserDao {
    User findByEmail(String email);
    User create(String firstName, String lastName, String email, String password);
    User create(User user);
    void update(User user);
    List<User> getAllUsers();
};
