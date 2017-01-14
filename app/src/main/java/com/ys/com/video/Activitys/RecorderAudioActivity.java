package com.ys.com.video.Activitys;

import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ys.com.video.R;
import com.ys.com.video.Tool.TimeFileTool;

import java.io.File;

public class RecorderAudioActivity extends AppCompatActivity {
    @ViewInject(R.id.textview_rec)
    private TextView textview_rec;

    @ViewInject(R.id.textview_time)
    private TextView textview_time;

    private int count=0;
    private boolean isrecording;
    private MediaRecorder recorder;
    private String fileName = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.image_start, R.id.image_stop})
    private void click(View view) {
        switch (view.getId()) {
            case R.id.image_start:
                record();
                break;
            case R.id.image_stop:
                if (recorder != null) {
                    isrecording = false;
                    count = 0;
                    textview_time.setText("00:00:00");
                    recorder.stop();
                    recorder.release();
                    recorder = null;
                    textview_rec.setText("录音完毕");
                    Toast.makeText(getApplicationContext(), "录音完毕", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    /**
     * 根据现在的情况进行播放判断
     *
     * @param
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void record() {
        if(isrecording){
            return;
        }
        isrecording=true;
        fileName = TimeFileTool.getTimeFile() + ".mp3";
        File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + fileName);
//  如果不存在就创建文件
        if (!file.exists()) {
            recorder = new MediaRecorder();
            Toast.makeText(getApplicationContext(), "正在录音，录音文件在" + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            // 从麦克风源进行录音
            recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            // 设置输出格式
            recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            // 设置编码格式
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            // 设置输出文件
            recorder.setOutputFile(file.getAbsolutePath());
            try {
                // 创建文件
                file.createNewFile();
                // 准备录制
                recorder.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 开始录制
            recorder.start();
            textview_rec.setText(fileName + "正在录音.....");
            settime();
        }
    }

    /**
     * 开始计时
     */
    private void settime() {
        isrecording = true;
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (isrecording) {
                    Log.i("mes", count + "");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textview_time.setText(formatCount());
                            count++;
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * @return
     */
    private String formatCount() {
        if (count < 60) {
            return count > 9 ? "00:00" + ":" + count : "00:00" + ":0" + count;
        } else if (count >= 60 && count < 60 * 60) {
            if(count > 9 * 60){
                return (count%60>9) ? "00:" + count / 60 + ":" + count % 60 : "00:" + count / 60 + ":0" + count % 60;
            }else{
                return (count%60>9) ? "00:0" + count / 60 + ":" + count % 60 : "00:0" + count / 60 + ":0" + count % 60;
            }
        } else if (count >= 60 * 60 && count < 60 * 60 * 60) {

            int a=count/(60*60);
            int b=count/60%60;
            int c=count%60;
            String aa=a>9?a+"":"0"+a;
            String bb=b>9?b+"":"0"+b;
            String cc=c>9?c+"":"0"+c;
            return aa+":"+bb+":"+cc;
        }
        return null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_recoder);
        ViewUtils.inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
