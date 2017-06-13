package com.lib.util;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.ma.qqmsg.MyApplication;
import com.ma.qqmsg.model.TuLingDetail;
import com.ma.qqmsg.model.TuLingModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by mags on 2017/6/12.
 */

public class TuLingUtil {
    OkHttpClient okHttpClient = null;
    private TuLingUtil(){
        okHttpClient = new OkHttpClient.Builder().build();
    }
    private static TuLingUtil tuLingUtil;
    public synchronized static TuLingUtil getInstance(){
        if(tuLingUtil == null){
            tuLingUtil = new TuLingUtil();
        }
        return tuLingUtil;
    }

    public void request(String userID, String msg, final Listener listener){
        Map<String, String> builder = new HashMap<>();
        builder.put("key", "56c80079a5554a678ca1ced967b281ab");
        builder.put("info", msg);
        builder.put("userid", userID);
        String postBody = JSON.toJSONString(builder);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), postBody);
        RequestBody formBody = new FormBody.Builder()
                .add("jsonData", postBody)
                .build();
        Request request = new Request.Builder().url("http://www.tuling123.com/openapi/api").post(requestBody).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(listener != null){
                    listener.fail();
                }
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                try {
                    String result = response.body().string();
                    TuLingModel tuLingModel = JSON.parseObject(result,TuLingModel.class, Feature.IgnoreNotMatch);
                    StringBuilder sb = new StringBuilder();
                    if(tuLingModel!= null){
                        if(tuLingModel.getCode() == 100000){
                            //文本类
                            sb.append(tuLingModel.getText());
                        }else if(tuLingModel.getCode() == 200000 ){
                            //链接类
                            sb.append(tuLingModel.getText()+"\n"+tuLingModel.getUrl());
                        }else if(tuLingModel.getCode() == 302000 ){
                            //新闻类
                            List<TuLingDetail> detailList = tuLingModel.getList();
                            if(detailList != null && detailList.size()>0){
                                sb.append(tuLingModel.getText());
                                for(int i=0;i<detailList.size();i++){
                                    TuLingDetail detail = detailList.get(i);
                                    sb.append("\n"+i+"、"+detail.getArticle()
                                            +"\n来源："+ detail.getSource()
                                            +"\n链接地址："+ detail.getDetailurl());
                                    if(!TextUtils.isEmpty(detail.getIcon())){
                                        sb.append("\n图片链接："+ detail.getIcon());
                                    }
                                }
                            }
                        }else if(tuLingModel.getCode() == 308000 ){
                            //菜谱类
                            List<TuLingDetail> detailList = tuLingModel.getList();
                            if(detailList != null && detailList.size()>0){
                                sb.append(tuLingModel.getText());
                                for(int i=0;i<detailList.size();i++){
                                    TuLingDetail detail = detailList.get(i);
                                    sb.append("\n"+i+"、"+detail.getName()
                                            +"\n菜谱信息："+ detail.getInfo()
                                            +"\n链接地址："+ detail.getDetailurl());
                                    if(!TextUtils.isEmpty(detail.getIcon())){
                                        sb.append("\n图片链接："+ detail.getIcon());
                                    }
                                }
                            }
                        }
                    }
                    if(listener != null){
                        if(!TextUtils.isEmpty(sb.toString())) {
                            listener.success(sb.toString());
                        }else{
                            listener.fail();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if(listener != null){
                        listener.fail();
                    }
                }
            }
        });
    }

    public interface Listener{
        void success(String result);
        void fail();
    }
}
