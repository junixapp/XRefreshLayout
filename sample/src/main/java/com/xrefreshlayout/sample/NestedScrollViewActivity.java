package com.xrefreshlayout.sample;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lxj.xrefreshlayout.XRefreshLayout;
import com.lxj.xrefreshlayout.loadinglayout.ILoadingLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dance on 2017/4/3.
 */

public class NestedScrollViewActivity extends AppCompatActivity {
    @BindView(R.id.xrefreshLayout)
    XRefreshLayout xrefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nestedscrollview);
        ButterKnife.bind(this);

        xrefreshLayout.setOnRefreshListener(new XRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doNothing();
            }

            @Override
            public void onLoadMore() {
                doNothing();
            }
        });

    }

    private void doNothing() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NestedScrollViewActivity.this, "Refresh Successfully!", Toast.LENGTH_SHORT).show();
                xrefreshLayout.completeRefresh();
            }
        },2500);
    }
}
