package com.samsung.android.game.gos.ipm;

import android.util.Base64;
import androidx.collection.SparseArrayCompat;
import com.samsung.android.game.gos.feature.gfi.value.GfiPolicy;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.zip.Deflater;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CommonUtil {
    private static final int BYTE_LENGTH = 1024;
    private static final String LOG_TAG = "CommonUtil";

    public static List<String> getEventsLists(Map<Long, String> map) {
        ArrayList arrayList = new ArrayList();
        for (Map.Entry next : map.entrySet()) {
            arrayList.add(next.getKey() + ":" + ((String) next.getValue()));
        }
        return arrayList;
    }

    public static String getIpmEncodedRinglog(File file) {
        try {
            byte[] compressBytes = compressBytes(getBytes(new File(file + "/ipm_ringlog")));
            if (compressBytes != null) {
                String encodeToString = Base64.encodeToString(compressBytes, 2);
                Log.d(LOG_TAG, "getIpmEncodedRinglog(), ipm encoded string length: " + encodeToString.length());
                return encodeToString;
            }
            Log.w(LOG_TAG, "getIpmEncodedRinglog(), ipm compressed bytes are null.");
            return null;
        } catch (Throwable th) {
            Log.w(LOG_TAG, th);
            return null;
        }
    }

    public static String getIpmReadableRinglogDescription(int i, boolean z, float f, int i2, int i3) {
        String str;
        String str2;
        String str3;
        String str4;
        int i4 = i;
        int i5 = i2;
        int i6 = i3;
        if ((524288 & i4) > 0) {
            str = "NoTC,";
        } else {
            String str5 = BuildConfig.VERSION_NAME + f;
            if (z) {
                str = str5 + "(LRPST),";
            } else {
                str = str5 + "C,";
            }
        }
        String[] strArr = {"Low,", "Hig,", "Ult,"};
        if (i5 < 0 || i5 > 2) {
            str2 = str + "Cus,";
        } else {
            str2 = str + strArr[i5];
        }
        String[] strArr2 = {"QX+S,", "QM,", "QK,", "Sm,", "LSTM,"};
        if (i6 < 0 || i6 > 4) {
            str3 = str2 + "?,";
        } else {
            str3 = str2 + strArr2[i6];
        }
        String[] strArr3 = {"ConfigError,", "DVFSHelper,", "DVFSWrapper,", "CFMHelper,", "CFMSem,", "Dir,", "DirM,", "DVFSWU,", "DirU,", "DirMU,"};
        int i7 = (i4 >> 5) & 15;
        if (i7 < 0 || i7 > 9) {
            str4 = str3 + "Freq?,";
        } else {
            str4 = str3 + strArr3[i7];
        }
        SparseArrayCompat sparseArrayCompat = new SparseArrayCompat();
        sparseArrayCompat.append(0, BuildConfig.VERSION_NAME);
        sparseArrayCompat.append(512, "Rec,");
        sparseArrayCompat.append(1024, "Cap,");
        sparseArrayCompat.append(2048, "Log1,");
        sparseArrayCompat.append(4096, "Log2,");
        sparseArrayCompat.append(8192, "Train,");
        sparseArrayCompat.append(32768, "QCF,");
        sparseArrayCompat.append(131072, "SFF,");
        sparseArrayCompat.append(262144, "Save,");
        sparseArrayCompat.append(1048576, "Stab,");
        sparseArrayCompat.append(4194304, "AllowMlOff,");
        return (((((((((str4 + ((String) sparseArrayCompat.get(i4 & 512))) + ((String) sparseArrayCompat.get(i4 & 1024))) + ((String) sparseArrayCompat.get(i4 & 2048))) + ((String) sparseArrayCompat.get(i4 & 4096))) + ((String) sparseArrayCompat.get(i4 & 8192))) + ((String) sparseArrayCompat.get(i4 & 32768))) + ((String) sparseArrayCompat.get(i4 & 131072))) + ((String) sparseArrayCompat.get(i4 & 262144))) + ((String) sparseArrayCompat.get(i4 & 1048576))) + ((String) sparseArrayCompat.get(i4 & 4194304));
    }

    public static String getIpmReadableRinglog(JSONObject jSONObject) {
        int i;
        int i2;
        StringBuilder sb;
        String str;
        boolean z;
        boolean z2;
        StringBuilder sb2;
        int i3;
        float f;
        Duration ofSeconds;
        long seconds;
        Locale locale;
        String str2;
        String str3;
        Object[] objArr;
        boolean z3;
        String sb3;
        JSONObject jSONObject2 = jSONObject;
        String str4 = LOG_TAG;
        StringBuilder sb4 = new StringBuilder();
        try {
            int i4 = jSONObject2.getInt(GosInterface.KeyName.LATEST_SESSION);
            int i5 = jSONObject2.getInt(GosInterface.KeyName.MAX_SESSIONS);
            int i6 = ((i4 - 19) + i5) % i5;
            int i7 = 0;
            while (i7 < 20) {
                int i8 = (i6 + i7) % i5;
                try {
                    if (!jSONObject2.has(Integer.toString(i8))) {
                        sb = sb4;
                        i2 = i5;
                        i = i6;
                    } else {
                        JSONObject jSONObject3 = jSONObject2.getJSONObject(Integer.toString(i8));
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                        long j = jSONObject3.getLong(GosInterface.KeyName.DATA_START_MS);
                        int i9 = jSONObject3.getInt("minutesWest");
                        Date date = new Date(j);
                        int i10 = jSONObject3.getInt(GosInterface.KeyName.FLAGS);
                        long j2 = jSONObject3.getLong("totalTime") / 1000;
                        long j3 = j2 - (jSONObject3.getLong("deletedTime") / 1000);
                        String string = jSONObject3.getString("package_name");
                        int i11 = jSONObject3.getInt(GosInterface.KeyName.PROFILE);
                        int i12 = jSONObject3.getInt(GosInterface.KeyName.INTEL_MODE);
                        i2 = i5;
                        try {
                            jSONObject3.getInt("targetPower");
                            i = i6;
                            z = (2097152 & i10) > 0;
                        } catch (JSONException e) {
                            e = e;
                            str = str4;
                            sb = sb4;
                            i = i6;
                            sb.append("Error parsing session from ringlog data (");
                            sb.append(e);
                            sb.append(")\n");
                            str4 = str;
                            Log.w(str4, (Throwable) e);
                            i7++;
                            sb4 = sb;
                            i5 = i2;
                            i6 = i;
                            jSONObject2 = jSONObject;
                        }
                        try {
                            str = str4;
                            z2 = jSONObject3.getInt("alive") == 1;
                        } catch (JSONException e2) {
                            e = e2;
                            str = str4;
                            sb = sb4;
                            sb.append("Error parsing session from ringlog data (");
                            sb.append(e);
                            sb.append(")\n");
                            str4 = str;
                            Log.w(str4, (Throwable) e);
                            i7++;
                            sb4 = sb;
                            i5 = i2;
                            i6 = i;
                            jSONObject2 = jSONObject;
                        }
                        try {
                            String string2 = jSONObject3.getString(GosInterface.KeyName.VERSION);
                            if (z) {
                                sb2 = sb4;
                                try {
                                    i3 = jSONObject3.getInt(GosInterface.KeyName.TARGET_LRPST);
                                } catch (JSONException e3) {
                                    e = e3;
                                    sb = sb2;
                                    sb.append("Error parsing session from ringlog data (");
                                    sb.append(e);
                                    sb.append(")\n");
                                    str4 = str;
                                    Log.w(str4, (Throwable) e);
                                    i7++;
                                    sb4 = sb;
                                    i5 = i2;
                                    i6 = i;
                                    jSONObject2 = jSONObject;
                                }
                            } else {
                                sb2 = sb4;
                                i3 = jSONObject3.getInt(GosInterface.KeyName.TARGET_PST);
                            }
                            f = ((float) i3) / 10.0f;
                            Duration ofSeconds2 = Duration.ofSeconds(j2);
                            ofSeconds = Duration.ofSeconds(j3);
                            seconds = ofSeconds2.getSeconds();
                            locale = Locale.US;
                            str2 = string2;
                            str3 = string;
                            objArr = new Object[3];
                            z3 = false;
                        } catch (JSONException e4) {
                            e = e4;
                            sb = sb4;
                            sb.append("Error parsing session from ringlog data (");
                            sb.append(e);
                            sb.append(")\n");
                            str4 = str;
                            Log.w(str4, (Throwable) e);
                            i7++;
                            sb4 = sb;
                            i5 = i2;
                            i6 = i;
                            jSONObject2 = jSONObject;
                        }
                        try {
                            objArr[0] = Long.valueOf(seconds / 3600);
                            objArr[1] = Long.valueOf((seconds % 3600) / 60);
                            objArr[2] = Long.valueOf(seconds % 60);
                            String format = String.format(locale, "%d:%02d:%02d", objArr);
                            long seconds2 = ofSeconds.getSeconds();
                            Locale locale2 = Locale.US;
                            Object[] objArr2 = new Object[3];
                            z3 = false;
                            objArr2[0] = Long.valueOf(seconds2 / 3600);
                            objArr2[1] = Long.valueOf((seconds2 % 3600) / 60);
                            objArr2[2] = Long.valueOf(seconds2 % 60);
                            String format2 = String.format(locale2, "%d:%02d:%02d", objArr2);
                            String num = Integer.toString(Math.floorDiv(i9, 60));
                            if (i9 % 60 > 0) {
                                num = Integer.toString(i9 % 60);
                            }
                            String str5 = "IPM";
                            if ((65536 & i10) > 0) {
                                str5 = "SIOP";
                            }
                            String ipmReadableRinglogDescription = getIpmReadableRinglogDescription(i10, z, f, i11, i12);
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append(str5);
                            sb5.append(" ");
                            sb5.append(19 - i7);
                            sb5.append(" : ");
                            sb5.append(simpleDateFormat.format(date));
                            sb5.append(" ");
                            sb5.append(num);
                            sb5.append(" (");
                            if (z2) {
                                format = "-ALIVE-";
                            }
                            sb5.append(format);
                            sb5.append("/");
                            sb5.append(format2);
                            sb5.append(") ");
                            sb5.append(str3);
                            sb5.append(" ");
                            try {
                                sb5.append(ipmReadableRinglogDescription.substring(0, ipmReadableRinglogDescription.length() - 1));
                                sb5.append(" v");
                                sb5.append(str2);
                                sb5.append("\n");
                                sb3 = sb5.toString();
                                sb = sb2;
                            } catch (JSONException e5) {
                                e = e5;
                                sb = sb2;
                                sb.append("Error parsing session from ringlog data (");
                                sb.append(e);
                                sb.append(")\n");
                                str4 = str;
                                Log.w(str4, (Throwable) e);
                                i7++;
                                sb4 = sb;
                                i5 = i2;
                                i6 = i;
                                jSONObject2 = jSONObject;
                            }
                        } catch (JSONException e6) {
                            e = e6;
                            boolean z4 = z3;
                            sb = sb2;
                            sb.append("Error parsing session from ringlog data (");
                            sb.append(e);
                            sb.append(")\n");
                            str4 = str;
                            Log.w(str4, (Throwable) e);
                            i7++;
                            sb4 = sb;
                            i5 = i2;
                            i6 = i;
                            jSONObject2 = jSONObject;
                        }
                        try {
                            sb.append(sb3);
                            str4 = str;
                        } catch (JSONException e7) {
                            e = e7;
                            sb.append("Error parsing session from ringlog data (");
                            sb.append(e);
                            sb.append(")\n");
                            str4 = str;
                            Log.w(str4, (Throwable) e);
                            i7++;
                            sb4 = sb;
                            i5 = i2;
                            i6 = i;
                            jSONObject2 = jSONObject;
                        }
                    }
                } catch (JSONException e8) {
                    e = e8;
                    str = str4;
                    sb = sb4;
                    i2 = i5;
                    i = i6;
                    sb.append("Error parsing session from ringlog data (");
                    sb.append(e);
                    sb.append(")\n");
                    str4 = str;
                    Log.w(str4, (Throwable) e);
                    i7++;
                    sb4 = sb;
                    i5 = i2;
                    i6 = i;
                    jSONObject2 = jSONObject;
                }
                i7++;
                sb4 = sb;
                i5 = i2;
                i6 = i;
                jSONObject2 = jSONObject;
            }
            return sb4.toString();
        } catch (JSONException e9) {
            Log.w(str4, (Throwable) e9);
            return sb4.toString();
        }
    }

    public static JSONObject processSpaCommands(GlobalSettings globalSettings, Ipm ipm, JSONObject jSONObject) {
        Log.d(LOG_TAG, "Received SPA command");
        JSONObject jSONObject2 = new JSONObject();
        jSONObject.keys().forEachRemaining(new Consumer(jSONObject, ipm, globalSettings, jSONObject2) {
            public final /* synthetic */ JSONObject f$0;
            public final /* synthetic */ Ipm f$1;
            public final /* synthetic */ GlobalSettings f$2;
            public final /* synthetic */ JSONObject f$3;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            public final void accept(Object obj) {
                CommonUtil.lambda$processSpaCommands$0(this.f$0, this.f$1, this.f$2, this.f$3, (String) obj);
            }
        });
        return jSONObject2;
    }

    static /* synthetic */ void lambda$processSpaCommands$0(JSONObject jSONObject, Ipm ipm, GlobalSettings globalSettings, JSONObject jSONObject2, String str) {
        char c = 65535;
        try {
            switch (str.hashCode()) {
                case -1319134980:
                    if (str.equals("set_ipm_target_temperature")) {
                        c = 6;
                        break;
                    }
                    break;
                case -395419798:
                    if (str.equals("set_supertrain")) {
                        c = 2;
                        break;
                    }
                    break;
                case 102230:
                    if (str.equals("get")) {
                        c = 8;
                        break;
                    }
                    break;
                case 213688733:
                    if (str.equals("release_mem")) {
                        c = 9;
                        break;
                    }
                    break;
                case 339040933:
                    if (str.equals("set_verbose")) {
                        c = 3;
                        break;
                    }
                    break;
                case 485775869:
                    if (str.equals("set_spa_capture_only")) {
                        c = 1;
                        break;
                    }
                    break;
                case 1004793261:
                    if (str.equals("set_ipm_force_mode")) {
                        c = 5;
                        break;
                    }
                    break;
                case 1475908005:
                    if (str.equals("set_thread_control")) {
                        c = 7;
                        break;
                    }
                    break;
                case 1765262681:
                    if (str.equals("set_ipm_mode")) {
                        c = 4;
                        break;
                    }
                    break;
                case 1960297826:
                    if (str.equals("handover_control_to_gamesdk")) {
                        c = 0;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    processSpaHandoverCommand(jSONObject.getJSONObject(str), ipm);
                    return;
                case 1:
                    boolean z = jSONObject.getBoolean(str);
                    globalSettings.setOnlyCapture(z);
                    ipm.setOnlyCapture(z);
                    return;
                case 2:
                    boolean z2 = jSONObject.getBoolean(str);
                    globalSettings.setSupertrainEnabled(z2);
                    ipm.setSupertrain(z2);
                    return;
                case 3:
                    globalSettings.setVerbose(jSONObject.getBoolean(str));
                    ipm.setLogLevel(ipm.getDefaultLogLevel());
                    return;
                case 4:
                    globalSettings.setProfile(Profile.fromInt(jSONObject.getInt(str)));
                    return;
                case 5:
                    ipm.setForceMode(jSONObject.getBoolean(str));
                    return;
                case 6:
                    globalSettings.setTargetTemperature(jSONObject.getInt(str));
                    return;
                case 7:
                    ipm.setThreadControl(jSONObject.getInt(str));
                    return;
                case 8:
                    processSpaGetCommands(jSONObject2, jSONObject.getJSONArray(str), globalSettings, ipm);
                    return;
                case 9:
                    ipm.destroySystem();
                    return;
                default:
                    if (!processSpaSetCommands(jSONObject, str, ipm)) {
                        Log.w(LOG_TAG, String.format("Unrecognized SPA command '%s'", new Object[]{str}));
                        return;
                    }
                    return;
            }
        } catch (JSONException e) {
            Log.w(LOG_TAG, String.format("Failed to parse SPA command '%s': %s", new Object[]{str, e.toString()}));
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean processSpaSetCommands(org.json.JSONObject r6, java.lang.String r7, com.samsung.android.game.gos.ipm.Ipm r8) throws org.json.JSONException {
        /*
            int r0 = r7.hashCode()
            r1 = 3
            r2 = 2
            r3 = 1
            r4 = 0
            switch(r0) {
                case -2095173639: goto L_0x0135;
                case -1913653515: goto L_0x012a;
                case -1761120230: goto L_0x0120;
                case -1350989361: goto L_0x0115;
                case -1010734648: goto L_0x010a;
                case -995672490: goto L_0x00ff;
                case -717929684: goto L_0x00f4;
                case -657156208: goto L_0x00ea;
                case -473003159: goto L_0x00e0;
                case -321492276: goto L_0x00d4;
                case -192521390: goto L_0x00c8;
                case -140181962: goto L_0x00bd;
                case 125076905: goto L_0x00b1;
                case 380403233: goto L_0x00a5;
                case 564496757: goto L_0x0099;
                case 654550998: goto L_0x008d;
                case 973933794: goto L_0x0082;
                case 1042565827: goto L_0x0076;
                case 1193128227: goto L_0x006a;
                case 1223559513: goto L_0x005f;
                case 1642341698: goto L_0x0054;
                case 1790338360: goto L_0x0048;
                case 1790348064: goto L_0x003d;
                case 1815044401: goto L_0x0031;
                case 2061445848: goto L_0x0025;
                case 2089847320: goto L_0x0019;
                case 2101402712: goto L_0x000d;
                default: goto L_0x000b;
            }
        L_0x000b:
            goto L_0x0140
        L_0x000d:
            java.lang.String r0 = "set_custom_profile"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = 14
            goto L_0x0141
        L_0x0019:
            java.lang.String r0 = "set_cpu_bottom_freq"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = 15
            goto L_0x0141
        L_0x0025:
            java.lang.String r0 = "set_thermal_control"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = 11
            goto L_0x0141
        L_0x0031:
            java.lang.String r0 = "set_min_freqs"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = 19
            goto L_0x0141
        L_0x003d:
            java.lang.String r0 = "set_target_pst"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = r4
            goto L_0x0141
        L_0x0048:
            java.lang.String r0 = "set_target_fps"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = 12
            goto L_0x0141
        L_0x0054:
            java.lang.String r0 = "set_allow_ml_off"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = 4
            goto L_0x0141
        L_0x005f:
            java.lang.String r0 = "set_high_stability_mode"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = 6
            goto L_0x0141
        L_0x006a:
            java.lang.String r0 = "set_fixed_target_fps"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = 9
            goto L_0x0141
        L_0x0076:
            java.lang.String r0 = "set_max_freqs"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = 18
            goto L_0x0141
        L_0x0082:
            java.lang.String r0 = "set_cpu_gap"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = r2
            goto L_0x0141
        L_0x008d:
            java.lang.String r0 = "reset_fixed_target_fps"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = 10
            goto L_0x0141
        L_0x0099:
            java.lang.String r0 = "set_enable_gpu_min_freq_control"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = 24
            goto L_0x0141
        L_0x00a5:
            java.lang.String r0 = "set_enable_bus_min_freq_control"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = 25
            goto L_0x0141
        L_0x00b1:
            java.lang.String r0 = "set_enable_to_tgpa"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = 16
            goto L_0x0141
        L_0x00bd:
            java.lang.String r0 = "set_enable_bus_freq"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = 5
            goto L_0x0141
        L_0x00c8:
            java.lang.String r0 = "enable_any_mode"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = 22
            goto L_0x0141
        L_0x00d4:
            java.lang.String r0 = "set_profile"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = 13
            goto L_0x0141
        L_0x00e0:
            java.lang.String r0 = "set_intel_mode"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = r1
            goto L_0x0141
        L_0x00ea:
            java.lang.String r0 = "set_use_ssrm"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = 7
            goto L_0x0141
        L_0x00f4:
            java.lang.String r0 = "set_log_level"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = 17
            goto L_0x0141
        L_0x00ff:
            java.lang.String r0 = "enable_cpu_min_freq_control"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = 20
            goto L_0x0141
        L_0x010a:
            java.lang.String r0 = "set_custom_tfps_shape"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = 8
            goto L_0x0141
        L_0x0115:
            java.lang.String r0 = "enable_lrpst"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = 21
            goto L_0x0141
        L_0x0120:
            java.lang.String r0 = "set_target_lrpst"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = r3
            goto L_0x0141
        L_0x012a:
            java.lang.String r0 = "set_frame_interpolation"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = 26
            goto L_0x0141
        L_0x0135:
            java.lang.String r0 = "set_enable_cpu_min_freq_control"
            boolean r0 = r7.equals(r0)
            if (r0 == 0) goto L_0x0140
            r0 = 23
            goto L_0x0141
        L_0x0140:
            r0 = -1
        L_0x0141:
            switch(r0) {
                case 0: goto L_0x027b;
                case 1: goto L_0x0273;
                case 2: goto L_0x026b;
                case 3: goto L_0x0239;
                case 4: goto L_0x0231;
                case 5: goto L_0x0229;
                case 6: goto L_0x0221;
                case 7: goto L_0x0219;
                case 8: goto L_0x0211;
                case 9: goto L_0x0207;
                case 10: goto L_0x0202;
                case 11: goto L_0x01f9;
                case 12: goto L_0x01ef;
                case 13: goto L_0x01e2;
                case 14: goto L_0x01c5;
                case 15: goto L_0x01bc;
                case 16: goto L_0x01b3;
                case 17: goto L_0x01a6;
                case 18: goto L_0x0195;
                case 19: goto L_0x0184;
                case 20: goto L_0x017b;
                case 21: goto L_0x0172;
                case 22: goto L_0x0169;
                case 23: goto L_0x0160;
                case 24: goto L_0x0157;
                case 25: goto L_0x014e;
                case 26: goto L_0x0145;
                default: goto L_0x0144;
            }
        L_0x0144:
            return r4
        L_0x0145:
            org.json.JSONObject r6 = r6.getJSONObject(r7)
            processSpaSetFrameInterpolationCommands(r6, r8)
            goto L_0x0282
        L_0x014e:
            boolean r6 = r6.getBoolean(r7)
            r8.setEnableBusMinFreqControl(r6)
            goto L_0x0282
        L_0x0157:
            boolean r6 = r6.getBoolean(r7)
            r8.setEnableGpuMinFreqControl(r6)
            goto L_0x0282
        L_0x0160:
            boolean r6 = r6.getBoolean(r7)
            r8.setEnableCpuMinFreqControl(r6)
            goto L_0x0282
        L_0x0169:
            boolean r6 = r6.getBoolean(r7)
            r8.setEnableAnyMode(r6)
            goto L_0x0282
        L_0x0172:
            boolean r6 = r6.getBoolean(r7)
            r8.setEnableLRPST(r6)
            goto L_0x0282
        L_0x017b:
            boolean r6 = r6.getBoolean(r7)
            r8.setEnableCpuMinFreqControl(r6)
            goto L_0x0282
        L_0x0184:
            org.json.JSONArray r6 = r6.getJSONArray(r7)
            long r0 = r6.getLong(r4)
            long r6 = r6.getLong(r3)
            r8.setMinFreqs(r0, r6)
            goto L_0x0282
        L_0x0195:
            org.json.JSONArray r6 = r6.getJSONArray(r7)
            long r0 = r6.getLong(r4)
            long r6 = r6.getLong(r3)
            r8.setMaxFreqs(r0, r6)
            goto L_0x0282
        L_0x01a6:
            int r6 = r6.getInt(r7)
            com.samsung.android.game.gos.ipm.LogLevel r6 = com.samsung.android.game.gos.ipm.LogLevel.fromInt(r6)
            r8.setLogLevel(r6)
            goto L_0x0282
        L_0x01b3:
            boolean r6 = r6.getBoolean(r7)
            r8.setEnableToTGPA(r6)
            goto L_0x0282
        L_0x01bc:
            long r6 = r6.getLong(r7)
            r8.setCPUBottomFreq(r6)
            goto L_0x0282
        L_0x01c5:
            org.json.JSONArray r6 = r6.getJSONArray(r7)
            double r4 = r6.getDouble(r4)
            float r7 = (float) r4
            double r4 = r6.getDouble(r3)
            float r0 = (float) r4
            double r4 = r6.getDouble(r2)
            float r2 = (float) r4
            double r4 = r6.getDouble(r1)
            float r6 = (float) r4
            r8.setCustomProfile(r7, r0, r2, r6)
            goto L_0x0282
        L_0x01e2:
            int r6 = r6.getInt(r7)
            com.samsung.android.game.gos.ipm.Profile r6 = com.samsung.android.game.gos.ipm.Profile.fromInt(r6)
            r8.setProfile(r6)
            goto L_0x0282
        L_0x01ef:
            double r6 = r6.getDouble(r7)
            float r6 = (float) r6
            r8.setTargetFps(r6)
            goto L_0x0282
        L_0x01f9:
            boolean r6 = r6.getBoolean(r7)
            r8.setThermalControl(r6)
            goto L_0x0282
        L_0x0202:
            r8.resetFixedTargetFps()
            goto L_0x0282
        L_0x0207:
            double r6 = r6.getDouble(r7)
            float r6 = (float) r6
            r8.setFixedTargetFps(r6)
            goto L_0x0282
        L_0x0211:
            int r6 = r6.getInt(r7)
            r8.setCustomTfpsFlags(r6)
            goto L_0x0282
        L_0x0219:
            boolean r6 = r6.getBoolean(r7)
            r8.setUseSsrm(r6)
            goto L_0x0282
        L_0x0221:
            boolean r6 = r6.getBoolean(r7)
            r8.setDynamicDecisions(r6)
            goto L_0x0282
        L_0x0229:
            boolean r6 = r6.getBoolean(r7)
            r8.setEnableBusFreq(r6)
            goto L_0x0282
        L_0x0231:
            boolean r6 = r6.getBoolean(r7)
            r8.setAllowMlOff(r6)
            goto L_0x0282
        L_0x0239:
            org.json.JSONObject r0 = r6.optJSONObject(r7)
            r1 = 0
            if (r0 != 0) goto L_0x0249
            int r6 = r6.getInt(r7)
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)
            goto L_0x025f
        L_0x0249:
            java.lang.String r6 = "mode"
            int r6 = r0.optInt(r6, r4)
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)
            java.lang.String r7 = "config"
            org.json.JSONObject r7 = r0.optJSONObject(r7)
            if (r7 == 0) goto L_0x025f
            java.lang.String r1 = r7.toString()
        L_0x025f:
            int r6 = r6.intValue()
            com.samsung.android.game.gos.ipm.IntelMode r6 = com.samsung.android.game.gos.ipm.IntelMode.fromInt(r6)
            r8.setIntelMode(r6, r1)
            goto L_0x0282
        L_0x026b:
            int r6 = r6.getInt(r7)
            r8.setCpuGap(r6)
            goto L_0x0282
        L_0x0273:
            int r6 = r6.getInt(r7)
            r8.setTargetLRPST(r6)
            goto L_0x0282
        L_0x027b:
            int r6 = r6.getInt(r7)
            r8.setTargetPST(r6)
        L_0x0282:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.ipm.CommonUtil.processSpaSetCommands(org.json.JSONObject, java.lang.String, com.samsung.android.game.gos.ipm.Ipm):boolean");
    }

    private static void processSpaHandoverCommand(JSONObject jSONObject, Ipm ipm) throws JSONException {
        ipm.handOverControlToGameSDK(jSONObject.getString("package_name"), jSONObject.getBoolean("enable"));
    }

    private static void processSpaSetFrameInterpolationCommands(JSONObject jSONObject, Ipm ipm) {
        jSONObject.keys().forEachRemaining(new Consumer(jSONObject) {
            public final /* synthetic */ JSONObject f$1;

            {
                this.f$1 = r2;
            }

            public final void accept(Object obj) {
                CommonUtil.lambda$processSpaSetFrameInterpolationCommands$1(Ipm.this, this.f$1, (String) obj);
            }
        });
    }

    static /* synthetic */ void lambda$processSpaSetFrameInterpolationCommands$1(Ipm ipm, JSONObject jSONObject, String str) {
        char c = 65535;
        try {
            switch (str.hashCode()) {
                case -1609594047:
                    if (str.equals("enabled")) {
                        c = 0;
                        break;
                    }
                    break;
                case 21397827:
                    if (str.equals("decay_half_life")) {
                        c = 3;
                        break;
                    }
                    break;
                case 25907646:
                    if (str.equals("temperature_offset")) {
                        c = 1;
                        break;
                    }
                    break;
                case 805250304:
                    if (str.equals("frame_rate_offset")) {
                        c = 2;
                        break;
                    }
                    break;
            }
            if (c == 0) {
                ipm.setFrameInterpolationEnabled(jSONObject.getBoolean(str));
            } else if (c == 1) {
                ipm.setFrameInterpolationTemperatureOffset((float) jSONObject.getDouble(str));
            } else if (c == 2) {
                ipm.setFrameInterpolationFrameRateOffset((float) jSONObject.getDouble(str));
            } else if (c != 3) {
                Log.w(LOG_TAG, String.format("Unrecognized SPA frame interpolation command '%s'", new Object[]{str}));
            } else {
                ipm.setFrameInterpolationDecayHalfLife((float) jSONObject.getDouble(str));
            }
        } catch (JSONException e) {
            Log.w(LOG_TAG, String.format("Failed to parse SPA frame interpolation command '%s': %s", new Object[]{str, e.toString()}));
        }
    }

    private static JSONObject getFrameInterpolationJson(Ipm ipm) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("enabled", ipm.isFrameInterpolationEnabled());
        jSONObject.put("temperature_offset", (double) ipm.getFrameInterpolationTemperatureOffset());
        jSONObject.put("frame_rate_offset", (double) ipm.getFrameInterpolationFrameRateOffset());
        jSONObject.put("decay_half_life", (double) ipm.getFrameInterpolationDecayHalfLife());
        return jSONObject;
    }

    private static void processSpaGetCommands(JSONObject jSONObject, JSONArray jSONArray, GlobalSettings globalSettings, Ipm ipm) {
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                String string = jSONArray.getString(i);
                char c = 65535;
                switch (string.hashCode()) {
                    case -1797629851:
                        if (string.equals("custom_tfps_shape")) {
                            c = 6;
                            break;
                        }
                        break;
                    case -1692400126:
                        if (string.equals("target_temperature_limit")) {
                            c = 15;
                            break;
                        }
                        break;
                    case -1350989361:
                        if (string.equals("enable_lrpst")) {
                            c = 17;
                            break;
                        }
                        break;
                    case -1154626840:
                        if (string.equals("to_tgpa")) {
                            c = 14;
                            break;
                        }
                        break;
                    case -995672490:
                        if (string.equals("enable_cpu_min_freq_control")) {
                            c = 18;
                            break;
                        }
                        break;
                    case -982670030:
                        if (string.equals("policy")) {
                            c = 2;
                            break;
                        }
                        break;
                    case -608092698:
                        if (string.equals("max_fps_guess")) {
                            c = 8;
                            break;
                        }
                        break;
                    case -597647242:
                        if (string.equals("default_temperature")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -573446013:
                        if (string.equals("update_time")) {
                            c = 5;
                            break;
                        }
                        break;
                    case -368965201:
                        if (string.equals("usb_temp")) {
                            c = 13;
                            break;
                        }
                        break;
                    case -192521390:
                        if (string.equals("enable_any_mode")) {
                            c = 16;
                            break;
                        }
                        break;
                    case -94588637:
                        if (string.equals("statistics")) {
                            c = 11;
                            break;
                        }
                        break;
                    case 3355:
                        if (string.equals("id")) {
                            c = 7;
                            break;
                        }
                        break;
                    case 3357091:
                        if (string.equals(GfiPolicy.KEY_INTERPOLATION_MODE)) {
                            c = 4;
                            break;
                        }
                        break;
                    case 351608024:
                        if (string.equals(GosInterface.KeyName.VERSION)) {
                            c = 12;
                            break;
                        }
                        break;
                    case 1082094510:
                        if (string.equals("current_temperature")) {
                            c = 10;
                            break;
                        }
                        break;
                    case 1129534067:
                        if (string.equals("training_version")) {
                            c = 3;
                            break;
                        }
                        break;
                    case 1230928885:
                        if (string.equals("cpu_bottom_freq")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 1521271750:
                        if (string.equals("target_temperature")) {
                            c = 9;
                            break;
                        }
                        break;
                    case 2089267794:
                        if (string.equals("frame_interpolation")) {
                            c = 19;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        jSONObject.put(string, globalSettings.getDefaultTemperature());
                        break;
                    case 1:
                        jSONObject.put(string, globalSettings.getCpuBottomFrequency());
                        break;
                    case 2:
                        jSONObject.put(string, globalSettings.getPolicy());
                        break;
                    case 3:
                        jSONObject.put(string, globalSettings.getTrainingVersion());
                        break;
                    case 4:
                        jSONObject.put(string, globalSettings.getMode());
                        break;
                    case 5:
                        jSONObject.put(string, globalSettings.getUpdateTime());
                        break;
                    case 6:
                        jSONObject.put(string, ipm.getCustomTfpsFlags());
                        break;
                    case 7:
                        jSONObject.put(string, ipm.getID());
                        break;
                    case 8:
                        jSONObject.put(string, ipm.getMaxFpsGuess());
                        break;
                    case 9:
                        jSONObject.put(string, (double) ipm.getCurrentTargetTemperature());
                        break;
                    case 10:
                        jSONObject.put(string, (double) ipm.getCurrentTemperature());
                        break;
                    case 11:
                        jSONObject.put(string, ipm.getStatistics());
                        break;
                    case 12:
                        jSONObject.put(string, ipm.getVersion());
                        break;
                    case 13:
                        jSONObject.put(string, ipm.getUsbTemp());
                        break;
                    case 14:
                        jSONObject.put(string, ipm.getToTGPA());
                        break;
                    case 15:
                        jSONObject.put(string, ipm.getTargetTemperatureLimit());
                        break;
                    case 16:
                        jSONObject.put(string, ipm.getEnableAnyMode());
                        break;
                    case 17:
                        jSONObject.put(string, ipm.getEnableLRPST());
                        break;
                    case 18:
                        jSONObject.put(string, ipm.getEnableCpuMinFreqControl());
                        break;
                    case 19:
                        jSONObject.put(string, getFrameInterpolationJson(ipm));
                        break;
                    default:
                        Log.w(LOG_TAG, String.format("Unrecognized SPA get command '%s'", new Object[]{string}));
                        break;
                }
            } catch (JSONException e) {
                Log.w(LOG_TAG, String.format("Failed to parse SPA get command %d: %s", new Object[]{Integer.valueOf(i), e.toString()}));
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0028, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0031, code lost:
        throw r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] getBytes(java.io.File r4) throws java.lang.Throwable {
        /*
            if (r4 == 0) goto L_0x0039
            long r0 = r4.length()
            int r0 = (int) r0
            byte[] r1 = new byte[r0]
            java.io.BufferedInputStream r2 = new java.io.BufferedInputStream     // Catch:{ all -> 0x0032 }
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ all -> 0x0032 }
            r3.<init>(r4)     // Catch:{ all -> 0x0032 }
            r2.<init>(r3)     // Catch:{ all -> 0x0032 }
            r4 = 0
            int r4 = r2.read(r1, r4, r0)     // Catch:{ all -> 0x0026 }
            if (r4 < 0) goto L_0x001e
            r2.close()     // Catch:{ all -> 0x0032 }
            goto L_0x003a
        L_0x001e:
            java.io.IOException r4 = new java.io.IOException     // Catch:{ all -> 0x0026 }
            java.lang.String r0 = "getBytes()-End of stream"
            r4.<init>(r0)     // Catch:{ all -> 0x0026 }
            throw r4     // Catch:{ all -> 0x0026 }
        L_0x0026:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x0028 }
        L_0x0028:
            r0 = move-exception
            r2.close()     // Catch:{ all -> 0x002d }
            goto L_0x0031
        L_0x002d:
            r1 = move-exception
            r4.addSuppressed(r1)     // Catch:{ all -> 0x0032 }
        L_0x0031:
            throw r0     // Catch:{ all -> 0x0032 }
        L_0x0032:
            r4 = move-exception
            java.lang.String r0 = "CommonUtil"
            com.samsung.android.game.gos.ipm.Log.w((java.lang.String) r0, (java.lang.Throwable) r4)
            throw r4
        L_0x0039:
            r1 = 0
        L_0x003a:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.ipm.CommonUtil.getBytes(java.io.File):byte[]");
    }

    private static byte[] compressBytes(byte[] bArr) throws IOException {
        if (bArr == null) {
            return null;
        }
        Deflater deflater = new Deflater(9);
        deflater.setInput(bArr);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bArr.length);
        deflater.finish();
        byte[] bArr2 = new byte[1024];
        while (!deflater.finished()) {
            byteArrayOutputStream.write(bArr2, 0, deflater.deflate(bArr2));
        }
        deflater.end();
        byteArrayOutputStream.close();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        Log.i(LOG_TAG, "compressByte(), Original: " + (bArr.length / 1024) + " KB, Compressed: " + (byteArray.length / 1024) + " KB");
        return byteArray;
    }
}
