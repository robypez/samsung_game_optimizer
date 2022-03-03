package com.samsung.android.game.gos.test.fragment.feature;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.FeatureHelper;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.gamemanager.GmsGlobalPackageDataSetter;
import com.samsung.android.game.gos.test.util.GosTestLog;
import com.samsung.android.game.gos.value.Constants;

public class TspPolicyJsonTestLayout extends FeatureTestLayout {
    private static final String LOG_TAG = "TspPolicyJsonTestLayout";
    private static final String TITLE = "TSP policy (JSON)";
    private String mPkgName = null;

    public TspPolicyJsonTestLayout(Context context) {
        super(context);
        this.mTitleTextView.setText(TITLE);
        this.mTurnOnSwitch.setVisibility(8);
        this.mCurrentValueTextView.setVisibility(0);
        this.mNewValueEditText.setVisibility(0);
        this.mNewValueEditText.setInputType(1);
        this.mSetButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                TspPolicyJsonTestLayout.this.lambda$new$0$TspPolicyJsonTestLayout(view);
            }
        });
    }

    public /* synthetic */ void lambda$new$0$TspPolicyJsonTestLayout(View view) {
        showSetValueAndForceToStopDialog(this.mPkgName);
    }

    public void setPkgData(Package packageR) {
        if (packageR != null) {
            this.mPkgName = packageR.getPkgName();
        } else {
            Toast.makeText(this.mContext, "pkg is null", 0).show();
        }
    }

    public void refreshInfo() {
        Package packageR;
        GosTestLog.d(LOG_TAG, "refreshInfo(), begin");
        if (FeatureHelper.isAvailable(Constants.V4FeatureFlag.TSP) && (packageR = DbHelper.getInstance().getPackageDao().getPackage(this.mPkgName)) != null) {
            String tspPolicy = packageR.getTspPolicy();
            GosTestLog.d(LOG_TAG, "refreshInfo(), tspModePolicy: " + tspPolicy);
            if (tspPolicy == null) {
                tspPolicy = "null";
            }
            if (this.mCurrentValueTextView != null) {
                this.mCurrentValueTextView.setText(tspPolicy);
            }
            if (this.mNewValueEditText != null) {
                this.mNewValueEditText.setText(tspPolicy);
            }
        }
        setEnabledOfUI(FeatureHelper.isAvailable(Constants.V4FeatureFlag.TSP));
    }

    /* access modifiers changed from: protected */
    public void setNewValue() {
        if (this.mNewValueEditText != null) {
            GosTestLog.d(LOG_TAG, "setNewValue(), pkg name : " + this.mPkgName);
            try {
                String obj = this.mNewValueEditText.getText().toString();
                GosTestLog.d(LOG_TAG, "newValue: " + obj);
                if (obj.equals("null") || obj.length() < 5) {
                    obj = null;
                    GosTestLog.d(LOG_TAG, "newValue real: null");
                }
                if (obj != null) {
                    DbHelper.getInstance().getPackageDao().setTspPolicy(new Package.PkgNameAndTspPolicy(this.mPkgName, obj));
                    GmsGlobalPackageDataSetter.getInstance().applySingleGame(this.mPkgName);
                }
            } catch (Exception e) {
                GosTestLog.w(LOG_TAG, (Throwable) e);
            }
        }
        refreshInfo();
    }
}
