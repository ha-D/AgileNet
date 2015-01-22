package models;

import dao.RoleDao;
import dao.UserDao;

public class Dependencies {
    private static UserDao userDao;
    private static RoleDao roleDao;

    public static void initialize(UserDao userDao, RoleDao roleDao) {
        setUserDao(userDao);
        setRoleDao(roleDao);
    }

    public static UserDao getUserDao() {
        return userDao;
    }

    public static void setUserDao(UserDao userDao) {
        Dependencies.userDao = userDao;
    }

    public static RoleDao getRoleDao() {
        return roleDao;
    }

    public static void setRoleDao(RoleDao roleDao) {
        Dependencies.roleDao = roleDao;
    }
}
