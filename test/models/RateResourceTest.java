package models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RateResourceTest {
    @Test
    public void testRateResourceEquals() {
        RateResource rr1 = new RateResource();
        RateResource rr2 = new RateResource();
        RateResource rr3 = new RateResource();
        RateResource rr4 = new RateResource();

        User u1 = new User(); u1.email="kh@gmail.com";
        User u2 = new User(); u2.email="h@gmail.com";

        Resource r1 = new Resource();
        Resource r2 = new Resource();
        r1.id = 1;
        r2.id = 2;

        rr1.user = u1;
        rr1.resource = r1;

        rr2.user = u1;
        rr2.resource = r1;

        rr3.user = u1;
        rr3.resource = r2;

        rr4.user = u2;
        rr4.resource = r1;

        String message = "RateResource instances are equal by both user and resource";
        assertTrue(message, rr1.equals(rr1));
        assertTrue(message, rr1.equals(rr2));
        assertFalse(message, rr1.equals(rr3));
        assertFalse(message, rr1.equals(rr4));
        assertFalse(message, rr1.equals(null));
    }
}
