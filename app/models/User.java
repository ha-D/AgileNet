package models;

import dao.UserDao;
import play.data.validation.Constraints;
import org.mindrot.jbcrypt.BCrypt;
import utilities.Dependencies;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

import static play.mvc.Controller.session;

@Entity
public class User extends BaseModel<UserDao> {
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
    public Set<Role> roles;

    public User() {
        creationDate = new Date();
        isActivated = false;
        isSuspended = false;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Assigns a role to the user
     * @param role The role to assign to the user
     */
    public void assignRole(Role role) {
        assignRole(role, true);
    }

    /**
     * Assigns a role to the user
     * @param role The role to assign to the user
     * @param commit If true, the changes would be committed to the data store
     */
    public void assignRole(Role role, boolean commit) {
        roles.add(role);
        if (commit) {
            Dependencies.getUserDao().update(this);
        }
    }

    /**
     * Removes a role from the user
     * @param role The role to remove from the user
     */
    public void removeRole(Role role) {
        removeRole(role, true);
    }

    /**
     * Removes a role from the user
     * @param role The role to remove from the user
     * @param commit If true, the changes would be committed to the data store
     */
    public void removeRole(Role role, boolean commit) {
        roles.remove(role);
        if (commit) {
            Dependencies.getUserDao().update(this);
        }
    }

    /**
     * Checks whether the user has a role
     * @param role The role to check
     * @return true if the user has the role, false otherwise
     */
    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    /**
     * Checks whether the user has a role
     * @param role The role name to check
     * @return true if the user has the role, false otherwise
     */
    public boolean hasRole(String role) {
        for(Role r: this.roles) {
            if (r.name.equals(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return true if user has the 'expert' role, false otherwise
     */
    public boolean isExpert(){
        return hasRole("expert");
    }

    /**
     * @return true if user has the 'admin' role, false otherwise
     */
    public boolean isAdmin(){
        return hasRole("admin");
    }

    /**
     * Checks the user password against the given password
     * @param password The password to match against the user's password
     * @return true if the password matches, false otherwise
     */
    public boolean authenticate(String password) {
        if (BCrypt.checkpw(password, this.password)) {
            return true;
        }
        return false;
    }

    /**
     * @return The user's full name, i.e first name and last name seperated by a space
     */
    public String getFullName() {
        return firstName + " " + lastName;
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
}
