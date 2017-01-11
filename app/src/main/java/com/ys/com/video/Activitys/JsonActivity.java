package com.ys.com.video.Activitys;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ys.com.video.Adapter.ListView_Adapter_QQ;
import com.ys.com.video.Bean.QQ;
import com.ys.com.video.Constants.Constant;
import com.ys.com.video.R;
import com.ys.com.video.Tool.DownLoadTool;
import com.ys.com.video.Tool.ToastTool;
import com.ys.com.video.Tool.Xutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

public class JsonActivity extends AppCompatActivity {
    @ViewInject(R.id.listview)
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);
        ViewUtils.inject(this);


//
        String path=Environment.getExternalStorageDirectory().getPath()+File.separator+"TkHot.json";
        File file=new File(path);
//        如果存在，就从本地加载，如果不存在，就下载后从  加载
        if(!file.exists()){
            ToastTool.toast(JsonActivity.this,"从网上下载数据");
            new Thread(){
                @Override
                public void run() {
                    //            下载到本地
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastTool.toast(JsonActivity.this,"进入下载流程....");
                        }
                    });
                    DownLoadTool.download(null,JsonActivity.this, Constant.URL_JSON,"TkHot.json");
                }
            }.start();

//            网上获取数据
            HttpUtils httpUtils = Xutils.getHttpUtils();
            httpUtils.send(HttpRequest.HttpMethod.GET, Constant.URL_JSON, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    setadapter(responseInfo.result);
            }

                @Override
                public void onFailure(HttpException error, String msg) {
                    ToastTool.toast(JsonActivity.this,"网络获取Json错误！！!");
                }
            });
        }else{
            ToastTool.toast(JsonActivity.this,"从本地加载数据！！!");
//            从本地加载数据
            try{
                FileInputStream fis=new FileInputStream(file);
                InputStreamReader isr=new InputStreamReader(fis);
                BufferedReader br=new BufferedReader(isr);
                StringBuffer sb=new StringBuffer();
                String s;
                while ( (s= br.readLine())!=null){
                    sb.append(s);
                }

                setadapter(sb.toString());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 为ListView 添加数据
     * @param str
     */
    private void setadapter(String str) {
        QQ mess=new Gson().fromJson(str,QQ.class);
        List<QQ.ContentBean> content = mess.getContent();
        listView.setAdapter(new ListView_Adapter_QQ(JsonActivity.this,content));
    }
}
