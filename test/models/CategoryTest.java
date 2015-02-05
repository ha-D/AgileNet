package models;

import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CategoryTest {
    @Test
    public void testCategoryEquals() {
        Category category1 = new Category();
        category1.name = "yek";

        Category category2 = new Category();
        category2.name = "yek";

        Category category3 = new Category();
        category3.name = "se";

        Category category4 = new Category();

        assertTrue("Categories are equal by name", category1.equals(category2));
        assertTrue("Categories are equal by name", category2.equals(category1));
        assertFalse("Categories are equal by name", category1.equals(category3));
        assertFalse("Categories are equal by name", category3.equals(category1));
        assertFalse("Categories are equal by name", category1.equals(category4));
    }

    @Test
    public void testFindDescendants() {
        Category cat1 = newCategory(1, "cat1");
        Category cat11 = newCategory(11, "cat11");
        Category cat12 = newCategory(12, "cat12");
        cat1.children.add(cat11);
        cat1.children.add(cat12);

        Category cat111 = newCategory(111, "cat111");
        Category cat112 = newCategory(112, "cat112");
        cat11.children.add(cat111);
        cat11.children.add(cat112);

        List<Category> descendants = cat1.getDescendants();

        assertEquals(4, descendants.size());
        assertTrue(descendants.contains(cat11));
        assertTrue(descendants.contains(cat12));
        assertTrue(descendants.contains(cat111));
        assertTrue(descendants.contains(cat112));
    }

    private Category newCategory(int id, String name) {
        Category category = new Category();
        category.id = id;
        category.name = name;
        return category;
    }
}
