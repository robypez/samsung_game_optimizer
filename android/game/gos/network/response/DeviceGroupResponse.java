package com.samsung.android.game.gos.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceGroupResponse extends BaseResponse {
    @SerializedName("device_group_name")
    @Expose
    private String deviceGroupName;

    public String toString() {
        return String.format("{\"device_group_name\":\"%s\"}", new Object[]{this.deviceGroupName});
    }

    public String getDeviceGroupName() {
        return this.deviceGroupName;
    }

    public void setDeviceGroupName(String str) {
        this.deviceGroupName = str;
    }
}
