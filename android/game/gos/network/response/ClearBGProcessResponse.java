package com.samsung.android.game.gos.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ClearBGProcessResponse extends FeatureResponse {
    @SerializedName("survive_app_list")
    @Expose
    public List<String> surviveAppList;
}
