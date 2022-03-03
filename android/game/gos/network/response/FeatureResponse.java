package com.samsung.android.game.gos.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeatureResponse extends BaseResponse {
    @SerializedName("state")
    @Expose
    public String state;
}
