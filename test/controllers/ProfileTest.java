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
    private final static String USER_EMAIL = "akbar@asghari.com";

    @Before
    public void before() {
        UserDao userDao = mock(UserDao.class);

        User user = new User();
        user.email = USER_EMAIL;
        user.setPassword("thepassword");
        user.firstName = "akbar";
        user.lastName = "asghari";
        user.contactPhone = "1111111";
        user.nationalId = "000000";

        when(userDao.findByEmail(USER_EMAIL)).thenReturn(user);

        Dependencies.setUserDao(userDao);
    }

    @Test
    public void updateSuccess(){
        Map<String, String> params = ImmutableMap.of(
                "firstName", "اکبر",
                "lastName", "اضغری",
                "contactPhone", "۱۱۱۱۱۱۱",
                "nationalId", "۰۰۰۰۰۰۰"
        );

        Result result = makeRequest(routes.ref.Accounts.updateProfile(), params, USER_EMAIL);

        User updatedUser = Dependencies.getUserDao().findByEmail(USER_EMAIL);
        assertEquals("Update is possible after login", status(result), 303); //redirected to profile
        assertEquals(params.get("firstName"), updatedUser.firstName);
        assertEquals(params.get("lastName"), updatedUser.lastName);
        assertEquals(params.get("nationalId"), updatedUser.nationalId);
        assertEquals(params.get("contactPhone"), updatedUser.contactPhone);
    }
}
