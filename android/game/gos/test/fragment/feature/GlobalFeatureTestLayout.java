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
import com.samsung.android.game.gos.data.dao.GlobalFeatureFlagDao;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.data.model.GlobalFeatureFlag;
import com.samsung.android.game.gos.data.model.State;
import com.samsung.android.game.gos.test.util.GosTestLog;
import com.samsung.android.game.gos.test.util.TestDataSetter;
import java.util.Map;

public class GlobalFeatureTestLayout {
    private static final char CHAR_OFF = '0';
    private static final char CHAR_ON = '1';
    private static final String LOG_TAG = "GlobalFeatureTestLayout";
    /* access modifiers changed from: private */
    public String mFeatureName;
    private LinearLayout mLayout;
    private AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            GosTestLog.d(GlobalFeatureTestLayout.LOG_TAG, "spinner_state, onItemSelected(), selection: " + i + ", featureName : " + GlobalFeatureTestLayout.this.mFeatureName);
            String state = DbHelper.getInstance().getGlobalFeatureFlagDao().getState(GlobalFeatureTestLayout.this.mFeatureName);
            String str = "inherited";
            for (SpinnerSelectionAndStateArray spinnerSelectionAndStateArray : GlobalFeatureTestLayout.getSpinnerSelectionAndStateArray()) {
                if (spinnerSelectionAndStateArray.getKey().intValue() == i) {
                    str = spinnerSelectionAndStateArray.getValue();
                }
            }
            if (str != null && !str.equals(state) && State.isValidV4State(str)) {
                GlobalDbHelper.getInstance().setFeatureFlagState(GlobalFeatureTestLayout.this.mFeatureName, str);
                TestDataSetter.applyGlobalData();
                Toast.makeText(AppContext.get(), "The value was applied. All running apps were stopped.", 1).show();
                GlobalFeatureTestLayout.this.loadData();
            }
        }
    };
    private Spinner spinner_state;
    private CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
        public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            GlobalFeatureTestLayout.this.lambda$new$0$GlobalFeatureTestLayout(compoundButton, z);
        }
    };
    private Switch switch_enabled;
    private Switch switch_using_pkg;
    private Switch switch_using_user;
    private TextView txt_available;
    private TextView txt_defaultFlag;

    public GlobalFeatureTestLayout(String str, String str2) {
        this.mFeatureName = str;
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(AppContext.get()).inflate(R.layout.row_feature_flag, (ViewGroup) null);
        this.mLayout = linearLayout;
        ((TextView) linearLayout.findViewById(R.id.txt_featureName)).setText(str2);
        this.txt_available = (TextView) this.mLayout.findViewById(R.id.txt_available);
        this.txt_defaultFlag = (TextView) this.mLayout.findViewById(R.id.txt_defaultFlag);
        Spinner spinner = (Spinner) this.mLayout.findViewById(R.id.spinner_state);
        this.spinner_state = spinner;
        spinner.setOnItemSelectedListener(this.selectedListener);
        Switch switchR = (Switch) this.mLayout.findViewById(R.id.switch_enabled);
        this.switch_enabled = switchR;
        switchR.setOnCheckedChangeListener(this.switchListener);
        Switch switchR2 = (Switch) this.mLayout.findViewById(R.id.switch_using_user);
        this.switch_using_user = switchR2;
        switchR2.setOnCheckedChangeListener(this.switchListener);
        Switch switchR3 = (Switch) this.mLayout.findViewById(R.id.switch_using_pkg);
        this.switch_using_pkg = switchR3;
        switchR3.setOnCheckedChangeListener(this.switchListener);
        loadData();
    }

    public /* synthetic */ void lambda$new$0$GlobalFeatureTestLayout(CompoundButton compoundButton, boolean z) {
        String str;
        int id = compoundButton.getId();
        GosTestLog.d(LOG_TAG, "onCheckedChanged(), isChecked: " + z + ", featureDisplayName: " + this.mFeatureName);
        GlobalFeatureFlagDao globalFeatureFlagDao = DbHelper.getInstance().getGlobalFeatureFlagDao();
        GlobalFeatureFlag globalFeatureFlag = globalFeatureFlagDao.get(this.mFeatureName);
        boolean z2 = false;
        switch (id) {
            case R.id.switch_enabled:
                boolean z3 = globalFeatureFlag != null && globalFeatureFlag.enabledFlagByUser;
                if (z3 != z) {
                    if (globalFeatureFlag != null) {
                        str = globalFeatureFlag.getState();
                    } else {
                        str = "inherited";
                    }
                    if (globalFeatureFlag != null && !globalFeatureFlag.available) {
                        this.switch_enabled.setChecked(z3);
                        Toast.makeText(AppContext.get(), "The feature is not available in this device.", 1).show();
                        return;
                    } else if (str == null) {
                        return;
                    } else {
                        if ("inherited".equalsIgnoreCase(str)) {
                            DbHelper.getInstance().getGlobalFeatureFlagDao().setEnabledFlagByUser(new GlobalFeatureFlag.NameAndEnabledFlagByUser(this.mFeatureName, z));
                            TestDataSetter.applyGlobalData();
                            Toast.makeText(AppContext.get(), "The value was applied. All running apps were stopped.", 1).show();
                            return;
                        }
                        this.switch_enabled.setChecked(z3);
                        Toast.makeText(AppContext.get(), "Please change the state to proper one before switching the enabled feature flag.", 1).show();
                        return;
                    }
                } else {
                    return;
                }
            case R.id.switch_using_pkg:
                if (globalFeatureFlag != null && globalFeatureFlag.usingPkgValue) {
                    z2 = true;
                }
                if (globalFeatureFlag == null || globalFeatureFlag.available) {
                    if (z2 != z) {
                        globalFeatureFlagDao.setUsingPkgValue(new GlobalFeatureFlag.NameAndUsingPkgValue(this.mFeatureName, z));
                        break;
                    }
                } else {
                    this.switch_using_pkg.setChecked(z2);
                    Toast.makeText(AppContext.get(), "The feature is not available in this device.", 1).show();
                    return;
                }
                break;
            case R.id.switch_using_user:
                if (globalFeatureFlag != null && globalFeatureFlag.usingUserValue) {
                    z2 = true;
                }
                if (globalFeatureFlag == null || globalFeatureFlag.available) {
                    if (z2 != z) {
                        globalFeatureFlagDao.setUsingUserValue(new GlobalFeatureFlag.NameAndUsingUserValue(this.mFeatureName, z));
                        break;
                    }
                } else {
                    this.switch_using_user.setChecked(z2);
                    Toast.makeText(AppContext.get(), "The feature is not available in this device.", 1).show();
                    return;
                }
                break;
            default:
                return;
        }
        TestDataSetter.applyGlobalData();
        loadData();
    }

    public LinearLayout getLayout() {
        return this.mLayout;
    }

    public void loadData() {
        GlobalFeatureFlagDao globalFeatureFlagDao = DbHelper.getInstance().getGlobalFeatureFlagDao();
        boolean isAvailable = globalFeatureFlagDao.isAvailable(this.mFeatureName);
        char c = CHAR_ON;
        this.txt_available.setText(String.valueOf(isAvailable ? '1' : '0'));
        if (!GlobalDbHelper.getInstance().isDefaultFeatureFlag(this.mFeatureName)) {
            c = '0';
        }
        this.txt_defaultFlag.setText(String.valueOf(c));
        String state = globalFeatureFlagDao.getState(this.mFeatureName);
        SpinnerSelectionAndStateArray[] spinnerSelectionAndStateArray = getSpinnerSelectionAndStateArray();
        int length = spinnerSelectionAndStateArray.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            SpinnerSelectionAndStateArray spinnerSelectionAndStateArray2 = spinnerSelectionAndStateArray[i];
            if (state != null && state.equals(spinnerSelectionAndStateArray2.getValue())) {
                this.spinner_state.setSelection(spinnerSelectionAndStateArray2.getKey().intValue());
                break;
            }
            i++;
        }
        this.switch_enabled.setChecked(globalFeatureFlagDao.isEnabledFlagByUser(this.mFeatureName));
        this.switch_using_user.setChecked(globalFeatureFlagDao.isUsingUserValue(this.mFeatureName));
        this.switch_using_pkg.setChecked(globalFeatureFlagDao.isUsingPkgValue(this.mFeatureName));
    }

    private static class SpinnerSelectionAndStateArray implements Map.Entry<Integer, String> {
        int mKey;
        String mStateId;

        public SpinnerSelectionAndStateArray(int i, String str) {
            this.mKey = i;
            this.mStateId = str;
        }

        public Integer getKey() {
            return Integer.valueOf(this.mKey);
        }

        public String getValue() {
            return this.mStateId;
        }

        public String setValue(String str) {
            this.mStateId = str;
            return str;
        }
    }

    /* access modifiers changed from: private */
    public static SpinnerSelectionAndStateArray[] getSpinnerSelectionAndStateArray() {
        String[] stringArray = AppContext.get().getResources().getStringArray(R.array.server_state);
        SpinnerSelectionAndStateArray[] spinnerSelectionAndStateArrayArr = new SpinnerSelectionAndStateArray[stringArray.length];
        int i = 0;
        while (i < stringArray.length) {
            try {
                spinnerSelectionAndStateArrayArr[i] = new SpinnerSelectionAndStateArray(i, stringArray[i]);
                i++;
            } catch (IndexOutOfBoundsException e) {
                GosTestLog.w(LOG_TAG, (Throwable) e);
            }
        }
        return spinnerSelectionAndStateArrayArr;
    }
}
