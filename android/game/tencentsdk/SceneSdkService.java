package com.samsung.android.game.tencentsdk;

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
import com.samsung.android.game.gos.feature.ipm.IpmCore;
import com.samsung.android.game.gos.selibrary.SeActivityManager;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.tencentsdk.ISceneSdkService;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class SceneSdkService extends Service {
    private static final String CHINA_PUBG_PACKAGENAME = "com.tencent.tmgp.pubgmhd";
    private static final int CREATE_FILE_FAIL = 2;
    private static final int CREATE_FILE_SUCCESS = 1;
    private static boolean DEBUG_INFO = false;
    private static final int DEFAULT = 0;
    private static final int FALL_BACK_ID = -1;
    private static final String FPS = "5";
    private static final int GAMEHOME = 4;
    private static final int GAMESCENE_INTO_BATTLE = 5;
    private static final int GAMESCENE_LOAD = 6;
    private static final int GAMESCENE_LOADING = 5;
    private static final int GAME_LOADING = 3;
    private static final int GAME_PAUSE_RESULT = -1001;
    private static final int GAME_RESUME_RESULT = -1002;
    private static final int GAME_STARTING = 1;
    private static final int GAME_UPDATING = 2;
    private static final String GLOBAL_PUBG_PACKAGENAME = "com.tencent.ig";
    private static final String INQUIRE_KEY = "10000";
    private static final String KOREA_PUBG_PACKAGENAME = "com.pubg.krmobile";
    private static final String LOG_TAG = "SceneSdkService";
    private static final String PLAYERS = "11";
    private static final int PUBG_WATCHING = 110;
    /* access modifiers changed from: private */
    public static boolean PUBG_WATCH_BOOST = false;
    /* access modifiers changed from: private */
    public static boolean PUBG_WATCH_MODE = false;
    /* access modifiers changed from: private */
    public static boolean SCENESDK_SWITCH = true;
    private static final String SCENE_ID = "4";
    private static final String SCENE_IMPORTANCE = "83";
    private static final String SCENE_IMPORTANCE_KEY = "1001";
    private static final float SCENE_SDK_VERSION = 0.91f;
    private static final int SIOP_LEVEL_BOUNDARY_FOR_HIGH = 5;
    private static final int SIOP_LEVEL_BOUNDARY_FOR_MID = 3;
    private static final int SUPPORT_BIGCORE_OPTIMIZATION = 3;
    private static final int SUPPORT_GAME_ACTIVATE = 1003;
    private static final int SUPPORT_NETWORK_OPTIMIZATION = 1002;
    private static final int SUPPORT_PERFORMANCE_OPTIMIZATION = 1001;
    private static final String SUPPORT_PROTOCOL_VERSION = "1.0";
    private static final int SUPPORT_RESOURCES_OPTIMIZATION = 1004;
    private static final int SUPPORT_SAMESCREEN_PLAYERS_OPTIMIZATION = 5;
    private static final int SUPPORT_SCENE_OPTIMIZATION = 2;
    private static final int SUPPORT_SIOP_NOTIFICATION = 1;
    private static final int SUPPORT_SMALLCORE_OPTIMIZATION = 4;
    private static final int SUPPORT_SSP_AI_OPTIMIZATION = 7;
    private static final int SUPPORT_TOUCH_MANAGEMENT_OPTIMIZATION = 8;
    private static final String TARGET_FPS = "7";
    static final int TEMP_LEVEL_HIGH = 2;
    static final int TEMP_LEVEL_LOW = 0;
    static final int TEMP_LEVEL_MID = 1;
    static final int TEMP_LEVEL_UNKNOWN = -1000;
    private static final int TENCENT_GAME_BOOST_TIMEOUT = 5;
    private static final int TENCENT_PLAYER_BOOST_TIMES_LIMIT = 20;
    private static final int VICTORY = 9;
    private static final int WATCHING = 803;
    private static final int WRITE_VALUE_FAIL = 4;
    private static final int WRITE_VALUE_SUCCESS = 3;
    private static final int mImportanceOffSet = 168;
    private static final int mSharedMemorySize = 256;
    /* access modifiers changed from: private */
    public int currentId;
    /* access modifiers changed from: private */
    public int gfiTargetFps = -1;
    /* access modifiers changed from: private */
    public boolean isFpsBoost = false;
    /* access modifiers changed from: private */
    public boolean mBPubgGame = false;
    /* access modifiers changed from: private */
    public int mBattleState = -1;
    private final ISceneSdkService.Stub mBinder = new ISceneSdkService.Stub() {
        public float getVersion() throws RemoteException {
            SceneSdkService.this.SLOGI("getVersion: 0.91");
            return SceneSdkService.SCENE_SDK_VERSION;
        }

        public boolean initSceneSdk() throws RemoteException {
            SceneSdkService.this.SLOGI("initSceneSdk.");
            String access$100 = SceneSdkService.this.getCallerPkgName();
            if (access$100 == null) {
                GosLog.w(SceneSdkService.LOG_TAG, "initSceneSdk. callerPkgName == null");
                return false;
            }
            boolean initSdk = ExternalSdkCore.getInstance().initSdk(Const.SdkType.TENCENT_SCENE_SDK, access$100);
            if (initSdk) {
                SceneSdkService.this.createSharedMemery();
                if (access$100.equals(SceneSdkService.CHINA_PUBG_PACKAGENAME) || access$100.equals(SceneSdkService.KOREA_PUBG_PACKAGENAME) || access$100.equals(SceneSdkService.GLOBAL_PUBG_PACKAGENAME)) {
                    boolean unused = SceneSdkService.this.mBPubgGame = true;
                }
            }
            return initSdk;
        }

        public boolean setSceneSdkListener(final ISceneSdkListener iSceneSdkListener) throws RemoteException {
            SceneSdkService.this.SLOGI("setSceneSdkListener.");
            return ExternalSdkCore.getInstance().setExternalSdkListener(new IExternalSdkListener() {
                public void onSiopLevelChanged(int i, int i2) {
                    int access$400 = SceneSdkService.this.getAbstractTempLevel(i2);
                    if (access$400 != SceneSdkService.this.mTempLevel) {
                        int unused = SceneSdkService.this.mTempLevel = access$400;
                        if (SceneSdkService.this.mTempLevel > 1) {
                            GosLog.e(SceneSdkService.LOG_TAG, "release boost due to the temp");
                            ExternalSdkCore.getInstance().setPerformanceLevel((Integer) null, 0);
                        }
                        if (iSceneSdkListener != null) {
                            try {
                                SceneSdkService sceneSdkService = SceneSdkService.this;
                                sceneSdkService.SLOGI("send systemCallBack. TempLevel: " + SceneSdkService.this.mTempLevel);
                                iSceneSdkListener.systemCallBack(SceneSdkService.this.mTempLevel);
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
                            int unused3 = SceneSdkService.this.gfiTargetFps = -1;
                        } else if (SceneSdkService.this.targetFpsRecord == null) {
                        } else {
                            if (SceneSdkService.this.targetFpsRecord.containsKey(ExternalSdkCore.getInstance().getCurGamePkgName())) {
                                int unused4 = SceneSdkService.this.targetFpsInt = ((Integer) SceneSdkService.this.targetFpsRecord.get(ExternalSdkCore.getInstance().getCurGamePkgName())).intValue();
                                String unused5 = SceneSdkService.this.targetFps = String.valueOf(SceneSdkService.this.targetFpsInt);
                                return;
                            }
                            int unused6 = SceneSdkService.this.targetFpsInt = -1;
                            int unused7 = SceneSdkService.this.gfiTargetFps = -1;
                            String unused8 = SceneSdkService.this.targetFps = "-1";
                            boolean unused9 = SceneSdkService.this.isFpsBoost = false;
                        }
                    } else if (iSceneSdkListener != null) {
                        try {
                            SceneSdkService sceneSdkService = SceneSdkService.this;
                            sceneSdkService.SLOGI("send onResult. id: " + i + ", result: " + i2);
                            iSceneSdkListener.resultCallBack(i, i2);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        public int updateGameInfo(String str) throws RemoteException {
            String str2 = "scene_importance";
            String str3 = "player";
            String str4 = "fps";
            String str5 = "sceneId";
            SceneSdkService sceneSdkService = SceneSdkService.this;
            sceneSdkService.SLOGI("updateGameInfo. json: " + str);
            int i = -1;
            if (!SceneSdkService.SCENESDK_SWITCH || str == null) {
                SceneSdkService sceneSdkService2 = SceneSdkService.this;
                sceneSdkService2.SLOGI("updateGameInfo. SCENESDK_SWITCH : " + SceneSdkService.SCENESDK_SWITCH);
                return -1;
            }
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (!jSONObject.has(str5)) {
                    str5 = SceneSdkService.SCENE_ID;
                }
                int optInt = jSONObject.optInt(str5, -1);
                if (!jSONObject.has(str4)) {
                    str4 = SceneSdkService.FPS;
                }
                double optDouble = jSONObject.optDouble(str4, -1.0d);
                SceneSdkService sceneSdkService3 = SceneSdkService.this;
                if (!jSONObject.has(str3)) {
                    str3 = SceneSdkService.PLAYERS;
                }
                String unused = sceneSdkService3.player = jSONObject.optString(str3, "-1");
                if (!jSONObject.has(str2)) {
                    str2 = SceneSdkService.SCENE_IMPORTANCE;
                }
                int optInt2 = jSONObject.optInt(str2, -1);
                if (optInt2 == -1) {
                    optInt2 = jSONObject.optInt(SceneSdkService.SCENE_IMPORTANCE_KEY, -1);
                }
                String unused2 = SceneSdkService.this.targetFps = jSONObject.optString(SceneSdkService.TARGET_FPS, "-1");
                int unused3 = SceneSdkService.this.currentId = jSONObject.optInt("id", SceneSdkService.this.currentId);
                if (optInt != -1 || jSONObject.has(SceneSdkService.TARGET_FPS)) {
                    ExternalSdkCore instance = ExternalSdkCore.getInstance();
                    instance.logEvent("updateGameInfo() " + str);
                }
                if (optInt2 > -1 && optInt2 < 6) {
                    SceneSdkService sceneSdkService4 = SceneSdkService.this;
                    sceneSdkService4.SLOGI("updateGameInfo. sceneImportanceLevel: " + optInt2);
                    SceneSdkService.this.writeImportanceLevelToSharedMemory(optInt2);
                }
                if (SceneSdkService.this.mTempLevel > 1) {
                    return 0;
                }
                try {
                    i = Integer.parseInt(SceneSdkService.this.player);
                    updateGameInfoUsingTargetFps(optInt);
                } catch (NumberFormatException e) {
                    GosLog.w(SceneSdkService.LOG_TAG, "updateGameInfo(): ", e);
                }
                updateGameInfoUsingSceneId(optInt);
                if (!setResourceApplyType(i, str, optDouble)) {
                    Const.ApplyType unused4 = SceneSdkService.this.mResourceApplyType = Const.ApplyType.NONE;
                    return 0;
                }
                Const.ApplyType unused5 = SceneSdkService.this.mResourceApplyType = Const.ApplyType.NONE;
                return 0;
            } catch (JSONException e2) {
                e2.printStackTrace();
                return -1;
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:37:0x0112, code lost:
            if (com.samsung.android.game.tencentsdk.SceneSdkService.access$800(r5.this$0) != com.samsung.android.game.tencentsdk.SceneSdkService.access$600(r5.this$0)) goto L_0x0116;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void updateGameInfoUsingTargetFps(int r6) {
            /*
                r5 = this;
                com.samsung.android.game.gos.data.dbhelper.DbHelper r0 = com.samsung.android.game.gos.data.dbhelper.DbHelper.getInstance()
                com.samsung.android.game.gos.data.dao.GlobalDao r0 = r0.getGlobalDao()
                int r0 = r0.getSiopMode()
                r1 = 0
                if (r0 != 0) goto L_0x001f
                com.samsung.android.game.tencentsdk.SceneSdkService r0 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                com.samsung.android.game.gos.feature.ipm.IpmCore r0 = r0.getIpmCore()
                r0.setEnabledDynamicSpa(r1)
                com.samsung.android.game.tencentsdk.SceneSdkService r0 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                java.lang.String r2 = "Dynamic SPA is disabled"
                r0.SLOGI(r2)
            L_0x001f:
                com.samsung.android.game.tencentsdk.SceneSdkService r0 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                boolean r0 = r0.mBPubgGame
                r2 = 5
                if (r0 == 0) goto L_0x004a
                if (r6 != r2) goto L_0x0038
                com.samsung.android.game.tencentsdk.SceneSdkService r0 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                int r0 = r0.mBattleState
                if (r0 == r2) goto L_0x0038
                com.samsung.android.game.tencentsdk.SceneSdkService r0 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                int unused = r0.mBattleState = r6
                goto L_0x004a
            L_0x0038:
                r0 = 3
                if (r6 == r0) goto L_0x003d
                if (r6 != 0) goto L_0x004a
            L_0x003d:
                com.samsung.android.game.tencentsdk.SceneSdkService r0 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                int r0 = r0.mBattleState
                if (r0 != r2) goto L_0x004a
                com.samsung.android.game.tencentsdk.SceneSdkService r0 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                int unused = r0.mBattleState = r6
            L_0x004a:
                com.samsung.android.game.tencentsdk.SceneSdkService r6 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                java.lang.String r6 = r6.targetFps
                float r6 = java.lang.Float.parseFloat(r6)
                int r6 = (int) r6
                if (r6 <= 0) goto L_0x0152
                com.samsung.android.game.tencentsdk.SceneSdkService r0 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                int r0 = r0.targetFpsInt
                r3 = 1
                if (r0 == r6) goto L_0x00da
                com.samsung.android.game.tencentsdk.SceneSdkService r0 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                int unused = r0.targetFpsInt = r6
                com.samsung.android.game.tencentsdk.SceneSdkService r6 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                int r6 = r6.targetFpsInt
                r0 = 30
                if (r6 > r0) goto L_0x0077
                com.samsung.android.game.tencentsdk.SceneSdkService r6 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                com.samsung.android.game.gos.feature.externalsdk.value.Const$GameFPSMode r0 = com.samsung.android.game.gos.feature.externalsdk.value.Const.GameFPSMode.LOW_FPS
                com.samsung.android.game.gos.feature.externalsdk.value.Const.GameFPSMode unused = r6.mFPSMode = r0
                goto L_0x009d
            L_0x0077:
                com.samsung.android.game.tencentsdk.SceneSdkService r6 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                int r6 = r6.targetFpsInt
                r0 = 60
                if (r6 > r0) goto L_0x0089
                com.samsung.android.game.tencentsdk.SceneSdkService r6 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                com.samsung.android.game.gos.feature.externalsdk.value.Const$GameFPSMode r0 = com.samsung.android.game.gos.feature.externalsdk.value.Const.GameFPSMode.HIGH_FPS
                com.samsung.android.game.gos.feature.externalsdk.value.Const.GameFPSMode unused = r6.mFPSMode = r0
                goto L_0x009d
            L_0x0089:
                com.samsung.android.game.tencentsdk.SceneSdkService r6 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                com.samsung.android.game.gos.feature.externalsdk.value.Const$GameFPSMode r0 = com.samsung.android.game.gos.feature.externalsdk.value.Const.GameFPSMode.ULTRA_FPS
                com.samsung.android.game.gos.feature.externalsdk.value.Const.GameFPSMode unused = r6.mFPSMode = r0
                com.samsung.android.game.gos.feature.externalsdk.ExternalSdkCore r6 = com.samsung.android.game.gos.feature.externalsdk.ExternalSdkCore.getInstance()
                com.samsung.android.game.tencentsdk.SceneSdkService r0 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                int r0 = r0.targetFpsInt
                r6.setMaxFps(r0)
            L_0x009d:
                com.samsung.android.game.gos.feature.vrr.DrrCore r6 = com.samsung.android.game.gos.feature.vrr.DrrCore.getInstance()
                com.samsung.android.game.tencentsdk.SceneSdkService r0 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                int r0 = r0.targetFpsInt
                com.samsung.android.game.tencentsdk.SceneSdkService r4 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                java.lang.String r4 = r4.getCallerPkgName()
                r6.updateGameRefreshRate(r0, r4)
                com.samsung.android.game.tencentsdk.SceneSdkService r6 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                java.util.HashMap r6 = r6.targetFpsRecord
                if (r6 == 0) goto L_0x00d1
                com.samsung.android.game.tencentsdk.SceneSdkService r6 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                java.util.HashMap r6 = r6.targetFpsRecord
                com.samsung.android.game.tencentsdk.SceneSdkService r0 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                java.lang.String r0 = r0.getCallerPkgName()
                com.samsung.android.game.tencentsdk.SceneSdkService r4 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                int r4 = r4.targetFpsInt
                java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
                r6.put(r0, r4)
            L_0x00d1:
                com.samsung.android.game.tencentsdk.SceneSdkService r6 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                boolean r6 = r6.mBPubgGame
                if (r6 != 0) goto L_0x00da
                r1 = r3
            L_0x00da:
                com.samsung.android.game.tencentsdk.SceneSdkService r6 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                boolean r6 = r6.mBPubgGame
                java.lang.String r0 = "SceneSdkService"
                if (r6 == 0) goto L_0x0115
                com.samsung.android.game.tencentsdk.SceneSdkService r6 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                int r6 = r6.mBattleState
                if (r6 != r2) goto L_0x0115
                java.lang.StringBuilder r6 = new java.lang.StringBuilder
                r6.<init>()
                java.lang.String r2 = "last value"
                r6.append(r2)
                com.samsung.android.game.tencentsdk.SceneSdkService r2 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                int r2 = r2.gfiTargetFps
                r6.append(r2)
                java.lang.String r6 = r6.toString()
                com.samsung.android.game.gos.util.GosLog.d(r0, r6)
                com.samsung.android.game.tencentsdk.SceneSdkService r6 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                int r6 = r6.gfiTargetFps
                com.samsung.android.game.tencentsdk.SceneSdkService r2 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                int r2 = r2.targetFpsInt
                if (r6 == r2) goto L_0x0115
                goto L_0x0116
            L_0x0115:
                r3 = r1
            L_0x0116:
                if (r3 == 0) goto L_0x0152
                com.samsung.android.game.tencentsdk.SceneSdkService r6 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                int r1 = r6.targetFpsInt
                int unused = r6.gfiTargetFps = r1
                java.lang.StringBuilder r6 = new java.lang.StringBuilder
                r6.<init>()
                java.lang.String r1 = "Gfi updateInGameFPSLimit"
                r6.append(r1)
                com.samsung.android.game.tencentsdk.SceneSdkService r1 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                int r1 = r1.gfiTargetFps
                r6.append(r1)
                java.lang.String r6 = r6.toString()
                com.samsung.android.game.gos.util.GosLog.d(r0, r6)
                android.app.Application r6 = com.samsung.android.game.gos.context.AppContext.get()
                com.samsung.android.game.gos.feature.gfi.GfiFeature r6 = com.samsung.android.game.gos.feature.gfi.GfiFeature.getInstance(r6)
                com.samsung.android.game.tencentsdk.SceneSdkService r0 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                java.lang.String r0 = r0.getCallerPkgName()
                com.samsung.android.game.tencentsdk.SceneSdkService r1 = com.samsung.android.game.tencentsdk.SceneSdkService.this
                int r1 = r1.gfiTargetFps
                r6.updateInGameFPSLimit(r0, r1)
            L_0x0152:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.tencentsdk.SceneSdkService.AnonymousClass1.updateGameInfoUsingTargetFps(int):void");
        }

        private void updateGameInfoUsingSceneId(int i) {
            if (i != 9) {
                if (i == 110) {
                    GosLog.d(SceneSdkService.LOG_TAG, "set boost for watching scene");
                    boolean unused = SceneSdkService.this.isFpsBoost = true;
                    Const.ApplyType unused2 = SceneSdkService.this.mResourceApplyType = Const.ApplyType.NONE;
                    boolean unused3 = SceneSdkService.PUBG_WATCH_MODE = true;
                    return;
                } else if (i != SceneSdkService.WATCHING) {
                    switch (i) {
                        case -1:
                            return;
                        case 0:
                        case 2:
                        case 6:
                            break;
                        case 1:
                        case 3:
                            boolean unused4 = SceneSdkService.this.isFpsBoost = false;
                            Const.ApplyType unused5 = SceneSdkService.this.mResourceApplyType = Const.ApplyType.CRITICAL;
                            ExternalSdkCore.getInstance().setBoost(10);
                            return;
                        case 4:
                            boolean unused6 = SceneSdkService.PUBG_WATCH_MODE = false;
                            boolean unused7 = SceneSdkService.PUBG_WATCH_BOOST = false;
                            boolean unused8 = SceneSdkService.this.isFpsBoost = false;
                            Const.ApplyType unused9 = SceneSdkService.this.mResourceApplyType = Const.ApplyType.NONE;
                            return;
                        case 5:
                            int unused10 = SceneSdkService.this.playerBoostCounts = 0;
                            boolean unused11 = SceneSdkService.this.isFpsBoost = true;
                            Const.ApplyType unused12 = SceneSdkService.this.mResourceApplyType = Const.ApplyType.CRITICAL;
                            ExternalSdkCore.getInstance().setBoost(10);
                            return;
                        default:
                            Const.ApplyType unused13 = SceneSdkService.this.mResourceApplyType = Const.ApplyType.NONE;
                            return;
                    }
                } else {
                    GosLog.d(SceneSdkService.LOG_TAG, "set boost for watching scene");
                    boolean unused14 = SceneSdkService.this.isFpsBoost = true;
                    Const.ApplyType unused15 = SceneSdkService.this.mResourceApplyType = Const.ApplyType.NONE;
                    return;
                }
            }
            boolean unused16 = SceneSdkService.this.isFpsBoost = false;
            Const.ApplyType unused17 = SceneSdkService.this.mResourceApplyType = Const.ApplyType.NONE;
        }

        private boolean setResourceApplyType(int i, String str, double d) {
            if (SceneSdkService.this.mResourceApplyType != Const.ApplyType.NONE) {
                ExternalSdkCore.getInstance().setPerformanceLevel(Integer.valueOf(SceneSdkService.this.mResourceApplyType.ordinal()), 15);
                return false;
            } else if (i > 6 && i < 11 && SceneSdkService.this.playerBoostCounts < 20) {
                ExternalSdkCore.getInstance().setPerformanceLevel(Integer.valueOf(Const.ApplyType.CRITICAL.ordinal()), 5);
                SceneSdkService.access$2108(SceneSdkService.this);
                ExternalSdkCore instance = ExternalSdkCore.getInstance();
                instance.logEvent("updateGameInfo() " + str);
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
                if (SceneSdkService.PUBG_WATCH_MODE && !SceneSdkService.PUBG_WATCH_BOOST) {
                    GosLog.d(SceneSdkService.LOG_TAG, "boost watching mode ");
                    Const.ApplyType unused3 = SceneSdkService.this.mResourceApplyType = Const.ApplyType.CRITICAL;
                    ExternalSdkCore.getInstance().setPerformanceLevel(Integer.valueOf(SceneSdkService.this.mResourceApplyType.ordinal()), 0);
                    boolean unused4 = SceneSdkService.PUBG_WATCH_BOOST = true;
                } else if (!SceneSdkService.PUBG_WATCH_MODE && SceneSdkService.this.mResourceApplyType != Const.ApplyType.NONE) {
                    ExternalSdkCore.getInstance().setPerformanceLevel(Integer.valueOf(SceneSdkService.this.mResourceApplyType.ordinal()), 5);
                }
                return false;
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:27:0x00a0 A[Catch:{ JSONException -> 0x00c3 }] */
        /* JADX WARNING: Removed duplicated region for block: B:38:0x0025 A[SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int initLowLatencyIPC(java.lang.String r11, com.samsung.android.game.compatibility.SharedMemory r12) throws android.os.RemoteException {
            /*
                r10 = this;
                java.lang.String r0 = "length"
                java.lang.String r1 = "type"
                java.lang.String r2 = "SceneSdkService"
                org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ JSONException -> 0x00c3 }
                r3.<init>(r11)     // Catch:{ JSONException -> 0x00c3 }
                java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x00c3 }
                r4.<init>()     // Catch:{ JSONException -> 0x00c3 }
                java.lang.String r5 = "initLowLatencyIPC: "
                r4.append(r5)     // Catch:{ JSONException -> 0x00c3 }
                r4.append(r11)     // Catch:{ JSONException -> 0x00c3 }
                java.lang.String r4 = r4.toString()     // Catch:{ JSONException -> 0x00c3 }
                com.samsung.android.game.gos.util.GosLog.d(r2, r4)     // Catch:{ JSONException -> 0x00c3 }
                java.util.Iterator r4 = r3.keys()     // Catch:{ JSONException -> 0x00c3 }
                r5 = 0
                r6 = r5
            L_0x0025:
                boolean r7 = r4.hasNext()     // Catch:{ JSONException -> 0x00c3 }
                if (r7 == 0) goto L_0x00a7
                java.lang.Object r7 = r4.next()     // Catch:{ JSONException -> 0x00c3 }
                java.lang.String r7 = (java.lang.String) r7     // Catch:{ JSONException -> 0x00c3 }
                org.json.JSONObject r7 = r3.getJSONObject(r7)     // Catch:{ JSONException -> 0x00c3 }
                boolean r8 = r7.has(r1)     // Catch:{ JSONException -> 0x00c3 }
                if (r8 == 0) goto L_0x0092
                java.lang.String r8 = r7.getString(r1)     // Catch:{ JSONException -> 0x00c3 }
                java.lang.String r9 = "string"
                boolean r8 = r8.equals(r9)     // Catch:{ JSONException -> 0x00c3 }
                if (r8 == 0) goto L_0x0065
                boolean r8 = r7.has(r0)     // Catch:{ JSONException -> 0x00c3 }
                if (r8 == 0) goto L_0x0098
                java.lang.String r8 = r7.getString(r0)     // Catch:{ JSONException -> 0x00c3 }
                int r8 = java.lang.Integer.parseInt(r8)     // Catch:{ JSONException -> 0x00c3 }
                int r9 = r8 % 4
                if (r9 == 0) goto L_0x0063
                java.lang.String r9 = "initLowLatencyIPC: strings must have length divisible by 4."
                com.samsung.android.game.gos.util.GosLog.e(r2, r9)     // Catch:{ JSONException -> 0x00c3 }
                int r9 = r8 % 4
                int r9 = 4 - r9
                int r8 = r8 + r9
            L_0x0063:
                int r6 = r6 + r8
                goto L_0x0098
            L_0x0065:
                java.lang.String r8 = r7.getString(r1)     // Catch:{ JSONException -> 0x00c3 }
                java.lang.String r9 = "integer"
                boolean r8 = r8.equals(r9)     // Catch:{ JSONException -> 0x00c3 }
                if (r8 != 0) goto L_0x008f
                java.lang.String r8 = r7.getString(r1)     // Catch:{ JSONException -> 0x00c3 }
                java.lang.String r9 = "label"
                boolean r8 = r8.equals(r9)     // Catch:{ JSONException -> 0x00c3 }
                if (r8 != 0) goto L_0x008f
                java.lang.String r8 = r7.getString(r1)     // Catch:{ JSONException -> 0x00c3 }
                java.lang.String r9 = "boolean"
                boolean r8 = r8.equals(r9)     // Catch:{ JSONException -> 0x00c3 }
                if (r8 == 0) goto L_0x008a
                goto L_0x008f
            L_0x008a:
                java.lang.String r8 = "Element type not recognised"
                com.samsung.android.game.gos.util.GosLog.e(r2, r8)     // Catch:{ JSONException -> 0x00c3 }
            L_0x008f:
                int r6 = r6 + 4
                goto L_0x0098
            L_0x0092:
                java.lang.String r8 = "initLowLatencyIPC: Element has no type."
                com.samsung.android.game.gos.util.GosLog.e(r2, r8)     // Catch:{ JSONException -> 0x00c3 }
                goto L_0x008f
            L_0x0098:
                java.lang.String r8 = "name"
                boolean r7 = r7.has(r8)     // Catch:{ JSONException -> 0x00c3 }
                if (r7 != 0) goto L_0x0025
                java.lang.String r7 = "initLowLatencyIPC: Element has no name."
                com.samsung.android.game.gos.util.GosLog.e(r2, r7)     // Catch:{ JSONException -> 0x00c3 }
                goto L_0x0025
            L_0x00a7:
                int r0 = r12.length()     // Catch:{ JSONException -> 0x00c3 }
                if (r0 < r6) goto L_0x00c1
                android.app.Application r0 = com.samsung.android.game.gos.context.AppContext.get()     // Catch:{ JSONException -> 0x00c3 }
                com.samsung.android.game.gos.feature.ipm.IpmCore r0 = com.samsung.android.game.gos.feature.ipm.IpmCore.getInstance(r0)     // Catch:{ JSONException -> 0x00c3 }
                int r1 = r12.getNativeFd()     // Catch:{ JSONException -> 0x00c3 }
                int r12 = r12.length()     // Catch:{ JSONException -> 0x00c3 }
                r0.setLowLatencySceneSDK(r1, r11, r12, r5)     // Catch:{ JSONException -> 0x00c3 }
                return r5
            L_0x00c1:
                r11 = -1
                return r11
            L_0x00c3:
                android.os.RemoteException r11 = new android.os.RemoteException
                r11.<init>()
                throw r11
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.tencentsdk.SceneSdkService.AnonymousClass1.initLowLatencyIPC(java.lang.String, com.samsung.android.game.compatibility.SharedMemory):int");
        }

        public String totgpa() throws RemoteException {
            return IpmCore.getInstance(AppContext.get()).getToTGPA();
        }

        public int registerToTGPACallback(IToTGPACallback iToTGPACallback, float f) throws RemoteException {
            if (iToTGPACallback != null) {
                if (SceneSdkService.this.mCallbackThread != null) {
                    SceneSdkService.this.mCallbackThread.disable();
                }
                TGPACallback unused = SceneSdkService.this.mCallbackThread = new TGPACallback(iToTGPACallback, f, IpmCore.getInstance(AppContext.get()));
                SceneSdkService.this.mCallbackThread.start();
                return 0;
            }
            if (SceneSdkService.this.mCallbackThread != null) {
                SceneSdkService.this.mCallbackThread.setFrequency(f);
            }
            return 0;
        }

        public int applyHardwareResource(String str) throws RemoteException {
            SceneSdkService sceneSdkService = SceneSdkService.this;
            sceneSdkService.SLOGI("applyHardwareResource. json: " + str);
            if (str == null) {
                return -1;
            }
            try {
                JSONObject jSONObject = new JSONObject(str);
                int unused = SceneSdkService.this.currentId = jSONObject.optInt("id", SceneSdkService.this.currentId);
                if (!jSONObject.has("cpuLevel") && !jSONObject.has("gpuLevel")) {
                    return 0;
                }
                ExternalSdkCore instance = ExternalSdkCore.getInstance();
                instance.logEvent("applyHardwareResource() " + str);
                return 0;
            } catch (JSONException e) {
                e.printStackTrace();
                return -1;
            }
        }

        public int applyThreadGuarantee(String str) throws RemoteException {
            SceneSdkService sceneSdkService = SceneSdkService.this;
            sceneSdkService.SLOGI("applyThreadGuarantee. json: " + str);
            if (str == null) {
                return -1;
            }
            try {
                JSONObject jSONObject = new JSONObject(str);
                int unused = SceneSdkService.this.currentId = jSONObject.optInt("id", SceneSdkService.this.currentId);
                if (!jSONObject.has("tid")) {
                    return 0;
                }
                ExternalSdkCore instance = ExternalSdkCore.getInstance();
                instance.logEvent("applyThreadGuarantee() " + str);
                return 0;
            } catch (JSONException e) {
                e.printStackTrace();
                return -1;
            }
        }

        public String getVendorSupportStrategy(String str) throws RemoteException {
            SceneSdkService.this.SLOGI("getVendorSupportStrategy. json: " + str);
            if (str == null) {
                return null;
            }
            JSONObject jSONObject = new JSONObject();
            try {
                if (new JSONObject(str).optInt(SceneSdkService.INQUIRE_KEY, -1) == 0) {
                    jSONObject.put("Version", Float.toString(getVersion()));
                    jSONObject.put("ProtocolVersion", SceneSdkService.SUPPORT_PROTOCOL_VERSION);
                    jSONObject.put("IsSupport", 1);
                    jSONObject.put("SupportScene", ((((((Integer.toString(1) + ",2") + ",5") + ",1002") + ",7") + ",1001") + ",1003") + ",1004");
                }
                return jSONObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    };
    /* access modifiers changed from: private */
    public TGPACallback mCallbackThread;
    private IpmCore mCore;
    /* access modifiers changed from: private */
    public Const.GameFPSMode mFPSMode = Const.GameFPSMode.LOW_FPS;
    private MemoryFile mFile = null;
    /* access modifiers changed from: private */
    public Const.ApplyType mResourceApplyType = Const.ApplyType.NONE;
    /* access modifiers changed from: private */
    public int mTempLevel = -1000;
    /* access modifiers changed from: private */
    public String player = "-1";
    /* access modifiers changed from: private */
    public int playerBoostCounts = 0;
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

    static /* synthetic */ int access$2108(SceneSdkService sceneSdkService) {
        int i = sceneSdkService.playerBoostCounts;
        sceneSdkService.playerBoostCounts = i + 1;
        return i;
    }

    static class TGPACallback extends Thread {
        private IToTGPACallback mCallback;
        private Boolean mContinue = true;
        private IpmCore mCore;
        private float mFrequency;

        TGPACallback(IToTGPACallback iToTGPACallback, float f, IpmCore ipmCore) {
            this.mCallback = iToTGPACallback;
            this.mFrequency = f;
            this.mCore = ipmCore;
        }

        public void run() {
            while (this.mContinue.booleanValue()) {
                try {
                    this.mCallback.totgpa(this.mCore.getToTGPA());
                    Thread.sleep((long) (this.mFrequency * 1000.0f));
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }

        public void disable() {
            this.mContinue = false;
        }

        public void enable() {
            this.mContinue = true;
        }

        public void setCallback(IToTGPACallback iToTGPACallback) {
            this.mCallback = iToTGPACallback;
        }

        public void setFrequency(float f) {
            this.mFrequency = f;
        }
    }

    /* access modifiers changed from: private */
    public void SLOGI(String str) {
        if (DEBUG_INFO) {
            GosLog.d(LOG_TAG, str);
        }
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

    private void sdkSwitch() {
        SCENESDK_SWITCH = !new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/tencentsdkswitch/").exists();
    }

    private void sdkLogSwitch() {
        DEBUG_INFO = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/tencentlog/").exists();
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
        this.gfiTargetFps = -1;
        this.player = "-1";
        this.playerBoostCounts = 0;
        this.isFpsBoost = false;
        this.unitTestFlag = 0;
        SCENESDK_SWITCH = true;
        DEBUG_INFO = false;
        this.mFPSMode = Const.GameFPSMode.LOW_FPS;
        this.mResourceApplyType = Const.ApplyType.NONE;
        this.mBPubgGame = false;
        MemoryFile memoryFile = this.mFile;
        if (memoryFile != null) {
            memoryFile.close();
            this.mFile = null;
        }
        HashMap<String, Integer> hashMap = this.targetFpsRecord;
        if (hashMap != null) {
            hashMap.clear();
            this.targetFpsRecord = null;
        }
        TGPACallback tGPACallback = this.mCallbackThread;
        if (tGPACallback != null) {
            tGPACallback.disable();
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
        if (nameForUid.contains(":")) {
            nameForUid = SeActivityManager.getInstance().getAppNameFromPid(AppContext.get(), Binder.getCallingPid());
            GosLog.d(LOG_TAG, "getCallerPkgName(), _pkgName: " + nameForUid);
        }
        SLOGI("getCallerPkgName(), callerUid: " + callingUid + ", callerPkgName: " + nameForUid);
        return nameForUid;
    }

    /* access modifiers changed from: private */
    public IpmCore getIpmCore() {
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
