package com.example.hellosensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;

import java.text.DecimalFormat;

public class accActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView x;
    private TextView y;
    private TextView z;
    private Vibrator vibrator;
    private TextView direction;
    private TextView chang;
    private boolean played;
    private MediaPlayer ring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        x = findViewById(R.id.X);
        y = findViewById(R.id.Y);
        z = findViewById(R.id.Z);
        direction = findViewById(R.id.Direction);
        chang = findViewById(R.id.chang);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        played = false;
        ring = MediaPlayer.create(accActivity.this,R.raw.chang);

        SensorEventListener _SensorEventListener=   new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                DecimalFormat df = new DecimalFormat("#.##");
                float xValue = event.values[0];
                float yValue = event.values[1];
                float zValue = event.values[2];
                x.setText("X: " + df.format(xValue));
                y.setText("Y: " + df.format(yValue));
                z.setText("Z: " + df.format(zValue));
                String s;
                if(xValue > 4){
                    s = "Vänster";
                    turn(s);
                } else if(xValue < -4){
                    s = "Höger";
                    turn(s);
                } else {
                    direction.setText("Rakt");
                }
                if(yValue < 4){
                    if(!played){
                        ring.start();
                        chang.setText("CHANG");
                        played = true;
                    }
                } else {
                    chang.setText("");
                    played = false;
                }
            }

            private void turn(String s) {
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                    direction.setText(s);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager.registerListener(_SensorEventListener , sensor, SensorManager.SENSOR_DELAY_UI);
    }

}
