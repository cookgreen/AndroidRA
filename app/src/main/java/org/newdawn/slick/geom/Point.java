package org.newdawn.slick.geom;

public class Point{
    protected float x;
    protected float y;

    public Point(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public void setX(float value){
        x = value;
    }

    public void setY(float value){
        y = value;
    }
}