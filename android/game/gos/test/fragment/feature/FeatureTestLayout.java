package com.samsung.android.game.gos.test.fragment.feature;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import com.samsung.android.game.gos.R;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.selibrary.SeActivityManager;
import com.samsung.android.game.gos.test.util.GosTestLog;
import java.util.ArrayList;

public abstract class FeatureTestLayout {
    private static final String LOG_TAG = "FeatureTestLayout";
    protected Context mContext;
    protected TextView mCurrentValueTextView = ((TextView) this.mView.findViewById(R.id.textView_currentValue));
    protected EditText mNewValueEditText = ((EditText) this.mView.findViewById(R.id.editText_newValue));
    protected Button mSetButton = ((Button) this.mView.findViewById(R.id.button_set));
    protected TextView mTitleTextView;
    protected Switch mTurnOnSwitch = ((Switch) this.mView.findViewById(R.id.switch_turnOn));
    protected View mView;

    public abstract void refreshInfo();

    /* access modifiers changed from: protected */
    public abstract void setNewValue();

    public abstract void setPkgData(Package packageR);

    protected FeatureTestLayout(Context context) {
        GosTestLog.d(LOG_TAG, "Constructor, begin");
        this.mContext = context;
        View inflate = LayoutInflater.from(context).inflate(R.layout.row_get_set_value, (ViewGroup) null);
        this.mView = inflate;
        this.mTitleTextView = (TextView) inflate.findViewById(2131296661);
    }

    public View getView() {
        return this.mView;
    }

    /* access modifiers changed from: protected */
    public void showSetValueAndForceToStopDialog(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setTitle("Needs to stop the app");
        builder.setMessage("The app will be forced to stop. Is it OK?");
        builder.setPositiveButton(17039379, new DialogInterface.OnClickListener(str) {
            public final /* synthetic */ String f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(DialogInterface dialogInterface, int i) {
                FeatureTestLayout.this.lambda$showSetValueAndForceToStopDialog$0$FeatureTestLayout(this.f$1, dialogInterface, i);
            }
        }).setNegativeButton(17039369, new DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                FeatureTestLayout.this.lambda$showSetValueAndForceToStopDialog$1$FeatureTestLayout(dialogInterface, i);
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            public final void onCancel(DialogInterface dialogInterface) {
                FeatureTestLayout.this.lambda$showSetValueAndForceToStopDialog$2$FeatureTestLayout(dialogInterface);
            }
        });
        builder.create().show();
    }

    public /* synthetic */ void lambda$showSetValueAndForceToStopDialog$0$FeatureTestLayout(String str, DialogInterface dialogInterface, int i) {
        setNewValue();
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        SeActivityManager.getInstance().forceStopPackages(arrayList);
    }

    public /* synthetic */ void lambda$showSetValueAndForceToStopDialog$1$FeatureTestLayout(DialogInterface dialogInterface, int i) {
        refreshInfo();
    }

    public /* synthetic */ void lambda$showSetValueAndForceToStopDialog$2$FeatureTestLayout(DialogInterface dialogInterface) {
        refreshInfo();
    }

    /* access modifiers changed from: protected */
    public void setEnabledOfUI(boolean z) {
        CharSequence charSequence;
        CharSequence charSequence2;
        if (this.mContext != null) {
            this.mSetButton.setEnabled(z);
            Button button = this.mSetButton;
            if (z) {
                charSequence = this.mContext.getText(R.string.set);
            } else {
                charSequence = this.mContext.getText(R.string.unable);
            }
            button.setText(charSequence);
            this.mTurnOnSwitch.setEnabled(z);
            Switch switchR = this.mTurnOnSwitch;
            if (z) {
                charSequence2 = this.mContext.getText(R.string.enable_feature);
            } else {
                charSequence2 = this.mContext.getText(R.string.not_available);
            }
            switchR.setText(charSequence2);
        }
    }
}
