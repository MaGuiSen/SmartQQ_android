package com.ma.qqmsg;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scienjus.smartqq.client.QQClient;
import com.scienjus.smartqq.model.Friend;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FriendFragment extends Fragment {
    @Bind(R.id.list_data)
    RecyclerView list_data;
    @Bind(R.id.txt_bar_title)
    TextView txt_bar_title;
    Handler handler = new Handler();
    MyAdapter adapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        ButterKnife.bind(this, view);
        txt_bar_title.setText("我的好友");
        //创建默认的线性LayoutManager
        list_data.setLayoutManager(new LinearLayoutManager(getContext()));
        //如果每个高度是一样的，设置这个可以提高性能
        list_data.setHasFixedSize(true);
        adapter = new MyAdapter(new ArrayList<Friend>());
        list_data.setAdapter(adapter);
        loadData();
        return view;
    }

    private void loadData() {
        QQClient.getInstance().getFriendList(new QQClient.Listener() {
            @Override
            public void success(Object object) {
                final List<Friend> friends = (List<Friend>)object;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setDatas(friends);
                    }
                });
            }

            @Override
            public void fail(int code, String msg) {

            }
        });
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        List<Friend> datas = null;
        public MyAdapter(List<Friend> datas){
            this.datas = datas;
        }

        public void setDatas(List<Friend> datas) {
            this.datas = datas;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_friend, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Friend data = datas.get(position);
            holder.txt_name.setText(data.getMarkname()+"("+data.getNickname()+")");
            holder.lay_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ReplyActivity.class);
                    intent.putExtra("type", ReplyActivity.Type_Single_USER);
                    intent.putExtra("id", data.getUserId());
                    intent.putExtra("title", data.getMarkname()+"("+data.getNickname()+")");
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            if(datas == null){
                return 0;
            }
            return datas.size();
        }

        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.txt_name)
            TextView txt_name;
            @Bind(R.id.img_avatar)
            ImageView img_avatar;
            @Bind(R.id.lay_item)
            LinearLayout lay_item;

            public ViewHolder(View view){
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
