package kapp.room.db.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by Arunraj on 2/28/2018.
 */

@Database(entities = { User.class }, version = 1)
public abstract class UserDatabase extends RoomDatabase {

    private static final String DB_NAME = "user_db.db";
    private static volatile UserDatabase instance;

    // Reference volatile and synchronized
    // https://www.geeksforgeeks.org/volatile-keyword-in-java/
    // https://www.geeksforgeeks.org/synchronized-in-java/

    public static synchronized UserDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static UserDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                UserDatabase.class,
                DB_NAME).build();
    }

    public abstract UserDao getUserDao();
}
