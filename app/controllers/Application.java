package controllers;

import play.*;
import play.data.Form;
import play.mvc.*;

import views.html.*;

import static play.data.Form.form;


public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
}
