package controllers;

import actions.Ajax;
import play.mvc.Result;
import utils.FormRequest;
import static play.mvc.Results.ok;
import static utils.FormRequest.formBody;

public class Categories {
    public static Result list() {
        String cats = "[{id:1, name:'عنوان ۱', children:[{id:2, name:'عنوان ۲'},{id:3, name:'عنوان ۴', children:[{id:4, name:'عنوان ۵شس یب شس ب'},{id:5, name:'عنو سی ب سی سشیان ۲'}]},{id:6, name:'عنوان ۳'}]}]";
        return ok(views.html.categories.render(cats));
    }
}
