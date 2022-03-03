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
import java.util.Locale;

public class VrrMinTestLayout extends FeatureTestLayout {
    private static final String LOG_TAG = "VrrMinTestLayout";
    private static final String TITLE = "VRR min (48, 60, 96, 120)";
    private String mPkgName = null;

    public VrrMinTestLayout(Context context) {
        super(context);
        this.mTitleTextView.setText(TITLE);
        this.mTurnOnSwitch.setVisibility(8);
        this.mNewValueEditText.setInputType(8194);
        this.mSetButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                VrrMinTestLayout.this.lambda$new$0$VrrMinTestLayout(view);
            }
        });
    }

    public /* synthetic */ void lambda$new$0$VrrMinTestLayout(View view) {
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
        if (!(!FeatureHelper.isAvailable(Constants.V4FeatureFlag.VRR) || this.mCurrentValueTextView == null || (packageR = DbHelper.getInstance().getPackageDao().getPackage(this.mPkgName)) == null)) {
            int i = packageR.vrrMinValue;
            GosTestLog.d(LOG_TAG, "refreshInfo(), VrrMinValue: " + i);
            int vrrMinValue = DbHelper.getInstance().getGlobalDao().getVrrMinValue();
            this.mCurrentValueTextView.setText(String.format(Locale.US, "%d (global: %d)", new Object[]{Integer.valueOf(i), Integer.valueOf(vrrMinValue)}));
        }
        setEnabledOfUI(FeatureHelper.isAvailable(Constants.V4FeatureFlag.VRR));
    }

    /* access modifiers changed from: protected */
    public void setNewValue() {
        if (this.mNewValueEditText != null) {
            GosTestLog.d(LOG_TAG, "setNewValue(), pkg name : " + this.mPkgName);
            try {
                DbHelper.getInstance().getPackageDao().setVrrMinValue(new Package.PkgNameAndVrrMinValue(this.mPkgName, Integer.parseInt(this.mNewValueEditText.getText().toString())));
                GmsGlobalPackageDataSetter.getInstance().applySingleGame(this.mPkgName);
            } catch (Exception e) {
                GosTestLog.w(LOG_TAG, (Throwable) e);
            }
        }
        refreshInfo();
    }
}
