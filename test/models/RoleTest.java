package models;

import org.junit.Test;
import utils.BaseTest;

import static org.junit.Assert.*;

public class RoleTest extends BaseTest  {

    @Test
    public void testCreateAndFindRole() {
        Role role = new Role();
        role.name = "newrole";
        role.save();

        role = Role.findByName("newrole");
        assertEquals("newrole", role.name);
        assertNotNull(role.id);

        role = Role.findByName("nosuchrole");
        assertNull(role);

        role = Role.create("anotherrole");
        assertNotNull(role.id);
        role = Role.findByName("anotherrole");
        assertNotNull(role);
    }

    @Test
    public void testRoleAssignment() {
        Role role1 = Role.create("newrole");
        Role role2 = Role.create("anothernewrole");

        User user = User.create("Hadi", "Zolfaghari", "hadi@zolfaghari.com", "thepassword");

        user.assignRole(role1, false);
        assertTrue("User must have assigned role", user.hasRole(role1));
        assertFalse("User must not have unassigned role", user.hasRole(role2));

        user = User.findByEmail("hadi@zolfaghari.com");
        assertFalse("User must not have assigned role if not commited", user.hasRole(role1));

        user.assignRole(role1);
        user = User.findByEmail("hadi@zolfaghari.com");
        assertTrue("User must have assigned role", user.hasRole(role1));
        assertFalse("User must not have unassigned role", user.hasRole(role2));
    }

    @Test
    public void testRoleEquals() {
        Role role1 = Role.create("arole");
        Role role2 = new Role("arole");
        Role role3 = Role.findByName("arole");
        Role role4 = new Role("anotherrole");
        Role role5 = new Role();

        assertTrue("Roles are equal by name", role1.equals(role2));
        assertTrue("Roles are equal by name", role2.equals(role1));
        assertTrue("Roles are equal by name", role1.equals(role3));
        assertFalse("Roles are equal by name", role1.equals(role4));
        assertFalse("Roles are equal by name", role1.equals(role4));
    }
}