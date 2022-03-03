package com.samsung.android.game.gos.data.model;

public class MonitoredApps {
    public String pkgName;
    public String subscriberPkgName;

    public static class SubscriberPkgName {
        public String subscriberPkgName;

        public SubscriberPkgName(String str) {
            this.subscriberPkgName = str;
        }
    }
}
