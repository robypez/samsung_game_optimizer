package com.samsung.android.game.gos.data;

import android.util.ArrayMap;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.FeatureHelper;
import com.samsung.android.game.gos.data.model.FeatureFlag;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.Constants;
import com.samsung.android.knox.SemPersonaManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class PkgData {
    private static final String LOG_TAG = "PkgData";
    private Map<String, FeatureFlag> featureFlagMap;
    private Package pkg;

    public PkgData(Package packageR, Map<String, FeatureFlag> map) {
        this.pkg = packageR;
        this.featureFlagMap = map;
    }

    public Package getPkg() {
        return this.pkg;
    }

    public Map<String, FeatureFlag> getFeatureFlagMap() {
        return this.featureFlagMap;
    }

    public void setFeatureFlagMap(Map<String, FeatureFlag> map) {
        this.featureFlagMap = map;
    }

    public String getPackageName() {
        return this.pkg.pkgName;
    }

    public String getCategoryCode() {
        return this.pkg.categoryCode;
    }

    public boolean isGame() {
        return Constants.CategoryCode.GAME.equals(this.pkg.categoryCode);
    }

    public boolean isSecGameFamily() {
        return Constants.CategoryCode.SEC_GAME_FAMILY.equals(this.pkg.categoryCode);
    }

    public boolean isManagedApp() {
        return Constants.CategoryCode.MANAGED_APP.equals(this.pkg.categoryCode);
    }

    public int getDefaultCpuLevel() {
        return this.pkg.defaultCpuLevel;
    }

    public int getDefaultGpuLevel() {
        return this.pkg.defaultGpuLevel;
    }

    public String getSiopModePolicy() {
        return this.pkg.siopModePolicy;
    }

    public int getCustomSiopMode() {
        return this.pkg.customSiopMode;
    }

    public void setCategoryCode(String str) {
        this.pkg.categoryCode = str;
    }

    public boolean isPositiveFeature(String str) {
        FeatureFlag featureFlag;
        if (!FeatureHelper.isAvailable(str) || (featureFlag = this.featureFlagMap.get(str)) == null) {
            return false;
        }
        GosLog.d(LOG_TAG, "isPositiveFeature(). inheritedFlag: " + featureFlag.isInheritedFlag() + ", forcedFlag: " + featureFlag.isForcedFlag() + ", enabledFlagByServer: " + featureFlag.isEnabledFlagByServer());
        if (featureFlag.isInheritedFlag() || !featureFlag.isForcedFlag() || featureFlag.isEnabledFlagByServer()) {
            return true;
        }
        return false;
    }

    public void mergeFeatureFlagList(Map<String, FeatureFlag> map) {
        for (FeatureFlag next : this.featureFlagMap.values()) {
            FeatureFlag featureFlag = map.get(next.getName());
            if (featureFlag != null) {
                next.setState(featureFlag.getState());
                next.setInheritedFlag(featureFlag.isInheritedFlag());
                next.setForcedFlag(featureFlag.isForcedFlag());
                next.setEnabledFlagByServer(featureFlag.isEnabledFlagByServer());
                map.remove(next.getName());
            }
        }
        for (Map.Entry next2 : map.entrySet()) {
            this.featureFlagMap.put(next2.getKey(), next2.getValue());
        }
    }

    public Map<String, Boolean> getActualFeatureFlagMap() {
        ArrayMap arrayMap = new ArrayMap(this.featureFlagMap.size());
        for (FeatureFlag next : this.featureFlagMap.values()) {
            if (next != null) {
                boolean calculateFinalEnabled = FeatureFlagUtil.calculateFinalEnabled(next, DbHelper.getInstance().getGlobalFeatureFlagDao());
                next.setEnabled(calculateFinalEnabled);
                arrayMap.put(next.getName(), Boolean.valueOf(calculateFinalEnabled));
            }
        }
        return arrayMap;
    }

    public Integer[] getInstalledUserIds() {
        return this.pkg.getInstalledUserIds();
    }

    public void setInstalledUserIds(Integer[] numArr) {
        this.pkg.setInstalledUserIds(numArr);
    }

    public boolean isInstalledSecureFolder() {
        Integer[] installedUserIds = getInstalledUserIds();
        if (installedUserIds != null) {
            for (Integer num : installedUserIds) {
                if (num != null && SemPersonaManager.isSecureFolderId(num.intValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addInstalledUserId(int i) {
        Package packageR = this.pkg;
        if (packageR != null && packageR.getInstalledUserIds() != null) {
            ArrayList arrayList = new ArrayList(Arrays.asList(this.pkg.getInstalledUserIds()));
            if (!arrayList.contains(Integer.valueOf(i))) {
                arrayList.add(Integer.valueOf(i));
            }
            setInstalledUserIds((Integer[]) arrayList.toArray(new Integer[0]));
        } else if (this.pkg != null) {
            setInstalledUserIds(new Integer[]{Integer.valueOf(i)});
        }
    }
}
