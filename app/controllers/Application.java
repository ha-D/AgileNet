package controllers;

import models.Dependencies;
import play.*;
import play.data.Form;
import play.mvc.*;

import views.html.*;

import static play.data.Form.form;
import models.User;


public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result users() {
        return ok(users.render(Dependencies.getUserDao().getAllUsers()));
    }

}

