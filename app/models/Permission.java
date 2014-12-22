package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class Permission extends Model {
    public final static int PERMISSION_ADMIN = 1;
    public final static int PERMISSION_EXPERT = 2;

    @Id
    String id;
    int permissionType;

    @ManyToOne
    public User user;
}
