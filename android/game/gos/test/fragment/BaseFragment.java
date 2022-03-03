package com.samsung.android.game.gos.test.fragment;

import androidx.fragment.app.Fragment;
import com.samsung.android.game.gos.test.util.GosTestLog;

public abstract class BaseFragment extends Fragment {
    public abstract int getNavItemId();

    /* access modifiers changed from: protected */
    public void showResult(String str, String str2) {
        GosTestLog.d(str, str2);
        TestActivity testActivity = (TestActivity) getActivity();
        if (testActivity != null) {
            testActivity.showResult(str2);
        }
    }
}
