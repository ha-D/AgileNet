package controllers;

import com.google.common.collect.ImmutableMap;
import dao.CommentDao;
import dao.ResourceDao;
import dao.UserDao;
import models.Comment;
import models.Resource;
import models.Role;
import models.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import play.mvc.Result;
import testutils.BaseTest;
import utilities.Dependencies;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static play.test.Helpers.callAction;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.status;

public class CommentTest extends BaseTest {
    private final static String USER_EMAIL = "some@guy.com";
    private final static String ADMIN_EMAIL = "admin@admin.com";

    private User user, admin;
    private Resource resource;
    private Comment baseComment, comment;

    private UserDao userDao;
    private ResourceDao resourceDao;
    private CommentDao commentDao;

    @Before
    public void before() {
        userDao = mock(UserDao.class);
        resourceDao = mock(ResourceDao.class);
        commentDao = mock(CommentDao.class);
        Dependencies.setUserDao(userDao);
        Dependencies.setResourceDao(resourceDao);
        Dependencies.setCommentDao(commentDao);

        user = newUser(1, USER_EMAIL, "thepassword");
        admin = newUser(2, ADMIN_EMAIL, "admin");
        resource = newResource(1, "resource");
        baseComment = newComment(1);
        comment = newComment(2);

        Role adminRole = new Role("admin");
        admin.assignRole(adminRole, false);

        when(userDao.findByEmail(USER_EMAIL)).thenReturn(user);
        when(userDao.findByEmail(ADMIN_EMAIL)).thenReturn(admin);
        when(resourceDao.findById(1)).thenReturn(resource);
        when(commentDao.findById(1)).thenReturn(baseComment);
        when(commentDao.findById(2)).thenReturn(comment);
        when(commentDao.create(any(User.class), anyString(), any(Resource.class))).thenReturn(comment);
        when(commentDao.create(any(User.class), anyString(), any(Comment.class))).thenReturn(comment);
    }

    @Test
    public void testAddCommentOnResource() {
        Map<String, String> params = ImmutableMap.of(
            "type", "on resource",
            "id", "1",
            "body", "content"
        );

        Result result = makeRequest(routes.ref.Comments.addComment(), params, USER_EMAIL);

        assertSuccess(null, result);
        verify(commentDao).create(user, "content", resource);
    }

    @Test
    public void testAddCommentOnComment() {
        Map<String, String> params = ImmutableMap.of(
                "type", "on comment",
                "id", "1",
                "body", "content"
        );

        Result result = makeRequest(routes.ref.Comments.addComment(), params, USER_EMAIL);

        assertSuccess(null, result);
        verify(commentDao).create(user, "content", baseComment);
    }

    @Test
    public void testCantCommentWithoutLogin() {
        Map<String, String> params = ImmutableMap.of(
                "type", "on resource",
                "id", "1",
                "body", "content"
        );

        Result result = makeRequest(routes.ref.Comments.addComment(), params);

        assertFail("Should not be able to comment if not signed in", result);
    }

    @Test
    public void testFilterComment() {
        Map<String, String> params = ImmutableMap.of("id", "2");

        Result result = makeRequest(routes.ref.Comments.filterComment(), params, ADMIN_EMAIL);

        assertSuccess(null, result);
        assertTrue("Comment filtered should be true after being filtered", comment.filtered);

        result = makeRequest(routes.ref.Comments.undoFilterComment(), params, ADMIN_EMAIL);

        assertSuccess(null, result);
        assertFalse("Comment filtered should be false after undoing filter", comment.filtered);
    }

    @Test
    public void testOnlyAdminCanFilterComment() {
        Map<String, String> params = ImmutableMap.of("id", "2");

        Result result = makeRequest(routes.ref.Comments.filterComment(), params, USER_EMAIL);

        assertFail(null, result);
        assertFalse("Comment should not be filtered by normal user", comment.filtered);
    }

    private void assertSuccess(String message, Result result) {
        int status = status(result);
        assertEquals(message, 200, status);
    }

    private void assertFail(String message, Result result) {
        int status = status(result);
        assertNotEquals(message, 200, status);
    }


}
