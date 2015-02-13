package controllers;

import com.google.common.collect.ImmutableMap;
import dao.UserDao;
import utilities.Dependencies;
import models.User;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;
import testutils.BaseTest;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.test.Helpers.*;

public class LoginTest extends BaseTest {

    @Before
    public void before() {
        UserDao userDao = mock(UserDao.class);

        User user = new User();
        user.email = "hadi@zolfaghari";
        user.setPassword("thepassword");
        user.firstName = "hadi";
        user.lastName = "zolfaghari";

        when(userDao.findByEmail("hadi@zolfaghari.com")).thenReturn(user);

        Dependencies.setUserDao(userDao);
    }

    @Test
    public void loginSuccess() {
        Map params = ImmutableMap.of(
            "email", "hadi@zolfaghari.com",
            "password", "thepassword"
        );
        Result result = makeRequest(routes.ref.Accounts.loginSubmit(), params);
        assertSuccess("Login should be successful with valid fields", result);
        assertEquals("User email should exist in session after successful loginSubmit",
                "hadi@zolfaghari.com", session(result).get("email"));
    }

    @Test
    public void loginFailPassword() {
        Map params = ImmutableMap.of(
            "email", "hadi@zolfaghari.com",
            "password", "thewrongpassword"
        );
        Result result = makeRequest(routes.ref.Accounts.loginSubmit(), params);
        assertFail("Login should fail if fields are invalid", result);
        assertNull("User email should not exist in session after failed loginSubmit", session(result).get("email"));
    }

    @Test
    public void loginFailEmail() {
        Map params = ImmutableMap.of(
            "email", "who@huh.com",
            "password", "somepassword"
        );
        Result result = makeRequest(routes.ref.Accounts.loginSubmit(), params);
        assertFail("Login should fail if fields are invalid", result);
        assertNull("User email should not exist in session after failed loginSubmit", session(result).get("email"));
    }

    private void assertSuccess(String message, Result result) {
        int status = status(result);
        assertTrue(message, status == 200 || status == 302 || status == 303);
    }

    private void assertFail(String message, Result result) {
        assertEquals(message, 400, status(result));
    }
}
