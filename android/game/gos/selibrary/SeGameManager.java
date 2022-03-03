package com.samsung.android.game.gos.selibrary;

import com.samsung.android.game.ManagerInterface;
import com.samsung.android.game.SemGameManager;
import com.samsung.android.game.SemPackageConfiguration;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.SecureFolderUtil;
import com.samsung.android.game.gos.util.TypeConverter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SeGameManager {
    private static final String LOG_TAG = "SeGameManager";
    private SemGameManager manager;
    private Method method_init;
    private Method method_isDynamicSurfaceScalingSupported;
    private Method method_requestWithJson;
    private Method method_setPackageConfigurations;
    private Method method_setPerformanceMode;
    private Method method_setTargetFrameRate;
    private Method method_syncGameList;

    public String getSosPolicyKeysCsv() {
        return "siop,governor_settings,app_start,touch_settings,scheduler_settings,boost_settings";
    }

    private SeGameManager() {
        this.method_init = null;
        this.method_syncGameList = null;
        this.method_requestWithJson = null;
        this.method_setTargetFrameRate = null;
        this.method_setPackageConfigurations = null;
        this.method_setPerformanceMode = null;
        this.method_isDynamicSurfaceScalingSupported = null;
        this.manager = new SemGameManager();
        try {
            if (SecureFolderUtil.isSupportSfGMS()) {
                this.method_syncGameList = this.manager.getClass().getMethod("syncGameList", new Class[]{Map.class});
            } else {
                this.method_init = this.manager.getClass().getMethod("init", new Class[]{Integer.TYPE, Map.class});
            }
            this.method_requestWithJson = this.manager.getClass().getMethod("requestWithJson", new Class[]{String.class, String.class});
            this.method_setTargetFrameRate = this.manager.getClass().getMethod("setTargetFrameRate", new Class[]{Integer.TYPE});
            this.method_setPackageConfigurations = this.manager.getClass().getMethod("setPackageConfigurations", new Class[]{List.class});
            this.method_setPerformanceMode = this.manager.getClass().getMethod("setPerformanceMode", new Class[]{Integer.TYPE, String.class});
            this.method_isDynamicSurfaceScalingSupported = this.manager.getClass().getMethod("isDynamicSurfaceScalingSupported", new Class[0]);
            GosLog.d(LOG_TAG, "ctor. succeeded to get reflected methods");
        } catch (NoSuchMethodException e) {
            GosLog.e(LOG_TAG, "ctor. failed to get reflected methods. " + e.getMessage());
        }
    }

    public static SeGameManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public String[] getSysFsData(String[] strArr) {
        String[] strArr2 = null;
        try {
            JSONObject jSONObject = new JSONObject();
            int i = 0;
            for (String put : strArr) {
                jSONObject.put(BuildConfig.VERSION_NAME + i, put);
                i++;
            }
            String requestWithJson = getInstance().requestWithJson(ManagerInterface.Command.GET_SYSFS_DATA, jSONObject.toString());
            if (requestWithJson == null) {
                return null;
            }
            JSONObject jSONObject2 = new JSONObject(requestWithJson);
            int length = jSONObject2.length();
            if (length > 0) {
                JSONArray names = jSONObject2.names();
                strArr2 = new String[length];
                JSONArray jSONArray = jSONObject2.toJSONArray(names);
                if (names != null) {
                    if (jSONArray != null) {
                        for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                            strArr2[i2] = jSONArray.getString(i2);
                        }
                    }
                }
                return strArr2;
            }
            return strArr2;
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public String readSysFile(String str) {
        String[] strArr = {BuildConfig.VERSION_NAME};
        strArr[0] = str;
        String[] sysFsData = getSysFsData(strArr);
        if (sysFsData == null) {
            return null;
        }
        return sysFsData[0];
    }

    public void boostUp(String str, int i) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(ManagerInterface.KeyName.VALUE_STRING_1, str);
            jSONObject.put(ManagerInterface.KeyName.VALUE_INT_1, i);
            requestWithJson(ManagerInterface.Command.BOOST_ACQUIRE, jSONObject.toString());
            GosLog.i(LOG_TAG, "boostUp(), target: " + str + ", durationSec: " + i);
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void releaseBoost() {
        requestWithJson(ManagerInterface.Command.BOOST_RELEASE, (String) null);
        GosLog.i(LOG_TAG, "releaseBoost()");
    }

    public void boostSetDvfsValue(String str, int i) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(ManagerInterface.KeyName.VALUE_STRING_1, str);
            jSONObject.put(ManagerInterface.KeyName.VALUE_INT_1, i);
            requestWithJson(ManagerInterface.Command.BOOST_SET_DVFS_VALUE, jSONObject.toString());
            GosLog.i(LOG_TAG, "boostSetDvfsValue(), target: " + str + ", freq: " + i);
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public void writeSystemFile(String str, String str2) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(ManagerInterface.KeyName.PATHNAME, str);
            jSONObject.put(ManagerInterface.KeyName.CONTENT, str2);
            getInstance().requestWithJson(ManagerInterface.Command.WRITE_FILE, jSONObject.toString());
            GosLog.i(LOG_TAG, "writeSystemFile(), pathname: " + str + ", content: " + str2);
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    public float getVersion() {
        String str;
        try {
            str = this.manager.getVersion();
        } catch (IllegalStateException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
            str = "failed";
        }
        if (str != null && !str.equalsIgnoreCase("failed")) {
            try {
                return Float.valueOf(str).floatValue();
            } catch (NumberFormatException e2) {
                GosLog.w(LOG_TAG, (Throwable) e2);
            }
        }
        return -1.0f;
    }

    public boolean isForegroundGame() {
        try {
            return this.manager.isForegroundGame();
        } catch (IllegalStateException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
            return false;
        }
    }

    public String getForegroundApp() {
        try {
            return this.manager.getForegroundApp();
        } catch (IllegalStateException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
            return null;
        }
    }

    public boolean init(Map map) {
        Method method;
        SemGameManager semGameManager = this.manager;
        if (semGameManager == null || (method = this.method_init) == null) {
            GosLog.e(LOG_TAG, "init(). " + "manager or method is null");
            return false;
        }
        try {
            return ((Boolean) method.invoke(semGameManager, new Object[]{1, map})).booleanValue();
        } catch (Exception e) {
            GosLog.e(LOG_TAG, "init(). " + "failed to invoke. " + e.getMessage());
            return false;
        }
    }

    public void syncGameList(Map<String, List<Integer>> map) {
        Method method;
        SemGameManager semGameManager = this.manager;
        if (semGameManager == null || (method = this.method_syncGameList) == null) {
            GosLog.e(LOG_TAG, "syncGameList(). " + "manager or method is null");
            return;
        }
        try {
            method.invoke(semGameManager, new Object[]{map});
        } catch (Exception e) {
            GosLog.e(LOG_TAG, "syncGameList(). " + "failed to invoke. " + e.toString());
        }
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final SeGameManager INSTANCE = new SeGameManager();

        private SingletonHolder() {
        }
    }

    public String requestWithJson(String str, String str2) {
        Method method;
        SemGameManager semGameManager = this.manager;
        if (semGameManager == null || (method = this.method_requestWithJson) == null) {
            GosLog.e(LOG_TAG, "requestWithJson(). " + "manager or method is null");
            return null;
        }
        try {
            return (String) method.invoke(semGameManager, new Object[]{str, str2});
        } catch (Exception e) {
            GosLog.e(LOG_TAG, "requestWithJson(). " + "failed to invoke. " + e.getMessage());
            return null;
        }
    }

    public void notifyCategoryToGameManagerNow(String str, String str2) {
        if (str == null || str2 == null) {
            GosLog.e(LOG_TAG, "notifyCategoryToGameManagerNow(), null check failed. pkgName: " + str + ", category: " + str2);
            return;
        }
        SemPackageConfiguration semPackageConfiguration = new SemPackageConfiguration(str);
        semPackageConfiguration.setCategoryByUser(TypeConverter.getCategoryIntValue(str2));
        semPackageConfiguration.setCategory(TypeConverter.getCategoryIntValue(str2));
        setPackageConfigurations(new ArrayList());
    }

    public boolean setTargetFrameRate(int i) {
        Method method;
        SemGameManager semGameManager = this.manager;
        if (semGameManager == null || (method = this.method_setTargetFrameRate) == null) {
            GosLog.e(LOG_TAG, "setTargetFrameRate(). " + "manager or method is null");
            return false;
        }
        try {
            return ((Boolean) method.invoke(semGameManager, new Object[]{Integer.valueOf(i)})).booleanValue();
        } catch (Exception e) {
            GosLog.e(LOG_TAG, "setTargetFrameRate(). " + "failed to invoke. " + e.getMessage());
            return false;
        }
    }

    public int getTargetFrameRate() {
        try {
            return this.manager.getTargetFrameRate();
        } catch (IllegalStateException | SecurityException e) {
            GosLog.w(LOG_TAG, e);
            return -1;
        }
    }

    public boolean setPackageConfigurations(List<SemPackageConfiguration> list) {
        Method method;
        SemGameManager semGameManager = this.manager;
        if (semGameManager == null || (method = this.method_setPackageConfigurations) == null) {
            GosLog.e(LOG_TAG, "setPackageConfigurations(). " + "manager or method is null");
            return false;
        }
        try {
            return ((Boolean) method.invoke(semGameManager, new Object[]{list})).booleanValue();
        } catch (Exception e) {
            GosLog.e(LOG_TAG, "setPackageConfigurations(). " + "failed to invoke. " + e.getMessage());
            return false;
        }
    }

    public boolean setPerformanceMode(int i, String str) {
        Method method;
        SemGameManager semGameManager = this.manager;
        if (semGameManager == null || (method = this.method_setPerformanceMode) == null) {
            GosLog.e(LOG_TAG, "setPerformanceMode(). " + "manager or method is null");
            return false;
        }
        try {
            return ((Boolean) method.invoke(semGameManager, new Object[]{Integer.valueOf(i), str})).booleanValue();
        } catch (Exception e) {
            GosLog.e(LOG_TAG, "setPerformanceMode(). " + "failed to invoke. " + e.getMessage());
            return false;
        }
    }

    public boolean isDynamicSurfaceScalingSupported() {
        Method method;
        SemGameManager semGameManager = this.manager;
        if (semGameManager == null || (method = this.method_isDynamicSurfaceScalingSupported) == null) {
            GosLog.e(LOG_TAG, "setTargetFrameRate(). " + "manager or method is null");
            return false;
        }
        try {
            return ((Boolean) method.invoke(semGameManager, new Object[0])).booleanValue();
        } catch (Exception e) {
            GosLog.e(LOG_TAG, "setTargetFrameRate(). " + "failed to invoke. " + e.getMessage());
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public SemGameManager getManager() {
        return this.manager;
    }
}
