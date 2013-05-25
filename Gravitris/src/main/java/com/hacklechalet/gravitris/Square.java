package com.hacklechalet.gravitris;
import android.util.Log;
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


public class Square extends  PhysiqueObject{
    // Our vertices.
    private Vector2<Float> center;
    private Vector2<Float> top_left; //0,0
    private Vector2<Float> top_right;//1,0
    private Vector2<Float> bottom_left;//1,1
    private Vector2<Float> bottom_right;//0,1
    // The order we like to connect them.
    private short[] indices = { 0, 1, 2, 0, 2, 3 };

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

    private float size =1;

    public Square(float _size,float posx,float posy)
    {
        super(posx,posy,BodyType.DYNAMIC);

        top_left = new Vector2<Float>((float)0,(float)1);
        top_right = new Vector2<Float>((float)1,(float)1);
        bottom_left = new Vector2<Float>((float)0,(float)0);
        bottom_right = new Vector2<Float>((float)1,(float)0);

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

        shape.setAsBox(toMet(_size/2),toMet(_size/2));
        fixtureDef.shape = shape;
        fixture = body.createFixture(fixtureDef);
        setSize(_size);

        joinFixtureList = new Vector<Fixture>();
    }

    /**
     * Place a square next to another one
     * @param position Position (1: Top, 2: Right, 3: Bottom, 4: Left)
     */
    public Square genNeighboor(int position)
    {
        Vector2<Float> originPosition = this.getPosition();
        float originX = originPosition.x;
        float originY = originPosition.y;
        float targetX;
        float targetY;

        switch(position)
        {
            case DIRECTION_TOP:
                targetX = originX;
                targetY = originY + 0.5f;
                break;
            case DIRECTION_RIGHT:
                targetX = originX + 0.5f;
                targetY = originY;
                break;
            case DIRECTION_BOTTOM:
                targetX = originX;
                targetY = originY - 0.5f;
                break;
            case DIRECTION_LEFT:
            default:
                targetX = originX - 0.5f;
                targetY = originY;
       }
       Square res = new Square(this.size, targetX, targetY);
        res.joinFixtureList.add(res.body.createFixture(this.shape, 1.0f));
        this.joinFixtureList.add(this.body.createFixture(this.shape, 1.0f));

        //DistanceJointDef jointDef = new DistanceJointDef();
        //jointDef.initialize(res.body, this.body, new Vec2(targetX, targetY), new Vec2(originX, originY));
        //jointDef.collideConnected = true;

        return res;
    }

    /*public void move(float x,float y)
    {
        x*=size;
        y*=size;

        top_left.x+=x;
        top_left.y+=y;

        top_right.x+=x;
        top_right.y+=y;

        bottom_left.x+=x;
        bottom_left.y+=y;

        bottom_right.x+=x;
        bottom_right.y+=y;
    }*/

    public void setPosition(float x,float y)
    {
        float ratio = 0.5f*size;

        top_left.x = x-ratio;
        top_left.y = y+ratio;

        top_right.x = x+ratio;
        top_right.y = y+ratio;

        bottom_left.x = x-ratio;
        bottom_left.y = y-ratio;

        bottom_right.x = x+ratio;
        bottom_right.y = y-ratio;
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
        float ratio = 0.5f*size;
        return new Vector2<Float>(top_left.x+ratio,top_left.y-ratio);
    }

    private void setSize(float _size)
    {
        size = _size;
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
                bottom_left.x, bottom_left.y, 0.0f,  // 1, Bottom Left
                bottom_right.x, bottom_right.y, 0.0f,  // 2, Bottom Right
                top_right.x,  top_right.y, 0.0f,  // 3, Top Right
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