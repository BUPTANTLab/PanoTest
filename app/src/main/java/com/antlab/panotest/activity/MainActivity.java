package com.antlab.panotest.activity;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.content.pm.ActivityInfo;
import android.widget.TextView;

import com.antlab.panotest.R;
import com.antlab.panotest.render.PanoImageView;
import com.antlab.panotest.render.PanoView;
import com.antlab.panotest.render.PanoVideoView;
import com.antlab.panotest.util.SlideWin;


public class MainActivity extends AppCompatActivity implements PanoView.showOrientation {
    private PanoView m_panoview;
    private TextView m_ori;
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int ImageView = 1;
    public static final int VideoView = 2;
    private SlideWin yaw = new SlideWin(10);
    private SlideWin roll = new SlideWin(10);
    private SlideWin pitch = new SlideWin(10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        init();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        getWindow().setAttributes(params);
        m_ori = (TextView) findViewById(R.id.text_ori);
    }

    private void init() {
        GLSurfaceView glSurfaceView = (GLSurfaceView) findViewById(R.id.surface_view);
        int type = getIntent().getIntExtra("type", ImageView);
        switch (type) {
            default:
            case ImageView:
                m_panoview = PanoImageView.build().setGLSurface(glSurfaceView).init(MainActivity.this, MainActivity.this);
                break;
            case VideoView:
                m_panoview = PanoVideoView.build().setGLSurface(glSurfaceView).init(MainActivity.this, MainActivity.this);
                break;
        }
        Log.i(TAG, "init");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        m_panoview.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        m_panoview.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_panoview.resume();
    }

    @Override
    public void OnOrientationChanged(float[] orientation) {
        yaw.add(orientation[0]);
        pitch.add(orientation[1]);
        roll.add(orientation[2]);
        m_ori.setText(
                "Yaw: " + yaw.average()
                        + "\nPitch: " + pitch.average()
                        + "\nRoll: " + roll.average()
        );
    }

}
