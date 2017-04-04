package com.xrefreshlayout.sample;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lxj.xrefreshlayout.XRefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dance on 2017/4/3.
 */

public class RecyclerViewActivity extends AppCompatActivity {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    ArrayList<String> list = new ArrayList<>();
    @BindView(R.id.xrefreshLayout)
    XRefreshLayout xrefreshLayout;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        ButterKnife.bind(this);

        initView();

        initData();


        xrefreshLayout.setOnRefreshListener(new XRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestDataFromServer(false);
            }

            @Override
            public void onLoadMore() {
                requestDataFromServer(true);
            }
        });

        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        myAdapter = new MyAdapter(list);
        recyclerview.setAdapter(myAdapter);

    }

    private void initView() {
        String title = getIntent().getStringExtra("title");
        getSupportActionBar().setTitle(title);
    }

    private void initData() {
        //prepareData
        list.clear();
        String data1 = "我是俊哥 - ";
        String data2 = "我是内容比较多的数据，春眠不觉晓，处处蚊子咬！- ";
        for (int i = 0; i < 20; i++) {
            list.add(i%2==0?data1 + i:data2+i);
        }

    }

    private void requestDataFromServer(final boolean isLoadMore) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                //模拟从服务器请求数据的时间
                SystemClock.sleep(2000);

                if (isLoadMore) {
                    list.add("加载更多的数据 - " + System.currentTimeMillis() / 1000);
                } else {
                    list.add(0, "下拉刷新的数据 - " + System.currentTimeMillis() / 1000);
                }

                //updateUI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myAdapter.notifyDataSetChanged();
                        xrefreshLayout.completeRefresh();
                    }
                });

            }
        }.start();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.linear_manager:
                initData();
                recyclerview.setLayoutManager(new LinearLayoutManager(this));
                myAdapter.notifyDataSetChanged();
                break;
            case R.id.grid_manager:
                initData();
                recyclerview.setLayoutManager(new GridLayoutManager(this,3));
                myAdapter.notifyDataSetChanged();
                break;
            case R.id.staggered_manager:
                initData();
                recyclerview.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                myAdapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
}
