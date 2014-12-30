package controllers;

import com.google.common.collect.ImmutableMap;
import models.User;
import org.junit.Test;
import play.mvc.Result;
import utils.BaseTest;

import java.util.Map;

import static org.junit.Assert.*;
import static play.test.Helpers.*;

public class SignupTest extends BaseTest {

    @Test
    public void signupSuccess() {
        Map params = ImmutableMap.of(
            "firstName", "hadi",
            "lastName", "zolfaghari",
            "email", "hadi@zolfaghari.com",
            "password", "thepassword",
            "passwordConfirm", "thepassword"
        );
        Result result = makeRequest(params);
        assertSuccess("Sign up should be successful with required fields", result);

        String message = "User data should be valid in database after successful sign up";
        User user = User.findByEmail("hadi@zolfaghari.com");
        assertNotNull(message, user);
        assertEquals(message, "hadi", user.firstName);
        assertEquals(message, "zolfaghari", user.lastName);
        assertEquals(message, "hadi@zolfaghari.com", user.email);

        user = User.authenticate("hadi@zolfaghari.com", "thepassword");
        assertNotNull("User password should be correct after sign up", user);
    }

    @Test
    public void signupFail() {
        Map params = ImmutableMap.of(
            "firstName", "hadi",
            "email", "hadi@zolfaghari.com",
            "password", "thepassword",
            "passwordConfirm", "thepassword"
        );
        Result result = makeRequest(params);
        assertFail("Sign Up should fail without required fields", result);
    }

    @Test
    public void signupPasswordFail() {
        Map params = ImmutableMap.of(
            "firstName", "hadi",
            "lastName", "zolfaghari",
            "email", "hadi@zolfaghari.com",
            "password", "thepassword",
            "passwordConfirm", "adifferentpassword"
        );
        Result result = makeRequest(params);
        assertFail("Sign up should fail if passwords differ", result);
    }

    @Test
    public void signupDuplicateFail() {
        User.create("some", "guy", "hadi@zolfaghari.com", "somepassword");
        Map params = ImmutableMap.of(
                "firstName", "hadi",
                "lastName", "zolfaghari",
                "email", "hadi@zolfaghari.com",
                "password", "thepassword",
                "passwordConfirm", "adifferentpassword"
        );
        Result result = makeRequest(params);
        assertFail("Sign up should fail if email already exists", result);
    }

    private Result makeRequest(Map params) {
        return callAction(
                routes.ref.Accounts.signup(),
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
