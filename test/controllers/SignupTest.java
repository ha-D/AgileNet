package controllers;

import com.google.common.collect.ImmutableMap;
import dao.UserDao;
import models.Dependencies;
import models.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import play.mvc.Result;
import testutils.BaseTest;

import java.util.Map;

import static org.junit.Assert.*;
import static play.test.Helpers.*;

public class SignupTest extends BaseTest {

    @Before
    public void before() {
        UserDao userDao = Mockito.mock(UserDao.class);
        Dependencies.setUserDao(userDao);
    }

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
        UserDao userDao = Mockito.mock(UserDao.class);
        Dependencies.setUserDao(userDao);
        Mockito.when(userDao.findByEmail("hadi@zolfaghari.com")).thenReturn(new User());

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
                routes.ref.Accounts.signupSubmit(),
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
