package com.hacklechalet.gravitris;

import android.view.View;

/**
 * Created by samuel on 25/05/13.
 */
public class OnClickListenerGameButton implements View.OnClickListener {
    private OpenGLRenderer render;
    private GameActivity activity;
    private boolean pause = false;

    public OnClickListenerGameButton(GameActivity activity, OpenGLRenderer render)
    {
        this.activity = activity;
        this.render = render;
    }

    @Override
    public void onClick(View v) {
        if(!this.pause)
        {
            this.pause = true;
            render.setPause();
            activity.setPausedStatus();
        }
        else
        {
            this.pause = false;
            render.resume();
            activity.setResumedStatus();
        }
    }
}
