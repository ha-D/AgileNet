package controllers;

import actions.Ajax;
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

public class Categories {
    //TODO:you should use StringBuilder
    public static Result list() {
//        String cats = "[{id:1, name:'عنوان ۱', children:[{id:2, name:'عنوان ۲'},{id:3, name:'عنوان ۴', children:[{id:4, name:'عنوان ۵شس یب شس ب'},{id:5, name:'عنو سی ب سی سشیان ۲'}]},{id:6, name:'عنوان ۳'}]}]";
        String cats = "[";
        for(Category c: Dependencies.getCategoryDao().findRootCategories())
            cats+=c.getJson()+", ";
        cats+="]";
        return ok(views.html.categories.render(cats));
    }

    @Ajax
    public static Result removeCategory() {
        FormRequest request = formBody();
        int id = Integer.parseInt(request.getSingleElement("category"));
        Dependencies.getCategoryDao().deleteCategory(id);
        return ok();
    }

    @Ajax
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

    @Ajax
    public static Result listCategoriesAsMap() {
        return ok(getCategoryMap());
    }

    private static JsonNode getCategoryMap() {
        List<Category> categories = Dependencies.getCategoryDao().findRootCategories();
        return CategorySerializer.serialize(categories);
    }
}
