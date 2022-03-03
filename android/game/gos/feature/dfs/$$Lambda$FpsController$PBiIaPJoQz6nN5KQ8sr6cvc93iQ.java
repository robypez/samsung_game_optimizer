package com.samsung.android.game.gos.feature.dfs;

import android.view.Display;
import java.util.function.ToDoubleFunction;

/* renamed from: com.samsung.android.game.gos.feature.dfs.-$$Lambda$FpsController$PBiIaPJoQz6nN5KQ8sr6cvc93iQ  reason: invalid class name */
/* compiled from: lambda */
public final /* synthetic */ class $$Lambda$FpsController$PBiIaPJoQz6nN5KQ8sr6cvc93iQ implements ToDoubleFunction {
    public static final /* synthetic */ $$Lambda$FpsController$PBiIaPJoQz6nN5KQ8sr6cvc93iQ INSTANCE = new $$Lambda$FpsController$PBiIaPJoQz6nN5KQ8sr6cvc93iQ();

    private /* synthetic */ $$Lambda$FpsController$PBiIaPJoQz6nN5KQ8sr6cvc93iQ() {
    }

    public final double applyAsDouble(Object obj) {
        return (double) ((Display.Mode) obj).getRefreshRate();
    }
}
