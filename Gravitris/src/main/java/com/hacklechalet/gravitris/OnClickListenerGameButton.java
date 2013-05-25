package com.hacklechalet.gravitris;

import android.view.View;

/**
 * Created by samuel on 25/05/13.
 */
public class OnClickListenerGameButton implements View.OnClickListener {
    private OpenGLRenderer render;
    private boolean pause = false;

    public OnClickListenerGameButton(OpenGLRenderer render)
    {
        this.render = render;
    }

    @Override
    public void onClick(View v) {
        if(!this.pause)
        {
            this.pause = true;
            render.setPause();
        }
        else
        {
            this.pause = false;
            render.resume();
        }
    }
}
