package com.antlab.panotest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.antlab.panotest.R;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Button m_img = (Button) findViewById(R.id.button_img);
        Button m_vid = (Button) findViewById(R.id.button_vid);

        m_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mint = new Intent(SplashActivity.this, MainActivity.class);
                mint.putExtra("type", MainActivity.ImageView);
                startActivity(mint);
                finish();
            }
        });
        m_vid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mint = new Intent(SplashActivity.this, MainActivity.class);
                mint.putExtra("type", MainActivity.VideoView);
                startActivity(mint);
                finish();
            }
        });
    }
}
