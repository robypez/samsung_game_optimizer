package com.samsung.android.game.gos.feature.siopmode;

import android.os.Build;
import com.samsung.android.game.ManagerInterface;
import com.samsung.android.game.SemPackageConfiguration;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.feature.StaticInterface;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.selibrary.SeDexManager;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.SiopModeConstant;
import org.json.JSONException;
import org.json.JSONObject;

public class SiopModeFeature implements StaticInterface {
    private static final String LOG_TAG = SiopModeFeature.class.getSimpleName();

    public String getName() {
        return "siop_mode";
    }

    public boolean isAvailableForSystemHelper() {
        return true;
    }

    public static SiopModeFeature getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private SiopModeFeature() {
    }

    public static int getDefaultSiopMode() {
        int optInt;
        String siopModePolicy = DbHelper.getInstance().getGlobalDao().getSiopModePolicy();
        if (siopModePolicy == null || siopModePolicy.equals(BuildConfig.VERSION_NAME)) {
            return 1;
        }
        String str = LOG_TAG;
        GosLog.i(str, "getDefaultSiopMode. siopModePolicyStr : " + siopModePolicy);
        try {
            JSONObject jSONObject = new JSONObject(siopModePolicy);
            if (!jSONObject.has(SiopModeConstant.POLICY_KEY_DEFAULT_MODE) || (optInt = jSONObject.optInt(SiopModeConstant.POLICY_KEY_DEFAULT_MODE, -1000)) == -1000) {
                return 1;
            }
            return optInt;
        } catch (JSONException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public int getTargetCpuLevel(int i, PkgData pkgData, int i2, boolean z) {
        String siopModePolicy;
        GosLog.i(LOG_TAG, "getTargetCpuLevel. original cpuLevel : " + i2);
        if (z || !SeDexManager.getInstance().isDexEnabled()) {
            String str = null;
            String siopModePolicy2 = DbHelper.getInstance().getGlobalDao().getSiopModePolicy();
            if (siopModePolicy2 != null && !siopModePolicy2.equals(BuildConfig.VERSION_NAME)) {
                GosLog.i(LOG_TAG, "getTargetCpuLevel. globalPolicy : " + siopModePolicy2);
                str = siopModePolicy2;
            }
            if (!(pkgData == null || (siopModePolicy = pkgData.getSiopModePolicy()) == null || siopModePolicy.equals(BuildConfig.VERSION_NAME))) {
                GosLog.i(LOG_TAG, "getTargetCpuLevel. pkgPolicy of " + pkgData.getPackageName() + " : " + siopModePolicy);
                str = siopModePolicy;
            }
            if (str != null) {
                try {
                    JSONObject jSONObject = new JSONObject(str);
                    String str2 = SiopModeConstant.PREFIX_CPU_LEVEL_FOR_MODE_ + i;
                    if (jSONObject.has(str2)) {
                        i2 = getTargetCpuLevelByJsonPolicy(i, i2, jSONObject.getString(str2));
                    }
                } catch (JSONException e) {
                    GosLog.w(LOG_TAG, (Throwable) e);
                }
            } else if (pkgData != null && GlobalDbHelper.getInstance().isPositiveFeatureFlag("siop_mode")) {
                boolean isPositiveFeature = pkgData.isPositiveFeature("siop_mode");
                GosLog.i(LOG_TAG, "getTargetCpuLevel. SIOP_MODE is positive. siopMode: " + i);
                i2 = getTargetCpuLevelBySimpleFlag(i, i2, isPositiveFeature);
            }
            GosLog.i(LOG_TAG, "getTargetCpuLevel. new cpuLevel : " + i2);
            return i2;
        }
        GosLog.i(LOG_TAG, "isDexEnabled: true. use default cpuLevel. " + i2);
        return i2;
    }

    public int[] getEachSiopModeCpuLevel(PkgData pkgData) {
        return new int[]{getTargetCpuLevel(1, pkgData, pkgData.getDefaultCpuLevel(), true), getTargetCpuLevel(0, pkgData, pkgData.getDefaultCpuLevel(), true), getTargetCpuLevel(-1, pkgData, pkgData.getDefaultCpuLevel(), true)};
    }

    public static int[] getEachSiopModeGpuLevel(PkgData pkgData) {
        int defaultGpuLevel = pkgData.getDefaultGpuLevel();
        return new int[]{defaultGpuLevel, defaultGpuLevel, defaultGpuLevel};
    }

    static int parseIntegerCalculation(String str, int i) {
        int i2;
        if (str != null && !str.isEmpty()) {
            String replace = str.replace(" ", BuildConfig.VERSION_NAME);
            String str2 = LOG_TAG;
            GosLog.d(str2, "parseIntegerCalculation(), spaceRemovedText : " + replace);
            try {
                if (replace.startsWith("+=")) {
                    i2 = Integer.parseInt(replace.substring(2)) + i;
                } else if (replace.startsWith("-=")) {
                    i2 = i - Integer.parseInt(replace.substring(2));
                } else if (replace.startsWith("*=")) {
                    i2 = Integer.parseInt(replace.substring(2)) * i;
                } else if (replace.startsWith("/=")) {
                    i2 = i / Integer.parseInt(replace.substring(2));
                } else if (replace.startsWith("=")) {
                    i2 = Integer.parseInt(replace.substring(1));
                } else {
                    i2 = Integer.parseInt(replace);
                }
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                String str3 = LOG_TAG;
                GosLog.w(str3, "parseIntegerCalculation(), invalid operationText : " + str + ", exception : " + e.getLocalizedMessage());
            }
            String str4 = LOG_TAG;
            GosLog.i(str4, "parseIntegerCalculation(), input : " + i + ", operationText : " + str + ", result : " + i2);
            return i2;
        }
        i2 = i;
        String str42 = LOG_TAG;
        GosLog.i(str42, "parseIntegerCalculation(), input : " + i + ", operationText : " + str + ", result : " + i2);
        return i2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001a, code lost:
        if (r4 > r3) goto L_0x001c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int getTargetCpuLevelByJsonPolicy(int r2, int r3, java.lang.String r4) {
        /*
            int r4 = parseIntegerCalculation(r4, r3)
            r0 = -1
            if (r2 == r0) goto L_0x001a
            r0 = 1
            if (r2 == r0) goto L_0x000b
            goto L_0x001d
        L_0x000b:
            com.samsung.android.game.gos.selibrary.SeSysProp$CpuGpuLevel r2 = com.samsung.android.game.gos.selibrary.SeSysProp.getCpuGpuLevelInstance()
            int r2 = r2.getMaxCpuLevel()
            if (r3 < r2) goto L_0x0016
            goto L_0x001c
        L_0x0016:
            if (r4 <= r2) goto L_0x001d
            r4 = r2
            goto L_0x001d
        L_0x001a:
            if (r4 <= r3) goto L_0x001d
        L_0x001c:
            r4 = r3
        L_0x001d:
            java.lang.String r2 = LOG_TAG
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "getTargetCpuLevelByJsonPolicy. cpuLevel original : "
            r0.append(r1)
            r0.append(r3)
            java.lang.String r3 = ", new : "
            r0.append(r3)
            r0.append(r4)
            java.lang.String r3 = r0.toString()
            com.samsung.android.game.gos.util.GosLog.i(r2, r3)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.siopmode.SiopModeFeature.getTargetCpuLevelByJsonPolicy(int, int, java.lang.String):int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001d, code lost:
        if (r3 > -2) goto L_0x0021;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int getTargetCpuLevelBySimpleFlag(int r2, int r3, boolean r4) {
        /*
            r0 = -1
            r1 = -2
            if (r2 == r0) goto L_0x001d
            r0 = 1
            if (r2 == r0) goto L_0x0008
            goto L_0x0020
        L_0x0008:
            if (r4 == 0) goto L_0x0020
            com.samsung.android.game.gos.selibrary.SeSysProp$CpuGpuLevel r2 = com.samsung.android.game.gos.selibrary.SeSysProp.getCpuGpuLevelInstance()
            int r2 = r2.getMaxCpuLevel()
            if (r3 < r2) goto L_0x0015
            goto L_0x0020
        L_0x0015:
            int r4 = r3 + 2
            if (r4 <= r2) goto L_0x001b
            r1 = r2
            goto L_0x0021
        L_0x001b:
            r1 = r4
            goto L_0x0021
        L_0x001d:
            if (r3 <= r1) goto L_0x0020
            goto L_0x0021
        L_0x0020:
            r1 = r3
        L_0x0021:
            java.lang.String r2 = LOG_TAG
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r0 = "getTargetCpuLevelBySimpleFlag. cpuLevel original : "
            r4.append(r0)
            r4.append(r3)
            java.lang.String r3 = ", new : "
            r4.append(r3)
            r4.append(r1)
            java.lang.String r3 = r4.toString()
            com.samsung.android.game.gos.util.GosLog.i(r2, r3)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.siopmode.SiopModeFeature.getTargetCpuLevelBySimpleFlag(int, int, boolean):int");
    }

    public SemPackageConfiguration getUpdatedConfig(PkgData pkgData, SemPackageConfiguration semPackageConfiguration, JSONObject jSONObject) {
        if (jSONObject != null) {
            int siopMode = DbHelper.getInstance().getGlobalDao().getSiopMode();
            String str = LOG_TAG;
            GosLog.i(str, "getUpdatedConfig. global siopMode: " + siopMode);
            boolean isUsingCustomValue = GlobalDbHelper.getInstance().isUsingCustomValue("siop_mode");
            if (isUsingCustomValue) {
                siopMode = pkgData.getCustomSiopMode();
                String str2 = LOG_TAG;
                GosLog.i(str2, "getUpdatedConfig. customSiopMode: " + siopMode);
            }
            if (jSONObject.has(ManagerInterface.KeyName.CPU_LEVEL)) {
                int optInt = jSONObject.optInt(ManagerInterface.KeyName.CPU_LEVEL);
                String str3 = LOG_TAG;
                GosLog.i(str3, "getUpdatedConfig. original cpuLevel : " + optInt);
                if (isUsingCustomValue) {
                    optInt = pkgData.getDefaultCpuLevel();
                }
                int targetCpuLevel = getTargetCpuLevel(siopMode, pkgData, optInt, false);
                String str4 = LOG_TAG;
                GosLog.i(str4, "getUpdatedConfig. new cpuLevel : " + targetCpuLevel);
                try {
                    jSONObject.put(ManagerInterface.KeyName.CPU_LEVEL, targetCpuLevel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (Build.VERSION.SDK_INT > 30) {
                try {
                    jSONObject.put("siop_mode", siopMode);
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return semPackageConfiguration;
    }

    public SemPackageConfiguration getDefaultConfig(PkgData pkgData, SemPackageConfiguration semPackageConfiguration, JSONObject jSONObject) {
        if (jSONObject != null) {
            try {
                jSONObject.put(ManagerInterface.KeyName.CPU_LEVEL, 0);
                jSONObject.put(ManagerInterface.KeyName.GPU_LEVEL, 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT > 30) {
                try {
                    jSONObject.put("siop_mode", 1);
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return semPackageConfiguration;
    }

    private static class SingletonHolder {
        public static SiopModeFeature INSTANCE = new SiopModeFeature();

        private SingletonHolder() {
        }
    }
}
