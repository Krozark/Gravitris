package com.hacklechalet.gravitris;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

/**
 * Created by samuel on 25/05/13.
 */
public class GameActivity extends Activity implements SensorEventListener {

    protected SensorManager sensorManager;
    protected Sensor sensor;
    protected float[] gravityValues;
    public final static int REFRESH_RATE = 1000000 / 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.gravityValues = new float[3];
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        this.sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        this.sensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        this.sensorManager.registerListener(this, sensor, GameActivity.REFRESH_RATE);

        this.startGame();
    }

    @Override
    protected void onPause() {
        this.sensorManager.unregisterListener(this);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onPause();
    }

    @Override
    protected void onResume() {
        this.sensorManager.registerListener(this, sensor, GameActivity.REFRESH_RATE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        this.sensorManager.unregisterListener(this);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onDestroy();
    }

    public void startGame()
    {
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();  // deprecated

        GLSurfaceView view = new GLSurfaceView(this);
        OpenGLRenderer openGlRender = new OpenGLRenderer(width,height,this.gravityValues);
        view.setRenderer(openGlRender);

        this.setContentView(view);

        LinearLayout row = new LinearLayout(this);

        TextView textViewScore = new TextView(this);
        textViewScore.setText("Score :");

        textViewScore.setBackgroundColor(Color.DKGRAY);
        textViewScore.setTextColor(Color.BLACK);

        TextView textViewResScore = new TextView(this);
        textViewResScore.setText(" 0 ");

        textViewResScore.setBackgroundColor(Color.DKGRAY);
        textViewResScore.setTextColor(Color.BLACK);

        Button buttonPause = new Button(this);

        buttonPause.setOnClickListener(new OnClickListenerGameButton(openGlRender));

        buttonPause.setText("Pause");

        row.setBackgroundColor(Color.DKGRAY);

        textViewScore.setWidth((width-10) / 3);
        textViewResScore.setWidth((width-10) / 3);
        buttonPause.setWidth((width-10) / 3);

        row.addView(textViewScore, 0);
        row.addView(textViewResScore, 1);
        row.addView(buttonPause, 2);

        this.addContentView(row, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY)
        {
            this.gravityValues[0] = event.values[0];
            this.gravityValues[1] = event.values[1];
            this.gravityValues[2] = event.values[2];
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
