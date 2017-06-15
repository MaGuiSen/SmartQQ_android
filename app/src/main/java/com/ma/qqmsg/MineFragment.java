package com.ma.qqmsg;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.scienjus.smartqq.client.QQClient;
import com.scienjus.smartqq.model.UserInfo;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MineFragment extends Fragment {
    @Bind(R.id.img_avatar)
    ImageView img_avatar;
    @Bind(R.id.txt_account)
    TextView txt_account;
    @Bind(R.id.txt_name)
    TextView txt_name;
    @Bind(R.id.lay_group_replay)
    LinearLayout lay_group_replay;
    @Bind(R.id.lay_discuss_replay)
    LinearLayout lay_discuss_replay;
    @Bind(R.id.lay_user_replay)
    LinearLayout lay_user_replay;
    @Bind(R.id.txt_logout)
    TextView txt_logout;

    Handler handler = new Handler();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        initView();
        loadAccount();
        return view;
    }

    private void loadAccount() {
        UserInfo userInfo = MyApplication.getInstance().userInfo;
        if(userInfo != null) {
            txt_account.setText(userInfo.getAccount());
            txt_name.setText(userInfo.getNick());
        }
    }

    private void initView() {
    }

    @OnClick({R.id.txt_logout, R.id.lay_group_replay, R.id.lay_discuss_replay, R.id.lay_user_replay, R.id.lay_set_name, R.id.lay_help})
    public void click(View view){
        if(view.getId() == R.id.txt_logout){
            MyApplication.getInstance().userInfo = null;
            QQClient.getInstance().logout(new QQClient.Listener() {
                @Override
                public void success(Object object) {
                }

                @Override
                public void fail(int code, String msg) {
                }
            });
            startActivity(new Intent(getActivity(), OperateChoiceActivity.class));
            getActivity().finish();
        }else if(view.getId() == R.id.lay_group_replay){
            Intent intent = new Intent(getActivity(), ReplyActivity.class);
            intent.putExtra("type", ReplyActivity.Type_All_GROUP);
            startActivity(intent);
        }else if(view.getId() == R.id.lay_discuss_replay){
            Intent intent = new Intent(getActivity(), ReplyActivity.class);
            intent.putExtra("type", ReplyActivity.Type_All_DISCUSS);
            startActivity(intent);
        }else if(view.getId() == R.id.lay_user_replay){
            Intent intent = new Intent(getActivity(), ReplyActivity.class);
            intent.putExtra("type", ReplyActivity.Type_All_USER);
            startActivity(intent);
        }else if(view.getId() == R.id.lay_set_name){
            Intent intent = new Intent(getActivity(), SetNameActivity.class);
            startActivity(intent);
        }else if(view.getId() == R.id.lay_help){
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=1059876295";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
