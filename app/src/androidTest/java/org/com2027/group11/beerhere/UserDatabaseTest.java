package org.com2027.group11.beerhere;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.com2027.group11.beerhere.user.User;
import org.com2027.group11.beerhere.user.UserDao;
import org.com2027.group11.beerhere.utilities.database.AppDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by river on 05/03/18.
 */
@RunWith(AndroidJUnit4.class)
public class UserDatabaseTest {
    private UserDao mUserDao;
    private AppDatabase mDB;

    @Before
    public void createDB() {
        Context context = InstrumentationRegistry.getTargetContext();

        mDB = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        mUserDao = mDB.userDao();
    }

    @After
    public void closeDB() throws IOException {
        mDB.close();
    }

    @Test
    public void writeAndReadUser() throws Exception {
        User user = new User("uid", "Joe Bloggs", "joe.bloggs@test.net", new Date(1, 1, 1970), "United Kingdom");
        mUserDao.insertUser(user);
        User dbUser = mUserDao.findByID("uid");
        assertEquals(user.country, dbUser.country);
        assertEquals(user.dateOfBirth, dbUser.dateOfBirth);
        assertEquals(user.email, dbUser.email);
        assertEquals(user.name, dbUser.name);
    }
}
