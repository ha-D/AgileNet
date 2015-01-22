package models;

import dao.EBeanUserDao;
import dao.UserDao;
import org.junit.Test;
import utils.BaseTest;

import java.util.Date;

import static org.junit.Assert.*;

public class UserDaoTest extends BaseTest {
    @Test
    public void testUserCreation() {
        UserDao userDao = new EBeanUserDao();

        User user = new User(userDao);
        user.firstName = "Hadi";
        user.lastName = "Zolfaghari";
        try {
            user.save();
            fail("User should not save without an email");
        } catch (Exception e) {}

        user = userDao.findByEmail("hadi@zolfaghari.com");
        assertNull(user);

        user = userDao.create(
                "Hadi",
                "Zolfaghari",
                "hadi@zolfaghari.com",
                "thepassword"
        );

        user = userDao.findByEmail("hadi@zolfaghari.com");
        assertNotNull("User should have id after save", user.id);
        assertEquals("Hadi", user.firstName);
        assertEquals("Zolfaghari", user.lastName);
        assertEquals("hadi@zolfaghari.com", user.email);
        assertNotNull(user.password);

        Date now = new Date();
        long timeDiff = now.getTime() - user.creationDate.getTime();
        assertTrue("User creation time should be set on creation", timeDiff < 2000);

        assertFalse("User should not be activated on creation", user.isActivated);
        assertFalse("User should not be suspended on creation", user.isSuspended);
    }

    @Test
    public void testPasswordAndAuthentication() {
        UserDao userDao = new EBeanUserDao();

        User user = userDao.create(
                "Hadi",
                "Zolfaghari",
                "hadi@zolfaghari.com",
                "thepassword"
        );

        user = userDao.findByEmail("hadi@zolfaghari.com");
        assertNotEquals("User password should not be saved in plain text", "thepassword", user.password);

        assertFalse("User should not authenticate with wrong password", user.authenticate("badpassword"));

        assertTrue("User should authenticate with right password", user.authenticate("thepassword"));
    }
}