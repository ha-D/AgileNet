package dao;

import dao.impl.RoleDaoImpl;
import dao.impl.UserDaoImpl;
import utilities.Dependencies;
import models.Role;
import models.User;
import org.junit.Test;
import testutils.BaseTest;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RoleDaoTest extends BaseTest {
    @Test
    public void testCreateAndFindRole() {
        dao.RoleDao roleDao = new RoleDaoImpl();
        Dependencies.setRoleDao(roleDao);

        Role role = new Role();
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
        dao.RoleDao roleDao = new RoleDaoImpl();
        dao.UserDao userDao = new UserDaoImpl();

        Dependencies.setUserDao(userDao);
        Dependencies.setRoleDao(roleDao);

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
