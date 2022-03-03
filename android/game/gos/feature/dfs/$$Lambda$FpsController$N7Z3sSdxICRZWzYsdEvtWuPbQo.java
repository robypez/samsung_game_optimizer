package com.samsung.android.game.gos.feature.dfs;

import android.view.Display;
import java.util.function.Function;

/* renamed from: com.samsung.android.game.gos.feature.dfs.-$$Lambda$FpsController$N7Z3sS-dxICRZWzYsdEvtWuPbQo  reason: invalid class name */
/* compiled from: lambda */
public final /* synthetic */ class $$Lambda$FpsController$N7Z3sSdxICRZWzYsdEvtWuPbQo implements Function {
    public static final /* synthetic */ $$Lambda$FpsController$N7Z3sSdxICRZWzYsdEvtWuPbQo INSTANCE = new $$Lambda$FpsController$N7Z3sSdxICRZWzYsdEvtWuPbQo();

    private /* synthetic */ $$Lambda$FpsController$N7Z3sSdxICRZWzYsdEvtWuPbQo() {
    }

    public final Object apply(Object obj) {
        return Float.valueOf(((Display.Mode) obj).getRefreshRate());
    }
}
