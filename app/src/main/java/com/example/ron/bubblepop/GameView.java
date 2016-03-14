package com.example.ron.bubblepop;

/**
 * Created by Ron on 3/12/2016.
 */

import android.content.*;
import android.graphics.*;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.*;
import java.util.*;

public class GameView extends View {
    private int xMin = 0;          // This view's bounds
    private int xMax;
    private int yMin = 0;
    private int yMax;
    private ArrayList<Bubble> bubbles = new ArrayList<Bubble>();
    private ArrayList<Bubble> poppedBubbles = new ArrayList<Bubble>();
    Random r = new Random();
    private Paint paint;           // The paint (e.g. style, color) used for drawing
    private ArrayList<Integer> colorCodes = new ArrayList<Integer>();
    private int redrawCount=0;
    private int score=0;
    private int multiplier= 1;
    private long timer=0;


    // Constructor
    public GameView(Context context) {
        super(context);
        this.setFocusableInTouchMode(true);
        colorCodes.add(Color.BLUE);
        colorCodes.add(Color.GREEN);
        colorCodes.add(Color.YELLOW);
        colorCodes.add(Color.RED);
        colorCodes.add(Color.CYAN);
        colorCodes.add(Color.MAGENTA);
        colorCodes.add(Color.BLACK);
        createNewBubble();
        paint = new Paint();
        timer = 0;
        new CountDownTimer(20000,1000) {

            @Override
            public void onTick(long millis) {
                timer = millis/1000;
            }

            @Override
            public void onFinish() {
                Context context = getContext();
                Intent i = new Intent(context, Retry.class);
                Bundle bundle = new Bundle();
                Intent j = new Intent(context, GamePlay.class);

//Add your data to bundle
                bundle.putInt("finalScore", score);

//Add the bundle to the intent
                i.putExtras(bundle);
                context.startActivity(i);
            }
        }.start();
    }


    // Called back to draw the view. Also called by invalidate().
    @Override
    protected void onDraw(Canvas canvas) {
        if(redrawCount == 20)
        {
            createNewBubble();
            redrawCount = 0;
        }
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        canvas.drawText("Score: " + score, 10, 50, paint);
        canvas.drawText("time: "+timer,this.getWidth()-300, 50, paint);
        for(Bubble b:bubbles)
        {
            try {
                paint.setColor(b.getColorCode());
                canvas.drawOval(b.getBounds(), paint);
            }
            catch(IndexOutOfBoundsException e)
            {

                canvas.drawText(":(",200,200,paint);
            }
        }

        // Update the position of the ball, including collision detection and reaction.
        update();

        // Delay
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) { }

        invalidate();  // Force a re-draw
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            float currentX = event.getX();
            float currentY = event.getY();

            for(Bubble b:bubbles)
            {
                if(b.getBounds().contains(currentX,currentY))
                {
                    if(poppedBubbles.size() == 0)
                        poppedBubbles.add(b);
                    else
                    {
                        if(poppedBubbles.get(poppedBubbles.size()-1).getColorCode() == b.getColorCode())
                        {
                            poppedBubbles.add(b);
                            multiplier++;
                        }
                        else
                        {
                            poppedBubbles.clear();
                            multiplier = 1;
                        }
                    }
                    bubbles.remove(b);
                    score += multiplier;
                    invalidate();
                    break;
                }
            }
        }
        return true;  // Event handled
    }
    private void createNewBubble() {
        Bubble bubble = new Bubble(colorCodes.get(r.nextInt(6)));
        boolean overlapFlag = false;
        //bubble.setBounds();
        //bubbles.add(bubble);
        for (Bubble b:bubbles)
        {
            if(calculateDistance(bubble.getCentreX(), bubble.getCentreY(), b.getCentreX(), b.getCentreY()) < (bubble.getRadius()+b.getRadius()))
            {
                overlapFlag = true;
                break;
            }
        }
        if(overlapFlag)
            createNewBubble();
        else
        {
            bubble.setBounds();
            bubbles.add(bubble);
            return;
        }
        //sphereCount++;
    }

    // Detect collision and update the position of the ball.
    private void update() {
        // Get new (x,y) position
        redrawCount++;
        ArrayList<Bubble> bubblesToRemove = new ArrayList<Bubble>();
        for(Bubble b:bubbles)
        {
            b.updateCentre();

            if (b.getCentreX() + b.getRadius() > xMax) {
                b.setDeltaX(-(b.getDeltaX()));
                b.setCentreX(xMax - b.getRadius());
            }
            else if (b.getCentreX() - b.getRadius() < xMin) {
                b.setDeltaX(-(b.getDeltaX()));
                b.setCentreX(xMin + b.getRadius());
            }
            if (b.getCentreY() - b.getRadius() > yMax) {
                bubblesToRemove.add(b);
            }

            detectCollisionAndRebound();
            b.setBounds();
        }

        for(Bubble b:bubblesToRemove)
        {
            bubbles.remove(b);
        }
    }

    public void detectCollisionAndRebound()
    {
        for (int i=0; i<bubbles.size(); i++)
        {
            for (int j=0; j<bubbles.size(); j++)
            {
                if(i!=j)
                {
                    double dist = calculateDistance(bubbles.get(i).getCentreX(), bubbles.get(i).getCentreY(), bubbles.get(j).getCentreX(), bubbles.get(j).getCentreY());
                    if (dist <= bubbles.get(i).getRadius() + bubbles.get(j).getRadius()) {
                        Bubble b1 = bubbles.get(i);
                        Bubble b2 = bubbles.get(j);
                        int tempX1 = b1.getDeltaX();
                        int tempY1 = b1.getDeltaY();
                        b1.setDelta(b2.getDeltaX(), b2.getDeltaY());
                        b2.setDelta(tempX1, tempY1);
                        b1.updateCentre();
                        b2.updateCentre();
                    }
                }
            }
        }
    }

    public double calculateDistance(int x1, int y1, int x2, int y2)
    {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    // Called back when the view is first created or its size changes.
    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        // Set the movement bounds for the ball
        xMax = w-1;
        yMax = h-1;
    }
}
