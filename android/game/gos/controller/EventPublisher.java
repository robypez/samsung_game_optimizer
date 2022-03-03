package com.samsung.android.game.gos.controller;

import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import com.samsung.android.game.gos.data.dbhelper.EventSubscriptionDbHelper;
import com.samsung.android.game.gos.data.dbhelper.LocalLogDbHelper;
import com.samsung.android.game.gos.data.model.EventSubscription;
import com.samsung.android.game.gos.util.GosLog;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class EventPublisher {
    public static final String EXTRA_KEY_EVENT_FOR_FIRST_SUBSCRIPTION = "eventForFirstSubscription";
    public static final String EXTRA_KEY_IS_CREATE = "isCreate";
    public static final String EXTRA_KEY_NEW_CATEGORY = "newCategory";
    public static final String EXTRA_KEY_PKG_NAME = "pkgName";
    public static final String EXTRA_KEY_PREV_CATEGORY = "prevCategory";
    public static final String EXTRA_KEY_SERVER_CATEGORY = "serverCategory";
    public static final String EXTRA_KEY_TYPE = "type";
    public static final String EXTRA_KEY_USER_ID = "userId";
    private static final String LOG_TAG = "EventPublisher";

    public static void publishEvent(Context context, EventSubscription eventSubscription, String str, String str2, Map<String, String> map) {
        String format = String.format("publishEvent()-(type=%s, pkgName=%s) to (subscriberInfo=%s)", new Object[]{str, str2, eventSubscription});
        GosLog.i(LOG_TAG, format);
        Intent intent = new Intent(eventSubscription.intentActionName);
        intent.setPackage(eventSubscription.getSubscriberPkgName());
        intent.putExtra("type", str);
        intent.putExtra(EXTRA_KEY_PKG_NAME, str2);
        intent.setFlags(eventSubscription.flags);
        if (map != null) {
            for (Map.Entry next : map.entrySet()) {
                intent.putExtra((String) next.getKey(), (String) next.getValue());
            }
        }
        if (isReceiverTypeService(eventSubscription.receiver_type)) {
            context.startForegroundService(intent);
        } else {
            context.sendBroadcast(intent);
        }
        LocalLogDbHelper.getInstance().addLocalLog(LOG_TAG, format);
    }

    public static void publishEvent(Context context, Map<String, EventSubscription> map, String str, String str2, Map<String, String> map2) {
        String format = String.format("publishEvent()-(type=%s, pkgName=%s) to (subscriberInfo=%s)", new Object[]{str, str2, map});
        GosLog.i(LOG_TAG, format);
        if (map.size() >= 1) {
            Intent intent = new Intent();
            intent.putExtra("type", str);
            if (str2 != null) {
                intent.putExtra(EXTRA_KEY_PKG_NAME, str2);
            }
            if (map2 != null) {
                for (Map.Entry next : map2.entrySet()) {
                    intent.putExtra((String) next.getKey(), (String) next.getValue());
                }
            }
            for (Map.Entry next2 : map.entrySet()) {
                intent.setPackage((String) next2.getKey());
                intent.setAction(((EventSubscription) next2.getValue()).intentActionName);
                intent.setFlags(((EventSubscription) next2.getValue()).flags);
                if (isReceiverTypeService(((EventSubscription) next2.getValue()).receiver_type)) {
                    context.startForegroundService(intent);
                } else {
                    context.sendBroadcast(intent);
                }
            }
            LocalLogDbHelper.getInstance().addLocalLog(LOG_TAG, format);
        }
    }

    private static boolean isReceiverTypeService(String str) {
        return Objects.equals(NotificationCompat.CATEGORY_SERVICE, str) || Objects.equals("s", str);
    }

    public static void publishCategoryChangedEventList(Context context, List<String> list, List<String> list2, List<String> list3) {
        int size;
        if (list != null && list2 != null && list3 != null && (size = list.size()) == list2.size() && size == list3.size()) {
            StringBuilder sb = new StringBuilder("publishCategoryChangedEventList()-");
            for (int i = 0; i < list.size(); i++) {
                String str = list.get(i);
                String str2 = list2.get(i);
                String str3 = list3.get(i);
                String format = String.format("(pkgName=%s, from=%s, to=%s), ", new Object[]{str, str2, str3});
                Map<String, EventSubscription> subscriberListOfEvent = EventSubscriptionDbHelper.getInstance().getSubscriberListOfEvent(EventSubscription.EVENTS.CATEGORY_CHANGED.toString());
                HashMap hashMap = new HashMap();
                hashMap.put(EXTRA_KEY_PREV_CATEGORY, str2);
                hashMap.put(EXTRA_KEY_NEW_CATEGORY, str3);
                hashMap.put(EXTRA_KEY_SERVER_CATEGORY, str3);
                publishEvent(context, subscriberListOfEvent, EventSubscription.EVENTS.CATEGORY_CHANGED.toString(), str, (Map<String, String>) hashMap);
                sb.append(format);
                if (sb.length() > 1024) {
                    LocalLogDbHelper.getInstance().addLocalLog(LOG_TAG, sb.toString());
                    sb = new StringBuilder("-");
                }
            }
            if (sb.length() > 1) {
                LocalLogDbHelper.getInstance().addLocalLog(LOG_TAG, sb.toString());
            }
        }
    }

    private EventPublisher() {
    }
}
