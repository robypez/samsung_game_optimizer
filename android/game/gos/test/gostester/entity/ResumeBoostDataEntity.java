package com.samsung.android.game.gos.test.gostester.entity;

import java.util.HashMap;
import java.util.Map;

public class ResumeBoostDataEntity {
    public Map<Integer, BoostDataEntity> feature = new HashMap();

    public static class BoostDataEntity {
        public String busFreq;
        public int busIndex;
        public String cpuFreq;
        public int cpuIndex;
        public int durationSec;
        public boolean isAvailable;
        public boolean isEnable;
    }
}
