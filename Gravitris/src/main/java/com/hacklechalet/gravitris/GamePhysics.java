package com.hacklechalet.gravitris;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

/**
 * Created by krozark on 25/05/13.
 */
public class GamePhysics {
    public World world;
    private int velocityIter = 8;
    private int positionIter = 3;

    public int lineSize = 6;
    public  int score = 0;
    public boolean fail;
    public long TIME_NEXT_SQUARESET = 1000*5;

    public GamePhysics()
    {
        fail = false;
        Vec2 gravity = new Vec2(0.f,9.8f);
        world = new World(gravity);
    }

    public void next(float timeStep)
    {
        world.step(timeStep,velocityIter,positionIter);
    }
}
