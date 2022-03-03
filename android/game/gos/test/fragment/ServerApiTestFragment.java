package com.samsung.android.game.gos.test.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.samsung.android.game.gos.R;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.controller.DataUploader;
import com.samsung.android.game.gos.data.SystemDataHelper;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.ReportDbHelper;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.network.NetworkConnector;
import com.samsung.android.game.gos.test.util.GosTestLog;
import com.samsung.android.game.gos.test.util.TestDataSetter;
import com.samsung.android.game.gos.value.AppVariable;

public class ServerApiTestFragment extends BaseFragment implements TestDataSetter.DataSettingFeedBack {
    private static final String LOG_TAG = "ServerApiTestFragment";
    Button btn_restoreData;
    Button btn_restoreDeviceName;
    Button btn_setDeviceName;
    Button btn_uploadData;
    EditText edit_deviceName;
    private int mLatestCheckedRadioButtonId = -1;
    TestDataSetter mTestDataSetter;
    private RadioGroup radioGroupURL;
    TextView txt_deviceName;
    TextView txt_orgDeviceName;

    public int getNavItemId() {
        return R.id.nav_serverApi;
    }

    public void onRestore(TestDataSetter.DataRangeToBeSet dataRangeToBeSet, String str) {
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        GosTestLog.d(LOG_TAG, "onCreateView()");
        this.mTestDataSetter = new TestDataSetter(getActivity(), this);
        View inflate = layoutInflater.inflate(R.layout.fragment_server_api, viewGroup, false);
        init(inflate);
        return inflate;
    }

    public void init(View view) {
        GosTestLog.d(LOG_TAG, "init()");
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup_url);
        this.radioGroupURL = radioGroup;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public final void onCheckedChanged(RadioGroup radioGroup, int i) {
                ServerApiTestFragment.this.lambda$init$0$ServerApiTestFragment(radioGroup, i);
            }
        });
        Button button = (Button) view.findViewById(R.id.btn_restoreDeviceName);
        this.btn_restoreDeviceName = button;
        button.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ServerApiTestFragment.this.lambda$init$1$ServerApiTestFragment(view);
            }
        });
        this.edit_deviceName = (EditText) view.findViewById(R.id.edit_deviceName);
        Button button2 = (Button) view.findViewById(R.id.btn_setDeviceName);
        this.btn_setDeviceName = button2;
        button2.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ServerApiTestFragment.this.lambda$init$2$ServerApiTestFragment(view);
            }
        });
        TextView textView = (TextView) view.findViewById(R.id.txt_orgDeviceName);
        this.txt_orgDeviceName = textView;
        textView.setText(AppVariable.getOriginalDeviceName());
        this.txt_deviceName = (TextView) view.findViewById(R.id.txt_deviceName);
        refreshDeviceName();
        Button button3 = (Button) view.findViewById(R.id.btn_restoreData);
        this.btn_restoreData = button3;
        button3.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ServerApiTestFragment.this.lambda$init$3$ServerApiTestFragment(view);
            }
        });
        Button button4 = (Button) view.findViewById(R.id.btn_uploadData);
        this.btn_uploadData = button4;
        button4.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ServerApiTestFragment.this.lambda$init$5$ServerApiTestFragment(view);
            }
        });
    }

    public /* synthetic */ void lambda$init$0$ServerApiTestFragment(RadioGroup radioGroup, int i) {
        if (this.mLatestCheckedRadioButtonId != i) {
            this.mLatestCheckedRadioButtonId = i;
            new NetworkConnector(getActivity()).setTargetServer(i == 2131296500 ? 0 : i == 2131296502 ? 1 : 2);
        }
    }

    public /* synthetic */ void lambda$init$1$ServerApiTestFragment(View view) {
        DbHelper.getInstance().getGlobalDao().setDeviceName(new Global.IdAndDeviceName(AppVariable.getOriginalDeviceName()));
        refreshDeviceName();
    }

    public /* synthetic */ void lambda$init$2$ServerApiTestFragment(View view) {
        DbHelper.getInstance().getGlobalDao().setDeviceName(new Global.IdAndDeviceName(this.edit_deviceName.getText().toString()));
        refreshDeviceName();
    }

    public /* synthetic */ void lambda$init$3$ServerApiTestFragment(View view) {
        this.mTestDataSetter.showDialogToRestore(TestDataSetter.DataRangeToBeSet.GLOBAL_AND_ALL_PACKAGES, (String) null);
    }

    public /* synthetic */ void lambda$init$5$ServerApiTestFragment(View view) {
        new Thread(new Runnable() {
            public final void run() {
                ServerApiTestFragment.this.lambda$null$4$ServerApiTestFragment();
            }
        }).start();
    }

    public /* synthetic */ void lambda$null$4$ServerApiTestFragment() {
        if (SystemDataHelper.isCollectingAgreedByUser(AppContext.get())) {
            DataUploader.uploadCombinationReportData(getContext(), new NetworkConnector(AppContext.get()));
            return;
        }
        boolean removeAll = ReportDbHelper.getInstance().removeAll();
        GosTestLog.d(LOG_TAG, "User not agreed, remove Ringlog data. successful: " + removeAll);
    }

    public void onResume() {
        GosTestLog.d(LOG_TAG, "onResume()");
        super.onResume();
        int targetServer = DbHelper.getInstance().getGlobalDao().getTargetServer();
        int i = targetServer != 0 ? targetServer != 1 ? R.id.radioBtn_Prd : R.id.radioBtn_Stg : R.id.radioBtn_Dev;
        this.radioGroupURL.check(i);
        this.mLatestCheckedRadioButtonId = i;
        refreshDeviceName();
    }

    /* access modifiers changed from: package-private */
    public void refreshDeviceName() {
        String deviceName = DbHelper.getInstance().getGlobalDao().getDeviceName();
        this.txt_deviceName.setText(deviceName);
        showResult(LOG_TAG, "refreshDeviceName(), deviceName : " + deviceName);
    }
}
