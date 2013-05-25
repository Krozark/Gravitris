package com.hacklechalet.gravitris;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

/**
 * Created by krozark on 25/05/13.
 */
public class GamePhysics {
    private World world;
    private int velocityIter = 8;
    private int positionIter = 3;

    public GamePhysics()
    {
        Vec2 gravity = new Vec2(0.f,-9.8f);
        world = new World(gravity);
    }

    public void next()
    {
        world.step(1f/60f,velocityIter,positionIter);
    }
}
