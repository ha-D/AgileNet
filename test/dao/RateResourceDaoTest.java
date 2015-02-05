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

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class RateResourceDaoTest extends BaseTest {
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
    }
    @Test
    public void testRateResourceCreation() {

        User user = Dependencies.getUserDao().findById(1);
        Resource resource=Dependencies.getResourceDao().create(ResourceType.BOOK, "my resource", null, "resource without categories", user ,"","","");

        RateResource rateResource = new RateResource();
        rateResource.rate = 7;
        rateResource.resource = resource;

        RateResource rr = Dependencies.getRateResourceDao().create(rateResource);
        assertThat(rr).isNull();


        rateResource.user = user;
        rr = Dependencies.getRateResourceDao().create(rateResource);
        rr = Dependencies.getRateResourceDao().findById(rr.id);
        assertThat(rr.rate).isEqualTo(5);
        assertThat(rr.user).isEqualTo(user);
        assertThat(rr.resource).isEqualTo(resource);

    }
}
