package dao;

import dao.impl.CategoryDaoImpl;
import dao.impl.CommentDaoImpl;
import dao.impl.RateResourceDaoImpl;
import dao.impl.ResourceDaoImpl;
import dao.impl.RoleDaoImpl;
import dao.impl.UserDaoImpl;
import models.*;
import org.junit.Before;
import org.junit.Test;
import play.libs.Yaml;
import testutils.BaseTest;
import utilities.Dependencies;

import static org.junit.Assert.*;

import static org.fest.assertions.Assertions.assertThat;


import java.util.List;

public class CommentDaoImplTest extends BaseTest{
    @Before
    public void before(){
        List<Category> categoryList =(List) Yaml.load("test-data/categories.yml");
        List<User> userList =(List) Yaml.load("test-data/users.yml");

        Dependencies.initialize(new UserDaoImpl(), new RoleDaoImpl(), new CategoryDaoImpl(), new ResourceDaoImpl(), new CommentDaoImpl(), new RateResourceDaoImpl());

        for (User user : userList) {
            Dependencies.getUserDao().create(user);
        }
        for (Category category : categoryList) {
            Dependencies.getCategoryDao().create(category);
        }

        Dependencies.getResourceDao().create(ResourceType.ARTICLE, "salam", null, "", Dependencies.getUserDao().findById(1), "","","");
    }

    @Test
    public void CommentCreationTest(){

        Resource resource = Dependencies.getResourceDao().findById(1);
        User user = Dependencies.getUserDao().findById(1);
        Comment comment = Dependencies.getCommentDao().create(user, "salam", resource);

        assertThat(comment.user).isEqualTo(user);
        assertThat(comment.body).isEqualTo("salam");
        assertThat(comment.parResource).isEqualTo(resource);
        assertNull(comment.parComment);

        Comment comment2 = Dependencies.getCommentDao().create(user, "salam2", comment);
        comment2 = Dependencies.getCommentDao().findById(comment2.id);
        assertThat(comment2.user).isEqualTo(user);
        assertThat(comment2.body).isEqualTo("salam2");
        assertThat(comment2.parComment).isEqualTo(comment);
        assertNull(comment2.parResource);

    }
}
