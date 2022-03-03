package com.samsung.android.game.gos.data.dao;

import android.database.sqlite.SQLiteFullException;
import com.samsung.android.game.gos.data.model.ClearBGSurviveApps;
import com.samsung.android.game.gos.util.GosLog;
import java.util.ArrayList;
import java.util.List;

public abstract class ClearBGSurviveAppsDao {
    private static final String LOG_TAG = ClearBGSurviveAppsDao.class.getSimpleName();

    /* access modifiers changed from: protected */
    public abstract void _deleteWithCallerPkgName(String str);

    /* access modifiers changed from: protected */
    public abstract void _insertAll(List<ClearBGSurviveApps> list);

    public abstract List<String> getSurviveAppList();

    public void deleteWithCallerPkgName(String str) {
        try {
            _deleteWithCallerPkgName(str);
        } catch (SQLiteFullException unused) {
        }
    }

    public void setSurviveList(String str, List<String> list) {
        if (str != null && list != null) {
            deleteWithCallerPkgName(str);
            ArrayList arrayList = new ArrayList();
            for (String next : list) {
                if (next != null) {
                    arrayList.add(new ClearBGSurviveApps(str, next));
                }
            }
            try {
                _insertAll(arrayList);
            } catch (SQLiteFullException e) {
                GosLog.w(LOG_TAG, (Throwable) e);
            }
        }
    }
}
