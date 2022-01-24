package com.example.task9.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.task9.Models.DatabaseModels.BookDatabaseModel;
import com.example.task9.Models.DatabaseModels.Review;

@Database(entities = {Review.class, BookDatabaseModel.class}, version = 1, exportSchema = false)
public abstract class DatabaseInstance extends RoomDatabase{
    public abstract ReviewDao reviewDao();
    public abstract BookDao bookDao();

    private static volatile DatabaseInstance Instance;

    static DatabaseInstance getInstance(final Context context) {
        if(Instance == null) {
            synchronized ( DatabaseInstance.class) {
                if(Instance == null) {
                    Instance = Room.databaseBuilder(context.getApplicationContext(), DatabaseInstance.class, "bookApp_db")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return Instance;
    }
    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
