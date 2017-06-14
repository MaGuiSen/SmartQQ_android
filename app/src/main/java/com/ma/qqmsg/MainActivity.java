package com.ma.qqmsg;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.lib.db.dao.ReplayDao;
import com.lib.util.PreferenceUtils;
import com.lib.util.TuLingUtil;
import com.ma.qqmsg.model.Replay;
import com.scienjus.smartqq.callback.MessageCallback;
import com.scienjus.smartqq.client.QQClient;
import com.scienjus.smartqq.model.DiscussMessage;
import com.scienjus.smartqq.model.GroupMessage;
import com.scienjus.smartqq.model.Message;
import com.scienjus.smartqq.model.UserInfo;

import java.sql.Time;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;
    Handler handler = new Handler();
    boolean isDestroy = false;
    boolean msgGetting = false;
    Timer timer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragment();
        initBottomTab();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                pollMessage();
            }
        }, 0, 1000);
    }

    AlertDialog dialog = null;
    protected void showAlert() {
        if(dialog != null && dialog.isShowing()){
            return;
        }
        if(dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("你需要进入http://w.qq.com 点击[设置]->[退出登录],后重新使用本软件");
            builder.setTitle("重复登录，无法接收消息！");
            builder.setPositiveButton("重新登陆", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    startActivity(new Intent(MainActivity.this, OperateChoiceActivity.class));
                    finish();
                }
            });
            dialog = builder.create();
        }
        dialog.show();
    }

    public void pollMessage(){
        if(isDestroy){
            return;
        }
        if(msgGetting){
            return;
        }
        msgGetting = true;
        Log.e("msg", "11111111111111111111");
        QQClient.getInstance().pollMessage(new QQClient.MessageCallbackNew() {
            @Override
            public void fail(int code, String msg) {
                msgGetting = false;
                if(code == 103){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showAlert();
                        }
                    });
                }
            }

            @Override
            public void onMessage(Message message) {
                if(message != null){
                    receiveMessage(message.getContent(), message.getUserId(), 1);
                }
            }

            @Override
            public void onGroupMessage(GroupMessage message) {
                if(message != null){
                    receiveMessage(message.getContent(), message.getGroupId(), 2);
                }
            }

            @Override
            public void onDiscussMessage(DiscussMessage message) {
                if(message != null){
                    receiveMessage(message.getContent(), message.getDiscussId(), 3);
                }
            }
        });
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

    boolean is_enable_ai = false,is_enable = false;
    String msg = "";
    long toId = 0;
    Replay replay = null;

    private void receiveMessage(String say,final long userId,final int type) {
        if(type == 2 || type == 3){
            UserInfo userInfo = MyApplication.getInstance().userInfo;
            String smallName = PreferenceUtils.getInstance().getStringParam(userInfo.getUin()+"smallName", "");
            boolean isEnableSmallName = PreferenceUtils.getInstance().getBooleanParam(userInfo.getUin()+"isEnableSmallName", false);
            if(userInfo != null){
                if(!say.contains("@"+userInfo.getNick())){
                    if(isEnableSmallName){
                        if(!say.contains("@"+smallName)){
                            msgGetting = false;
                            return;
                        }else{
                            say = say.replaceAll("@"+smallName, "");
                        }
                    }else{
                        msgGetting = false;
                        return;
                    }
                }else{
                    say = say.replaceAll("@"+userInfo.getNick(), "");
                }
            }else{
                msgGetting = false;
                return;
            }
        }
        final String content = say;
        toId = userId;
        //先判断单独的是否有设置，如果没有在调用全局的
        replay = ReplayDao.getReplayByToId(toId +"", null);
        if(replay != null){
            //需要自动回复
            is_enable_ai =  TextUtils.equals("1", replay.getIs_enable_ai());
            is_enable =  TextUtils.equals("1", replay.getIs_enable());
            msg = replay.getContent();
            if (is_enable_ai) {
                TuLingUtil.getInstance().request(userId+"", content, new TuLingUtil.Listener() {
                    @Override
                    public void success(String result) {
                        sendMessage(userId, type, result);
                    }

                    @Override
                    public void fail() {
                        if(is_enable && !TextUtils.isEmpty(msg)){
                            sendMessage(userId, type, msg);
                        }else{
                            //在ReplayActivity 内设置
                            if(type == 1){
                                toId =  10991;
                            }else if(type == 2){
                                toId =  10992;
                            }else if(type == 3){
                                toId =  10993;
                            }
                            //说明需要判定全局的 1099 代表全局设定的id
                            replay = ReplayDao.getReplayByToId(toId +"", null);
                            if(replay != null){
                                is_enable_ai =  TextUtils.equals("1", replay.getIs_enable_ai());
                                is_enable =  TextUtils.equals("1", replay.getIs_enable());
                                msg = replay.getContent();
                                if (is_enable_ai) {
                                    TuLingUtil.getInstance().request(userId+"", content, new TuLingUtil.Listener() {
                                        @Override
                                        public void success(String result) {
                                            sendMessage(userId, type, result);
                                        }

                                        @Override
                                        public void fail() {
                                            if(is_enable && !TextUtils.isEmpty(msg)) {
                                                sendMessage(userId, type, msg);
                                            }else{
                                                msgGetting = false;
                                            }
                                        }
                                    });
                                }else if(is_enable && !TextUtils.isEmpty(msg)) {
                                    sendMessage(userId, type, msg);
                                }else{
                                    msgGetting = false;
                                }
                            }else{
                                msgGetting = false;
                            }
                    }
                }});
            }else if(is_enable && !TextUtils.isEmpty(msg)){
                sendMessage(userId, type, msg);
            }else{
                //在ReplayActivity 内设置
                if(type == 1){
                    toId =  10991;
                }else if(type == 2){
                    toId =  10992;
                }else if(type == 3){
                    toId =  10993;
                }
                //说明需要判定全局的 1099 代表全局设定的id
                replay = ReplayDao.getReplayByToId(toId +"", null);
                if(replay != null){
                    is_enable_ai =  TextUtils.equals("1", replay.getIs_enable_ai());
                    is_enable =  TextUtils.equals("1", replay.getIs_enable());
                    msg = replay.getContent();
                    if (is_enable_ai) {
                        TuLingUtil.getInstance().request(userId+"", content, new TuLingUtil.Listener() {
                            @Override
                            public void success(String result) {
                                sendMessage(userId, type, result);
                            }

                            @Override
                            public void fail() {
                                if(is_enable && !TextUtils.isEmpty(msg)) {
                                    sendMessage(userId, type, msg);
                                }else{
                                    msgGetting = false;
                                }
                            }
                        });
                    }else if(is_enable && !TextUtils.isEmpty(msg)) {
                        sendMessage(userId, type, msg);
                    }else{
                        msgGetting = false;
                    }
                }else{
                    msgGetting = false;
                }
            }
        }else{
            //在ReplayActivity 内设置
            if(type == 1){
                toId =  10991;
            }else if(type == 2){
                toId =  10992;
            }else if(type == 3){
                toId =  10993;
            }
            //说明需要判定全局的 1099 代表全局设定的id
            replay = ReplayDao.getReplayByToId(toId +"", null);
            if(replay != null){
                is_enable_ai =  TextUtils.equals("1", replay.getIs_enable_ai());
                is_enable =  TextUtils.equals("1", replay.getIs_enable());
                msg = replay.getContent();
                if (is_enable_ai) {
                    TuLingUtil.getInstance().request(userId+"", content, new TuLingUtil.Listener() {
                        @Override
                        public void success(String result) {
                            sendMessage(userId, type, result);
                        }

                        @Override
                        public void fail() {
                            if(is_enable && !TextUtils.isEmpty(msg)) {
                                sendMessage(userId, type, msg);
                            }else{
                                msgGetting = false;
                            }
                        }
                    });
                }else if(is_enable && !TextUtils.isEmpty(msg)) {
                    sendMessage(userId, type, msg);
                }else{
                    msgGetting = false;
                }
            }else{
                msgGetting = false;
            }
        }
    }

    private void sendMessage(long id, int type, String msg) {
        Log.e("msg____", "发送消息》"+msg);
        if(type == 1){
            QQClient.getInstance().sendMessageToFriend(id , msg);
        }else if(type == 2){
            QQClient.getInstance().sendMessageToGroup(id , msg);
        }else if(type == 3){
            QQClient.getInstance().sendMessageToDiscuss(id , msg);
        }
        msgGetting = false;
    }

    private void initBottomTab() {
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.ic_tab_fun_white_24dp, "好友"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_tab_mine_white_24dp, "群组"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_tab_mine_white_24dp, "讨论组"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_tab_mine_white_24dp, "我的"))
                .initialise();
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switchFragment(position);
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });
    }

    private void switchFragment(int position){
        switch (position) {
            case 0:
                switchContent(currFrm, getHomeFrm());
                break;
            case 1:
                switchContent(currFrm, getGroupFrm());
                break;
            case 2:
                switchContent(currFrm, getDiscussFrm());
                break;
            case 3:
                switchContent(currFrm, getMineFrm());
                break;
        }
    }

    /**
     * 切换fragment
     */
    public void switchContent(Fragment from, Fragment to) {
        if (currFrm != to) {
            currFrm = to;
            FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();//.setCustomAnimations(android.R.anim.fade_in, R.anim.fade_out);
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(from).add(R.id.frm_content, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    private void initFragment() {
        currFrm = new FriendFragment();
        this.getSupportFragmentManager().beginTransaction().add(R.id.frm_content, currFrm).commit();
    }

    Fragment currFrm, homeFrm, groupFrm, discussFrm, mineFrm;
    public Fragment getHomeFrm() {
        if(homeFrm ==null) homeFrm = new FriendFragment();
        return homeFrm;
    }

    public Fragment getMineFrm() {
        if(mineFrm==null) mineFrm = new MineFragment();
        return mineFrm;
    }

    public Fragment getGroupFrm() {
        if(groupFrm==null) groupFrm = new GroupFragment();
        return groupFrm;
    }

    public Fragment getDiscussFrm() {
        if(discussFrm==null) discussFrm = new DiscussFragment();
        return discussFrm;
    }

    @Override
    protected void onDestroy() {
        isDestroy = true;
        super.onDestroy();
        ButterKnife.unbind(this);
        if(timer != null){
            timer.cancel();
        }
    }
}
