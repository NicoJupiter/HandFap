package com.jupiter.sensortest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

    private static final String Tag = MainActivity.class.getSimpleName();
    private boolean color = false;
    private View view;
    private long lastUpdate;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private int compteur = 0;
    private boolean timerStop = true;

    private TextView timer;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(MainActivity.this , mSensor , mSensorManager.SENSOR_DELAY_NORMAL);

        setContentView(R.layout.activity_main);
        timer = (TextView) findViewById(R.id.timer_id);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("seconds remaining: " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                timer.setText("done!");
                timerStop = false;
            }

        }.start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
      if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && timerStop == true) {
            getAccelerometer(event);
        }

    }

    private void getAccelerometer(SensorEvent event) {
        TextView compteurText = (TextView) findViewById(R.id.compteur_id);


        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];



        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

        Log.i(Tag , "Values :" + x + " " + y + " " + z + " " + accelationSquareRoot);

        if (accelationSquareRoot >= 2.5) {
           compteur ++;
            setActivityBackgroundColor(Color.GRAY);
            compteurText.setText("Fap point : " + compteur);
        }
        else {
            setActivityBackgroundColor(Color.WHITE);
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
    }

    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }
}
