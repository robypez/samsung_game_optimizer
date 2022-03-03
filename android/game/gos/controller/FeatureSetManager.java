package com.samsung.android.game.gos.controller;

import android.content.Context;
import android.util.ArrayMap;
import com.samsung.android.game.gos.feature.CommonInterface;
import com.samsung.android.game.gos.feature.NetworkEventInterface;
import com.samsung.android.game.gos.feature.RuntimeInterface;
import com.samsung.android.game.gos.feature.ScheduledInterface;
import com.samsung.android.game.gos.feature.StaticInterface;
import com.samsung.android.game.gos.feature.autocontrol.AutoControlFeature;
import com.samsung.android.game.gos.feature.clearbgprocess.ClearBGProcessFeature;
import com.samsung.android.game.gos.feature.cmhstop.CmhStopFeature;
import com.samsung.android.game.gos.feature.dfs.DfsFeature;
import com.samsung.android.game.gos.feature.dss.DssFeature;
import com.samsung.android.game.gos.feature.externalsdk.ExternalSdkFeature;
import com.samsung.android.game.gos.feature.gfi.GfiFeature;
import com.samsung.android.game.gos.feature.governorsettings.GovernorSettingsFeature;
import com.samsung.android.game.gos.feature.ipm.IpmFeature;
import com.samsung.android.game.gos.feature.limitbgnetwork.LimitBGNetworkFeature;
import com.samsung.android.game.gos.feature.mdnie.MdnieFeature;
import com.samsung.android.game.gos.feature.mdswitchwifiblock.MDSwitchWifiBlockFeature;
import com.samsung.android.game.gos.feature.networkcontrol.WifiQosFeature;
import com.samsung.android.game.gos.feature.renderthreadaffinity.RenderThreadAffinityFeature;
import com.samsung.android.game.gos.feature.resumeboost.LaunchBoostFeature;
import com.samsung.android.game.gos.feature.resumeboost.ResumeBoostFeature;
import com.samsung.android.game.gos.feature.ringlog.RinglogFeature;
import com.samsung.android.game.gos.feature.siopmode.SiopModeFeature;
import com.samsung.android.game.gos.feature.touchboost.TouchBoostFeature;
import com.samsung.android.game.gos.feature.tsp.TspFeature;
import com.samsung.android.game.gos.feature.volumecontrol.VolumeControlFeature;
import com.samsung.android.game.gos.feature.vrr.VrrFeature;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.value.Constants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class FeatureSetManager {
    public static final String LOG_TAG = FeatureSetManager.class.getSimpleName();

    public static Map<String, RuntimeInterface> getRuntimeFeatureMap(Context context) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put(Constants.V4FeatureFlag.LAUNCH_BOOST, LaunchBoostFeature.getInstance());
        linkedHashMap.put(Constants.V4FeatureFlag.RESUME_BOOST, ResumeBoostFeature.getInstance());
        linkedHashMap.put("volume_control", VolumeControlFeature.getInstance());
        linkedHashMap.put(Constants.V4FeatureFlag.BOOST_TOUCH, TouchBoostFeature.getInstance());
        linkedHashMap.put(Constants.V4FeatureFlag.GALLERY_CMH_STOP, CmhStopFeature.getInstance());
        linkedHashMap.put("ipm", IpmFeature.getInstance(context));
        linkedHashMap.put(Constants.V4FeatureFlag.DFS, DfsFeature.getInstance());
        linkedHashMap.put(Constants.V4FeatureFlag.EXTERNAL_SDK, ExternalSdkFeature.getInstance());
        linkedHashMap.put(Constants.V4FeatureFlag.WIFI_QOS, WifiQosFeature.getInstance());
        linkedHashMap.put(Constants.V4FeatureFlag.CLEAR_BG_PROCESS, ClearBGProcessFeature.getInstance());
        linkedHashMap.put(Constants.V4FeatureFlag.RENDER_THREAD_AFFINITY, RenderThreadAffinityFeature.getInstance());
        linkedHashMap.put("limit_bg_network", LimitBGNetworkFeature.getInstance());
        linkedHashMap.put(Constants.V4FeatureFlag.MDNIE, MdnieFeature.getInstance());
        linkedHashMap.put(Constants.V4FeatureFlag.TSP, TspFeature.getInstance());
        linkedHashMap.put(Constants.V4FeatureFlag.AUTO_CONTROL, AutoControlFeature.getInstance());
        linkedHashMap.put(Constants.V4FeatureFlag.MD_SWITCH_WIFI, MDSwitchWifiBlockFeature.getInstance());
        linkedHashMap.put(Constants.V4FeatureFlag.GFI, GfiFeature.getInstance(context));
        linkedHashMap.put(Constants.V4FeatureFlag.RINGLOG, RinglogFeature.getInstance());
        Iterator it = linkedHashMap.keySet().iterator();
        boolean z = false;
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            String str = (String) it.next();
            if (!str.equals("ipm")) {
                if (str.equals(Constants.V4FeatureFlag.GFI) && !z) {
                    GosLog.w(LOG_TAG, "GFI comes before SPA in the RuntimeFeatureMap - this means GFI cannot query SPA's status correctly!");
                    break;
                }
            } else {
                z = true;
            }
        }
        return linkedHashMap;
    }

    public static Map<String, StaticInterface> getStaticFeatureMap(Context context) {
        HashMap hashMap = new HashMap();
        hashMap.put(Constants.V4FeatureFlag.GOVERNOR_SETTINGS, GovernorSettingsFeature.getInstance());
        hashMap.put("siop_mode", SiopModeFeature.getInstance());
        hashMap.put(Constants.V4FeatureFlag.RESOLUTION, DssFeature.getInstance());
        hashMap.put(Constants.V4FeatureFlag.VRR, VrrFeature.getInstance());
        return hashMap;
    }

    public static Map<String, ScheduledInterface> getScheduledFeatureMap(Context context) {
        return new HashMap();
    }

    public static Map<String, NetworkEventInterface> getNetworkEventFeatureMap(Context context) {
        return new HashMap();
    }

    private static Map<String, CommonInterface> getCommonFeatureMap(Context context) {
        HashMap hashMap = new HashMap();
        hashMap.putAll(getRuntimeFeatureMap(context));
        hashMap.putAll(getStaticFeatureMap(context));
        hashMap.putAll(getScheduledFeatureMap(context));
        hashMap.putAll(getNetworkEventFeatureMap(context));
        return hashMap;
    }

    public static Map<String, Boolean> getAvailableFeatureFlagMap(Context context) {
        Collection<CommonInterface> values = getCommonFeatureMap(context).values();
        ArrayMap arrayMap = new ArrayMap(values.size());
        arrayMap.put(Constants.V4FeatureFlag.ALLOW_MORE_HEAT, Boolean.valueOf(IpmFeature.isAllowMoreHeatAvailable()));
        for (CommonInterface next : values) {
            if (next.isAvailableForSystemHelper()) {
                arrayMap.put(next.getName(), true);
            } else {
                arrayMap.put(next.getName(), false);
            }
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (Map.Entry entry : arrayMap.entrySet()) {
            if (((Boolean) entry.getValue()).booleanValue()) {
                arrayList.add(entry.getKey());
            } else {
                arrayList2.add(entry.getKey());
            }
        }
        GosLog.i(LOG_TAG, String.format(Locale.US, "final feature available list: %navailable(%s), %nunavailable(%s)", new Object[]{TypeConverter.stringsToCsv((Iterable<String>) arrayList), TypeConverter.stringsToCsv((Iterable<String>) arrayList2)}));
        return arrayMap;
    }

    private FeatureSetManager() {
    }
}
