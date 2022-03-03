package com.samsung.android.game.gos.test.fragment.feature;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.samsung.android.game.gos.R;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.model.FeatureFlag;
import com.samsung.android.game.gos.data.model.State;
import com.samsung.android.game.gos.test.util.GosTestLog;

public class PackageFeatureTestLayout {
    private static final char CHAR_OFF = '0';
    private static final char CHAR_ON = '1';
    private static final String LOG_TAG = "PackageFeatureTestLayout";
    /* access modifiers changed from: private */
    public FeatureFlag mFeatureFlag;
    private String mFeatureName;
    private LinearLayout mLayout = ((LinearLayout) LayoutInflater.from(AppContext.get()).inflate(R.layout.row_package_feature_flag, (ViewGroup) null));
    private String mPkgName;
    private Spinner spinner_state;
    private Switch switch_enabledByUser;
    private TextView txt_available;

    public PackageFeatureTestLayout(String str, final String str2, String str3) {
        this.mFeatureName = str;
        this.mPkgName = str3;
        try {
            this.mFeatureFlag = DbHelper.getInstance().getFeatureFlagDao().getByFeatureNameAndPkgName(this.mFeatureName, this.mPkgName);
        } catch (Exception e) {
            GosTestLog.w(LOG_TAG, (Throwable) e);
        }
        ((TextView) this.mLayout.findViewById(R.id.txt_featureName)).setText(str2);
        this.txt_available = (TextView) this.mLayout.findViewById(R.id.txt_available);
        Spinner spinner = (Spinner) this.mLayout.findViewById(R.id.spinner_state);
        this.spinner_state = spinner;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                String str;
                GosTestLog.d(PackageFeatureTestLayout.LOG_TAG, "spinner_serverPolicyFlag, onItemSelected(), selection: " + i + ", featureDisplayName : " + str2);
                SpinnerStateArray[] access$000 = PackageFeatureTestLayout.getSpinnerStateArray();
                int length = access$000.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        str = "inherited";
                        break;
                    }
                    SpinnerStateArray spinnerStateArray = access$000[i2];
                    if (spinnerStateArray.getKey().intValue() == i) {
                        str = spinnerStateArray.getValue();
                        break;
                    }
                    i2++;
                }
                if (PackageFeatureTestLayout.this.mFeatureFlag != null && !str.equalsIgnoreCase(PackageFeatureTestLayout.this.mFeatureFlag.getState())) {
                    PackageFeatureTestLayout.this.mFeatureFlag.setState(str);
                    DbHelper.getInstance().getFeatureFlagDao().insertOrUpdate(PackageFeatureTestLayout.this.mFeatureFlag);
                    Toast.makeText(AppContext.get(), "The value was applied. All running apps were stopped.", 1).show();
                }
                PackageFeatureTestLayout.this.loadData();
            }
        });
        Switch switchR = (Switch) this.mLayout.findViewById(R.id.switch_enabledFlagByUser);
        this.switch_enabledByUser = switchR;
        switchR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(str2) {
            public final /* synthetic */ String f$1;

            {
                this.f$1 = r2;
            }

            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                PackageFeatureTestLayout.this.lambda$new$0$PackageFeatureTestLayout(this.f$1, compoundButton, z);
            }
        });
        loadData();
    }

    public /* synthetic */ void lambda$new$0$PackageFeatureTestLayout(String str, CompoundButton compoundButton, boolean z) {
        GosTestLog.d(LOG_TAG, "switch_enabledByUser, onCheckedChanged(), checked : " + z + ", featureDisplayName : " + str);
        FeatureFlag featureFlag = this.mFeatureFlag;
        if (featureFlag != null) {
            featureFlag.setEnabledFlagByUser(z);
            DbHelper.getInstance().getFeatureFlagDao().insertOrUpdate(this.mFeatureFlag);
        }
        loadData();
    }

    /* access modifiers changed from: private */
    public void loadData() {
        this.txt_available.setText(String.valueOf(DbHelper.getInstance().getGlobalFeatureFlagDao().isAvailable(this.mFeatureName) ? CHAR_ON : CHAR_OFF));
        try {
            if (this.mFeatureFlag != null) {
                String state = this.mFeatureFlag.getState();
                this.switch_enabledByUser.setChecked(this.mFeatureFlag.isEnabledFlagByUser());
                for (SpinnerStateArray spinnerStateArray : getSpinnerStateArray()) {
                    if (spinnerStateArray.getValue().equalsIgnoreCase(state)) {
                        this.spinner_state.setSelection(spinnerStateArray.getKey().intValue());
                        return;
                    }
                }
            }
        } catch (Exception e) {
            GosTestLog.w(LOG_TAG, (Throwable) e);
        }
    }

    /* access modifiers changed from: private */
    public static SpinnerStateArray[] getSpinnerStateArray() {
        String[] strArr = {State.FORCIBLY_ENABLED, "enabled", "inherited", State.DISABLED, State.FORCIBLY_DISABLED};
        SpinnerStateArray[] spinnerStateArrayArr = new SpinnerStateArray[5];
        int i = 0;
        while (i < 5) {
            try {
                spinnerStateArrayArr[i] = new SpinnerStateArray(i, strArr[i]);
                i++;
            } catch (IndexOutOfBoundsException e) {
                GosTestLog.w(LOG_TAG, (Throwable) e);
            }
        }
        return spinnerStateArrayArr;
    }

    public LinearLayout getLayout() {
        return this.mLayout;
    }
}
