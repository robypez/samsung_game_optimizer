package com.samsung.android.game.gos.value;

import android.util.Pair;
import com.google.gson.annotations.SerializedName;
import com.samsung.android.game.gos.ipm.ParameterRequest;
import com.samsung.android.game.gos.value.Constants;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import datautil.IDataUtil;
import java.util.ArrayList;
import java.util.List;

public class RinglogConstants {
    public static final long DATA_COLLECTION_MIN_SESSION_DURATION_MS = 5000;
    public static boolean DEBUG = false;
    public static final int HOUR_TO_SECONDS = 3600;
    public static final String KEY_GPP_STATE_CONTROL_LEVEL = "control_level";
    public static final double MAX_BATTERY_BENEFIT = 0.2d;
    public static final double MAX_FPS_BENEFIT = 0.2d;
    public static final double MIN_BATTERY_BENEFIT = 0.03d;
    public static final double MIN_FPS_BENEFIT = 0.03d;
    public static final long ONGOING_SESSION_TIME_GAP_MS = 3000;
    public static final int REPORT_SESSION_LOG_DATA_COLLECTION_RATE_MS = 1000;
    public static final long SYSTEM_STATUS_MINIMUM_SESSION_LENGTH_MS = 10000;
    public static final String SYSTEM_STATUS_RAW_BATTERY_LEVEL = "battery_level";
    public static final String SYSTEM_STATUS_RAW_EXPLORATION_DURATION = "exploration_duration";
    public static final String SYSTEM_STATUS_RAW_EXPLORATION_LEVEL = "exploration_level";
    public static final String SYSTEM_STATUS_RAW_EXPLORATION_SUM = "exploration_sum";
    public static final String SYSTEM_STATUS_RAW_FPS = "fps";
    public static final String SYSTEM_STATUS_RAW_FPS_BENEFIT = "fps_benefit";
    public static final String SYSTEM_STATUS_RAW_FPS_LEVEL = "fps_level";
    public static final String SYSTEM_STATUS_RAW_FPS_MAX_GUESS = "fps_max_guess";
    public static final String SYSTEM_STATUS_RAW_FREE_MEMORY = "free_memory";
    public static final String SYSTEM_STATUS_RAW_FREE_MEMORY_LEVEL = "free_memory_level";
    public static final String SYSTEM_STATUS_RAW_IS_LRPST_MODE = "is_lrpst_mode";
    public static final String SYSTEM_STATUS_RAW_NETWORK_LEVEL = "network_level";
    public static final String SYSTEM_STATUS_RAW_PLAYTIME_BENEFIT = "playtime_benefit";
    public static final String SYSTEM_STATUS_RAW_SESSION_LEN = "session_length";
    public static final String SYSTEM_STATUS_RAW_TARGET_TEMP = "target_temp";
    public static final String SYSTEM_STATUS_RAW_TEMP = "temp";
    public static final String SYSTEM_STATUS_RAW_TEMP_DIFF = "temp_diff";
    public static final String SYSTEM_STATUS_RAW_TOTAL_MEMORY = "total_memory";

    public static class DataUploadMode {
        public static final String AGG = "agg";
        public static final String BOTH = "both";
        public static final String DEFAULT_MODE = "agg";
        public static final String RAW = "raw";
    }

    public enum ParameterDataType {
        INTEGER,
        LONG,
        FLOAT
    }

    public static class ParameterInfo {
        public String param;
        public String param_description;
        public int param_type;
    }

    public static class SaveRinglogSession {
        public static final String DATABASE_KEY_RINGLOG_AND_GPP_REP = "data";
        public static Pair<PerfParams, IDataUtil.AggType[]>[] ParameterGspRepAggregationPairs = {new Pair<>(PerfParams.BATTERY, IDataUtil.BATTERY), new Pair<>(PerfParams.CPU_LOAD, IDataUtil.CPU_LOAD), new Pair<>(PerfParams.FPS, IDataUtil.FPS), new Pair<>(PerfParams.GPU_LOAD, IDataUtil.GPU_LOAD), new Pair<>(PerfParams.POWER, IDataUtil.POWER), new Pair<>(PerfParams.TEMP_LRPST, IDataUtil.LRPST), new Pair<>(PerfParams.TEMP_PST, IDataUtil.PST), new Pair<>(PerfParams.CPU_F, IDataUtil.CPU_F), new Pair<>(PerfParams.CPU_MIN_SET_F, IDataUtil.CPU_MIN_SET_F), new Pair<>(PerfParams.CPU_MIN_WANTED_F, IDataUtil.CPU_MIN_WANTED_F), new Pair<>(PerfParams.CPU_SET_F, IDataUtil.CPU_SET_F), new Pair<>(PerfParams.CPU_WANTED_F, IDataUtil.CPU_WANTED_F), new Pair<>(PerfParams.CPU_F2, IDataUtil.CPU_F2), new Pair<>(PerfParams.CPU_F3, IDataUtil.CPU_F3), new Pair<>(PerfParams.MAX_GUESS_FPS, IDataUtil.FPS_MAX_GUESS), new Pair<>(PerfParams.GPU_F, IDataUtil.GPU_F), new Pair<>(PerfParams.GPU_MIN_SET_F, IDataUtil.GPU_MIN_SET_F), new Pair<>(PerfParams.GPU_MIN_WANTED_F, IDataUtil.GPU_MIN_WANTED_F), new Pair<>(PerfParams.GPU_SET_F, IDataUtil.GPU_SET_F), new Pair<>(PerfParams.GPU_WANTED_F, IDataUtil.GPU_WANTED_F)};
        public static PerfParams[] ParameterKeys = {PerfParams.BATTERY, PerfParams.CPU_LOAD, PerfParams.FPS, PerfParams.GPU_LOAD, PerfParams.POWER, PerfParams.TEMP_LRPST, PerfParams.TEMP_PST, PerfParams.CPU_F, PerfParams.CPU_MIN_SET_F, PerfParams.CPU_MIN_WANTED_F, PerfParams.CPU_SET_F, PerfParams.CPU_WANTED_F, PerfParams.CPU_F2, PerfParams.CPU_F3, PerfParams.MAX_GUESS_FPS, PerfParams.GPU_F, PerfParams.GPU_MIN_SET_F, PerfParams.GPU_MIN_WANTED_F, PerfParams.GPU_SET_F, PerfParams.GPU_WANTED_F};
        public static final String SERVER_KEY_PERFORMANCE_DATA = "performance_data";
        public static final String SERVER_KEY_PERFORMANCE_DATA_COLLECTION_RATE = "performance_data_collection_rate";
        public static final String SERVER_KEY_SESSION = "session";
        public static String[] SessionKeysExcluded = {GosInterface.KeyName.DATA_START, GosInterface.KeyName.DATA_END};
    }

    public static class Sessions {
        public int latest_session;
        public int max_sessions;
        public SessionInfo sessionInfo1;
        public SessionInfo sessionInfo2;
    }

    public static class SimpleDataRequest {
        long end;
        List<ParameterRequest> perf_params;
        long start;
    }

    public static class SystemStatusGrade {
        public static final int AVERAGE = 2;
        public static final int ERR_INSUFFICIENT_DATA = -2;
        public static final int ERR_READ_FAIL = -1;
        public static final int EXCELLENT = 4;
        public static final int GOOD = 3;
        public static final int POOR = 1;
        public static final int VERY_POOR = 0;
    }

    public static class SessionInfo {
        @SerializedName("data_end")
        public long data_end;
        @SerializedName("data_end_ms")
        public long data_end_ms;
        @SerializedName("data_start")
        public long data_start;
        @SerializedName("data_start_ms")
        public long data_start_ms;
        @SerializedName("deletedTime")
        public long deletedTime;
        @SerializedName("end")
        public long end;
        @SerializedName("flags")
        public int flags;
        @SerializedName("intel_mode")
        public int intel_mode;
        @SerializedName("minutesWest")
        public int minutesWest;
        @SerializedName("package_name")
        public String package_name;
        @SerializedName("profile")
        public int profile;
        @SerializedName("start")
        public long start;
        @SerializedName("start_ms")
        public long start_ms;
        @SerializedName("targetLRPST")
        public int targetLRPST;
        @SerializedName("targetPower")
        public int targetPower;
        @SerializedName("targetPst")
        public int targetPst;
        @SerializedName("totalTime")
        public long totalTime;
        @SerializedName("version")
        public String version;

        public String toString() {
            return "package_name = " + this.package_name + ", start = " + this.start + ", end = " + this.end + ", start_ms = " + this.start_ms + ", data_start = " + this.data_start + ", data_end = " + this.data_end + ", data_start_ms = " + this.data_start_ms + ", data_end_ms = " + this.data_end_ms + ", deleted_time = " + this.deletedTime + ", total_time = " + this.totalTime + ", version = " + this.version + ", profile = " + this.profile + ", targetPst = " + this.targetPst + ", targetLRPST = " + this.targetLRPST + ", targetPower = " + this.targetPower + ", intel_mode = " + this.intel_mode + ", flags = " + this.flags + ", minutesWest = " + this.minutesWest + ".";
        }

        public boolean isUsingLrpst() {
            return (this.flags & 2097152) == 2097152;
        }
    }

    public static class SessionWrapper {
        public int id = -1;
        public SessionInfo info;

        public String toString() {
            return "SessionWrapper{id=" + this.id + ", info=" + this.info + '}';
        }
    }

    public enum AggregationType {
        MEAN(0),
        MEDIAN(1),
        MODE(2),
        MIN(3),
        MAX(4),
        SUM(5);
        
        int value;

        private AggregationType(int i) {
            this.value = i;
        }

        public int getValue() {
            return this.value;
        }

        public static AggregationType getTypeFromValue(int i) {
            for (AggregationType aggregationType : values()) {
                if (aggregationType.value == i) {
                    return aggregationType;
                }
            }
            return null;
        }
    }

    public enum PerfParams {
        GPU_F(Constants.RingLog.Parameter.GPU_F, "The real frequency of the GPU.", ParameterDataType.INTEGER),
        GPU_WANTED_F(Constants.RingLog.Parameter.GPU_WANTED_F, "The maximum frequency of the GPU set by SPA.", ParameterDataType.INTEGER),
        GPU_SET_F(Constants.RingLog.Parameter.GPU_SET_F, "The maximum frequency of the GPU as set by the system.", ParameterDataType.INTEGER),
        GPU_MIN_WANTED_F(Constants.RingLog.Parameter.GPU_MIN_WANTED_F, "The minimum frequency the GPU can take as set by SPA.", ParameterDataType.INTEGER),
        GPU_MIN_SET_F(Constants.RingLog.Parameter.GPU_MIN_SET_F, "The minimum frequency the GPU can take as set by the system.", ParameterDataType.INTEGER),
        GPU_LOAD(Constants.RingLog.Parameter.GPU_LOAD, "The GPU’s load percentage.", ParameterDataType.FLOAT),
        CPU_LOAD(Constants.RingLog.Parameter.CPU_LOAD, "Packed CPU load in group of 4bits for each core.", ParameterDataType.LONG),
        TEMP_AP(Constants.RingLog.Parameter.TEMP_AP, "The temperature of AP.", ParameterDataType.FLOAT),
        TEMP_PST(Constants.RingLog.Parameter.TEMP_PST, "The predicted temperature of the device's surface.", ParameterDataType.FLOAT),
        TEMP_LRPST(Constants.RingLog.Parameter.TEMP_LRPST, "The LRPST temperature if present.", ParameterDataType.FLOAT),
        CPU_F(Constants.RingLog.Parameter.CPU_F, "The CPU frequency of cluster 1(Big).", ParameterDataType.INTEGER),
        CPU_F2(Constants.RingLog.Parameter.CPU_F2, "The CPU frequency of cluster 2(middle).", ParameterDataType.INTEGER),
        CPU_F3(Constants.RingLog.Parameter.CPU_F3, "The CPU frequency of cluster 3(little).", ParameterDataType.INTEGER),
        CPU_WANTED_F(Constants.RingLog.Parameter.CPU_WANTED_F, "The maximum frequency the CPU can take as set by SPA.", ParameterDataType.INTEGER),
        CPU_SET_F(Constants.RingLog.Parameter.CPU_SET_F, "The maximum frequency the CPU can take as set by the system.", ParameterDataType.INTEGER),
        CPU_MIN_WANTED_F(Constants.RingLog.Parameter.CPU_MIN_WANTED_F, "The minimum frequency the CPU can take as set by SPA.", ParameterDataType.INTEGER),
        CPU_MIN_SET_F(Constants.RingLog.Parameter.CPU_MIN_SET_F, "The minimum frequency the CPU can take as set by the system.", ParameterDataType.INTEGER),
        FPS("fps", "The game's Frames Per Second.", ParameterDataType.FLOAT),
        FPS_BENEFIT_PERCENT(Constants.RingLog.Parameter.FPS_BENEFIT_PERCENT, "The SPA fps benefit over SIOP as percentage.", ParameterDataType.FLOAT),
        FPS_BENEFIT_PERCENT_LOW(Constants.RingLog.Parameter.FPS_BENEFIT_PERCENT_LOW, "The SPA fps benefit (lower freqs) over SIOP as percentage.", ParameterDataType.FLOAT),
        TFPS(Constants.RingLog.Parameter.TFPS, "The Frame-rate the ML agent was aiming towards.", ParameterDataType.INTEGER),
        MAX_GUESS_FPS("fps_max_guess", "SPA’s best guess for the game’s maximum achievable FPS.", ParameterDataType.INTEGER),
        REFRESH_RATE(Constants.RingLog.Parameter.REFRESH_RATE, "Current refresh rate", ParameterDataType.INTEGER),
        POWER(Constants.RingLog.Parameter.POWER, "Power in mW.", ParameterDataType.INTEGER),
        BATTERY(Constants.RingLog.Parameter.BATTERY, "The battery percentage.", ParameterDataType.INTEGER),
        MEMORY(Constants.RingLog.Parameter.MEMORY, "Memory in MB.", ParameterDataType.INTEGER),
        CPU_LOAD_TOTAL(Constants.RingLog.Parameter.CPU_LOAD_TOTAL, "Average CPU load of all the cores.", ParameterDataType.INTEGER),
        ML_EXPLORATION(Constants.RingLog.Parameter.ML_EXPLORATION, "The number of explorations done.", ParameterDataType.INTEGER),
        ML_RANDOM(Constants.RingLog.Parameter.ML_RANDOM, "The number of random actions done.", ParameterDataType.INTEGER),
        BATTERY_PREDICTION(Constants.RingLog.Parameter.BATTERY_PREDICTION, "The number of seconds predicted to be left on battery.", ParameterDataType.INTEGER),
        BATTERY_PREDICTION_BENEFIT(Constants.RingLog.Parameter.BATTERY_PREDICTION_BENEFIT, "The number of seconds predicted that are more than SIOP.", ParameterDataType.INTEGER),
        BATTERY_PREDICTION_BENEFIT_LOW(Constants.RingLog.Parameter.BATTERY_PREDICTION_BENEFIT_LOW, "The number of seconds predicted (lower freqs) that are more than SIOP.", ParameterDataType.INTEGER),
        BATTERY_PREDICTION_BENEFIT_PERCENT(Constants.RingLog.Parameter.BATTERY_PREDICTION_BENEFIT_PERCENT, "The number of seconds predicted that are more than SIOP as percentage of SPA prediction", ParameterDataType.INTEGER),
        BATTERY_PREDICTION_BENEFIT_PERCENT_LOW(Constants.RingLog.Parameter.BATTERY_PREDICTION_BENEFIT_PERCENT_LOW, "The number of seconds predicted (lower freqs) that are more than SIOP as percentage of SPA prediction", ParameterDataType.INTEGER),
        CPU_LOAD_LABEL(Constants.RingLog.Parameter.CPU_LOAD_LABEL, "The avg CPU load of all the cores as a label where 1-LOW to 3-HIGH", ParameterDataType.INTEGER),
        GPU_LOAD_LABEL(Constants.RingLog.Parameter.GPU_LOAD_LABEL, "The GPU load as a label where 1-LOW to 3-HIGH", ParameterDataType.INTEGER);
        
        String description;
        ParameterDataType parameterType;
        String repr;

        private PerfParams(String str, String str2, ParameterDataType parameterDataType) {
            this.repr = str;
            this.description = str2;
            this.parameterType = parameterDataType;
        }

        public String getName() {
            return this.repr;
        }

        public String getDescription() {
            return this.description;
        }

        public ParameterDataType getParameterType() {
            return this.parameterType;
        }

        public static PerfParams getParameterFromStr(String str) {
            for (PerfParams perfParams : values()) {
                if (perfParams.getName().equals(str)) {
                    return perfParams;
                }
            }
            return null;
        }
    }

    public enum SystemStatusParams {
        BATTERY(Constants.RingLog.Parameter.BATTERY),
        NETWORK("network"),
        MEMORY(Constants.RingLog.Parameter.MEMORY),
        TEMPERATURE("temperature"),
        GAME_PERF("game_perf"),
        SPA_LEARNING("spa_learning"),
        GAME_HEAVY("game_heavy");
        
        String repr;

        static String getPrefix() {
            return "system_status_";
        }

        private SystemStatusParams(String str) {
            this.repr = str;
        }

        public String getName() {
            return this.repr;
        }

        public String getPrefixedName() {
            return getPrefix() + getName();
        }

        public List<String> getRawDataList() {
            ArrayList arrayList = new ArrayList();
            switch (AnonymousClass1.$SwitchMap$com$samsung$android$game$gos$value$RinglogConstants$SystemStatusParams[ordinal()]) {
                case 1:
                    arrayList.add(RinglogConstants.SYSTEM_STATUS_RAW_BATTERY_LEVEL);
                    break;
                case 2:
                    arrayList.add(RinglogConstants.SYSTEM_STATUS_RAW_NETWORK_LEVEL);
                    break;
                case 3:
                    arrayList.add(RinglogConstants.SYSTEM_STATUS_RAW_FREE_MEMORY);
                    arrayList.add(RinglogConstants.SYSTEM_STATUS_RAW_TOTAL_MEMORY);
                    arrayList.add(RinglogConstants.SYSTEM_STATUS_RAW_FREE_MEMORY_LEVEL);
                    break;
                case 4:
                    arrayList.add(RinglogConstants.SYSTEM_STATUS_RAW_TEMP);
                    arrayList.add(RinglogConstants.SYSTEM_STATUS_RAW_TARGET_TEMP);
                    arrayList.add(RinglogConstants.SYSTEM_STATUS_RAW_TEMP_DIFF);
                    arrayList.add(RinglogConstants.SYSTEM_STATUS_RAW_SESSION_LEN);
                    arrayList.add(RinglogConstants.SYSTEM_STATUS_RAW_IS_LRPST_MODE);
                    break;
                case 5:
                    arrayList.add("fps");
                    arrayList.add("fps_max_guess");
                    arrayList.add(RinglogConstants.SYSTEM_STATUS_RAW_FPS_LEVEL);
                    arrayList.add(RinglogConstants.SYSTEM_STATUS_RAW_SESSION_LEN);
                    break;
                case 6:
                    arrayList.add(RinglogConstants.SYSTEM_STATUS_RAW_EXPLORATION_SUM);
                    arrayList.add(RinglogConstants.SYSTEM_STATUS_RAW_EXPLORATION_DURATION);
                    arrayList.add(RinglogConstants.SYSTEM_STATUS_RAW_EXPLORATION_LEVEL);
                    arrayList.add(RinglogConstants.SYSTEM_STATUS_RAW_SESSION_LEN);
                    break;
                case 7:
                    arrayList.add(RinglogConstants.SYSTEM_STATUS_RAW_FPS_BENEFIT);
                    arrayList.add(RinglogConstants.SYSTEM_STATUS_RAW_PLAYTIME_BENEFIT);
                    break;
            }
            return arrayList;
        }

        /* JADX WARNING: Can't fix incorrect switch cases order */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static com.samsung.android.game.gos.value.RinglogConstants.ParameterDataType getDataType(java.lang.String r1) {
            /*
                int r0 = r1.hashCode()
                switch(r0) {
                    case -2084577854: goto L_0x00c5;
                    case -1982904272: goto L_0x00bb;
                    case -1607545844: goto L_0x00b1;
                    case -877252910: goto L_0x00a6;
                    case -611156429: goto L_0x009c;
                    case -515255450: goto L_0x0092;
                    case -428254791: goto L_0x0087;
                    case -244156242: goto L_0x007c;
                    case -136523212: goto L_0x0072;
                    case -105672145: goto L_0x0068;
                    case 101609: goto L_0x005c;
                    case 3556308: goto L_0x0050;
                    case 521103964: goto L_0x0045;
                    case 1296559937: goto L_0x0039;
                    case 1521354344: goto L_0x002d;
                    case 1709686959: goto L_0x0021;
                    case 1969800592: goto L_0x0015;
                    case 2033446489: goto L_0x0009;
                    default: goto L_0x0007;
                }
            L_0x0007:
                goto L_0x00cf
            L_0x0009:
                java.lang.String r0 = "playtime_benefit"
                boolean r1 = r1.equals(r0)
                if (r1 == 0) goto L_0x00cf
                r1 = 16
                goto L_0x00d0
            L_0x0015:
                java.lang.String r0 = "temp_diff"
                boolean r1 = r1.equals(r0)
                if (r1 == 0) goto L_0x00cf
                r1 = 12
                goto L_0x00d0
            L_0x0021:
                java.lang.String r0 = "session_length"
                boolean r1 = r1.equals(r0)
                if (r1 == 0) goto L_0x00cf
                r1 = 8
                goto L_0x00d0
            L_0x002d:
                java.lang.String r0 = "exploration_level"
                boolean r1 = r1.equals(r0)
                if (r1 == 0) goto L_0x00cf
                r1 = 15
                goto L_0x00d0
            L_0x0039:
                java.lang.String r0 = "fps_benefit"
                boolean r1 = r1.equals(r0)
                if (r1 == 0) goto L_0x00cf
                r1 = 17
                goto L_0x00d0
            L_0x0045:
                java.lang.String r0 = "total_memory"
                boolean r1 = r1.equals(r0)
                if (r1 == 0) goto L_0x00cf
                r1 = 7
                goto L_0x00d0
            L_0x0050:
                java.lang.String r0 = "temp"
                boolean r1 = r1.equals(r0)
                if (r1 == 0) goto L_0x00cf
                r1 = 11
                goto L_0x00d0
            L_0x005c:
                java.lang.String r0 = "fps"
                boolean r1 = r1.equals(r0)
                if (r1 == 0) goto L_0x00cf
                r1 = 13
                goto L_0x00d0
            L_0x0068:
                java.lang.String r0 = "exploration_sum"
                boolean r1 = r1.equals(r0)
                if (r1 == 0) goto L_0x00cf
                r1 = 3
                goto L_0x00d0
            L_0x0072:
                java.lang.String r0 = "free_memory"
                boolean r1 = r1.equals(r0)
                if (r1 == 0) goto L_0x00cf
                r1 = 6
                goto L_0x00d0
            L_0x007c:
                java.lang.String r0 = "fps_level"
                boolean r1 = r1.equals(r0)
                if (r1 == 0) goto L_0x00cf
                r1 = 14
                goto L_0x00d0
            L_0x0087:
                java.lang.String r0 = "free_memory_level"
                boolean r1 = r1.equals(r0)
                if (r1 == 0) goto L_0x00cf
                r1 = 10
                goto L_0x00d0
            L_0x0092:
                java.lang.String r0 = "fps_max_guess"
                boolean r1 = r1.equals(r0)
                if (r1 == 0) goto L_0x00cf
                r1 = 2
                goto L_0x00d0
            L_0x009c:
                java.lang.String r0 = "network_level"
                boolean r1 = r1.equals(r0)
                if (r1 == 0) goto L_0x00cf
                r1 = 0
                goto L_0x00d0
            L_0x00a6:
                java.lang.String r0 = "battery_level"
                boolean r1 = r1.equals(r0)
                if (r1 == 0) goto L_0x00cf
                r1 = 9
                goto L_0x00d0
            L_0x00b1:
                java.lang.String r0 = "is_lrpst_mode"
                boolean r1 = r1.equals(r0)
                if (r1 == 0) goto L_0x00cf
                r1 = 5
                goto L_0x00d0
            L_0x00bb:
                java.lang.String r0 = "exploration_duration"
                boolean r1 = r1.equals(r0)
                if (r1 == 0) goto L_0x00cf
                r1 = 4
                goto L_0x00d0
            L_0x00c5:
                java.lang.String r0 = "target_temp"
                boolean r1 = r1.equals(r0)
                if (r1 == 0) goto L_0x00cf
                r1 = 1
                goto L_0x00d0
            L_0x00cf:
                r1 = -1
            L_0x00d0:
                switch(r1) {
                    case 0: goto L_0x00d9;
                    case 1: goto L_0x00d9;
                    case 2: goto L_0x00d9;
                    case 3: goto L_0x00d9;
                    case 4: goto L_0x00d9;
                    case 5: goto L_0x00d9;
                    case 6: goto L_0x00d6;
                    case 7: goto L_0x00d6;
                    case 8: goto L_0x00d6;
                    default: goto L_0x00d3;
                }
            L_0x00d3:
                com.samsung.android.game.gos.value.RinglogConstants$ParameterDataType r1 = com.samsung.android.game.gos.value.RinglogConstants.ParameterDataType.FLOAT
                return r1
            L_0x00d6:
                com.samsung.android.game.gos.value.RinglogConstants$ParameterDataType r1 = com.samsung.android.game.gos.value.RinglogConstants.ParameterDataType.LONG
                return r1
            L_0x00d9:
                com.samsung.android.game.gos.value.RinglogConstants$ParameterDataType r1 = com.samsung.android.game.gos.value.RinglogConstants.ParameterDataType.INTEGER
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.value.RinglogConstants.SystemStatusParams.getDataType(java.lang.String):com.samsung.android.game.gos.value.RinglogConstants$ParameterDataType");
        }
    }

    /* renamed from: com.samsung.android.game.gos.value.RinglogConstants$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$game$gos$value$RinglogConstants$SystemStatusParams;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|(3:13|14|16)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|16) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.samsung.android.game.gos.value.RinglogConstants$SystemStatusParams[] r0 = com.samsung.android.game.gos.value.RinglogConstants.SystemStatusParams.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$samsung$android$game$gos$value$RinglogConstants$SystemStatusParams = r0
                com.samsung.android.game.gos.value.RinglogConstants$SystemStatusParams r1 = com.samsung.android.game.gos.value.RinglogConstants.SystemStatusParams.BATTERY     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$value$RinglogConstants$SystemStatusParams     // Catch:{ NoSuchFieldError -> 0x001d }
                com.samsung.android.game.gos.value.RinglogConstants$SystemStatusParams r1 = com.samsung.android.game.gos.value.RinglogConstants.SystemStatusParams.NETWORK     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$value$RinglogConstants$SystemStatusParams     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.samsung.android.game.gos.value.RinglogConstants$SystemStatusParams r1 = com.samsung.android.game.gos.value.RinglogConstants.SystemStatusParams.MEMORY     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$value$RinglogConstants$SystemStatusParams     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.samsung.android.game.gos.value.RinglogConstants$SystemStatusParams r1 = com.samsung.android.game.gos.value.RinglogConstants.SystemStatusParams.TEMPERATURE     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$value$RinglogConstants$SystemStatusParams     // Catch:{ NoSuchFieldError -> 0x003e }
                com.samsung.android.game.gos.value.RinglogConstants$SystemStatusParams r1 = com.samsung.android.game.gos.value.RinglogConstants.SystemStatusParams.GAME_PERF     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$value$RinglogConstants$SystemStatusParams     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.samsung.android.game.gos.value.RinglogConstants$SystemStatusParams r1 = com.samsung.android.game.gos.value.RinglogConstants.SystemStatusParams.SPA_LEARNING     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$value$RinglogConstants$SystemStatusParams     // Catch:{ NoSuchFieldError -> 0x0054 }
                com.samsung.android.game.gos.value.RinglogConstants$SystemStatusParams r1 = com.samsung.android.game.gos.value.RinglogConstants.SystemStatusParams.GAME_HEAVY     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.value.RinglogConstants.AnonymousClass1.<clinit>():void");
        }
    }

    public enum GameStatus {
        Unknown(-1),
        Light(0),
        Heavy(1);
        
        int grade;

        private GameStatus(int i) {
            this.grade = i;
        }

        public int getGrade() {
            return this.grade;
        }
    }
}
