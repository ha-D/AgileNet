package utils;

import models.Dependencies;
import models.Role;
import models.User;

import java.util.Map;

import static play.mvc.Controller.request;

public class FormRequest {
    private Map<String, String[]> body;

    public FormRequest(Map<String, String[]> body) {
        this.body = body;
    }

    private String getSingleElement(String tokenName) {
        String[] parts = body.get(tokenName);
        if (parts == null || parts.length == 0 || parts[0] == null || parts[0].isEmpty()) {
            throw new RequestParseException("Missing parameter [" + tokenName + "]");
        } else if (parts.length > 1) {
            throw new RequestParseException(
                    "Expected single argument for parameter [" + tokenName + "] but got " + parts.length);
        }
        return parts[0];
    }

    public User parseUser(String tokenName) {
        String userString = getSingleElement(tokenName);
        int userId = 0;

        try {
            userId = Integer.parseInt(userString);
        } catch (NumberFormatException e) {
            throw new RequestParseException("Invalid user id");
        }

        User user = Dependencies.getUserDao().findById(userId);
        if (user == null) {
            throw new RequestParseException("No user with id " + userString);
        }

        return user;
    }

    public User parseUser() {
        return parseUser("user");
    }

    public Role parseRole(String tokenName) {
        String roleString = getSingleElement(tokenName);

        if (roleString == null || roleString.isEmpty()) {
            throw new RequestParseException("Missing parameter [role]");
        }
        Role role = Dependencies.getRoleDao().findByName(roleString);
        if (role == null) {
            throw new RequestParseException("No such role: " + roleString);
        }
        return role;
    }

    public Role parseRole() {
        return parseRole("role");
    }

    public static FormRequest formBody() {
        return new FormRequest(request().body().asFormUrlEncoded());
    }
}
