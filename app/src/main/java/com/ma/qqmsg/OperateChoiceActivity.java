package com.ma.qqmsg;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.lib.util.TuLingUtil;

public class OperateChoiceActivity extends AppCompatActivity {
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate_choice);
    }

    // 再按一次退出
    private long firstTime;
    private long secondTime;
    private long spaceTime;

    @Override
    public void onBackPressed() {
        firstTime = System.currentTimeMillis();
        spaceTime = firstTime - secondTime;
        secondTime = firstTime;
        if (spaceTime > 2000) {
            Toast.makeText(this, "再点一次退出", Toast.LENGTH_LONG).show();
        } else {
            super.onBackPressed();
            System.exit(0);
        }
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
