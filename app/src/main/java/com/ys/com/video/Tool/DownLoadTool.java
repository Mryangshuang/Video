package com.ys.com.video.Tool;

import android.content.Context;
import android.os.Environment;
import android.support.v4.app.Fragment;

import com.ys.com.video.Fragments.VideoFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2016/12/31 0031.
 */

public class DownLoadTool {
    /**
     * context,下载地址，生成文件名
     * @param context
     * @param urlpath
     * @param filename
     */
    public  static void download(Fragment fragment, Context context, String urlpath, String filename) {
        String path= Environment.getExternalStorageDirectory().getPath();
        File file=new File(path,filename);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        if(!file.exists()){
            try{
                file.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        try{
            URL url = new URL(urlpath);
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            if(urlConnection.getContentLength()==-1){
                if(fragment!=null&&fragment instanceof VideoFragment){
                    VideoFragment.isDownLoad=false;
                }
                return;
            }
            FileOutputStream fos=new FileOutputStream(path+File.separator+filename,false);
            byte[ ] buff=new byte[1024*1024];
            int len=0;
            while((len=is.read(buff))!=-1){
                fos.write(buff,0,len);
                fos.flush();
            }
            is.close();
            fos.close();
            if(fragment!=null&&fragment instanceof VideoFragment){
                VideoFragment.isDownLoad=true;
            }
        }catch (Exception e){
             e.printStackTrace();
        }
    }
}
