package com.antlab.panotest.render;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.opengl.GLES11Ext;
import android.view.Surface;

import java.io.IOException;

import com.antlab.panotest.PanoSE;

import android.net.Uri;

import com.antlab.panotest.R;
import com.antlab.panotest.Shade;
import com.antlab.panotest.Sphere;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class PanoVideoView extends PanoView implements GLSurfaceView.Renderer, PanoSE.updateSensorMatrix, SurfaceTexture.OnFrameAvailableListener, MediaPlayer.OnVideoSizeChangedListener {
    private static final String TAG = PanoVideoView.class.getSimpleName();
    private GLSurfaceView m_glsv;
    private Sphere m_sphere;
    private int aPositionHandle;
    private int programId;
    private int uTextureSamplerHandle;
    private int aTextureCoordHandle;
    private int textureId;
    private float[] modelMatrix = new float[16];
    private float[] projectionMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] modelViewMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private int uMatrixHandle;
    private PanoSE m_pse = new PanoSE();
    private SurfaceTexture surfaceTexture;
    private MediaPlayer mediaPlayer;

    private PanoVideoView() {
    }

    public PanoVideoView setGLSurface(GLSurfaceView gl) {
        m_glsv = gl;
        m_glsv.setEGLContextClientVersion(2);
        m_glsv.setRenderer(this);
        m_glsv.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        return this;
    }

    public static PanoVideoView build() {
        return new PanoVideoView();
    }

    public PanoVideoView init(Context context) {
        m_sphere = new Sphere(18, 100);
        Matrix.setIdentityM(modelMatrix, 0);
        m_pse.init((Activity) context, this);

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(context, Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.demo_video));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setLooping(true);
        mediaPlayer.setOnVideoSizeChangedListener(this);
        return this;
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        Log.i(TAG, "onDraw");
        surfaceTexture.updateTexImage();

        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(programId);

        Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
        GLES20.glUniformMatrix4fv(uMatrixHandle, 1, false, mMVPMatrix, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);

        GLES20.glUniform1i(uTextureSamplerHandle, 0);
        m_sphere.uploadVerticesBuffer(aPositionHandle);
        m_sphere.uploadTexCoordinateBuffer(aTextureCoordHandle);
        m_sphere.draw();
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.perspectiveM(projectionMatrix, 0, 60, ratio, 1f, 500f);//视角为60度，近平面为1，远平面500

        Matrix.setLookAtM(viewMatrix, 0,
                0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 1.0f, 0.0f);
        //相机所在点是原点：0.0f, 0.0f, 0.0f
        //相机的视线朝向：0.0f, 0.0f,1.0f
        //相机的正（头顶的）朝向：0.0f, 1.0f, 0.0f
        m_glsv.requestRender();
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        Log.d(TAG, "onVideoSizeChanged: " + width + " " + height);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        programId = Shade.createProgram(Shade.vertex, Shade.video_frag);
        aPositionHandle = GLES20.glGetAttribLocation(programId, "aPosition");
        uMatrixHandle = GLES20.glGetUniformLocation(programId, "uMatrix");
        uTextureSamplerHandle = GLES20.glGetUniformLocation(programId, "sTexture");
        aTextureCoordHandle = GLES20.glGetAttribLocation(programId, "aTexCoord");
        textureId = Shade.loadTexture();

        surfaceTexture = new SurfaceTexture(textureId);
        surfaceTexture.setOnFrameAvailableListener(this);

        Surface surface = new Surface(surfaceTexture);
        mediaPlayer.setSurface(surface);
        surface.release();

        try {
            mediaPlayer.prepare();
        } catch (IOException t) {
            Log.e(TAG, "media player prepare failed");
        }
        mediaPlayer.start();
    }

    @Override
    public void release() {
        mediaPlayer.release();
    }

    @Override
    public void pause() {
        m_glsv.onPause();
        mediaPlayer.pause();
    }

    @Override
    public void resume() {
        m_glsv.onResume();
    }

    @Override
    public void update(float[] rotationMatrix, float[] orientation) {
        modelMatrix = rotationMatrix;
        m_glsv.requestRender();
        Log.i(TAG, "requestRender");
    }

    @Override
    synchronized public void onFrameAvailable(SurfaceTexture surface) {
        m_glsv.requestRender();
    }
}
