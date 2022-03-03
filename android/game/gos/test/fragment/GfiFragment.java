package com.samsung.android.game.gos.test.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.samsung.android.game.gos.R;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.PackageDbHelper;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.data.model.State;
import com.samsung.android.game.gos.feature.gfi.GfiFeature;
import com.samsung.android.game.gos.feature.gfi.value.GfiPolicy;
import com.samsung.android.game.gos.feature.gfi.value.GfiSettings;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.test.util.GosTestLog;
import com.samsung.android.game.gos.value.Constants;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

public class GfiFragment extends BaseFragment {
    private static final String LOG_TAG = "GfiFragment";
    private EditText editText_DFSMaximum;
    private EditText editText_DFSOffset;
    private EditText editText_GFPSOffset;
    private EditText editText_GfiDFS;
    private EditText editText_GfiFPS;
    private EditText editText_GfiMaxVersion;
    private EditText editText_GfiMinVersion;
    private EditText editText_MinimumGFPS;
    private EditText editText_WatchdogExpire;
    private EditText editText_maxConsecGL;
    private EditText editText_minAcceptableFps;
    private LinearLayout linearLayout_DFS;
    private LinearLayout linearLayout_DFS_settings;
    private LinearLayout linearLayout_GFPS;
    private LinearLayout linearLayout_GFPS_settings;
    private String mCurrentPkgName = null;
    private ArrayList<String> mInstalledPackageNameArray;
    /* access modifiers changed from: private */
    public PkgIconAndTextArrayAdapter mInstalledPackageNameArrayAdapter;
    private RadioButton radioButton_FillinMode;
    private RadioButton radioButton_LatencyReductionMode;
    private RadioButton radioButton_MLLatencyReductionMode;
    private RadioButton radioButton_NoneMode;
    private RadioButton radioButton_RetimeMode;
    private RadioButton radioButton_UnlimitedMode;
    /* access modifiers changed from: private */
    public RadioGroup radioGroup_interpolationMode;
    /* access modifiers changed from: private */
    public RadioGroup radioGroup_interpolationModeDataCollection;
    private SeekBar seekBar_DFSSmoothness;
    private SeekBar seekBar_maxPercentGL;
    private SeekBar seekBar_regalStability;
    private Spinner spinner_GfiLogLevel;
    private Spinner spinner_targetPkgData;
    private Switch switch_ApplyWatchdogMaxFpsLimit;
    private Switch switch_EnableDFSOffset;
    private Switch switch_EnableExternalControl;
    private Switch switch_EnableFBRecording;
    private Switch switch_EnableGFPSOffset;
    private Switch switch_EnableSmartDelay;
    private Switch switch_GfiEnabled;
    private Switch switch_KeepTwoHwcLayers;
    private Switch switch_WriteToFrameTracker;
    private Switch switch_noInterpWithExtraLayers;
    /* access modifiers changed from: private */
    public TextView textView_DFSSmoothness;
    private TextView textView_GfiVersion;
    /* access modifiers changed from: private */
    public TextView textView_maxPercentGL;
    /* access modifiers changed from: private */
    public TextView textView_regalStability;

    public int getNavItemId() {
        return R.id.nav_gfi;
    }

    /* access modifiers changed from: private */
    public void setFPSControlVisibility(int i) {
        this.linearLayout_GFPS.setVisibility(i);
        this.linearLayout_DFS.setVisibility(i);
        this.linearLayout_DFS_settings.setVisibility(i);
        this.linearLayout_GFPS_settings.setVisibility(i);
    }

    private String getGfiPolicyString(Package packageR) {
        String str = "{}";
        if (packageR != null) {
            String gfiPolicy = packageR.getGfiPolicy();
            if (gfiPolicy == null) {
                packageR.setGfiPolicy(str);
            } else {
                str = gfiPolicy;
            }
            DbHelper.getInstance().getPackageDao().insertOrUpdate(packageR);
        } else {
            GosTestLog.w(LOG_TAG, "getGfiPolicyString(): pkg is null");
        }
        return str;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:16|17) */
    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        r1 = com.samsung.android.game.gos.feature.gfi.value.GfiPolicy.GfiMode.RETIME;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:16:0x00fd */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onPackageSelected(java.lang.String r14) {
        /*
            r13 = this;
            java.lang.String r0 = "mode"
            java.lang.String r1 = "target_dfs"
            java.lang.String r2 = "gfps_offset"
            java.lang.String r3 = "dfs_offset"
            java.lang.String r4 = ""
            java.lang.String r5 = "enabled"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "onPackageSelected(): "
            r6.append(r7)
            r6.append(r14)
            java.lang.String r6 = r6.toString()
            java.lang.String r7 = "GfiFragment"
            com.samsung.android.game.gos.test.util.GosTestLog.d(r7, r6)
            r6 = 0
            r13.mCurrentPkgName = r6
            com.samsung.android.game.gos.data.dbhelper.DbHelper r8 = com.samsung.android.game.gos.data.dbhelper.DbHelper.getInstance()
            com.samsung.android.game.gos.data.dao.PackageDao r8 = r8.getPackageDao()
            com.samsung.android.game.gos.data.model.Package r8 = r8.getPackage(r14)
            java.lang.String r9 = r13.getGfiPolicyString(r8)
            org.json.JSONObject r10 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0241 }
            r10.<init>(r9)     // Catch:{ JSONException -> 0x0241 }
            boolean r9 = r10.has(r3)     // Catch:{ JSONException -> 0x0241 }
            if (r9 == 0) goto L_0x0045
            org.json.JSONObject r3 = r10.getJSONObject(r3)     // Catch:{ JSONException -> 0x0241 }
            goto L_0x0046
        L_0x0045:
            r3 = r6
        L_0x0046:
            boolean r9 = r10.has(r2)     // Catch:{ JSONException -> 0x0241 }
            if (r9 == 0) goto L_0x0050
            org.json.JSONObject r6 = r10.getJSONObject(r2)     // Catch:{ JSONException -> 0x0241 }
        L_0x0050:
            r2 = 0
            boolean r9 = r10.optBoolean(r5, r2)     // Catch:{ JSONException -> 0x0241 }
            android.widget.Switch r11 = r13.switch_GfiEnabled     // Catch:{ JSONException -> 0x0241 }
            r11.setChecked(r9)     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r9 = "gfi_minimum_version"
            java.lang.String r11 = "1.0.0"
            java.lang.String r9 = r10.optString(r9, r11)     // Catch:{ JSONException -> 0x0241 }
            android.widget.EditText r11 = r13.editText_GfiMinVersion     // Catch:{ JSONException -> 0x0241 }
            r11.setText(r9)     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r9 = "gfi_maximum_version"
            java.lang.String r11 = "999.999.99999"
            java.lang.String r9 = r10.optString(r9, r11)     // Catch:{ JSONException -> 0x0241 }
            android.widget.EditText r11 = r13.editText_GfiMaxVersion     // Catch:{ JSONException -> 0x0241 }
            r11.setText(r9)     // Catch:{ JSONException -> 0x0241 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x0241 }
            r9.<init>()     // Catch:{ JSONException -> 0x0241 }
            r9.append(r4)     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r11 = "game_fps_limit"
            int r11 = r10.optInt(r11, r2)     // Catch:{ JSONException -> 0x0241 }
            r9.append(r11)     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r9 = r9.toString()     // Catch:{ JSONException -> 0x0241 }
            android.widget.EditText r11 = r13.editText_GfiFPS     // Catch:{ JSONException -> 0x0241 }
            r11.setText(r9)     // Catch:{ JSONException -> 0x0241 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x0241 }
            r9.<init>()     // Catch:{ JSONException -> 0x0241 }
            r9.append(r4)     // Catch:{ JSONException -> 0x0241 }
            float r8 = com.samsung.android.game.gos.feature.dfs.DfsFeature.getDefaultDfs(r8)     // Catch:{ JSONException -> 0x0241 }
            r9.append(r8)     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r8 = r9.toString()     // Catch:{ JSONException -> 0x0241 }
            boolean r9 = r10.has(r1)     // Catch:{ JSONException -> 0x0241 }
            if (r9 == 0) goto L_0x00ba
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x0241 }
            r8.<init>()     // Catch:{ JSONException -> 0x0241 }
            r8.append(r4)     // Catch:{ JSONException -> 0x0241 }
            java.lang.Object r1 = r10.get(r1)     // Catch:{ JSONException -> 0x0241 }
            r8.append(r1)     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r8 = r8.toString()     // Catch:{ JSONException -> 0x0241 }
        L_0x00ba:
            android.widget.EditText r1 = r13.editText_GfiDFS     // Catch:{ JSONException -> 0x0241 }
            r1.setText(r8)     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r1 = "smart_delay"
            r4 = 1
            boolean r1 = r10.optBoolean(r1, r4)     // Catch:{ JSONException -> 0x0241 }
            android.widget.Switch r8 = r13.switch_EnableSmartDelay     // Catch:{ JSONException -> 0x0241 }
            r8.setChecked(r1)     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r1 = "write_to_frametracker"
            boolean r1 = r10.optBoolean(r1, r4)     // Catch:{ JSONException -> 0x0241 }
            android.widget.Switch r8 = r13.switch_WriteToFrameTracker     // Catch:{ JSONException -> 0x0241 }
            r8.setChecked(r1)     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r1 = "no_interp_with_extra_layers"
            boolean r1 = r10.optBoolean(r1, r2)     // Catch:{ JSONException -> 0x0241 }
            android.widget.Switch r8 = r13.switch_noInterpWithExtraLayers     // Catch:{ JSONException -> 0x0241 }
            r8.setChecked(r1)     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r1 = "allow_external_control"
            boolean r1 = r10.optBoolean(r1, r2)     // Catch:{ JSONException -> 0x0241 }
            android.widget.Switch r8 = r13.switch_EnableExternalControl     // Catch:{ JSONException -> 0x0241 }
            r8.setChecked(r1)     // Catch:{ JSONException -> 0x0241 }
            com.samsung.android.game.gos.feature.gfi.value.GfiPolicy$GfiMode r1 = com.samsung.android.game.gos.feature.gfi.value.GfiPolicy.DEFAULT_INTERPOLATION_MODE     // Catch:{ JSONException -> 0x0241 }
            boolean r8 = r10.has(r0)     // Catch:{ JSONException -> 0x0241 }
            if (r8 == 0) goto L_0x00ff
            java.lang.String r0 = r10.getString(r0)     // Catch:{ Exception -> 0x00fd }
            com.samsung.android.game.gos.feature.gfi.value.GfiPolicy$GfiMode r1 = com.samsung.android.game.gos.feature.gfi.value.GfiPolicy.GfiMode.fromString(r0)     // Catch:{ Exception -> 0x00fd }
            goto L_0x00ff
        L_0x00fd:
            com.samsung.android.game.gos.feature.gfi.value.GfiPolicy$GfiMode r1 = com.samsung.android.game.gos.feature.gfi.value.GfiPolicy.GfiMode.RETIME     // Catch:{ JSONException -> 0x0241 }
        L_0x00ff:
            android.widget.RadioGroup r0 = r13.radioGroup_interpolationMode     // Catch:{ JSONException -> 0x0241 }
            r0.clearCheck()     // Catch:{ JSONException -> 0x0241 }
            android.widget.RadioGroup r0 = r13.radioGroup_interpolationModeDataCollection     // Catch:{ JSONException -> 0x0241 }
            r0.clearCheck()     // Catch:{ JSONException -> 0x0241 }
            int[] r0 = com.samsung.android.game.gos.test.fragment.GfiFragment.AnonymousClass24.$SwitchMap$com$samsung$android$game$gos$feature$gfi$value$GfiPolicy$GfiMode     // Catch:{ JSONException -> 0x0241 }
            int r1 = r1.ordinal()     // Catch:{ JSONException -> 0x0241 }
            r0 = r0[r1]     // Catch:{ JSONException -> 0x0241 }
            if (r0 == r4) goto L_0x0144
            r1 = 2
            if (r0 == r1) goto L_0x013b
            r1 = 3
            if (r0 == r1) goto L_0x0132
            r1 = 4
            if (r0 == r1) goto L_0x0129
            r1 = 5
            if (r0 == r1) goto L_0x0120
            goto L_0x014c
        L_0x0120:
            android.widget.RadioButton r0 = r13.radioButton_UnlimitedMode     // Catch:{ JSONException -> 0x0241 }
            r0.setChecked(r4)     // Catch:{ JSONException -> 0x0241 }
            r13.setFPSControlVisibility(r2)     // Catch:{ JSONException -> 0x0241 }
            goto L_0x014c
        L_0x0129:
            android.widget.RadioButton r0 = r13.radioButton_LatencyReductionMode     // Catch:{ JSONException -> 0x0241 }
            r0.setChecked(r4)     // Catch:{ JSONException -> 0x0241 }
            r13.setFPSControlVisibility(r1)     // Catch:{ JSONException -> 0x0241 }
            goto L_0x014c
        L_0x0132:
            android.widget.RadioButton r0 = r13.radioButton_NoneMode     // Catch:{ JSONException -> 0x0241 }
            r0.setChecked(r4)     // Catch:{ JSONException -> 0x0241 }
            r13.setFPSControlVisibility(r2)     // Catch:{ JSONException -> 0x0241 }
            goto L_0x014c
        L_0x013b:
            android.widget.RadioButton r0 = r13.radioButton_FillinMode     // Catch:{ JSONException -> 0x0241 }
            r0.setChecked(r4)     // Catch:{ JSONException -> 0x0241 }
            r13.setFPSControlVisibility(r2)     // Catch:{ JSONException -> 0x0241 }
            goto L_0x014c
        L_0x0144:
            android.widget.RadioButton r0 = r13.radioButton_RetimeMode     // Catch:{ JSONException -> 0x0241 }
            r0.setChecked(r4)     // Catch:{ JSONException -> 0x0241 }
            r13.setFPSControlVisibility(r2)     // Catch:{ JSONException -> 0x0241 }
        L_0x014c:
            java.lang.String r0 = "value"
            r8 = 4636737291354636288(0x4059000000000000, double:100.0)
            if (r3 == 0) goto L_0x01a9
            r1 = 10
            int r1 = r3.optInt(r0, r1)     // Catch:{ JSONException -> 0x0241 }
            android.widget.EditText r4 = r13.editText_DFSOffset     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r1 = java.lang.String.valueOf(r1)     // Catch:{ JSONException -> 0x0241 }
            r4.setText(r1)     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r1 = "maximum"
            r4 = 60
            int r1 = r3.optInt(r1, r4)     // Catch:{ JSONException -> 0x0241 }
            android.widget.EditText r4 = r13.editText_DFSMaximum     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r1 = java.lang.String.valueOf(r1)     // Catch:{ JSONException -> 0x0241 }
            r4.setText(r1)     // Catch:{ JSONException -> 0x0241 }
            boolean r1 = r3.optBoolean(r5, r2)     // Catch:{ JSONException -> 0x0241 }
            android.widget.Switch r4 = r13.switch_EnableDFSOffset     // Catch:{ JSONException -> 0x0241 }
            r4.setChecked(r1)     // Catch:{ JSONException -> 0x0241 }
            android.widget.EditText r4 = r13.editText_DFSOffset     // Catch:{ JSONException -> 0x0241 }
            r4.setEnabled(r1)     // Catch:{ JSONException -> 0x0241 }
            android.widget.EditText r4 = r13.editText_DFSMaximum     // Catch:{ JSONException -> 0x0241 }
            r4.setEnabled(r1)     // Catch:{ JSONException -> 0x0241 }
            android.widget.SeekBar r4 = r13.seekBar_DFSSmoothness     // Catch:{ JSONException -> 0x0241 }
            r4.setEnabled(r1)     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r1 = "smoothness"
            r11 = 4605380978949069210(0x3fe999999999999a, double:0.8)
            double r3 = r3.optDouble(r1, r11)     // Catch:{ JSONException -> 0x0241 }
            android.widget.TextView r1 = r13.textView_DFSSmoothness     // Catch:{ JSONException -> 0x0241 }
            java.lang.Double r11 = java.lang.Double.valueOf(r3)     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r11 = java.lang.String.valueOf(r11)     // Catch:{ JSONException -> 0x0241 }
            r1.setText(r11)     // Catch:{ JSONException -> 0x0241 }
            android.widget.SeekBar r1 = r13.seekBar_DFSSmoothness     // Catch:{ JSONException -> 0x0241 }
            double r3 = r3 * r8
            int r3 = (int) r3     // Catch:{ JSONException -> 0x0241 }
            r1.setProgress(r3)     // Catch:{ JSONException -> 0x0241 }
        L_0x01a9:
            if (r6 == 0) goto L_0x01dc
            boolean r1 = r6.optBoolean(r5, r2)     // Catch:{ JSONException -> 0x0241 }
            android.widget.Switch r3 = r13.switch_EnableGFPSOffset     // Catch:{ JSONException -> 0x0241 }
            r3.setChecked(r1)     // Catch:{ JSONException -> 0x0241 }
            android.widget.EditText r3 = r13.editText_GFPSOffset     // Catch:{ JSONException -> 0x0241 }
            r3.setEnabled(r1)     // Catch:{ JSONException -> 0x0241 }
            android.widget.EditText r3 = r13.editText_MinimumGFPS     // Catch:{ JSONException -> 0x0241 }
            r3.setEnabled(r1)     // Catch:{ JSONException -> 0x0241 }
            int r0 = r6.optInt(r0, r2)     // Catch:{ JSONException -> 0x0241 }
            android.widget.EditText r1 = r13.editText_GFPSOffset     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ JSONException -> 0x0241 }
            r1.setText(r0)     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r0 = "minimum"
            r1 = 15
            int r0 = r6.optInt(r0, r1)     // Catch:{ JSONException -> 0x0241 }
            android.widget.EditText r1 = r13.editText_MinimumGFPS     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ JSONException -> 0x0241 }
            r1.setText(r0)     // Catch:{ JSONException -> 0x0241 }
        L_0x01dc:
            java.lang.String r0 = "minimum_regal_stability"
            r3 = 4606281698874543309(0x3feccccccccccccd, double:0.9)
            double r0 = r10.optDouble(r0, r3)     // Catch:{ JSONException -> 0x0241 }
            android.widget.TextView r3 = r13.textView_regalStability     // Catch:{ JSONException -> 0x0241 }
            java.lang.Double r4 = java.lang.Double.valueOf(r0)     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r4 = java.lang.String.valueOf(r4)     // Catch:{ JSONException -> 0x0241 }
            r3.setText(r4)     // Catch:{ JSONException -> 0x0241 }
            android.widget.SeekBar r3 = r13.seekBar_regalStability     // Catch:{ JSONException -> 0x0241 }
            double r0 = r0 * r8
            int r0 = (int) r0     // Catch:{ JSONException -> 0x0241 }
            r3.setProgress(r0)     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r0 = "max_consecutive_gl_compositions"
            r1 = 600(0x258, float:8.41E-43)
            int r0 = r10.optInt(r0, r1)     // Catch:{ JSONException -> 0x0241 }
            android.widget.EditText r1 = r13.editText_maxConsecGL     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ JSONException -> 0x0241 }
            r1.setText(r0)     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r0 = "max_percent_gl_compositions"
            r3 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r0 = r10.optDouble(r0, r3)     // Catch:{ JSONException -> 0x0241 }
            android.widget.TextView r3 = r13.textView_maxPercentGL     // Catch:{ JSONException -> 0x0241 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x0241 }
            r4.<init>()     // Catch:{ JSONException -> 0x0241 }
            double r0 = r0 * r8
            int r0 = (int) r0     // Catch:{ JSONException -> 0x0241 }
            r4.append(r0)     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r1 = "%"
            r4.append(r1)     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r1 = r4.toString()     // Catch:{ JSONException -> 0x0241 }
            r3.setText(r1)     // Catch:{ JSONException -> 0x0241 }
            android.widget.SeekBar r1 = r13.seekBar_maxPercentGL     // Catch:{ JSONException -> 0x0241 }
            r1.setProgress(r0)     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r0 = "min_acceptable_fps"
            int r0 = r10.optInt(r0, r2)     // Catch:{ JSONException -> 0x0241 }
            android.widget.EditText r1 = r13.editText_minAcceptableFps     // Catch:{ JSONException -> 0x0241 }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ JSONException -> 0x0241 }
            r1.setText(r0)     // Catch:{ JSONException -> 0x0241 }
            goto L_0x0246
        L_0x0241:
            java.lang.String r0 = "onPackageSelected(): error reading JSON"
            com.samsung.android.game.gos.test.util.GosTestLog.d(r7, r0)
        L_0x0246:
            r13.mCurrentPkgName = r14
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.test.fragment.GfiFragment.onPackageSelected(java.lang.String):void");
    }

    /* renamed from: com.samsung.android.game.gos.test.fragment.GfiFragment$24  reason: invalid class name */
    static /* synthetic */ class AnonymousClass24 {
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$game$gos$feature$gfi$value$GfiPolicy$GfiMode;

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.samsung.android.game.gos.feature.gfi.value.GfiPolicy$GfiMode[] r0 = com.samsung.android.game.gos.feature.gfi.value.GfiPolicy.GfiMode.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$samsung$android$game$gos$feature$gfi$value$GfiPolicy$GfiMode = r0
                com.samsung.android.game.gos.feature.gfi.value.GfiPolicy$GfiMode r1 = com.samsung.android.game.gos.feature.gfi.value.GfiPolicy.GfiMode.RETIME     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$feature$gfi$value$GfiPolicy$GfiMode     // Catch:{ NoSuchFieldError -> 0x001d }
                com.samsung.android.game.gos.feature.gfi.value.GfiPolicy$GfiMode r1 = com.samsung.android.game.gos.feature.gfi.value.GfiPolicy.GfiMode.FILLIN     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$feature$gfi$value$GfiPolicy$GfiMode     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.samsung.android.game.gos.feature.gfi.value.GfiPolicy$GfiMode r1 = com.samsung.android.game.gos.feature.gfi.value.GfiPolicy.GfiMode.NONE     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$feature$gfi$value$GfiPolicy$GfiMode     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.samsung.android.game.gos.feature.gfi.value.GfiPolicy$GfiMode r1 = com.samsung.android.game.gos.feature.gfi.value.GfiPolicy.GfiMode.LATENCY_REDUCTION     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$feature$gfi$value$GfiPolicy$GfiMode     // Catch:{ NoSuchFieldError -> 0x003e }
                com.samsung.android.game.gos.feature.gfi.value.GfiPolicy$GfiMode r1 = com.samsung.android.game.gos.feature.gfi.value.GfiPolicy.GfiMode.UNLIMITED     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.test.fragment.GfiFragment.AnonymousClass24.<clinit>():void");
        }
    }

    private JSONObject getCurrentPolicyJson() throws JSONException, NullPointerException {
        Package packageR = DbHelper.getInstance().getPackageDao().getPackage(this.mCurrentPkgName);
        if (packageR != null) {
            return packageR.gfiPolicy != null ? new JSONObject(packageR.gfiPolicy) : new JSONObject();
        }
        GosTestLog.w(LOG_TAG, "No PkgData for " + this.mCurrentPkgName);
        return new JSONObject();
    }

    /* access modifiers changed from: private */
    public <T> void updatePolicy(String str, T t) {
        String str2 = "enabled";
        if (this.mCurrentPkgName == null || DbHelper.getInstance().getPackageDao().getPackage(this.mCurrentPkgName) == null) {
            GosTestLog.d(LOG_TAG, "updatePolicy() No current package");
            return;
        }
        GosTestLog.d(LOG_TAG, "updatePolicy() " + str + ":" + t);
        try {
            Package packageR = DbHelper.getInstance().getPackageDao().getPackage(this.mCurrentPkgName);
            JSONObject currentPolicyJson = getCurrentPolicyJson();
            if (str2.equals(str)) {
                if (!(t instanceof Boolean)) {
                    str2 = State.DISABLED;
                }
                PackageDbHelper.getInstance().setServerPolicy(Constants.V4FeatureFlag.GFI, this.mCurrentPkgName, str2);
                GosTestLog.d(LOG_TAG, "updatePolicy change state: " + str2);
            }
            currentPolicyJson.put(str, t);
            packageR.gfiPolicy = currentPolicyJson.toString();
            GosTestLog.d(LOG_TAG, "updatePolicy result: " + currentPolicyJson.toString());
            DbHelper.getInstance().getPackageDao().insertOrUpdate(packageR);
        } catch (JSONException e) {
            GosTestLog.e(LOG_TAG, "updatePolicy() JSON manipulation error: " + e);
        } catch (NullPointerException e2) {
            GosTestLog.e(LOG_TAG, "updatePolicy() null pointer error: " + e2);
        }
    }

    /* access modifiers changed from: private */
    public <T> void updatePolicyNested(String str, String str2, T t) {
        if (this.mCurrentPkgName == null || DbHelper.getInstance().getPackageDao().getPackage(this.mCurrentPkgName) == null) {
            GosTestLog.d(LOG_TAG, "updatePolicyNested() No current package");
            return;
        }
        try {
            JSONObject currentPolicyJson = getCurrentPolicyJson();
            JSONObject jSONObject = currentPolicyJson.has(str) ? currentPolicyJson.getJSONObject(str) : new JSONObject();
            jSONObject.put(str2, t);
            updatePolicy(str, jSONObject);
        } catch (JSONException e) {
            GosTestLog.d(LOG_TAG, "updatePolicyNested() JSON manipulation error: " + e);
        } catch (NullPointerException e2) {
            GosTestLog.e(LOG_TAG, "updatePolicyNested() null pointer error: " + e2);
        }
    }

    /* access modifiers changed from: private */
    public void clearPolicy(String str) {
        GosTestLog.d(LOG_TAG, "clearPolicy() " + str);
        if (this.mCurrentPkgName != null && DbHelper.getInstance().getPackageDao().getPackage(this.mCurrentPkgName) != null) {
            try {
                Package packageR = DbHelper.getInstance().getPackageDao().getPackage(this.mCurrentPkgName);
                JSONObject jSONObject = new JSONObject(packageR.gfiPolicy);
                jSONObject.remove(str);
                packageR.gfiPolicy = jSONObject.toString();
                GosTestLog.d(LOG_TAG, "clearPolicy: " + jSONObject.toString());
                DbHelper.getInstance().getPackageDao().insertOrUpdate(packageR);
            } catch (JSONException unused) {
                GosTestLog.d(LOG_TAG, "clearPolicy() JSON manipulation error");
            }
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        GosTestLog.d(LOG_TAG, "onCreateView");
        View inflate = layoutInflater.inflate(R.layout.fragment_gfi, viewGroup, false);
        init(inflate);
        return inflate;
    }

    private void init(View view) {
        GosTestLog.d(LOG_TAG, "init");
        if (view == null) {
            GosTestLog.d(LOG_TAG, "rootView is null");
            return;
        }
        TextView textView = (TextView) view.findViewById(R.id.textView_gfiVersion);
        this.textView_GfiVersion = textView;
        textView.setText(GfiFeature.getInstance(AppContext.get()).getVersionString());
        this.spinner_GfiLogLevel = (Spinner) view.findViewById(R.id.spinner_GfiLogLevel);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), 17367048, new CharSequence[]{"Release", "Debug", "Verbose"});
        arrayAdapter.setDropDownViewResource(17367050);
        this.spinner_GfiLogLevel.setAdapter(arrayAdapter);
        this.spinner_GfiLogLevel.setSelection(GfiFeature.getInstance(AppContext.get()).getLogLevel().level);
        this.spinner_GfiLogLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                GfiFeature.getInstance(AppContext.get()).setLogLevel(GfiFeature.LogLevel.fromInteger(i));
            }
        });
        Switch switchR = (Switch) view.findViewById(R.id.switch_KeepTwoHwcLayers);
        this.switch_KeepTwoHwcLayers = switchR;
        switchR.setChecked(GfiFeature.getInstance(AppContext.get()).getKeepTwoHwcLayers());
        this.switch_KeepTwoHwcLayers.setOnCheckedChangeListener($$Lambda$GfiFragment$V2xkMzrJms6oNGtYvU_Cdy9PeU.INSTANCE);
        this.switch_ApplyWatchdogMaxFpsLimit = (Switch) view.findViewById(R.id.switch_ApplyWatchdogMaxFpsLimit);
        if (GfiFeature.getInstance(AppContext.get()).isFrameCorruptionIssueChipset()) {
            this.switch_ApplyWatchdogMaxFpsLimit.setChecked(GfiFeature.getInstance(AppContext.get()).mApplyWatchdogMaxFpsLimit);
            this.switch_ApplyWatchdogMaxFpsLimit.setOnCheckedChangeListener($$Lambda$GfiFragment$sq4tYvyQR6ao14lQFGGFp9i8F4c.INSTANCE);
        } else {
            this.switch_ApplyWatchdogMaxFpsLimit.setChecked(false);
            this.switch_ApplyWatchdogMaxFpsLimit.setEnabled(false);
        }
        this.mInstalledPackageNameArray = new ArrayList<>();
        this.mInstalledPackageNameArrayAdapter = new PkgIconAndTextArrayAdapter(getActivity(), R.layout.row_icon_text, new ArrayList());
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_selectPkg);
        this.spinner_targetPkgData = spinner;
        spinner.setAdapter(this.mInstalledPackageNameArrayAdapter);
        this.spinner_targetPkgData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                String str = (String) GfiFragment.this.mInstalledPackageNameArrayAdapter.getItem(i);
                GosTestLog.d(GfiFragment.LOG_TAG, "onItemSelected(), position: " + i + ", pkgName: " + str);
                GfiFragment.this.onPackageSelected(str);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                GosTestLog.d(GfiFragment.LOG_TAG, "onNothingSelected()");
            }
        });
        Switch switchR2 = (Switch) view.findViewById(R.id.switch_enable_gfi);
        this.switch_GfiEnabled = switchR2;
        switchR2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                GfiFragment.this.lambda$init$2$GfiFragment(compoundButton, z);
            }
        });
        EditText editText = (EditText) view.findViewById(R.id.edittext_gfi_min_version);
        this.editText_GfiMinVersion = editText;
        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                GfiFragment.this.updatePolicy(GfiPolicy.KEY_MINIMUM_VERSION, charSequence.toString());
            }
        });
        EditText editText2 = (EditText) view.findViewById(R.id.edittext_gfi_max_version);
        this.editText_GfiMaxVersion = editText2;
        editText2.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                GfiFragment.this.updatePolicy(GfiPolicy.KEY_MAXIMUM_VERSION, charSequence.toString());
            }
        });
        EditText editText3 = (EditText) view.findViewById(R.id.edittext_gfps);
        this.editText_GfiFPS = editText3;
        editText3.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                try {
                    GfiFragment.this.updatePolicy(GfiPolicy.KEY_INTERPOLATION_FPS, Integer.valueOf(Integer.parseInt(charSequence.toString())));
                } catch (NumberFormatException unused) {
                    GosTestLog.d(GfiFragment.LOG_TAG, "onTextChanged(): text not integer");
                }
            }
        });
        EditText editText4 = (EditText) view.findViewById(R.id.edittext_gfi_dfs);
        this.editText_GfiDFS = editText4;
        editText4.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                try {
                    if (charSequence.length() == 0) {
                        GfiFragment.this.clearPolicy(GfiPolicy.KEY_TARGET_DFS);
                        return;
                    }
                    float parseFloat = Float.parseFloat(charSequence.toString());
                    if (parseFloat < 0.5f) {
                        GfiFragment.this.clearPolicy(GfiPolicy.KEY_TARGET_DFS);
                    } else {
                        GfiFragment.this.updatePolicy(GfiPolicy.KEY_TARGET_DFS, Float.valueOf(parseFloat));
                    }
                } catch (NumberFormatException unused) {
                    GosTestLog.d(GfiFragment.LOG_TAG, "onTextChanged(): text not integer");
                }
            }
        });
        Switch switchR3 = (Switch) view.findViewById(R.id.switch_enable_smart_delay);
        this.switch_EnableSmartDelay = switchR3;
        switchR3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                GfiFragment.this.lambda$init$3$GfiFragment(compoundButton, z);
            }
        });
        Switch switchR4 = (Switch) view.findViewById(R.id.switch_writeToFrameTracker);
        this.switch_WriteToFrameTracker = switchR4;
        switchR4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                GfiFragment.this.lambda$init$4$GfiFragment(compoundButton, z);
            }
        });
        Switch switchR5 = (Switch) view.findViewById(R.id.switch_enableExternalControl);
        this.switch_EnableExternalControl = switchR5;
        switchR5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                GfiFragment.this.lambda$init$5$GfiFragment(compoundButton, z);
            }
        });
        Switch switchR6 = (Switch) view.findViewById(R.id.switch_noInterpWithExtraLayers);
        this.switch_noInterpWithExtraLayers = switchR6;
        switchR6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                GfiFragment.this.lambda$init$6$GfiFragment(compoundButton, z);
            }
        });
        this.editText_WatchdogExpire = (EditText) view.findViewById(R.id.eddittext_watchdog_expire);
        this.editText_WatchdogExpire.setText(BuildConfig.VERSION_NAME + GfiSettings.getInstance(AppContext.get()).getWatchdogExpireDuration());
        this.editText_WatchdogExpire.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                try {
                    GfiSettings.getInstance(AppContext.get()).setWatchdogExpireDuration(Integer.parseInt(charSequence.toString()));
                } catch (NumberFormatException unused) {
                    GfiSettings.getInstance(AppContext.get()).setWatchdogExpireDuration(0);
                    GosTestLog.d(GfiFragment.LOG_TAG, "onTextChanged(): text not integer");
                }
            }
        });
        Switch switchR7 = (Switch) view.findViewById(R.id.switch_enableFBRecording);
        this.switch_EnableFBRecording = switchR7;
        switchR7.setChecked(GfiSettings.getInstance(AppContext.get()).isSessionRecordingEnabled());
        this.switch_EnableFBRecording.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                GfiSettings.getInstance(AppContext.get()).setSessionRecordingEnabled(z);
            }
        });
        this.radioGroup_interpolationMode = (RadioGroup) view.findViewById(R.id.radiogroup_interpolation_mode);
        this.radioGroup_interpolationModeDataCollection = (RadioGroup) view.findViewById(R.id.radiogroup_interpolation_data_collection_mode);
        RadioButton radioButton = (RadioButton) view.findViewById(R.id.radiobutton_mode_retime);
        this.radioButton_RetimeMode = radioButton;
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    GfiFragment.this.setFPSControlVisibility(0);
                    GfiFragment.this.radioGroup_interpolationModeDataCollection.clearCheck();
                    GfiFragment.this.updatePolicy(GfiPolicy.KEY_INTERPOLATION_MODE, "retime");
                }
            }
        });
        RadioButton radioButton2 = (RadioButton) view.findViewById(R.id.radiobutton_mode_fillin);
        this.radioButton_FillinMode = radioButton2;
        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    GfiFragment.this.setFPSControlVisibility(0);
                    GfiFragment.this.radioGroup_interpolationModeDataCollection.clearCheck();
                    GfiFragment.this.updatePolicy(GfiPolicy.KEY_INTERPOLATION_MODE, "fillin");
                }
            }
        });
        RadioButton radioButton3 = (RadioButton) view.findViewById(R.id.radiobutton_mode_latency_reduction);
        this.radioButton_LatencyReductionMode = radioButton3;
        radioButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    GfiFragment.this.setFPSControlVisibility(4);
                    GfiFragment.this.radioGroup_interpolationModeDataCollection.clearCheck();
                    GfiFragment.this.updatePolicy(GfiPolicy.KEY_INTERPOLATION_MODE, "latred");
                }
            }
        });
        RadioButton radioButton4 = (RadioButton) view.findViewById(R.id.radiobutton_mode_ml_latency_reduction);
        this.radioButton_MLLatencyReductionMode = radioButton4;
        radioButton4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    GfiFragment.this.setFPSControlVisibility(4);
                    GfiFragment.this.radioGroup_interpolationModeDataCollection.clearCheck();
                    GfiFragment.this.updatePolicy(GfiPolicy.KEY_INTERPOLATION_MODE, "mllatred");
                }
            }
        });
        RadioButton radioButton5 = (RadioButton) view.findViewById(R.id.radiobutton_mode_none);
        this.radioButton_NoneMode = radioButton5;
        radioButton5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    GfiFragment.this.setFPSControlVisibility(0);
                    GfiFragment.this.radioGroup_interpolationMode.clearCheck();
                    GfiFragment.this.updatePolicy(GfiPolicy.KEY_INTERPOLATION_MODE, "none");
                }
            }
        });
        RadioButton radioButton6 = (RadioButton) view.findViewById(R.id.radiobutton_mode_unlimited);
        this.radioButton_UnlimitedMode = radioButton6;
        radioButton6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    GfiFragment.this.setFPSControlVisibility(0);
                    GfiFragment.this.radioGroup_interpolationMode.clearCheck();
                    GfiFragment.this.updatePolicy(GfiPolicy.KEY_INTERPOLATION_MODE, "unlimited");
                }
            }
        });
        EditText editText5 = (EditText) view.findViewById(R.id.edittext_dfs_offset);
        this.editText_DFSOffset = editText5;
        editText5.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                try {
                    GfiFragment.this.updatePolicyNested(GfiPolicy.KEY_DFS_OFFSET, "value", Integer.valueOf(Integer.parseInt(charSequence.toString())));
                } catch (NumberFormatException unused) {
                    GosTestLog.d(GfiFragment.LOG_TAG, "onTextChanged(): text not integer");
                }
            }
        });
        Switch switchR8 = (Switch) view.findViewById(R.id.switch_enable_dfs_offset);
        this.switch_EnableDFSOffset = switchR8;
        switchR8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                GfiFragment.this.lambda$init$7$GfiFragment(compoundButton, z);
            }
        });
        EditText editText6 = (EditText) view.findViewById(R.id.edittext_dfs_maximum);
        this.editText_DFSMaximum = editText6;
        editText6.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                try {
                    GfiFragment.this.updatePolicyNested(GfiPolicy.KEY_DFS_OFFSET, "maximum", Integer.valueOf(Integer.parseInt(charSequence.toString())));
                } catch (NumberFormatException unused) {
                    GosTestLog.d(GfiFragment.LOG_TAG, "onTextChanged(): text not integer");
                }
            }
        });
        this.seekBar_DFSSmoothness = (SeekBar) view.findViewById(R.id.seekbar_dfs_smoothness);
        this.textView_DFSSmoothness = (TextView) view.findViewById(R.id.text_dfs_smoothness_value);
        this.seekBar_DFSSmoothness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                GfiFragment.this.textView_DFSSmoothness.setText(String.valueOf(((double) seekBar.getProgress()) / 100.0d));
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                GfiFragment.this.updatePolicyNested(GfiPolicy.KEY_DFS_OFFSET, GfiPolicy.DfsOffset.KEY_SMOOTHNESS, Double.valueOf(((double) seekBar.getProgress()) / 100.0d));
            }
        });
        Switch switchR9 = (Switch) view.findViewById(R.id.switch_enable_gfps_offset);
        this.switch_EnableGFPSOffset = switchR9;
        switchR9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                GfiFragment.this.lambda$init$8$GfiFragment(compoundButton, z);
            }
        });
        EditText editText7 = (EditText) view.findViewById(R.id.edittext_gfps_offset);
        this.editText_GFPSOffset = editText7;
        editText7.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                try {
                    GfiFragment.this.updatePolicyNested(GfiPolicy.KEY_GFPS_OFFSET, "value", Integer.valueOf(Integer.parseInt(charSequence.toString())));
                } catch (NumberFormatException unused) {
                    GosTestLog.d(GfiFragment.LOG_TAG, "onTextChanged(): text not integer");
                }
            }
        });
        EditText editText8 = (EditText) view.findViewById(R.id.edittext_minimum_gfps);
        this.editText_MinimumGFPS = editText8;
        editText8.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                try {
                    GfiFragment.this.updatePolicyNested(GfiPolicy.KEY_GFPS_OFFSET, "minimum", Integer.valueOf(Integer.parseInt(charSequence.toString())));
                } catch (NumberFormatException unused) {
                    GosTestLog.d(GfiFragment.LOG_TAG, "onTextChanged(): text not integer");
                }
            }
        });
        this.seekBar_regalStability = (SeekBar) view.findViewById(R.id.seekbar_min_regal_stability);
        this.textView_regalStability = (TextView) view.findViewById(R.id.text_min_regal_stability);
        this.seekBar_regalStability.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                GfiFragment.this.textView_regalStability.setText(String.valueOf(((double) seekBar.getProgress()) / 100.0d));
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                GfiFragment.this.updatePolicy(GfiPolicy.KEY_MINIMUM_REGAL_STABILITY, Double.valueOf(((double) seekBar.getProgress()) / 100.0d));
            }
        });
        EditText editText9 = (EditText) view.findViewById(R.id.edittext_max_consec_gl_compositions);
        this.editText_maxConsecGL = editText9;
        editText9.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                try {
                    GfiFragment.this.updatePolicy(GfiPolicy.KEY_MAX_CONSECUTIVE_GL_COMPOSITIONS, Integer.valueOf(Integer.parseInt(charSequence.toString())));
                } catch (NumberFormatException unused) {
                    GosTestLog.i(GfiFragment.LOG_TAG, "onTextChanged(): text not integer");
                }
            }
        });
        this.seekBar_maxPercentGL = (SeekBar) view.findViewById(R.id.seekbar_max_pc_gl_compositions);
        this.textView_maxPercentGL = (TextView) view.findViewById(R.id.text_max_pc_gl_compositions);
        this.seekBar_maxPercentGL.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                TextView access$1000 = GfiFragment.this.textView_maxPercentGL;
                access$1000.setText(String.valueOf(seekBar.getProgress()) + "%");
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                GfiFragment.this.updatePolicy(GfiPolicy.KEY_MAX_PERCENT_GL_COMPOSITIONS, Double.valueOf(((double) seekBar.getProgress()) / 100.0d));
            }
        });
        EditText editText10 = (EditText) view.findViewById(R.id.edittext_min_acceptable_fps);
        this.editText_minAcceptableFps = editText10;
        editText10.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                try {
                    GfiFragment.this.updatePolicy(GfiPolicy.KEY_MIN_ACCEPTABLE_FPS, Integer.valueOf(Integer.parseInt(charSequence.toString())));
                } catch (NumberFormatException unused) {
                    GosTestLog.i(GfiFragment.LOG_TAG, "onTextChanged(): text not integer");
                }
            }
        });
        this.linearLayout_GFPS = (LinearLayout) view.findViewById(R.id.layout_gfps);
        this.linearLayout_DFS = (LinearLayout) view.findViewById(R.id.layout_dfs);
        this.linearLayout_DFS_settings = (LinearLayout) view.findViewById(R.id.linearlayout_dfs_settings);
        this.linearLayout_GFPS_settings = (LinearLayout) view.findViewById(R.id.linearlayout_gfps_settings);
    }

    public /* synthetic */ void lambda$init$2$GfiFragment(CompoundButton compoundButton, boolean z) {
        updatePolicy("enabled", Boolean.valueOf(z));
    }

    public /* synthetic */ void lambda$init$3$GfiFragment(CompoundButton compoundButton, boolean z) {
        updatePolicy(GfiPolicy.KEY_AUTODELAY_ENABLED, Boolean.valueOf(z));
    }

    public /* synthetic */ void lambda$init$4$GfiFragment(CompoundButton compoundButton, boolean z) {
        updatePolicy(GfiPolicy.KEY_WRITE_TO_FRAMETRACKER, Boolean.valueOf(z));
    }

    public /* synthetic */ void lambda$init$5$GfiFragment(CompoundButton compoundButton, boolean z) {
        updatePolicy(GfiPolicy.KEY_EXTERNAL_CONTROL, Boolean.valueOf(z));
    }

    public /* synthetic */ void lambda$init$6$GfiFragment(CompoundButton compoundButton, boolean z) {
        updatePolicy(GfiPolicy.KEY_NO_INTERP_WITH_EXTRA_LAYERS, Boolean.valueOf(z));
    }

    public /* synthetic */ void lambda$init$7$GfiFragment(CompoundButton compoundButton, boolean z) {
        this.editText_DFSOffset.setEnabled(z);
        this.editText_DFSMaximum.setEnabled(z);
        this.seekBar_DFSSmoothness.setEnabled(z);
        if (z) {
            try {
                updatePolicyNested(GfiPolicy.KEY_DFS_OFFSET, "value", Integer.valueOf(Integer.parseInt(this.editText_DFSOffset.getText().toString())));
                updatePolicyNested(GfiPolicy.KEY_DFS_OFFSET, "maximum", Integer.valueOf(Integer.parseInt(this.editText_DFSMaximum.getText().toString())));
                updatePolicyNested(GfiPolicy.KEY_DFS_OFFSET, GfiPolicy.DfsOffset.KEY_SMOOTHNESS, Double.valueOf(((double) this.seekBar_DFSSmoothness.getProgress()) / 100.0d));
                updatePolicyNested(GfiPolicy.KEY_DFS_OFFSET, "enabled", true);
            } catch (NumberFormatException unused) {
                GosTestLog.d(LOG_TAG, "onTextChanged(): text not integer");
            }
        } else {
            clearPolicy(GfiPolicy.KEY_DFS_OFFSET);
            updatePolicyNested(GfiPolicy.KEY_DFS_OFFSET, "enabled", false);
        }
    }

    public /* synthetic */ void lambda$init$8$GfiFragment(CompoundButton compoundButton, boolean z) {
        this.editText_GFPSOffset.setEnabled(z);
        this.editText_MinimumGFPS.setEnabled(z);
        if (z) {
            try {
                updatePolicyNested(GfiPolicy.KEY_GFPS_OFFSET, "value", Integer.valueOf(Integer.parseInt(this.editText_GFPSOffset.getText().toString())));
                updatePolicyNested(GfiPolicy.KEY_GFPS_OFFSET, "minimum", Integer.valueOf(Integer.parseInt(this.editText_MinimumGFPS.getText().toString())));
                updatePolicyNested(GfiPolicy.KEY_GFPS_OFFSET, "enabled", true);
            } catch (NumberFormatException unused) {
                GosTestLog.d(LOG_TAG, "onTextChanged(): text not integer");
            }
        } else {
            clearPolicy(GfiPolicy.KEY_GFPS_OFFSET);
            updatePolicyNested(GfiPolicy.KEY_GFPS_OFFSET, "enabled", false);
        }
    }

    public void onResume() {
        super.onResume();
        this.mInstalledPackageNameArray.clear();
        this.mInstalledPackageNameArray.addAll(DbHelper.getInstance().getPackageDao().getPkgNameListByCategory(Constants.CategoryCode.GAME));
        GosTestLog.d(LOG_TAG, "mInstalledPackageNameArray.size(): " + this.mInstalledPackageNameArray.size());
        this.mInstalledPackageNameArrayAdapter.clear();
        this.mInstalledPackageNameArrayAdapter.addAll(this.mInstalledPackageNameArray);
        GosTestLog.d(LOG_TAG, "mInstalledPackageNameArrayAdapter.getCount(): " + this.mInstalledPackageNameArrayAdapter.getCount());
    }
}
