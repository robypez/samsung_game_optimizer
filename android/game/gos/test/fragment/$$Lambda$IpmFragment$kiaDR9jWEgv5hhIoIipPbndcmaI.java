package com.samsung.android.game.gos.test.fragment;

import android.widget.CompoundButton;
import com.samsung.android.game.gos.feature.ipm.IpmFeature;

/* renamed from: com.samsung.android.game.gos.test.fragment.-$$Lambda$IpmFragment$kiaDR9jWEgv5hhIoIipPbndcmaI  reason: invalid class name */
/* compiled from: lambda */
public final /* synthetic */ class $$Lambda$IpmFragment$kiaDR9jWEgv5hhIoIipPbndcmaI implements CompoundButton.OnCheckedChangeListener {
    public static final /* synthetic */ $$Lambda$IpmFragment$kiaDR9jWEgv5hhIoIipPbndcmaI INSTANCE = new $$Lambda$IpmFragment$kiaDR9jWEgv5hhIoIipPbndcmaI();

    private /* synthetic */ $$Lambda$IpmFragment$kiaDR9jWEgv5hhIoIipPbndcmaI() {
    }

    public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        IpmFeature.mUseJSONPolicy = z;
    }
}
