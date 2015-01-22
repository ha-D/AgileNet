package models;

import dao.UserDao;
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
    public int id;

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

    @Transient
    private UserDao userDao;

    public User() {
        this(Dependencies.getUserDao());
    }

    public User(UserDao userDao) {
        creationDate = new Date();
        isActivated = false;
        isSuspended = false;

        this.userDao = userDao;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void assignRole(Role role) {
        assignRole(role, true);
    }

    public void assignRole(Role role, boolean commit) {
        roles.add(role);
        if (commit) {
            userDao.update(this);
        }
    }

    public void removeRole(Role role) {
        removeRole(role, true);
    }

    public void removeRole(Role role, boolean commit) {
        roles.remove(role);
        if (commit) {
            userDao.update(this);
        }
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    public boolean hasRole(String role) {
        for(Role r: this.roles) {
            if (r.name.equals(role)) {
                return true;
            }
        }
        return false;
    }

    public boolean isExpert(){
        return hasRole("expert");
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            User u = (User)o;
            if (email == null || u.email == null) {
                return false;
            }
            return email.equals(u.email);
        }
        return false;
    }

    public boolean authenticate(String password) {
        if (BCrypt.checkpw(password, this.password)) {
            return true;
        }
        return false;
    }
}
