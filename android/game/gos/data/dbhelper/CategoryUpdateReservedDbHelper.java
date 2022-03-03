package com.samsung.android.game.gos.data.dbhelper;

import com.samsung.android.game.gos.data.model.CategoryUpdateReserved;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.AppVariable;
import java.util.ArrayList;
import java.util.List;

public class CategoryUpdateReservedDbHelper {
    private static final String LOG_TAG = CategoryUpdateReservedDbHelper.class.getSimpleName();

    private CategoryUpdateReservedDbHelper() {
    }

    public static CategoryUpdateReservedDbHelper getInstance() {
        if (AppVariable.isUnitTest()) {
            return new CategoryUpdateReservedDbHelper();
        }
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final CategoryUpdateReservedDbHelper INSTANCE = new CategoryUpdateReservedDbHelper();

        private SingletonHolder() {
        }
    }

    public boolean addReservedCategory(String str) {
        GosLog.d(LOG_TAG, "addReservedCategory: ");
        if (str == null) {
            return false;
        }
        DbHelper.getInstance().getCategoryUpdateReservedDao().insertOrUpdate(new CategoryUpdateReserved(str));
        return true;
    }

    public List<String> getReservedCategoryList() {
        GosLog.d(LOG_TAG, "getReservedCategoryList: ");
        List<CategoryUpdateReserved> all = DbHelper.getInstance().getCategoryUpdateReservedDao().getAll();
        if (all == null || all.size() == 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (CategoryUpdateReserved categoryUpdateReserved : all) {
            arrayList.add(categoryUpdateReserved.pkgName);
        }
        return arrayList;
    }

    public int delete(String str) {
        return DbHelper.getInstance().getCategoryUpdateReservedDao().delete(str);
    }
}
