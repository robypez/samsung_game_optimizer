package com.samsung.android.game.gos.network;

import java.util.Random;

class XAmznTraceIdCreator {
    XAmznTraceIdCreator() {
    }

    public static String create() {
        StringBuilder sb = new StringBuilder("1-");
        sb.append(String.format("%x-", new Object[]{Long.valueOf(System.currentTimeMillis() / 1000)}));
        byte[] bArr = new byte[12];
        new Random().nextBytes(bArr);
        for (int i = 0; i < 12; i++) {
            sb.append(String.format("%02x", new Object[]{Byte.valueOf(bArr[i])}));
        }
        return sb.toString();
    }
}
