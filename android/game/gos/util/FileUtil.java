package com.samsung.android.game.gos.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
    private static final int BYTE_LENGTH = 1024;
    private static final String LOG_TAG = "FileUtil";

    public static File getFileInstance(String str) {
        return new File(str);
    }

    public static FileWriter getFileWriterInstance(String str, boolean z) throws IOException {
        return new FileWriter(str, z);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0022, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0027, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r4.addSuppressed(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x002b, code lost:
        throw r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x002e, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0037, code lost:
        throw r4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void copy(java.io.File r3, java.io.File r4) throws java.io.IOException {
        /*
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x0038 }
            r0.<init>(r3)     // Catch:{ FileNotFoundException -> 0x0038 }
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ all -> 0x002c }
            r3.<init>(r4)     // Catch:{ all -> 0x002c }
            r4 = 1024(0x400, float:1.435E-42)
            byte[] r4 = new byte[r4]     // Catch:{ all -> 0x0020 }
        L_0x000e:
            int r1 = r0.read(r4)     // Catch:{ all -> 0x0020 }
            if (r1 <= 0) goto L_0x0019
            r2 = 0
            r3.write(r4, r2, r1)     // Catch:{ all -> 0x0020 }
            goto L_0x000e
        L_0x0019:
            r3.close()     // Catch:{ all -> 0x002c }
            r0.close()     // Catch:{ FileNotFoundException -> 0x0038 }
            goto L_0x0053
        L_0x0020:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x0022 }
        L_0x0022:
            r1 = move-exception
            r3.close()     // Catch:{ all -> 0x0027 }
            goto L_0x002b
        L_0x0027:
            r3 = move-exception
            r4.addSuppressed(r3)     // Catch:{ all -> 0x002c }
        L_0x002b:
            throw r1     // Catch:{ all -> 0x002c }
        L_0x002c:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x002e }
        L_0x002e:
            r4 = move-exception
            r0.close()     // Catch:{ all -> 0x0033 }
            goto L_0x0037
        L_0x0033:
            r0 = move-exception
            r3.addSuppressed(r0)     // Catch:{ FileNotFoundException -> 0x0038 }
        L_0x0037:
            throw r4     // Catch:{ FileNotFoundException -> 0x0038 }
        L_0x0038:
            r3 = move-exception
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r0 = "copy()-FileNotFoundException: "
            r4.append(r0)
            java.lang.String r3 = r3.toString()
            r4.append(r3)
            java.lang.String r3 = r4.toString()
            java.lang.String r4 = "FileUtil"
            com.samsung.android.game.gos.util.GosLog.e(r4, r3)
        L_0x0053:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.util.FileUtil.copy(java.io.File, java.io.File):void");
    }

    public static boolean delete(File file) throws SecurityException {
        return file.delete();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0028, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0031, code lost:
        throw r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] getBytes(java.io.File r4) throws java.lang.Throwable {
        /*
            if (r4 == 0) goto L_0x0039
            long r0 = r4.length()
            int r0 = (int) r0
            byte[] r1 = new byte[r0]
            java.io.BufferedInputStream r2 = new java.io.BufferedInputStream     // Catch:{ all -> 0x0032 }
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ all -> 0x0032 }
            r3.<init>(r4)     // Catch:{ all -> 0x0032 }
            r2.<init>(r3)     // Catch:{ all -> 0x0032 }
            r4 = 0
            int r4 = r2.read(r1, r4, r0)     // Catch:{ all -> 0x0026 }
            if (r4 < 0) goto L_0x001e
            r2.close()     // Catch:{ all -> 0x0032 }
            goto L_0x003a
        L_0x001e:
            java.io.IOException r4 = new java.io.IOException     // Catch:{ all -> 0x0026 }
            java.lang.String r0 = "getBytes()-End of stream"
            r4.<init>(r0)     // Catch:{ all -> 0x0026 }
            throw r4     // Catch:{ all -> 0x0026 }
        L_0x0026:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x0028 }
        L_0x0028:
            r0 = move-exception
            r2.close()     // Catch:{ all -> 0x002d }
            goto L_0x0031
        L_0x002d:
            r1 = move-exception
            r4.addSuppressed(r1)     // Catch:{ all -> 0x0032 }
        L_0x0031:
            throw r0     // Catch:{ all -> 0x0032 }
        L_0x0032:
            r4 = move-exception
            java.lang.String r0 = "FileUtil"
            com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r0, (java.lang.Throwable) r4)
            throw r4
        L_0x0039:
            r1 = 0
        L_0x003a:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.util.FileUtil.getBytes(java.io.File):byte[]");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0028, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0031, code lost:
        throw r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getString(java.io.File r3) {
        /*
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0032 }
            java.io.FileReader r2 = new java.io.FileReader     // Catch:{ IOException -> 0x0032 }
            r2.<init>(r3)     // Catch:{ IOException -> 0x0032 }
            r1.<init>(r2)     // Catch:{ IOException -> 0x0032 }
        L_0x000f:
            java.lang.String r3 = r1.readLine()     // Catch:{ all -> 0x0026 }
            if (r3 == 0) goto L_0x001e
            r0.append(r3)     // Catch:{ all -> 0x0026 }
            r3 = 10
            r0.append(r3)     // Catch:{ all -> 0x0026 }
            goto L_0x000f
        L_0x001e:
            r1.close()     // Catch:{ IOException -> 0x0032 }
            java.lang.String r3 = r0.toString()
            return r3
        L_0x0026:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x0028 }
        L_0x0028:
            r0 = move-exception
            r1.close()     // Catch:{ all -> 0x002d }
            goto L_0x0031
        L_0x002d:
            r1 = move-exception
            r3.addSuppressed(r1)     // Catch:{ IOException -> 0x0032 }
        L_0x0031:
            throw r0     // Catch:{ IOException -> 0x0032 }
        L_0x0032:
            r3 = move-exception
            r3.printStackTrace()
            r3 = 0
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.util.FileUtil.getString(java.io.File):java.lang.String");
    }

    public static void deleteRecursive(File file) {
        File[] listFiles;
        if (file.isDirectory() && (listFiles = file.listFiles()) != null) {
            for (File deleteRecursive : listFiles) {
                deleteRecursive(deleteRecursive);
            }
        }
        delete(file);
    }
}
