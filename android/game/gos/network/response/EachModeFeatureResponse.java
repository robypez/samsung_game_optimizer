package com.samsung.android.game.gos.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Locale;

public class EachModeFeatureResponse extends FeatureResponse {
    @SerializedName("mode_values")
    @Expose
    public EachModeValues modeValues;

    public static class EachModeValues extends BaseResponse {
        @SerializedName("optimized")
        @Expose
        public Double optimized;
        @SerializedName("power_save")
        @Expose
        public Double powerSave;
        @SerializedName("ultra_power_save")
        @Expose
        public Double ultraPowerSave;
        @SerializedName("unmanaged")
        @Expose
        public Double unmanaged;
    }

    public String getEachModeIntValueCsv() {
        Locale locale = Locale.US;
        Object[] objArr = new Object[4];
        int i = 0;
        objArr[0] = Integer.valueOf(this.modeValues.unmanaged != null ? this.modeValues.unmanaged.intValue() : 0);
        objArr[1] = Integer.valueOf(this.modeValues.optimized != null ? this.modeValues.optimized.intValue() : 0);
        objArr[2] = Integer.valueOf(this.modeValues.powerSave != null ? this.modeValues.powerSave.intValue() : 0);
        if (this.modeValues.ultraPowerSave != null) {
            i = this.modeValues.ultraPowerSave.intValue();
        }
        objArr[3] = Integer.valueOf(i);
        return String.format(locale, "%d,%d,%d,%d", objArr);
    }

    public String getEachModeFloatValueCsv() {
        Locale locale = Locale.US;
        Object[] objArr = new Object[4];
        float f = 0.0f;
        objArr[0] = Float.valueOf(this.modeValues.unmanaged != null ? this.modeValues.unmanaged.floatValue() : 0.0f);
        objArr[1] = Float.valueOf(this.modeValues.optimized != null ? this.modeValues.optimized.floatValue() : 0.0f);
        objArr[2] = Float.valueOf(this.modeValues.powerSave != null ? this.modeValues.powerSave.floatValue() : 0.0f);
        if (this.modeValues.ultraPowerSave != null) {
            f = this.modeValues.ultraPowerSave.floatValue();
        }
        objArr[3] = Float.valueOf(f);
        return String.format(locale, "%.2f,%.2f,%.2f,%.2f", objArr);
    }
}
