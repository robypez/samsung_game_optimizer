package com.samsung.android.game.gos.selibrary;

import com.samsung.android.displaysolution.SemDisplaySolutionManager;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.value.AppVariable;

public class SeDisplaySolutionManager {
    private SemDisplaySolutionManager mDisplaySolutionManager;

    private SeDisplaySolutionManager() {
        this.mDisplaySolutionManager = (SemDisplaySolutionManager) AppContext.get().getSystemService("DisplaySolution");
    }

    public static SeDisplaySolutionManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final SeDisplaySolutionManager INSTANCE = new SeDisplaySolutionManager();

        private SingletonHolder() {
        }
    }

    public boolean isMdnieScenarioControlServiceEnabled() throws RuntimeException {
        if (this.mDisplaySolutionManager == null || AppVariable.isUnitTest()) {
            return false;
        }
        return this.mDisplaySolutionManager.isMdnieScenarioControlServiceEnabled();
    }
}
