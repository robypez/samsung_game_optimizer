package com.samsung.android.game.gos.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SpaResponse extends FeatureResponse {
    @SerializedName("enableCpuMinFreqControl")
    @Expose
    private String enableCpuMinFreqControl;
    @SerializedName("enableGpuMinFreqControl")
    @Expose
    private String enableGpuMinFreqControl;
    @SerializedName("ipm_allow_during_charging")
    @Expose
    private String ipmAllowDuringCharging;
    @SerializedName("ipm_allow_ml_off")
    @Expose
    private String ipmAllowMlOff;
    @SerializedName("ipm_allow_shutdown")
    @Expose
    private String ipmAllowShutdown;
    @SerializedName("ipm_bottom_freqs")
    @Expose
    private String ipmBottomFreqs;
    @SerializedName("ipm_cpu_max_freqs")
    @Expose
    private String ipmCpuMaxFreqs;
    @SerializedName("ipm_cpu_min_freqs")
    @Expose
    private String ipmCpuMinFreqs;
    @SerializedName("ipm_custom_profile")
    @Expose
    private String ipmCustomProfile;
    @SerializedName("ipm_gpu_max_freqs")
    @Expose
    private String ipmGpuMaxFreqs;
    @SerializedName("ipm_gpu_min_freqs")
    @Expose
    private String ipmGpuMinFreqs;
    @SerializedName("ipm_gtlm")
    @Expose
    private String ipmGtlm;
    @SerializedName("ipm_high_stability_mode")
    @Expose
    private String ipmHighStabilityMode;
    @SerializedName("ipm_input_temp_type")
    @Expose
    private String ipmInputTempType;
    @SerializedName("ipm_revert_SIOP_if_high")
    @Expose
    private String ipmRevertSIOPIfHigh;
    @SerializedName("ipm_revert_SIOP_if_less")
    @Expose
    private String ipmRevertSIOPIfLess;
    @SerializedName("ipm_revert_SIOP_if_over")
    @Expose
    private String ipmRevertSIOPIfOver;
    @SerializedName("ipm_save_power")
    @Expose
    private String ipmSavePower;
    @SerializedName("ipm_shutdown_freq_missmatch")
    @Expose
    public String ipmShutdownFreqMissmatch;
    @SerializedName("ipm_start_wout_trining")
    @Expose
    private String ipmStartWoutTrining;
    @SerializedName("ipm_target_lrp_default")
    @Expose
    private String ipmTargetLrpDefault;
    @SerializedName("ipm_target_lrp_high")
    @Expose
    private String ipmTargetLrpHigh;
    @SerializedName("ipm_target_pst_default")
    @Expose
    private String ipmTargetPstDefault;
    @SerializedName("ipm_target_pst_high")
    @Expose
    private String ipmTargetPstHigh;
    @SerializedName("ipm_thermal_control")
    @Expose
    private String ipmThermalControl;
    @SerializedName("ipm_trim_gpu_freq_table")
    @Expose
    private String ipmTrimGpuFregTable;
    @SerializedName("mode")
    @Expose
    private Integer mode;
    @SerializedName("siop_modes")
    @Expose
    private List<Boolean> siopModes = null;
    @SerializedName("target_power")
    @Expose
    private Integer targetPower;
    @SerializedName("use_whitelist")
    @Expose
    private Boolean useWhitelist;

    public Integer getMode() {
        return this.mode;
    }

    public Integer getTargetPower() {
        return this.targetPower;
    }

    public String getIpmTargetPstDefault() {
        return this.ipmTargetPstDefault;
    }

    public void setMode(Integer num) {
        this.mode = num;
    }

    public void setTargetPower(Integer num) {
        this.targetPower = num;
    }

    public void setIpmTargetPstDefault(String str) {
        this.ipmTargetPstDefault = str;
    }
}
