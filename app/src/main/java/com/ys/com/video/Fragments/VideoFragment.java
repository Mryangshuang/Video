package com.ys.com.video.Fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ys.com.video.Constants.Constant;
import com.ys.com.video.R;
import com.ys.com.video.Tool.DownLoadTool;
import com.ys.com.video.Tool.ToastTool;

import java.io.File;

public class VideoFragment extends LazyFragment {
    public static final String VIDEO_NAME[] = {"V_zhouxingchi.mp4", "V_home.mp4"};
    public static boolean isDownLoad = false;
    private View view;
    @ViewInject(R.id.blank)
    private LinearLayout blank;
    @ViewInject(R.id.videoview_zxc)
    private VideoView videoView_zxc;
    @ViewInject(R.id.videoview_home)
    private VideoView videoView_home;
    private VideoView[] videoViews;
    @ViewInject(R.id.ll)
    private LinearLayout ll;
    private String path;
    private  boolean isPrepared;

    @OnClick({R.id.refresh})
    private void click(View view) {
        switch (view.getId()) {
            case R.id.refresh:
                playVideo(videoViews[0], Constant.URL_VIDEO_ZXC, VIDEO_NAME[0]);
                playVideo(videoViews[1], Constant.URL_VIDEO_HOME, VIDEO_NAME[1]);
                break;
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_video,null);
        isPrepared=true;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewUtils.inject(this,view);
        videoViews = new VideoView[2];
        videoViews[0] = videoView_zxc;
        videoViews[1] = videoView_home;
//        playVideo(videoViews[0], Constant.URL_VIDEO_ZXC, VIDEO_NAME[0]);
//        playVideo(videoViews[1], Constant.URL_VIDEO_HOME, VIDEO_NAME[1]);
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
    private void playVideo(VideoView view, String douwnloadUrl, String videoName) {
        //如果本地有就从本地播放  如果没有  就从网络下载后  在从本地进行播放
        path = Environment.getExternalStorageDirectory().getPath() + File.separator + videoName;
        File file = new File(path);
        if (file.exists()) {
            isDownLoad = true;
            blank.setVisibility(View.INVISIBLE);
            ToastTool.toast(getContext(),new VideoFragment(),"本地视频源播放");
            ll.setVisibility(View.INVISIBLE);
//            从本地进行播放
            palyfromLocal(view, path);
        } else {
            isDownLoad = false;
            ToastTool.toast(getContext(),new VideoFragment(),"网络下载后进行播放");
//            先下载后进行本地播放
            playbyAsyncTask(view, douwnloadUrl, videoName);
        }
    }

    /**
     * 通过AsyncTask 下载后 播放
     *
     * @param loadpath
     */
    private void playbyAsyncTask(VideoView view, String loadpath, String videoName) {
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute(loadpath, videoName);
    }


    /**
     * 从本地进行播放
     */
    private void palyfromLocal(VideoView view, String videoPath) {
        Uri uri = Uri.parse(videoPath);
            view.setMediaController(new MediaController(getContext()));
            view.setVideoURI(uri);
            view.start();
            view.requestFocus();
//        设置播放完成后 返回主界面
            view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    ToastTool.toast(getContext(),new VideoFragment(),"放完了一部了");
                }
            });
    }

    /**
     * 懒加载
     */
    @Override
    protected void lazyLoad() {
//        if (!isPrepared || !isVisible()) {
//            return;
//        }
        playVideo(videoViews[0], Constant.URL_VIDEO_ZXC, VIDEO_NAME[0]);
        playVideo(videoViews[1], Constant.URL_VIDEO_HOME, VIDEO_NAME[1]);
    }

    class MyAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            DownLoadTool.download(new VideoFragment(),getContext(), params[0], params[1]);
            return params[1];
        }

        @Override
        protected void onPostExecute(String videoname) {
            super.onPostExecute(videoname);
            ll.setVisibility(View.INVISIBLE);
            if (isDownLoad) {
                blank.setVisibility(View.INVISIBLE);
                VideoView view = null;
//                相应的歌曲找到相应的组件进行播放
                if (videoname.equals(VIDEO_NAME[0])) {
                    view = videoViews[0];
                } else if (videoname.equals(VIDEO_NAME[1])) {
                    view = videoViews[1];
                }
                path = Environment.getExternalStorageDirectory().getPath() + File.separator + videoname;
                palyfromLocal(view, path);
            } else {
                blank.setVisibility(View.VISIBLE);
            }
        }
    }
}
