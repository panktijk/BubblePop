package com.example.ron.bubblepop;

import android.graphics.*;

import java.util.*;

/**
 * Created by Ron on 3/12/2016.
 */
public class Bubble {
    private int centreX;
    private int centreY;
    private int deltaX;
    private int deltaY;
    private int radius;
    private RectF bounds = new RectF();
    private int colorCode;
    private Random r = new Random();

    Bubble(int colorCode)
    {
        radius = r.nextInt(40)+60;
        centreX = radius + r.nextInt(1600);
        centreY = radius - 10;
        this.deltaX = r.nextInt(5) + 2;
        this.deltaY = r.nextInt(5) + 2;
        this.colorCode = colorCode;
    }

    public void setCentre(int x, int y)
    {
        centreX = x;
        centreY = y;
    }

    public void setCentreX(int x)
    {
        centreX = x;
    }

    public void setCentreY(int y)
    {
        centreY = y;
    }

    public void updateCentre()
    {
        centreX += deltaX;
        centreY += deltaY;

    }

//    public void UpdateCentre(double x)
//    {
//        centreX += x;
//        centreY += x;
//
//    }

    public void setDelta(int deltaX, int deltaY)
    {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public void setDeltaX(int deltaX)
    {
        this.deltaX = deltaX;
    }

    public void setDeltaY(int deltaY)
    {
        this.deltaY = deltaY;
    }

    public void setColor(int colorCode)
    {
        this.colorCode = colorCode;
    }

    public void setBounds()
    {
        bounds.set(centreX - radius, centreY - radius, centreX + radius, centreY + radius);
    }

    public int getCentreX()
    {
        return centreX;
    }

    public int getCentreY()
    {
        return centreY;
    }

    public int getDeltaX()
    {
        return deltaX;
    }

    public int getDeltaY()
    {
        return deltaY;
    }

    public int getColorCode()
    {
        return colorCode;
    }

    public RectF getBounds()
    {
        return bounds;
    }

    public int getRadius()
    {
        return radius;
    }
}
