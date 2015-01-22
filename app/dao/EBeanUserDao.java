package dao;

import models.User;
import play.db.ebean.Model;

public class EBeanUserDao implements UserDao {
    @Override
    public User findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }

    @Override
    public  User create(String firstName, String lastName, String email, String password) {
        User user = new User(this);
        user.firstName = firstName;
        user.lastName = lastName;
        user.email = email;
        user.setPassword(password);
        user.save();
        return user;
    }

    @Override
    public User create(User user) {
        user.save();
        return user;
    }

    @Override
    public void update(User user) {
        user.save();
    }

    public static Model.Finder<String,User> find = new Model.Finder<String,User>(
            String.class, User.class
    );
}
