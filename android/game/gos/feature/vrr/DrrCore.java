package com.samsung.android.game.gos.feature.vrr;

import android.os.Looper;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.PreferenceHelper;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.ipm.AndroidDisplay;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.SecureSettingConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class DrrCore {
    private static final ArrayList<String> DRR_BLOCKED_LIST = new ArrayList<>(Arrays.asList(new String[]{"com.tencent.af", "com.tencent.lzhx"}));
    private static final String LOG_TAG = "DrrCore";
    private AndroidDisplay mAndroidDisplay;
    private final TreeMap<String, Integer> mLastTargetFPSTreeMap;
    private TreeMap<Integer, String> mRefreshRatesTreeMap;
    private VrrFeature mVrrInstance;

    /* access modifiers changed from: package-private */
    public int checkDrrValue(int i, int i2, int i3) {
        return i < i2 ? i2 : i > i3 ? i3 : i;
    }

    private DrrCore() {
        this.mRefreshRatesTreeMap = new TreeMap<>();
        this.mLastTargetFPSTreeMap = new TreeMap<>();
        this.mVrrInstance = null;
        this.mAndroidDisplay = new AndroidDisplay(AppContext.get(), Looper.getMainLooper());
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:13|14) */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        r4 = r3.mRefreshRatesTreeMap.lowerKey(java.lang.Integer.valueOf(r4)).intValue();
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x002d */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int changeTargetFPSToRefreshRate(int r4) {
        /*
            r3 = this;
            java.util.TreeMap<java.lang.Integer, java.lang.String> r0 = r3.mRefreshRatesTreeMap
            monitor-enter(r0)
            java.util.TreeMap<java.lang.Integer, java.lang.String> r1 = r3.mRefreshRatesTreeMap     // Catch:{ all -> 0x003f }
            int r1 = r1.size()     // Catch:{ all -> 0x003f }
            if (r1 != 0) goto L_0x000e
            monitor-exit(r0)     // Catch:{ all -> 0x003f }
            r4 = -1
            return r4
        L_0x000e:
            java.util.TreeMap<java.lang.Integer, java.lang.String> r1 = r3.mRefreshRatesTreeMap     // Catch:{ all -> 0x003f }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r4)     // Catch:{ all -> 0x003f }
            boolean r1 = r1.containsKey(r2)     // Catch:{ all -> 0x003f }
            if (r1 == 0) goto L_0x001c
            monitor-exit(r0)     // Catch:{ all -> 0x003f }
            return r4
        L_0x001c:
            java.util.TreeMap<java.lang.Integer, java.lang.String> r1 = r3.mRefreshRatesTreeMap     // Catch:{ NullPointerException -> 0x002d }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r4)     // Catch:{ NullPointerException -> 0x002d }
            java.lang.Object r1 = r1.higherKey(r2)     // Catch:{ NullPointerException -> 0x002d }
            java.lang.Integer r1 = (java.lang.Integer) r1     // Catch:{ NullPointerException -> 0x002d }
            int r4 = r1.intValue()     // Catch:{ NullPointerException -> 0x002d }
            goto L_0x003d
        L_0x002d:
            java.util.TreeMap<java.lang.Integer, java.lang.String> r1 = r3.mRefreshRatesTreeMap     // Catch:{ all -> 0x003f }
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ all -> 0x003f }
            java.lang.Object r4 = r1.lowerKey(r4)     // Catch:{ all -> 0x003f }
            java.lang.Integer r4 = (java.lang.Integer) r4     // Catch:{ all -> 0x003f }
            int r4 = r4.intValue()     // Catch:{ all -> 0x003f }
        L_0x003d:
            monitor-exit(r0)     // Catch:{ all -> 0x003f }
            return r4
        L_0x003f:
            r4 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x003f }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.vrr.DrrCore.changeTargetFPSToRefreshRate(int):int");
    }

    private static class SingletonHolder {
        public static DrrCore INSTANCE = new DrrCore();

        private SingletonHolder() {
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isAllowed48HzByUser() {
        return new PreferenceHelper().getValue(SecureSettingConstants.KEY_VRR_48HZ_ALLOWED, 0) != 0;
    }

    /* access modifiers changed from: package-private */
    public boolean isInExceptionList(String str) {
        return DRR_BLOCKED_LIST.contains(str);
    }

    /* access modifiers changed from: package-private */
    public int getLastRefreshRate(String str) {
        int intValue;
        synchronized (this.mLastTargetFPSTreeMap) {
            intValue = this.mLastTargetFPSTreeMap.containsKey(str) ? this.mLastTargetFPSTreeMap.get(str).intValue() : -1;
        }
        if (intValue > 0) {
            return changeTargetFPSToRefreshRate(intValue);
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public boolean isDrrAllowed(Package packageR) {
        int drrAllowed = DbHelper.getInstance().getGlobalDao().getDrrAllowed();
        int drrAllowed2 = packageR.getDrrAllowed();
        boolean z = false;
        if (drrAllowed2 != -1 ? drrAllowed2 != 0 : drrAllowed != 0) {
            z = true;
        }
        GosLog.d(LOG_TAG, packageR.getPkgName() + ": packageDrrAllowed = " + drrAllowed2 + ", globalDrrAllowed = " + drrAllowed + ",drrEnable =" + z);
        return z;
    }

    public static DrrCore getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void updateGameRefreshRate(int i, String str) {
        if (isInExceptionList(str)) {
            GosLog.w(LOG_TAG, str + " is blocked by drr");
            return;
        }
        Package packageR = DbHelper.getInstance().getPackageDao().getPackage(str);
        if (packageR == null) {
            GosLog.w(LOG_TAG, str + " is not a game");
        } else if (!isDrrAllowed(packageR)) {
            GosLog.w(LOG_TAG, str + " is not allowed by drr");
        } else {
            synchronized (this.mLastTargetFPSTreeMap) {
                this.mLastTargetFPSTreeMap.put(str, Integer.valueOf(i));
            }
            int changeTargetFPSToRefreshRate = changeTargetFPSToRefreshRate(i);
            if (changeTargetFPSToRefreshRate <= 0 || changeTargetFPSToRefreshRate == ((int) this.mAndroidDisplay.getRefreshRate())) {
                GosLog.w(LOG_TAG, "Don't set refresh rate. (targetFPS = " + i + ",refreshRate = " + changeTargetFPSToRefreshRate + ")");
                return;
            }
            try {
                if (!isAllowed48HzByUser()) {
                    if (this.mVrrInstance == null) {
                        this.mVrrInstance = VrrFeature.getInstance();
                    }
                    GosLog.d(LOG_TAG, "Drr call setVrr to set pkgName = " + str + ", targetFPS = " + i);
                    this.mVrrInstance.setVrr(str);
                }
            } catch (NullPointerException e) {
                GosLog.w(LOG_TAG, "DrrCore failed with exception: " + e);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public int getDrrValue(String str, int i, int i2, boolean z) {
        int lastRefreshRate;
        if (z || (lastRefreshRate = getLastRefreshRate(str)) <= 0) {
            return -1;
        }
        int checkDrrValue = checkDrrValue(lastRefreshRate, i, i2);
        GosLog.d(LOG_TAG, "getDrrValue pkgName = " + str + " ------ get DRR value is " + checkDrrValue);
        return checkDrrValue;
    }

    /* access modifiers changed from: package-private */
    public void updateModeList(Map<Integer, String> map) {
        synchronized (this.mRefreshRatesTreeMap) {
            this.mRefreshRatesTreeMap.putAll(map);
            this.mRefreshRatesTreeMap.entrySet().removeIf($$Lambda$DrrCore$MRctntJW66hHY54kK_DN9kVxsw.INSTANCE);
        }
    }

    static /* synthetic */ boolean lambda$updateModeList$0(Map.Entry entry) {
        return entry.getValue() == null;
    }

    public void updateLastTargetFPSTreeMap(Package packageR) {
        if (isDrrAllowed(packageR)) {
            String pkgName = packageR.getPkgName();
            synchronized (this.mLastTargetFPSTreeMap) {
                if (this.mLastTargetFPSTreeMap.containsKey(pkgName)) {
                    this.mLastTargetFPSTreeMap.remove(pkgName);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public TreeMap<String, Integer> getLastTargetFPSTreeMap() {
        return this.mLastTargetFPSTreeMap;
    }

    /* access modifiers changed from: package-private */
    public void setLastTargetFPSTreeMap(TreeMap<String, Integer> treeMap) {
        this.mLastTargetFPSTreeMap.putAll(treeMap);
    }

    /* access modifiers changed from: package-private */
    public void setVrrFeatureInstance(VrrFeature vrrFeature) {
        this.mVrrInstance = vrrFeature;
    }
}
