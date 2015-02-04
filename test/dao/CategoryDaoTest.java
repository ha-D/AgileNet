package dao;

import com.avaje.ebean.Ebean;
import models.Category;
import utilities.Dependencies;
import org.junit.Test;
import play.libs.Yaml;
import testutils.BaseTest;

import java.util.List;

import static junit.framework.TestCase.*;
import static junit.framework.TestCase.assertTrue;

public class CategoryDaoTest extends BaseTest {
    @Test
    public void testCategoryCreation(){
        CategoryDao categorydao = new EBeanCategoryDao();
        Dependencies.setCategoryDao(categorydao);

        Category category = new Category();
        Category parent;

        try {
            category.save();
            fail("Category should not be saved without name");
        } catch (Exception e) {
        }

        parent = categorydao.create("parent");
        assertNotNull("user should have id after being saved", parent.id);
        Category found_parent = categorydao.findById(parent.id);
        assertNotNull("user should have been saved", found_parent);
        assertEquals("parent", found_parent.name);
        assertEquals(null, found_parent.parent);
        category = categorydao.create("child", parent.id);
        Category found_category = categorydao.findById(category.id);
        assertEquals(parent, found_category.parent);
        assertTrue(categorydao.findById(parent.id).children.contains(found_category));

        categorydao.deleteCategory(parent.id);
        assertNull("parent should be deleted",categorydao.findById(parent.id));
        assertNull("child should be deleted", categorydao.findById(category.id));
    }

    @Test
    public void testFindRootCategories() {
        CategoryDao categoryDao = new EBeanCategoryDao();
        Dependencies.setCategoryDao(categoryDao);

        List<Category> categoryList =(List) Yaml.load("test-data/categories.yml");
        Ebean.save(categoryList);

        List<Category> rootCategories = categoryDao.findRootCategories();

        assertNotSame(categoryList.size(), rootCategories.size());

        for(Category category : categoryList) {
            if (category.parent == null) {
                assertTrue("A category with no parents should be retrieved in root categories",
                        rootCategories.contains(category));
            }
        }

        for(Category category : rootCategories) {
                assertNull("A category with parents should not be retrieved in root categories", category.parent);
        }
    }
}
