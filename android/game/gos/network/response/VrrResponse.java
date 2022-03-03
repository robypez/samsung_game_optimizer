package com.samsung.android.game.gos.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VrrResponse extends FeatureResponse {
    @SerializedName("display_hz")
    @Expose
    public DisplayHz displayHz;
    @SerializedName("drr_allowed")
    @Expose
    public Boolean drrAllowed;
    @SerializedName("mode")
    @Expose
    public Mode mode;

    public static class DisplayHz extends BaseResponse {
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

    public static class Mode extends BaseResponse {
        @SerializedName("forced")
        @Expose
        public Integer forced;
    }
}
