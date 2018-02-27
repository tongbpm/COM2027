package org.com2027.group11.beerhere;

import org.com2027.group11.beerhere.user.User;
import org.junit.Test;

import java.util.Calendar;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by river on 27/02/18.
 */

public class UserTest {

    @Test
    public void defaultConstructorTest() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    public void paramaterisedConstructor() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1997, 11, 2); // Assumes MM/dd/yyyy
        User user = new User("River Phillips", "rp00375@surrey.ac.uk", calendar.getTime(), "United Kingdom");
        assertEquals(user.name, "River Phillips");
        assertEquals(user.email, "rp00375@surrey.ac.uk");
        assertEquals(user.dateOfBirth, calendar.getTime());
        assertEquals(user.country, "United Kingdom");
    }


}
