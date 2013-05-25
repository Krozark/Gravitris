package com.hacklechalet.gravitris;

import android.os.SystemClock;

/**
 * Created by samuel on 25/05/13.
 */
public class Clock{

    long timeStart;

    public Clock()
    {
        this.start();
    }

    public void start()
    {
        timeStart = System.currentTimeMillis();
    }

    public void restart()
    {
        timeStart = System.currentTimeMillis();
    }

    public long reset()
    {
        long timeActu = System.currentTimeMillis();
        long res =  timeActu - this.timeStart;

        this.timeStart = timeActu;

        return res;
    }

    public void stop()
    {
        timeStart = System.currentTimeMillis();
    }
}
