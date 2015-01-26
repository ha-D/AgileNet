package models;

import org.junit.Test;
import testutils.BaseTest;

import static org.junit.Assert.*;

public class RoleTest extends BaseTest  {

    @Test
    public void testRoleAssignment() {
        Role role1 = new Role("newrole", null);
        Role role2 = new Role("anothernewrole", null);

        User user = new User(null);

        user.assignRole(role1, false);
        assertTrue("User must have assigned role", user.hasRole(role1));
        assertFalse("User must not have unassigned role", user.hasRole(role2));
    }

    @Test
    public void testRoleEquals() {
        Role role1 = new Role("arole", null);
        Role role2 = new Role("arole", null);
        Role role3 = new Role("anotherrole", null);

        assertTrue("Roles are equal by name", role1.equals(role2));
        assertTrue("Roles are equal by name", role2.equals(role1));
        assertFalse("Roles are equal by name", role1.equals(role3));
        assertFalse("Roles are equal by name", role3.equals(role2));
    }
}