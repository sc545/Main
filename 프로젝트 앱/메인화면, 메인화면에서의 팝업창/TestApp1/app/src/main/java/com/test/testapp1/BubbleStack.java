package com.test.testapp1;

import android.graphics.Rect;

/**
 * Created by 나두혁 on 2015-10-12.
 */
public class BubbleStack {
    private int size=50;
    private Rect[] stackArr = new Rect[size];
    private int top = 0;

    public void pushBubble(Rect newRect)
    {
        top++;
        //System.out.println("push " + newRect);
        stackArr[top] = newRect;
    }

    public Rect popBubble()
    {
        Rect popRect = stackArr[top];
        top--;
        //System.out.println("pop " + popRect);
        return popRect;
    }

    public boolean fullBubble()
    {
        boolean fullStack;

        if(top==size)
            fullStack=true;
        else
            fullStack=false;

        return fullStack;
    }

}
