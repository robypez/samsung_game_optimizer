package com.samsung.android.game.gos.test.fragment.feature;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.FeatureHelper;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.gamemanager.GmsGlobalPackageDataSetter;
import com.samsung.android.game.gos.test.util.GosTestLog;

public class SiopModePolicyJsonTestLayout extends FeatureTestLayout {
    private static final String LOG_TAG = "SiopModePolicyJsonTestLayout";
    private static final String TITLE = "siop_mode_policy (JSON)";
    private String mPkgName = null;

    public SiopModePolicyJsonTestLayout(Context context) {
        super(context);
        this.mTitleTextView.setText(TITLE);
        this.mTurnOnSwitch.setVisibility(8);
        this.mCurrentValueTextView.setVisibility(0);
        this.mNewValueEditText.setVisibility(0);
        this.mNewValueEditText.setInputType(1);
        this.mSetButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                SiopModePolicyJsonTestLayout.this.lambda$new$0$SiopModePolicyJsonTestLayout(view);
            }
        });
    }

    public /* synthetic */ void lambda$new$0$SiopModePolicyJsonTestLayout(View view) {
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
        if (FeatureHelper.isAvailable("siop_mode") && (packageR = DbHelper.getInstance().getPackageDao().getPackage(this.mPkgName)) != null) {
            String siopModePolicy = packageR.getSiopModePolicy();
            GosTestLog.d(LOG_TAG, "refreshInfo(), siopModePolicy: " + siopModePolicy);
            if (siopModePolicy == null) {
                siopModePolicy = "null";
            }
            if (this.mCurrentValueTextView != null) {
                this.mCurrentValueTextView.setText(siopModePolicy);
            }
            if (this.mNewValueEditText != null) {
                this.mNewValueEditText.setText(siopModePolicy);
            }
        }
        setEnabledOfUI(FeatureHelper.isAvailable("siop_mode"));
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
                DbHelper.getInstance().getPackageDao().setSiopModePolicy(new Package.PkgNameAndSiopModePolicy(this.mPkgName, obj));
                GmsGlobalPackageDataSetter.getInstance().applySingleGame(this.mPkgName);
            } catch (Exception e) {
                GosTestLog.w(LOG_TAG, (Throwable) e);
            }
        }
        refreshInfo();
    }
}
