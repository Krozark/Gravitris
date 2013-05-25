package com.hacklechalet.gravitris;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.*;

/**
 * Created by samuel on 25/05/13.
 */
public class GameActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window win = this.getWindow();
        this.requestWindowFeature(win.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.startGame();
    }

    public void startGame()
    {
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();  // deprecated

        GLSurfaceView view = new GLSurfaceView(this);
        view.setRenderer(new OpenGLRenderer(width,height));


        this.setContentView(view);
    }
}
