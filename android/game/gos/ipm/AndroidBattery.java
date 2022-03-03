package com.samsung.android.game.gos.ipm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class AndroidBattery {
    private static final String LOG_TAG = "AndroidBattery";
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                char c = 65535;
                int hashCode = action.hashCode();
                if (hashCode != -1886648615) {
                    if (hashCode == 1019184907 && action.equals("android.intent.action.ACTION_POWER_CONNECTED")) {
                        c = 0;
                    }
                } else if (action.equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
                    c = 1;
                }
                if (c == 0) {
                    Iterator it = AndroidBattery.this.mListeners.iterator();
                    while (it.hasNext()) {
                        ((Listener) it.next()).onPowerConnected();
                    }
                } else if (c == 1) {
                    Iterator it2 = AndroidBattery.this.mListeners.iterator();
                    while (it2.hasNext()) {
                        ((Listener) it2.next()).onPowerDisconnected();
                    }
                }
            }
        }
    };
    private final Context mContext;
    /* access modifiers changed from: private */
    public final ArrayList<Listener> mListeners = new ArrayList<>();
    private final Sysfs mSysfs;

    public interface Listener {
        void onPowerConnected();

        void onPowerDisconnected();
    }

    public AndroidBattery(Context context, Sysfs sysfs) {
        this.mContext = context;
        this.mSysfs = sysfs;
    }

    public void activate() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter);
    }

    public void deactivate() {
        try {
            this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        } catch (IllegalArgumentException e) {
            Log.e(LOG_TAG, e.getMessage());
        } catch (Exception unused) {
        }
    }

    public void register(Listener listener) {
        this.mListeners.add(listener);
    }

    public void deregister(Listener listener) {
        this.mListeners.remove(listener);
    }

    public boolean isPowerConnected() {
        Intent registerReceiver = this.mContext.registerReceiver((BroadcastReceiver) null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (registerReceiver == null || registerReceiver.getIntExtra("plugged", 0) == 0) {
            return false;
        }
        return true;
    }

    public int getUsbTemperature() {
        String readSysFile;
        String readSysFile2;
        Integer num = -1;
        File file = new File("/sys/class/power_supply/battery/usb_temp");
        if (file.exists() && file.canRead() && (readSysFile2 = this.mSysfs.readSysFile("/sys/class/power_supply/battery/usb_temp")) != null) {
            try {
                if (!readSysFile2.equals(BuildConfig.VERSION_NAME)) {
                    Integer valueOf = Integer.valueOf(readSysFile2.replace("\n", BuildConfig.VERSION_NAME));
                    Log.d(LOG_TAG, "tempHardLimit: Using USB temp " + valueOf);
                    return valueOf.intValue();
                }
            } catch (NumberFormatException unused) {
            }
        }
        File file2 = new File("/sys/class/power_supply/battery/temp");
        if (file2.exists() && file2.canRead() && (readSysFile = this.mSysfs.readSysFile("/sys/class/power_supply/battery/temp")) != null) {
            try {
                if (!readSysFile.equals(BuildConfig.VERSION_NAME)) {
                    Integer valueOf2 = Integer.valueOf(readSysFile.replace("\n", BuildConfig.VERSION_NAME));
                    Log.v(LOG_TAG, "Reading USB temp " + valueOf2);
                    return valueOf2.intValue();
                }
            } catch (NumberFormatException unused2) {
            }
        }
        if (num.intValue() == -1) {
            Log.v(LOG_TAG, "Unable to get USB Temp");
        }
        return num.intValue();
    }
}
