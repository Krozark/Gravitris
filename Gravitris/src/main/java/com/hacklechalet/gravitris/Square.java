package com.hacklechalet.gravitris;
import android.util.Log;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.RopeJointDef;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import java.util.Vector;

import static android.util.FloatMath.cos;
import static android.util.FloatMath.sin;

public class Square extends  PhysiqueObject{
    // Our vertices.
    private Vector2<Float> center;
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

    private float size =1;

    public Square(float _size,float posx,float posy)
    {
        super(posx,posy,BodyType.DYNAMIC);

        top_left = new Vector2<Float>(posx,posy);
        top_right = new Vector2<Float>(posx + (float)1*size, posy);
        bottom_left = new Vector2<Float>(posx, posy + (float)1*size);
        bottom_right = new Vector2<Float>(posx + (float)1*size, posy + (float)1*size);

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

        setSize(_size);
        shape.setAsBox(toMet(size),toMet(size));
        fixtureDef.shape = shape;
        fixture = body.createFixture(fixtureDef);
        //majPosition();

        joinFixtureList = new Vector<Fixture>();
    }


    /**
     * Place a square next to another one
     * @param position Position (1: Top, 2: Right, 3: Bottom, 4: Left)
     */
    public Square genNeighboor(int position)
    {
        /*
        Vector2<Float> originPosition = this.getPosition();
        float originX = originPosition.x;
        float originY = originPosition.y;
        */
        float originX = this.top_left.x;
        float originY = this.top_left.y;
        float targetX;
        float targetY;

        switch(position)
        {
            case DIRECTION_TOP:
                targetX = originX;
                targetY = originY + this.size * 2;
                break;
            case DIRECTION_RIGHT:
                targetX = originX + this.size * 2;
                targetY = originY;
                break;
            case DIRECTION_BOTTOM:
                targetX = originX;
                targetY = originY - this.size * 2;
                break;
            case DIRECTION_LEFT:
            default:
                targetX = originX - this.size * 2;
                targetY = originY;
       }
        Square res = new Square(this.size, targetX, targetY);
        DistanceJointDef jd = new DistanceJointDef();
        jd.initialize(body,res.body,body.getWorldCenter(), res.body.getWorldCenter());
        jd.collideConnected = true;
        jd.frequencyHz = 4.0f;
        jd.dampingRatio = 0.5f;

        world.createJoint(jd);

        return res;
    }


    private void setPosition(float x,float y)
    {
        float angle = body.getAngle();
        float _cos = cos(angle);
        float _sin = sin(angle);

        //x2 = (cos(radials) * x1) - (sin(radials) * y1);
        //y2 = (sin(radials) * x1) + (cos(radials) * y1);

        top_left.x = x*size;
        top_left.y = y*size;
        
        //top_left.x += _cos*size - _sin *size;
        //top_left.y += _sin*size + _cos*size;

        top_right.x = x*size+size;
        top_right.y = y*size;

        //top_right.x += _cos*size - _sin *size;
        //top_right.y += _sin*size + _cos*size;

        bottom_left.x = x*size;
        bottom_left.y = y*size+size;

        //bottom_left.x += _cos*size - _sin *size;
        //bottom_left.y += _sin*size + _cos*size;

        bottom_right.x = x*size+size;
        bottom_right.y = y*size+size;

        //bottom_right.x += _cos*size - _sin *size;
        //bottom_right.y += _sin*size + _cos*size;
    }

    private void majPosition()
    {
        Vec2 origine = body.getPosition();
        setPosition(toPix(origine.x),toPix(origine.y));
    }

    public Vector2<Float> getPosition()
    {
        return new Vector2<Float>(top_left.x+size,top_left.y+size);
    }

    private void setSize(float _size)
    {
        size = _size;
    }

    public void draw(GL10 gl) {
        majPosition();

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

        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        // Point out the where the color buffer is.
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);

        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_SHORT, indexBuffer);

        // Disable the vertices buffer.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        // Disable face culling.
        gl.glDisable(GL10.GL_CULL_FACE);
    }
}