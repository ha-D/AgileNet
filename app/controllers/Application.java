package controllers;

import actions.Authorized;
import models.Resource;
import utilities.Dependencies;
import play.mvc.*;

import views.html.*;

import static play.data.Form.form;
import static utilities.UserUtils.sessionUser;


public class Application extends Controller {
    /**
     * Render index page
     */
    public static Result index() {
        if (sessionUser() == null) {
            return ok(index.render());
        } else {
            return Resources.searchPage();
        }
    }
}

