package models;

import dao.RoleDao;
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

    private RoleDao roleDao;

    public Role(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public Role(String name, RoleDao roleDao) {
        this(roleDao);
        this.name = name;
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
