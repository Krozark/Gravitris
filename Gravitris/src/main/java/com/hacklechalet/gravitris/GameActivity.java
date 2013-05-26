package com.hacklechalet.gravitris;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by samuel on 25/05/13.
 */
public class GameActivity extends Activity implements SensorEventListener {

    protected SensorManager sensorManager;
    protected Sensor sensor;
    public float[] gravityValues;
    public final static int REFRESH_RATE = 1000000 / 60;
    private TextView textViewScore;
    private TextView textViewResScore;

    private LinearLayout row;
    public String stringScore = "0";
    public Timer timer;
    private OpenGLRenderer openGlRender;

    public int height, width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        textViewResScore = new TextView(this);
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }).start();

        super.onDestroy();
    }

    public void startGame()
    {
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth(); // deprecated
        height = display.getHeight(); // deprecated

        row = new LinearLayout(this);

        textViewScore = new TextView(this);
        textViewScore.setText("Score :");

        textViewScore.setBackgroundColor(Color.DKGRAY);
        textViewScore.setTextColor(Color.BLACK);


        textViewResScore.setText(stringScore);

        textViewResScore.setBackgroundColor(Color.DKGRAY);
        textViewResScore.setTextColor(Color.BLACK);

        GLSurfaceView view = new GLSurfaceView(this);
        openGlRender = new OpenGLRenderer(width,height,this.gravityValues);
        view.setOnTouchListener(openGlRender);

        Button buttonPause = new Button(this);

        buttonPause.setOnClickListener(new OnClickListenerGameButton(openGlRender));

        buttonPause.setText("Pause");

        row.setBackgroundColor(Color.DKGRAY);

        textViewScore.setWidth((width-10) / 3);
        textViewResScore.setWidth((width-10) / 3);
        buttonPause.setWidth((width - 10) / 3);

        row.addView(textViewScore, 0);
        row.addView(textViewResScore, 1);
        row.addView(buttonPause, 2);

        view.setRenderer(openGlRender);
        view.setOnTouchListener(openGlRender);

        this.setContentView(view);


        this.addContentView(row, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        timer = new Timer();

        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable() {
                    public void run() {
                        textViewResScore.setText(String.valueOf(openGlRender.getScorePlayer()));
                        stringScore = String.valueOf(openGlRender.getScorePlayer());

                        //if(openGlRender.getStatusGame() == true)
                        //{
                            if(openGlRender.loose())
                            {
                                finish();
                            }
                            /*
                            Dialog d = new Dialog(getBaseContext());
                            d.setTitle("Perdu");

                            LinearLayout l = new LinearLayout(getBaseContext());
                            TextView tLoose = new TextView(getBaseContext());
                            tLoose.setText("Vous avez perdu, cliquer pour recommencer !");

                            l.addView(tLoose);

                            d.setContentView(l);
                            */
                        //}
                    }
                });
            }
        }, 250, 250);
    };

    public void showScore()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Perdu ! Votre score : "+this.stringScore);
        builder1.setCancelable(true);
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
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
