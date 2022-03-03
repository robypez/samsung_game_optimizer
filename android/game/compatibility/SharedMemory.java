package com.samsung.android.game.compatibility;

import android.os.MemoryFile;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import com.samsung.android.game.gos.util.GosLog;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;

public class SharedMemory implements Closeable, Parcelable {
    public static final Parcelable.Creator<SharedMemory> CREATOR = new Parcelable.Creator<SharedMemory>() {
        public SharedMemory createFromParcel(Parcel parcel) {
            return new SharedMemory(parcel);
        }

        public SharedMemory[] newArray(int i) {
            return new SharedMemory[i];
        }
    };
    private static final String LOG_TAG = "SharedMemory";
    private FileDescriptor mFd;
    private MemoryFile mFile;
    private int mNativeFd;
    private int mSize;
    private boolean mWriteable;

    public void close() {
    }

    public int describeContents() {
        return 1;
    }

    public SharedMemory(String str, int i) throws IOException {
        MemoryFile memoryFile = new MemoryFile(str, i);
        this.mFile = memoryFile;
        this.mSize = i;
        this.mWriteable = true;
        this.mNativeFd = -1;
        try {
            this.mFd = (FileDescriptor) memoryFile.getClass().getDeclaredMethod("getFileDescriptor", new Class[0]).invoke(this.mFile, new Object[0]);
            GosLog.i(LOG_TAG, "File is " + this.mFd.valid());
        } catch (Exception unused) {
            throw new IOException();
        }
    }

    private SharedMemory(Parcel parcel) {
        this.mWriteable = false;
        ParcelFileDescriptor readFileDescriptor = parcel.readFileDescriptor();
        this.mNativeFd = readFileDescriptor.getFd();
        this.mFd = readFileDescriptor.getFileDescriptor();
        this.mSize = parcel.readInt();
    }

    public int getNativeFd() {
        if (!this.mWriteable) {
            return this.mNativeFd;
        }
        GosLog.e(LOG_TAG, "The creator cannot get the native file descriptor");
        throw new RuntimeException();
    }

    public void writeBytes(byte[] bArr, int i, int i2, int i3) throws IOException {
        if (this.mWriteable) {
            this.mFile.writeBytes(bArr, i, i2, i3);
        } else {
            GosLog.e(LOG_TAG, "Only java-writeable from the creator side.");
            throw new IOException();
        }
    }

    public int length() {
        return this.mSize;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFileDescriptor(this.mFd);
        parcel.writeInt(this.mSize);
    }
}
