package models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ResourceTest {
    @Test
    public void testResourceEquals() {
        Resource resource1 = new Resource();
        Resource resource2 = new Resource();
        Resource resource3 = new Resource();

        resource1.id = 1;
        resource2.id = 1;
        resource3.id = 3;

        String message = "Resources classes are equals by id";
        assertTrue(message, resource1.equals(resource2));
        assertTrue(message, resource2.equals(resource1));
        assertFalse(message, resource1.equals(resource3));
        assertFalse(message, resource3.equals(resource1));
        assertFalse(message, resource1.equals(null));
    }
}
