package dao;

import models.Role;

import java.util.List;

public interface RoleDao {
    Role create(String name);
    Role findByName(String name);
    int getRoleCount();
    List<Role> getUserRoles(String userEmail);
}
