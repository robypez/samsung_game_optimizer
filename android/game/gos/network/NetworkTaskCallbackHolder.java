package com.samsung.android.game.gos.network;

public class NetworkTaskCallbackHolder {
    private INetworkTaskCallback cb;

    private NetworkTaskCallbackHolder() {
    }

    private static class SingletonHolder {
        static NetworkTaskCallbackHolder INSTANCE = new NetworkTaskCallbackHolder();

        private SingletonHolder() {
        }
    }

    public static NetworkTaskCallbackHolder getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void setCallback(INetworkTaskCallback iNetworkTaskCallback) {
        this.cb = iNetworkTaskCallback;
    }

    /* access modifiers changed from: package-private */
    public void callOnFail() {
        INetworkTaskCallback iNetworkTaskCallback = this.cb;
        if (iNetworkTaskCallback != null) {
            iNetworkTaskCallback.onFail();
        }
    }
}
