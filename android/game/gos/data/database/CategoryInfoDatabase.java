package com.samsung.android.game.gos.data.database;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.dao.CategoryInfoDao;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.AppVariable;

public abstract class CategoryInfoDatabase extends RoomDatabase {
    private static final String LOG_TAG = CategoryInfoDatabase.class.getSimpleName();
    private static CategoryInfoDatabase mCategoryInfoDatabase;

    public abstract CategoryInfoDao categoryInfoDao();

    public void clearAllTables() {
    }

    public static void buildCategoryInfoDatabase() {
        if (AppVariable.isUnitTest()) {
            setCategoryInfoDatabase();
        } else if (mCategoryInfoDatabase == null) {
            setCategoryInfoDatabase();
            GosLog.d(LOG_TAG, "Room: categoryInfo file was initialized.");
        }
    }

    private static void setCategoryInfoDatabase() {
        mCategoryInfoDatabase = Room.databaseBuilder(AppContext.get(), CategoryInfoDatabase.class, "categoryInfo.db").createFromAsset("databases/categoryInfo.db").fallbackToDestructiveMigrationOnDowngrade().fallbackToDestructiveMigration().allowMainThreadQueries().build();
    }

    public static CategoryInfoDatabase getCategoryInfoDatabase() {
        buildCategoryInfoDatabase();
        return mCategoryInfoDatabase;
    }

    public static void closeDb() {
        CategoryInfoDatabase categoryInfoDatabase = getCategoryInfoDatabase();
        if (categoryInfoDatabase != null) {
            categoryInfoDatabase.getOpenHelper().close();
            categoryInfoDatabase.close();
        }
    }
}
