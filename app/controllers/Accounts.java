package controllers;

import static play.data.validation.Constraints.*;

import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.login;

public class Accounts extends Controller {

    public static Result signup() {
        Form<SignupForm> form = Form.form(SignupForm.class).bindFromRequest();

        if (form.hasErrors()) {
            return badRequest("Signup Form");
        } else {
            User user = new User();
            user.firstName = form.get().firstName;
            user.lastName = form.get().lastName;
            user.email = form.get().email;
            user.contactPhone = form.get().contactPhone;
            user.nationalId = form.get().nationalId;
            user.setPassword(form.get().password);
            user.save();
            return redirect(routes.Application.index());
        }
    }

    public static Result login() {
        Form<LoginForm> form = Form.form(LoginForm.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest("Login Form");
        } else {
            session().clear();
            session().put("email", form.get().email);
            return redirect(routes.Application.index());
        }
    }

    public static class SignupForm {
        @Required
        public String firstName;
        @Required
        public String lastName;
        @Required
        @Email
        public String email;
        @Required
        public String password;
        @Required
        public String passwordConfirm;
        public String nationalId;
        public String contactPhone;

        public String validate() {
            User user = User.findByEmail(email);
            if (user != null) {
                return "A user already exists with that email address";
            }
            if (!password.equals(passwordConfirm)) {
                return "Passwords do not match";
            }
            return null;
        }
    }

    public static class LoginForm {
        @Required
        public String email;
        @Required
        public String password;

        public String validate() {
            User user = User.authenticate(email, password);
            if (user == null) {
                return "The Email/Password is not valid";
            }
            return null;
        }
    }
}
