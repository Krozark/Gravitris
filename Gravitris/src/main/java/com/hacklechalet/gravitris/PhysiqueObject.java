package com.hacklechalet.gravitris;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

/**
 * Created by krozark on 25/05/13.
 */
public class PhysiqueObject {
    protected static BodyDef bodyDef = new BodyDef();
    public Body body;
    protected PolygonShape shape;
    protected static FixtureDef fixtureDef = new FixtureDef();
    protected Fixture fixture;

    public PhysiqueObject(float posx,float posy,BodyType type)
    {
        bodyDef.type = type;
        bodyDef.position.set(toMet(posx),toMet(posy));
        body = world.createBody(bodyDef);

        shape = new PolygonShape();

        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.5f;
    }

    protected static float toMet(float x)
    {
        return x/scale;
    }
    protected static float toPix(float x)
    {
        return x*scale;
    }

    protected static float toRad(float x)
    {
        return pi*x/180.0f;
    }

    protected  static float toDeg(float x)
    {
        return 180.0f*x/pi;
    }

    protected static Vector2<Float> toVector2f(Vec2 v)
    {
        return new Vector2<Float>(toPix(v.x),toPix(v.y));
    }

    protected static Vec2 toVec2(Vector2<Float> v)
    {
        return new Vec2(toMet(v.x),toMet(v.y));
    }
    final static float pi = 3.14159265358979323846f;
    final static float scale = 0.25f;
    static World world;
}
