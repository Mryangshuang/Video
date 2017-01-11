package com.ys.com.video.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.ys.com.video.Bean.QQ;
import com.ys.com.video.R;
import com.ys.com.video.Tool.Xutils;

import java.util.List;

/**
 * Created by Administrator on 2017/1/1 0001.
 */

public class ListView_Adapter_QQ extends BaseAdapter {

    private List<QQ.ContentBean> content;
    private BitmapUtils bitmapUtils;
    private LayoutInflater inflater;

    public ListView_Adapter_QQ(Context context, List<QQ.ContentBean> content) {
        this.content = content;
        bitmapUtils= Xutils.getBitmapUtils(context);
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return content.size();
    }

    @Override
    public Object getItem(int position) {
        return content.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.json_layout,null);
            viewHolder.imageView= (ImageView) convertView.findViewById(R.id.imageview);
            viewHolder.textView= (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        QQ.ContentBean contentBean = content.get(position);
        viewHolder.textView.setText(contentBean.getName());
        bitmapUtils.display(viewHolder.imageView, contentBean.getIconurl(), new BitmapLoadCallBack<ImageView>() {
            @Override
            public void onLoadCompleted(ImageView container, String uri, Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
                container.setImageBitmap(bitmap);
            }
            @Override
            public void onLoadFailed(ImageView container, String uri, Drawable drawable) {
                container.setImageDrawable(drawable);
            }
        });
        return convertView;
    }
    private class ViewHolder{
        private ImageView imageView;
        private TextView textView;
    }
}
