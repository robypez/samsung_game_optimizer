package com.samsung.android.game.gos.network;

import com.google.gson.JsonObject;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

interface GameModePostService {
    public static final String POST_DEV_URL = "https://c-api.dev-gos-gsp.io/";
    public static final String POST_PRD_URL = "https://c-api.gos-gsp.io/";
    public static final String POST_STG_URL = "https://c-api.stg-gos-gsp.io/";

    @POST("v4/glog")
    @Headers({"Accept: application/json;charset=UTF-8"})
    Call<JsonObject> post(@Body RequestBody requestBody);
}
