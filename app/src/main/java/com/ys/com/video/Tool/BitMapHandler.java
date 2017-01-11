package com.ys.com.video.Tool;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

/**
 * Created by Administrator on 2016/7/29.
 */
public class BitMapHandler {
    public static Bitmap creatCircleBitmap(Bitmap srcBitmap){
        if(srcBitmap==null){
            return srcBitmap;
        }
        int bmWidth=srcBitmap.getWidth();
        int bmHeight=srcBitmap.getHeight();
        int min=bmWidth>bmHeight?bmHeight:bmWidth;
        Bitmap bitmap=Bitmap.createBitmap(min,min, Bitmap.Config.ARGB_8888);

        Canvas canvas=new Canvas(bitmap);
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        canvas.drawCircle(min/2,min/2,min/2,paint);
//        设置两者之间
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(srcBitmap,0,0,paint);
        return bitmap;
    }

    /**
     * 画矩形圆角 bitmap
     */
    public static Bitmap createRoundBitmap(Bitmap srcbitmap){
        if(srcbitmap==null){
            return srcbitmap;
        }else{
            int bmWidth=srcbitmap.getWidth();
            int bmHeight=srcbitmap.getHeight();

            Bitmap bitmap=Bitmap.createBitmap(bmWidth,bmHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas=new Canvas(bitmap);
            Paint paint=new Paint();
            RectF rect=new RectF(0,0,bmWidth,bmHeight);
            canvas.drawRoundRect(rect,20,20,paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(srcbitmap,0,0,paint);
            return bitmap;
        }
    }
}
