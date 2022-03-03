package com.samsung.android.game.gos.data.dbhelper;

import com.samsung.android.game.gos.data.model.EventSubscription;
import com.samsung.android.game.gos.data.model.MonitoredApps;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.TypeConverter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventSubscriptionDbHelper {
    private static final String LOG_TAG = "EventSubscriptionDbHelper";

    private EventSubscriptionDbHelper() {
    }

    public static EventSubscriptionDbHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final EventSubscriptionDbHelper INSTANCE = new EventSubscriptionDbHelper();

        private SingletonHolder() {
        }
    }

    public boolean setSubscriber(EventSubscription eventSubscription, Set<String> set) {
        GosLog.d(LOG_TAG, "setSubscriber(" + eventSubscription.getSubscriberPkgName() + ", " + eventSubscription.intentActionName + ", events: " + TypeConverter.stringsToCsv((Iterable<String>) set) + ")");
        DbHelper.getInstance().getEventSubscriptionDao().deleteSubscriber(new EventSubscription.SubscriberPkgName(eventSubscription.getSubscriberPkgName()));
        if (set == null) {
            String format = String.format("setSubscriber()-events is null (getSubscriberPkgName=%s)", new Object[]{eventSubscription.getSubscriberPkgName()});
            GosLog.w(LOG_TAG, format);
            LocalLogDbHelper.getInstance().addLocalLog(LOG_TAG, format);
            return false;
        }
        ArrayList arrayList = new ArrayList();
        for (String eventSubscription2 : set) {
            EventSubscription eventSubscription3 = new EventSubscription(eventSubscription2, eventSubscription.getSubscriberPkgName());
            eventSubscription3.intentActionName = eventSubscription.intentActionName;
            eventSubscription3.flags = eventSubscription.flags;
            eventSubscription3.receiver_type = eventSubscription.receiver_type;
            arrayList.add(eventSubscription3);
        }
        if (arrayList.size() > 0) {
            DbHelper.getInstance().getEventSubscriptionDao().insertAll(arrayList);
        }
        return true;
    }

    public Map<String, EventSubscription> getSubscriberListOfEvent(String str) {
        GosLog.d(LOG_TAG, "getSubscriberListOfEvent(" + str + ")");
        HashMap hashMap = new HashMap();
        for (EventSubscription next : DbHelper.getInstance().getEventSubscriptionDao().getSubscriberListOfEvent(str)) {
            hashMap.put(next.getSubscriberPkgName(), next);
        }
        return hashMap;
    }

    public void setMonitoredApps(String str, Set<String> set) {
        GosLog.d(LOG_TAG, "setMonitoredApps(" + str + ", pkgNames: " + TypeConverter.stringsToCsv((Iterable<String>) set) + ")");
        DbHelper.getInstance().getMonitoredAppsDao().deleteSubscriber(new MonitoredApps.SubscriberPkgName(str));
        ArrayList arrayList = new ArrayList();
        for (String str2 : set) {
            MonitoredApps monitoredApps = new MonitoredApps();
            monitoredApps.subscriberPkgName = str;
            monitoredApps.pkgName = str2;
            arrayList.add(monitoredApps);
        }
        if (arrayList.size() > 0) {
            DbHelper.getInstance().getMonitoredAppsDao().insertAll(arrayList);
        }
    }

    public List<String> getSubscriberApps(String str) {
        GosLog.d(LOG_TAG, "getSubscriberApps(" + str + ")");
        return DbHelper.getInstance().getMonitoredAppsDao().getSubscriberAppsByMonitoredAppPkgName(str);
    }

    public List<String> getMonitoredApps(String str) {
        GosLog.d(LOG_TAG, "getMonitoredApps(" + str + ")");
        return DbHelper.getInstance().getMonitoredAppsDao().getMonitoredAppsBySubscriberPkgName(str);
    }
}
