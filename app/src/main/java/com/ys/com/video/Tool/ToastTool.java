package com.ys.com.video.Tool;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.ys.com.video.Activitys.BaseTabActivity;
import com.ys.com.video.Fragments.BaiduFragment;
import com.ys.com.video.Fragments.BdzFragment;
import com.ys.com.video.Fragments.MusicFragment;
import com.ys.com.video.Fragments.QishiFragment;
import com.ys.com.video.Fragments.VideoFragment;

/**
 * Created by Administrator on 2017/1/7 0007.
 */

public class ToastTool {
    private static Toast mtoast;

    public static void toast(Context context, Object object) {
        if (mtoast == null) {
            mtoast = Toast.makeText(context, object + "", Toast.LENGTH_SHORT);
        } else {
            mtoast.setText(object + "");
        }
        mtoast.show();
    }

    public static void toast(Context context, Fragment fragment, Object object) {
        if (mtoast == null) {
            mtoast = Toast.makeText(context, object + "", Toast.LENGTH_SHORT);
        } else {
            mtoast.setText(object + "");
        }

        switch (BaseTabActivity.curIndex) {
            case 0:
                if (fragment instanceof BaiduFragment) {
                    mtoast.show();
                }
                break;
            case 1:
                if (fragment instanceof QishiFragment) {
                    mtoast.show();
                }
                break;
            case 2:
                if (fragment instanceof BdzFragment) {
                    mtoast.show();
                }
                break;
            case 3:
                if (fragment instanceof VideoFragment) {
                    mtoast.show();
                }
                break;
            case 4:
                if (fragment instanceof MusicFragment) {
                    mtoast.show();
                }
                break;
        }


    }
}
