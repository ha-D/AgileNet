package dao;

import models.Role;

public interface RoleDao {
    Role create(String name);
    Role findByName(String name);
    int getRoleCount();
}
