package org.com2027.group11.beerhere.user;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by river on 05/03/18.
 */
@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE uid = :uid LIMIT 1")
    User findByID(String uid);

    @Insert
    void insertUser(User user);

    @Delete
    void delete(User user);
}
