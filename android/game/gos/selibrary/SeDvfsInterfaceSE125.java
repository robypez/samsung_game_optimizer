package com.samsung.android.game.gos.selibrary;

import android.content.Context;
import com.samsung.android.game.gos.value.AppVariable;
import com.samsung.android.os.SemDvfsManager;

public class SeDvfsInterfaceSE125 extends SeDvfsInterfaceImpl {
    private SeDvfsInterfaceSE125(Context context, int i) {
        this.mInstance = SemDvfsManager.createInstance(context, context.getPackageName());
        this.mType = i;
    }

    public static SeDvfsInterfaceSE125 createInstance(Context context, int i) {
        return new SeDvfsInterfaceSE125(context, i);
    }

    public int[] getSupportedFrequency() {
        if (AppVariable.isUnitTest()) {
            return DEF_FREQUENCY;
        }
        return this.mInstance.getSupportedFrequency(this.mType, 1);
    }

    public int[] getSupportedFrequencyForSsrm() {
        if (AppVariable.isUnitTest()) {
            return DEF_FREQUENCY;
        }
        return this.mInstance.getSupportedFrequency(this.mType, 0);
    }

    public void setDvfsValue(long j) {
        if (!AppVariable.isUnitTest()) {
            this.mInstance.addResourceValue(this.mType, (int) j);
        }
    }
}
