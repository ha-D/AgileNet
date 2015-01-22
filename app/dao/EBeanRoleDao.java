package dao;

import models.Role;
import play.db.ebean.Model;

public class EBeanRoleDao implements RoleDao {
    public Role create(String name) {
        Role role = new Role(name, this);
        role.save();
        return role;
    }

    public Role findByName(String name) {
        return find.where().eq("name", name).findUnique();
    }

    public int getRoleCount() {
        return find.findRowCount();
    }

    public static Model.Finder<String, Role> find = new Model.Finder<String,Role>(
            String.class, Role.class
    );
}
