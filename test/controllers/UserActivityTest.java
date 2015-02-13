package controllers;

import dao.CommentDao;
import dao.RateResourceDao;
import dao.ResourceDao;
import dao.UserDao;
import models.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;
import testutils.BaseTest;
import utilities.Dependencies;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.mvc.Http.HeaderNames.CACHE_CONTROL;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import static play.test.Helpers.status;

public class UserActivityTest extends BaseTest {
    private final static String ADMIN_EMAIL = "admin@admin.com";

    private User user;
    private Resource resource;
    private Comment comment;
    private RateResource rate;

    @Before
    public void setUp() throws ParseException {
        user = newUser(1, "some@guy.com", "thepassword");
        resource = newResource(1, "resource");
        comment = newComment(1, user);
        comment.parResource = resource;
        rate = newRateResource(resource, user, 4);

        User admin = newUser(2, ADMIN_EMAIL, "adminpassword");
        Role adminRole = new Role("admin");
        admin.assignRole(adminRole, false);

        ResourceDao resourceDao = mock(ResourceDao.class);
        CommentDao commentDao = mock(CommentDao.class);
        RateResourceDao rateDao = mock(RateResourceDao.class);
        UserDao userDao = mock(UserDao.class);
        Dependencies.setUserDao(userDao);
        Dependencies.setResourceDao(resourceDao);
        Dependencies.setCommentDao(commentDao);
        Dependencies.setRateResourceDao(rateDao);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        comment.date = sdf.parse("25/02/2015");
        rate.date = sdf.parse("26/02/2015");

        when(resourceDao.findById(1)).thenReturn(resource);
        when(commentDao.findLatest(anyInt())).thenReturn(Arrays.asList(comment));
        when(rateDao.findLatest(anyInt())).thenReturn(Arrays.asList(rate));
        when(userDao.findByEmail(ADMIN_EMAIL)).thenReturn(admin);
        when(userDao.findByEmail(user.email)).thenReturn(user);
    }

    @Test
    public void testGetUserActivities() throws JSONException {
        Result result = makeRequest(routes.ref.Activities.getUserActivity(), ADMIN_EMAIL);
        assertSuccess(null, result);
        assertThat(contentType(result)).isEqualTo("application/json");

        JSONObject json = new JSONObject(contentAsString(result));
        int count = json.getInt("resultCount");
        JSONArray results = json.getJSONArray("results");

        JSONObject firstResult = results.getJSONObject(0);
        assertEquals(resource.id, firstResult.getInt("resourceId"));
        assertEquals(user.getFullName(), firstResult.getString("user"));
        assertEquals("rate", firstResult.getString("type"));
        assertEquals(rate.rate, firstResult.getInt("value"));

        JSONObject secondResult = results.getJSONObject(1);
        assertEquals(comment.parResource.id, secondResult.getInt("resourceId"));
        assertEquals(user.getFullName(), secondResult.getString("user"));
        assertEquals("comment", secondResult.getString("type"));
    }

    @Test
    public void testUserActivityAuthorization() {
        Result result = makeRequest(routes.ref.Activities.getUserActivity(), user.email);
        assertFail("User Activities should only be available for admin users", result);
    }

    private void assertSuccess(String message, Result result) {
        assertEquals(message, 200, status(result));
    }

    private void assertFail(String message, Result result) {
        assertNotEquals(message, 200, status(result));
    }
}
