package com.samsung.android.game.gos.test.fragment;

import android.widget.CompoundButton;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.feature.gfi.GfiFeature;

/* renamed from: com.samsung.android.game.gos.test.fragment.-$$Lambda$GfiFragment$V2xkMzrJms-6oNGtYvU_Cdy9PeU  reason: invalid class name */
/* compiled from: lambda */
public final /* synthetic */ class $$Lambda$GfiFragment$V2xkMzrJms6oNGtYvU_Cdy9PeU implements CompoundButton.OnCheckedChangeListener {
    public static final /* synthetic */ $$Lambda$GfiFragment$V2xkMzrJms6oNGtYvU_Cdy9PeU INSTANCE = new $$Lambda$GfiFragment$V2xkMzrJms6oNGtYvU_Cdy9PeU();

    private /* synthetic */ $$Lambda$GfiFragment$V2xkMzrJms6oNGtYvU_Cdy9PeU() {
    }

    public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        GfiFeature.getInstance(AppContext.get()).setKeepTwoHwcLayers(z);
    }
}
