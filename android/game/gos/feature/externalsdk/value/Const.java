package com.samsung.android.game.gos.feature.externalsdk.value;

public class Const {

    public enum ApplyType {
        NONE,
        LOW,
        HIGH,
        ULTRA,
        CUSTOM,
        MID,
        CRITICAL
    }

    public enum GameFPSMode {
        LOW_FPS,
        HIGH_FPS,
        ULTRA_FPS
    }

    public enum SdkType {
        NONE,
        NETEASE_SCENE_SDK,
        TENCENT_SCENE_SDK,
        TENCENT_WECHAT
    }

    public enum ControlFlags {
        NONE,
        PERFORMANCE_LEVEL,
        BOOST;

        public long value() {
            return 1 << ordinal();
        }

        public boolean isPresent(long j) {
            return isPresent(this, j);
        }

        public boolean isPresent(ControlFlags controlFlags, long j) {
            return (j & controlFlags.value()) > 0;
        }
    }

    public enum ReportFlags {
        NONE,
        SIOP_LEVEL,
        RESULTS;

        public long value() {
            return 1 << ordinal();
        }

        public boolean isPresent(long j) {
            return isPresent(this, j);
        }

        public boolean isPresent(ReportFlags reportFlags, long j) {
            return (j & reportFlags.value()) > 0;
        }

        public long removeFrom(long j) {
            return removeFrom(this, j);
        }

        public long removeFrom(ReportFlags reportFlags, long j) {
            return j & (~reportFlags.value());
        }
    }
}
