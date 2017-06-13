package com.antlab.panotest;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


class PanoView implements GLSurfaceView.Renderer {
    private Context m_context;
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

    private PanoView() {
    }

    PanoView setGLSurface(GLSurfaceView m_glsv) {
        m_glsv.setEGLContextClientVersion(2);
        m_glsv.setRenderer(this);
        m_glsv.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        return this;
    }

    static PanoView build() {
        return new PanoView();
    }

    PanoView init(Context context) {
        m_sphere = new Sphere(18, 100, 200);
        m_context = context;
        return this;
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {

        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(programId);

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
        GLES20.glUniformMatrix4fv(uMatrixHandle, 1, false, mMVPMatrix, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

        GLES20.glUniform1i(uTextureSamplerHandle, 0);
        m_sphere.uploadVerticesBuffer(aPositionHandle);
        m_sphere.uploadTexCoordinateBuffer(aTextureCoordHandle);
        m_sphere.draw();
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {

        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.perspectiveM(projectionMatrix, 0, 90, ratio, 1f, 500f);

        Matrix.setLookAtM(viewMatrix, 0,
                0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 1.0f, 0.0f);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        programId = Shade.createProgram(Shade.vertex, Shade.frag);
        aPositionHandle = GLES20.glGetAttribLocation(programId, "aPosition");
        uMatrixHandle = GLES20.glGetUniformLocation(programId, "uMatrix");
        uTextureSamplerHandle = GLES20.glGetUniformLocation(programId, "sTexture");
        aTextureCoordHandle = GLES20.glGetAttribLocation(programId, "aTexCoord");
        textureId = Shade.loadTexture(m_context, R.drawable.texture_360_n);
    }
}
