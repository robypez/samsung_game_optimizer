package com.samsung.android.game.gos.ipm;

public enum Cluster {
    LITTLE((String) 0),
    BIG((String) 1),
    UNKNOWN((String) -1),
    ERROR((String) -2);
    
    private final int swigValue;

    public final int swigValue() {
        return this.swigValue;
    }

    public static Cluster swigToEnum(int i) {
        Class<Cluster> cls = Cluster.class;
        Cluster[] clusterArr = (Cluster[]) cls.getEnumConstants();
        if (i < clusterArr.length && i >= 0 && clusterArr[i].swigValue == i) {
            return clusterArr[i];
        }
        for (Cluster cluster : clusterArr) {
            if (cluster.swigValue == i) {
                return cluster;
            }
        }
        throw new IllegalArgumentException("No enum " + cls + " with value " + i);
    }

    private Cluster(int i) {
        this.swigValue = i;
        int unused = SwigNext.next = i + 1;
    }

    private Cluster(Cluster cluster) {
        int i = cluster.swigValue;
        this.swigValue = i;
        int unused = SwigNext.next = i + 1;
    }

    private static class SwigNext {
        /* access modifiers changed from: private */
        public static int next;

        private SwigNext() {
        }

        static /* synthetic */ int access$008() {
            int i = next;
            next = i + 1;
            return i;
        }
    }
}
