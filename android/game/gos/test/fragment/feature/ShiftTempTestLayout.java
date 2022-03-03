package com.samsung.android.game.gos.test.fragment.feature;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import androidx.core.view.InputDeviceCompat;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.gamemanager.GmsGlobalPackageDataSetter;
import com.samsung.android.game.gos.test.util.GosTestLog;

public class ShiftTempTestLayout extends FeatureTestLayout {
    private static final String LOG_TAG = "ShiftTempTestLayout";
    private static final String TITLE = "Shift Temperature";
    private String mPkgName = null;

    public ShiftTempTestLayout(Context context) {
        super(context);
        this.mTitleTextView.setText(TITLE);
        this.mTurnOnSwitch.setVisibility(8);
        this.mNewValueEditText.setInputType(InputDeviceCompat.SOURCE_TOUCHSCREEN);
        this.mSetButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ShiftTempTestLayout.this.lambda$new$0$ShiftTempTestLayout(view);
            }
        });
    }

    public /* synthetic */ void lambda$new$0$ShiftTempTestLayout(View view) {
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
            int shiftTemperature = packageR.getShiftTemperature();
            GosTestLog.d(LOG_TAG, "refreshInfo(), defaultShiftTemp: " + shiftTemperature);
            this.mCurrentValueTextView.setText(String.valueOf(shiftTemperature));
        }
        setEnabledOfUI(true);
    }

    /* access modifiers changed from: protected */
    public void setNewValue() {
        if (this.mNewValueEditText != null) {
            GosTestLog.d(LOG_TAG, "setNewValue(), pkg name : " + this.mPkgName);
            try {
                DbHelper.getInstance().getPackageDao().setShiftTemperature(new Package.PkgNameAndShiftTemperature(this.mPkgName, Integer.parseInt(this.mNewValueEditText.getText().toString())));
                GmsGlobalPackageDataSetter.getInstance().applySingleGame(this.mPkgName);
            } catch (Exception e) {
                GosTestLog.w(LOG_TAG, (Throwable) e);
            }
        }
        refreshInfo();
    }
}
