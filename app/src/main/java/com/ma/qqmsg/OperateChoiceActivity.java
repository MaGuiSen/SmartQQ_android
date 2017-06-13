package com.ma.qqmsg;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lib.util.TuLingUtil;

public class OperateChoiceActivity extends AppCompatActivity {
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate_choice);
    }

    public void qqLogin(View view){
        startActivity(new Intent(this, QQLogin.class));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                OperateChoiceActivity.this.finish();
            }
        }, 300);
    }

    public void otherLogin(View view){

//        TuLingUtil.getInstance().request("123", "肉", new TuLingUtil.Listener() {
//            @Override
//            public void success(String result) {
//
//            }
//
//            @Override
//            public void fail() {
//
//            }
//        });
    }
}
