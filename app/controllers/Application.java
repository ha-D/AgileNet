package controllers;

import actions.Authorized;
import models.Dependencies;
import play.mvc.*;

import views.html.*;

import static play.data.Form.form;


public class Application extends Controller {

    public static Result index() {
        return ok(index.render());
    }

    @Authorized({})
    public static Result users() {
        return ok(users.render(Dependencies.getUserDao().findAll()));
    }

}

