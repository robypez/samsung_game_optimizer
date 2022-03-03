package com.samsung.android.game.gos.endpoint;

import android.content.Context;
import android.content.pm.PackageManager;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.controller.EventPublisher;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.EventSubscriptionDbHelper;
import com.samsung.android.game.gos.data.dbhelper.LocalLogDbHelper;
import com.samsung.android.game.gos.data.model.EventSubscription;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.value.Constants;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

class EventCommand {
    private static final String LOG_TAG = EventCommand.class.getSimpleName();

    EventCommand() {
    }

    /* access modifiers changed from: package-private */
    public String setMonitoredApps(String str, String str2) {
        String string;
        String str3 = LOG_TAG;
        GosLog.i(str3, "setMonitoredApps(), jsonParam: " + str);
        try {
            JSONObject jSONObject = new JSONObject(str);
            JSONObject jSONObject2 = new JSONObject();
            if (!jSONObject.has(GosInterface.KeyName.PACKAGE_NAMES)) {
                jSONObject2.put(GosInterface.KeyName.SUCCESSFUL, false).put(GosInterface.KeyName.COMMENT, "wrong parameter");
                return jSONObject2.toString();
            }
            if (jSONObject.has(GosInterface.KeyName.SUBSCRIBER_NAME) && (string = jSONObject.getString(GosInterface.KeyName.SUBSCRIBER_NAME)) != null) {
                str2 = string;
            }
            Set<String> csvToStringSet = TypeConverter.csvToStringSet(jSONObject.getString(GosInterface.KeyName.PACKAGE_NAMES));
            if (str2 != null) {
                if (csvToStringSet != null) {
                    EventSubscriptionDbHelper.getInstance().setMonitoredApps(str2, csvToStringSet);
                    jSONObject2.put(GosInterface.KeyName.SUCCESSFUL, true);
                    return jSONObject2.toString();
                }
            }
            jSONObject2.put(GosInterface.KeyName.SUCCESSFUL, false).put(GosInterface.KeyName.COMMENT, "wrong parameter");
            return jSONObject2.toString();
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public String subscribeEvents(String str, String str2) {
        String string;
        String str3 = LOG_TAG;
        GosLog.i(str3, "subscribeEvents(), jsonParam: " + str);
        try {
            JSONObject jSONObject = new JSONObject(str);
            JSONObject jSONObject2 = new JSONObject();
            if (jSONObject.has(GosInterface.KeyName.EVENTS)) {
                if (jSONObject.has(GosInterface.KeyName.INTENT_ACTION_NAME)) {
                    Set<String> csvToStringSet = TypeConverter.csvToStringSet(jSONObject.getString(GosInterface.KeyName.EVENTS));
                    String string2 = jSONObject.getString(GosInterface.KeyName.INTENT_ACTION_NAME);
                    if (jSONObject.has(GosInterface.KeyName.SUBSCRIBER_NAME) && (string = jSONObject.getString(GosInterface.KeyName.SUBSCRIBER_NAME)) != null) {
                        str2 = string;
                    }
                    int optInt = jSONObject.optInt(GosInterface.KeyName.FLAGS, 0);
                    String optString = jSONObject.optString(GosInterface.KeyName.RECEIVER_TYPE, "b");
                    if (str2 != null) {
                        if (string2 != null) {
                            EventSubscription eventSubscription = new EventSubscription("dummy", str2);
                            eventSubscription.intentActionName = string2;
                            eventSubscription.flags = optInt;
                            eventSubscription.receiver_type = optString;
                            if (csvToStringSet != null) {
                                if (csvToStringSet.contains(EventSubscription.EVENTS.GAME_INSTALLED.toString())) {
                                    publishFirstEvent(EventSubscription.EVENTS.GAME_INSTALLED, eventSubscription);
                                }
                                if (csvToStringSet.contains(EventSubscription.EVENTS.MONITORED_APP_INSTALLED.toString())) {
                                    publishFirstEvent(EventSubscription.EVENTS.MONITORED_APP_INSTALLED, eventSubscription);
                                }
                            }
                            EventSubscriptionDbHelper.getInstance().setSubscriber(eventSubscription, csvToStringSet);
                            jSONObject2.put(GosInterface.KeyName.SUCCESSFUL, true);
                            return jSONObject2.toString();
                        }
                    }
                    jSONObject2.put(GosInterface.KeyName.SUCCESSFUL, false).put(GosInterface.KeyName.COMMENT, "wrong parameter");
                    return jSONObject2.toString();
                }
            }
            jSONObject2.put(GosInterface.KeyName.SUCCESSFUL, false).put(GosInterface.KeyName.COMMENT, "wrong parameter");
            return jSONObject2.toString();
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public void publishFirstEvent(EventSubscription.EVENTS events, EventSubscription eventSubscription) {
        String str = LOG_TAG;
        GosLog.d(str, "publishFirstEvent(), subscriberPkgName: " + eventSubscription.getSubscriberPkgName() + ", intentActionName: " + eventSubscription.intentActionName + ", event: " + events);
        if (eventSubscription.getSubscriberPkgName() != null && eventSubscription.intentActionName != null && events != null) {
            Map<String, EventSubscription> subscriberListOfEvent = EventSubscriptionDbHelper.getInstance().getSubscriberListOfEvent(events.toString());
            if (subscriberListOfEvent.containsKey(eventSubscription.getSubscriberPkgName())) {
                GosLog.d(LOG_TAG, "publishFirstEvent(), it's not first subscribe. no need to publish");
                return;
            }
            HashMap hashMap = new HashMap();
            hashMap.put(EventPublisher.EXTRA_KEY_EVENT_FOR_FIRST_SUBSCRIPTION, String.valueOf(true));
            List<String> list = null;
            if (EventSubscription.EVENTS.GAME_INSTALLED == events) {
                list = DbHelper.getInstance().getPackageDao().getPkgNameListByCategory(Constants.CategoryCode.GAME);
            } else if (EventSubscription.EVENTS.MONITORED_APP_INSTALLED == events) {
                list = new ArrayList<>();
                for (String next : EventSubscriptionDbHelper.getInstance().getMonitoredApps(eventSubscription.getSubscriberPkgName())) {
                    Package packageR = DbHelper.getInstance().getPackageDao().getPackage(next);
                    if (packageR != null && !packageR.getCategoryCode().equalsIgnoreCase(Constants.CategoryCode.GAME)) {
                        list.add(next);
                    }
                }
            }
            int i = 0;
            String format = String.format("publishFirstEvent()-(event(%s), subscriberListOfEvent.size=%d, es.getSubscriberPkgName()=%s)", new Object[]{events, Integer.valueOf(subscriberListOfEvent.size()), eventSubscription.getSubscriberPkgName()});
            GosLog.i(LOG_TAG, format);
            LocalLogDbHelper.getInstance().addLocalLog(LOG_TAG, format);
            PackageManager packageManager = AppContext.get().getPackageManager();
            if (!(list == null || packageManager == null)) {
                for (String str2 : list) {
                    try {
                        packageManager.getPackageInfo(str2, 128);
                        EventPublisher.publishEvent((Context) AppContext.get(), eventSubscription, events.toString(), str2, (Map<String, String>) hashMap);
                        i++;
                    } catch (PackageManager.NameNotFoundException unused) {
                    }
                }
            }
            String str3 = LOG_TAG;
            GosLog.i(str3, "publishFirstEvent(), publishedCount: " + i);
        }
    }
}
