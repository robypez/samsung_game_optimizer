package com.samsung.android.game.gos.test.fragment.feature;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.FeatureHelper;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.feature.dss.DssFeature;
import com.samsung.android.game.gos.feature.dss.TssCore;
import com.samsung.android.game.gos.gamemanager.GmsGlobalPackageDataSetter;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.test.util.GosTestLog;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.value.Constants;
import java.util.Locale;

public class TssTestLayout extends FeatureTestLayout {
    private static final String LOG_TAG = TssTestLayout.class.getSimpleName();
    private static final String TITLE = "Target Short Side";
    private Package mPkg;

    public TssTestLayout(Context context) {
        super(context);
        this.mTitleTextView.setText(TITLE);
        this.mTurnOnSwitch.setVisibility(8);
        this.mSetButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                TssTestLayout.this.lambda$new$0$TssTestLayout(view);
            }
        });
    }

    public /* synthetic */ void lambda$new$0$TssTestLayout(View view) {
        Package packageR = this.mPkg;
        if (packageR != null) {
            showSetValueAndForceToStopDialog(packageR.pkgName);
        } else {
            Toast.makeText(this.mContext, "mPkg is null", 0).show();
        }
    }

    public void refreshInfo() {
        String str;
        int i;
        GosTestLog.d(LOG_TAG, "refreshInfo(), begin");
        if (!(this.mContext == null || !TssCore.isAvailable() || this.mCurrentValueTextView == null)) {
            this.mTitleTextView.setText(String.format(Locale.US, "%s (1~%d)", new Object[]{TITLE, Integer.valueOf(DssFeature.getInstance().getDisplayShortSide())}));
            int[] csvToInts = TypeConverter.csvToInts(DbHelper.getInstance().getGlobalDao().getEachModeTargetShortSide());
            if (csvToInts == null || csvToInts.length <= 1) {
                str = "not set";
            } else {
                int i2 = csvToInts[1];
                Package packageR = this.mPkg;
                if (packageR != null && packageR.getEachModeTargetShortSide() != null && !this.mPkg.getEachModeTargetShortSide().equals(BuildConfig.VERSION_NAME) && (i = this.mPkg.getEachModeTargetShortSideArray()[1]) > 0) {
                    i2 = i;
                }
                String str2 = LOG_TAG;
                GosTestLog.d(str2, "refreshInfo(), defaultTss: " + i2);
                str = String.valueOf(i2);
            }
            this.mCurrentValueTextView.setText(str);
        }
        setEnabledOfUI(FeatureHelper.isAvailable(Constants.V4FeatureFlag.RESOLUTION));
    }

    public void setPkgData(Package packageR) {
        this.mPkg = packageR;
    }

    /* access modifiers changed from: protected */
    public void setNewValue() {
        if (!(this.mNewValueEditText == null || this.mPkg == null)) {
            String str = LOG_TAG;
            GosTestLog.d(str, "setNewValue(), pkg name : " + this.mPkg.pkgName);
            try {
                int parseInt = Integer.parseInt(this.mNewValueEditText.getText().toString());
                int[] eachModeTargetShortSideArray = this.mPkg.getEachModeTargetShortSideArray();
                if (eachModeTargetShortSideArray == null) {
                    eachModeTargetShortSideArray = new int[]{-1, -1, -1, -1};
                }
                eachModeTargetShortSideArray[1] = parseInt;
                this.mPkg.setEachModeTargetShortSide(eachModeTargetShortSideArray);
                DbHelper.getInstance().getPackageDao().insertOrUpdate(this.mPkg);
                GmsGlobalPackageDataSetter.getInstance().applySingleGame(this.mPkg.pkgName);
            } catch (Exception e) {
                GosTestLog.w(LOG_TAG, (Throwable) e);
            }
        }
        refreshInfo();
    }
}
