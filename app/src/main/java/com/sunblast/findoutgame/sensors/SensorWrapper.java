package com.sunblast.findoutgame.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import static android.content.Context.SENSOR_SERVICE;


/**
 * Created by sonnymonti on 17.11.17.
 * This class handles motion sensor such as Gyroscope
 */

public class SensorWrapper implements SensorEventListener {
    // Create a constant to convert nanoseconds to seconds.
    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];

    private float[] rotationMatrix = new float[9];

    private static float EPSILON = (float) 0.001;
    private float timestamp;

    private SensorManager sensorManager;
    private Sensor gyroscope;

    public SensorWrapper(Context context) {
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // This timestep's delta rotation to be multiplied by the current rotation
        // after computing it from the gyro sample data.
        if (event.sensor.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR)
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
    }

    public float[] getDeltaRotationVector() {
        return deltaRotationVector;
    }

    public float[] getRotationMatrix() {
        return rotationMatrix;
    }
}
