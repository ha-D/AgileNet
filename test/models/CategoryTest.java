package models;

import org.junit.Test;
import testutils.BaseTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CategoryTest extends BaseTest {
    @Test
    public void testCategoryEquals() {
        Category category1 = new Category(null);
        category1.name = "yek";

        Category category2 = new Category(null);
        category2.name = "yek";

        Category category3 = new Category(null);
        category3.name = "se";

        Category category4 = new Category(null);

        assertTrue("Categories are equal by name", category1.equals(category2));
        assertTrue("Categories are equal by name", category2.equals(category1));
        assertFalse("Categories are equal by name", category1.equals(category3));
        assertFalse("Categories are equal by name", category3.equals(category1));
        assertFalse("Categories are equal by name", category1.equals(category4));
    }
}
