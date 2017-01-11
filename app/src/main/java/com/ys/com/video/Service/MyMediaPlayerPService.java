package com.ys.com.video.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.ys.com.video.Interface.IPlayer;
import com.ys.com.video.Tool.ToastTool;
import com.ys.com.video.Fragments.MusicFragment;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class MyMediaPlayerPService extends Service {

    private MediaPlayer mPlayer;
    private String mPath;
    private Timer mTimer;
    private TimerTask mTimerTask;

    @Override
    public IBinder onBind(Intent intent) {
        mPlayer = new MediaPlayer();
        return new MBinder();
    }

    public class MBinder extends Binder implements IPlayer {
        private OnPlayListener listener;
        public void setOnPlayListener(OnPlayListener listener){
            this.listener=listener;
        }

        @Override
        public void callPlay(String str) {
            play(str);
            if(listener!=null){
                listener.onplay("宽"+mPlayer.getVideoWidth()+"---"+"高"+mPlayer.getVideoHeight());
            }
        }

        @Override
        public void callPause() {
            pause();
        }

    }

    public interface OnPlayListener{
        public void onplay(String mess);
    }

    private void play(String strName) {
        if(mPlayer==null) {
            mPlayer=new MediaPlayer();
        }
        if (!mPlayer.isPlaying()) {
            if (strName.equals("")) {
//                设置默认歌曲
                mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + MusicFragment.songs[0];
                Toast.makeText(getApplicationContext(), "默认歌曲", Toast.LENGTH_LONG).show();
            } else {
                mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + strName;
//                mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +  MusicActivity.songs[0];
                Log.i("MES",mPath);
            }
            if (!new File(mPath).exists()) {
                ToastTool.toast(getApplicationContext(),"没有找到文件");
                return;
            }else{
                ToastTool.toast(getApplicationContext(),mPath);
            }
            try {
                mPlayer.reset();
                mPlayer.setDataSource(mPath);
                mPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mPlayer.start();
            mTimer = new Timer();
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if (mPlayer.getDuration() == mPlayer.getCurrentPosition()) {
                        mTimer.cancel();
                        mTimerTask.cancel();
                        mPlayer.release();
                    }
                    Message message = Message.obtain();
                    message.what = 1;
                    message.arg1 = getcurr();
                    message.arg2 = druation();
                    MusicFragment.handler.sendMessage(message);
                }
            };
            mTimer.schedule(mTimerTask, 0, 1000);
        }
    }

    private void pause() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }


    private int getcurr() {
        return mPlayer.getCurrentPosition();
    }

    private int druation() {
        return mPlayer.getDuration();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if(mTimer!=null&&mTimerTask!=null){
            mTimer.cancel();
            mTimerTask.cancel();
        }
        mPlayer.release();
        return super.onUnbind(intent);
    }
}



