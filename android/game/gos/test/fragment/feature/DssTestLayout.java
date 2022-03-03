package com.samsung.android.game.gos.test.fragment.feature;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.FeatureHelper;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.feature.dss.DssFeature;
import com.samsung.android.game.gos.gamemanager.GmsGlobalPackageDataSetter;
import com.samsung.android.game.gos.test.util.GosTestLog;
import com.samsung.android.game.gos.value.Constants;

public class DssTestLayout extends FeatureTestLayout {
    private static final String LOG_TAG = "DssTestLayout";
    private static final String TITLE = "DSS (1.0~100.0)";
    private static final String TITLE_DSS1 = "DSS for the 1st OS Resolution (1.0~100.0)";
    private Package mPkg;

    public DssTestLayout(Context context) {
        super(context);
        this.mTitleTextView.setText(TITLE);
        this.mTurnOnSwitch.setVisibility(8);
        this.mSetButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                DssTestLayout.this.lambda$new$0$DssTestLayout(view);
            }
        });
    }

    public /* synthetic */ void lambda$new$0$DssTestLayout(View view) {
        Package packageR = this.mPkg;
        if (packageR != null) {
            showSetValueAndForceToStopDialog(packageR.pkgName);
        } else {
            Toast.makeText(this.mContext, "mPkg is null", 0).show();
        }
    }

    public void setPkgData(Package packageR) {
        this.mPkg = packageR;
    }

    public void refreshInfo() {
        if (!(!FeatureHelper.isAvailable(Constants.V4FeatureFlag.RESOLUTION) || this.mPkg == null || this.mCurrentValueTextView == null)) {
            float defaultDss = DssFeature.getDefaultDss(this.mPkg);
            GosTestLog.d(LOG_TAG, "refreshInfo(), defaultDss: " + defaultDss);
            this.mCurrentValueTextView.setText(String.valueOf(defaultDss));
        }
        setEnabledOfUI(FeatureHelper.isAvailable(Constants.V4FeatureFlag.RESOLUTION));
    }

    /* access modifiers changed from: protected */
    public void setNewValue() {
        if (!(this.mNewValueEditText == null || this.mPkg == null)) {
            GosTestLog.d(LOG_TAG, "setNewValue(), pkg name : " + this.mPkg.pkgName);
            try {
                float parseFloat = Float.parseFloat(this.mNewValueEditText.getText().toString());
                float[] eachModeDssArray = this.mPkg.getEachModeDssArray();
                if (eachModeDssArray == null) {
                    eachModeDssArray = new float[]{-1.0f, -1.0f, -1.0f, -1.0f};
                }
                eachModeDssArray[1] = parseFloat;
                this.mPkg.setEachModeDss(eachModeDssArray);
                DbHelper.getInstance().getPackageDao().insertOrUpdate(this.mPkg);
                GmsGlobalPackageDataSetter.getInstance().applySingleGame(this.mPkg.pkgName);
            } catch (Exception e) {
                GosTestLog.w(LOG_TAG, (Throwable) e);
            }
        }
        refreshInfo();
    }

    public void switchTitle() {
        this.mTitleTextView.setText(TITLE_DSS1);
    }
}
