package com.xrefreshlayout.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_recyclerview, R.id.btn_scrollview, R.id.btn_animation})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_recyclerview:
                intent = new Intent(this,RecyclerViewActivity.class);
                break;
            case R.id.btn_scrollview:
                intent = new Intent(this,NestedScrollViewActivity.class);
                break;
            case R.id.btn_animation:
                intent = new Intent(this,CustomRefreshAnimActivity.class);
                break;
        }
        intent.putExtra("title",((Button)view).getText().toString());
        startActivity(intent);
    }

}
