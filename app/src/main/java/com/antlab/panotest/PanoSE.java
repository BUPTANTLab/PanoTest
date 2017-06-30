package com.antlab.panotest;

import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.app.Activity;
import android.hardware.SensorManager;
import android.content.Context;
import android.util.Log;
import android.opengl.Matrix;

import java.util.List;


public class PanoSE implements SensorEventListener {
    private static final String TAG = PanoSE.class.getSimpleName();
    private float[] m_rotationMatrix = new float[16];
    private float[] m_orientation = new float[3];
    private updateSE m_usm;

    public void init(Activity root, updateSE usm) {
        SensorManager sensorManager = (SensorManager) root
                .getSystemService(Context.SENSOR_SERVICE);
        Sensor sensorRot = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if (sensorRot == null) {
            Log.e(TAG, "init");
            List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
            for (Sensor item : sensors) {
                Log.e(TAG, item.getName());
            }
            return;
        }
        sensorManager.registerListener(this, sensorRot, SensorManager.SENSOR_DELAY_GAME);
        m_usm = usm;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.accuracy != 0) {
            int type = event.sensor.getType();
            switch (type) {
                case Sensor.TYPE_ROTATION_VECTOR:
                    sensorRotationVectorToMatrix(event, m_rotationMatrix);
                    SensorManager.getOrientation(m_rotationMatrix, m_orientation);
                    m_usm.OnSEChanged(m_rotationMatrix,m_orientation);
                    break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void sensorRotationVectorToMatrix(SensorEvent event, float[] output) {
        float[] mTmp = new float[16];
        float[] values = event.values;
        SensorManager.getRotationMatrixFromVector(mTmp, values);
        SensorManager.remapCoordinateSystem(mTmp, SensorManager.AXIS_Y, SensorManager.AXIS_X, output);

        Matrix.rotateM(output, 0, -90.0F, 1.0F, 0.0F, 0.0F);
    }

    public interface updateSE {
        void OnSEChanged(float[] rotationMatrix, float[] orientation);
    }
}
