package com.samsung.android.game.gos.test.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;
import com.samsung.android.game.gos.R;
import com.samsung.android.game.gos.data.dbhelper.FeatureHelper;
import com.samsung.android.game.gos.endpoint.MethodsForJsonCommand;
import com.samsung.android.game.gos.test.util.GosTestLog;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import org.json.JSONException;
import org.json.JSONObject;

public class SiopModeFragment extends BaseFragment {
    private static final String LOG_TAG = SiopModeFragment.class.getSimpleName();
    private boolean isSiopModeEnabled = false;
    private RadioButton radioButton_gamePlayTimeMode;
    private RadioButton radioButton_normalMode;
    private RadioButton radioButton_offMode;
    private RadioButton radioButton_performanceMode;

    public int getNavItemId() {
        return R.id.nav_siopModeTest;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        GosTestLog.d(LOG_TAG, "onCreateView()");
        View inflate = layoutInflater.inflate(R.layout.fragment_mode_test, viewGroup, false);
        init(inflate);
        return inflate;
    }

    public void onResume() {
        GosTestLog.d(LOG_TAG, "onResume()");
        getInfo();
        super.onResume();
    }

    private void init(View view) {
        GosTestLog.v(LOG_TAG, "init()");
        this.isSiopModeEnabled = FeatureHelper.isEnabledFlagByUser("siop_mode");
        RadioButton radioButton = (RadioButton) view.findViewById(R.id.radioButton_performanceMode);
        this.radioButton_performanceMode = radioButton;
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SiopModeFragment.this.lambda$init$0$SiopModeFragment(compoundButton, z);
            }
        });
        RadioButton radioButton2 = (RadioButton) view.findViewById(R.id.radioButton_normalMode);
        this.radioButton_normalMode = radioButton2;
        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SiopModeFragment.this.lambda$init$1$SiopModeFragment(compoundButton, z);
            }
        });
        RadioButton radioButton3 = (RadioButton) view.findViewById(R.id.radioButton_gamePlayTimeMode);
        this.radioButton_gamePlayTimeMode = radioButton3;
        radioButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SiopModeFragment.this.lambda$init$2$SiopModeFragment(compoundButton, z);
            }
        });
        RadioButton radioButton4 = (RadioButton) view.findViewById(R.id.radioButton_offMode);
        this.radioButton_offMode = radioButton4;
        radioButton4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SiopModeFragment.this.lambda$init$3$SiopModeFragment(compoundButton, z);
            }
        });
    }

    public /* synthetic */ void lambda$init$0$SiopModeFragment(CompoundButton compoundButton, boolean z) {
        setGlobalSiopMode("radioButton_performanceMode, onCheckedChanged()", z, 1);
    }

    public /* synthetic */ void lambda$init$1$SiopModeFragment(CompoundButton compoundButton, boolean z) {
        setGlobalSiopMode("radioButton_normalMode, onCheckedChanged()", z, 0);
    }

    public /* synthetic */ void lambda$init$2$SiopModeFragment(CompoundButton compoundButton, boolean z) {
        setGlobalSiopMode("radioButton_gamePlayTimeMode, onCheckedChanged()", z, -1);
    }

    public /* synthetic */ void lambda$init$3$SiopModeFragment(CompoundButton compoundButton, boolean z) {
        setGlobalSiopMode("radioButton_offMode, onCheckedChanged()", z, -1000);
    }

    private void setGlobalSiopMode(String str, boolean z, int i) {
        String str2 = LOG_TAG;
        GosTestLog.d(str2, str + ", isChecked: " + z);
        if (z) {
            if (!this.isSiopModeEnabled) {
                Toast.makeText(getActivity(), "SIOP_MODE is not enabled.", 0).show();
            }
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("siop_mode", i);
                showResultFromJsonResponse(str, MethodsForJsonCommand.respondWithJson(GosInterface.Command.SET_GLOBAL_DATA, jSONObject.toString(), (String) null));
            } catch (Exception e) {
                String str3 = LOG_TAG;
                showResult(str3, str + " error: " + e.getLocalizedMessage());
                String str4 = LOG_TAG;
                GosTestLog.w(str4, str + " error: ", e);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x00b1  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00d3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void getInfo() {
        /*
            r9 = this;
            java.lang.String r0 = LOG_TAG
            java.lang.String r1 = "getInfo()"
            com.samsung.android.game.gos.test.util.GosTestLog.v(r0, r1)
            java.lang.String r0 = "siop_mode"
            boolean r1 = com.samsung.android.game.gos.data.dbhelper.FeatureHelper.isAvailable(r0)
            r2 = 0
            if (r1 != 0) goto L_0x001e
            androidx.fragment.app.FragmentActivity r1 = r9.getActivity()
            java.lang.String r3 = "SIOP_MODE is not available."
            android.widget.Toast r1 = android.widget.Toast.makeText(r1, r3, r2)
            r1.show()
            goto L_0x002f
        L_0x001e:
            boolean r1 = r9.isSiopModeEnabled
            if (r1 != 0) goto L_0x002f
            androidx.fragment.app.FragmentActivity r1 = r9.getActivity()
            java.lang.String r3 = "SIOP_MODE is not enabled."
            android.widget.Toast r1 = android.widget.Toast.makeText(r1, r3, r2)
            r1.show()
        L_0x002f:
            java.lang.String r1 = "get_global_data"
            r3 = 0
            java.lang.String r1 = com.samsung.android.game.gos.endpoint.MethodsForJsonCommand.respondWithJson(r1, r3, r3)
            r3 = -1000(0xfffffffffffffc18, float:NaN)
            r4 = 1
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{ Exception -> 0x006e }
            r5.<init>(r1)     // Catch:{ Exception -> 0x006e }
            boolean r1 = r5.has(r0)     // Catch:{ Exception -> 0x006e }
            if (r1 == 0) goto L_0x0063
            int r0 = r5.getInt(r0)     // Catch:{ Exception -> 0x006e }
            java.lang.String r1 = LOG_TAG     // Catch:{ Exception -> 0x0061 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0061 }
            r5.<init>()     // Catch:{ Exception -> 0x0061 }
            java.lang.String r6 = "getInfo(), current SiopMode: "
            r5.append(r6)     // Catch:{ Exception -> 0x0061 }
            r5.append(r0)     // Catch:{ Exception -> 0x0061 }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x0061 }
            com.samsung.android.game.gos.test.util.GosTestLog.d(r1, r5)     // Catch:{ Exception -> 0x0061 }
            r9.isSiopModeEnabled = r4     // Catch:{ Exception -> 0x0061 }
            goto L_0x008f
        L_0x0061:
            r1 = move-exception
            goto L_0x0070
        L_0x0063:
            java.lang.String r0 = LOG_TAG     // Catch:{ Exception -> 0x006e }
            java.lang.String r1 = "getInfo(), SiopMode is disabled."
            com.samsung.android.game.gos.test.util.GosTestLog.e(r0, r1)     // Catch:{ Exception -> 0x006e }
            r9.isSiopModeEnabled = r2     // Catch:{ Exception -> 0x006e }
            r0 = r3
            goto L_0x008f
        L_0x006e:
            r1 = move-exception
            r0 = r3
        L_0x0070:
            java.lang.String r5 = LOG_TAG
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "getInfo() error: "
            r6.append(r7)
            java.lang.String r8 = r1.getLocalizedMessage()
            r6.append(r8)
            java.lang.String r6 = r6.toString()
            r9.showResult(r5, r6)
            java.lang.String r5 = LOG_TAG
            com.samsung.android.game.gos.test.util.GosTestLog.w(r5, r7, r1)
        L_0x008f:
            com.samsung.android.game.gos.data.DataManager r1 = com.samsung.android.game.gos.data.DataManager.getInstance()
            int r1 = r1.getResolutionMode()
            java.lang.String r5 = LOG_TAG
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "getInfo(), current mode: "
            r6.append(r7)
            r6.append(r1)
            java.lang.String r1 = r6.toString()
            com.samsung.android.game.gos.test.util.GosTestLog.d(r5, r1)
            boolean r1 = r9.isSiopModeEnabled
            if (r1 == 0) goto L_0x00d3
            if (r0 == r3) goto L_0x00cd
            r1 = -1
            if (r0 == r1) goto L_0x00c7
            if (r0 == 0) goto L_0x00c1
            if (r0 == r4) goto L_0x00bb
            goto L_0x00e7
        L_0x00bb:
            android.widget.RadioButton r0 = r9.radioButton_performanceMode
            r0.setChecked(r4)
            goto L_0x00e7
        L_0x00c1:
            android.widget.RadioButton r0 = r9.radioButton_normalMode
            r0.setChecked(r4)
            goto L_0x00e7
        L_0x00c7:
            android.widget.RadioButton r0 = r9.radioButton_gamePlayTimeMode
            r0.setChecked(r4)
            goto L_0x00e7
        L_0x00cd:
            android.widget.RadioButton r0 = r9.radioButton_offMode
            r0.setChecked(r4)
            goto L_0x00e7
        L_0x00d3:
            android.widget.RadioButton r0 = r9.radioButton_performanceMode
            r0.setChecked(r2)
            android.widget.RadioButton r0 = r9.radioButton_normalMode
            r0.setChecked(r2)
            android.widget.RadioButton r0 = r9.radioButton_gamePlayTimeMode
            r0.setChecked(r2)
            android.widget.RadioButton r0 = r9.radioButton_offMode
            r0.setChecked(r2)
        L_0x00e7:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.test.fragment.SiopModeFragment.getInfo():void");
    }

    private void showResultFromJsonResponse(String str, String str2) {
        try {
            JSONObject jSONObject = new JSONObject(str2);
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            appendResultLog(sb, jSONObject, GosInterface.KeyName.SUCCESSFUL);
            appendResultLog(sb, jSONObject, GosInterface.KeyName.SUCCESSFUL_ITEMS);
            appendResultLog(sb, jSONObject, GosInterface.KeyName.COMMENT);
            showResult(LOG_TAG, sb.toString());
        } catch (Exception e) {
            String str3 = LOG_TAG;
            showResult(str3, str + " error: " + e.getLocalizedMessage());
            String str4 = LOG_TAG;
            GosTestLog.w(str4, str + " error: ", e);
        }
    }

    private void appendResultLog(StringBuilder sb, JSONObject jSONObject, String str) throws JSONException {
        if (jSONObject.has(str)) {
            Object obj = jSONObject.get(str);
            sb.append(", ");
            sb.append(str);
            sb.append(": ");
            sb.append(obj);
        }
    }
}
