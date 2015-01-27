package models;

import dao.CategoryDao;
import dao.EBeanCategoryDao;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;
import testutils.BaseTest;

import static junit.framework.Assert.*;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.fail;

/**
 * Created by Atiye on 1/24/2015.
 */
public class CategoryDaoTest extends BaseTest {
    @Test
    public void testCategoryCreation(){
        CategoryDao categorydao = new EBeanCategoryDao();

        Category category = new Category(categorydao);
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
}
