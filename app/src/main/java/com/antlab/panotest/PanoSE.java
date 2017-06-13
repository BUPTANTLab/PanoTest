package com.antlab.panotest;

import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.app.Activity;
import android.hardware.SensorManager;
import android.content.Context;
import android.view.Surface;
import android.opengl.Matrix;


class PanoSE implements SensorEventListener {
    private Activity m_root;
    private float[] m_rotationMatrix = new float[16];

    public void init(Activity root) {
        m_root = root;
        SensorManager sensorManager = (SensorManager) root
                .getSystemService(Context.SENSOR_SERVICE);
        Sensor sensorRot = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if (sensorRot == null) return;
        sensorManager.registerListener(this, sensorRot, SensorManager.SENSOR_DELAY_GAME);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.accuracy != 0) {
            int type = event.sensor.getType();
            switch (type) {
                case Sensor.TYPE_ROTATION_VECTOR:
                    int mDeviceRotation = m_root.getWindowManager().getDefaultDisplay().getRotation();
                    sensorRotationVectorToMatrix(event, mDeviceRotation, m_rotationMatrix);
                    updateSensorMatrix(m_rotationMatrix);
                    break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void updateSensorMatrix(float[] rotationMatrix) {

    }

    private void sensorRotationVectorToMatrix(SensorEvent event, int deviceRotation, float[] output) {
        float[] mTmp = new float[16];
        float[] values = event.values;
        switch (deviceRotation) {
            case Surface.ROTATION_0:
                SensorManager.getRotationMatrixFromVector(output, values);
                break;
            default:
                SensorManager.getRotationMatrixFromVector(mTmp, values);
                SensorManager.remapCoordinateSystem(mTmp, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, output);
        }
        Matrix.rotateM(output, 0, 90.0F, 1.0F, 0.0F, 0.0F);
    }
}
