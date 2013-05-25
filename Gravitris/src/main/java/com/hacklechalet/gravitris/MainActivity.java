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

    protected boolean gameStarted = false;
    protected SensorManager sensorManager;
    protected Sensor sensor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        this.sensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        this.sensorManager.registerListener(this, sensor, 1000/60);
        this.setContentView(R.layout.activity_main);

        //this.startGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (gameStarted && keyCode == KeyEvent.KEYCODE_BACK)
        {
            //this.moveTaskToBack(true);
            this.setContentView(R.layout.activity_main);
            gameStarted = false;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY)
        {
            TextView xText = (TextView)findViewById(R.id.textView_valueX);
            TextView yText = (TextView)findViewById(R.id.textView_valueY);
            TextView zText = (TextView)findViewById(R.id.textView_valueZ);
            xText.setText(String.valueOf(event.values[0]));
            yText.setText(String.valueOf(event.values[1]));
            zText.setText(String.valueOf(event.values[2]));
        }
    }
    public void startGame(View v)
    {
        GLSurfaceView view = new GLSurfaceView(this);
        view.setRenderer(new OpenGLRenderer());
        this.setContentView(view);
        gameStarted = true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
