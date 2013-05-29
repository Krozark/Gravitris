package com.hacklechalet.gravitris;

import javax.microedition.khronos.opengles.GL10;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by marc on 25/05/13.
 */
public class SquareSet {
    public Set<Square> set;

    protected int type = -1;
    public final static int SQUARE_SET_T = 0;
    public final static int SQUARE_SET_I = 1;
    public final static int SQUARE_SET_L = 2;
    public final static int SQUARE_SET_S = 3;
    public final static int SQUARE_SET_C = 4;
    public final static int SQUARE_SET_L2 = 5;

    public SquareSet()
    {
        this.set = new HashSet<Square>();
    }
    public SquareSet(float size)
    {
        this(size, -1);
    }
    public SquareSet(float size, int type)
    {
        this(size, type, 0);
    }
    public SquareSet(float size, int type, int orientation)
    {
        this();
        if(type < 0 || type > 5)
        {
            Random rnd = new Random();
            type = rnd.nextInt(5);
        }
        Square primitive;
        float x,y;
        if (orientation == 1)// Landscape
        {
            x = 1.5f;
            y = -1.0f;
        }
        else if (orientation == 2) // Landscape in the other way
        {
            x = -3.0f;
            y = -1.0f;
        }
        else // (orientation == 0)  Portrait
        {
            x = -1;
            y = 5;
        }


        switch(type)
        {
            case SQUARE_SET_T:
            {
                float[] c = {
                        1f, 0f, 0f, 1f, // vertex 0 red
                        1f, 0f, 0f, 1f,
                        1f, 0f, 0f, 1f,
                        1f, 0f, 0f, 1f,
                };
                Square.colors = c;
                primitive = new Square(size, x,y);
                this.set.add(primitive);
                this.set.add(primitive.genNeighboor(Square.DIRECTION_LEFT));
                this.set.add(primitive.genNeighboor(Square.DIRECTION_RIGHT));
                this.set.add(primitive.genNeighboor(Square.DIRECTION_BOTTOM));

            }break;
            case SQUARE_SET_I:
            {
                float[] c= {
                        0f, 1f, 0f, 1f,
                        0f, 1f, 0f, 1f, // vertex 1 green
                        0f, 1f, 0f, 1f,
                        0f, 1f, 0f, 1f,
                };
                Square.colors = c;
                primitive = new Square(size, x,y);
                this.set.add(primitive);
                Square left = primitive.genNeighboor(Square.DIRECTION_LEFT);
                this.set.add(left);
                this.set.add(left.genNeighboor(Square.DIRECTION_LEFT));
                this.set.add(primitive.genNeighboor(Square.DIRECTION_RIGHT));
            }break;
            case SQUARE_SET_L:
            {
                float[] c= {
                        0f, 0f, 1f, 1f,
                        0f, 0f, 1f, 1f,
                        0f, 0f, 1f, 1f, // vertex 2 blue
                        0f, 0f, 1f, 1f,
                };
                Square.colors = c;
                primitive = new Square(size, x,y);
                this.set.add(primitive);
                Square right = primitive.genNeighboor(Square.DIRECTION_RIGHT);
                this.set.add(right);
                this.set.add(right.genNeighboor(Square.DIRECTION_RIGHT));
                this.set.add(primitive.genNeighboor(Square.DIRECTION_BOTTOM));
            }break;
            case SQUARE_SET_L2:
            {
                float[] c= {
                        1f, 1f, 0f, 1f,
                        1f, 1f, 0f, 1f,
                        1f, 1f, 0f, 1f,
                        1f, 1f, 0f, 1f,
                };
                Square.colors = c;
                primitive = new Square(size, x,y);
                this.set.add(primitive);
                Square right2 = primitive.genNeighboor(Square.DIRECTION_RIGHT);
                this.set.add(right2);
                this.set.add(right2.genNeighboor(Square.DIRECTION_RIGHT));
                this.set.add(primitive.genNeighboor(Square.DIRECTION_TOP));
            }break;
            case SQUARE_SET_S:
            {
                float[] c= {
                        1f, 0f, 1f, 1f,
                        1f, 0f, 1f, 1f,
                        1f, 0f, 1f, 1f,
                        1f, 0f, 1f, 1f, // vertex 3 magenta
                };
                Square.colors = c;
                primitive = new Square(size, x,y);
                this.set.add(primitive);
                Square rightS = primitive.genNeighboor(Square.DIRECTION_RIGHT);
                this.set.add(rightS);
                Square bottomS = rightS.genNeighboor(Square.DIRECTION_BOTTOM);
                this.set.add(bottomS);
                this.set.add(bottomS.genNeighboor(Square.DIRECTION_RIGHT));
            }   break;
            case SQUARE_SET_C:
            default:
            {
                float[] c= {
                        0f, 1f, 1f, 1f,
                        0f, 1f, 1f, 1f,
                        0f, 1f, 1f, 1f,
                        0f, 1f, 1f, 1f,
                };
                Square.colors = c;
                primitive = new Square(size, x,y);
                this.set.add(primitive);
                Square rightC = primitive.genNeighboor(Square.DIRECTION_RIGHT);
                this.set.add(rightC);
                this.set.add(primitive.genNeighboor(Square.DIRECTION_BOTTOM));
                this.set.add(rightC.genNeighboor(Square.DIRECTION_BOTTOM));
            }
        }
        this.type = type;
    }

    public void add(Square square)
    {
        this.set.add(square);
    }

    public void add(SquareSet squareSet)
    {
        for(Square square : squareSet.set)
        {
            this.add(square);
        }
    }

    public void remove(Square square)
    {
        this.set.remove(square);
    }

    public void remove(SquareSet squareSet)
    {
        for(Square square : squareSet.set)
        {
            this.remove(square);
        }
    }


    public void draw(GL10 gl)
    {
        for(Square square : this.set)
        {
            square.draw(gl);
        }
    }
    public int getType()
    {
        return this.type;
    }
}
