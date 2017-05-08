package com.xrefreshlayout.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lxj.xrefreshlayout.XRefreshLayout;
import com.lxj.xrefreshlayout.loadinglayout.DefaultLoadingLayout;
import com.lxj.xrefreshlayout.loadinglayout.CircleLoadingLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dance on 2017/5/8.
 */

public class CustomRefreshAnimActivity extends AppCompatActivity {
    @BindView(R.id.xrefreshLayout)
    XRefreshLayout xrefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_refresh_anim);
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
                Toast.makeText(CustomRefreshAnimActivity.this, "Refresh Successfully!", Toast.LENGTH_SHORT).show();
                xrefreshLayout.completeRefresh();
            }
        },2500);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.dafulat:
                xrefreshLayout.setLoadingLayout(new DefaultLoadingLayout());
                break;
            case R.id.circle:
                CircleLoadingLayout circleLoadingLayout = new CircleLoadingLayout();
//                circleLoadingLayout.setCircleColor(Color.GREEN);
                xrefreshLayout.setLoadingLayout(circleLoadingLayout);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_refresh,menu);
        return true;
    }
}
