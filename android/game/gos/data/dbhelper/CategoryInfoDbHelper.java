package com.samsung.android.game.gos.data.dbhelper;

import com.samsung.android.game.gos.data.dao.CategoryInfoDao;
import com.samsung.android.game.gos.data.database.CategoryInfoDatabase;
import com.samsung.android.game.gos.data.model.CategoryInfo;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.AppVariable;

public class CategoryInfoDbHelper {
    private static final String LOG_TAG = CategoryInfoDbHelper.class.getSimpleName();
    private final CategoryInfoDao categoryInfoDao;

    public static CategoryInfoDbHelper getInstance() {
        if (AppVariable.isUnitTest()) {
            return new CategoryInfoDbHelper();
        }
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final CategoryInfoDbHelper INSTANCE = new CategoryInfoDbHelper();

        private SingletonHolder() {
        }
    }

    private CategoryInfoDbHelper() {
        this.categoryInfoDao = CategoryInfoDatabase.getCategoryInfoDatabase().categoryInfoDao();
    }

    public CategoryInfo getCategoryInfo(String str) {
        return this.categoryInfoDao.getPackage(str);
    }

    public String getCategory(String str) {
        CategoryInfo categoryInfo = getCategoryInfo(str);
        if (categoryInfo != null) {
            String str2 = LOG_TAG;
            GosLog.d(str2, "getCategoryInfo() pc != null: " + str + ", " + categoryInfo.category);
            return categoryInfo.category;
        }
        String str3 = LOG_TAG;
        GosLog.d(str3, "getCategoryInfo() pc == null: " + str);
        return null;
    }

    /* access modifiers changed from: package-private */
    public boolean addCategoryInfo(String str, String str2, int i) {
        if (str == null || str2 == null) {
            return false;
        }
        this.categoryInfoDao.insertOrUpdate(new CategoryInfo(str, str2, i));
        return true;
    }

    public boolean isFixed(String str) {
        CategoryInfo categoryInfo = getCategoryInfo(str);
        boolean z = true;
        if (categoryInfo == null || categoryInfo.fixed != 1) {
            z = false;
        }
        String str2 = LOG_TAG;
        GosLog.d(str2, "getCategoryInfo() isFixed: " + z);
        return z;
    }

    /* access modifiers changed from: package-private */
    public int delete(String str) {
        return this.categoryInfoDao.delete(str);
    }
}
