package com.samsung.android.game.gos.test.fragment.feature;

import com.samsung.android.game.gos.value.Constants;
import java.util.ArrayList;

public class FeatureFragmentUtil {
    public static ArrayList<FeatureInfo> getFeatureInfoArrayList() {
        ArrayList<FeatureInfo> arrayList = new ArrayList<>();
        arrayList.add(new FeatureInfo(Constants.V4FeatureFlag.RESOLUTION, "DSS"));
        arrayList.add(new FeatureInfo(Constants.V4FeatureFlag.DFS, "DFS"));
        arrayList.add(new FeatureInfo(Constants.V4FeatureFlag.RENDER_THREAD_AFFINITY, "RENDER_THREAD_AFFINITY"));
        arrayList.add(new FeatureInfo(Constants.V4FeatureFlag.MDNIE, "MDNIE"));
        arrayList.add(new FeatureInfo(Constants.V4FeatureFlag.BOOST_TOUCH, "TOUCH_BOOST"));
        arrayList.add(new FeatureInfo("volume_control", "VOLUME_CONTROL"));
        arrayList.add(new FeatureInfo(Constants.V4FeatureFlag.GALLERY_CMH_STOP, "CMH_STOP"));
        arrayList.add(new FeatureInfo(Constants.V4FeatureFlag.CLEAR_BG_PROCESS, "CLEAR_BG_PROCESS"));
        arrayList.add(new FeatureInfo("siop_mode", "SIOP_MODE"));
        arrayList.add(new FeatureInfo(Constants.V4FeatureFlag.GOVERNOR_SETTINGS, "GOVERNOR_SETTINGS"));
        arrayList.add(new FeatureInfo("ipm", "SPA"));
        arrayList.add(new FeatureInfo(Constants.V4FeatureFlag.EXTERNAL_SDK, "EXTERNAL_SDK"));
        arrayList.add(new FeatureInfo(Constants.V4FeatureFlag.RESUME_BOOST, "RESUME_BOOST"));
        arrayList.add(new FeatureInfo(Constants.V4FeatureFlag.LAUNCH_BOOST, "LAUNCH_BOOST"));
        arrayList.add(new FeatureInfo(Constants.V4FeatureFlag.WIFI_QOS, "WIFI_QOS"));
        arrayList.add(new FeatureInfo("limit_bg_network", "LIMIT_BG_NETWORK"));
        arrayList.add(new FeatureInfo(Constants.V4FeatureFlag.GFI, "GFI"));
        arrayList.add(new FeatureInfo(Constants.V4FeatureFlag.VRR, "VRR"));
        arrayList.add(new FeatureInfo(Constants.V4FeatureFlag.TSP, "TSP"));
        arrayList.add(new FeatureInfo(Constants.V4FeatureFlag.AUTO_CONTROL, "AUTO_CONTROL"));
        arrayList.add(new FeatureInfo(Constants.V4FeatureFlag.MD_SWITCH_WIFI, "MD_SWITCH_WIFI"));
        arrayList.add(new FeatureInfo(Constants.V4FeatureFlag.ALLOW_MORE_HEAT, "ALLOW_MORE_HEAT"));
        arrayList.add(new FeatureInfo(Constants.V4FeatureFlag.RINGLOG, "RINGLOG"));
        return arrayList;
    }
}
