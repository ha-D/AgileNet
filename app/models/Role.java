package models;

import dao.RoleDao;
import play.db.ebean.Model;

import javax.persistence.*;


@Entity
public class Role extends BaseModel<RoleDao> {
    @Id
    public int id;
    @Column(nullable = false, unique = true)
    public String name;

    @Transient
    private RoleDao roleDao;

    public Role() {
        this(Dependencies.getRoleDao());
    }

    public Role(RoleDao roleDao) {
        super(roleDao);
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
