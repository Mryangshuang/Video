package com.ys.com.video.Fragments;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ys.com.video.Activitys.JsonActivity;
import com.ys.com.video.Activitys.RecorderActivity;
import com.ys.com.video.Activitys.SMSActivity;
import com.ys.com.video.Activitys.SurfaceActivity;
import com.ys.com.video.Constants.Constant;
import com.ys.com.video.Interface.IPlayer;
import com.ys.com.video.R;
import com.ys.com.video.Service.MyMediaPlayerPService;
import com.ys.com.video.Tool.TimeFileTool;
import com.ys.com.video.Tool.ToastTool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;

public class MusicFragment extends LazyFragment {
    @ViewInject(R.id.progressBar)
    private ProgressBar mProgressBar;

    @ViewInject(R.id.seekbar)
    private static SeekBar seekbar;

    @ViewInject(R.id.textView)
    private TextView tv;

    @ViewInject(R.id.textView1)
    private TextView tv1;

    @ViewInject(R.id.button)
    private Button btn;

    @ViewInject(R.id.actv)
    private AutoCompleteTextView actv;

    @ViewInject(R.id.image_tween)
    private ImageView image_tween;

    @ViewInject(R.id.image_frame)
    private static ImageView image_frame;

    @ViewInject(R.id.textview_sdcard)
    private TextView textview_sdcard;

    private View view;
    private InputStream is;
    private IPlayer mIPlayer;
    private ServiceConnection mConn;
    private Intent intent;
    public static String[] songs;
    public List<String> list;
    private boolean isprepared,sms;
    private String NameFromTime;
    private File FileFromTime;

    public static Handler handler = new Handler(new Handler.Callback() {
        AnimationDrawable background;
        @Override
        public boolean handleMessage(Message message) {
            int what = message.what;
            switch (what) {
                case 1:
                    seekbar.setProgress(message.arg1);
                    seekbar.setMax(message.arg2);
                    break;
                case 2:
                    //         帧动画开始  src
                    background = (AnimationDrawable) image_frame.getDrawable();
//                    通过设置background  来设置动画
//                    background = (AnimationDrawable) image_frame.getBackground();
                    if (!background.isRunning()) {
                        background.start();
                    }
                    break;
                case 3:
                    //         帧动画停止
                    if (background.isRunning()) {
                        background.stop();
                    }
                    break;
            }
            return false;
        }
    });


    //    初始透明度
    private float alpha = 0.1f;

    @OnClick({R.id.button, R.id.btn_play, R.id.btn_pause, R.id.btn_json,
            R.id.btn_share_1, R.id.btn_surf, R.id.image_tween, R.id.btn_sdcard,
            R.id.btn_rec, R.id.del})
    private void click(View view) {
        switch (view.getId()) {
            case R.id.button:
//                下载
                myAsyncTask task = new myAsyncTask();
                task.execute(Constant.URL_MUSIC);
                break;
            case R.id.btn_play:
                mIPlayer.callPlay(actv.getText().toString());
                if (mIPlayer instanceof MyMediaPlayerPService.MBinder) {
                    MyMediaPlayerPService.MBinder binder = (MyMediaPlayerPService.MBinder) mIPlayer;
//                    回调
                    binder.setOnPlayListener(new MyMediaPlayerPService.OnPlayListener() {
                        @Override
                        public void onplay(String mess) {
//                            ToastTool.toast(getContext(), new MusicFragment(), mess);
                        }
                    });
                }

                break;
            case R.id.btn_pause:
                mIPlayer.callPause();
                break;
            case R.id.btn_json:
                Intent intent = new Intent(getContext(), JsonActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_sdcard:
                sms=!sms;
                String path = Environment.getExternalStorageDirectory().getPath();
                File file = new File(path, "sdTest.txt");
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    FileOutputStream fos = new FileOutputStream(file, false);
                    fos.write("就是这个文件".getBytes());
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                textview_sdcard.setText("内存：" + path + "(或找到sdTest.txt)");
                break;
//            分享1
            case R.id.btn_share_1:
//                new ShareAction(MusicActivity.this).setPlatform(SHARE_MEDIA.QQ)
//                        .withText("hello")
//                        .setCallback(umShareListener)
//                        .share();
                break;
//            surfaceView
            case R.id.btn_surf:
                intent = new Intent(getContext(), SurfaceActivity.class);
                startActivity(intent);
                break;
//            补间动画  透明度
            case R.id.image_tween:
                if (alpha > 1.0) {
                    alpha = 0.0f;
                }
                image_tween.setAlpha(alpha);
                alpha += 0.1f;
                break;
            case R.id.btn_rec:
                if(sms){
                    intent = new Intent(getContext(), SMSActivity.class);
                }else{
                    intent = new Intent(getContext(), RecorderActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.del:
                String song = actv.getText().toString();
                actv.setText("");
                for (int i = 0; i < songs.length; i++) {
                    if (song.equals(songs[i])) {
                        file = new File(Environment.getExternalStorageDirectory() + File.separator + song);
                        file.delete();
                        findMp3();
                        break;
                    } else {
                        ToastTool.toast(getContext(), "文件未找到！！");
                        return;
                    }
                }

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        再次出现的时候   查找mp3
        findMp3();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_music, null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isprepared = true;
        ViewUtils.inject(this, view);
        //                帧动画开启
        handler.sendEmptyMessage(2);
//        设置初始透明度
        image_tween.setAlpha(alpha);
//        开启服务
        intent = new Intent(getActivity(), MyMediaPlayerPService.class);
        mConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mIPlayer = (IPlayer) iBinder;
                System.out.print(mIPlayer);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                System.out.print("链接错误");
            }
        };
//        绑定播放器服务
        getActivity().bindService(intent, mConn, BIND_AUTO_CREATE);
        findMp3();
    }

    /**
     * 找到  MP3 文件  更新actv
     */
    private void findMp3() {
        list = new ArrayList<String>();
        songs = null;
        File desfile = Environment.getExternalStorageDirectory();
//        找到内存所有的文件
        File[] files = desfile.listFiles();
//        获得所有文件的数量
        for (File num : files) {
//   如果是文件   切后缀为  .mp3   算出个数
            if (num.isFile()) {
                String[] split = num.getName().split("\\.");
                if ("mp3".equalsIgnoreCase(split[split.length - 1])) {
                    list.add(num.getName());
                }
            }
        }
        songs = (String[]) list.toArray(new String[list.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, songs);
        actv.setAdapter(adapter);
    }

    @Override
    protected void lazyLoad() {
        if (isVisible && isprepared) {
            findMp3();
        }
    }


    /**
     * 下载
     */
    class myAsyncTask extends AsyncTask<String, Integer, String> {
        private URL mUrl;
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int Max = values[0];
            int progress = values[1];

            mProgressBar.setMax(Max);
            mProgressBar.setProgress(progress);

            setText(tv, progress);
            setText(tv1, Max);
        }

        @Override
        protected String doInBackground(String... strings) {
            int curr = 0;
            NameFromTime = TimeFileTool.getTimeFile() + ".mp3";
            FileFromTime = new File(Environment.getExternalStorageDirectory(), NameFromTime);
            if (!FileFromTime.getParentFile().exists()) {
                FileFromTime.getParentFile().mkdirs();
            }
            try {
                FileFromTime.createNewFile();
                mUrl = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
                int length = conn.getContentLength();
                Log.i("length", length + "");
                if (length == -1) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastTool.toast(getContext(), new MusicFragment(), "服务器异常.....");
                        }
                    });
                    return "未下载";
                }
                InputStream is = conn.getInputStream();
                OutputStream os = new FileOutputStream(FileFromTime);
                byte[] buff = new byte[1024];
                int len = -1;
                while ((len = is.read(buff)) != -1) {
                    os.write(buff, 0, len);
                    os.flush();
                    curr += len;
                    publishProgress(length, curr);
                }
                is.close();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "已下载";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("已下载")) {
//                发消息停止动画
                handler.sendEmptyMessage(3);
                btn.setText(s);
                getContext().bindService(intent, mConn, BIND_AUTO_CREATE);
                findMp3();
            } else if (s.equals("未下载")) {
                btn.setText(s);
                if (FileFromTime.exists()) {
                    FileFromTime.delete();
                }
                return;
            }
        }
    }

    /**
     * 根据不同的字节长度给部件换算不同的单位显示
     *
     * @param progress
     */
    private void setText(TextView tv, int progress) {
        switch (tv.getId()) {
            case R.id.textView:
                if (progress < 1024) {
                    tv.setText("已下:" + progress + "byte");
                } else if (1024 < progress && progress < 1024 * 1024) {
                    tv.setText("已下" + parseFloat((float) progress / 1024) + "K");
                } else if (1024 * 1024 < progress) {
                    tv.setText("已下：" + parseFloat((float) progress / (1024 * 1024)) + "M");
                }
                break;
            case R.id.textView1:
                if (progress < 1024) {
                    tv.setText("总共:" + parseFloat(progress) + "byte");
                } else if (1024 < progress && progress < 1024 * 1024) {
                    tv.setText("总共" + parseFloat((float) progress / 1024) + "K");
                } else if (1024 * 1024 < progress) {
                    tv.setText("总共：" + parseFloat((float) progress / (1024 * 1024)) + "M");
                }
                break;
        }
    }

    /**
     * 取小数两位数
     *
     * @param progress
     */
    private String parseFloat(float progress) {
        String num = progress + "";
        Log.i("parseFloat", num);
        int delen = 2;//需要显示小数的最大位数
        String[] dfa = num.split("\\.");//todayFlow为从数据库读取的float值
        StringBuilder sb = new StringBuilder();
        if (dfa.length == 0) {
        } else if (dfa.length == 1) {
        } else if (dfa.length == 2) {
//            如果分成了两部分   先添加整数部分
            sb.append(dfa[0]);
//再添加小数部分
            String dde = dfa[1].substring(0, dfa[1].length() > delen ? delen : dfa[1].length());
            sb.append("." + dde);
        }
        return sb.toString();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getContext().unbindService(mConn);
    }
}
