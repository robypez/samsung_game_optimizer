package com.samsung.android.game.gos.test.fragment.feature;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import androidx.core.view.InputDeviceCompat;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.gamemanager.GmsGlobalPackageDataSetter;
import com.samsung.android.game.gos.selibrary.SeSysProp;
import com.samsung.android.game.gos.test.util.GosTestLog;

public class CpuTestLayout extends FeatureTestLayout {
    private static final String LOG_TAG = "CpuTestLayout";
    private static final String TITLE = ("CPU level (" + SeSysProp.getCpuGpuLevelInstance().getCpuLevelsCsv() + ")");
    private String mPkgName = null;

    public CpuTestLayout(Context context) {
        super(context);
        this.mTitleTextView.setText(TITLE);
        this.mTurnOnSwitch.setVisibility(8);
        this.mNewValueEditText.setInputType(InputDeviceCompat.SOURCE_TOUCHSCREEN);
        this.mSetButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CpuTestLayout.this.lambda$new$0$CpuTestLayout(view);
            }
        });
    }

    public /* synthetic */ void lambda$new$0$CpuTestLayout(View view) {
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
        if (!(this.mCurrentValueTextView == null || (packageR = DbHelper.getInstance().getPackageDao().getPackage(this.mPkgName)) == null)) {
            int defaultCpuLevel = packageR.getDefaultCpuLevel();
            GosTestLog.d(LOG_TAG, "refreshInfo(), defaultCpuLevel: " + defaultCpuLevel);
            this.mCurrentValueTextView.setText(String.valueOf(defaultCpuLevel));
        }
        setEnabledOfUI(true);
    }

    /* access modifiers changed from: protected */
    public void setNewValue() {
        if (this.mNewValueEditText != null) {
            GosTestLog.d(LOG_TAG, "setNewValue(), pkg name : " + this.mPkgName);
            try {
                DbHelper.getInstance().getPackageDao().setDefaultCpuLevel(new Package.PkgNameAndDefaultCpuLevel(this.mPkgName, Integer.parseInt(this.mNewValueEditText.getText().toString())));
                GmsGlobalPackageDataSetter.getInstance().applySingleGame(this.mPkgName);
            } catch (Exception e) {
                GosTestLog.w(LOG_TAG, (Throwable) e);
            }
        }
        refreshInfo();
    }
}
