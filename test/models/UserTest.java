package models;

import org.junit.Test;
import testutils.BaseTest;

import static org.junit.Assert.*;

public class UserTest {
    @Test
    public void testUserEquals() {
        User user1 = new User();
        user1.email = "hadi@zolfaghari.com";

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


    @Test
    public void testAuthenticate() {
        User user = new User();
        user.email = "hadi@zolfaghari";
        user.setPassword("thepassword");
        user.firstName = "hadi";
        user.lastName = "zolfaghari";

        Boolean result = user.authenticate("thepassword");
        assertTrue(result);

        result = user.authenticate("wrongpass");
        assertFalse(result);
    }

}