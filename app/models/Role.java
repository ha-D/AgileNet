package models;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class Role extends Model {
    @Id
    public String id;
    @Column(nullable = false, unique = true)
    public String name;

    public Role() {
    }
    public Role(String name) {
        this.name = name;
    }

    public static Role create(String name) {
        Role role = new Role(name);
        role.save();
        return role;
    }

    public static Finder<String,Role> find = new Finder<String,Role>(
            String.class, Role.class
    );

    public static Role findByName(String name) {
        return find.where().eq("name", name).findUnique();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Role) {
            Role r = (Role)o;
            if (name == null || r.name == null) {
                return false;
            }
            return name.equals(r.name);
        }
        return false;
    }
}
