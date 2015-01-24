package controllers;

import actions.Ajax;
import models.Role;
import models.User;
import play.mvc.Result;
import utils.FormRequest;
import static play.mvc.Results.ok;
import static utils.FormRequest.formBody;

public class Roles {
    @Ajax
    public static Result addRole() {
        FormRequest request = formBody();
        User user = request.parseUser();
        Role role = request.parseRole();
        user.assignRole(role, true);

        Accounts.unsuspendUser();
        return ok();
    }

    @Ajax
    public static Result removeRole() {
        FormRequest request = formBody();
        User user = request.parseUser();
        Role role = request.parseRole();
        user.removeRole(role, true);

        Accounts.unsuspendUser();
        return ok();
    }
}
