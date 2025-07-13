package com.example.photoHub;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Photographer.class}, version = 2, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class DbHelper extends RoomDatabase {

    private static final String DB_NAME = "PhotoHubDb";
    private static DbHelper instance;

    public abstract PhotographerDao photographerDao();

    public static synchronized DbHelper getDB(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            DbHelper.class,
                            DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
