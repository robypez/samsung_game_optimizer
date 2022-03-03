package com.samsung.android.game.gos.data;

import com.samsung.android.game.gos.data.dao.GlobalFeatureFlagDao;
import com.samsung.android.game.gos.data.model.FeatureFlag;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.data.model.GlobalFeatureFlag;
import com.samsung.android.game.gos.value.Constants;
import java.util.Map;

public class FeatureFlagUtil {
    private static final String LOG_TAG = "FeatureFlagUtil";

    public static boolean fillMissingFeatureFlag(Map<String, FeatureFlag> map, String str) {
        int i = 0;
        for (String next : Constants.V4FeatureFlag.V4_FEATURE_FLAG_NAMES) {
            if (!map.containsKey(next)) {
                map.put(next, new FeatureFlag(next, str, "inherited"));
                i++;
            }
        }
        if (i > 0) {
            return true;
        }
        return false;
    }

    public static boolean calculateFinalEnabled(FeatureFlag featureFlag, GlobalFeatureFlagDao globalFeatureFlagDao) {
        String name = featureFlag.getName();
        GlobalFeatureFlag globalFeatureFlag = globalFeatureFlagDao.get(name);
        if (globalFeatureFlag == null || !globalFeatureFlag.available) {
            return false;
        }
        if (featureFlag.isInheritedFlag()) {
            if (globalFeatureFlag.inheritedFlag) {
                if (Global.DefaultGlobalData.isForcedByDefault(name)) {
                    return Global.DefaultGlobalData.isEnabledByDefault(name);
                }
            } else if (globalFeatureFlag.forcedFlag) {
                return globalFeatureFlag.enabledFlagByServer;
            }
        } else if (featureFlag.isForcedFlag()) {
            return featureFlag.isEnabledFlagByServer();
        }
        if (globalFeatureFlag.usingUserValue) {
            if (globalFeatureFlag.usingPkgValue) {
                return featureFlag.isEnabledFlagByUser();
            }
            return globalFeatureFlag.enabledFlagByUser;
        } else if (!featureFlag.isInheritedFlag()) {
            return featureFlag.isEnabledFlagByServer();
        } else {
            if (globalFeatureFlag.inheritedFlag) {
                return Global.DefaultGlobalData.isEnabledByDefault(name);
            }
            return globalFeatureFlag.enabledFlagByServer;
        }
    }
}
