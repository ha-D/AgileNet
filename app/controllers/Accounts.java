package controllers;

import static play.mvc.Controller.session;
import static play.data.validation.Constraints.*;
import static utils.FormRequest.formBody;

import actions.Ajax;
import dao.UserDao;
import models.Dependencies;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.FormRequest;

import java.lang.String;
import java.lang.System;
import java.net.UnknownServiceException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


import actions.Authorized;

public class Accounts extends Controller {
    public static Result signup() {
        return ok(views.html.signUp.render(Form.form(SignupForm.class)));
    }

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

    @Ajax
    public static Result suspendUser() {
        FormRequest request = formBody();
        User user = request.parseUser();
        user.isSuspended = true;
        Dependencies.getUserDao().update(user);
        return ok();
    }

    @Ajax
    public static Result unsuspendUser() {
        FormRequest request = formBody();
        User user = request.parseUser();
        user.isSuspended = false;
        Dependencies.getUserDao().update(user);
        return ok();
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

    @Authorized({})
    public static Result profile() {
        String email = session().get("email");
        User user = Dependencies.getUserDao().findByEmail(email);
        Form<User> userForm = Form.form(User.class);
        userForm = userForm.fill(user);
        return ok(views.html.profile.render(userForm));
    }

    @Authorized({})
    public static Result updateProfile() {
        Form<User> form = Form.form(User.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(views.html.profile.render(form));
        } else {
            updateUser(form);
            System.out.println("new firstName is: "+ form.get().firstName);
            return redirect(routes.Application.index());
        }
    }

    private static void updateUser(Form<User> form) {
    }

    public static User getUser(Integer id) {
        UserDao userDao = Dependencies.getUserDao();
        User user = userDao.findById(id);
        return user;
    }
}
