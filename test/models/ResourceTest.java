package models;

import dao.UserDao;
import org.junit.Before;
import org.junit.Test;
import testutils.BaseTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResourceTest extends BaseTest {
    @Before
    public void before() {
        UserDao userDao = mock(UserDao.class);

        User user = new User();
        user.email = "hadi@zolfaghari";
        user.setPassword("thepassword");
        user.firstName = "hadi";
        user.lastName = "zolfaghari";

        when(userDao.findByEmail("hadi@zolfaghari.com")).thenReturn(user);

        Dependencies.setUserDao(userDao);
    }

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

    @Test
    public void testGetRate(){
        Resource resource = new Resource();
        resource.rates = new ArrayList<RateResource>();
        for(int i=0; i<=5; ++i) {
            RateResource rr = new RateResource();
            rr.rate = i;
            resource.rates.add(rr);
        }

        assertTrue("GetRate should be equal to average of rates", resource.getRate()-((0.0+1+2+3+4+5)/6)<0.000001);

    }
}
