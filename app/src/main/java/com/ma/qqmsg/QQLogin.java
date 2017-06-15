package com.ma.qqmsg;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.util.TuLingUtil;
import com.scienjus.smartqq.client.QQClient;
import com.scienjus.smartqq.model.UserInfo;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QQLogin extends AppCompatActivity {
    @Bind(R.id.img_ercode)
    ImageView img_ercode;
    @Bind(R.id.btn_get_ercode)
    Button btn_get_ercode;
    @Bind(R.id.txt_status)
    TextView txt_status;
    @Bind(R.id.txt_help)
    TextView txt_help;

    boolean requesting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqlogin);
        ButterKnife.bind(this);
        txt_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "mqqwpa://im/chat?chat_type=wpa&uin=1059876295";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public void getErCode(View view){
        if(requesting){
            return;
        }
        requesting = true;
        btn_get_ercode.setClickable(false);
        btn_get_ercode.setText("正在请求二维码");
        QQClient.getInstance().getQRCode(new QQClient.Listener() {
            @Override
            public void success(final Object object) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = (Bitmap)object;
                        img_ercode.setImageBitmap(bitmap);
                    }
                });
                QQClient.getInstance().checkVCode(new QQClient.CheckErCodeListener() {
                    @Override
                    public void checking(final String status) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                             txt_status.setText(status+"\n请使用系统截屏功能，截取屏幕二维码，并打开QQ扫一扫，从相册选取截屏二维码进行扫码登录");
                             btn_get_ercode.setText("二维码正在认证");
                            }
                        });
                    }

                    @Override
                    public void success(final Object object) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btn_get_ercode.setText("正在获取用户资料");
                                txt_status.setText("二维码状态:已认证");

                                QQClient.getInstance().getAccount(new QQClient.Listener() {
                                    @Override
                                    public void success(Object object) {
                                        UserInfo userInfo = (UserInfo)object;
                                        if(userInfo != null){
                                            MyApplication.getInstance().userInfo = userInfo;
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    btn_get_ercode.setClickable(true);
                                                    btn_get_ercode.setText("获取QQ二维码");
                                                    txt_status.setText("二维码状态:待请求");
                                                    requesting = false;
                                                    showTip("登录成功");
                                                }
                                            });
                                            startActivity(new Intent(QQLogin.this, MainActivity.class));
                                            QQLogin.this.finish();
                                        }
                                    }

                                    @Override
                                    public void fail(int code, final String msg) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                requesting = false;
                                                btn_get_ercode.setClickable(true);
                                                btn_get_ercode.setText("获取QQ二维码");
                                                txt_status.setText("二维码状态:待请求");
                                                showTip("登录失败");
                                                QQClient.getInstance().clear();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void fail(int code, final String msg) {
                        Log.e("result", msg + "");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                requesting = false;
                                btn_get_ercode.setClickable(true);
                                btn_get_ercode.setText("获取QQ二维码");
                                txt_status.setText("二维码状态:待请求");
                                showTip(msg);
                            }
                        });
                    }
                });
            }

            @Override
            public void fail(int code,final String msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        requesting = false;
                        btn_get_ercode.setClickable(true);
                        btn_get_ercode.setText("获取QQ二维码");
                        showTip(msg);
                    }
                });
            }
        });
    }




    void showTip(String tip){
        Toast.makeText(QQLogin.this, tip, Toast.LENGTH_LONG).show();
    }

}
