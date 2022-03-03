package com.samsung.android.game.gos.test.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.samsung.android.game.gos.R;
import com.samsung.android.game.gos.data.dbhelper.FeatureHelper;
import com.samsung.android.game.gos.feature.resumeboost.ResumeBoostFeature;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.selibrary.SeDvfsInterfaceImpl;
import com.samsung.android.game.gos.test.util.GosTestLog;
import com.samsung.android.game.gos.test.util.TestDataSetter;
import com.samsung.android.game.gos.value.Constants;

public class ResumeBoostFragment extends BaseFragment implements TestDataSetter.DataSettingFeedBack {
    private static final String LOG_TAG = "ResumeBoostFragment";
    Button btn_apply;
    Button btn_restore;
    EditText edit_bus_index;
    EditText edit_cpu_index;
    EditText edit_duration;
    boolean isLaunchBoostEnabled = false;
    private TestDataSetter mTestDataSetter;
    TextView txt_bus_freq;
    TextView txt_cpu_freq;

    public int getNavItemId() {
        return R.id.nav_resumeBoost;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        GosTestLog.d(LOG_TAG, "onCreateView()");
        View inflate = layoutInflater.inflate(R.layout.fragment_launch_boost, viewGroup, false);
        init(inflate);
        return inflate;
    }

    public void onResume() {
        super.onResume();
        GosTestLog.d(LOG_TAG, "onResume()");
        getInfo();
    }

    private void init(View view) {
        GosTestLog.d(LOG_TAG, "init()");
        boolean isEnabledFlagByUser = FeatureHelper.isEnabledFlagByUser(Constants.V4FeatureFlag.RESUME_BOOST);
        this.isLaunchBoostEnabled = isEnabledFlagByUser;
        if (!isEnabledFlagByUser) {
            Toast.makeText(getActivity(), "RESUME_BOOST is not enabled.", 0).show();
        }
        this.mTestDataSetter = new TestDataSetter(getActivity(), this);
        this.txt_cpu_freq = (TextView) view.findViewById(R.id.txt_cpu_freq);
        this.txt_bus_freq = (TextView) view.findViewById(R.id.txt_bus_freq);
        this.edit_duration = (EditText) view.findViewById(R.id.edit_duration);
        this.edit_cpu_index = (EditText) view.findViewById(R.id.edit_cpu_index);
        this.edit_bus_index = (EditText) view.findViewById(R.id.edit_bus_index);
        Button button = (Button) view.findViewById(R.id.btn_apply);
        this.btn_apply = button;
        button.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ResumeBoostFragment.this.lambda$init$0$ResumeBoostFragment(view);
            }
        });
        Button button2 = (Button) view.findViewById(R.id.btn_restore);
        this.btn_restore = button2;
        button2.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ResumeBoostFragment.this.lambda$init$1$ResumeBoostFragment(view);
            }
        });
    }

    public /* synthetic */ void lambda$init$0$ResumeBoostFragment(View view) {
        int currentDurationSec = ResumeBoostFeature.getInstance().getCurrentDurationSec();
        int currentCpuIndex = ResumeBoostFeature.getInstance().getCurrentCpuIndex();
        int currentBusIndex = ResumeBoostFeature.getInstance().getCurrentBusIndex();
        try {
            currentDurationSec = Integer.parseInt(this.edit_duration.getText().toString());
        } catch (NumberFormatException unused) {
            GosTestLog.d(LOG_TAG, "NumberFormatException due to durationSec");
        }
        try {
            currentCpuIndex = Integer.parseInt(this.edit_cpu_index.getText().toString());
        } catch (NumberFormatException unused2) {
            GosTestLog.d(LOG_TAG, "NumberFormatException due to index");
        }
        try {
            currentBusIndex = Integer.parseInt(this.edit_bus_index.getText().toString());
        } catch (NumberFormatException unused3) {
            GosTestLog.d(LOG_TAG, "NumberFormatException due to index");
        }
        ResumeBoostFeature.getInstance().changeSettings(1, currentDurationSec, currentCpuIndex, currentBusIndex);
        getInfo();
        this.edit_duration.setText(BuildConfig.VERSION_NAME);
        this.edit_cpu_index.setText(BuildConfig.VERSION_NAME);
        this.edit_bus_index.setText(BuildConfig.VERSION_NAME);
    }

    public /* synthetic */ void lambda$init$1$ResumeBoostFragment(View view) {
        this.mTestDataSetter.showDialogToRestore(TestDataSetter.DataRangeToBeSet.GLOBAL, (String) null);
    }

    private void getInfo() {
        GosTestLog.d(LOG_TAG, "getInfo()");
        int currentDurationSec = ResumeBoostFeature.getInstance().getCurrentDurationSec();
        int currentCpuIndex = ResumeBoostFeature.getInstance().getCurrentCpuIndex();
        int currentBusIndex = ResumeBoostFeature.getInstance().getCurrentBusIndex();
        String supportedFreq = ResumeBoostFeature.getInstance().getSupportedFreq(SeDvfsInterfaceImpl.TYPE_CPU_MIN);
        String supportedFreq2 = ResumeBoostFeature.getInstance().getSupportedFreq(SeDvfsInterfaceImpl.TYPE_BUS_MIN);
        this.txt_cpu_freq.setText(supportedFreq);
        this.txt_bus_freq.setText(supportedFreq2);
        this.edit_duration.setHint(String.valueOf(currentDurationSec));
        this.edit_cpu_index.setHint(String.valueOf(currentCpuIndex));
        this.edit_bus_index.setHint(String.valueOf(currentBusIndex));
    }

    public void onRestore(TestDataSetter.DataRangeToBeSet dataRangeToBeSet, String str) {
        GosTestLog.d(LOG_TAG, "onRestore()");
        ResumeBoostFeature.getInstance().updatePolicy();
        getInfo();
    }
}
