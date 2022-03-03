package com.samsung.android.game.gos.test.fragment;

import android.widget.CompoundButton;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.model.Global;

/* renamed from: com.samsung.android.game.gos.test.fragment.-$$Lambda$FeatureTestFragment$AcOrIYce4kkJBxrdxuxHoVokM_A  reason: invalid class name */
/* compiled from: lambda */
public final /* synthetic */ class $$Lambda$FeatureTestFragment$AcOrIYce4kkJBxrdxuxHoVokM_A implements CompoundButton.OnCheckedChangeListener {
    public static final /* synthetic */ $$Lambda$FeatureTestFragment$AcOrIYce4kkJBxrdxuxHoVokM_A INSTANCE = new $$Lambda$FeatureTestFragment$AcOrIYce4kkJBxrdxuxHoVokM_A();

    private /* synthetic */ $$Lambda$FeatureTestFragment$AcOrIYce4kkJBxrdxuxHoVokM_A() {
    }

    public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        DbHelper.getInstance().getGlobalDao().setAutomaticUpdate(new Global.IdAndAutomaticUpdate(z));
    }
}
