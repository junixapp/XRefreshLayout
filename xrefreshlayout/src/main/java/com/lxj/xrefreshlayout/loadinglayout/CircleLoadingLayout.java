package com.lxj.xrefreshlayout.loadinglayout;

import android.animation.FloatEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.lxj.xrefreshlayout.R;

/**
 * Created by dance on 2017/5/8.
 */

public class CircleLoadingLayout implements ILoadingLayout {

    private CircleLoadingView headerProgressView;
    private CircleLoadingView footerProgressView;

    private FloatEvaluator floatEval = new FloatEvaluator();
    private View header;
    private float startVal = 0f;

    private final int MSG_HEADER = 1;
    private final int MSG_FOOTER = 2;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_HEADER:
                    rotateView(headerProgressView);
                    break;
                case MSG_FOOTER:
                    rotateView(footerProgressView);
                    break;
            }

            handler.sendEmptyMessageDelayed(msg.what, 1200);
        }
    };

    private int circleColor = Color.BLUE;

    /**
     * set the circle color!
     * @param color
     */
    public void setCircleColor(int color){
        this.circleColor = color;
    }

    @Override
    public View createLoadingHeader(Context context, ViewGroup parent) {
        header = (View) LayoutInflater.from(context).inflate(R.layout.xrl_progress_header, parent, false);
        headerProgressView = (CircleLoadingView) header.findViewById(R.id.progressView);
        headerProgressView.setProgressColor(circleColor);
        return header;
    }

    @Override
    public View createLoadingFooter(Context context, ViewGroup parent) {
        View footer = (View) LayoutInflater.from(context).inflate(R.layout.xrl_progress_footer, parent, false);
        footerProgressView = (CircleLoadingView) footer.findViewById(R.id.progressView);
        footerProgressView.setProgressColor(circleColor);
        return footer;
    }

    @Override
    public void initAndResetHeader() {
        handler.removeCallbacksAndMessages(null);
        headerProgressView.setProgress(0);
        headerProgressView.setRotation(0);
        if(startVal==0f){
            header.post(new Runnable() {
                @Override
                public void run() {
                    //计算当headerProgressView开始完全可见的百分比
                    startVal = headerProgressView.getHeight()*1f/header.getHeight();
                }
            });
        }
    }

    @Override
    public void initAndResetFooter() {
        handler.removeCallbacksAndMessages(null);
        footerProgressView.setStart(-270);
        footerProgressView.setProgress(0);
        footerProgressView.setRotation(0);

    }

    @Override
    public void onPullHeader(float percent) {
        if(percent>=startVal){
            float p = (percent-startVal)/(1-startVal);
            headerProgressView.setProgress(p);
        }
    }

    @Override
    public void onPullFooter(float percent) {
        if(percent>=startVal){
            float p = (percent-startVal)/(1-startVal);
            footerProgressView.setProgress(p);
        }
    }

    @Override
    public void onHeaderRefreshing() {
        handler.sendEmptyMessage(MSG_HEADER);
    }

    @Override
    public void onFooterRefreshing() {
        handler.sendEmptyMessage(MSG_FOOTER);
    }

    private void rotateView(View view){
        ViewCompat.animate(view).rotationXBy(360).setDuration(1000)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

}
