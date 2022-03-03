package com.samsung.android.game.gos.data.model;

public class EventSubscription {
    private String eventName;
    public int flags;
    public String intentActionName;
    public String receiver_type = "b";
    private String subscriberPkgName;

    public enum EVENTS {
        GAME_INSTALLED,
        GAME_RESUMED,
        GAME_PAUSED,
        GAME_UNINSTALLED,
        GAME_REPLACED,
        CATEGORY_CHANGED,
        MONITORED_APP_INSTALLED,
        MONITORED_APP_UNINSTALLED,
        MONITORED_APP_REPLACED,
        SERVER_SYNCED,
        GLOBAL_DATA_UPDATED,
        PACKAGE_DATA_UPDATED
    }

    public EventSubscription(String str, String str2) {
        this.eventName = str;
        this.subscriberPkgName = str2;
    }

    public String getEventName() {
        return this.eventName;
    }

    public void setEventName(String str) {
        this.eventName = str;
    }

    public String getSubscriberPkgName() {
        return this.subscriberPkgName;
    }

    public void setSubscriberPkgName(String str) {
        this.subscriberPkgName = str;
    }

    public String toString() {
        return "{" + this.eventName + "," + this.subscriberPkgName + "}";
    }

    public static class SubscriberPkgName {
        public String subscriberPkgName;

        public SubscriberPkgName(String str) {
            this.subscriberPkgName = str;
        }
    }
}
