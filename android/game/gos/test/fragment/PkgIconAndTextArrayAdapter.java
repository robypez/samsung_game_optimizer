package com.samsung.android.game.gos.test.fragment;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class PkgIconAndTextArrayAdapter extends ArrayAdapter<String> {
    private static final String LOG_TAG = "PkgIconAndTextArrayAdapter";
    private List<String> mPkgNames;

    public PkgIconAndTextArrayAdapter(Activity activity, int i, List<String> list) {
        super(activity.getApplicationContext(), i, list);
        this.mPkgNames = list;
    }

    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        return getCustomView(i, view, viewGroup);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        return getCustomView(i, view, viewGroup);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v4, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v4, resolved type: com.samsung.android.game.gos.test.fragment.PkgIconAndTextArrayAdapter$CustomViewHolder} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.view.View getCustomView(int r8, android.view.View r9, android.view.ViewGroup r10) {
        /*
            r7 = this;
            r0 = 0
            if (r9 != 0) goto L_0x0023
            android.app.Application r1 = com.samsung.android.game.gos.context.AppContext.get()
            java.lang.String r2 = "layout_inflater"
            java.lang.Object r1 = r1.getSystemService(r2)
            android.view.LayoutInflater r1 = (android.view.LayoutInflater) r1
            r2 = 0
            if (r1 == 0) goto L_0x002a
            r9 = 2131492934(0x7f0c0046, float:1.8609334E38)
            android.view.View r9 = r1.inflate(r9, r10, r0)
            com.samsung.android.game.gos.test.fragment.PkgIconAndTextArrayAdapter$CustomViewHolder r10 = new com.samsung.android.game.gos.test.fragment.PkgIconAndTextArrayAdapter$CustomViewHolder
            r10.<init>(r9)
            r9.setTag(r10)
            r2 = r10
            goto L_0x002a
        L_0x0023:
            java.lang.Object r10 = r9.getTag()
            r2 = r10
            com.samsung.android.game.gos.test.fragment.PkgIconAndTextArrayAdapter$CustomViewHolder r2 = (com.samsung.android.game.gos.test.fragment.PkgIconAndTextArrayAdapter.CustomViewHolder) r2
        L_0x002a:
            java.util.List<java.lang.String> r10 = r7.mPkgNames
            if (r10 == 0) goto L_0x0073
            if (r2 == 0) goto L_0x0073
            android.widget.TextView r10 = r2.mTextView
            android.widget.ImageView r1 = r2.mImageView
            java.util.List<java.lang.String> r2 = r7.mPkgNames     // Catch:{ Exception -> 0x006d }
            java.lang.Object r8 = r2.get(r8)     // Catch:{ Exception -> 0x006d }
            java.lang.String r8 = (java.lang.String) r8     // Catch:{ Exception -> 0x006d }
            android.app.Application r2 = com.samsung.android.game.gos.context.AppContext.get()     // Catch:{ Exception -> 0x006d }
            android.content.pm.PackageManager r2 = r2.getPackageManager()     // Catch:{ Exception -> 0x006d }
            java.util.Locale r3 = java.util.Locale.US     // Catch:{ Exception -> 0x006d }
            java.lang.String r4 = "%s%n%s"
            r5 = 2
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Exception -> 0x006d }
            r5[r0] = r8     // Catch:{ Exception -> 0x006d }
            r0 = 1
            r6 = 128(0x80, float:1.794E-43)
            android.content.pm.ApplicationInfo r6 = r2.getApplicationInfo(r8, r6)     // Catch:{ Exception -> 0x006d }
            java.lang.CharSequence r6 = r2.getApplicationLabel(r6)     // Catch:{ Exception -> 0x006d }
            r5[r0] = r6     // Catch:{ Exception -> 0x006d }
            java.lang.String r0 = java.lang.String.format(r3, r4, r5)     // Catch:{ Exception -> 0x006d }
            r10.setText(r0)     // Catch:{ Exception -> 0x006d }
            android.graphics.drawable.Drawable r8 = r2.getApplicationIcon(r8)     // Catch:{ Exception -> 0x006d }
            r1.setImageDrawable(r8)     // Catch:{ Exception -> 0x006d }
            goto L_0x0073
        L_0x006d:
            r8 = move-exception
            java.lang.String r10 = "PkgIconAndTextArrayAdapter"
            com.samsung.android.game.gos.test.util.GosTestLog.w((java.lang.String) r10, (java.lang.Throwable) r8)
        L_0x0073:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.test.fragment.PkgIconAndTextArrayAdapter.getCustomView(int, android.view.View, android.view.ViewGroup):android.view.View");
    }

    private static class CustomViewHolder {
        /* access modifiers changed from: private */
        public final ImageView mImageView;
        /* access modifiers changed from: private */
        public final TextView mTextView;

        private CustomViewHolder(View view) {
            this.mTextView = (TextView) view.findViewById(2131296619);
            this.mImageView = (ImageView) view.findViewById(2131296425);
        }
    }
}
