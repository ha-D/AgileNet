package controllers;

import actions.Ajax;
import actions.Authorized;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Category;
import play.libs.Json;
import utilities.Dependencies;
import play.mvc.Result;
import utilities.CategorySerializer;
import java.util.List;
import utilities.FormRequest;
import static play.mvc.Results.ok;
import static utilities.FormRequest.formBody;

/**
 * Controllers for viewing and modifying categories
 */
public class Categories {
    /**
     * JSON list of all categories
     *
     * Ajax Method
     */
    @Ajax
    public static Result list() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for(Category c: Dependencies.getCategoryDao().findRootCategories())
            builder.append(c.getJson()+", ");
        builder.append("]");
        return ok(views.html.categories.render(builder.toString()));
    }

    /**
     * Add a category
     * POST parent: id of parent category to add new category under
     * POST name: name of new category
     *
     * Ajax Method
     * Authorization: Expert, Admin
     */
    @Ajax
    @Authorized({"admin", "expert"})
    public static Result addCategory() {
        FormRequest request = formBody();
        Category cat;
        try {
            int parent = Integer.parseInt(request.getSingleElement("parent"));
            cat = Dependencies.getCategoryDao().create(request.getSingleElement("name"), parent);
        }catch (Exception e){
            cat = Dependencies.getCategoryDao().create(request.getSingleElement("name"));
        }

        ObjectNode json = Json.newObject();
        json.put("id", cat.id);
        json.put("categories", getCategoryMap());
        return ok(json);
    }

    /**
     * Remove a category
     * POST category: id of category to remove
     *
     * Ajax Method
     * Authorization: Expert, Admin
     */
    @Ajax
    @Authorized({"admin", "expert"})
    public static Result removeCategory() {
        FormRequest request = formBody();
        int id = Integer.parseInt(request.getSingleElement("category"));
        Dependencies.getCategoryDao().deleteCategory(id);
        return ok();
    }


    /**
     * JSON map of categories
     */
    @Ajax
    public static Result listCategoriesAsMap() {
        return ok(getCategoryMap());
    }

    private static JsonNode getCategoryMap() {
        List<Category> categories = Dependencies.getCategoryDao().findRootCategories();
        return CategorySerializer.serialize(categories);
    }
}
