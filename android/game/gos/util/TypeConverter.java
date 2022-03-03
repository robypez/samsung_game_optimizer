package com.samsung.android.game.gos.util;

import com.samsung.android.game.gos.ipm.BuildConfig;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.zip.Deflater;

public class TypeConverter {
    private static final int BYTE_LENGTH = 1024;
    private static final String LOG_TAG = "TypeConverter";

    public static String floatsToCsv(float[] fArr) {
        if (fArr != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < fArr.length; i++) {
                sb.append(fArr[i]);
                if (i < fArr.length - 1) {
                    sb.append(',');
                }
            }
            if (sb.length() > 0) {
                return sb.toString();
            }
        }
        return null;
    }

    public static String intsToCsv(int[] iArr) {
        if (iArr != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < iArr.length; i++) {
                sb.append(iArr[i]);
                if (i < iArr.length - 1) {
                    sb.append(',');
                }
            }
            if (sb.length() > 0) {
                return sb.toString();
            }
        }
        return null;
    }

    public static String integersToCsv(Integer[] numArr) {
        if (numArr != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < numArr.length; i++) {
                if (numArr[i] != null) {
                    sb.append(numArr[i]);
                    if (i < numArr.length - 1) {
                        sb.append(',');
                    }
                }
            }
            if (sb.length() > 0) {
                return sb.toString();
            }
        }
        return null;
    }

    public static String booleansToCsv(boolean[] zArr) {
        if (zArr != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < zArr.length; i++) {
                sb.append(zArr[i]);
                if (i < zArr.length - 1) {
                    sb.append(',');
                }
            }
            if (sb.length() > 0) {
                return sb.toString();
            }
        }
        return null;
    }

    public static float[] csvToFloats(String str) {
        float[] fArr = null;
        if (str != null && !str.equals(BuildConfig.VERSION_NAME)) {
            try {
                String[] split = str.replaceAll("\\s+", BuildConfig.VERSION_NAME).split(",");
                fArr = new float[split.length];
                for (int i = 0; i < split.length; i++) {
                    fArr[i] = Float.parseFloat(split[i]);
                }
            } catch (Exception e) {
                GosLog.w(LOG_TAG, (Throwable) e);
            }
        }
        return fArr;
    }

    public static int[] csvToInts(String str) {
        int[] iArr = null;
        if (str != null && !str.equals(BuildConfig.VERSION_NAME)) {
            try {
                String[] split = str.replaceAll("\\s+", BuildConfig.VERSION_NAME).split(",");
                iArr = new int[split.length];
                for (int i = 0; i < split.length; i++) {
                    iArr[i] = Integer.parseInt(split[i]);
                }
            } catch (Exception e) {
                GosLog.w(LOG_TAG, (Throwable) e);
            }
        }
        return iArr;
    }

    public static Integer[] csvToIntegers(String str) {
        Integer[] numArr = null;
        if (str != null && !str.equals(BuildConfig.VERSION_NAME)) {
            try {
                String[] split = str.replaceAll("\\s+", BuildConfig.VERSION_NAME).split(",");
                numArr = new Integer[split.length];
                for (int i = 0; i < split.length; i++) {
                    numArr[i] = Integer.valueOf(Integer.parseInt(split[i]));
                }
            } catch (Exception e) {
                GosLog.w(LOG_TAG, (Throwable) e);
            }
        }
        return numArr;
    }

    public static boolean[] csvToBooleans(String str) {
        boolean[] zArr = null;
        if (str != null && !str.equals(BuildConfig.VERSION_NAME)) {
            try {
                String[] split = str.replaceAll("\\s+", BuildConfig.VERSION_NAME).split(",");
                zArr = new boolean[split.length];
                for (int i = 0; i < split.length; i++) {
                    zArr[i] = Boolean.parseBoolean(split[i]);
                }
            } catch (Exception e) {
                GosLog.w(LOG_TAG, (Throwable) e);
            }
        }
        return zArr;
    }

    public static String stringsToCsv(String[] strArr) {
        if (strArr != null) {
            return String.join(",", strArr);
        }
        return null;
    }

    public static String[] csvToStrings(String str) {
        if (str != null && !str.equals(BuildConfig.VERSION_NAME)) {
            try {
                return str.replaceAll("\\s+", BuildConfig.VERSION_NAME).split(",");
            } catch (Exception e) {
                GosLog.w(LOG_TAG, (Throwable) e);
            }
        }
        return null;
    }

    public static List<String> csvToStringList(String str) {
        String[] csvToStrings = csvToStrings(str);
        if (csvToStrings != null) {
            return Arrays.asList(csvToStrings);
        }
        return null;
    }

    public static Set<String> csvToStringSet(String str) {
        String[] csvToStrings = csvToStrings(str);
        if (csvToStrings != null) {
            return new HashSet(Arrays.asList(csvToStrings));
        }
        return null;
    }

    public static ArrayList<Integer> dsvToInts(String str) {
        ArrayList<Integer> arrayList = new ArrayList<>(3);
        if (str != null && !str.equals(BuildConfig.VERSION_NAME)) {
            if (str.contains("-")) {
                str = str.substring(0, str.indexOf("-"));
            }
            try {
                for (String parseInt : str.replaceAll("\\s+", BuildConfig.VERSION_NAME).split("\\.", -1)) {
                    arrayList.add(Integer.valueOf(Integer.parseInt(parseInt)));
                }
            } catch (Exception e) {
                GosLog.w(LOG_TAG, (Throwable) e);
            }
        }
        return arrayList;
    }

    public static String[] ssvToStrings(String str) {
        if (str == null) {
            return null;
        }
        try {
            return str.replaceAll("\\s+", BuildConfig.VERSION_NAME).split(";");
        } catch (Exception e) {
            GosLog.w(LOG_TAG, (Throwable) e);
            return null;
        }
    }

    public static float[] floatListToArray(List<Float> list) {
        if (list == null) {
            return null;
        }
        float[] fArr = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            fArr[i] = list.get(i).floatValue();
        }
        return fArr;
    }

    public static String[] stringListToArray(List<String> list) {
        if (list == null) {
            return null;
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    public static String stringsToCsv(Iterable<String> iterable) {
        if (iterable != null) {
            return String.join(",", iterable);
        }
        return null;
    }

    public static byte[] compressBytes(byte[] bArr) throws IOException {
        if (bArr == null) {
            return null;
        }
        Deflater deflater = new Deflater(9);
        deflater.setInput(bArr);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bArr.length);
        deflater.finish();
        byte[] bArr2 = new byte[1024];
        while (!deflater.finished()) {
            byteArrayOutputStream.write(bArr2, 0, deflater.deflate(bArr2));
        }
        deflater.end();
        byteArrayOutputStream.close();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        GosLog.i(LOG_TAG, "compressByte(), Original: " + (bArr.length / 1024) + " KB, Compressed: " + (byteArray.length / 1024) + " KB");
        return byteArray;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: java.util.zip.GZIPOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: java.util.zip.GZIPOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v0, resolved type: java.util.zip.GZIPOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: java.util.zip.GZIPOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v4, resolved type: byte[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v1, resolved type: java.util.zip.GZIPOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v3, resolved type: java.util.zip.GZIPOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: java.util.zip.GZIPOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v5, resolved type: java.util.zip.GZIPOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v6, resolved type: java.util.zip.GZIPOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v7, resolved type: java.util.zip.GZIPOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v8, resolved type: byte[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v9, resolved type: java.util.zip.GZIPOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v10, resolved type: java.util.zip.GZIPOutputStream} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0063 A[SYNTHETIC, Splitter:B:25:0x0063] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x006b A[Catch:{ IOException -> 0x0067 }] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0077 A[SYNTHETIC, Splitter:B:35:0x0077] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x007f A[Catch:{ IOException -> 0x007b }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] compressBytesGzip(byte[] r7) {
        /*
            java.lang.String r0 = "TypeConverter"
            r1 = 0
            if (r7 == 0) goto L_0x0087
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x005b, all -> 0x0058 }
            int r3 = r7.length     // Catch:{ IOException -> 0x005b, all -> 0x0058 }
            r2.<init>(r3)     // Catch:{ IOException -> 0x005b, all -> 0x0058 }
            java.util.zip.GZIPOutputStream r3 = new java.util.zip.GZIPOutputStream     // Catch:{ IOException -> 0x0055 }
            r3.<init>(r2)     // Catch:{ IOException -> 0x0055 }
            r3.write(r7)     // Catch:{ IOException -> 0x0050, all -> 0x004d }
            r3.close()     // Catch:{ IOException -> 0x0050, all -> 0x004d }
            byte[] r1 = r2.toByteArray()     // Catch:{ IOException -> 0x0050, all -> 0x004d }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0050, all -> 0x004d }
            r4.<init>()     // Catch:{ IOException -> 0x0050, all -> 0x004d }
            java.lang.String r5 = "compressBytesGzip(), Original: "
            r4.append(r5)     // Catch:{ IOException -> 0x0050, all -> 0x004d }
            int r7 = r7.length     // Catch:{ IOException -> 0x0050, all -> 0x004d }
            int r7 = r7 / 1024
            r4.append(r7)     // Catch:{ IOException -> 0x0050, all -> 0x004d }
            java.lang.String r7 = " KB, Compressed: "
            r4.append(r7)     // Catch:{ IOException -> 0x0050, all -> 0x004d }
            int r7 = r1.length     // Catch:{ IOException -> 0x0050, all -> 0x004d }
            int r7 = r7 / 1024
            r4.append(r7)     // Catch:{ IOException -> 0x0050, all -> 0x004d }
            java.lang.String r7 = " KB"
            r4.append(r7)     // Catch:{ IOException -> 0x0050, all -> 0x004d }
            java.lang.String r7 = r4.toString()     // Catch:{ IOException -> 0x0050, all -> 0x004d }
            com.samsung.android.game.gos.util.GosLog.i(r0, r7)     // Catch:{ IOException -> 0x0050, all -> 0x004d }
            r3.close()     // Catch:{ IOException -> 0x0048 }
            r2.close()     // Catch:{ IOException -> 0x0048 }
            goto L_0x0087
        L_0x0048:
            r7 = move-exception
            com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r0, (java.lang.Throwable) r7)
            goto L_0x0087
        L_0x004d:
            r7 = move-exception
            r1 = r3
            goto L_0x0075
        L_0x0050:
            r7 = move-exception
            r6 = r3
            r3 = r1
            r1 = r6
            goto L_0x005e
        L_0x0055:
            r7 = move-exception
            r3 = r1
            goto L_0x005e
        L_0x0058:
            r7 = move-exception
            r2 = r1
            goto L_0x0075
        L_0x005b:
            r7 = move-exception
            r2 = r1
            r3 = r2
        L_0x005e:
            com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r0, (java.lang.Throwable) r7)     // Catch:{ all -> 0x0074 }
            if (r1 == 0) goto L_0x0069
            r1.close()     // Catch:{ IOException -> 0x0067 }
            goto L_0x0069
        L_0x0067:
            r7 = move-exception
            goto L_0x006f
        L_0x0069:
            if (r2 == 0) goto L_0x0072
            r2.close()     // Catch:{ IOException -> 0x0067 }
            goto L_0x0072
        L_0x006f:
            com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r0, (java.lang.Throwable) r7)
        L_0x0072:
            r1 = r3
            goto L_0x0087
        L_0x0074:
            r7 = move-exception
        L_0x0075:
            if (r1 == 0) goto L_0x007d
            r1.close()     // Catch:{ IOException -> 0x007b }
            goto L_0x007d
        L_0x007b:
            r1 = move-exception
            goto L_0x0083
        L_0x007d:
            if (r2 == 0) goto L_0x0086
            r2.close()     // Catch:{ IOException -> 0x007b }
            goto L_0x0086
        L_0x0083:
            com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r0, (java.lang.Throwable) r1)
        L_0x0086:
            throw r7
        L_0x0087:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.util.TypeConverter.compressBytesGzip(byte[]):byte[]");
    }

    private static final class CategoryInt {
        private static final int GAME = 1;
        private static final int MANAGED_APP = 10;
        private static final int NON_GAME = 0;
        private static final int SEC_GAME_FAMILY = 3;
        private static final int UNDEFINED = -1;
        private static final int UNKNOWN = 4;

        private CategoryInt() {
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getCategoryIntValue(java.lang.String r8) {
        /*
            r0 = -1
            if (r8 != 0) goto L_0x0004
            return r0
        L_0x0004:
            int r1 = r8.hashCode()
            r2 = 0
            r3 = 5
            r4 = 2
            r5 = 4
            r6 = 3
            r7 = 1
            switch(r1) {
                case -1038130864: goto L_0x0044;
                case -284840886: goto L_0x003a;
                case 3165170: goto L_0x0030;
                case 26661249: goto L_0x0026;
                case 1358199538: goto L_0x001c;
                case 2072199523: goto L_0x0012;
                default: goto L_0x0011;
            }
        L_0x0011:
            goto L_0x004e
        L_0x0012:
            java.lang.String r1 = "sec_game_family"
            boolean r8 = r8.equals(r1)
            if (r8 == 0) goto L_0x004e
            r8 = r6
            goto L_0x004f
        L_0x001c:
            java.lang.String r1 = "non-game"
            boolean r8 = r8.equals(r1)
            if (r8 == 0) goto L_0x004e
            r8 = r7
            goto L_0x004f
        L_0x0026:
            java.lang.String r1 = "managed_app"
            boolean r8 = r8.equals(r1)
            if (r8 == 0) goto L_0x004e
            r8 = r3
            goto L_0x004f
        L_0x0030:
            java.lang.String r1 = "game"
            boolean r8 = r8.equals(r1)
            if (r8 == 0) goto L_0x004e
            r8 = r4
            goto L_0x004f
        L_0x003a:
            java.lang.String r1 = "unknown"
            boolean r8 = r8.equals(r1)
            if (r8 == 0) goto L_0x004e
            r8 = r5
            goto L_0x004f
        L_0x0044:
            java.lang.String r1 = "undefined"
            boolean r8 = r8.equals(r1)
            if (r8 == 0) goto L_0x004e
            r8 = r2
            goto L_0x004f
        L_0x004e:
            r8 = r0
        L_0x004f:
            if (r8 == r7) goto L_0x0060
            if (r8 == r4) goto L_0x005f
            if (r8 == r6) goto L_0x005e
            if (r8 == r5) goto L_0x005d
            if (r8 == r3) goto L_0x005a
            return r0
        L_0x005a:
            r8 = 10
            return r8
        L_0x005d:
            return r5
        L_0x005e:
            return r6
        L_0x005f:
            return r7
        L_0x0060:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.util.TypeConverter.getCategoryIntValue(java.lang.String):int");
    }

    public static float roundOff(float f) {
        try {
            return BigDecimal.valueOf((double) f).setScale(2, 4).stripTrailingZeros().floatValue();
        } catch (Exception e) {
            GosLog.w(LOG_TAG, e.getMessage());
            return f;
        }
    }

    public static ArrayList<Integer> arrayToIntegerList(int[] iArr) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int valueOf : iArr) {
            arrayList.add(Integer.valueOf(valueOf));
        }
        return arrayList;
    }

    public static String getDateFormattedTime(long j) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US).format(new Date(j));
    }
}
