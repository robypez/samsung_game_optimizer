package com.samsung.android.game.gos.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GfiResponse extends FeatureResponse {
    @SerializedName("dfs_offset")
    @Expose
    private DfsOffset dfsOffset;
    @SerializedName("game_fps_limit")
    @Expose
    private Integer gameFpsLimit;
    @SerializedName("gfi_maximum_version")
    @Expose
    private String gfiMaximumVersion;
    @SerializedName("gfi_minimum_version")
    @Expose
    private String gfiMinimumVersion;
    @SerializedName("gfps_offset")
    @Expose
    private GfpsOffset gfpsOffset;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("smart_delay")
    @Expose
    private Boolean smartDelay;
    @SerializedName("target_dfs")
    @Expose
    private Integer targetDfs;

    public static class DfsOffset extends BaseResponse {
        @SerializedName("enabled")
        @Expose
        private Boolean enabled;
        @SerializedName("maximum")
        @Expose
        private Integer maximum;
        @SerializedName("minimum")
        @Expose
        private Integer minimum;
        @SerializedName("smoothness")
        @Expose
        private Double smoothness;
        @SerializedName("value")
        @Expose
        private Integer value;
    }

    public static class GfpsOffset extends BaseResponse {
        @SerializedName("enabled")
        @Expose
        private Boolean enabled;
        @SerializedName("maximum")
        @Expose
        private Integer maximum;
        @SerializedName("minimum")
        @Expose
        private Integer minimum;
        @SerializedName("value")
        @Expose
        private Integer value;
    }
}
