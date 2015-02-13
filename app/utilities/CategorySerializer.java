package utilities;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Category;
import play.libs.Json;

import java.util.List;

public class CategorySerializer {
    /**
     * Serializes categories into a JSON map
     * @param rootCategories The root categories to be serialized
     * @return A JSON object of the categories
     */
    public static JsonNode serialize(List<Category> rootCategories) {
        ObjectNode root = Json.newObject();

        for (Category category : rootCategories) {
            ObjectNode child = root.putObject(String.valueOf(category.id));
            child.put("name", category.name);

            if (category.children != null && category.children.size() > 0) {
                JsonNode childsChildren = serialize(category.children);
                child.put("subcategories", childsChildren);
            }
        }

        return root;
    }
}
