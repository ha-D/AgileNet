package controllers;

import static play.data.validation.Constraints.*;

import dao.UserDao;
import models.Dependencies;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Accounts extends Controller {
    public static Result signup() {
        return ok(views.html.signUp.render(Form.form(SignupForm.class)));
    }

    public static Result signupSubmit() {
        Form<SignupForm> form = Form.form(SignupForm.class).bindFromRequest();

        if (form.hasErrors()) {
            return badRequest(views.html.signUp.render(form));
        } else {
            User user = new User(Dependencies.getUserDao());
            user.firstName = form.get().firstName;
            user.lastName = form.get().lastName;
            user.email = form.get().email;
            user.contactPhone = form.get().contactPhone;
            user.nationalId = form.get().nationalID;
            user.setPassword(form.get().password);
            Dependencies.getUserDao().create(user);
            return redirect(routes.Application.index());
        }
    }

    public static Result login() {
        return ok(views.html.login.render(Form.form(LoginForm.class)));
    }

    public static Result loginSubmit() {
        Form<LoginForm> form = Form.form(LoginForm.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(views.html.login.render(form));
        } else {
            session().clear();
            session().put("email", form.get().email);
            return redirect(routes.Application.index());
        }
    }

    public static Result logout() {
        session().clear();
        return redirect(routes.Application.index());
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
        public String nationalID;
        public String contactPhone;

        public String validate() {
            UserDao userDao = Dependencies.getUserDao();
            User user = userDao.findByEmail(email);
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
            UserDao userDao = Dependencies.getUserDao();
            User user = userDao.findByEmail(email);
            if (user == null || !user.authenticate(password)) {
                return "The Email/Password is not valid";
            }
            return null;
        }
    }
}
