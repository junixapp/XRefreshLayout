package com.lxj.xrefreshlayout.util;

import android.util.Log;

/**
 * Created by dance on 2017/4/2.
 */

public class L {
    private static final String TAG = "XRefreshLayout";
    public static boolean isDebug = true;
    public static void d(String msg){
        if(isDebug){
            Log.d(TAG,msg);
        }
    }
    public static void e(String msg){
        if(isDebug){
            Log.e(TAG,msg);
        }
    }
}
