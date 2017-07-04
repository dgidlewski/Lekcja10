package com.example.darek.lekcja10;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager manager;

    private float azimuth = 0f;
    private float currentAzimuth = 0;
    private Sensor orientation;

    ImageView compassArrow;
    TextView azimuthTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        orientation = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        compassArrow = (ImageView) findViewById(R.id.compassarrow);
        azimuthTextView = (TextView) findViewById(R.id.azimuth);

        showSensors();
        registerListeners();
    }

    public void showSensors(){
        TextView sensorsTextView = (TextView) findViewById(R.id.sensorlist);
        List<Sensor> deviceSensors = manager.getSensorList(Sensor.TYPE_ALL);

        String allSensorsList = new String();
        Sensor tmp;
        for(int i=0; i<deviceSensors.size(); i++) {
            tmp = deviceSensors.get(i);
            allSensorsList += tmp.getName() + "\n";
        }
        sensorsTextView.setText(allSensorsList);

        //String allSensorsList = new String("Lista czujników: \n");
        /*
        for(Sensor sensor : deviceSensors){
            allSensorsList += sensor.getName() + "\n";
        }
        sensorsTextView.setText(allSensorsList);
        */
    }

    public void registerListeners(){
        manager.registerListener( this, orientation, SensorManager.SENSOR_DELAY_GAME);
    }

    public void calibrateCompass(){
        Animation animation = new RotateAnimation(-currentAzimuth, -azimuth, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        currentAzimuth = azimuth;
        animation.setRepeatCount(0);
        animation.setDuration(500);
        animation.setFillAfter(true);
        compassArrow.startAnimation(animation);
    }

     public void updateAzimuth(){
           azimuthTextView.setText(Float.toString(azimuth));
     }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if( event.sensor.getType() == Sensor.TYPE_ORIENTATION ){
            azimuth = (event.values[0] + 360 ) % 360;
            updateAzimuth();
            calibrateCompass();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
