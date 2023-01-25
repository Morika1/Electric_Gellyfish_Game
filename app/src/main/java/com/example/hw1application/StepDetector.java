package com.example.hw1application;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class StepDetector {

    private CallBack_steps callBack_steps;
    private SensorManager sensorManager;
    private Sensor sensor;
    private long timeStamp;

    public interface CallBack_steps {
        void oneStep(boolean direction);
    }

    public StepDetector(Context context, CallBack_steps callBack_steps){
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        this.callBack_steps = callBack_steps;
    }

    public void start() {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorManager.unregisterListener(sensorEventListener);
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            calculateStep(x);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // do nothing
        }
    };

    private void calculateStep(float x) {
        if (x > 5.0) {
            if (System.currentTimeMillis() - timeStamp > 30) {
                timeStamp = System.currentTimeMillis();
                if (callBack_steps != null)
                    callBack_steps.oneStep(true);
            }
        }

        if(x < -5.0){
            if (System.currentTimeMillis() - timeStamp > 50) {
                timeStamp = System.currentTimeMillis();
                if (callBack_steps != null)
                    callBack_steps.oneStep(false);
            }
        }
    }


}
