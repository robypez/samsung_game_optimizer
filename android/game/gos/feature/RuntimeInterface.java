package com.samsung.android.game.gos.feature;

import com.samsung.android.game.gos.data.PkgData;

public interface RuntimeInterface extends CommonInterface {
    void onFocusIn(PkgData pkgData, boolean z);

    void onFocusOut(PkgData pkgData);

    void restoreDefault(PkgData pkgData);
}
