package com.lxj.xrefreshlayout.loadinglayout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dance on 2017/4/2.
 */

public interface ILoadingLayout {
    /**
     * make your loading view!
     * @return
     */
    View createLoadingHeader(Context context,ViewGroup parent);
    View createLoadingFooter(Context context,ViewGroup parent);

    /**
     * init your views or reset them.
     */
    void initAndResetHeader();
    void initAndResetFooter();

    /**
     * called when you are pulling the XRefreshLayout, you
     * can make your pulling animation!
     */
    void onPullHeader(float percent);
    void onPullFooter(float percent);

    /**
     * called when release a view form pull state!
     */
    void onHeaderRefreshing();
    void onFooterRefreshing();

}
