package models;

import dao.EBeanRoleDao;
import dao.EBeanUserDao;
import dao.RoleDao;
import dao.UserDao;
import org.junit.Test;
import utils.BaseTest;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RoleDaoTest extends BaseTest {
    @Test
    public void testCreateAndFindRole() {
        RoleDao roleDao = new EBeanRoleDao();

        Role role = new Role(roleDao);
        roleDao.create("newrole");

        role = roleDao.findByName("newrole");
        assertEquals("newrole", role.name);
        assertNotNull(role.id);

        role = roleDao.findByName("nosuchrole");
        assertNull(role);

        role = roleDao.create("anotherrole");
        assertNotNull(role.id);
        role = roleDao.findByName("anotherrole");
        assertNotNull(role);
    }

    @Test
    public void testRoleAssignment() {
        RoleDao roleDao = new EBeanRoleDao();
        UserDao userDao = new EBeanUserDao();

        Role role1 = roleDao.create("newrole");
        Role role2 = roleDao.create("anothernewrole");

        User user = userDao.create("Hadi", "Zolfaghari", "hadi@zolfaghari.com", "thepassword");

        user.assignRole(role1, false);
        assertTrue("User must have assigned role", user.hasRole(role1));
        assertFalse("User must not have unassigned role", user.hasRole(role2));

        user = userDao.findByEmail("hadi@zolfaghari.com");
        assertFalse("User must not have assigned role if not commited", user.hasRole(role1));

        user.assignRole(role1);
        user = userDao.findByEmail("hadi@zolfaghari.com");
        assertTrue("User must have assigned role", user.hasRole(role1));
        assertFalse("User must not have unassigned role", user.hasRole(role2));
    }
}
