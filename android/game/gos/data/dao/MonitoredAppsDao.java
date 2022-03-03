package com.samsung.android.game.gos.data.dao;

import android.database.sqlite.SQLiteFullException;
import com.samsung.android.game.gos.data.model.MonitoredApps;
import java.util.List;

public abstract class MonitoredAppsDao {
    /* access modifiers changed from: protected */
    public abstract void _deleteSubscriber(MonitoredApps.SubscriberPkgName subscriberPkgName);

    /* access modifiers changed from: protected */
    public abstract void _insertAll(List<MonitoredApps> list);

    public abstract List<String> getMonitoredAppsBySubscriberPkgName(String str);

    public abstract List<String> getSubscriberAppsByMonitoredAppPkgName(String str);

    public void deleteSubscriber(MonitoredApps.SubscriberPkgName subscriberPkgName) {
        try {
            _deleteSubscriber(subscriberPkgName);
        } catch (SQLiteFullException unused) {
        }
    }

    public void insertAll(List<MonitoredApps> list) {
        try {
            _insertAll(list);
        } catch (SQLiteFullException unused) {
        }
    }
}
