package com.ys.com.video.Fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ys.com.video.Constants.Constant;
import com.ys.com.video.R;
import com.ys.com.video.Tool.DownLoadTool;
import com.ys.com.video.Tool.TimeFileTool;
import com.ys.com.video.Tool.ToastTool;

import java.io.File;
import java.util.ArrayList;

public class VideoFragment extends LazyFragment {
    @ViewInject(R.id.blank)
    private LinearLayout blank;
    /**
     * 主界面
     */
    @ViewInject(R.id.mainview)
    private RelativeLayout mainview;

    @ViewInject(R.id.videoview)
    private VideoView videoview;

    @ViewInject(R.id.ll)
    private LinearLayout ll;

    @ViewInject(R.id.actv)
    private AutoCompleteTextView actv;

    private String path, videoName;
    /**
     * 为 懒加载服务
     */
    private boolean isPrepared;
    public static String[] videos;
    public static boolean isDownLoad = false;
    private View view;
    private ArrayList<String> list;

    @OnClick({R.id.play, R.id.del})
    private void click(View view) {
        switch (view.getId()) {
            case R.id.play:
                if (videos.length == 0) {
//                    如果本地没有视频  就从网上进行下载
                    videoName = TimeFileTool.getTimeFile() + ".3gp";
                    playVideoFromURl(videoview, Constant.URL_VIDEO_HOME, videoName);
                    return;
                } else if (!TextUtils.isEmpty(actv.getText().toString())) {
//                    如果搜索框有字  就播放搜索框里面的视频
                    path = Environment.getExternalStorageDirectory().getPath() + File.separator + actv.getText().toString();
                    File file = new File(path);
                    if (!file.exists()) {
                        ToastTool.toast(getContext(), "没有此视频");
                        return;
                    }
                    palyfromLocal(videoview, path);
                } else if (TextUtils.isEmpty(actv.getText().toString())) {
//                    如果搜索框没字  就播放默认的第一个视频
                    ToastTool.toast(getContext(), "播放默认视频");
                    path = Environment.getExternalStorageDirectory().getPath() + File.separator + videos[0];
                    palyfromLocal(videoview, path);
                }
                break;
            case R.id.del:
                String song = actv.getText().toString();
                actv.setText("");
                for (int i = 0; i < videos.length; i++) {
                    if (song.equals(videos[i])) {
                        File file = new File(Environment.getExternalStorageDirectory() + File.separator + song);
                        if (file.exists()) {
                            file.delete();
                            find3gpormp4();
                        }
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
        find3gpormp4();
        mainview.setVisibility(View.VISIBLE);
        ll.setVisibility(View.INVISIBLE);
        blank.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        videoview.stopPlayback();

    }

    /**
     * 找到  MP3 文件  刷新actv 数据
     */
    private void find3gpormp4() {
        list = new ArrayList<String>();
        videos = null;
        File desfile = Environment.getExternalStorageDirectory();
//        找到内存所有的文件
        File[] files = desfile.listFiles();
//        获得所有文件的数量
        for (File num : files) {
//   如果是文件   切后缀为  .mp3   算出个数
            if (num.isFile()) {
                String[] split = num.getName().split("\\.");
                if ("3gp".equalsIgnoreCase(split[split.length - 1]) || "mp4".equalsIgnoreCase(split[split.length - 1])) {
                    list.add(num.getName());
                }
            }
        }
        videos = (String[]) list.toArray(new String[list.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, videos);
        actv.setAdapter(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewUtils.inject(this, view);
        isPrepared = true;
        find3gpormp4();
    }

    /**
     * 需要在哪个VideoView 组件上进行播放
     * \视频下载地址
     * \下载到本地的文件名
     *
     * @param view
     * @param douwnloadUrl
     * @param videoName
     */
    private void playVideoFromURl(VideoView view, String douwnloadUrl, String videoName) {
        //如果本地有就从本地播放  如果没有  就从网络下载后  在从本地进行播放
        path = Environment.getExternalStorageDirectory().getPath() + File.separator + videoName;
        File file = new File(path);
        if (file.exists()) {
            isDownLoad = true;
            mainview.setVisibility(View.VISIBLE);
            ll.setVisibility(View.INVISIBLE);
            blank.setVisibility(View.INVISIBLE);
            ToastTool.toast(getContext(), new VideoFragment(), "本地视频源播放");
            find3gpormp4();
//            从本地进行播放
            palyfromLocal(view, path);
        } else {
            isDownLoad = false;
            ToastTool.toast(getContext(), new VideoFragment(), "网络下载后进行播放");
//            先下载后进行本地播放
            playbyAsyncTask(view, douwnloadUrl, videoName);
        }
    }

    /**
     * 从本地进行播放 videoview \ 播放文件的绝对路径
     */
    private void palyfromLocal(final VideoView view, String videoPath) {
        Uri uri = Uri.parse(videoPath);
        view.setMediaController(new MediaController(getContext()));
        view.setVideoURI(uri);
        view.start();
        view.requestFocus();
//        设置播放完成后 返回主界面
        view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ToastTool.toast(getContext(), new VideoFragment(), "放完了");
                view.stopPlayback();
            }
        });
    }

    /**
     * 懒加载
     */
    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible) {
            find3gpormp4();
            mainview.setVisibility(View.VISIBLE);
            ll.setVisibility(View.INVISIBLE);
            blank.setVisibility(View.INVISIBLE);
        }
    }
    /**
     * 通过AsyncTask 下载后 播放 videoview,播放的url ,播放音乐的名字
     *
     * @param loadpath
     */
    private void playbyAsyncTask(VideoView view, String loadpath, String videoName) {
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute(loadpath, videoName);
//        一下载的时候就把下载动画开启  主界面消失
        ll.setVisibility(View.VISIBLE);
        mainview.setVisibility(View.INVISIBLE);
    }

    class MyAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            DownLoadTool.download(new VideoFragment(), getContext(), params[0], params[1]);
            return params[1];
        }

        @Override
        protected void onPostExecute(String videoname) {
            super.onPostExecute(videoname);
            ll.setVisibility(View.INVISIBLE);
            path = Environment.getExternalStorageDirectory().getPath() + File.separator + videoname;

            if (isDownLoad) {
                mainview.setVisibility(View.VISIBLE);
                blank.setVisibility(View.INVISIBLE);
                find3gpormp4();
                palyfromLocal(videoview, path);
            } else {
//                如果没有下载成功  就把这个音乐文件删除掉
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
                mainview.setVisibility(View.INVISIBLE);
                blank.setVisibility(View.VISIBLE);
            }
        }
    }
}
