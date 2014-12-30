package controllers;

import com.google.common.collect.ImmutableMap;
import models.User;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;
import utils.BaseTest;

import java.util.Map;

import static org.junit.Assert.*;
import static play.test.Helpers.*;

public class LoginTest extends BaseTest {
    @Before
    public void before() {
        User.create("hadi", "zolfaghari", "hadi@zolfaghari.com", "thepassword");
        User user = User.findByEmail("hadi@zolfaghari.com");
        assertNotNull("User creation in database must work", user);
    }

    @Test
    public void loginSuccess() {
        Map params = ImmutableMap.of(
            "email", "hadi@zolfaghari.com",
            "password", "thepassword"
        );
        Result result = makeRequest(params);
        assertSuccess("Login should be successful with valid fields", result);
        assertEquals("User email should exist in session after successful login",
                "hadi@zolfaghari.com", session(result).get("email"));
    }

    @Test
    public void loginFailPassword() {
        Map params = ImmutableMap.of(
            "email", "hadi@zolfaghari.com",
            "password", "thewrongpassword"
        );
        Result result = makeRequest(params);
        assertFail("Login should fail if fields are invalid", result);
        assertNull("User email should not exist in session after failed login", session(result).get("email"));
    }

    @Test
    public void loginFailEmail() {
        Map params = ImmutableMap.of(
            "email", "who@huh.com",
            "password", "somepassword"
        );
        Result result = makeRequest(params);
        assertFail("Login should fail if fields are invalid", result);
        assertNull("User email should not exist in session after failed login", session(result).get("email"));
    }

    private Result makeRequest(Map params) {
        return callAction(
                routes.ref.Accounts.login(),
                fakeRequest().withFormUrlEncodedBody(params)
        );
    }

    private void assertSuccess(String message, Result result) {
        int status = status(result);
        assertTrue(message, status == 200 || status == 302 || status == 303);
    }

    private void assertFail(String message, Result result) {
        assertEquals(message, 400, status(result));
    }
}
