package com.samsung.android.game.gos.feature.vrr;

import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Pair;
import android.view.Display;
import com.samsung.android.feature.SemFloatingFeature;
import com.samsung.android.game.ManagerInterface;
import com.samsung.android.game.SemPackageConfiguration;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.PreferenceHelper;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.FeatureHelper;
import com.samsung.android.game.gos.data.dbhelper.PackageDbHelper;
import com.samsung.android.game.gos.feature.StaticInterface;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.selibrary.SeGameManager;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.PlatformUtil;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.util.ValidationUtil;
import com.samsung.android.game.gos.value.AppVariable;
import com.samsung.android.game.gos.value.BadHardcodedConstant;
import com.samsung.android.game.gos.value.Constants;
import com.samsung.android.game.gos.value.SecureSettingConstants;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VrrFeature implements StaticInterface {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String LOG_TAG = "VrrFeature";
    private static final int MSG_VRR_SETTING_CHANGED = 1;
    private Map<Integer, String> availableModeIdMap;
    private SeGameManager gameManager;
    private Handler mHandler;
    private Integer mHfrMode;
    private boolean mIsHubble;
    private DisplayManager.DisplayListener mListener;
    private Boolean mRrModeIsHigh;
    private Map<Integer, String> systemModeIdList;

    public SemPackageConfiguration getDefaultConfig(PkgData pkgData, SemPackageConfiguration semPackageConfiguration, JSONObject jSONObject) {
        return semPackageConfiguration;
    }

    public String getName() {
        return Constants.V4FeatureFlag.VRR;
    }

    public SemPackageConfiguration getUpdatedConfig(PkgData pkgData, SemPackageConfiguration semPackageConfiguration, JSONObject jSONObject) {
        return semPackageConfiguration;
    }

    private VrrFeature() {
        this.systemModeIdList = new HashMap();
        this.availableModeIdMap = new HashMap();
        this.mHfrMode = null;
        this.mRrModeIsHigh = null;
        this.mListener = new DisplayManager.DisplayListener() {
            public void onDisplayAdded(int i) {
            }

            public void onDisplayRemoved(int i) {
            }

            public void onDisplayChanged(int i) {
                GosLog.d(VrrFeature.LOG_TAG, "onDisplayChanged()");
                VrrFeature.this.sendVrrSettingChangedMessage();
            }
        };
        this.gameManager = SeGameManager.getInstance();
        this.mIsHubble = isHubble();
        this.mHandler = new InnerClassHandler(this);
        DisplayManager displayManager = (DisplayManager) AppContext.get().getSystemService("display");
        if (displayManager != null) {
            updateDisplayModes(getDisplayModeList(displayManager.getDisplay(0)));
            if (!AppVariable.isUnitTest() && PlatformUtil.isFoldableDevice()) {
                displayManager.registerDisplayListener(this.mListener, new Handler(Looper.getMainLooper()));
            }
        }
        if (isAvailableForSystemHelper()) {
            int hfrMode = getHfrMode();
            checkVrrModeIsHigh(this.mHfrMode.intValue());
            if (hfrMode == 1) {
                PreferenceHelper preferenceHelper = new PreferenceHelper(AppContext.get().createDeviceProtectedStorageContext());
                String value = preferenceHelper.getValue(PreferenceHelper.PREF_AVAILABLE_REFRESH_RATE, (String) null);
                this.availableModeIdMap = getAvailableRefreshRate(value == null ? SeGameManager.getInstance().requestWithJson(ManagerInterface.Command.GET_AVAILABLE_REFRESH_RATE_LIST, (String) null) : value, preferenceHelper);
                GosLog.d(LOG_TAG, "VrrFeature()-rate map: " + this.availableModeIdMap.toString());
            }
        }
        updateDrrModeList();
    }

    private Map<Integer, String> getAvailableRefreshRate(String str, PreferenceHelper preferenceHelper) {
        HashMap hashMap = new HashMap();
        if (str == null) {
            GosLog.e(LOG_TAG, "getAvailableRefreshRate()-json is null!!!");
            return hashMap;
        }
        try {
            JSONArray jSONArray = new JSONObject(str).getJSONArray(ManagerInterface.KeyName.VALUE_ARRAY_1);
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                double d = jSONObject.getDouble(ManagerInterface.KeyName.VALUE_FLOAT_1);
                int i2 = jSONObject.getInt(ManagerInterface.KeyName.VALUE_INT_1);
                Integer valueOf = Integer.valueOf((int) d);
                hashMap.put(valueOf, BuildConfig.VERSION_NAME + i2);
            }
            if (preferenceHelper != null) {
                preferenceHelper.put(PreferenceHelper.PREF_AVAILABLE_REFRESH_RATE, str);
            }
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
        return hashMap;
    }

    public static VrrFeature getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* access modifiers changed from: package-private */
    public void updateDisplayModes(List<Display.Mode> list) {
        GosLog.i(LOG_TAG, "updateDisplayModes(): displayModes: " + list);
        clearDisplayModeIdsForEachHz();
        Iterator<Display.Mode> it = list.iterator();
        while (true) {
            if (it.hasNext()) {
                Display.Mode next = it.next();
                for (int i : VrrConstant.CANDIDATE_VRR_LIST) {
                    if (this.systemModeIdList.get(Integer.valueOf(i)) == null && ValidationUtil.floatEqual(next.getRefreshRate(), (float) i)) {
                        this.systemModeIdList.put(Integer.valueOf(i), String.valueOf(next.getModeId()));
                    }
                }
            } else {
                GosLog.i(LOG_TAG, String.format(Locale.US, "updateDisplayModes()-%s", new Object[]{this.systemModeIdList.toString()}));
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void updateDrrModeList() {
        Map<Integer, String> map;
        if (getHfrMode() == 1) {
            map = this.availableModeIdMap;
        } else {
            map = this.systemModeIdList;
        }
        DrrCore.getInstance().updateModeList(map);
    }

    /* access modifiers changed from: package-private */
    public void clearDisplayModeIdsForEachHz() {
        for (int valueOf : VrrConstant.CANDIDATE_VRR_LIST) {
            this.systemModeIdList.put(Integer.valueOf(valueOf), (Object) null);
        }
    }

    public void sendVrrSettingChangedMessage() {
        this.mHandler.removeMessages(1);
        Message obtainMessage = this.mHandler.obtainMessage(1);
        if (!AppVariable.isUnitTest()) {
            this.mHandler.sendMessageDelayed(obtainMessage, 500);
        } else {
            this.mHandler.sendMessage(obtainMessage);
        }
    }

    private static class InnerClassHandler extends Handler {
        private final WeakReference<VrrFeature> mVrrFeature;

        InnerClassHandler(VrrFeature vrrFeature) {
            super(Looper.getMainLooper());
            this.mVrrFeature = new WeakReference<>(vrrFeature);
        }

        public void handleMessage(Message message) {
            VrrFeature vrrFeature = (VrrFeature) this.mVrrFeature.get();
            if (message.what == 1) {
                vrrFeature.onVrrSettingChanged();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onVrrSettingChanged() {
        DisplayManager displayManager = (DisplayManager) AppContext.get().getSystemService("display");
        if (displayManager != null) {
            updateDisplayModes(getDisplayModeList(displayManager.getDisplay(0)));
            updateDrrModeList();
            getInstance().setVrr((String) null);
        }
    }

    private static class SingletonHolder {
        public static VrrFeature INSTANCE = new VrrFeature();

        private SingletonHolder() {
        }
    }

    public void setVrr(String str) {
        if (!FeatureHelper.isAvailable(Constants.V4FeatureFlag.VRR)) {
            GosLog.i(LOG_TAG, "setVrr(): !isAvailable() return!");
            return;
        }
        int hfrMode = getHfrMode();
        if (AppVariable.isUnitTest() || hfrMode != 0) {
            int checkVrrModeIsHigh = checkVrrModeIsHigh(hfrMode);
            if (checkVrrModeIsHigh >= 0) {
                boolean z = false;
                boolean z2 = checkVrrModeIsHigh > 0;
                if (new PreferenceHelper().getValue(SecureSettingConstants.KEY_VRR_48HZ_ALLOWED, 0) != 0) {
                    z = true;
                }
                if (hfrMode == 1) {
                    GosLog.d(LOG_TAG, "refreshIds() - isVrrHsModeEnabled: " + z2 + ", isAllowed48HzByUser: " + z + ", " + this.availableModeIdMap.toString());
                } else {
                    refreshIds(z2, z);
                }
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                if (str == null) {
                    Map<String, PkgData> pkgDataMap = PackageDbHelper.getInstance().getPkgDataMap(DbHelper.getInstance().getPackageDao().getPkgNameListByCategory(Constants.CategoryCode.GAME));
                    ArrayList arrayList3 = new ArrayList();
                    for (Map.Entry next : pkgDataMap.entrySet()) {
                        PkgData pkgData = (PkgData) next.getValue();
                        if (pkgData == null) {
                            arrayList3.add(next.getKey());
                        } else {
                            Boolean bool = pkgData.getActualFeatureFlagMap().get(Constants.V4FeatureFlag.VRR);
                            if (bool != null && !bool.booleanValue()) {
                                arrayList.add(pkgData.getPackageName());
                            } else if (bool != null) {
                                arrayList2.add(pkgData.getPkg());
                            }
                        }
                    }
                    if (arrayList3.size() > 0) {
                        GosLog.e(LOG_TAG, "setVrr(): pkgData is null: " + arrayList3.toString());
                    }
                    if (arrayList.size() > 0) {
                        requestRemoveVrr(z2, arrayList);
                    }
                    if (arrayList2.size() > 0) {
                        requestVrr(z2, z, arrayList2, hfrMode);
                    }
                    GosLog.d(LOG_TAG, "setVrr(): setPackageList : " + arrayList2 + ", remove list : " + arrayList);
                    return;
                }
                PkgData pkgData2 = PackageDbHelper.getInstance().getPkgData(str);
                if (pkgData2 == null) {
                    GosLog.e(LOG_TAG, "setVrr(): pkgData is null vrr set failed.");
                    return;
                }
                Boolean bool2 = pkgData2.getActualFeatureFlagMap().get(Constants.V4FeatureFlag.VRR);
                if (bool2 != null && !bool2.booleanValue()) {
                    arrayList.add(str);
                    requestRemoveVrr(z2, arrayList);
                } else if (bool2 != null) {
                    arrayList2.add(DbHelper.getInstance().getPackageDao().getPackage(str));
                    requestVrr(z2, z, arrayList2, hfrMode);
                }
                GosLog.d(LOG_TAG, "setVrr(): packageName is " + str + ", setPackageList : " + arrayList2 + ", removePackageList : " + arrayList);
                return;
            }
            GosLog.w(LOG_TAG, "setVrr(): vrrMode is Dynamic, return!");
            return;
        }
        GosLog.i(LOG_TAG, "setVrr(): HFR mode is 0. return!");
    }

    /* access modifiers changed from: package-private */
    public int getHfrMode() {
        Integer num = this.mHfrMode;
        if (num != null) {
            return num.intValue();
        }
        int i = 0;
        if (!AppVariable.isUnitTest()) {
            i = SemFloatingFeature.getInstance().getInt("SEC_FLOATING_FEATURE_LCD_CONFIG_HFR_MODE");
            GosLog.v(LOG_TAG, "getHfrMode()-got hfrMode: " + i);
            if (i == -1) {
                i = 1;
            }
        }
        this.mHfrMode = Integer.valueOf(i);
        GosLog.v(LOG_TAG, "getHfrMode()-result hfrMode: " + i);
        return i;
    }

    public boolean isHighRefreshRateMode() {
        Boolean bool = this.mRrModeIsHigh;
        if (bool != null) {
            return bool.booleanValue();
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x008e  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0092  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int checkVrrModeIsHigh(int r9) {
        /*
            r8 = this;
            com.samsung.android.game.gos.data.SecureSettingHelper r0 = com.samsung.android.game.gos.data.SecureSettingHelper.getInstance()
            java.lang.String r1 = "refresh_rate_mode"
            r0.updateValue(r1)
            com.samsung.android.game.gos.data.PreferenceHelper r0 = new com.samsung.android.game.gos.data.PreferenceHelper
            r0.<init>()
            r2 = -1
            int r0 = r0.getValue((java.lang.String) r1, (int) r2)
            java.lang.String r1 = "VrrFeature"
            r3 = 0
            r4 = 1
            r5 = 3
            if (r9 == r5) goto L_0x0058
            r5 = 2
            if (r9 != r5) goto L_0x001e
            goto L_0x0058
        L_0x001e:
            if (r9 != r4) goto L_0x0039
            java.lang.String r6 = com.samsung.android.game.gos.value.AppVariable.getOriginalDeviceName()
            java.lang.String r7 = "gts7xl"
            boolean r6 = r6.startsWith(r7)
            if (r6 == 0) goto L_0x0039
            java.lang.String r6 = com.samsung.android.game.gos.value.AppVariable.getOriginalModelName()
            java.lang.String r7 = "SM-T97"
            boolean r6 = r6.startsWith(r7)
            if (r6 == 0) goto L_0x0039
            goto L_0x003a
        L_0x0039:
            r5 = r3
        L_0x003a:
            if (r0 == r2) goto L_0x003d
            r5 = r0
        L_0x003d:
            if (r5 == 0) goto L_0x0040
            r3 = r4
        L_0x0040:
            if (r5 != r4) goto L_0x0060
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r6 = "checkVrrModeIsHigh(), abnormal case vrrMode: "
            r4.append(r6)
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r1, (java.lang.String) r4)
            r5 = r2
            goto L_0x0060
        L_0x0058:
            if (r0 == r2) goto L_0x005c
            r5 = r0
            goto L_0x005d
        L_0x005c:
            r5 = r4
        L_0x005d:
            if (r5 == 0) goto L_0x0060
            r3 = r4
        L_0x0060:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r6 = "checkVrrModeIsHigh(), hfrMode: "
            r4.append(r6)
            r4.append(r9)
            java.lang.String r6 = ", vrrModeFromSystem: "
            r4.append(r6)
            r4.append(r0)
            java.lang.String r0 = ", vrrMode: "
            r4.append(r0)
            r4.append(r5)
            java.lang.String r0 = ", isVrrHsModeEnabled: "
            r4.append(r0)
            r4.append(r3)
            java.lang.String r0 = r4.toString()
            com.samsung.android.game.gos.util.GosLog.d(r1, r0)
            if (r5 >= 0) goto L_0x0092
            r9 = 0
            r8.mRrModeIsHigh = r9
            return r2
        L_0x0092:
            java.lang.Integer r0 = r8.mHfrMode
            if (r0 == 0) goto L_0x00a2
            int r0 = r0.intValue()
            if (r9 != r0) goto L_0x00a2
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r3)
            r8.mRrModeIsHigh = r9
        L_0x00a2:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.vrr.VrrFeature.checkVrrModeIsHigh(int):int");
    }

    /* access modifiers changed from: package-private */
    public void refreshIds(boolean z, boolean z2) {
        boolean z3;
        int[] iArr = VrrConstant.CANDIDATE_VRR_LIST;
        int length = iArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                z3 = false;
                break;
            }
            if (this.systemModeIdList.get(Integer.valueOf(iArr[i])) == null) {
                z3 = true;
                break;
            }
            i++;
        }
        if (z3) {
            GosLog.w(LOG_TAG, String.format(Locale.US, "refreshIds()- Some DisplayMode not found!: %s", new Object[]{this.systemModeIdList.toString()}));
            DisplayManager displayManager = (DisplayManager) AppContext.get().getSystemService("display");
            if (displayManager != null) {
                updateDisplayModes(getDisplayModeList(displayManager.getDisplay(0)));
                updateDrrModeList();
            }
        }
        GosLog.i(LOG_TAG, String.format(Locale.US, "refreshIds() - isVrrHsModeEnabled: " + z + ", isAllowed48HzByUser: " + z2 + ", %s", new Object[]{this.systemModeIdList.toString()}));
    }

    /* access modifiers changed from: package-private */
    public void requestRemoveVrr(boolean z, List<String> list) {
        GosLog.i(LOG_TAG, "requestRemoveVrr() - " + list.toString());
        if (isRemovingRrAvailable()) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put(ManagerInterface.KeyName.PACKAGE_NAME_LIST, TypeConverter.stringsToCsv((Iterable<String>) list));
                SeGameManager.getInstance().requestWithJson(ManagerInterface.Command.REQUEST_TO_REMOVE_REFRESH_RATE, jSONObject.toString());
            } catch (JSONException e) {
                GosLog.w(LOG_TAG, (Throwable) e);
            }
        } else {
            ArrayList arrayList = new ArrayList();
            int i = 60;
            if (z) {
                i = 120;
            }
            for (int i2 = 0; i2 < list.size(); i2++) {
                arrayList.add(this.systemModeIdList.get(Integer.valueOf(i)));
            }
            try {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put(ManagerInterface.KeyName.PACKAGE_NAME_LIST, TypeConverter.stringsToCsv((Iterable<String>) list));
                jSONObject2.put(ManagerInterface.KeyName.DISPLAY_MODE_ID_LIST, TypeConverter.stringsToCsv((Iterable<String>) arrayList));
                SeGameManager.getInstance().requestWithJson(ManagerInterface.Command.REQUEST_TO_SET_REFRESH_RATE, jSONObject2.toString());
            } catch (JSONException e2) {
                GosLog.w(LOG_TAG, (Throwable) e2);
            }
        }
    }

    public int publicGetSystemVrrMax() {
        Pair<Integer, Integer> pair;
        if (!isAvailableForSystemHelper()) {
            GosLog.w(LOG_TAG, "publicGetSystemVrrMax() called when feature is not available; returning default 60");
            return 60;
        }
        if (getHfrMode() != 1) {
            pair = getSystemVrrMinMax(VrrConstant.CANDIDATE_VRR_LIST, this.systemModeIdList);
        } else if (isHighRefreshRateMode()) {
            pair = getSystemVrrMinMax(VrrConstant.CANDIDATE_VRR_LIST_SWITCHABLE_HIGH, this.availableModeIdMap);
        } else {
            pair = getSystemVrrMinMax(VrrConstant.CANDIDATE_VRR_LIST_SWITCHABLE_NORMAL, this.availableModeIdMap);
        }
        if (pair.first != null) {
            return ((Integer) pair.first).intValue();
        }
        GosLog.w(LOG_TAG, "publicGetSystemVrrMax() found null; returning default 60");
        return 60;
    }

    private Pair<Integer, Integer> getSystemVrrMinMax(int[] iArr, Map<Integer, String> map) {
        int i = 0;
        int i2 = iArr[0];
        int i3 = iArr[iArr.length - 1];
        if (AppVariable.isUnitTest()) {
            int length = iArr.length;
            int i4 = 0;
            int i5 = 1;
            while (i4 < length) {
                map.put(Integer.valueOf(iArr[i4]), String.valueOf(i5));
                i4++;
                i5++;
            }
        }
        int length2 = iArr.length;
        while (true) {
            if (i >= length2) {
                break;
            }
            int i6 = iArr[i];
            if (map.get(Integer.valueOf(i6)) != null) {
                i2 = i6;
                break;
            }
            i++;
        }
        int length3 = iArr.length - 1;
        while (true) {
            if (length3 < 0) {
                break;
            }
            int i7 = iArr[length3];
            if (map.get(Integer.valueOf(i7)) != null) {
                i3 = i7;
                break;
            }
            length3--;
        }
        return new Pair<>(Integer.valueOf(i2), Integer.valueOf(i3));
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0126, code lost:
        if (r0 != false) goto L_0x012d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x012b, code lost:
        if (r0 != false) goto L_0x012d;
     */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0169  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0182  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0189  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x01b0  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void requestVrr(boolean r21, boolean r22, java.util.List<com.samsung.android.game.gos.data.model.Package> r23, int r24) {
        /*
            r20 = this;
            r1 = r20
            r2 = r21
            r0 = r22
            r3 = r24
            java.util.ArrayList r4 = new java.util.ArrayList
            r4.<init>()
            r5 = 1
            if (r3 != r5) goto L_0x0024
            if (r2 == 0) goto L_0x001b
            int[] r6 = com.samsung.android.game.gos.feature.vrr.VrrConstant.CANDIDATE_VRR_LIST_SWITCHABLE_HIGH
            java.util.Map<java.lang.Integer, java.lang.String> r7 = r1.availableModeIdMap
            android.util.Pair r6 = r1.getSystemVrrMinMax(r6, r7)
            goto L_0x002c
        L_0x001b:
            int[] r6 = com.samsung.android.game.gos.feature.vrr.VrrConstant.CANDIDATE_VRR_LIST_SWITCHABLE_NORMAL
            java.util.Map<java.lang.Integer, java.lang.String> r7 = r1.availableModeIdMap
            android.util.Pair r6 = r1.getSystemVrrMinMax(r6, r7)
            goto L_0x002c
        L_0x0024:
            int[] r6 = com.samsung.android.game.gos.feature.vrr.VrrConstant.CANDIDATE_VRR_LIST
            java.util.Map<java.lang.Integer, java.lang.String> r7 = r1.systemModeIdList
            android.util.Pair r6 = r1.getSystemVrrMinMax(r6, r7)
        L_0x002c:
            java.lang.Object r7 = r6.first
            java.lang.Integer r7 = (java.lang.Integer) r7
            int r7 = r7.intValue()
            java.lang.Object r6 = r6.second
            java.lang.Integer r6 = (java.lang.Integer) r6
            int r6 = r6.intValue()
            java.lang.String r8 = "VrrFeature"
            if (r7 > r6) goto L_0x0080
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r3 = "No options for VRR, systemVrrMax="
            r0.append(r3)
            r0.append(r7)
            java.lang.String r3 = ", systemVrrMin="
            r0.append(r3)
            r0.append(r6)
            java.lang.String r0 = r0.toString()
            com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r8, (java.lang.String) r0)
            java.util.Iterator r0 = r23.iterator()
        L_0x0060:
            boolean r3 = r0.hasNext()
            if (r3 == 0) goto L_0x0076
            java.lang.Object r3 = r0.next()
            com.samsung.android.game.gos.data.model.Package r3 = (com.samsung.android.game.gos.data.model.Package) r3
            if (r3 == 0) goto L_0x0060
            java.lang.String r3 = r3.getPkgName()
            r4.add(r3)
            goto L_0x0060
        L_0x0076:
            int r0 = r4.size()
            if (r0 <= 0) goto L_0x007f
            r1.requestRemoveVrr(r2, r4)
        L_0x007f:
            return
        L_0x0080:
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            java.util.ArrayList r10 = new java.util.ArrayList
            r10.<init>()
            java.util.ArrayList r11 = new java.util.ArrayList
            r11.<init>()
            com.samsung.android.game.gos.data.dbhelper.DbHelper r12 = com.samsung.android.game.gos.data.dbhelper.DbHelper.getInstance()
            com.samsung.android.game.gos.data.dao.GlobalDao r12 = r12.getGlobalDao()
            int r12 = r12.getVrrMaxValue()
            com.samsung.android.game.gos.data.PreferenceHelper r13 = new com.samsung.android.game.gos.data.PreferenceHelper
            r13.<init>()
            java.lang.String r14 = "game_refresh_rate_max_hz"
            r15 = 0
            int r13 = r13.getValue((java.lang.String) r14, (int) r15)
            r14 = 120(0x78, float:1.68E-43)
            if (r13 < r14) goto L_0x00ad
            r12 = r15
            goto L_0x00b0
        L_0x00ad:
            if (r13 == 0) goto L_0x00b0
            r12 = r13
        L_0x00b0:
            com.samsung.android.game.gos.data.dbhelper.DbHelper r14 = com.samsung.android.game.gos.data.dbhelper.DbHelper.getInstance()
            com.samsung.android.game.gos.data.dao.GlobalDao r14 = r14.getGlobalDao()
            int r14 = r14.getVrrMinValue()
            java.util.Map<java.lang.Integer, java.lang.String> r15 = r1.systemModeIdList
            if (r3 != r5) goto L_0x00c2
            java.util.Map<java.lang.Integer, java.lang.String> r15 = r1.availableModeIdMap
        L_0x00c2:
            java.util.Iterator r3 = r23.iterator()
        L_0x00c6:
            boolean r16 = r3.hasNext()
            if (r16 == 0) goto L_0x01c1
            java.lang.Object r16 = r3.next()
            r5 = r16
            com.samsung.android.game.gos.data.model.Package r5 = (com.samsung.android.game.gos.data.model.Package) r5
            if (r5 != 0) goto L_0x00d8
        L_0x00d6:
            r5 = 1
            goto L_0x00c6
        L_0x00d8:
            int r16 = r5.getVrrMaxValue()
            if (r16 <= 0) goto L_0x00f2
            if (r13 == 0) goto L_0x00eb
            r23 = r3
            int r3 = r5.getVrrMaxValue()
            int r3 = java.lang.Math.min(r13, r3)
            goto L_0x00f5
        L_0x00eb:
            r23 = r3
            int r3 = r5.getVrrMaxValue()
            goto L_0x00f5
        L_0x00f2:
            r23 = r3
            r3 = r12
        L_0x00f5:
            if (r3 <= r7) goto L_0x00f9
            r3 = r7
            goto L_0x00fe
        L_0x00f9:
            if (r3 <= 0) goto L_0x00fe
            if (r3 >= r6) goto L_0x00fe
            r3 = r6
        L_0x00fe:
            int r16 = r5.getVrrMinValue()
            if (r16 <= 0) goto L_0x010f
            int r16 = r5.getVrrMinValue()
            r19 = r16
            r16 = r12
            r12 = r19
            goto L_0x0112
        L_0x010f:
            r16 = r12
            r12 = r14
        L_0x0112:
            if (r12 >= r6) goto L_0x0116
            r12 = r6
            goto L_0x0119
        L_0x0116:
            if (r12 <= r7) goto L_0x0119
            r12 = r7
        L_0x0119:
            if (r3 <= 0) goto L_0x011e
            if (r12 <= r3) goto L_0x011e
            r12 = r3
        L_0x011e:
            if (r2 == 0) goto L_0x0129
            r24 = r3
            boolean r3 = r1.mIsHubble
            if (r3 != 0) goto L_0x0131
            if (r0 == 0) goto L_0x0131
            goto L_0x012d
        L_0x0129:
            r24 = r3
            if (r0 == 0) goto L_0x0131
        L_0x012d:
            r17 = r6
            r3 = r12
            goto L_0x0135
        L_0x0131:
            r3 = r24
            r17 = r6
        L_0x0135:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            r18 = r7
            java.lang.String r7 = "requestVrr(), max(result for static)="
            r6.append(r7)
            r6.append(r3)
            java.lang.String r7 = ", min="
            r6.append(r7)
            r6.append(r12)
            java.lang.String r7 = ", pkgName="
            r6.append(r7)
            java.lang.String r7 = r5.getPkgName()
            r6.append(r7)
            java.lang.String r6 = r6.toString()
            com.samsung.android.game.gos.util.GosLog.i(r8, r6)
            com.samsung.android.game.gos.feature.vrr.DrrCore r6 = com.samsung.android.game.gos.feature.vrr.DrrCore.getInstance()
            boolean r6 = r6.isDrrAllowed(r5)
            if (r6 == 0) goto L_0x0182
            if (r3 != 0) goto L_0x016e
            r6 = r18
            goto L_0x016f
        L_0x016e:
            r6 = r3
        L_0x016f:
            com.samsung.android.game.gos.feature.vrr.DrrCore r7 = com.samsung.android.game.gos.feature.vrr.DrrCore.getInstance()
            r24 = r3
            java.lang.String r3 = r5.getPkgName()
            int r3 = r7.getDrrValue(r3, r12, r6, r0)
            r6 = -1
            if (r3 == r6) goto L_0x0184
            r6 = 1
            goto L_0x0187
        L_0x0182:
            r24 = r3
        L_0x0184:
            r3 = r24
            r6 = 0
        L_0x0187:
            if (r3 <= 0) goto L_0x01b0
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            java.lang.Object r3 = r15.get(r3)
            java.lang.String r3 = (java.lang.String) r3
            if (r3 == 0) goto L_0x01aa
            java.lang.String r5 = r5.getPkgName()
            r10.add(r5)
            r9.add(r3)
            if (r6 == 0) goto L_0x01a4
            java.lang.String r3 = "T"
            goto L_0x01a6
        L_0x01a4:
            java.lang.String r3 = "F"
        L_0x01a6:
            r11.add(r3)
            goto L_0x01b7
        L_0x01aa:
            java.lang.String r3 = "requestVrr(), modeId is null exclude set list"
            com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r8, (java.lang.String) r3)
            goto L_0x01b7
        L_0x01b0:
            java.lang.String r3 = r5.getPkgName()
            r4.add(r3)
        L_0x01b7:
            r3 = r23
            r12 = r16
            r6 = r17
            r7 = r18
            goto L_0x00d6
        L_0x01c1:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r3 = "requestVrr(), gamePackageNameListToBeSet="
            r0.append(r3)
            java.lang.String r3 = r10.toString()
            r0.append(r3)
            java.lang.String r0 = r0.toString()
            com.samsung.android.game.gos.util.GosLog.i(r8, r0)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r3 = "requestVrr(), modeIdList="
            r0.append(r3)
            java.lang.String r3 = r9.toString()
            r0.append(r3)
            java.lang.String r0 = r0.toString()
            com.samsung.android.game.gos.util.GosLog.i(r8, r0)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r3 = "requestVrr(), setByDrrStateList="
            r0.append(r3)
            java.lang.String r3 = r11.toString()
            r0.append(r3)
            java.lang.String r0 = r0.toString()
            com.samsung.android.game.gos.util.GosLog.i(r8, r0)
            int r0 = r10.size()
            if (r0 <= 0) goto L_0x0241
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ JSONException -> 0x023d }
            r0.<init>()     // Catch:{ JSONException -> 0x023d }
            java.lang.String r3 = "package_name_list"
            java.lang.String r5 = com.samsung.android.game.gos.util.TypeConverter.stringsToCsv((java.lang.Iterable<java.lang.String>) r10)     // Catch:{ JSONException -> 0x023d }
            r0.put(r3, r5)     // Catch:{ JSONException -> 0x023d }
            java.lang.String r3 = "display_mode_id_list"
            java.lang.String r5 = com.samsung.android.game.gos.util.TypeConverter.stringsToCsv((java.lang.Iterable<java.lang.String>) r9)     // Catch:{ JSONException -> 0x023d }
            r0.put(r3, r5)     // Catch:{ JSONException -> 0x023d }
            java.lang.String r3 = "drr_enabled_state_list"
            java.lang.String r5 = com.samsung.android.game.gos.util.TypeConverter.stringsToCsv((java.lang.Iterable<java.lang.String>) r11)     // Catch:{ JSONException -> 0x023d }
            r0.put(r3, r5)     // Catch:{ JSONException -> 0x023d }
            com.samsung.android.game.gos.selibrary.SeGameManager r3 = com.samsung.android.game.gos.selibrary.SeGameManager.getInstance()     // Catch:{ JSONException -> 0x023d }
            java.lang.String r5 = "request_to_set_refresh_rate"
            java.lang.String r0 = r0.toString()     // Catch:{ JSONException -> 0x023d }
            r3.requestWithJson(r5, r0)     // Catch:{ JSONException -> 0x023d }
            goto L_0x0241
        L_0x023d:
            r0 = move-exception
            com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r8, (java.lang.Throwable) r0)
        L_0x0241:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r3 = "requestVrr(), gamePackageNameListToBeRemoved="
            r0.append(r3)
            java.lang.String r3 = r4.toString()
            r0.append(r3)
            java.lang.String r0 = r0.toString()
            com.samsung.android.game.gos.util.GosLog.i(r8, r0)
            int r0 = r4.size()
            if (r0 <= 0) goto L_0x0262
            r1.requestRemoveVrr(r2, r4)
        L_0x0262:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.vrr.VrrFeature.requestVrr(boolean, boolean, java.util.List, int):void");
    }

    /* access modifiers changed from: package-private */
    public boolean isHubble() {
        String originalDeviceName = AppVariable.getOriginalDeviceName();
        boolean contains = Arrays.asList(BadHardcodedConstant.HUBBLE_DEVICE_LIST.split(",")).contains(originalDeviceName);
        GosLog.v(LOG_TAG, "isHubble()-deviceName: " + originalDeviceName + ", result: " + contains);
        return contains;
    }

    private static boolean isRemovingRrAvailable() {
        return DbHelper.getInstance().getGlobalDao().getGmsVersion() > 110.012f;
    }

    /* access modifiers changed from: package-private */
    public List<Display.Mode> getDisplayModeList(Display display) {
        ArrayList arrayList = new ArrayList();
        if (display != null) {
            arrayList.addAll(Arrays.asList(display.getSupportedModes()));
        } else {
            GosLog.e(LOG_TAG, "getDisplayModeList()-DefaultDisplay null!");
        }
        return arrayList;
    }

    public boolean isAvailableForSystemHelper() {
        boolean z;
        String requestWithJson = this.gameManager.requestWithJson(ManagerInterface.Command.IS_VARIABLE_REFRESH_RATE_SUPPORTED, (String) null);
        if (requestWithJson != null) {
            try {
                z = new JSONObject(requestWithJson).getBoolean(ManagerInterface.KeyName.VALUE_BOOL_1);
            } catch (JSONException e) {
                GosLog.w(LOG_TAG, (Throwable) e);
            }
            GosLog.d(LOG_TAG, "isAvailableForSystemHelper: " + z);
            return z;
        }
        z = false;
        GosLog.d(LOG_TAG, "isAvailableForSystemHelper: " + z);
        return z;
    }

    public void setMockedGameManager(SeGameManager seGameManager) {
        if (seGameManager != null) {
            this.gameManager = seGameManager;
        }
    }

    /* access modifiers changed from: package-private */
    public List<String> getDisplayModeIdsForEachHz() {
        ArrayList arrayList = new ArrayList();
        for (int valueOf : VrrConstant.CANDIDATE_VRR_LIST) {
            arrayList.add(this.systemModeIdList.get(Integer.valueOf(valueOf)));
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public void setIsHubble(boolean z) {
        this.mIsHubble = z;
    }
}
