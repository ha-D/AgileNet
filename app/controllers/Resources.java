package controllers;

import play.mvc.Result;
import play.twirl.api.Html;

import static play.mvc.Results.ok;

public class Resources {
    public static Result searchPage() {
        return ok(views.html.resourceSearch.render());
    }
}
