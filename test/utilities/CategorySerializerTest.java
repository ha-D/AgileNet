package utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Category;
import org.junit.Test;

import javax.management.ObjectName;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CategorySerializerTest {

    @Test
    public void testSerialization() throws IOException {
        List<Category> rootList = new ArrayList<Category>();
        rootList.add(
            newCategory(1, "cat1",
                newCategory(11, "cat11",
                        newCategory(111, "cat111"),
                        newCategory(112, "cat112")),
                newCategory(12, "cat12"))
        );
        rootList.add(
            newCategory(2, "cat2",
                    newCategory(21, "cat21"))
        );
        rootList.add(
                newCategory(3, "cat3")
        );


        JsonNode json = CategorySerializer.serialize(rootList);

        assertJSON(json, "1", "cat1", 2);
        assertJSON(json, "1.11", "cat11", 2);
        assertJSON(json, "1.11.111", "cat111", 0);
        assertJSON(json, "1.11.112", "cat112", 0);
        assertJSON(json, "1.12", "cat12", 0);
        assertJSON(json, "2", "cat2", 1);
        assertJSON(json, "2.21", "cat21", 0);
        assertJSON(json, "3", "cat3", 0);
    }

    private void assertJSON(JsonNode json, String path, String val, Integer childNum) {
        String[] parts = path.split("[.]");
        for (int i = 0; i < parts.length; i++) {
            json = json.findValue(parts[i]);

            if (i == parts.length - 1) {
                if (val != null) {
                    assertEquals(val, json.findValue("name").asText());
                }
                if (childNum != null) {
                    json = json.findValue("subcategories");
                    if (childNum == 0) {
                        assertNull("Expected no children in node but found 'subcategories'", json);
                    } else {
                        assertEquals((int)childNum, json.size());
                    }
                }
                return;
            }

            json = json.findValue("subcategories");
            assertNotNull(json);
        }

    }

    private Category newCategory(int id, String name, Category... children) {
        Category category = new Category();
        category.id = id;
        category.name = name;
        category.children = Arrays.asList(children);
        return category;
    }

}
