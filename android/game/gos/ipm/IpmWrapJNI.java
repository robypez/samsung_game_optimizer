package com.samsung.android.game.gos.ipm;

public class IpmWrapJNI {
    public static final native float Display_getRefreshRate(long j, Display display);

    public static final native void Display_onRefreshRateChanged(long j, Display display, float f, float f2);

    public static final native void GameManager_change_ownership(GameManager gameManager, long j, boolean z);

    public static final native void GameManager_director_connect(GameManager gameManager, long j, boolean z, boolean z2);

    public static final native String GameManager_requestWithJson(long j, GameManager gameManager, String str, String str2);

    public static final native String GameManager_requestWithJsonSwigExplicitGameManager(long j, GameManager gameManager, String str, String str2);

    public static final native void IpmCallback_change_ownership(IpmCallback ipmCallback, long j, boolean z);

    public static final native void IpmCallback_director_connect(IpmCallback ipmCallback, long j, boolean z, boolean z2);

    public static final native String IpmCallback_getApplicationAbi(long j, IpmCallback ipmCallback);

    public static final native String IpmCallback_getApplicationName(long j, IpmCallback ipmCallback);

    public static final native int IpmCallback_getApplicationUid(long j, IpmCallback ipmCallback);

    public static final native int IpmCallback_getBigTurboDurationSeconds(long j, IpmCallback ipmCallback);

    public static final native String IpmCallback_getCachePath(long j, IpmCallback ipmCallback);

    public static final native int IpmCallback_getClusterCount(long j, IpmCallback ipmCallback);

    public static final native Object IpmCallback_getContext(long j, IpmCallback ipmCallback);

    public static final native long IpmCallback_getCpuEff(long j, IpmCallback ipmCallback, int i);

    public static final native String IpmCallback_getFilesPath(long j, IpmCallback ipmCallback);

    public static final native String IpmCallback_getGosNamespace(long j, IpmCallback ipmCallback);

    public static final native String IpmCallback_getGosVersion(long j, IpmCallback ipmCallback);

    public static final native long IpmCallback_getGpuEff(long j, IpmCallback ipmCallback);

    public static final native int IpmCallback_getMinLockBoosterDurationSeconds(long j, IpmCallback ipmCallback);

    public static final native boolean IpmCallback_getScenario(long j, IpmCallback ipmCallback);

    public static final native long IpmCallback_getSysFsData(long j, IpmCallback ipmCallback, long j2, VectorString vectorString);

    public static final native boolean IpmCallback_isBigTurboWorking(long j, IpmCallback ipmCallback);

    public static final native boolean IpmCallback_isMinLockBoosterWorking(long j, IpmCallback ipmCallback);

    public static final native boolean IpmCallback_isScenarioDynamicChecked(long j, IpmCallback ipmCallback);

    public static final native void IpmCallback_onStarted(long j, IpmCallback ipmCallback);

    public static final native void IpmCallback_onStopped(long j, IpmCallback ipmCallback);

    public static final native void IpmCallback_onSystemCreated(long j, IpmCallback ipmCallback);

    public static final native void IpmJava_addJson(long j, IpmJava ipmJava, String str, String str2);

    public static final native boolean IpmJava_canRun(long j, IpmJava ipmJava);

    public static final native void IpmJava_disableFrameInterpolation(long j, IpmJava ipmJava);

    public static final native void IpmJava_enableFrameInterpolation(long j, IpmJava ipmJava, float f, float f2, float f3);

    public static final native float IpmJava_getCurrentTargetTemp(long j, IpmJava ipmJava);

    public static final native float IpmJava_getCurrentTemp(long j, IpmJava ipmJava);

    public static final native String IpmJava_getID(long j, IpmJava ipmJava);

    public static final native int IpmJava_getMaxFpsGuess(long j, IpmJava ipmJava);

    public static final native boolean IpmJava_getOnlyCapture(long j, IpmJava ipmJava);

    public static final native int IpmJava_getProfile(long j, IpmJava ipmJava);

    public static final native String IpmJava_getStatistics(long j, IpmJava ipmJava);

    public static final native boolean IpmJava_getSupertrain(long j, IpmJava ipmJava);

    public static final native int IpmJava_getTargetPST(long j, IpmJava ipmJava);

    public static final native String IpmJava_getToTGPA(long j, IpmJava ipmJava);

    public static final native int IpmJava_getVersion(long j, IpmJava ipmJava);

    public static final native void IpmJava_handOverControlToGameSDK(long j, IpmJava ipmJava, String str, boolean z);

    public static final native boolean IpmJava_isClusterControlAvailable(long j, IpmJava ipmJava);

    public static final native boolean IpmJava_isGameSdkEnabledForPkg(long j, IpmJava ipmJava, String str);

    public static final native void IpmJava_pauseActions(long j, IpmJava ipmJava, boolean z);

    public static final native String IpmJava_readDataJSON(long j, IpmJava ipmJava, long j2, VectorParameterRequest vectorParameterRequest, int i, long j3, long j4);

    public static final native String IpmJava_readSessionsJSON__SWIG_0(long j, IpmJava ipmJava);

    public static final native String IpmJava_readSessionsJSON__SWIG_1(long j, IpmJava ipmJava, long j2, VectorInt vectorInt);

    public static final native void IpmJava_releaseMem(long j, IpmJava ipmJava);

    public static final native void IpmJava_resetFixedTargetFps(long j, IpmJava ipmJava);

    public static final native void IpmJava_setAllowMlOff(long j, IpmJava ipmJava, boolean z);

    public static final native void IpmJava_setBusGap(long j, IpmJava ipmJava, int i);

    public static final native void IpmJava_setCPUBottomFreq(long j, IpmJava ipmJava, long j2);

    public static final native void IpmJava_setCpuGap(long j, IpmJava ipmJava, int i);

    public static final native void IpmJava_setCustomProfile(long j, IpmJava ipmJava, float f, float f2, float f3, float f4);

    public static final native void IpmJava_setCustomTfpsFlags(long j, IpmJava ipmJava, int i);

    public static final native void IpmJava_setDynamicPowerMode(long j, IpmJava ipmJava, boolean z);

    public static final native void IpmJava_setEnableBusFreq(long j, IpmJava ipmJava, boolean z);

    public static final native void IpmJava_setEnableToTGPA(long j, IpmJava ipmJava, boolean z);

    public static final native void IpmJava_setFixedTargetFps(long j, IpmJava ipmJava, float f);

    public static final native void IpmJava_setGpuGap(long j, IpmJava ipmJava, int i);

    public static final native void IpmJava_setGpuMinBoost(long j, IpmJava ipmJava, int i);

    public static final native void IpmJava_setHighStabilityMode(long j, IpmJava ipmJava, boolean z);

    public static final native void IpmJava_setInputTempType(long j, IpmJava ipmJava, int i);

    public static final native void IpmJava_setLogLevel(long j, IpmJava ipmJava, int i);

    public static final native void IpmJava_setLowLatencySceneSDK(long j, IpmJava ipmJava, int i, String str, int i2, boolean z);

    public static final native void IpmJava_setMaxFreqs(long j, IpmJava ipmJava, long j2, long j3);

    public static final native void IpmJava_setMinFreqs(long j, IpmJava ipmJava, long j2, long j3);

    public static final native void IpmJava_setOnlyCapture(long j, IpmJava ipmJava, boolean z);

    public static final native void IpmJava_setProfile(long j, IpmJava ipmJava, int i);

    public static final native void IpmJava_setSupertrain(long j, IpmJava ipmJava, boolean z);

    public static final native void IpmJava_setTargetLRPST(long j, IpmJava ipmJava, int i);

    public static final native void IpmJava_setTargetPST(long j, IpmJava ipmJava, int i);

    public static final native void IpmJava_setTargetTemperature(long j, IpmJava ipmJava, int i);

    public static final native void IpmJava_setThermalControl(long j, IpmJava ipmJava, boolean z);

    public static final native void IpmJava_setThreadControl(long j, IpmJava ipmJava, int i);

    public static final native void IpmJava_setUseSsrm(long j, IpmJava ipmJava, boolean z);

    public static final native void IpmJava_start__SWIG_0(long j, IpmJava ipmJava, int i, String str);

    public static final native void IpmJava_start__SWIG_1(long j, IpmJava ipmJava, int i);

    public static final native void IpmJava_stop(long j, IpmJava ipmJava);

    public static final native void MapLongFloat_clear(long j, MapLongFloat mapLongFloat);

    public static final native boolean MapLongFloat_containsKey(long j, MapLongFloat mapLongFloat, long j2);

    public static final native float MapLongFloat_get(long j, MapLongFloat mapLongFloat, long j2);

    public static final native boolean MapLongFloat_isEmpty(long j, MapLongFloat mapLongFloat);

    public static final native void MapLongFloat_put(long j, MapLongFloat mapLongFloat, long j2, float f);

    public static final native void MapLongFloat_remove(long j, MapLongFloat mapLongFloat, long j2);

    public static final native long MapLongFloat_size(long j, MapLongFloat mapLongFloat);

    public static final native int ParameterRequest_aggregation_get(long j, ParameterRequest parameterRequest);

    public static final native void ParameterRequest_aggregation_set(long j, ParameterRequest parameterRequest, int i);

    public static final native String ParameterRequest_parameter_get(long j, ParameterRequest parameterRequest);

    public static final native void ParameterRequest_parameter_set(long j, ParameterRequest parameterRequest, String str);

    public static final native long ParameterRequest_rate_get(long j, ParameterRequest parameterRequest);

    public static final native void ParameterRequest_rate_set(long j, ParameterRequest parameterRequest, long j2);

    public static final native void SurfaceFlinger_change_ownership(SurfaceFlinger surfaceFlinger, long j, boolean z);

    public static final native int SurfaceFlinger_connectPipe(long j, SurfaceFlinger surfaceFlinger, int i, int i2);

    public static final native void SurfaceFlinger_director_connect(SurfaceFlinger surfaceFlinger, long j, boolean z, boolean z2);

    public static final native boolean SurfaceFlinger_dumpPipe(long j, SurfaceFlinger surfaceFlinger, int i, long j2, VectorString vectorString);

    public static final native boolean SurfaceFlinger_enableGfi(long j, SurfaceFlinger surfaceFlinger, float f);

    public static final native int SurfaceFlinger_flipCount(long j, SurfaceFlinger surfaceFlinger);

    public static final native boolean SurfaceFlinger_isUsingGfi(long j, SurfaceFlinger surfaceFlinger, int i);

    public static final native boolean SurfaceFlinger_registerForFrameStats(long j, SurfaceFlinger surfaceFlinger, int i, int i2);

    public static final native void VectorInt_add(long j, VectorInt vectorInt, int i);

    public static final native void VectorInt_clear(long j, VectorInt vectorInt);

    public static final native void VectorInt_ensureCapacity(long j, VectorInt vectorInt, long j2);

    public static final native int VectorInt_get(long j, VectorInt vectorInt, long j2);

    public static final native boolean VectorInt_isEmpty(long j, VectorInt vectorInt);

    public static final native void VectorInt_set(long j, VectorInt vectorInt, int i, int i2);

    public static final native int VectorInt_size(long j, VectorInt vectorInt);

    public static final native void VectorParameterRequest_add(long j, VectorParameterRequest vectorParameterRequest, long j2, ParameterRequest parameterRequest);

    public static final native void VectorParameterRequest_clear(long j, VectorParameterRequest vectorParameterRequest);

    public static final native void VectorParameterRequest_ensureCapacity(long j, VectorParameterRequest vectorParameterRequest, long j2);

    public static final native long VectorParameterRequest_get(long j, VectorParameterRequest vectorParameterRequest, long j2);

    public static final native boolean VectorParameterRequest_isEmpty(long j, VectorParameterRequest vectorParameterRequest);

    public static final native void VectorParameterRequest_set(long j, VectorParameterRequest vectorParameterRequest, int i, long j2, ParameterRequest parameterRequest);

    public static final native int VectorParameterRequest_size(long j, VectorParameterRequest vectorParameterRequest);

    public static final native void VectorString_add(long j, VectorString vectorString, String str);

    public static final native void VectorString_clear(long j, VectorString vectorString);

    public static final native void VectorString_ensureCapacity(long j, VectorString vectorString, long j2);

    public static final native String VectorString_get(long j, VectorString vectorString, long j2);

    public static final native boolean VectorString_isEmpty(long j, VectorString vectorString);

    public static final native void VectorString_set(long j, VectorString vectorString, int i, String str);

    public static final native int VectorString_size(long j, VectorString vectorString);

    public static final native void delete_Display(long j);

    public static final native void delete_GameManager(long j);

    public static final native void delete_IpmCallback(long j);

    public static final native void delete_IpmJava(long j);

    public static final native void delete_MapLongFloat(long j);

    public static final native void delete_ParameterRequest(long j);

    public static final native void delete_SurfaceFlinger(long j);

    public static final native void delete_VectorInt(long j);

    public static final native void delete_VectorParameterRequest(long j);

    public static final native void delete_VectorString(long j);

    public static final native int kMlActionCount_get();

    public static final native int kMlStateCount_get();

    public static final native long new_Display();

    public static final native long new_GameManager();

    public static final native long new_IpmCallback();

    public static final native long new_IpmJava(long j, IpmCallback ipmCallback, long j2, Display display, long j3, SurfaceFlinger surfaceFlinger, long j4, GameManager gameManager);

    public static final native long new_MapLongFloat();

    public static final native long new_ParameterRequest__SWIG_0();

    public static final native long new_ParameterRequest__SWIG_1(String str, int i, long j);

    public static final native long new_SurfaceFlinger();

    public static final native long new_VectorInt();

    public static final native long new_VectorParameterRequest();

    public static final native long new_VectorString();

    private static final native void swig_module_init();

    static {
        if (!AppVariable.isUnitTest()) {
            System.loadLibrary("ipm");
            Log.d("IpmWrapJNI", "Loaded libipm.so");
        }
        swig_module_init();
    }

    public static String SwigDirector_GameManager_requestWithJson(GameManager gameManager, String str, String str2) {
        return gameManager.requestWithJson(str, str2);
    }

    public static Object SwigDirector_IpmCallback_getContext(IpmCallback ipmCallback) {
        return ipmCallback.getContext();
    }

    public static String SwigDirector_IpmCallback_getGosVersion(IpmCallback ipmCallback) {
        return ipmCallback.getGosVersion();
    }

    public static String SwigDirector_IpmCallback_getGosNamespace(IpmCallback ipmCallback) {
        return ipmCallback.getGosNamespace();
    }

    public static String SwigDirector_IpmCallback_getFilesPath(IpmCallback ipmCallback) {
        return ipmCallback.getFilesPath();
    }

    public static String SwigDirector_IpmCallback_getCachePath(IpmCallback ipmCallback) {
        return ipmCallback.getCachePath();
    }

    public static int SwigDirector_IpmCallback_getApplicationUid(IpmCallback ipmCallback) {
        return ipmCallback.getApplicationUid();
    }

    public static String SwigDirector_IpmCallback_getApplicationAbi(IpmCallback ipmCallback) {
        return ipmCallback.getApplicationAbi();
    }

    public static String SwigDirector_IpmCallback_getApplicationName(IpmCallback ipmCallback) {
        return ipmCallback.getApplicationName();
    }

    public static boolean SwigDirector_IpmCallback_isMinLockBoosterWorking(IpmCallback ipmCallback) {
        return ipmCallback.isMinLockBoosterWorking();
    }

    public static int SwigDirector_IpmCallback_getBigTurboDurationSeconds(IpmCallback ipmCallback) {
        return ipmCallback.getBigTurboDurationSeconds();
    }

    public static boolean SwigDirector_IpmCallback_isBigTurboWorking(IpmCallback ipmCallback) {
        return ipmCallback.isBigTurboWorking();
    }

    public static int SwigDirector_IpmCallback_getMinLockBoosterDurationSeconds(IpmCallback ipmCallback) {
        return ipmCallback.getMinLockBoosterDurationSeconds();
    }

    public static long SwigDirector_IpmCallback_getGpuEff(IpmCallback ipmCallback) {
        return MapLongFloat.getCPtr(ipmCallback.getGpuEff());
    }

    public static long SwigDirector_IpmCallback_getCpuEff(IpmCallback ipmCallback, int i) {
        return MapLongFloat.getCPtr(ipmCallback.getCpuEff(i));
    }

    public static int SwigDirector_IpmCallback_getClusterCount(IpmCallback ipmCallback) {
        return ipmCallback.getClusterCount();
    }

    public static long SwigDirector_IpmCallback_getSysFsData(IpmCallback ipmCallback, long j) {
        return VectorString.getCPtr(ipmCallback.getSysFsData(new VectorString(j, false)));
    }

    public static void SwigDirector_IpmCallback_onStarted(IpmCallback ipmCallback) {
        ipmCallback.onStarted();
    }

    public static void SwigDirector_IpmCallback_onStopped(IpmCallback ipmCallback) {
        ipmCallback.onStopped();
    }

    public static void SwigDirector_IpmCallback_onSystemCreated(IpmCallback ipmCallback) {
        ipmCallback.onSystemCreated();
    }

    public static boolean SwigDirector_IpmCallback_getScenario(IpmCallback ipmCallback) {
        return ipmCallback.getScenario();
    }

    public static boolean SwigDirector_IpmCallback_isScenarioDynamicChecked(IpmCallback ipmCallback) {
        return ipmCallback.isScenarioDynamicChecked();
    }

    public static boolean SwigDirector_SurfaceFlinger_dumpPipe(SurfaceFlinger surfaceFlinger, int i, long j) {
        return surfaceFlinger.dumpPipe(i, new VectorString(j, false));
    }

    public static int SwigDirector_SurfaceFlinger_connectPipe(SurfaceFlinger surfaceFlinger, int i, int i2) {
        return surfaceFlinger.connectPipe(i, i2);
    }

    public static int SwigDirector_SurfaceFlinger_flipCount(SurfaceFlinger surfaceFlinger) {
        return surfaceFlinger.flipCount();
    }

    public static boolean SwigDirector_SurfaceFlinger_isUsingGfi(SurfaceFlinger surfaceFlinger, int i) {
        return surfaceFlinger.isUsingGfi(i);
    }

    public static boolean SwigDirector_SurfaceFlinger_enableGfi(SurfaceFlinger surfaceFlinger, float f) {
        return surfaceFlinger.enableGfi(f);
    }

    public static boolean SwigDirector_SurfaceFlinger_registerForFrameStats(SurfaceFlinger surfaceFlinger, int i, int i2) {
        return surfaceFlinger.registerForFrameStats(i, i2);
    }
}
