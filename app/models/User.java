package models;

import play.data.validation.Constraints;
import org.mindrot.jbcrypt.BCrypt;
import play.db.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
public class User extends Model {
    @Id
    public String id;

    @Column(nullable = false)
    public String firstName;
    @Column(nullable = false)
    public String lastName;

    @Constraints.Email
    @Column(unique = true, nullable = false)
    public String email;

    @Column(nullable = false)
    public String password;

    @Column(unique = true)
    public String nationalId;
    public String contactPhone;

    @Column(nullable = false)
    public Date creationDate;

    public boolean isActivated;
    public boolean isSuspended;

    @ManyToMany(cascade= CascadeType.ALL)
    public List<Role> roles;

    public User() {
        creationDate = new Date();
        isActivated = false;
        isSuspended = false;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }


    public static User create(String firstName, String lastName, String email, String password) {
        User user = new User();
        user.firstName = firstName;
        user.lastName = lastName;
        user.email = email;
        user.setPassword(password);
        user.save();
        return user;
    }

    public void assignRole(Role role) {
        assignRole(role, true);
    }

    public void assignRole(Role role, boolean commit) {
        roles.add(role);
        if (commit) {
            this.save();
        }
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    public static Finder<String,User> find = new Finder<String,User>(
            String.class, User.class
    );

    public static User findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }

    public static User authenticate(String email, String password) {
        User user = find.where().eq("email", email).findUnique();
        if (user != null && BCrypt.checkpw(password, user.password)) {
            return user;
        }
        return null;

    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Role) {
            User u = (User)o;
            if (email == null || u.email == null) {
                return false;
            }
            return email.equals(u.email);
        }
        return false;
    }

}
