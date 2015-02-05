package utilities;

import models.Role;
import models.User;

import java.util.*;

import static play.mvc.Controller.request;

public class FormRequest {
    private Map<String, String[]> body;

    public FormRequest(Map<String, String[]> body) {
        if (body == null) {
            this.body = new HashMap<String, String[]>();
        } else {
            this.body = body;
        }
    }

    //TODO:you should use resource file to define exception message
    public String getSingleElement(String tokenName) {
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

    //TODO: "static final String" use to define column name
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

    public String get(String name, String defaultValue) {
        String[] params = body.get(name);
        if (params == null || params.length < 1) {
            return defaultValue;
        } else if (params.length > 1) {
            throw new RequestParseException("More than one argument found for " + name + " in request");
        }

        return params[0];
    }

    public String get(String name) {
        return get(name, null);
    }

    public List<String> getList(String name,  List<String> defaultValue) {
        String[] params = body.get(name);
        if (params == null) {
            return defaultValue;
        }

        return new ArrayList<String>(Arrays.asList(params));
    }

    public List<String> getList(String name) {
        return getList(name, null);
    }

    public Integer getInt(String name, Integer defaultValue) {
        String value = get(name, null);
        if (value == null) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    public Integer getInt(String name) {
        return getInt(name, null);
    }

    public static FormRequest formBody() {
        return new FormRequest(request().body().asFormUrlEncoded());
    }
}
