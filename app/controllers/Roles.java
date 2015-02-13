package controllers;

import actions.Ajax;
import actions.Authorized;
import models.Role;
import models.User;
import play.mvc.Result;
import utilities.FormRequest;
import static play.mvc.Results.ok;
import static utilities.FormRequest.formBody;

/**
 * Controllers for assigning roles to users
 */
public class Roles {
    /**
     * Add a role to a user
     * POST user: The id of the user to add the role to
     * POST role: The name of the role to add to the user
     *
     * Ajax Method
     * Authorization: Admin
     */
    @Ajax
    @Authorized({"admin"})
    public static Result addRole() {
        FormRequest request = formBody();
        User user = request.parseUser();
        Role role = request.parseRole();
        user.assignRole(role, true);

        Accounts.unsuspendUser();
        return ok();
    }

    /**
     * Remove a role from a user
     * POST user: The id of the user to remove the role from
     * POST role: The name of the role to remove from the user
     *
     * Ajax Method
     * Authorization: Admin
     */
    @Ajax
    @Authorized({"admin"})
    public static Result removeRole() {
        FormRequest request = formBody();
        User user = request.parseUser();
        Role role = request.parseRole();
        user.removeRole(role, true);

        Accounts.unsuspendUser();
        return ok();
    }
}
