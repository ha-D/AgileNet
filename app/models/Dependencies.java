package models;

import dao.CategoryDao;
import dao.ResourceDao;
import dao.RoleDao;
import dao.UserDao;

public class Dependencies {
    private static UserDao userDao;
    private static RoleDao roleDao;
    private static CategoryDao categoryDao;
    private static ResourceDao resourceDao;

    public static void initialize(UserDao userDao, RoleDao roleDao, CategoryDao categoryDao, ResourceDao resourceDao) {
        setUserDao(userDao);
        setRoleDao(roleDao);
        setCategoryDao(categoryDao);
        setResourceDao(resourceDao);
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

    public static void setCategoryDao(CategoryDao categoryDao){
        Dependencies.categoryDao = categoryDao;
    }

    public static CategoryDao getCategoryDao(){
        return categoryDao;
    }

    public static ResourceDao getResourceDao() {
        return resourceDao;
    }

    public static void setResourceDao(ResourceDao resourceDao) {
        Dependencies.resourceDao = resourceDao;
    }

}
