package com.example.k.alltogether.alltogether;

import android.graphics.Bitmap;

/**
 * Created by K on 2015-11-13.
 */
public class Bubble {
    int x;
    int y;
    int bubbleR, bombBubbleR;
    boolean isBombBubble;
    Bitmap imgBubble, imgFeverBubble, imgBombBubble;

    public Bubble(int x, int y, int r, Bitmap m_ImgBubble, Bitmap m_ImgFeverBubble){ // 일반 버블 생성자
        this.x = x;
        this.y = y;
        this.bubbleR = r;

        imgBubble = m_ImgBubble;
        imgBubble = Bitmap.createScaledBitmap(imgBubble, r * 2, r * 2, false); // 버블 반지름에 맞게 버블 이미지 조정
        imgFeverBubble = m_ImgFeverBubble;
        imgFeverBubble = Bitmap.createScaledBitmap(imgFeverBubble, r * 2, r * 2, false); // 버블 반지름에 맞게 버블 이미지 조정
    }

    public Bubble(int x, int y, int r, Bitmap m_ImgBombBubble){ // 폭탄 버블 생성자
        this.x = x;
        this.y = y;
        this.bubbleR = r;
        bombBubbleR = r*3;
        isBombBubble = true;

        imgBombBubble = m_ImgBombBubble;
        imgBombBubble = Bitmap.createScaledBitmap(imgBombBubble, r * 2, r * 2, false); // 버블 반지름에 맞게 버블 이미지 조정
    }

    /*
        화면 터치시 터치한 좌표가 버블 안인지 밖인지 판별해주는 함수
    */
    public boolean contains(int x, int y){
        return Math.pow(this.x-x, 2)+Math.pow(this.y-y, 2) <= Math.pow(bubbleR, 2);
    }

    /*
        폭탄 버블 터치시 주변 버블 위치를 판별해주는 함수
    */
    public boolean bombContains(int x, int y){
        return Math.pow(this.x-x, 2)+Math.pow(this.y-y, 2) <= Math.pow(bombBubbleR, 2);
    }
}
