package com.samsung.android.game.gos.network.response;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.samsung.android.game.gos.data.model.FeatureFlag;
import com.samsung.android.game.gos.data.model.State;
import com.samsung.android.game.gos.value.Constants;
import java.util.HashMap;
import java.util.Map;

public class PerfPolicyResponse extends BaseResponse {
    private static final String LOG_TAG = PerfPolicyResponse.class.getSimpleName();
    @SerializedName("allow_more_heat")
    @Expose
    public FeatureResponse allowMoreHeat;
    @SerializedName("boost_launch")
    @Expose
    public BoostLaunchResponse boostLaunch;
    @SerializedName("boost_resume")
    @Expose
    public BoostResumeResponse boostResume;
    @SerializedName("boost_touch")
    @Expose
    public FeatureResponse boostTouch;
    @SerializedName("brightness_control")
    @Expose
    public FeatureResponse brightness;
    @SerializedName("clear_bg_process")
    @Expose
    public ClearBGProcessResponse clearBgProcess;
    @Expose
    public EachModeFeatureResponse dfs;
    @SerializedName("external_sdk")
    @Expose
    public FeatureResponse externalSdk;
    @SerializedName("gallery_cmh_stop")
    @Expose
    public FeatureResponse galleryCmhStop;
    @SerializedName("game_sdk")
    @Expose
    public JsonObject gameSdkResponse;
    @SerializedName("gfi")
    @Expose
    public GfiResponse gfiResponse;
    @SerializedName("governor_settings")
    @Expose
    public JsonObject governorSettings;
    @SerializedName("limit_bg_network")
    @Expose
    public FeatureResponse limitBgNetwork;
    @SerializedName("logging_policy")
    @Expose
    public JsonObject loggingPolicy;
    @SerializedName("md_switch_wifi")
    @Expose
    public FeatureResponse mdSwitchWifiBlock;
    @SerializedName("mdnie")
    @Expose
    public FeatureResponse mdnie;
    @SerializedName("network_wifi_qos")
    @Expose
    public NetworkWifiQos networkWifiQos;
    @SerializedName("pkg_name")
    @Expose
    public String pkgName;
    @SerializedName("render_thread_affinity")
    @Expose
    public FeatureResponse renderThreadAffinity;
    @Expose
    public ResolutionResponse resolution;
    @SerializedName("ringlog")
    @Expose
    public RinglogResponse ringlog;
    @SerializedName("selective_target_api")
    @Expose
    public FeatureResponse selectiveTargetResponse;
    @SerializedName("shift_temperature")
    @Expose
    public ShiftTemperature shiftTemperature;
    @SerializedName("game_performance_siop")
    @Expose
    public SiopResponse siopResponse;
    @SerializedName("game_performance_spa")
    @Expose
    public SpaResponse spaResponse;
    @SerializedName("target_group_name")
    @Expose
    public String targetGroupName;
    @SerializedName("tsp")
    @Expose
    public TspResponse tspResponse;
    @SerializedName("volume_control")
    @Expose
    public FeatureResponse volumeControl;
    @SerializedName("vrr")
    @Expose
    public VrrResponse vrrResponse;

    public static class BoostLaunchResponse extends FeatureResponse {
        @SerializedName("big_turbo_duration")
        @Expose
        private String bigTurboDuration;
        @SerializedName("big_turbo_policy")
        @Expose
        private String bigTurboPolicy;
        @SerializedName("bus_freq")
        @Expose
        private String busFreq;
        @SerializedName("cpu_freq")
        @Expose
        private String cpuFreq;
        @SerializedName("duration")
        @Expose
        public String duration;
    }

    public static class BoostResumeResponse extends FeatureResponse {
        @SerializedName("bus_freq")
        @Expose
        private String busFreq;
        @SerializedName("cpu_freq")
        @Expose
        private String cpuFreq;
        @SerializedName("duration")
        @Expose
        public String duration;
    }

    public static class ModeValuesResponse extends BaseResponse {
        @SerializedName("-1")
        @Expose
        public String minus;
        @SerializedName("1")
        @Expose
        public String plus;
        @SerializedName("0")
        @Expose
        public String zero;
    }

    public static class NetworkWifiQos extends FeatureResponse {
        @SerializedName("tid")
        @Expose
        public Integer tid;
    }

    public static class ResolutionResponse extends EachModeFeatureResponse {
        @SerializedName("type")
        @Expose
        public String type;
    }

    public static class RinglogResponse extends FeatureResponse {
        @SerializedName("mode")
        @Expose
        public String mode;
    }

    public static class ShiftTemperature extends FeatureResponse {
        @SerializedName("value")
        @Expose
        public Integer value;
    }

    public static class SiopResponse extends FeatureResponse {
        @SerializedName("cpu")
        @Expose
        public Cpu cpu;
        @SerializedName("default_mode")
        @Expose
        public Integer defaultMode;
        @SerializedName("gpu")
        @Expose
        public Gpu gpu;

        public static class Cpu extends BaseResponse {
            @SerializedName("default_level")
            @Expose
            public Integer defaultLevel;
            @SerializedName("mode_values")
            @Expose
            public ModeValuesResponse modeValues;
        }

        public static class Gpu extends BaseResponse {
            @SerializedName("default_level")
            @Expose
            public Integer defaultLevel;
            @SerializedName("mode_values")
            @Expose
            public ModeValuesResponse modeValues;
        }
    }

    public PerfPolicyResponse() {
    }

    public PerfPolicyResponse(String str) {
        this.pkgName = str;
    }

    public void setPkgName(String str) {
        this.pkgName = str;
    }

    public String getPkgName() {
        return this.pkgName;
    }

    private Map<String, String> getV4StateMap() {
        HashMap hashMap = new HashMap();
        hashMap.put(Constants.V4FeatureFlag.RESOLUTION, getState((FeatureResponse) this.resolution));
        hashMap.put(Constants.V4FeatureFlag.DFS, getState((FeatureResponse) this.dfs));
        hashMap.put(Constants.V4FeatureFlag.RENDER_THREAD_AFFINITY, getState(this.renderThreadAffinity));
        hashMap.put(Constants.V4FeatureFlag.MDNIE, getState(this.mdnie));
        hashMap.put(Constants.V4FeatureFlag.BOOST_TOUCH, getState(this.boostTouch));
        hashMap.put("volume_control", getState(this.volumeControl));
        hashMap.put(Constants.V4FeatureFlag.GALLERY_CMH_STOP, getState(this.galleryCmhStop));
        hashMap.put(Constants.V4FeatureFlag.CLEAR_BG_PROCESS, getState((FeatureResponse) this.clearBgProcess));
        hashMap.put("siop_mode", getState((FeatureResponse) this.siopResponse));
        hashMap.put(Constants.V4FeatureFlag.GOVERNOR_SETTINGS, getState(this.governorSettings));
        hashMap.put("ipm", getState((FeatureResponse) this.spaResponse));
        hashMap.put(Constants.V4FeatureFlag.EXTERNAL_SDK, getState(this.externalSdk));
        hashMap.put(Constants.V4FeatureFlag.RESUME_BOOST, getState((FeatureResponse) this.boostResume));
        hashMap.put(Constants.V4FeatureFlag.LAUNCH_BOOST, getState((FeatureResponse) this.boostLaunch));
        hashMap.put(Constants.V4FeatureFlag.WIFI_QOS, getState((FeatureResponse) this.networkWifiQos));
        hashMap.put("limit_bg_network", getState(this.limitBgNetwork));
        hashMap.put(Constants.V4FeatureFlag.GFI, getState((FeatureResponse) this.gfiResponse));
        hashMap.put(Constants.V4FeatureFlag.VRR, getState((FeatureResponse) this.vrrResponse));
        hashMap.put(Constants.V4FeatureFlag.TSP, getState((FeatureResponse) this.tspResponse));
        hashMap.put(Constants.V4FeatureFlag.MD_SWITCH_WIFI, getState(this.mdSwitchWifiBlock));
        hashMap.put(Constants.V4FeatureFlag.ALLOW_MORE_HEAT, getState(this.allowMoreHeat));
        hashMap.put(Constants.V4FeatureFlag.RINGLOG, getState((FeatureResponse) this.ringlog));
        return hashMap;
    }

    private Map<String, String> getSTPV4StateMap() {
        HashMap hashMap = new HashMap();
        hashMap.put(Constants.V4FeatureFlag.RESOLUTION, getSTPState((FeatureResponse) this.resolution));
        hashMap.put(Constants.V4FeatureFlag.DFS, getSTPState((FeatureResponse) this.dfs));
        hashMap.put(Constants.V4FeatureFlag.RENDER_THREAD_AFFINITY, getSTPState(this.renderThreadAffinity));
        hashMap.put(Constants.V4FeatureFlag.MDNIE, getSTPState(this.mdnie));
        hashMap.put(Constants.V4FeatureFlag.BOOST_TOUCH, getSTPState(this.boostTouch));
        hashMap.put("volume_control", getSTPState(this.volumeControl));
        hashMap.put(Constants.V4FeatureFlag.GALLERY_CMH_STOP, getSTPState(this.galleryCmhStop));
        hashMap.put(Constants.V4FeatureFlag.CLEAR_BG_PROCESS, getSTPState((FeatureResponse) this.clearBgProcess));
        hashMap.put("siop_mode", getSTPState((FeatureResponse) this.siopResponse));
        hashMap.put(Constants.V4FeatureFlag.GOVERNOR_SETTINGS, getSTPState(this.governorSettings));
        hashMap.put("ipm", getSTPState((FeatureResponse) this.spaResponse));
        hashMap.put(Constants.V4FeatureFlag.EXTERNAL_SDK, getSTPState(this.externalSdk));
        hashMap.put(Constants.V4FeatureFlag.RESUME_BOOST, getSTPState((FeatureResponse) this.boostResume));
        hashMap.put(Constants.V4FeatureFlag.LAUNCH_BOOST, getSTPState((FeatureResponse) this.boostLaunch));
        hashMap.put(Constants.V4FeatureFlag.WIFI_QOS, getSTPState((FeatureResponse) this.networkWifiQos));
        hashMap.put("limit_bg_network", getSTPState(this.limitBgNetwork));
        hashMap.put(Constants.V4FeatureFlag.GFI, getSTPState((FeatureResponse) this.gfiResponse));
        hashMap.put(Constants.V4FeatureFlag.VRR, getSTPState((FeatureResponse) this.vrrResponse));
        hashMap.put(Constants.V4FeatureFlag.TSP, getSTPState((FeatureResponse) this.tspResponse));
        hashMap.put(Constants.V4FeatureFlag.MD_SWITCH_WIFI, getSTPState(this.mdSwitchWifiBlock));
        hashMap.put(Constants.V4FeatureFlag.ALLOW_MORE_HEAT, getSTPState(this.allowMoreHeat));
        hashMap.put(Constants.V4FeatureFlag.RINGLOG, getSTPState((FeatureResponse) this.ringlog));
        return hashMap;
    }

    public Map<String, FeatureFlag> getServerFeatureFlag() {
        HashMap hashMap = new HashMap();
        for (Map.Entry next : getV4StateMap().entrySet()) {
            hashMap.put(next.getKey(), new FeatureFlag((String) next.getKey(), this.pkgName, (String) next.getValue()));
        }
        return hashMap;
    }

    public Map<String, FeatureFlag> getSTPServerFeatureFlag() {
        HashMap hashMap = new HashMap();
        for (Map.Entry next : getSTPV4StateMap().entrySet()) {
            hashMap.put(next.getKey(), new FeatureFlag((String) next.getKey(), this.pkgName, (String) next.getValue()));
        }
        return hashMap;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003b, code lost:
        if (r7.contains("inherited") != false) goto L_0x0040;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String getState(com.google.gson.JsonObject r7) {
        /*
            r6 = this;
            java.lang.String r0 = "disabled"
            java.lang.String r1 = "enabled"
            java.lang.String r2 = "forcibly_disabled"
            java.lang.String r3 = "forcibly_enabled"
            java.lang.String r4 = "inherited"
            if (r7 == 0) goto L_0x0040
            java.lang.String r5 = "state"
            com.google.gson.JsonElement r7 = r7.get(r5)
            java.lang.String r7 = java.lang.String.valueOf(r7)
            if (r7 == 0) goto L_0x003e
            boolean r5 = r7.contains(r3)
            if (r5 == 0) goto L_0x0020
            r0 = r3
            goto L_0x0041
        L_0x0020:
            boolean r3 = r7.contains(r2)
            if (r3 == 0) goto L_0x0028
            r0 = r2
            goto L_0x0041
        L_0x0028:
            boolean r2 = r7.contains(r1)
            if (r2 == 0) goto L_0x0030
            r0 = r1
            goto L_0x0041
        L_0x0030:
            boolean r1 = r7.contains(r0)
            if (r1 == 0) goto L_0x0037
            goto L_0x0041
        L_0x0037:
            boolean r0 = r7.contains(r4)
            if (r0 == 0) goto L_0x003e
            goto L_0x0040
        L_0x003e:
            r0 = r7
            goto L_0x0041
        L_0x0040:
            r0 = r4
        L_0x0041:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.network.response.PerfPolicyResponse.getState(com.google.gson.JsonObject):java.lang.String");
    }

    private String getSTPState(JsonObject jsonObject) {
        if (jsonObject == null) {
            return "none";
        }
        String valueOf = String.valueOf(jsonObject.get("state"));
        if (valueOf != null) {
            if (valueOf.contains(State.FORCIBLY_ENABLED)) {
                return State.FORCIBLY_ENABLED;
            }
            if (valueOf.contains(State.FORCIBLY_DISABLED)) {
                return State.FORCIBLY_DISABLED;
            }
            if (valueOf.contains("enabled")) {
                return "enabled";
            }
            if (valueOf.contains(State.DISABLED)) {
                return State.DISABLED;
            }
            if (valueOf.contains("inherited")) {
                return "inherited";
            }
        }
        return valueOf;
    }

    private String getState(FeatureResponse featureResponse) {
        return (featureResponse == null || featureResponse.state == null || !State.isValidV4State(featureResponse.state)) ? "inherited" : featureResponse.state;
    }

    private String getSTPState(FeatureResponse featureResponse) {
        return (featureResponse == null || featureResponse.state == null || !State.isValidV4State(featureResponse.state)) ? "none" : featureResponse.state;
    }
}
