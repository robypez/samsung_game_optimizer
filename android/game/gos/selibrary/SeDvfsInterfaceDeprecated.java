package com.samsung.android.game.gos.selibrary;

import android.content.Context;
import com.samsung.android.game.gos.value.AppVariable;
import com.samsung.android.os.SemDvfsManager;

public class SeDvfsInterfaceDeprecated extends SeDvfsInterfaceImpl {
    private SeDvfsInterfaceDeprecated(Context context, int i) {
        this.mInstance = SemDvfsManager.createInstance(context, context.getPackageName(), i);
    }

    public static SeDvfsInterfaceImpl createInstance(Context context, int i) {
        return new SeDvfsInterfaceDeprecated(context, i);
    }

    public int[] getSupportedFrequency() {
        if (AppVariable.isUnitTest()) {
            return DEF_FREQUENCY;
        }
        return this.mInstance.getSupportedFrequency();
    }

    public int[] getSupportedFrequencyForSsrm() {
        if (AppVariable.isUnitTest()) {
            return DEF_FREQUENCY;
        }
        return this.mInstance.getSupportedFrequencyForSsrm();
    }

    public void setDvfsValue(long j) {
        if (!AppVariable.isUnitTest()) {
            this.mInstance.setDvfsValue((int) j);
        }
    }
}
