package utils;

import dao.RoleDao;
import dao.UserDao;
import models.Dependencies;
import models.Role;
import models.User;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FormRequestTest {
    @Test
    public void testParseUser() {
        UserDao userDao = mock(UserDao.class);
        when(userDao.findById(Mockito.anyInt())).thenReturn(null);
        when(userDao.findById(1)).thenReturn(new User());

        Dependencies.setUserDao(userDao);

        Map<String, String[]> body = new HashMap<String, String[]>();
        FormRequest formRequest = new FormRequest(body);

        try {
            formRequest.parseUser();
            fail("parseUser should throw exception when no 'user' in form body");
        } catch (RequestParseException e) {}

        body.put("user", new String[]{"5"});
        formRequest = new FormRequest(body);
        try {
            formRequest.parseUser();
            fail("parseUser should throw exception when 'user' doesn't exists");
        } catch (RequestParseException e) {}

        body.put("user", new String[]{"1"});
        formRequest = new FormRequest(body);
        User user = formRequest.parseUser();
        assertNotNull("parseUser should return user with valid id", user);
    }

    @Test
    public void testParseRole() {
        RoleDao roleDao = mock(RoleDao.class);

        when(roleDao.findByName(Mockito.anyString())).thenReturn(null);
        when(roleDao.findByName("somerole")).thenReturn(new Role("somerole"));

        Dependencies.setRoleDao(roleDao);

        Map<String, String[]> body = new HashMap<String, String[]>();
        FormRequest formRequest = new FormRequest(body);

        try {
            formRequest.parseRole();
            fail("parseRole should throw exception when no 'role' in form body");
        } catch (RequestParseException e) {}

        body.put("user", new String[]{"someotherrole"});
        formRequest = new FormRequest(body);
        try {
            formRequest.parseRole();
            fail("parseRole should throw exception when 'role' doesn't exists");
        } catch (RequestParseException e) {}

        body.put("role", new String[]{"somerole"});
        formRequest = new FormRequest(body);
        Role role = formRequest.parseRole();
        assertNotNull("parseRole should return role with valid name", role);
    }
}
