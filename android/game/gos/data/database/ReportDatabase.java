package com.samsung.android.game.gos.data.database;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.dao.ReportDao;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.AppVariable;

public abstract class ReportDatabase extends RoomDatabase {
    private static final String LOG_TAG = ReportDatabase.class.getSimpleName();
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        public void migrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.execSQL("CREATE TABLE temp_Report(id INTEGER PRIMARY KEY NOT NULL, tag TEXT, msg TEXT, ringlogMsg TEXT, gppRepAggregation TEXT, gppRepDataSchemeVersion TEXT, byteSize INTEGER NOT NULL DEFAULT 0)");
            supportSQLiteDatabase.execSQL("INSERT INTO temp_Report(id, tag, msg, ringlogMsg, byteSize) SELECT id, tag, msg, ringlogMsg, byteSize FROM Report");
            supportSQLiteDatabase.execSQL("DROP TABLE Report");
            supportSQLiteDatabase.execSQL("ALTER TABLE temp_Report RENAME TO Report");
        }
    };
    public static final String REPORT_DATABASE_FILE_DB = "report-db.db";
    private static ReportDatabase mReportDatabase;

    public abstract ReportDao reportDao();

    public static ReportDatabase getReportDatabase() {
        Class<ReportDatabase> cls = ReportDatabase.class;
        if (AppVariable.isUnitTest()) {
            mReportDatabase = Room.databaseBuilder(AppContext.get(), cls, REPORT_DATABASE_FILE_DB).fallbackToDestructiveMigrationOnDowngrade().addMigrations(MIGRATION_1_2).allowMainThreadQueries().build();
        } else if (mReportDatabase == null) {
            mReportDatabase = Room.databaseBuilder(AppContext.get(), cls, REPORT_DATABASE_FILE_DB).fallbackToDestructiveMigrationOnDowngrade().addMigrations(MIGRATION_1_2).build();
            GosLog.d(LOG_TAG, "Room: report file was initialized.");
        }
        return mReportDatabase;
    }

    public static void closeDb() {
        ReportDatabase reportDatabase = getReportDatabase();
        if (reportDatabase != null) {
            reportDatabase.getOpenHelper().close();
            reportDatabase.close();
        }
    }
}
