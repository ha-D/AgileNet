package controllers;

import static play.mvc.Controller.session;
import static play.data.validation.Constraints.*;
import static utilities.FormRequest.formBody;

import actions.Ajax;
import dao.UserDao;
import models.Comment;
import utilities.Dependencies;
import models.User;
import models.Category;
import models.Resource;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utilities.FormRequest;
import java.util.List;

import java.lang.String;


import actions.Authorized;

/**
 * Controllers for account management and user authentication
 */
public class Accounts extends Controller {
    /**
     * Render Signup form html
     */
    public static Result signup() {
        return ok(views.html.signUp.render(Form.form(SignupForm.class)));
    }

    /**
     * POST submit for sign up form
     * post parameter partial Determines if the submitted form was a complete form or a partial one (from the index page)
     */
    public static Result signupSubmit() {
        Form<SignupForm> form = Form.form(SignupForm.class).bindFromRequest();

        String[] partial = request().body().asFormUrlEncoded().get("partial");
        if (partial != null) {
            SignupForm templateForm = new SignupForm();
            templateForm.email = form.data().get("email");

            templateForm.password = form.data().get("password");
            return ok(views.html.signUp.render(Form.form(SignupForm.class).fill(templateForm)));
        }

        if (form.hasErrors()) {
            return badRequest(views.html.signUp.render(form));
        } else {
            User user = new User();
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

    /**
     *  Render the login form html page
     */
    public static Result login() {
        return ok(views.html.login.render(Form.form(LoginForm.class)));
    }

    /**
     * POST submit for the login form
     */
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

    /**
     * Logout the currently logged in user and redirect to index page
     */
    public static Result logout() {
        session().clear();
        return redirect(routes.Application.index());
    }

    /**
     * Suspend a user
     * POST user: the id of the user to suspend
     *
     * Ajax Method
     * Authorization: Admin
     */
    @Ajax
    @Authorized({"admin"})
    public static Result suspendUser() {
        FormRequest request = formBody();
        User user = request.parseUser();
        user.isSuspended = true;
        Dependencies.getUserDao().update(user);
        return ok();
    }

    /**
     * Remove a user suspension
     * POST user: the id of the user to remove suspension for
     *
     * Ajax Method
     * Authorization: Admin
     */
    @Ajax
    @Authorized({"admin"})
    public static Result unsuspendUser() {
        FormRequest request = formBody();
        User user = request.parseUser();
        user.isSuspended = false;
        Dependencies.getUserDao().update(user);
        return ok();
    }

    /**
     * Render settings page html
     *
     * Authorization: User
     */
    @Authorized({})
    public static Result settings() {
        String email = session().get("email");
        User user = Dependencies.getUserDao().findByEmail(email);
        Form<User> userForm = Form.form(User.class);
        userForm = userForm.fill(user);
        List<User> users = Dependencies.getUserDao().findAll();
        String cats=Category.getAllJson();
        Form<Resource> resourceForm = Form.form(Resource.class);
        return ok(views.html.settings.render(user, userForm, users, cats, resourceForm));
    }

    /**
     * POST submit for update profile form
     *
     * Authorization: User
     */
    @Authorized({})
    public static Result updateProfile() {
        Form<User> form = Form.form(User.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(views.html.settings.render(Dependencies.getUserDao().findByEmail(session().get("email")), form, Dependencies.getUserDao().findAll(), Category.getAllJson(), Form.form(Resource.class)));
        } else {
            updateUser(form);
            return redirect(routes.Accounts.settings()+"#profile");
        }
    }

    private static void updateUser(Form<User> form) {
        User user = Dependencies.getUserDao().findByEmail(session().get("email"));
        user.firstName = form.get().firstName;
        user.lastName = form.get().lastName;
        user.nationalId = form.get().nationalId;
        user.contactPhone = form.get().contactPhone;
        Dependencies.getUserDao().update(user);
    }

    /**
     * SignUp Form
     */
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

    /**
     * Login Form
     */
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
