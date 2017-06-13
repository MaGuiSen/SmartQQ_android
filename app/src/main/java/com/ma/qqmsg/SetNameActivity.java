package com.ma.qqmsg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.db.dao.ReplayDao;
import com.lib.util.PreferenceUtils;
import com.ma.qqmsg.model.Replay;
import com.scienjus.smartqq.client.QQClient;
import com.scienjus.smartqq.model.UserInfo;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetNameActivity extends AppCompatActivity {
    @Bind(R.id.enable_now)
    CheckBox enable_now;
    @Bind(R.id.edit_reply)
    EditText edit_reply;
    @Bind(R.id.img_bar_left)
    ImageView img_bar_left;
    @Bind(R.id.txt_bar_title)
    TextView txt_bar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name);
        ButterKnife.bind(this);
        txt_bar_title.setText("设置你的群小名");
        loadFromDb();
    }

    public void loadFromDb(){
        UserInfo userInfo = MyApplication.getInstance().userInfo;
        String smallName = PreferenceUtils.getInstance().getStringParam(userInfo.getUin()+"smallName", "");
        boolean isEnableSmallName = PreferenceUtils.getInstance().getBooleanParam(userInfo.getUin()+"isEnableSmallName", false);
        edit_reply.setText(smallName);
        enable_now.setChecked(isEnableSmallName);
    }

    public void save(View view){
        if(TextUtils.isEmpty(edit_reply.getText().toString())){
            Toast.makeText(this, "请输入群小名", Toast.LENGTH_LONG).show();
            return;
        }
        UserInfo userInfo = MyApplication.getInstance().userInfo;
        PreferenceUtils.getInstance().saveParam(userInfo.getUin()+"smallName", edit_reply.getText().toString());
        PreferenceUtils.getInstance().saveParam(userInfo.getUin()+"isEnableSmallName", enable_now.isChecked());
        Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show();
        this.finish();
    }

    @OnClick({R.id.img_bar_left})
    public void click(View view){
        if(view.getId() == R.id.img_bar_left){
            this.finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
