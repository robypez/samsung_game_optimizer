package com.samsung.android.game.neteasesdk;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.MemoryFile;
import android.os.RemoteException;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.feature.externalsdk.ExternalSdkCore;
import com.samsung.android.game.gos.feature.externalsdk.IExternalSdkListener;
import com.samsung.android.game.gos.feature.externalsdk.value.Const;
import com.samsung.android.game.gos.feature.gfi.GfiFeature;
import com.samsung.android.game.gos.feature.ipm.IpmCore;
import com.samsung.android.game.gos.selibrary.SeActivityManager;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.neteasesdk.INeteaseSceneSdkService;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class SceneSdkService extends Service {
    private static final int CREATE_FILE_FAIL = 2;
    private static final int CREATE_FILE_SUCCESS = 1;
    private static boolean DEBUG_INFO = false;
    private static final int DEFAULT = 0;
    private static final int DRIVING = 103;
    private static final int FALL_BACK_ID = -1;
    private static final String FPS = "3";
    private static final int GAMEHOME = 4;
    private static final int GAMESCENE_LOADING = 5;
    private static final int GAME_LOADING = 3;
    private static final int GAME_PAUSE_RESULT = -1001;
    private static final int GAME_RESUME_RESULT = -1002;
    private static final int GAME_STARTING = 1;
    private static final int GAME_UPDATING = 2;
    private static final int GAMING = 7;
    private static final int HIGH_LOAD_BOOST = 10;
    private static final String LOG_TAG = "SceneSdkService";
    private static final int NETEASE_GAME_BOOST_TIMEOUT = 5;
    private static final int ONPLANE = 14;
    private static final int PARACHUTE = 12;
    /* access modifiers changed from: private */
    public static boolean SCENESDK_SWITCH = true;
    private static final String SCENE_ID = "2";
    private static final String SCENE_IMPORTANCE = "83";
    private static final float SCENE_SDK_VERSION = 0.6f;
    private static final int SIOP_LEVEL_BOUNDARY_FOR_HIGH = 5;
    private static final int SIOP_LEVEL_BOUNDARY_FOR_MID = 3;
    private static final String TARGET_FPS = "4";
    static final int TEMP_LEVEL_HIGH = 2;
    static final int TEMP_LEVEL_LOW = 0;
    static final int TEMP_LEVEL_MID = 1;
    static final int TEMP_LEVEL_UNKNOWN = -1000;
    private static final int WRITE_VALUE_FAIL = 4;
    private static final int WRITE_VALUE_SUCCESS = 3;
    private static final int mImportanceOffSet = 168;
    private static final int mSharedMemorySize = 256;
    /* access modifiers changed from: private */
    public boolean isFpsBoost = false;
    private final INeteaseSceneSdkService.Stub mBinder = new INeteaseSceneSdkService.Stub() {
        public float getVersion() {
            SceneSdkService.this.SLOGI("getVersion: 0.6");
            return SceneSdkService.SCENE_SDK_VERSION;
        }

        public boolean initNeteaseSceneSdk() {
            SceneSdkService.this.SLOGI("initSceneSdk.");
            String access$100 = SceneSdkService.this.getCallerPkgName();
            if (access$100 == null) {
                GosLog.w(SceneSdkService.LOG_TAG, "initSceneSdk. callerPkgName == null");
                return false;
            }
            boolean initSdk = ExternalSdkCore.getInstance().initSdk(Const.SdkType.NETEASE_SCENE_SDK, access$100);
            if (initSdk) {
                SceneSdkService.this.createSharedMemery();
            }
            return initSdk;
        }

        public boolean setNeteaseSceneSdkListener(final INeteaseSceneSdkListener iNeteaseSceneSdkListener) {
            GosLog.d(SceneSdkService.LOG_TAG, "setSceneSdkListener.");
            return ExternalSdkCore.getInstance().setExternalSdkListener(new IExternalSdkListener() {
                public void onSiopLevelChanged(int i, int i2) {
                    int access$300 = SceneSdkService.this.getAbstractTempLevel(i2);
                    if (access$300 != SceneSdkService.this.mTempLevel) {
                        int unused = SceneSdkService.this.mTempLevel = access$300;
                        if (SceneSdkService.this.mTempLevel > 1) {
                            GosLog.e(SceneSdkService.LOG_TAG, "release boost due to the temp");
                            ExternalSdkCore.getInstance().setPerformanceLevel((Integer) null, 0);
                        }
                        if (iNeteaseSceneSdkListener != null) {
                            try {
                                SceneSdkService sceneSdkService = SceneSdkService.this;
                                sceneSdkService.SLOGI("send systemCallBack. TempLevel: " + SceneSdkService.this.mTempLevel);
                                iNeteaseSceneSdkListener.systemCallBack(SceneSdkService.this.mTempLevel);
                                ExternalSdkCore instance = ExternalSdkCore.getInstance();
                                instance.logEvent("systemCallBack() {\"mTempLevel\":" + SceneSdkService.this.mTempLevel + "}");
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                public void onResult(int i, int i2) {
                    if (i < 0) {
                        if (i != SceneSdkService.GAME_RESUME_RESULT) {
                            if (i != SceneSdkService.GAME_PAUSE_RESULT) {
                                GosLog.e(SceneSdkService.LOG_TAG, "unexpected id: " + i);
                                return;
                            }
                            int unused = SceneSdkService.this.targetFpsInt = -1;
                            String unused2 = SceneSdkService.this.targetFps = "-1";
                        } else if (SceneSdkService.this.targetFpsRecord == null) {
                        } else {
                            if (SceneSdkService.this.targetFpsRecord.containsKey(ExternalSdkCore.getInstance().getCurGamePkgName())) {
                                int unused3 = SceneSdkService.this.targetFpsInt = ((Integer) SceneSdkService.this.targetFpsRecord.get(ExternalSdkCore.getInstance().getCurGamePkgName())).intValue();
                                String unused4 = SceneSdkService.this.targetFps = String.valueOf(SceneSdkService.this.targetFpsInt);
                                return;
                            }
                            int unused5 = SceneSdkService.this.targetFpsInt = -1;
                            String unused6 = SceneSdkService.this.targetFps = "-1";
                            boolean unused7 = SceneSdkService.this.isFpsBoost = false;
                        }
                    } else if (iNeteaseSceneSdkListener != null) {
                        SceneSdkService sceneSdkService = SceneSdkService.this;
                        sceneSdkService.SLOGI("send onResult. id: " + i + ", result: " + i2);
                    }
                }
            });
        }

        public int transferGameInfo(String str) {
            String str2 = "scene_importance";
            String str3 = "fps";
            String str4 = "sceneId";
            SceneSdkService sceneSdkService = SceneSdkService.this;
            sceneSdkService.SLOGI("transferGameInfo. json: " + str);
            if (!SceneSdkService.SCENESDK_SWITCH || str == null) {
                SceneSdkService sceneSdkService2 = SceneSdkService.this;
                sceneSdkService2.SLOGI("transferGameInfo. SCENESDK_SWITCH : " + SceneSdkService.SCENESDK_SWITCH);
                return -1;
            }
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (!jSONObject.has(str4)) {
                    str4 = SceneSdkService.SCENE_ID;
                }
                int optInt = jSONObject.optInt(str4, -1);
                if (!jSONObject.has(str3)) {
                    str3 = SceneSdkService.FPS;
                }
                double optDouble = jSONObject.optDouble(str3, -1.0d);
                if (!jSONObject.has(str2)) {
                    str2 = SceneSdkService.SCENE_IMPORTANCE;
                }
                int optInt2 = jSONObject.optInt(str2, -1);
                String unused = SceneSdkService.this.targetFps = jSONObject.optString(SceneSdkService.TARGET_FPS, SceneSdkService.this.targetFps);
                if (optDouble > ((double) Integer.parseInt(SceneSdkService.this.targetFps))) {
                    String unused2 = SceneSdkService.this.targetFps = String.valueOf((int) optDouble);
                }
                if (optInt2 > -1 && optInt2 < 6) {
                    SceneSdkService sceneSdkService3 = SceneSdkService.this;
                    sceneSdkService3.SLOGI("transferGameInfo. sceneImportanceLevel: " + optInt2);
                    SceneSdkService.this.writeImportanceLevelToSharedMemory(optInt2);
                }
                if (optInt != -1 || jSONObject.has(SceneSdkService.TARGET_FPS)) {
                    ExternalSdkCore instance = ExternalSdkCore.getInstance();
                    instance.logEvent("transferGameInfo() " + str);
                }
                if (SceneSdkService.this.mTempLevel > 1) {
                    SceneSdkService sceneSdkService4 = SceneSdkService.this;
                    sceneSdkService4.SLOGI("mTempLevel is: " + SceneSdkService.this.mTempLevel);
                    return 0;
                }
                try {
                    transferGameInfoUsingTargetFps();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                transferGameInfoUsingSceneId(optInt);
                if (!setResourceApplyType(optDouble)) {
                    Const.ApplyType unused3 = SceneSdkService.this.mResourceApplyType = Const.ApplyType.NONE;
                    return 0;
                }
                Const.ApplyType unused4 = SceneSdkService.this.mResourceApplyType = Const.ApplyType.NONE;
                return 0;
            } catch (NumberFormatException | JSONException e2) {
                e2.printStackTrace();
                return -1;
            }
        }

        private void transferGameInfoUsingTargetFps() {
            if (SceneSdkService.this.targetFpsInt != Integer.parseInt(SceneSdkService.this.targetFps)) {
                SceneSdkService sceneSdkService = SceneSdkService.this;
                int unused = sceneSdkService.targetFpsInt = Integer.parseInt(sceneSdkService.targetFps);
                if (SceneSdkService.this.targetFpsInt > 0) {
                    if (SceneSdkService.this.targetFpsInt <= 30) {
                        Const.GameFPSMode unused2 = SceneSdkService.this.mFPSMode = Const.GameFPSMode.LOW_FPS;
                    } else if (SceneSdkService.this.targetFpsInt <= 60) {
                        Const.GameFPSMode unused3 = SceneSdkService.this.mFPSMode = Const.GameFPSMode.HIGH_FPS;
                    } else {
                        Const.GameFPSMode unused4 = SceneSdkService.this.mFPSMode = Const.GameFPSMode.ULTRA_FPS;
                        ExternalSdkCore.getInstance().setMaxFps(SceneSdkService.this.targetFpsInt);
                    }
                    GosLog.d(SceneSdkService.LOG_TAG, "Gfi updateInGameFPSLimit" + SceneSdkService.this.targetFpsInt);
                    GfiFeature.getInstance(AppContext.get()).updateInGameFPSLimit(SceneSdkService.this.getCallerPkgName(), SceneSdkService.this.targetFpsInt);
                }
                if (SceneSdkService.this.targetFpsRecord != null) {
                    SceneSdkService.this.targetFpsRecord.put(SceneSdkService.this.getCallerPkgName(), Integer.valueOf(SceneSdkService.this.targetFpsInt));
                }
            }
        }

        private void transferGameInfoUsingSceneId(int i) {
            if (i != 7) {
                if (i != 10) {
                    if (!(i == 12 || i == 14 || i == 103)) {
                        switch (i) {
                            case -1:
                                return;
                            case 0:
                            case 4:
                                break;
                            case 1:
                            case 2:
                            case 3:
                                boolean unused = SceneSdkService.this.isFpsBoost = false;
                                Const.ApplyType unused2 = SceneSdkService.this.mResourceApplyType = Const.ApplyType.CRITICAL;
                                ExternalSdkCore.getInstance().setBoost(10);
                                return;
                            case 5:
                                break;
                            default:
                                Const.ApplyType unused3 = SceneSdkService.this.mResourceApplyType = Const.ApplyType.NONE;
                                return;
                        }
                    }
                    boolean unused4 = SceneSdkService.this.isFpsBoost = true;
                    Const.ApplyType unused5 = SceneSdkService.this.mResourceApplyType = Const.ApplyType.CRITICAL;
                    ExternalSdkCore.getInstance().setBoost(10);
                    return;
                }
                boolean unused6 = SceneSdkService.this.isFpsBoost = false;
                Const.ApplyType unused7 = SceneSdkService.this.mResourceApplyType = Const.ApplyType.CRITICAL;
                return;
            }
            boolean unused8 = SceneSdkService.this.isFpsBoost = false;
            Const.ApplyType unused9 = SceneSdkService.this.mResourceApplyType = Const.ApplyType.NONE;
        }

        private boolean setResourceApplyType(double d) {
            if (SceneSdkService.this.mResourceApplyType != Const.ApplyType.NONE) {
                ExternalSdkCore.getInstance().setPerformanceLevel(Integer.valueOf(SceneSdkService.this.mResourceApplyType.ordinal()), 15);
                return false;
            } else if (!SceneSdkService.this.isFpsBoost || SceneSdkService.this.targetFpsInt <= 0 || d <= 1.0d) {
                return true;
            } else {
                if (SceneSdkService.this.mFPSMode != Const.GameFPSMode.ULTRA_FPS) {
                    SceneSdkService sceneSdkService = SceneSdkService.this;
                    Const.ApplyType unused = sceneSdkService.mResourceApplyType = d < ((double) sceneSdkService.targetFpsInt) * 0.85d ? Const.ApplyType.CRITICAL : Const.ApplyType.NONE;
                } else {
                    SceneSdkService sceneSdkService2 = SceneSdkService.this;
                    Const.ApplyType unused2 = sceneSdkService2.mResourceApplyType = d < ((double) sceneSdkService2.targetFpsInt) / 2.0d ? Const.ApplyType.CRITICAL : Const.ApplyType.NONE;
                }
                if (SceneSdkService.this.mResourceApplyType == Const.ApplyType.ULTRA) {
                    ExternalSdkCore.getInstance().setPerformanceLevel(Integer.valueOf(SceneSdkService.this.mResourceApplyType.ordinal()), 5);
                }
                return false;
            }
        }

        public int transferThreadId(String str) {
            SceneSdkService sceneSdkService = SceneSdkService.this;
            sceneSdkService.SLOGI("transferThreadId. json: " + str);
            if (str == null) {
                return -1;
            }
            try {
                if (!new JSONObject(str).has("tid")) {
                    return 0;
                }
                ExternalSdkCore instance = ExternalSdkCore.getInstance();
                instance.logEvent("transferThreadId() " + str);
                return 0;
            } catch (JSONException e) {
                e.printStackTrace();
                return -1;
            }
        }
    };
    private IpmCore mCore;
    /* access modifiers changed from: private */
    public Const.GameFPSMode mFPSMode = Const.GameFPSMode.LOW_FPS;
    private MemoryFile mFile = null;
    /* access modifiers changed from: private */
    public Const.ApplyType mResourceApplyType = Const.ApplyType.NONE;
    /* access modifiers changed from: private */
    public int mTempLevel = -1000;
    /* access modifiers changed from: private */
    public String targetFps = "-1";
    /* access modifiers changed from: private */
    public int targetFpsInt = -1;
    /* access modifiers changed from: private */
    public HashMap<String, Integer> targetFpsRecord = null;
    private int unitTestFlag = 0;

    /* access modifiers changed from: private */
    public int getAbstractTempLevel(int i) {
        if (i >= 5) {
            return 2;
        }
        return i >= 3 ? 1 : 0;
    }

    /* access modifiers changed from: private */
    public void SLOGI(String str) {
        if (DEBUG_INFO) {
            GosLog.d(LOG_TAG, str);
        }
    }

    private void sdkSwitch() {
        SCENESDK_SWITCH = !new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/scenesdkswitch/").exists();
    }

    private void sdkLogSwitch() {
        DEBUG_INFO = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/scenesdklog/").exists();
    }

    public IBinder onBind(Intent intent) {
        SLOGI("onBind.");
        getIpmCore().setEnableToTGPA(true);
        return this.mBinder;
    }

    public void onRebind(Intent intent) {
        SLOGI("onRebind.");
    }

    public boolean onUnbind(Intent intent) {
        SLOGI("onUnbind.");
        this.targetFps = "-1";
        this.targetFpsInt = -1;
        this.isFpsBoost = false;
        SCENESDK_SWITCH = true;
        DEBUG_INFO = false;
        this.mFPSMode = Const.GameFPSMode.LOW_FPS;
        this.mResourceApplyType = Const.ApplyType.NONE;
        HashMap<String, Integer> hashMap = this.targetFpsRecord;
        if (hashMap != null) {
            hashMap.clear();
            this.targetFpsRecord = null;
        }
        getIpmCore().setEnableToTGPA(false);
        ExternalSdkCore.getInstance().restoreDefault();
        return super.onUnbind(intent);
    }

    public void onCreate() {
        super.onCreate();
        this.targetFpsRecord = new HashMap<>();
        sdkSwitch();
        sdkLogSwitch();
        SLOGI("onCreate.");
    }

    /* access modifiers changed from: private */
    public String getCallerPkgName() {
        PackageManager packageManager = getPackageManager();
        if (packageManager == null) {
            return null;
        }
        int callingUid = Binder.getCallingUid();
        String nameForUid = packageManager.getNameForUid(callingUid);
        if (nameForUid != null && nameForUid.contains(":")) {
            nameForUid = SeActivityManager.getInstance().getAppNameFromPid(AppContext.get(), Binder.getCallingPid());
            GosLog.d(LOG_TAG, "getCallerPkgName(), _pkgName: " + nameForUid);
        }
        SLOGI("getCallerPkgName(), callerUid: " + callingUid + ", callerPkgName: " + nameForUid);
        return nameForUid;
    }

    /* access modifiers changed from: private */
    public void createSharedMemery() {
        try {
            MemoryFile memoryFile = new MemoryFile("sceneSDKShareMemory", 256);
            this.mFile = memoryFile;
            FileDescriptor fileDescriptor = (FileDescriptor) memoryFile.getClass().getDeclaredMethod("getFileDescriptor", new Class[0]).invoke(this.mFile, new Object[0]);
            SLOGI("File is " + fileDescriptor.valid());
            int intValue = ((Integer) fileDescriptor.getClass().getDeclaredMethod("getInt$", new Class[0]).invoke(fileDescriptor, new Object[0])).intValue();
            SLOGI("nativeFd is " + intValue);
            IpmCore.getInstance(AppContext.get()).setLowLatencySceneSDK(intValue, "{\"1\":{\"name\":\"main version\",\"type\":\"string\",\"length\":\"16\"},\"2\":{\"name\":\"resource version\",\"type\":\"string\",\"length\":\"16\"},\"4\":{\"name\":\"scene\",\"type\":\"integer\",\"maximum\":\"1000\",\"minimum\":\"0\"},\"5\":{\"name\":\"fps\",\"type\":\"integer\",\"maximum\":\"60\",\"minimum\":\"0\"},\"6\":{\"name\":\"frame miss\",\"type\":\"integer\",\"maximum\":\"30\",\"minimum\":\"0\"},\"7\":{\"name\":\"limit fps\",\"type\":\"integer\",\"maximum\":\"60\",\"minimum\":\"0\"},\"8\":{\"name\":\"model quality\",\"type\":\"integer\"},\"9\":{\"name\":\"effect quality\",\"type\":\"integer\"},\"10\":{\"name\":\"resolution\",\"type\":\"label\",\"value\":{\"0\":\"720P\",\"1\":\"1080P\"}},\"11\":{\"name\":\"user count\",\"type\":\"integer\",\"maximum\":\"100\",\"minimum\":\"1\"},\"12\":{\"name\":\"network latency\",\"type\":\"integer\"},\"13\":{\"name\":\"record status\",\"type\":\"boolean\"},\"15\":{\"name\":\"server ip address\",\"type\":\"string\",\"length\":\"32\"},\"16\":{\"name\":\"role status\",\"type\":\"boolean\"},\"40\":{\"name\":\"scene type\",\"type\":\"integer\",\"maximum\":\"1000\",\"minimum\":\"0\"},\"41\":{\"name\":\"load map status\",\"type\":\"boolean\"},\"42\":{\"name\":\"bombing status\",\"type\":\"boolean\"},\"43\":{\"name\":\"multithread status\",\"type\":\"boolean\"},\"51\":{\"name\":\"heavy thread\",\"type\":\"integer\"},\"52\":{\"name\":\"role outline\",\"type\":\"boolean\"},\"53\":{\"name\":\"picture style\",\"type\":\"integer\"},\"54\":{\"name\":\"Anti-aliasing\",\"type\":\"integer\"},\"55\":{\"name\":\"Server IP port\",\"type\":\"integer\"},\"56\":{\"name\":\"protocol type\",\"type\":\"integer\"},\"57\":{\"name\":\"shadow\",\"type\":\"integer\"},\"80\":{\"name\":\"cpu level\",\"type\":\"integer\"},\"81\":{\"name\":\"gpu level\",\"type\":\"integer\"},\"82\":{\"name\":\"target fps\",\"type\":\"integer\"},\"83\":{\"name\":\"scene importance\",\"type\":\"integer\"},\"90\":{\"name\":\"tgpa version\",\"type\":\"string\",\"length\":\"16\"}}", 256, true);
            this.unitTestFlag = 1;
        } catch (Exception e) {
            this.unitTestFlag = 2;
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void writeImportanceLevelToSharedMemory(int i) {
        try {
            if (this.mFile != null) {
                this.mFile.writeBytes(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(i).array(), 0, mImportanceOffSet, 4);
                this.unitTestFlag = 3;
            }
        } catch (IOException e) {
            this.unitTestFlag = 4;
            e.printStackTrace();
        }
    }

    private IpmCore getIpmCore() {
        if (this.mCore == null) {
            this.mCore = IpmCore.getInstance(AppContext.get());
        }
        return this.mCore;
    }

    public void setIpmCore(IpmCore ipmCore) {
        this.mCore = ipmCore;
    }

    public boolean getFpsBoost() {
        return this.isFpsBoost;
    }

    public int getUnitTestFlag() {
        return this.unitTestFlag;
    }
}
