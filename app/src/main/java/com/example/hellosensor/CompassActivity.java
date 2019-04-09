package com.example.hellosensor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

public class CompassActivity extends AppCompatActivity {

    private ImageView compass;
    private float currentDegree = 0f;
    float[] rMat = new float[9];
    float[] orientation = new float[3];
    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView degreeText;
    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        compass = findViewById(R.id.compass);
        degreeText = findViewById(R.id.DegreeText);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        layout = findViewById(R.id.layout);

        SensorEventListener _SensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                DecimalFormat df = new DecimalFormat("#.##");
                SensorManager.getRotationMatrixFromVector(rMat, event.values);
                float degree = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
                degreeText.setText("Degrees: " + degree);

                RotateAnimation ra = new RotateAnimation(
                        currentDegree,
                        -degree,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f);

                // how long the animation will take place
                ra.setDuration(210);

                // set the animation after the end of the reservation status
                ra.setFillAfter(true);
                int intDegree = (int) degree;

                if(intDegree < 60) {
                    layout.setBackgroundColor(Color.MAGENTA);
                } else if(intDegree < 120) {
                    layout.setBackgroundColor(Color.RED);
                } else if(intDegree < 180) {
                    layout.setBackgroundColor(Color.YELLOW);
                } else if(intDegree < 240) {
                    layout.setBackgroundColor(Color.GREEN);
                } else if(intDegree < 300) {
                    layout.setBackgroundColor(Color.CYAN);
                } else{
                    layout.setBackgroundColor(Color.BLUE);
                }

                // Start the animation
                compass.startAnimation(ra);
                currentDegree = -degree;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager.registerListener(_SensorEventListener , sensor, SensorManager.SENSOR_DELAY_UI);
    }
}
