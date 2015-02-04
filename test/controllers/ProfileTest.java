package controllers;

import com.google.common.collect.ImmutableMap;
import dao.UserDao;
import utilities.Dependencies;
import models.User;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import testutils.BaseTest;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.test.Helpers.*;
import static play.test.Helpers.session;

public class ProfileTest extends BaseTest {

    @Before
    public void before() {
        UserDao userDao = mock(UserDao.class);

        User user = new User();
        user.email = "hadi@zolfaghari";
        user.setPassword("thepassword");
        user.firstName = "hadi";
        user.lastName = "zolfaghari";
        user.contactPhone = "1111111";
        user.nationalId = "000000";

        when(userDao.findByEmail("hadi@zolfaghari.com")).thenReturn(user);

        Dependencies.setUserDao(userDao);
    }

    @Test
    public void updateSuccess(){
        //login
        Map params = ImmutableMap.of(
                "email", "hadi@zolfaghari.com",
                "password", "thepassword"
        );
        Result result = callAction(
                routes.ref.Accounts.loginSubmit(),
                fakeRequest().withFormUrlEncodedBody(params)
        );
        Http.Cookie cookie = play.test.Helpers.cookie("PLAY_SESSION", result);
        params = ImmutableMap.of(
                "firstName", "هادی",
                "lastName", "ذوالفقاری",
                "contactPhone", "۱۱۱۱۱۱۱",
                "nationalId", "۰۰۰۰۰۰۰"
        );
        result = makeRequest(params, cookie);
        User updatedUser = Dependencies.getUserDao().findByEmail("hadi@zolfaghari.com");
        assertEquals("Update is possible after login", status(result), 303); //redirected to profile
        assertEquals(params.get("firstName"), updatedUser.firstName);
        assertEquals(params.get("lastName"), updatedUser.lastName);
        assertEquals(params.get("nationalId"), updatedUser.nationalId);
        assertEquals(params.get("contactPhone"), updatedUser.contactPhone);
    }

    private Result makeRequest(Map params, Http.Cookie cookie) {
        return callAction(
                routes.ref.Accounts.updateProfile(),
                (cookie==null? fakeRequest().withFormUrlEncodedBody(params)
                        : fakeRequest().withFormUrlEncodedBody(params).withCookies(cookie))
        );
    }


}
