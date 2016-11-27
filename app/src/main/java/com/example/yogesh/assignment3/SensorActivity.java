package com.example.yogesh.assignment3;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Yogesh on 11/26/2016.
 */

public class SensorActivity extends MainActivity implements SensorEventListener
{
    private TextView textView;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    float accelValuesX;
    float accelValuesY;
    float accelValuesZ;
    float accelData[] = new float[150];
    int index = 0;
    long current_time= System.currentTimeMillis();
    static int accelerometer_filter_time= 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        textView = (TextView)findViewById(R.id.txtView1);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            // fail! we dont have an accelerometer!
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            if((System.currentTimeMillis()- current_time)>= accelerometer_filter_time)
            {
                current_time = System.currentTimeMillis();
                accelValuesX = sensorEvent.values[0];
                accelData[index]=sensorEvent.values[0];
                index++;

                accelValuesY = sensorEvent.values[1];
                accelData[index]=sensorEvent.values[1];
                index++;

                accelValuesZ = sensorEvent.values[2];
                accelData[index]=sensorEvent.values[2];
                index++;

                /*textView.setText("X="+accelValuesX+", Y="+accelValuesY+", Z="+accelValuesZ);
                Log.i("Tag","X="+accelValuesX+", Y="+accelValuesY+", Z="+accelValuesZ);

                System.out.print("X"+i+"="+accelValuesX);
                System.out.print(" Y"+i+"="+accelValuesY);
                System.out.println(" Z"+i+"="+accelValuesZ);*/

                if(index>=150)
                {
                    mSensorManager.unregisterListener(this);
                    Intent out = new Intent(SensorActivity.this, MainActivity.class);
                    startActivity(out);
                }
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
