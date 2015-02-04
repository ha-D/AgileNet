package dao;

import com.avaje.ebean.Ebean;
import models.Role;
import play.db.ebean.Model;

import java.util.List;

public class EBeanRoleDao implements RoleDao {
    public Role create(String name) {
        Role role = new Role(name);
        Ebean.save(role);
        return role;
    }

    @Override
    public Role create(Role object) {
        Ebean.save(object);
        return object;
    }

    public Role findByName(String name) {
        return find.where().eq("name", name).findUnique();
    }

    public int getRoleCount() {
        return find.findRowCount();
    }

    @Override
    public List<Role> getUserRoles(String userEmail) {
        return null;
    }

    @Override
    public Role findById(int id) {
        return find.where().eq("id", id).findUnique();
    }

    @Override
    public List<Role> findAll() {
        return find.all();
    }

    @Override
    public void update(Role object) {
        Ebean.save(object);
    }

    public Model.Finder<Integer, Role> find = new Model.Finder<Integer,Role>(
            Integer.class, Role.class
    );
}
