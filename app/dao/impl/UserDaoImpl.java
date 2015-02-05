package dao.impl;

import com.avaje.ebean.Ebean;
import dao.UserDao;
import models.User;
import play.db.ebean.Model;

import java.util.List;

public class UserDaoImpl implements dao.UserDao {
    @Override
    public User findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }

    @Override
    public User findById(int id) {
        return find.where().eq("id", id).findUnique();
    }

    @Override
    public  User create(String firstName, String lastName, String email, String password) {
        User user = new User();
        user.firstName = firstName;
        user.lastName = lastName;
        user.email = email;
        user.setPassword(password);
        Ebean.save(user);
        return user;
    }

    @Override
    public User create(User user) {
        Ebean.save(user);
        return user;
    }

    @Override
    public void update(User user) {
        Ebean.save(user);
    }

    @Override
    public List<User> findAll(){
        return find.all();
    }

    public Model.Finder<Integer, User> find = new Model.Finder<Integer, User>(
            Integer.class, User.class
    );
}
