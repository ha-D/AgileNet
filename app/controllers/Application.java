package controllers;

import actions.Authorized;
import utilities.Dependencies;
import play.mvc.*;

import views.html.*;

import static play.data.Form.form;


public class Application extends Controller {
    public static Result index() {
        return ok(index.render());
    }
}

