package com.ys.com.video.Activitys;

import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ys.com.video.R;
import com.ys.com.video.Tool.TimeFileTool;

import java.io.IOException;

public class RecorderVideoActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    @ViewInject(R.id.surfaceview)
    private SurfaceView surfaceView;// 显示视频的控件

    @ViewInject(R.id.stop)
    private ImageView imagestop;

    private MediaRecorder mediarecorder;// 录制视频的类
    private SurfaceHolder surfaceHolder;
    private boolean isRecording;

    @OnClick({R.id.start,R.id.stop})
    private void click(View view){
        switch (view.getId()){
            case R.id.start:
                if(isRecording){
                    return;
                }
                imagestop.setImageResource(R.drawable.stop1);
                isRecording=true;
                mediarecorder = new MediaRecorder();// 创建mediarecorder对象
                // 设置录制视频源为Camera(相机)
                mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                // 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
                mediarecorder
                        .setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                // 设置录制的视频编码h263 h264
                mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
                // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
                mediarecorder.setVideoSize(176, 144);
                // 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
                mediarecorder.setVideoFrameRate(20);
                mediarecorder.setPreviewDisplay(surfaceHolder.getSurface());
                // 设置视频文件输出的路径
               String fileName = TimeFileTool.getTimeFile() + ".3gp";
                mediarecorder.setOutputFile("/sdcard/"+fileName);
                try{
                // 准备录制  
                mediarecorder.prepare();
                // 开始录制  
                mediarecorder.start();
                }catch(IllegalStateException e){
                // TODO Auto-generated catch block  
                e.printStackTrace();
                }catch(IOException e){
                // TODO Auto-generated catch block  
                e.printStackTrace();
               }
                break;
            case R.id.stop:
                if (mediarecorder != null) {
                    if(isRecording){
                        imagestop.setImageResource(R.drawable.stop2);
                    }
                    isRecording=false;
                    // 停止录制
                    mediarecorder.stop();
                    // 释放资源
                    mediarecorder.release();
                    mediarecorder = null;
                }
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recoder_video);
        ViewUtils.inject(this);
//        设置停止按钮初始画面
        imagestop.setImageResource(R.drawable.stop2);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 选择支持半透明模式,在有surfaceview的activity中使用。  
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
       init();
    }

    /**
     * 初始化数据
     */
    private void init() {
        SurfaceHolder holder = surfaceView.getHolder();// 取得holder
        holder.addCallback(RecorderVideoActivity.this); // holder加入回调接口
        // setType必须设置，要不出错.
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
// 将holder，这个holder为开始在oncreat里面取得的holder，将它赋给surfaceHolder
        surfaceHolder = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // 将holder，这个holder为开始在surfaceChanged里面取得的holder，将它赋给surfaceHolder
        surfaceHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
// surfaceDestroyed的时候同时对象设置为null
        surfaceView = null;
        surfaceHolder = null;
        mediarecorder = null;
    }
}
