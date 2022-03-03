package com.samsung.android.game.gos.ipm;

public class GameManager {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected GameManager(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(GameManager gameManager) {
        if (gameManager == null) {
            return 0;
        }
        return gameManager.swigCPtr;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                IpmWrapJNI.delete_GameManager(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    /* access modifiers changed from: protected */
    public void swigDirectorDisconnect() {
        this.swigCMemOwn = false;
        delete();
    }

    public void swigReleaseOwnership() {
        this.swigCMemOwn = false;
        IpmWrapJNI.GameManager_change_ownership(this, this.swigCPtr, false);
    }

    public void swigTakeOwnership() {
        this.swigCMemOwn = true;
        IpmWrapJNI.GameManager_change_ownership(this, this.swigCPtr, true);
    }

    public String requestWithJson(String str, String str2) {
        return getClass() == GameManager.class ? IpmWrapJNI.GameManager_requestWithJson(this.swigCPtr, this, str, str2) : IpmWrapJNI.GameManager_requestWithJsonSwigExplicitGameManager(this.swigCPtr, this, str, str2);
    }

    public GameManager() {
        this(IpmWrapJNI.new_GameManager(), true);
        IpmWrapJNI.GameManager_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }
}
