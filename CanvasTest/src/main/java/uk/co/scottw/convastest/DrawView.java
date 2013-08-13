package uk.co.scottw.convastest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;

public class DrawView extends View
{
    Paint paint = new Paint();

    public static int height = 0;
    public static int width = 0;

    Player player;
    ArrayList<Wall> walls = new ArrayList<Wall>();


    int floorTopLeftX = 10;
    int floorTopLeftY = 450;

    int floorTopRightX = width - 10;
    int floorTopRightY = 450;

    boolean jumping = false;
    int jumpBase = 400;
    int jumpHeight = 100;
    int jumpSpeed = 15;

    int gravity = 5;

    public DrawView(Context context)
    {
        super(context);
        paint.setAntiAlias(true);
        player = new Player(200, 400, Color.BLACK);
        initWorld();
    }

    private void initWorld()
    {
        walls.add(new Wall(new Point(0,500), new Point(1300, 525), Color.RED));
        walls.add(new Wall(new Point(500,400), new Point(1300, 425), Color.BLUE));
        walls.add(new Wall(new Point(0, 300), new Point(300, 325), Color.GREEN));
    }


    public void onDraw(Canvas canvas)
    {
        if(leftDown)
        {
            if(!collision(player.getX() - 5, player.getY()))
                player.move(-5, 0);
        }

        if(rightDown)
        {
            if(!collision(player.getX() + 5, player.getY()))
                player.move(5, 0);
        }

        doPhysics();

        for(Wall w : walls)
        {
            w.draw(canvas, paint);
        }

        drawButtons(canvas);
        player.draw(canvas, paint);
        postInvalidateOnAnimation();
    }



    public void doPhysics()
    {
        if(!collision(player.getX(), player.getY() + gravity))
            player.move(0, gravity);

        if(jumping && Math.abs(player.getY() - jumpBase) > jumpHeight)
            jumping = false;

        if(jumping && !collision(player.getX(), player.getY() - jumpSpeed))
            player.move(0, -jumpSpeed);

    }

    public boolean collision(int newPlayerX, int newPlayerY)
    {
        for(Wall w : walls)
        {
            if(newPlayerY + 5 >= w.getYMin() && newPlayerX + 5 >w.getXMin() && newPlayerY -5 <= w.getYMax() && newPlayerX - 5 <= w.getXMax())
                return true;
        }
        return false;
    }

    public void drawButtons(Canvas canvas)
    {
        paint.setColor(Color.BLACK);
        //height = 756
        //width = 1280
        canvas.drawRect(100.0f, (float)(height-200), 300.0f, (float)(height -150), paint);
        canvas.drawRect(350.0f, (float)(height-200), 550.0f, (float)(height -150), paint);
        canvas.drawRect((float)(width-300), (float)(height-200), (float)(width-100), (float)(height -150), paint);

        paint.setColor(Color.WHITE);
        canvas.drawLine(150, ((height-200) + (height -150)) / 2, 160, ((height-200) + (height -150)) /2 + 10, paint);
        canvas.drawLine(150, ((height-200) + (height -150)) / 2, 160, ((height-200) + (height -150)) /2 - 10, paint);

        canvas.drawLine(475, ((height-200) + (height -150)) / 2, 465, ((height-200) + (height -150)) /2 + 10, paint);
        canvas.drawLine(475, ((height-200) + (height -150)) / 2, 465, ((height-200) + (height -150)) /2 - 10, paint);

        canvas.drawLine((float)(width-300) + 20, ((height-200) + (height -150)) / 2 - 10, (float)(width-300) + 20 + 10, ((height-200) + (height -150)) / 2 + 10, paint);
        canvas.drawLine((float)(width-300) + 20, ((height-200) + (height -150)) / 2 - 10, (float)(width-300) + 20 - 10, ((height-200) + (height -150)) / 2 + 10, paint);

    }

    boolean leftDown = false;
    boolean rightDown = false;


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float eventX = -1.0f;
        float eventY = -1.0f;
        int actionEvent = -1;


        int action = event.getAction();
        if(event.getPointerCount()>1)
        {
            actionEvent = event.getActionMasked();
            int actionPointerId = event.getActionIndex();
            Log.i("err", Integer.toString(actionPointerId));
            int index = event.findPointerIndex(actionPointerId);

            // Gets its coordinates
            eventX = event.getX(index);
            eventY = event.getY(index);
        }
        else
        {
            eventX = event.getX();
            eventY = event.getY();
            actionEvent = event.getAction();
        }

        if(eventX <= 300 && eventX >= 100 && eventY <= (float)(height -150) && eventY >= (float)(height-200))
        {
            if(actionEvent == MotionEvent.ACTION_DOWN)
                leftDown = true;
            else if(actionEvent == MotionEvent.ACTION_UP)
                leftDown = false;
        }


        if(eventX <= 550 && eventX >= 350 && eventY <= (float)(height -150) && eventY >= (float)(height-200))
        {
            if(actionEvent == MotionEvent.ACTION_DOWN)
                rightDown = true;
            else if(actionEvent == MotionEvent.ACTION_UP)
                rightDown = false;
        }


        if(!jumping && eventX >= (float)(width-300) && eventX <= (float)(width-100) && eventY <= (float)(height -150) && eventY >= (float)(height-200))
        {
            jumping = true;
            jumpBase = player.getY();
        }


        return true;
    }
}