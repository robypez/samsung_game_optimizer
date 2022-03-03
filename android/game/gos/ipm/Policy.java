package com.samsung.android.game.gos.ipm;

import java.util.Arrays;

public class Policy {
    private static final String KEY_DEFAULT_MODE = "default";
    private static final String KEY_NAME_ALLOW_ML_OFF = "ipm_allow_ml_off";
    private static final String KEY_NAME_CPU_BOTTOM_FREQ = "ipm_bottom_freqs";
    private static final String KEY_NAME_CPU_MAX_FREQ = "ipm_cpu_max_freqs";
    private static final String KEY_NAME_CPU_MIN_FREQ = "ipm_cpu_min_freqs";
    private static final String KEY_NAME_CUSTOM_PROFILE = "ipm_custom_profile";
    private static final String KEY_NAME_CUSTOM_TFPS = "ipm_custom_tfps";
    private static final String KEY_NAME_ENABLE_BUS_MINFREQ_CONTROL = "enableBusMinFreqControl";
    private static final String KEY_NAME_ENABLE_CPU_MINFREQ_CONTROL = "enableCpuMinFreqControl";
    private static final String KEY_NAME_ENABLE_GPU_MINFREQ_CONTROL = "enableGpuMinFreqControl";
    private static final String KEY_NAME_FRAME_INTERPOLATION_DECAY_HALF_LIFE = "ipm_frame_interpolation_decay_half_life";
    private static final String KEY_NAME_FRAME_INTERPOLATION_ENABLED = "ipm_frame_interpolation_enabled";
    private static final String KEY_NAME_FRAME_INTERPOLATION_FRAME_RATE_OFFSET = "ipm_frame_interpolation_frame_rate_offset";
    private static final String KEY_NAME_FRAME_INTERPOLATION_TEMPERATURE_OFFSET = "ipm_frame_interpolation_temperature_offset";
    private static final String KEY_NAME_GPU_MAX_FREQ = "ipm_gpu_max_freqs";
    private static final String KEY_NAME_GPU_MIN_BOOST = "ipm_trim_gpu_freq_table";
    private static final String KEY_NAME_GPU_MIN_FREQ = "ipm_gpu_min_freqs";
    private static final String KEY_NAME_HIGH_STABILITY_MODE = "ipm_high_stability_mode";
    private static final String KEY_NAME_INTEL_MODE = "ipm_intel_mode";
    private static final String KEY_NAME_TARGET_LRPST_DEFAULT = "ipm_target_lrp_default";
    private static final String KEY_NAME_TARGET_LRPST_HIGH = "ipm_target_lrp_high";
    private static final String KEY_NAME_TARGET_PST = "ipm_target_pst";
    private static final String KEY_NAME_TARGET_PST_DEFAULT = "ipm_target_pst_default";
    private static final String KEY_NAME_TARGET_PST_HIGH = "ipm_target_pst_high";
    private static final String KEY_NAME_TARGET_TEMP_HARDLIMIT = "ipm_target_hardlimit";
    private static final String KEY_NAME_THERMAL_CONTROL = "ipm_thermal_control";
    private static final String KEY_NAME_THREAD_CONTROL = "thread_control";
    private static final String KEY_SIOP_MODES = "siop_modes";
    private static final String LAUNCH_MODES = "launch_modes";
    private static final String LOG_TAG = "Policy";
    private final int TARGET_LRP_TEMP_IDX = 1;
    private final int TARGET_PST_TEMP_IDX = 1;
    public Boolean allowMlOff = null;
    public Long cpuBottomFreq = null;
    public Float[] customProfileValues = null;
    public Integer customTfpsFlags = null;
    public int defaultMode = -1;
    public Boolean enableBusMinFreqControl = null;
    public Boolean enableCpuMinFreqControl = null;
    public Boolean enableGpuMinFreqControl = null;
    public Float frameInterpolationDecayHalfLife = null;
    public Boolean frameInterpolationEnabled = null;
    public Float frameInterpolationFrameRateOffset = null;
    public Float frameInterpolationTemperatureOffset = null;
    public Integer gpuMinBoost = null;
    public Boolean highStabilityMode = null;
    public String intelConfig = null;
    public Integer intelMode = null;
    public Long maxFreqCpu = null;
    public Long maxFreqGpu = null;
    public Long minFreqCpu = null;
    public Long minFreqGpu = null;
    public boolean[] siopMode = {true, true, true};
    public Integer[] targetLrpstMode = {null, null, null, null, null, null};
    public Integer targetPstDefault = null;
    public Integer[] targetPstMode = {null, null, null, null, null, null};
    public Integer targetTempHardLimit = null;
    public Boolean thermalControl = null;
    public Integer threadControl = null;

    /* JADX WARNING: Can't wrap try/catch for region: R(5:41|42|43|44|(3:46|(1:48)(1:49)|50)) */
    /* JADX WARNING: Code restructure failed: missing block: B:141:0x02ca, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:142:0x02cb, code lost:
        r3 = r17;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x0118 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:55:0x013d */
    /* JADX WARNING: Missing exception handler attribute for start block: B:88:0x01a7 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:96:0x01c4 */
    /* JADX WARNING: Removed duplicated region for block: B:130:0x027c A[Catch:{ Exception -> 0x02c8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x0290 A[Catch:{ Exception -> 0x02c8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:136:0x02a5 A[Catch:{ Exception -> 0x02c8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:139:0x02ba A[Catch:{ Exception -> 0x02c8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:147:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x011c A[Catch:{ Exception -> 0x02ca }] */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x0141 A[Catch:{ Exception -> 0x02ca }] */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x01ab A[Catch:{ Exception -> 0x02ca }] */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x01c8 A[Catch:{ Exception -> 0x02ca }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void parse(org.json.JSONObject r27) {
        /*
            r26 = this;
            r1 = r26
            r0 = r27
            java.lang.String r2 = "ipm_trim_gpu_freq_table"
            java.lang.String r3 = "enableBusMinFreqControl"
            java.lang.String r4 = "enableGpuMinFreqControl"
            java.lang.String r5 = "enableCpuMinFreqControl"
            java.lang.String r6 = "thread_control"
            java.lang.String r7 = "ipm_gpu_max_freqs"
            java.lang.String r8 = "ipm_cpu_max_freqs"
            java.lang.String r9 = "ipm_gpu_min_freqs"
            java.lang.String r10 = "ipm_cpu_min_freqs"
            java.lang.String r11 = "ipm_bottom_freqs"
            java.lang.String r12 = "ipm_target_lrp_high"
            java.lang.String r13 = "ipm_target_pst_high"
            java.lang.String r14 = "ipm_target_pst"
            java.lang.String r15 = "ipm_target_hardlimit"
            r16 = r2
            java.lang.String r2 = "Policy"
            r17 = r2
            java.lang.String r2 = "siop_modes"
            r18 = r3
            java.lang.String r3 = "ipm_intel_mode"
            r19 = r3
            java.lang.String r3 = "ipm_allow_ml_off"
            r20 = r3
            java.lang.String r3 = "ipm_high_stability_mode"
            r21 = r4
            java.lang.String r4 = "ipm_thermal_control"
            boolean r22 = r0.has(r2)     // Catch:{ Exception -> 0x02ca }
            r23 = r5
            if (r22 == 0) goto L_0x0057
            org.json.JSONArray r2 = r0.getJSONArray(r2)     // Catch:{ Exception -> 0x02ca }
            r24 = r3
            r5 = 0
        L_0x0047:
            boolean[] r3 = r1.siopMode     // Catch:{ Exception -> 0x02ca }
            int r3 = r3.length     // Catch:{ Exception -> 0x02ca }
            if (r5 >= r3) goto L_0x0059
            boolean[] r3 = r1.siopMode     // Catch:{ Exception -> 0x02ca }
            boolean r25 = r2.getBoolean(r5)     // Catch:{ Exception -> 0x02ca }
            r3[r5] = r25     // Catch:{ Exception -> 0x02ca }
            int r5 = r5 + 1
            goto L_0x0047
        L_0x0057:
            r24 = r3
        L_0x0059:
            java.lang.String r2 = "default"
            int r3 = r1.defaultMode     // Catch:{ Exception -> 0x02ca }
            int r2 = r0.optInt(r2, r3)     // Catch:{ Exception -> 0x02ca }
            r1.defaultMode = r2     // Catch:{ Exception -> 0x02ca }
            boolean r2 = r0.has(r15)     // Catch:{ Exception -> 0x02ca }
            if (r2 == 0) goto L_0x0073
            int r2 = r0.getInt(r15)     // Catch:{ Exception -> 0x02ca }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ Exception -> 0x02ca }
            r1.targetTempHardLimit = r2     // Catch:{ Exception -> 0x02ca }
        L_0x0073:
            boolean r2 = r0.has(r14)     // Catch:{ Exception -> 0x02ca }
            if (r2 == 0) goto L_0x0083
            int r2 = r0.getInt(r14)     // Catch:{ Exception -> 0x02ca }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ Exception -> 0x02ca }
            r1.targetPstDefault = r2     // Catch:{ Exception -> 0x02ca }
        L_0x0083:
            boolean r2 = r0.has(r13)     // Catch:{ Exception -> 0x02ca }
            r3 = 1
            if (r2 == 0) goto L_0x0096
            java.lang.Integer[] r2 = r1.targetPstMode     // Catch:{ Exception -> 0x02ca }
            int r5 = r0.getInt(r13)     // Catch:{ Exception -> 0x02ca }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ Exception -> 0x02ca }
            r2[r3] = r5     // Catch:{ Exception -> 0x02ca }
        L_0x0096:
            boolean r2 = r0.has(r12)     // Catch:{ Exception -> 0x02ca }
            if (r2 == 0) goto L_0x00a8
            java.lang.Integer[] r2 = r1.targetLrpstMode     // Catch:{ Exception -> 0x02ca }
            int r5 = r0.getInt(r12)     // Catch:{ Exception -> 0x02ca }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ Exception -> 0x02ca }
            r2[r3] = r5     // Catch:{ Exception -> 0x02ca }
        L_0x00a8:
            boolean r2 = r0.has(r11)     // Catch:{ Exception -> 0x02ca }
            if (r2 == 0) goto L_0x00b8
            long r11 = r0.getLong(r11)     // Catch:{ Exception -> 0x02ca }
            java.lang.Long r2 = java.lang.Long.valueOf(r11)     // Catch:{ Exception -> 0x02ca }
            r1.cpuBottomFreq = r2     // Catch:{ Exception -> 0x02ca }
        L_0x00b8:
            boolean r2 = r0.has(r10)     // Catch:{ Exception -> 0x02ca }
            if (r2 == 0) goto L_0x00c8
            long r10 = r0.getLong(r10)     // Catch:{ Exception -> 0x02ca }
            java.lang.Long r2 = java.lang.Long.valueOf(r10)     // Catch:{ Exception -> 0x02ca }
            r1.minFreqCpu = r2     // Catch:{ Exception -> 0x02ca }
        L_0x00c8:
            boolean r2 = r0.has(r9)     // Catch:{ Exception -> 0x02ca }
            if (r2 == 0) goto L_0x00d8
            long r9 = r0.getLong(r9)     // Catch:{ Exception -> 0x02ca }
            java.lang.Long r2 = java.lang.Long.valueOf(r9)     // Catch:{ Exception -> 0x02ca }
            r1.minFreqGpu = r2     // Catch:{ Exception -> 0x02ca }
        L_0x00d8:
            boolean r2 = r0.has(r8)     // Catch:{ Exception -> 0x02ca }
            if (r2 == 0) goto L_0x00e8
            long r8 = r0.getLong(r8)     // Catch:{ Exception -> 0x02ca }
            java.lang.Long r2 = java.lang.Long.valueOf(r8)     // Catch:{ Exception -> 0x02ca }
            r1.maxFreqCpu = r2     // Catch:{ Exception -> 0x02ca }
        L_0x00e8:
            boolean r2 = r0.has(r7)     // Catch:{ Exception -> 0x02ca }
            if (r2 == 0) goto L_0x00f8
            long r7 = r0.getLong(r7)     // Catch:{ Exception -> 0x02ca }
            java.lang.Long r2 = java.lang.Long.valueOf(r7)     // Catch:{ Exception -> 0x02ca }
            r1.maxFreqGpu = r2     // Catch:{ Exception -> 0x02ca }
        L_0x00f8:
            boolean r2 = r0.has(r6)     // Catch:{ Exception -> 0x02ca }
            if (r2 == 0) goto L_0x0108
            int r2 = r0.getInt(r6)     // Catch:{ Exception -> 0x02ca }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ Exception -> 0x02ca }
            r1.threadControl = r2     // Catch:{ Exception -> 0x02ca }
        L_0x0108:
            boolean r2 = r0.has(r4)     // Catch:{ Exception -> 0x02ca }
            if (r2 == 0) goto L_0x012b
            boolean r2 = r0.getBoolean(r4)     // Catch:{ Exception -> 0x0118 }
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r2)     // Catch:{ Exception -> 0x0118 }
            r1.thermalControl = r2     // Catch:{ Exception -> 0x0118 }
        L_0x0118:
            java.lang.Boolean r2 = r1.thermalControl     // Catch:{ Exception -> 0x02ca }
            if (r2 != 0) goto L_0x012b
            int r2 = r0.getInt(r4)     // Catch:{ Exception -> 0x02ca }
            if (r2 != r3) goto L_0x0124
            r2 = r3
            goto L_0x0125
        L_0x0124:
            r2 = 0
        L_0x0125:
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r2)     // Catch:{ Exception -> 0x02ca }
            r1.thermalControl = r2     // Catch:{ Exception -> 0x02ca }
        L_0x012b:
            r2 = r24
            boolean r4 = r0.has(r2)     // Catch:{ Exception -> 0x02ca }
            if (r4 == 0) goto L_0x0150
            boolean r4 = r0.getBoolean(r2)     // Catch:{ Exception -> 0x013d }
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r4)     // Catch:{ Exception -> 0x013d }
            r1.highStabilityMode = r4     // Catch:{ Exception -> 0x013d }
        L_0x013d:
            java.lang.Boolean r4 = r1.highStabilityMode     // Catch:{ Exception -> 0x02ca }
            if (r4 != 0) goto L_0x0150
            int r2 = r0.getInt(r2)     // Catch:{ Exception -> 0x02ca }
            if (r2 != r3) goto L_0x0149
            r2 = r3
            goto L_0x014a
        L_0x0149:
            r2 = 0
        L_0x014a:
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r2)     // Catch:{ Exception -> 0x02ca }
            r1.highStabilityMode = r2     // Catch:{ Exception -> 0x02ca }
        L_0x0150:
            r2 = r23
            boolean r4 = r0.has(r2)     // Catch:{ Exception -> 0x02ca }
            if (r4 == 0) goto L_0x0167
            int r2 = r0.getInt(r2)     // Catch:{ Exception -> 0x02ca }
            if (r2 != r3) goto L_0x0160
            r2 = r3
            goto L_0x0161
        L_0x0160:
            r2 = 0
        L_0x0161:
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r2)     // Catch:{ Exception -> 0x02ca }
            r1.enableCpuMinFreqControl = r2     // Catch:{ Exception -> 0x02ca }
        L_0x0167:
            r2 = r21
            boolean r4 = r0.has(r2)     // Catch:{ Exception -> 0x02ca }
            if (r4 == 0) goto L_0x017e
            int r2 = r0.getInt(r2)     // Catch:{ Exception -> 0x02ca }
            if (r2 != r3) goto L_0x0177
            r2 = r3
            goto L_0x0178
        L_0x0177:
            r2 = 0
        L_0x0178:
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r2)     // Catch:{ Exception -> 0x02ca }
            r1.enableGpuMinFreqControl = r2     // Catch:{ Exception -> 0x02ca }
        L_0x017e:
            r2 = r18
            boolean r4 = r0.has(r2)     // Catch:{ Exception -> 0x02ca }
            if (r4 == 0) goto L_0x0195
            int r2 = r0.getInt(r2)     // Catch:{ Exception -> 0x02ca }
            if (r2 != r3) goto L_0x018e
            r2 = r3
            goto L_0x018f
        L_0x018e:
            r2 = 0
        L_0x018f:
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r2)     // Catch:{ Exception -> 0x02ca }
            r1.enableBusMinFreqControl = r2     // Catch:{ Exception -> 0x02ca }
        L_0x0195:
            r2 = r16
            boolean r4 = r0.has(r2)     // Catch:{ Exception -> 0x02ca }
            if (r4 == 0) goto L_0x01b2
            int r2 = r0.getInt(r2)     // Catch:{ Exception -> 0x01a7 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ Exception -> 0x01a7 }
            r1.gpuMinBoost = r2     // Catch:{ Exception -> 0x01a7 }
        L_0x01a7:
            java.lang.Integer r2 = r1.gpuMinBoost     // Catch:{ Exception -> 0x02ca }
            if (r2 != 0) goto L_0x01b2
            r2 = 0
            java.lang.Integer r4 = java.lang.Integer.valueOf(r2)     // Catch:{ Exception -> 0x02ca }
            r1.gpuMinBoost = r4     // Catch:{ Exception -> 0x02ca }
        L_0x01b2:
            r2 = r20
            boolean r4 = r0.has(r2)     // Catch:{ Exception -> 0x02ca }
            if (r4 == 0) goto L_0x01d6
            boolean r4 = r0.getBoolean(r2)     // Catch:{ Exception -> 0x01c4 }
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r4)     // Catch:{ Exception -> 0x01c4 }
            r1.allowMlOff = r4     // Catch:{ Exception -> 0x01c4 }
        L_0x01c4:
            java.lang.Boolean r4 = r1.allowMlOff     // Catch:{ Exception -> 0x02ca }
            if (r4 != 0) goto L_0x01d6
            int r2 = r0.getInt(r2)     // Catch:{ Exception -> 0x02ca }
            if (r2 != r3) goto L_0x01cf
            goto L_0x01d0
        L_0x01cf:
            r3 = 0
        L_0x01d0:
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r3)     // Catch:{ Exception -> 0x02ca }
            r1.allowMlOff = r2     // Catch:{ Exception -> 0x02ca }
        L_0x01d6:
            java.lang.String r2 = "ipm_custom_profile"
            boolean r2 = r0.has(r2)     // Catch:{ Exception -> 0x02ca }
            if (r2 == 0) goto L_0x0206
            java.lang.String r2 = "ipm_custom_profile"
            org.json.JSONArray r2 = r0.getJSONArray(r2)     // Catch:{ Exception -> 0x02ca }
            int r3 = r2.length()     // Catch:{ Exception -> 0x02ca }
            r4 = 4
            if (r3 != r4) goto L_0x0206
            r3 = 4
            java.lang.Float[] r3 = new java.lang.Float[r3]     // Catch:{ Exception -> 0x02ca }
            r1.customProfileValues = r3     // Catch:{ Exception -> 0x02ca }
            r3 = 0
        L_0x01f1:
            java.lang.Float[] r4 = r1.customProfileValues     // Catch:{ Exception -> 0x02ca }
            int r4 = r4.length     // Catch:{ Exception -> 0x02ca }
            if (r3 >= r4) goto L_0x0206
            java.lang.Float[] r4 = r1.customProfileValues     // Catch:{ Exception -> 0x02ca }
            double r5 = r2.getDouble(r3)     // Catch:{ Exception -> 0x02ca }
            float r5 = (float) r5     // Catch:{ Exception -> 0x02ca }
            java.lang.Float r5 = java.lang.Float.valueOf(r5)     // Catch:{ Exception -> 0x02ca }
            r4[r3] = r5     // Catch:{ Exception -> 0x02ca }
            int r3 = r3 + 1
            goto L_0x01f1
        L_0x0206:
            java.lang.String r2 = "ipm_custom_tfps"
            boolean r2 = r0.has(r2)     // Catch:{ Exception -> 0x02ca }
            if (r2 == 0) goto L_0x021a
            java.lang.String r2 = "ipm_custom_tfps"
            int r2 = r0.getInt(r2)     // Catch:{ Exception -> 0x02ca }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ Exception -> 0x02ca }
            r1.customTfpsFlags = r2     // Catch:{ Exception -> 0x02ca }
        L_0x021a:
            r2 = r19
            boolean r3 = r0.has(r2)     // Catch:{ Exception -> 0x02ca }
            if (r3 == 0) goto L_0x0272
            org.json.JSONObject r3 = r0.optJSONObject(r2)     // Catch:{ Exception -> 0x02ca }
            if (r3 != 0) goto L_0x0236
            int r2 = r0.getInt(r2)     // Catch:{ Exception -> 0x02ca }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ Exception -> 0x02ca }
            r1.intelMode = r2     // Catch:{ Exception -> 0x02ca }
            r2 = 0
            r1.intelConfig = r2     // Catch:{ Exception -> 0x02ca }
            goto L_0x0272
        L_0x0236:
            java.lang.String r2 = "mode"
            r4 = 0
            int r2 = r3.optInt(r2, r4)     // Catch:{ Exception -> 0x02ca }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ Exception -> 0x02ca }
            r1.intelMode = r2     // Catch:{ Exception -> 0x02ca }
            java.lang.String r2 = "config"
            org.json.JSONObject r2 = r3.optJSONObject(r2)     // Catch:{ Exception -> 0x02ca }
            if (r2 == 0) goto L_0x0251
            java.lang.String r3 = r2.toString()     // Catch:{ Exception -> 0x02ca }
            r1.intelConfig = r3     // Catch:{ Exception -> 0x02ca }
        L_0x0251:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02ca }
            r3.<init>()     // Catch:{ Exception -> 0x02ca }
            java.lang.String r4 = "Intel mode = "
            r3.append(r4)     // Catch:{ Exception -> 0x02ca }
            java.lang.Integer r4 = r1.intelMode     // Catch:{ Exception -> 0x02ca }
            r3.append(r4)     // Catch:{ Exception -> 0x02ca }
            java.lang.String r4 = " cfg = "
            r3.append(r4)     // Catch:{ Exception -> 0x02ca }
            r3.append(r2)     // Catch:{ Exception -> 0x02ca }
            java.lang.String r2 = r3.toString()     // Catch:{ Exception -> 0x02ca }
            r3 = r17
            com.samsung.android.game.gos.ipm.Log.d(r3, r2)     // Catch:{ Exception -> 0x02c8 }
            goto L_0x0274
        L_0x0272:
            r3 = r17
        L_0x0274:
            java.lang.String r2 = "ipm_frame_interpolation_enabled"
            boolean r2 = r0.has(r2)     // Catch:{ Exception -> 0x02c8 }
            if (r2 == 0) goto L_0x0288
            java.lang.String r2 = "ipm_frame_interpolation_enabled"
            boolean r2 = r0.getBoolean(r2)     // Catch:{ Exception -> 0x02c8 }
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r2)     // Catch:{ Exception -> 0x02c8 }
            r1.frameInterpolationEnabled = r2     // Catch:{ Exception -> 0x02c8 }
        L_0x0288:
            java.lang.String r2 = "ipm_frame_interpolation_temperature_offset"
            boolean r2 = r0.has(r2)     // Catch:{ Exception -> 0x02c8 }
            if (r2 == 0) goto L_0x029d
            java.lang.String r2 = "ipm_frame_interpolation_temperature_offset"
            double r4 = r0.getDouble(r2)     // Catch:{ Exception -> 0x02c8 }
            float r2 = (float) r4     // Catch:{ Exception -> 0x02c8 }
            java.lang.Float r2 = java.lang.Float.valueOf(r2)     // Catch:{ Exception -> 0x02c8 }
            r1.frameInterpolationTemperatureOffset = r2     // Catch:{ Exception -> 0x02c8 }
        L_0x029d:
            java.lang.String r2 = "ipm_frame_interpolation_frame_rate_offset"
            boolean r2 = r0.has(r2)     // Catch:{ Exception -> 0x02c8 }
            if (r2 == 0) goto L_0x02b2
            java.lang.String r2 = "ipm_frame_interpolation_frame_rate_offset"
            double r4 = r0.getDouble(r2)     // Catch:{ Exception -> 0x02c8 }
            float r2 = (float) r4     // Catch:{ Exception -> 0x02c8 }
            java.lang.Float r2 = java.lang.Float.valueOf(r2)     // Catch:{ Exception -> 0x02c8 }
            r1.frameInterpolationFrameRateOffset = r2     // Catch:{ Exception -> 0x02c8 }
        L_0x02b2:
            java.lang.String r2 = "ipm_frame_interpolation_decay_half_life"
            boolean r2 = r0.has(r2)     // Catch:{ Exception -> 0x02c8 }
            if (r2 == 0) goto L_0x02e1
            java.lang.String r2 = "ipm_frame_interpolation_decay_half_life"
            double r4 = r0.getDouble(r2)     // Catch:{ Exception -> 0x02c8 }
            float r0 = (float) r4     // Catch:{ Exception -> 0x02c8 }
            java.lang.Float r0 = java.lang.Float.valueOf(r0)     // Catch:{ Exception -> 0x02c8 }
            r1.frameInterpolationDecayHalfLife = r0     // Catch:{ Exception -> 0x02c8 }
            goto L_0x02e1
        L_0x02c8:
            r0 = move-exception
            goto L_0x02cd
        L_0x02ca:
            r0 = move-exception
            r3 = r17
        L_0x02cd:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r4 = "Exception parsing JSONPolicy "
            r2.append(r4)
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            com.samsung.android.game.gos.ipm.Log.e(r3, r0)
        L_0x02e1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.ipm.Policy.parse(org.json.JSONObject):void");
    }

    public Policy apply(GlobalSettings globalSettings, Ipm ipm) {
        Policy policy = new Policy();
        if (this.targetTempHardLimit != null) {
            Log.d(LOG_TAG, "start(), targetPstHardLimit: " + this.targetTempHardLimit);
            policy.targetTempHardLimit = Integer.valueOf(ipm.getTargetTemperatureLimit());
            ipm.changeTempHardLimit(this.targetTempHardLimit.intValue());
        }
        if (this.cpuBottomFreq != null) {
            Log.d(LOG_TAG, "start(), cpuBottomFreq: " + this.cpuBottomFreq);
            policy.cpuBottomFreq = 0L;
            globalSettings.setCpuBottomFrequency(this.cpuBottomFreq.longValue());
        }
        if (this.enableCpuMinFreqControl != null) {
            Log.d(LOG_TAG, "start(), enableCpuMinFreqControl: " + this.enableCpuMinFreqControl);
            policy.enableCpuMinFreqControl = Boolean.valueOf(ipm.getEnableCpuMinFreqControl());
            ipm.setEnableCpuMinFreqControl(this.enableCpuMinFreqControl.booleanValue());
        }
        if (this.enableGpuMinFreqControl != null) {
            Log.d(LOG_TAG, "start(), enableGpuMinFreqControl: " + this.enableGpuMinFreqControl);
            policy.enableGpuMinFreqControl = Boolean.valueOf(ipm.getEnableGpuMinFreqControl());
            ipm.setEnableGpuMinFreqControl(this.enableGpuMinFreqControl.booleanValue());
        }
        if (this.enableBusMinFreqControl != null) {
            Log.d(LOG_TAG, "start(), enableBusMinFreqControl: " + this.enableBusMinFreqControl);
            policy.enableBusMinFreqControl = Boolean.valueOf(ipm.getEnableBusMinFreqControl());
            ipm.setEnableBusMinFreqControl(this.enableBusMinFreqControl.booleanValue());
        }
        if (this.gpuMinBoost != null) {
            Log.d(LOG_TAG, "start(), gpuMinBoost: " + this.gpuMinBoost);
            policy.gpuMinBoost = 0;
            ipm.setGpuMinBoost(this.gpuMinBoost.intValue());
        }
        if (!(this.minFreqCpu == null && this.minFreqGpu == null)) {
            Long l = this.minFreqCpu;
            long longValue = l != null ? l.longValue() : 0;
            Long l2 = this.minFreqGpu;
            long longValue2 = l2 != null ? l2.longValue() : 0;
            Log.d(LOG_TAG, "start(), minFreqGpu/Cpu: " + longValue2 + " " + longValue);
            policy.minFreqGpu = 0L;
            policy.minFreqCpu = 0L;
            ipm.setMinFreqs(longValue2, longValue);
        }
        if (!(this.maxFreqCpu == null && this.maxFreqGpu == null)) {
            Long l3 = this.maxFreqCpu;
            long longValue3 = l3 != null ? l3.longValue() : 0;
            Long l4 = this.maxFreqGpu;
            long longValue4 = l4 != null ? l4.longValue() : 0;
            Log.d(LOG_TAG, "start(), maxFreqGpu/Cpu: " + longValue4 + " " + longValue3);
            policy.maxFreqGpu = 0L;
            policy.maxFreqCpu = 0L;
            ipm.setMaxFreqs(longValue4, longValue3);
        }
        if (this.targetPstMode[1] != null) {
            Log.d(LOG_TAG, "start(), ipmTargetPst: " + this.targetPstMode[1]);
            policy.targetPstMode[0] = Integer.valueOf(globalSettings.getTargetTemperature());
            globalSettings.setTargetTemperature(this.targetPstMode[1].intValue());
        }
        if (this.targetLrpstMode[1] != null) {
            Log.d(LOG_TAG, "start(), ipmTargetLRPst: " + this.targetLrpstMode[1]);
            ipm.setLRPST(this.targetLrpstMode[1].intValue());
        }
        Float[] fArr = this.customProfileValues;
        if (fArr != null && fArr.length == 4) {
            Log.d(LOG_TAG, "start(), useCustomProfile: " + Arrays.toString(this.customProfileValues));
            ipm.setCustomProfile(this.customProfileValues[0].floatValue(), this.customProfileValues[1].floatValue(), this.customProfileValues[2].floatValue(), this.customProfileValues[3].floatValue());
            globalSettings.setMode(Profile.CUSTOM.toInt());
        }
        if (this.threadControl != null) {
            Log.d(LOG_TAG, "start(), threadControl: " + this.threadControl);
            policy.threadControl = 0;
            ipm.setThreadControl(this.threadControl.intValue());
        }
        if (this.thermalControl != null) {
            Log.d(LOG_TAG, "start(), thermalControl: " + this.thermalControl);
            policy.thermalControl = true;
            ipm.setThermalControl(this.thermalControl.booleanValue());
        }
        if (this.highStabilityMode != null) {
            Log.d(LOG_TAG, "start(), highStabilityMode: " + this.highStabilityMode);
            policy.highStabilityMode = false;
            ipm.setDynamicDecisions(this.highStabilityMode.booleanValue());
        }
        if (this.allowMlOff != null) {
            Log.d(LOG_TAG, "start(), allowMlOff: " + this.allowMlOff);
            policy.allowMlOff = false;
            ipm.setAllowMlOff(this.allowMlOff.booleanValue());
        }
        if (this.customTfpsFlags != null) {
            Log.d(LOG_TAG, "start(), customTfpsFlags: " + this.customTfpsFlags);
            policy.customTfpsFlags = Integer.valueOf(ipm.getCustomTfpsFlags());
            ipm.setCustomTfpsFlags(this.customTfpsFlags.intValue());
        }
        if (this.intelMode != null) {
            Log.d(LOG_TAG, "start(), intelMode: " + this.intelMode + " cfg = " + this.intelConfig);
            policy.intelMode = Integer.valueOf(ipm.getIntelMode().toInt());
            policy.intelConfig = ipm.getIntelConfig();
            ipm.setIntelMode(IntelMode.fromInt(this.intelMode.intValue()), this.intelConfig);
        }
        if (this.frameInterpolationEnabled != null) {
            Log.d(LOG_TAG, "start(), frameInterpolationEnabled: " + this.frameInterpolationEnabled);
            policy.frameInterpolationEnabled = Boolean.valueOf(ipm.isFrameInterpolationEnabled());
            ipm.setFrameInterpolationEnabled(this.frameInterpolationEnabled.booleanValue());
        }
        if (this.frameInterpolationTemperatureOffset != null) {
            Log.d(LOG_TAG, "start(), frameInterpolationTemperatureOffset: " + this.frameInterpolationTemperatureOffset);
            policy.frameInterpolationTemperatureOffset = Float.valueOf(ipm.getFrameInterpolationTemperatureOffset());
            ipm.setFrameInterpolationTemperatureOffset(this.frameInterpolationTemperatureOffset.floatValue());
        }
        if (this.frameInterpolationFrameRateOffset != null) {
            Log.d(LOG_TAG, "start(), frameInterpolationFrameRateOffset: " + this.frameInterpolationFrameRateOffset);
            policy.frameInterpolationFrameRateOffset = Float.valueOf(ipm.getFrameInterpolationFrameRateOffset());
            ipm.setFrameInterpolationFrameRateOffset(this.frameInterpolationFrameRateOffset.floatValue());
        }
        if (this.frameInterpolationDecayHalfLife != null) {
            Log.d(LOG_TAG, "start(), frameInterpolationDecayHalfLife: " + this.frameInterpolationDecayHalfLife);
            policy.frameInterpolationDecayHalfLife = Float.valueOf(ipm.getFrameInterpolationDecayHalfLife());
            ipm.setFrameInterpolationDecayHalfLife(this.frameInterpolationDecayHalfLife.floatValue());
        }
        return policy;
    }

    public void restore(GlobalSettings globalSettings, Ipm ipm) {
        Log.v(LOG_TAG, "restore");
        Integer num = this.targetTempHardLimit;
        if (num != null) {
            ipm.changeTempHardLimit(num.intValue());
        }
        Long l = this.cpuBottomFreq;
        if (l != null) {
            globalSettings.setCpuBottomFrequency(l.longValue());
        }
        Boolean bool = this.enableCpuMinFreqControl;
        if (bool != null) {
            ipm.setEnableCpuMinFreqControl(bool.booleanValue());
            if (this.enableCpuMinFreqControl.booleanValue()) {
                ipm.setCpuGap(-2);
            } else {
                ipm.setCpuGap(-1);
            }
        }
        Boolean bool2 = this.enableGpuMinFreqControl;
        if (bool2 != null) {
            ipm.setEnableGpuMinFreqControl(bool2.booleanValue());
            if (this.enableGpuMinFreqControl.booleanValue()) {
                ipm.setGpuGap(-2);
            } else {
                ipm.setGpuGap(-1);
            }
        }
        Boolean bool3 = this.enableBusMinFreqControl;
        if (bool3 != null) {
            ipm.setEnableBusMinFreqControl(bool3.booleanValue());
            if (this.enableBusMinFreqControl.booleanValue()) {
                ipm.setEnableBusMinFreqControl(true);
            } else {
                ipm.setEnableBusMinFreqControl(false);
            }
        }
        Integer num2 = this.gpuMinBoost;
        if (num2 != null) {
            ipm.setGpuMinBoost(num2.intValue());
        }
        long j = 0;
        if (!(this.minFreqCpu == null && this.minFreqGpu == null)) {
            Long l2 = this.minFreqCpu;
            long longValue = l2 != null ? l2.longValue() : 0;
            Long l3 = this.minFreqGpu;
            ipm.setMinFreqs(l3 != null ? l3.longValue() : 0, longValue);
        }
        if (!(this.maxFreqCpu == null && this.maxFreqGpu == null)) {
            Long l4 = this.maxFreqCpu;
            long longValue2 = l4 != null ? l4.longValue() : 0;
            Long l5 = this.maxFreqGpu;
            if (l5 != null) {
                j = l5.longValue();
            }
            ipm.setMaxFreqs(j, longValue2);
        }
        Integer[] numArr = this.targetPstMode;
        if (!(numArr == null || numArr.length <= 0 || numArr[0] == null)) {
            globalSettings.setTargetTemperature(numArr[0].intValue());
        }
        Integer[] numArr2 = this.targetLrpstMode;
        if (!(numArr2 == null || numArr2.length <= 0 || numArr2[0] == null)) {
            ipm.setTargetLRPST(numArr2[0].intValue());
        }
        Integer num3 = this.threadControl;
        if (num3 != null) {
            ipm.setThreadControl(num3.intValue());
        }
        Boolean bool4 = this.thermalControl;
        if (bool4 != null) {
            ipm.setThermalControl(bool4.booleanValue());
        }
        Boolean bool5 = this.highStabilityMode;
        if (bool5 != null) {
            ipm.setDynamicDecisions(bool5.booleanValue());
        }
        Boolean bool6 = this.allowMlOff;
        if (bool6 != null) {
            ipm.setAllowMlOff(bool6.booleanValue());
        }
        Integer num4 = this.customTfpsFlags;
        if (num4 != null) {
            ipm.setCustomTfpsFlags(num4.intValue());
        }
        Integer num5 = this.intelMode;
        if (num5 != null) {
            ipm.setIntelMode(IntelMode.fromInt(num5.intValue()), this.intelConfig);
        }
        Boolean bool7 = this.frameInterpolationEnabled;
        if (bool7 != null) {
            ipm.setFrameInterpolationEnabled(bool7.booleanValue());
        }
        Float f = this.frameInterpolationTemperatureOffset;
        if (f != null) {
            ipm.setFrameInterpolationTemperatureOffset(f.floatValue());
        }
        Float f2 = this.frameInterpolationFrameRateOffset;
        if (f2 != null) {
            ipm.setFrameInterpolationFrameRateOffset(f2.floatValue());
        }
        Float f3 = this.frameInterpolationDecayHalfLife;
        if (f3 != null) {
            ipm.setFrameInterpolationDecayHalfLife(f3.floatValue());
        }
    }
}
