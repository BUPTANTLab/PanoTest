package com.antlab.panotest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.antlab.panotest.R;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        setContentView(R.layout.splash);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        getWindow().setAttributes(params);

        Button m_img = (Button) findViewById(R.id.button_img);
        Button m_vid = (Button) findViewById(R.id.button_vid);

        m_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, MainActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("type", MainActivity.ImageView);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
        m_vid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, MainActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("type", MainActivity.VideoView);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
    }
}
