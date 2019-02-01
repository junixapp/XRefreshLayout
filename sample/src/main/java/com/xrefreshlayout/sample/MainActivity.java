package com.xrefreshlayout.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_recyclerview).setOnClickListener(this);
        findViewById(R.id.btn_scrollview).setOnClickListener(this);
        findViewById(R.id.btn_animation).setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
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
