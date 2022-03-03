package com.samsung.android.game.gos.test.fragment;

import android.widget.CompoundButton;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.model.Global;

/* renamed from: com.samsung.android.game.gos.test.fragment.-$$Lambda$GlobalDataFragment$QMKwMthy4qj1e8FY8NPzWEPwf1A  reason: invalid class name */
/* compiled from: lambda */
public final /* synthetic */ class $$Lambda$GlobalDataFragment$QMKwMthy4qj1e8FY8NPzWEPwf1A implements CompoundButton.OnCheckedChangeListener {
    public static final /* synthetic */ $$Lambda$GlobalDataFragment$QMKwMthy4qj1e8FY8NPzWEPwf1A INSTANCE = new $$Lambda$GlobalDataFragment$QMKwMthy4qj1e8FY8NPzWEPwf1A();

    private /* synthetic */ $$Lambda$GlobalDataFragment$QMKwMthy4qj1e8FY8NPzWEPwf1A() {
    }

    public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        DbHelper.getInstance().getGlobalDao().setAutomaticUpdate(new Global.IdAndAutomaticUpdate(z));
    }
}
