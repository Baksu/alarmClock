package com.example.budzik;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MathRiddle extends View
{

    private Context my_context;
    private Random generator;

    private boolean touchDown;
    private float x_pos,y_pos;

    private int top_margin = 160, left_margin=150, right_margin = 100;
    private int rect_height = 150;

    private int random_range = 100, min_random_size=100, random_diff_size=5;
    private int max_number = min_random_size+random_range, max_output=max_number*max_number;

    private int first_number = 0, second_number = 0;
    private int correct_output =0;
    private List<Integer> all_outputs;

    private char operator;
    private char[] operators = {'+','-','*'};
    private Paint text_paint, violet_paint, output_text_paint;

    private int screenWidth, screenHeight;

    private Point[] pos;
    private String description = "Tap in growing order";

    public MathRiddle(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        generator = new Random();
        my_context = context;
    }

    private int resolve(char operator, int first_number, int second_number)
    {
        int output = 0;
        switch (operator)
        {
            case '+':
                 output = first_number + second_number;
                break;
            case '-':
                 output = first_number - second_number;
                break;
            case '*':
                 output = first_number * second_number;
                break;

            default:
                Log.e("resolve function : ","Nieznany symbol");
            break;
        }

        return output;
    }

    private List<Integer> getFalse(char operator, int first_number, int second_number)
    {
        List<Integer> output = new ArrayList<Integer>();

        for(int i = 0; i < 3; i++)
        {
            int val_1 = first_number;
            int val_2 = second_number;
            if(generator.nextInt(2)%2!=0)
            {
                val_1 = generator.nextInt(2) % 2 != 0 ? first_number - generator.nextInt(random_diff_size) - 1 : first_number + generator.nextInt(random_diff_size) + 1;
            }
            else
            {
                val_2 = generator.nextInt(2) % 2 != 0 ? second_number - generator.nextInt(random_diff_size) - 1 : second_number + generator.nextInt(random_diff_size) + 1;
            }

            output.add(resolve(operator, val_1, val_2));
        }

        return output;
    }

    private void init()
    {

        first_number = generator.nextInt(random_range)+min_random_size;

        second_number = generator.nextInt(random_range)+min_random_size;

        operator = operators[generator.nextInt(operators.length)];

        correct_output = resolve(operator, first_number, second_number);

        all_outputs = getFalse(operator, first_number, second_number);

        all_outputs.add(generator.nextInt(4), correct_output);

        text_paint = new Paint(0);
        int size = determineMaxTextSize(""+first_number,getMeasuredWidth()/2);
        text_paint.setTextSize(size);
        text_paint.setColor(0xff000000);

        output_text_paint = new Paint(0);
        size = determineMaxTextSize(""+min_random_size*random_range,getMeasuredWidth()/3);
        output_text_paint.setTextSize(size);
        output_text_paint.setColor(0xff000000);

        violet_paint = new Paint(0);
        violet_paint.setColor(0xffAA66CC);
        violet_paint.setStrokeWidth(5);
        violet_paint.setStyle(Paint.Style.STROKE);
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

        //canvas.drawLine(10, screenHeight/2, screenWidth-10, screenHeight/2, violet_paint);

        canvas.drawText(""+first_number,left_margin,top_margin,text_paint);
        canvas.drawText(""+second_number,left_margin,top_margin+text_paint.getTextSize(),text_paint);
        canvas.drawText("" + operator, 10, top_margin + text_paint.getTextSize(), text_paint);
        canvas.drawLine(20, top_margin + text_paint.getTextSize() + 10, screenWidth - 20, top_margin + text_paint.getTextSize() + 10, violet_paint);

        pos = randPos(output_text_paint);

        for(int i=0; i<all_outputs.size(); i++)
        {
            canvas.drawText(""+all_outputs.get(i),pos[i].x, pos[i].y, output_text_paint);
        }

        int val_1 = (int) output_text_paint.measureText(""+correct_output);
        canvas.drawLine(pos[0].x, pos[0].y+15 ,pos[2].x+val_1, pos[2].y+15, violet_paint);
        canvas.drawLine(screenWidth/2, pos[0].y-output_text_paint.getTextSize() ,screenWidth/2, pos[1].y+15, violet_paint);

    }

    private Point[] randPos(Paint paint)
    {
        Point[] pos = new Point[4];

        for(int i =0; i< pos.length; i++)
        {
            pos[i] = new Point();
        }

        int pos_correction = (int) paint.measureText(""+correct_output);

        pos[0].x = screenWidth/2 - pos_correction;
        pos[1].x = screenWidth/2 - pos_correction;
        pos[2].x = pos[0].x + pos_correction;
        pos[3].x = pos[1].x + pos_correction;

        pos[0].x -= 50;
        pos[1].x -= 50;
        pos[2].x += 50;
        pos[3].x += 50;

        pos[0].y = (int) (screenHeight-2* paint.getTextSize()-15) - 100;
        pos[1].y = (int) (screenHeight- paint.getTextSize()+15) - 100;
        pos[2].y = (int) (screenHeight-2* paint.getTextSize()-15) - 100;
        pos[3].y = (int) (screenHeight- paint.getTextSize()+15) - 100;

        return pos;
    }

    public void update()
    {
        if(pos != null) {
            if (x_pos < screenWidth / 2 && y_pos < pos[0].y + 15 && y_pos > screenHeight / 2) {
                if (all_outputs.get(0) != correct_output) {
                    init();
                }
            } else if (x_pos < screenWidth / 2 && y_pos > pos[0].y + 15) {
                if (all_outputs.get(1) != correct_output) {
                    init();
                }
            } else if (x_pos > screenWidth / 2 && y_pos < pos[0].y + 15 && y_pos > screenHeight / 2) {
                if (all_outputs.get(2) != correct_output) {
                    init();
                }
            } else if (x_pos > screenWidth / 2 && y_pos > pos[0].y + 15) {
                if (all_outputs.get(3) != correct_output) {
                    init();
                }
            }
        }
        x_pos = 0;
        y_pos = 0;
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
