package core;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Item.class}, version = 1)
@TypeConverters({Vec2TypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ItemDao itemDao();

    private static AppDatabase db;
    private static final Object dbLock = new Object();

    public static AppDatabase getDb(Context context) {
        synchronized (dbLock) {
            if (db == null) {
                db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "db").build();
            }
        }
        return db;
    }
}
