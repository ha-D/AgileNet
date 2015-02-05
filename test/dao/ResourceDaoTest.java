package dao;

import com.avaje.ebean.Ebean;
import dao.impl.ResourceDaoImpl;
import models.*;
import org.junit.Test;
import play.libs.Yaml;
import testutils.BaseTest;

import java.util.HashSet;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class ResourceDaoTest extends BaseTest {
    @Test
    public void testResourceCreation() {
        List<Category> categoryList =(List) Yaml.load("test-data/categories.yml");
        List<User> userList =(List) Yaml.load("test-data/users.yml");
        Ebean.save(categoryList);
        Ebean.save(userList);

        ResourceDaoImpl resourceDao = new ResourceDaoImpl();

        User user = Ebean.find(User.class, 1);
        resourceDao.create(ResourceType.BOOK, "my resource", null, "resource without categories", user,"","","" );

        Resource resource = new Resource();
        resource.resourceType = ResourceType.ARTICLE;
        resource.name = "other resource";
        resource.owner = "some guy";
        resource.url = "http://amazon.com/agilebook";
        resource.categories = new HashSet<Category>();
        resource.categories.add(categoryList.get(0));
        resource.categories.add(categoryList.get(2));
        resource.user = user;
        resourceDao.create(resource);

        resource = resourceDao.findById(1);
        assertThat(resource.name).isEqualTo("my resource");
        assertThat(resource.resourceType).isEqualTo(ResourceType.BOOK);

        resource = resourceDao.findById(2);
        assertThat(resource.user).isEqualTo(user);
        assertThat(resource.owner).isEqualTo("some guy");
        assertThat(resource.resourceType).isEqualTo(ResourceType.ARTICLE);
        assertThat(resource.categories.size()).isEqualTo(2);
        assertTrue(resource.categories.contains(categoryList.get(0)));
        assertTrue(resource.categories.contains(categoryList.get(2)));
    }
}
