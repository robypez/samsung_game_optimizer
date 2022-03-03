package com.samsung.android.game.gos.feature.gfi;

import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.feature.gfi.value.GfiVersion;
import com.samsung.android.game.gos.util.FileUtil;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.RinglogConstants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;

public class GfiRinglogAggregator {
    public static final String BRANCH = "fb_branch";
    public static final String CHECKSUM = "fb_checksum";
    private static final int DELAY = 10000;
    public static final String DURATION = "duration";
    private static final String FEATURE_FOLDER = "FB";
    private static final String LOG_TAG = "GfiRinglogAggregator";
    public static final String MAJOR_VERSION = "fb_major_version";
    public static final String METADATA_FILE = "metadata.json";
    public static final String MINOR_VERSION = "fb_minor_version";
    private static final int MS_IN_SEC = 1000;
    private static final int PERIOD = 60000;
    public static final String PKG_NAME = "package_name";
    public static final String REVISION = "fb_revision";
    public static final String RINGLOG_VERSION = "fb_ringlog_version";
    public static final String SESSION_START = "start";
    public static final String UID = "uid";
    public final LocalDateTime mCreationTime = LocalDateTime.now();
    public final String mFullPathToMetadataFile;
    public final String mFullpathToSessionFolder;
    public final int mGameUid;
    private TimerTask mGetRinglogTask;
    /* access modifiers changed from: private */
    public JSONObject mMetadata;
    public final String mPackageName;
    /* access modifiers changed from: private */
    public int mRinglogCount;
    private Timer mScheduler;
    public final String mSessionFolder;

    public GfiRinglogAggregator(String str, int i, GfiVersion gfiVersion) {
        this.mPackageName = str;
        this.mGameUid = i;
        this.mRinglogCount = 0;
        this.mScheduler = null;
        this.mSessionFolder = this.mCreationTime.toString() + "_" + this.mPackageName + "_" + this.mGameUid;
        this.mFullpathToSessionFolder = AppContext.get().getCacheDir() + "/" + FEATURE_FOLDER + "/" + this.mSessionFolder + "/";
        StringBuilder sb = new StringBuilder();
        sb.append(this.mFullpathToSessionFolder);
        sb.append("/");
        sb.append(METADATA_FILE);
        this.mFullPathToMetadataFile = sb.toString();
        try {
            JSONObject jSONObject = new JSONObject();
            this.mMetadata = jSONObject;
            jSONObject.put(MAJOR_VERSION, gfiVersion.mMajorVersion);
            this.mMetadata.put(MINOR_VERSION, gfiVersion.mMinorVersion);
            this.mMetadata.put(REVISION, gfiVersion.mRevision);
            this.mMetadata.put(REVISION, gfiVersion.mBranch);
            this.mMetadata.put(CHECKSUM, gfiVersion.mChecksum);
            this.mMetadata.put("package_name", str);
            this.mMetadata.put(UID, i);
            this.mMetadata.put("start", this.mCreationTime);
        } catch (JSONException e) {
            GosLog.e(LOG_TAG, "metadata creation failed with exception " + e);
        }
    }

    public boolean start() {
        boolean z;
        File fileInstance = FileUtil.getFileInstance(this.mFullpathToSessionFolder);
        if (fileInstance.exists()) {
            if (this.mRinglogCount == 0) {
                GosLog.e(LOG_TAG, "start: session folder " + this.mFullpathToSessionFolder + " already exists.");
            } else {
                GosLog.d(LOG_TAG, "start: resuming session " + this.mFullpathToSessionFolder);
            }
            z = false;
        } else {
            z = fileInstance.mkdirs();
            updateMetadataFile();
        }
        if (z) {
            Timer timer = new Timer();
            this.mScheduler = timer;
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    String str = GfiRinglogAggregator.this.mRinglogCount + ".fbrl";
                    GfiRinglogAggregator gfiRinglogAggregator = GfiRinglogAggregator.this;
                    int unused = gfiRinglogAggregator.mRinglogCount = gfiRinglogAggregator.mRinglogCount + 1;
                    GfiRinglog requestRinglog = GfiRinglog.requestRinglog(GfiRinglogAggregator.this.mGameUid);
                    if (requestRinglog == null || !requestRinglog.isValid()) {
                        GosLog.e(GfiRinglogAggregator.LOG_TAG, "TimerTask.run: received invalid ringlog from package " + GfiRinglogAggregator.this.mPackageName);
                        return;
                    }
                    try {
                        GfiRinglogAggregator.this.mMetadata.put(GfiRinglogAggregator.RINGLOG_VERSION, requestRinglog.mVersion);
                        File file = new File(GfiRinglogAggregator.this.mFullpathToSessionFolder + str);
                        if (!file.createNewFile()) {
                            GosLog.e(GfiRinglogAggregator.LOG_TAG, "TimerTask.run: failed to create the file mFullpathToSessionFolder " + str);
                        }
                        FileOutputStream fileOutputStream = new FileOutputStream(file, false);
                        fileOutputStream.write(requestRinglog.mHeader);
                        fileOutputStream.write(requestRinglog.mRingBuffer);
                    } catch (IOException | JSONException e) {
                        GosLog.e(GfiRinglogAggregator.LOG_TAG, "TimerTask.run: failed to write with exception " + e);
                    }
                }
            }, RinglogConstants.SYSTEM_STATUS_MINIMUM_SESSION_LENGTH_MS, 60000);
        } else {
            GosLog.e(LOG_TAG, "start: unable to create session folder " + this.mFullpathToSessionFolder + ".");
        }
        return z;
    }

    private void updateMetadataFile() {
        try {
            File fileInstance = FileUtil.getFileInstance(this.mFullPathToMetadataFile);
            if (!fileInstance.exists()) {
                fileInstance.createNewFile();
            }
            FileWriter fileWriterInstance = FileUtil.getFileWriterInstance(this.mFullPathToMetadataFile, false);
            fileWriterInstance.write(this.mMetadata.toString());
            fileWriterInstance.flush();
            fileWriterInstance.close();
        } catch (IOException e) {
            GosLog.e(LOG_TAG, "updateMetadataFile: metadata write failed with exception " + e);
        }
    }

    public void stop() {
        Timer timer = this.mScheduler;
        if (timer != null) {
            timer.cancel();
            this.mScheduler = null;
            try {
                this.mMetadata.put("duration", this.mCreationTime.until(LocalDateTime.now(), ChronoUnit.SECONDS));
            } catch (JSONException e) {
                GosLog.e(LOG_TAG, "stop: metadata update failed with exception " + e);
            }
            updateMetadataFile();
        }
    }
}
