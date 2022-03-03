package com.samsung.android.game.gos.data.dbhelper;

import com.samsung.android.game.gos.data.dao.LocalLogDao;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.value.AppVariable;

public class LocalLogDbHelper {
    private static final String LOG_TAG = LocalLogDao.class.getSimpleName();
    static final int MAX_ROW_SIZE = 10000;

    private LocalLogDbHelper() {
    }

    public static LocalLogDbHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void addLocalLog(String str, String str2) {
        if (str2 != null) {
            String str3 = LOG_TAG;
            GosLog.d(str3, "addLocalLog(), tag: " + str + ", msg: " + str2);
            long currentTimeMillis = System.currentTimeMillis();
            long insert = DbHelper.getInstance().getLocalLogDao().insert(currentTimeMillis, TypeConverter.getDateFormattedTime(currentTimeMillis), str2, str);
            if (insert >= ((long) getMaxRowSize())) {
                DbHelper.getInstance().getLocalLogDao().deleteByIdBetween(0, insert - ((long) getMaxRowSize()));
            }
        }
    }

    protected static int getMaxRowSize() {
        return AppVariable.isUnitTest() ? 10 : 10000;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final LocalLogDbHelper INSTANCE = new LocalLogDbHelper();

        private SingletonHolder() {
        }
    }
}
