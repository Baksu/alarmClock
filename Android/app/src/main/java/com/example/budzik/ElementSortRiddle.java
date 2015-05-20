package com.example.budzik;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ElementSortRiddle extends View
{

    private Context my_context;
    private Random generator;

    private boolean touchDown;
    private float x_pos,y_pos;

    private int number_of_red_elements;
    private int number_of_blue_elements;
    private int number_of_green_elements;
    private int number_of_orange_elements;
    private int points;

    private int random_range = 10;
    private int min_random_elems = 5;

    private List<Point> r_points;
    private List<Point> b_points;
    private List<Point> g_points;
    private List<Point> o_points;

    private Paint red_paint, blue_paint, green_paint, orange_paint, violet_paint, black_paint, text_paint;

    private int rect_size=25;

    private int screenWidth, screenHeight;

    private int[] elems_size = new int[5];

    private String description = "Tap in growing order";

    public ElementSortRiddle(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        generator = new Random();
        my_context = context;
    }

    private void init()
    {
        points = 0;

        text_paint = new Paint(0);
        int size = determineMaxTextSize(description,getMeasuredWidth()-10);
        text_paint.setTextSize(size);
        text_paint.setColor(0xff000000);
        text_paint.setAlpha(50);

        black_paint = new Paint(0);
        black_paint.setColor(0xff000000);

        violet_paint = new Paint(0);
        violet_paint.setColor(0xffAA66CC);
        violet_paint.setStrokeWidth(5);
        violet_paint.setAlpha(50);

        red_paint = new Paint(0);
        red_paint.setColor(0xffff0000);

        blue_paint = new Paint(0);
        blue_paint.setColor(0xff33b5e5);

        green_paint= new Paint(0);
        green_paint.setColor(0xff00ff00);

        orange_paint = new Paint(0);
        orange_paint.setColor(0xffffa500);
        
        number_of_red_elements = getUniqueNumber();
        number_of_blue_elements = getUniqueNumber(number_of_red_elements);
        number_of_green_elements = getUniqueNumber(number_of_red_elements, number_of_blue_elements);
        number_of_orange_elements = getUniqueNumber(number_of_red_elements, number_of_blue_elements, number_of_orange_elements);

        elems_size[0] = number_of_red_elements;
        elems_size[1] = number_of_blue_elements;
        elems_size[2] = number_of_green_elements;
        elems_size[3] = number_of_orange_elements;

        r_points = getPositions(number_of_red_elements, 0, 0);
        b_points = getPositions(number_of_blue_elements, getMeasuredWidth()/2, 0);
        g_points = getPositions(number_of_green_elements, 0, getMeasuredHeight()/2);
        o_points = getPositions(number_of_orange_elements, getMeasuredWidth()/2, getMeasuredHeight()/2);
    }

    private int determineMaxTextSize(String str, float maxWidth)
    {
        int size = 0;
        Paint paint = new Paint();

        do {
            paint.setTextSize(++ size);
        } while(paint.measureText(str) < maxWidth);

        return size;
    }

    public int getUniqueNumber(int... integers)
    {
        boolean uniq=false;
        int num = 0;

        while(!uniq)
        {
            uniq = true;

            num = generator.nextInt(random_range)+min_random_elems;

            for (int i : integers) {
                if (i == num)
                {
                    uniq = false;
                }
            }
        }

        return num;
    }

    public List<Point> getPositions(int number_of_elements, int min_x, int min_y)
    {
        List<Point> list = new ArrayList<Point>();
        int index = 0;

        while(index<number_of_elements)
        {
            Point pos = new Point();

            pos.x = min_x + generator.nextInt(getMeasuredWidth()/2-rect_size);
            pos.y = min_y + generator.nextInt(getMeasuredHeight()/2-rect_size);

            if(!list.contains(pos))
            {
                list.add(pos);
                index++;
            }
        }

        return list;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        screenHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(screenWidth, screenHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        drawRect(canvas,r_points,red_paint);
        drawRect(canvas,b_points,blue_paint);
        drawRect(canvas,g_points,green_paint);
        drawRect(canvas,o_points,orange_paint);

        canvas.drawLine(screenWidth/2, 10, screenWidth/2,screenHeight-10, violet_paint);
        canvas.drawLine(10, screenHeight/2, screenWidth-10, screenHeight/2, violet_paint);

        canvas.drawText(description,5,screenHeight-10,text_paint);
    }

    private void drawRect(Canvas canvas, List<Point> pos_array, Paint paint)
    {
        for(Point pos : pos_array)
        {
            canvas.drawRect(pos.x - 2, pos.y - 2, pos.x + rect_size + 2, pos.y + rect_size + 2, black_paint);
            canvas.drawRect(pos.x, pos.y, pos.x + rect_size, pos.y + rect_size, paint);
        }
    }


    public void update()
    {
        if(x_pos > 0 && y_pos > 0)
        {
            if (x_pos < screenWidth / 2 && y_pos < screenHeight / 2)
            {
                r_points.clear();
                check(0);
            }
            else if (x_pos < screenWidth && y_pos < screenHeight / 2)
            {
                b_points.clear();
                check(1);
            }
            else if (x_pos < screenWidth /2 && y_pos < screenHeight)
            {
                g_points.clear();
                check(2);
            }
            else
            {
                o_points.clear();
                check(3);
            }
        }
        x_pos = 0;
        y_pos = 0;
    }

    public void check(int nr)
    {
        int min = -1;
        int min_nr = -1;
        for(int i=0; i< elems_size.length; i++)
        {
            if(elems_size[i]>0 && (elems_size[i]<min || min == -1)){
                min = elems_size[i];
                min_nr = i;
            }
        }

        if(nr != min_nr || min == -1)
        {
            init();
        }
        else
        {
            points++;
            elems_size[min_nr] = 0;
        }
    }

    public boolean onTouchEvent(MotionEvent e)
    {
        int action = e.getActionMasked();

        switch (action)
        {

            case MotionEvent.ACTION_DOWN:
                if(!touchDown)
                {
                    x_pos = e.getX();
                    y_pos = e.getY();
                }
                touchDown=true;
            break;

            case MotionEvent.ACTION_UP:
                touchDown=false;
            break;
        }
        return true;
    }
}
