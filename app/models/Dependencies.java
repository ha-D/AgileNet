package models;

import dao.*;

public class Dependencies {
    private static UserDao userDao;
    private static RoleDao roleDao;
    private static CategoryDao categoryDao;
    private static ResourceDao resourceDao;
    private static CommentDao commentDao;
    private static RateResourceDao rateResourceDao;

    public static void initialize(UserDao userDao, RoleDao roleDao, CategoryDao categoryDao, ResourceDao resourceDao, CommentDao commentDao, RateResourceDao rateResourceDao) {
        setUserDao(userDao);
        setRoleDao(roleDao);
        setCategoryDao(categoryDao);
        setResourceDao(resourceDao);
        setCommentDao(commentDao);
        setRateResourceDao(rateResourceDao);
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

    private static void setRateResourceDao(RateResourceDao rateResourceDao) {
        Dependencies.rateResourceDao = rateResourceDao;
    }

    public static RateResourceDao getRateResourceDao(){
        return rateResourceDao;
    }
    public static ResourceDao getResourceDao() {
        return resourceDao;
    }
    public static void setResourceDao(ResourceDao resourceDao) {
        Dependencies.resourceDao = resourceDao;
    }

    public static CommentDao getCommentDao() { return commentDao;}

    private static void setCommentDao(CommentDao commentDao) {
        Dependencies.commentDao=commentDao;
    }
}
