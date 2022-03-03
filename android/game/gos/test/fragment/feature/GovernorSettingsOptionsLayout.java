package com.samsung.android.game.gos.test.fragment.feature;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.samsung.android.game.gos.R;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.test.util.GosTestLog;
import org.json.JSONException;
import org.json.JSONObject;

public class GovernorSettingsOptionsLayout {
    private static final String LOG_TAG = "GovernorSettingsOptionsLayout";
    protected Context mContext;
    protected TextView mCurrentValueTextView = ((TextView) this.mView.findViewById(R.id.textView_currentValue));
    protected EditText mNewValueEditText = ((EditText) this.mView.findViewById(R.id.editText_newValue));
    protected Button mSetButton;
    protected TextView mTitleTextView;
    private View mView;

    public GovernorSettingsOptionsLayout(Context context, String str, Package packageR) {
        String str2;
        GosTestLog.d(LOG_TAG, "Constructor, begin");
        this.mContext = context;
        View inflate = LayoutInflater.from(context).inflate(R.layout.row_governor_settings_options, (ViewGroup) null);
        this.mView = inflate;
        TextView textView = (TextView) inflate.findViewById(2131296661);
        this.mTitleTextView = textView;
        textView.setText(str);
        if (packageR == null) {
            try {
                str2 = DbHelper.getInstance().getGlobalDao().getGovernorSettings();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            str2 = packageR.getGovernorSettings();
        }
        if (str2 != null) {
            String string = new JSONObject(str2).getString(str);
            this.mCurrentValueTextView.setText(str2);
            this.mNewValueEditText.setText(string);
        }
        Button button = (Button) this.mView.findViewById(R.id.button_set);
        this.mSetButton = button;
        button.setOnClickListener(new View.OnClickListener(str, packageR) {
            public final /* synthetic */ String f$1;
            public final /* synthetic */ Package f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void onClick(View view) {
                GovernorSettingsOptionsLayout.this.lambda$new$0$GovernorSettingsOptionsLayout(this.f$1, this.f$2, view);
            }
        });
    }

    public /* synthetic */ void lambda$new$0$GovernorSettingsOptionsLayout(String str, Package packageR, View view) {
        setNewValue(str, packageR);
    }

    public View getView() {
        return this.mView;
    }

    /* access modifiers changed from: protected */
    public void setEnabledOfUI(boolean z) {
        CharSequence charSequence;
        this.mSetButton.setEnabled(z);
        Button button = this.mSetButton;
        if (z) {
            charSequence = this.mContext.getText(R.string.set);
        } else {
            charSequence = this.mContext.getText(R.string.unable);
        }
        button.setText(charSequence);
    }

    /* access modifiers changed from: protected */
    public void setNewValue(String str, Package packageR) {
        if (this.mNewValueEditText.getText() == null) {
            return;
        }
        if (packageR == null) {
            try {
                Global global = DbHelper.getInstance().getGlobalDao().get();
                if (global != null) {
                    JSONObject jSONObject = global.governorSettings != null ? new JSONObject(DbHelper.getInstance().getGlobalDao().getGovernorSettings()) : new JSONObject();
                    jSONObject.put(str, this.mNewValueEditText.getText());
                    global.governorSettings = jSONObject.toString();
                    DbHelper.getInstance().getGlobalDao().insertOrUpdate(global);
                    this.mCurrentValueTextView.setText(jSONObject.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            JSONObject jSONObject2 = packageR.getGovernorSettings() != null ? new JSONObject(packageR.getGovernorSettings()) : new JSONObject();
            jSONObject2.put(str, this.mNewValueEditText.getText());
            packageR.setGovernorSettings(jSONObject2.toString());
            DbHelper.getInstance().getPackageDao().insertOrUpdate(packageR);
            this.mCurrentValueTextView.setText(jSONObject2.toString());
        }
    }
}
