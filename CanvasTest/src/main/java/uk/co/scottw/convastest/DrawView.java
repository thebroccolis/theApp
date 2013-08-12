package uk.co.scottw.convastest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.MotionEvent;
import android.graphics.Path;

import java.util.ArrayList;

public class DrawView extends View
{
    Paint paint = new Paint();
    ArrayList<Point> circles = new ArrayList<Point>();
    ArrayList<Integer> colors = new ArrayList<Integer>();
    ArrayList<Integer> pressure = new ArrayList<Integer>();
    public int height = 0;
    public int width = 0;

    public DrawView(Context context)
    {
        super(context);
        paint.setAntiAlias(true);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        int i = 0;
        for(Point cir : circles)
        {
            paint.setColor(colors.get(i));
            canvas.drawCircle(cir.x, cir.y, 10, paint);
            i ++;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float eventX = event.getX();
        float eventY = event.getY();

        int xDiff = (width/2 - ((int)eventX));
        int yDiff = (height/2 - ((int)eventY));

        circles.add(new Point((int)eventX, (int)eventY));
        circles.add(new Point((int)eventX, height/2 + yDiff));
        circles.add(new Point(width/2 + xDiff, (int)eventY));
        circles.add(new Point(width/2 + xDiff, height/2 + yDiff));

        colors.add(Color.rgb((int)(Math.random()*255),(int)(Math.random()*255), (int)(Math.random()*255)));
        colors.add(Color.rgb((int)(Math.random()*255),(int)(Math.random()*255), (int)(Math.random()*255)));
        colors.add(Color.rgb((int)(Math.random()*255),(int)(Math.random()*255), (int)(Math.random()*255)));
        colors.add(Color.rgb((int)(Math.random()*255),(int)(Math.random()*255), (int)(Math.random()*255)));

        invalidate();
        return true;
    }
}