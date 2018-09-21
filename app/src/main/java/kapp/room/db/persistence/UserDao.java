package kapp.room.db.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Arunraj on 2/28/2018.
 */

@Dao
public interface UserDao {

    @Insert
    long insert(User user);

    @Update
    int update(User user);

    @Query("select * from user")
    List<User> getAllUsers();

    @Delete
    int delete(User user);
}
