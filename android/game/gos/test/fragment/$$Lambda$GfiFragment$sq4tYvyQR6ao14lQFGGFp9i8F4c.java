package com.samsung.android.game.gos.test.fragment;

import android.widget.CompoundButton;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.feature.gfi.GfiFeature;

/* renamed from: com.samsung.android.game.gos.test.fragment.-$$Lambda$GfiFragment$sq4tYvyQR6ao14lQFGGFp9i8F4c  reason: invalid class name */
/* compiled from: lambda */
public final /* synthetic */ class $$Lambda$GfiFragment$sq4tYvyQR6ao14lQFGGFp9i8F4c implements CompoundButton.OnCheckedChangeListener {
    public static final /* synthetic */ $$Lambda$GfiFragment$sq4tYvyQR6ao14lQFGGFp9i8F4c INSTANCE = new $$Lambda$GfiFragment$sq4tYvyQR6ao14lQFGGFp9i8F4c();

    private /* synthetic */ $$Lambda$GfiFragment$sq4tYvyQR6ao14lQFGGFp9i8F4c() {
    }

    public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        GfiFeature.getInstance(AppContext.get()).mApplyWatchdogMaxFpsLimit = z;
    }
}
