package com.hacklechalet.gravitris;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.*;
import android.opengl.GLSurfaceView;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

    //protected SensorManager sensorManager;
    //protected Sensor sensor;
    //public final static int REFRESH_RATE = 1000000 / 60;
    GameActivity gameActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /*
        this.sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        this.sensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        this.sensorManager.registerListener(this, sensor, MainActivity.REFRESH_RATE);
        */
        this.setContentView(R.layout.activity_main);


        gameActivity = new GameActivity();
        //this.startGame();
    }

    @Override
    protected void onPause() {
        //this.sensorManager.unregisterListener(this);
        super.onPause();
    }
    @Override
    protected void onResume() {
        //this.sensorManager.registerListener(this, sensor, MainActivity.REFRESH_RATE);
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        //this.sensorManager.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onSensorChanged(SensorEvent event)
    {
    /*
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY)
        {
            TextView xText = (TextView)findViewById(R.id.textView_valueX);
            TextView yText = (TextView)findViewById(R.id.textView_valueY);
            TextView zText = (TextView)findViewById(R.id.textView_valueZ);
            xText.setText(String.valueOf(event.values[0]));
            yText.setText(String.valueOf(event.values[1]));
            zText.setText(String.valueOf(event.values[2]));

        }
    */
    }
    public void goToGameActivity(View v)
    {
        Intent intentActivityGame;

        intentActivityGame = new Intent(MainActivity.this, gameActivity.getClass());
        this.startActivityForResult(intentActivityGame, 0);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
