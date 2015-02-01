package dao;

import com.avaje.ebean.Ebean;
import models.Category;
import models.Resource;
import models.ResourceType;
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
        Ebean.save(categoryList);

        EBeanResourceDao resourceDao = new EBeanResourceDao();

        resourceDao.create(ResourceType.BOOK, "my resource", null, "resource without categories");

        Resource resource = new Resource();
        resource.resourceType = ResourceType.ARTICLE;
        resource.name = "other resource";
        resource.owner = "some guy";
        resource.url = "http://amazon.com/agilebook";
        resource.categories = new HashSet<Category>();
        resource.categories.add(categoryList.get(0));
        resource.categories.add(categoryList.get(2));
        resourceDao.create(resource);

        resource = resourceDao.findById(1);
        assertThat(resource.name).isEqualTo("my resource");
        assertThat(resource.resourceType).isEqualTo(ResourceType.BOOK);

        resource = resourceDao.findById(2);
        assertThat(resource.owner).isEqualTo("some guy");
        assertThat(resource.resourceType).isEqualTo(ResourceType.ARTICLE);
        assertThat(resource.categories.size()).isEqualTo(2);
        assertTrue(resource.categories.contains(categoryList.get(0)));
        assertTrue(resource.categories.contains(categoryList.get(2)));
    }
}
