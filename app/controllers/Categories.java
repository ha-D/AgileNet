package controllers;

import actions.Ajax;
import models.Category;
import models.Dependencies;
import play.mvc.Result;
import utilities.CategorySerializer;

import java.util.List;

import static play.mvc.Results.ok;

public class Categories {
    public static Result list() {
        String cats = "[{id:1, name:'عنوان ۱', children:[{id:2, name:'عنوان ۲'},{id:3, name:'عنوان ۴', children:[{id:4, name:'عنوان ۵شس یب شس ب'},{id:5, name:'عنو سی ب سی سشیان ۲'}]},{id:6, name:'عنوان ۳'}]}]";
        return ok(views.html.categories.render(cats));
    }

    @Ajax
    public static Result listCategories() {
        List<Category> categories = Dependencies.getCategoryDao().findRootCategories();
        return ok(CategorySerializer.serialize(categories));
    }
}
