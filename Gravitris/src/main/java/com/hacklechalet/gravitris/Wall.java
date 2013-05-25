package com.hacklechalet.gravitris;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.joints.DistanceJointDef;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Vector;

/**
 * Created by krozark on 25/05/13.
 */
public class Wall  extends  PhysiqueObject{
    // Our vertices.
    private Vector2<Float> top_left; //0,0
    private Vector2<Float> top_right;//1,0
    private Vector2<Float> bottom_left;//1,1
    private Vector2<Float> bottom_right;//0,1
    // The order we like to connect them.
    private short[] indices = { 0, 1, 2, 2, 1, 3 };

    public final static int DIRECTION_TOP = 1;
    public final static int DIRECTION_RIGHT = 2;
    public final static int DIRECTION_BOTTOM = 3;
    public final static int DIRECTION_LEFT = 4;

    float[] colors = {
            1f, 0f, 0f, 1f, // vertex 0 red
            0f, 1f, 0f, 1f, // vertex 1 green
            0f, 0f, 1f, 1f, // vertex 2 blue
            1f, 0f, 1f, 1f, // vertex 3 magenta
    };

    // Our vertex buffer.
    private FloatBuffer vertexBuffer;
    // Our index buffer.
    private ShortBuffer indexBuffer;
    private FloatBuffer colorBuffer;

    private Vector<Fixture> joinFixtureList;

    private float X;
    private float Y;

    public Wall(float x,float y,float posx,float posy)
    {
        super(posx,posy, BodyType.STATIC);

        top_left = new Vector2<Float>(posx,posy);
        top_right = new Vector2<Float>(posx + (float)1*x, posy);
        bottom_left = new Vector2<Float>(posx, posy + (float)1*y);
        bottom_right = new Vector2<Float>(posx + (float)1*x, posy + (float)1*y);

        // short is 2 bytes, therefore we multiply the number if
        // vertices with 2.
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indexBuffer = ibb.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);

        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer = cbb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);

        setSize(x,y);
        shape.setAsBox(toMet(x/2),toMet(y/2));
        fixtureDef.shape = shape;
        fixture = body.createFixture(fixtureDef);
        //majPosition();

        joinFixtureList = new Vector<Fixture>();
    }


    private void setPosition(float x,float y)
    {
        top_left.x = x*X;
        top_left.y = y*Y;

        top_right.x = x*X+X;
        top_right.y = y*Y;

        bottom_left.x = x*X;
        bottom_left.y = y*Y+Y;

        bottom_right.x = x*X+X;
        bottom_right.y = y*Y+Y;
    }

    private void majPosition()
    {
        Vec2 origine = body.getPosition();
        setPosition(toPix(origine.x),toPix(origine.y));
    }

    private void setRotation(float angle)
    {

    }

    public Vector2<Float> getPosition()
    {
        return new Vector2<Float>(top_left.x+X,top_left.y+Y);
    }

    private void setSize(float x,float y)
    {
        X=x;
        Y=y;
    }

    private void next()
    {
        majPosition();
        setRotation(-toDeg(body.getAngle()));
    }

    public void draw(GL10 gl) {
        next();

        float matrixVertices[] = {
                top_left.x,  top_left.y, 0.0f,  // 0, Top Left
                top_right.x,  top_right.y, 0.0f,  // 3, Top Right
                bottom_left.x, bottom_left.y, 0.0f,  // 1, Bottom Left
                bottom_right.x, bottom_right.y, 0.0f,  // 2, Bottom Right
        };
        // a float is 4 bytes, therefore we multiply the number if
        // vertices with 4.
        ByteBuffer vbb = ByteBuffer.allocateDirect(matrixVertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(matrixVertices);
        vertexBuffer.position(0);

        // Counter-clockwise winding.
        gl.glFrontFace(GL10.GL_CCW);
        // Enable face culling.
        gl.glEnable(GL10.GL_CULL_FACE);
        // What faces to remove with the face culling.
        gl.glCullFace(GL10.GL_BACK);

        // Enabled the vertices buffer for writing and to be used during
        // rendering.
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        // Specifies the location and data format of an array of vertex
        // coordinates to use when rendering.
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

        gl.glEnableClientState(GL10.GL_COLOR_ARRAY); // NEW LINE ADDED.
        // Point out the where the color buffer is.
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer); // NEW LINE ADDED

        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_SHORT, indexBuffer);

        // Disable the vertices buffer.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        // Disable face culling.
        gl.glDisable(GL10.GL_CULL_FACE);
    }
}
