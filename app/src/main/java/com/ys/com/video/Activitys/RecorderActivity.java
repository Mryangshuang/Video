package com.ys.com.video.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ys.com.video.R;

public class RecorderActivity extends AppCompatActivity {
    @OnClick({R.id.audio, R.id.video})
    private void click(View view) {
        switch (view.getId()) {
            case R.id.audio:
                Intent intent = new Intent(RecorderActivity.this, RecorderAudioActivity.class);
                startActivity(intent);
                break;
            case R.id.video:
                intent = new Intent(RecorderActivity.this, RecorderVideoActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);
        ViewUtils.inject(this);
    }
}
