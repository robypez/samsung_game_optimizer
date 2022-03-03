package com.samsung.android.game.gos.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TspResponse extends FeatureResponse {
    @SerializedName("touch_hz")
    @Expose
    public TouchHz touchHz;

    public static class TouchHz extends BaseResponse {
        @SerializedName("forced")
        @Expose
        public Integer forced;
        @SerializedName("max")
        @Expose
        public Integer max;
        @SerializedName("min")
        @Expose
        public Integer min;
    }
}
