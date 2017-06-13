package com.antlab.panotest;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.content.pm.ActivityInfo;

public class MainActivity extends AppCompatActivity {
    private PanoView m_panoview;
    public static final String TAG = MainActivity.class.getSimpleName();

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
    }

    private void init() {
        GLSurfaceView glSurfaceView = (GLSurfaceView) findViewById(R.id.surface_view);
        m_panoview = PanoView.build().setGLSurface(glSurfaceView).init(this);
    }
}
