package models;

import org.junit.Test;
import utils.BaseTest;

import java.util.Date;

import static org.junit.Assert.*;

public class UserTest extends BaseTest {

    @Test
    public void testUserCreation() {
        User user = new User();
        user.firstName = "Hadi";
        user.lastName = "Zolfaghari";
        try {
            user.save();
            fail("User should not save without an email");
        } catch (Exception e) {}

        user = User.findByEmail("hadi@zolfaghari.com");
        assertNull(user);

        user = User.create(
            "Hadi",
            "Zolfaghari",
            "hadi@zolfaghari.com",
            "thepassword"
        );

        user = User.findByEmail("hadi@zolfaghari.com");
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
        User user = User.create(
                "Hadi",
                "Zolfaghari",
                "hadi@zolfaghari.com",
                "thepassword"
        );

        user = User.findByEmail("hadi@zolfaghari.com");
        assertNotEquals("User password should not be saved in plain text", "thepassword", user.password);

        user = User.authenticate("wrongmail", "thepassword");
        assertNull(user);

        user = User.authenticate("hadi@zolfaghari.com", "badpassword");
        assertNull("User should not authenticate with wrong password", user);

        user = User.authenticate("hadi@zolfaghari.com", "thepassword");
        assertNotNull("User should authenticate with right password", user);
        assertEquals("hadi@zolfaghari.com", user.email);
        assertEquals("hadi", user.firstName);
    }

    @Test
    public void testUserEquals() {
        User user1 = User.create("Hadi", "Zolfaghari", "hadi@zolfaghari.com", "thepassword");

        User user2 = new User();
        user2.email = "hadi@zolfaghari.com";

        User user3 = new User();
        user3.email = "nothadi@zolfaghari.com";

        User user4 = new User();

        assertTrue("Users are equal by email address", user1.equals(user2));
        assertTrue("Users are equal by email address", user2.equals(user1));
        assertFalse("Users are equal by email address", user1.equals(user3));
        assertFalse("Users are equal by email address", user3.equals(user1));
        assertFalse("Users are equal by email address", user2.equals(user3));
        assertFalse("Users are equal by email address", user1.equals(user4));
    }
}