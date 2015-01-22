package dao;

import models.User;

public interface UserDao {
    User findByEmail(String email);
    User create(String firstName, String lastName, String email, String password);
    User create(User user);
    void update(User user);
};
