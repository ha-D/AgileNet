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

    /**
     * Retrieves a single string parameter assigned with the tokenName
     * @param tokenName The parameter name
     * @return The value assigned to the tokenName
     * @throws RequestParseException If no value or more than one value exists with the tokenName
     */
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

    /**
     * Retrieves a user based on request parameters.
     * @param tokenName The tokenName to retrieve the user based upon. The id of the user should exist in the value
     *                  of this parameter.
     * @return The retrieved user
     * @throws RequestParseException If invalid value was assigned to tokenName or no user exists with the given id.
     */
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

    /**
     * Retrieves a user based on request parameters. The user id should exist in a parameter with the token name 'user'.
     * @return The retrieved user
     * @throws RequestParseException If invalid value was assigned to tokenName or no user exists with the given id.
     */
    public User parseUser() {
        return parseUser("user");
    }

    /**
     * Retrieves a role based on request parameters.
     * @param tokenName The tokenName to retrieve the role based upon. The name of the role should exist in the value
     *                  of this parameter.
     * @return The retrieved role
     * @throws RequestParseException If invalid value was assigned to tokenName or no role exists with the given name.
     */
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

    /**
     * Retrieves a role based on request parameters. The role name should exist in a parameter with the name 'role'.
     * @return The retrieved role
     * @throws RequestParseException If invalid value was assigned to tokenName or no role exists with the given name.
     */
    public Role parseRole() {
        return parseRole("role");
    }

    /**
     * Retrieves a single string parameter assigned with the given name
     * @param name The parameter name
     * @param defaultValue The value to return if the parameter was not found
     * @return The value assigned to the given name
     * @throws RequestParseException If more than one value exists with the given name
     */
    public String get(String name, String defaultValue) {
        String[] params = body.get(name);
        if (params == null || params.length < 1) {
            return defaultValue;
        } else if (params.length > 1) {
            throw new RequestParseException("More than one argument found for " + name + " in request");
        }

        return params[0];
    }

    /**
     * Retrieves a single string parameter assigned with the given name
     * @param name The parameter name
     * @return The value assigned to the given name
     * @throws RequestParseException If no value or more than one value exists with the given name
     */
    public String get(String name) {
        String res =  get(name, null);
        if (res == null) {
            throw new RequestParseException("Missing parameter " + name);
        }
        return res;
    }

    /**
     * Retrieves a list of string parameters assigned with the given name
     * @param name The parameter name
     * @param defaultValue The default value to return if no value was assigned to the given name
     * @return The value assigned to the given name
     */
    public List<String> getList(String name,  List<String> defaultValue) {
        String[] params = body.get(name);
        if (params == null) {
            return defaultValue;
        }

        return new ArrayList<String>(Arrays.asList(params));
    }

    /**
     * Retrieves a list of string parameters assigned with the given name
     * @param name The parameter name
     * @return The value assigned to the given name
     * @throws RequestParseException If no value exists with the given name
     */
    public List<String> getList(String name) {
        return getList(name, null);
    }

    /**
     * Retrieves a single integer parameter assigned with the given name
     * @param name The parameter name
     * @param defaultValue The default value to return if no value was assigned to the given name
     * @return The value assigned to the given name
     * @throws RequestParseException If more than value exists with the given name or if the value
     *                               was not a valid integer
     */
    public Integer getInt(String name, Integer defaultValue) {
        String value = get(name, null);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException e) {
            throw new RequestParseException("Integer expected for '" + name + "' but got '" + value + "'");
        }
    }

    /**
     * Retrieves a single integer parameter assigned with the given name
     * @param name The parameter name
     * @return The value assigned to the given name
     * @throws RequestParseException If no value or more than one value exists with the given name or if the value
     *                               was not a valid integer
     */
    public Integer getInt(String name) {
        Integer val = getInt(name, null);
        if (val == null) {
            throw new RequestParseException("Missing parameter " + name);
        }
        return val;
    }

    /**
     * Creates a FormRequest object based on the POST parameters of the current request
     * @return The newly created FormRequest object
     */
    public static FormRequest formBody() {
        return new FormRequest(request().body().asFormUrlEncoded());
    }

    /**
     * Creates a FormRequest object based on the GET parameters of the current request
     * @return The newly created FormRequest object
     */
    public static FormRequest formGetBody() {
        return new FormRequest(request().queryString());
    }
}
