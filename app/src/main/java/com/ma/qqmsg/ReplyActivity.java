package com.ma.qqmsg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.db.dao.ReplayDao;
import com.ma.qqmsg.model.Replay;
import com.scienjus.smartqq.client.QQClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReplyActivity extends AppCompatActivity {
    public static int Type_All_USER = 1;
    public static int Type_All_GROUP = 2;
    public static int Type_All_DISCUSS = 3;
    public static int Type_Single_USER = 4;
    public static int Type_Single_GROUP = 5;
    public static int Type_Single_DISCUSS = 6;
    long toId_user = 10991;
    long toId_group = 10992;
    long toId_discuss = 10993;

    @Bind(R.id.enable_now)
    CheckBox enable_now;
    @Bind(R.id.can_ai)
    CheckBox can_ai;
    @Bind(R.id.edit_reply)
    EditText edit_reply;
    @Bind(R.id.img_bar_left)
    ImageView img_bar_left;
    @Bind(R.id.txt_bar_title)
    TextView txt_bar_title;
    @Bind(R.id.txt_tip)
    TextView txt_tip;

    int type = -1;
    long id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);
        if(type == 2){
            id = toId_group;
            txt_bar_title.setText("群自动回复设置");
        }else if(type == 3){
            id = toId_discuss;
            txt_bar_title.setText("讨论组自动回复设置");
        }else if(type == 1){
            id = toId_user;
            txt_bar_title.setText("好友自动回复设置");
        }else{
            id = intent.getLongExtra("id", 0);
            String title = intent.getStringExtra("title");
            txt_bar_title.setText(title + "自动回复设置");
            txt_tip.setText("注意:\n1、当前针对 “"+title+"” 做的自动回复，启用后将会覆盖全局消息的统一回复，但不影响其他设置的回复信息。");
        }
        loadFromDb();
    }

    public void loadFromDb(){
        QQClient.getInstance().getFriendInfo(id, null);
        ReplayDao.getReplayByToId(id + "", new ReplayDao.Listener() {
            @Override
            public void success(Replay model) {

                if(model != null){
                    can_ai.setChecked(TextUtils.equals("1", model.getIs_enable_ai()));
                    enable_now.setChecked(TextUtils.equals("1", model.getIs_enable()));
                    edit_reply.setText(model.getContent());
                }
            }

            @Override
            public void fail(int errCode, String errMsg) {

            }
        });
    }

    public void save(View view){
        Replay replay = new Replay();
        replay.setIs_enable(enable_now.isChecked()?"1":"0");
        replay.setIs_enable_ai(can_ai.isChecked()?"1":"0");
        replay.setTo_id(id + "");
        replay.setContent(edit_reply.getText().toString());
        ReplayDao.updateOrInsert(replay);
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
