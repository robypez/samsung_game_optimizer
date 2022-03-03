package com.samsung.android.game.gos.feature.externalsdk;

interface IReportStrategy {
    boolean isAvailable();

    long setListener(long j, IExternalSdkListener iExternalSdkListener);

    boolean startWatching();

    boolean stopWatching();
}
