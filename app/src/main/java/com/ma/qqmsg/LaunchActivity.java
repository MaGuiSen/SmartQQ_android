package com.ma.qqmsg;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LaunchActivity.this, OperateChoiceActivity.class));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LaunchActivity.this.finish();
                    }
                }, 300);
            }
        },3000);
    }
}
