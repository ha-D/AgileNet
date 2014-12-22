package models;

import play.db.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@Entity
public class User extends Model {
    @Id
    public String id;
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String nationalId;
    public String contactPhone;
    public Date creationDate;
    public boolean isActivated;
    public boolean isSuspended;

    @OneToMany(mappedBy="user", cascade= CascadeType.ALL)
    public List<Permission> permissions;
}
