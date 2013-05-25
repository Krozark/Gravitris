package com.hacklechalet.gravitris;
import static java.lang.Math.sqrt;

/**
 * Created by Krozark on 25/05/13.
 */

public class Square {
    public Square(float x,float y)
    {
        center = new Vector2<Float>((float)x,(float)y);
        top = new Vector2<Float>((float)(-SQRT2+x),(float)(-SQRT2+y));
        right = new Vector2<Float>((float)(1-SQRT2+x),(float)(-SQRT2+y));
        down = new Vector2<Float>((float)(1-SQRT2+x),(float)(1-SQRT2+y));
        left = new Vector2<Float>((float)(-SQRT2+x),(float)(1-SQRT2+y));
    }

    public Square()
    {
        center = new Vector2<Float>((float)0,(float)0);
        top = new Vector2<Float>((float)(-SQRT2),(float)(-SQRT2));
        right = new Vector2<Float>((float)(1-SQRT2),(float)(-SQRT2));
        down = new Vector2<Float>((float)(1-SQRT2),(float)(1-SQRT2));
        left = new Vector2<Float>((float)(-SQRT2),(float)(1-SQRT2));
    }

    public void rotate(float angle)
    {
    }

    private Vector2<Float> center;
    private Vector2<Float> top; //0,0
    private Vector2<Float> right;//1,0
    private Vector2<Float> down;//1,1
    private Vector2<Float> left;//0,1
    final double SQRT2 =  sqrt(2.0);
}
