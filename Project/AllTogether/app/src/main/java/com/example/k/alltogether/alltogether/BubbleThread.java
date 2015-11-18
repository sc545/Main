package com.example.k.alltogether.alltogether;

import android.graphics.Rect;
import android.os.Handler;

import java.util.ArrayList;

/**
 * Created by K on 2015-11-13.
 */
public class BubbleThread extends Thread {
    Handler handler; // 핸들러 사용 이유 : 메인스레드를 제외하고는 View 를 건드릴 수 없기 때문에 핸들러를 통해서 화면 갱신 => invalidate()
    GameStageActivity gameStageActivity;
    GameStageActivity.GameView gameView;
    ArrayList<Bubble> arrayListBubble, arrayListFeverBubble;
    Rect rectBubbleArea;
    GameOverDialog gameOverDialog;
    int score;

    public BubbleThread(GameStageActivity gameStageActivity){
        handler = new Handler();
        this.gameStageActivity = gameStageActivity;
        gameView = gameStageActivity.gameView;
        arrayListBubble = gameStageActivity.m_arraylistBubble;
        arrayListFeverBubble = gameStageActivity.m_arraylistFeverBubble;
        rectBubbleArea = gameStageActivity.m_rectBubbleArea;
        gameOverDialog = gameStageActivity.m_dlgGameOverDialog;
    }

    public void run(){
        while(gameStageActivity.m_bThreadState) {
            if(gameStageActivity.m_bGameState) {
                score = gameStageActivity.m_nScore/10000;
                if(gameStageActivity.m_bFeverState){
                    createBubble(arrayListFeverBubble);
                }else if(gameStageActivity.m_bBombBubbleState) {
                    int random = (int) (Math.random()*7);
                    if(random == 6) {
                        createBombBubble();
                    }else{
                        createBubble(arrayListBubble);
                    }
                }else{
                    createBubble(arrayListBubble);
                }


            }
        }
    }
    void createBubble(ArrayList<Bubble> array){
        ArrayList<Bubble> arrayList = array;
        try {
            int x = (int) ((Math.random() * rectBubbleArea.width() + rectBubbleArea.left));
            int y = (int) ((Math.random() * rectBubbleArea.height() + rectBubbleArea.top));
            int r = (int) (Math.random()*2);
            Bubble bubble = new Bubble(x, y, gameStageActivity.m_nBubbleRadius, gameStageActivity);
//            switch (r){
//                case 0:
//                    bubble = new Bubble(x, y, gameStageActivity.m_nBubbleRadius*1, gameStageActivity);
//                    break;
//                case 1:
//                    bubble = new Bubble(x, y, gameStageActivity.m_nBubbleRadius*2, gameStageActivity);
//                    break;
//            }
            gameStageActivity.m_NewBubble = bubble;
            if (rectBubbleArea.left < (bubble.x - bubble.bubbleR) && rectBubbleArea.top < (bubble.y - bubble.bubbleR) && rectBubbleArea.right > (bubble.x + bubble.bubbleR) && rectBubbleArea.bottom > (bubble.y + bubble.bubbleR)) {
                if (!gameStageActivity.m_bFeverState) {
                    switch (gameStageActivity.m_nCombo) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                            Thread.sleep(400 - score); // 스레드가 0.5초 동안 잠든다 => 0.5초 간격으로 버블 생성
                            aniNewBubble();
                            arrayList.add(bubble);
                            break;
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                            Thread.sleep(350 - score);
                            aniNewBubble();
                            arrayList.add(bubble);
                            break;
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                            Thread.sleep(300 - score);
                            aniNewBubble();
                            arrayList.add(bubble);
                            break;
                        case 15:
                        case 16:
                        case 17:
                        case 18:
                        case 19:
                            Thread.sleep(250 - score);
                            aniNewBubble();
                            arrayList.add(bubble);
                            break;
                        case 20:
                        case 21:
                        case 22:
                        case 23:
                        case 24:
                            Thread.sleep(200 - score);
                            aniNewBubble();
                            arrayList.add(bubble);
                            break;
                        case 25:
                        case 26:
                        case 27:
                        case 28:
                        case 29:
                            Thread.sleep(190 - score);
                            aniNewBubble();
                            arrayList.add(bubble);
                            break;
                        case 30:
                        case 31:
                        case 32:
                        case 33:
                        case 34:
                            Thread.sleep(180 - score);
                            aniNewBubble();
                            arrayList.add(bubble);
                            break;
                        case 35:
                        case 36:
                        case 37:
                        case 38:
                        case 39:
                            Thread.sleep(170 - score);
                            aniNewBubble();
                            arrayList.add(bubble);
                            break;
                        case 40:
                        case 41:
                        case 42:
                        case 43:
                        case 44:
                            Thread.sleep(160 - score);
                            aniNewBubble();
                            arrayList.add(bubble);
                            break;
                        default:
                            Thread.sleep(150 - score);
                            aniNewBubble();
                            arrayList.add(bubble);
                    }
                }else{
                    Thread.sleep(100 - score);
                    aniNewBubble();
                    arrayList.add(bubble);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                gameView.invalidate(); // 핸들러를 통해서 화면 갱신
                if (arrayListBubble.size() == gameStageActivity.m_nBubbleMaxSize) { // 게임 실패시
                    gameStageActivity.m_bGameState = false;
                    gameOverDialog.musicStart();
                    gameOverDialog.show();
                }

                if(arrayListFeverBubble.size() >= 50)
                    arrayListFeverBubble.clear();
                gameStageActivity.m_bBombBubbleState = gameStageActivity.m_nCombo > 10 && arrayListBubble.size() > 20;

            }
        });
    }

    void createBombBubble(){
        try{
            int x = (int) ((Math.random() * rectBubbleArea.width() + rectBubbleArea.left));
            int y = (int) ((Math.random() * rectBubbleArea.height() + rectBubbleArea.top));
            Bubble bubble = new Bubble(x, y, gameStageActivity.m_nBubbleRadius, gameStageActivity, true);
            gameStageActivity.m_NewBubble = bubble;
            if (rectBubbleArea.left < (bubble.x - bubble.bubbleR) && rectBubbleArea.top < (bubble.y - bubble.bubbleR) && rectBubbleArea.right > (bubble.x + bubble.bubbleR) && rectBubbleArea.bottom > (bubble.y + bubble.bubbleR)) {
                switch (gameStageActivity.m_nCombo) {
                    case 0:case 1:case 2:case 3:case 4:
                        Thread.sleep(500-score); // 스레드가 0.5초 동안 잠든다 => 0.5초 간격으로 버블 생성
                        aniNewBombBubble();
                        arrayListBubble.add(bubble);
                        break;
                    case 5:case 6:case 7:case 8:case 9:
                        Thread.sleep(400-score);
                        aniNewBombBubble();
                        arrayListBubble.add(bubble);
                        break;
                    case 10:case 11:case 12:case 13:case 14:
                        Thread.sleep(300-score);
                        aniNewBombBubble();
                        arrayListBubble.add(bubble);
                        break;
                    case 15:case 16:case 17:case 18:case 19:
                        Thread.sleep(200-score);
                        aniNewBombBubble();
                        arrayListBubble.add(bubble);
                        break;
                    case 20:case 21:case 22:case 23:case 24:
                        Thread.sleep(150-score);
                        aniNewBombBubble();
                        arrayListBubble.add(bubble);
                        break;
                    case 25:case 26:case 27:case 28:case 29:
                        Thread.sleep(140-score);
                        aniNewBombBubble();
                        arrayListBubble.add(bubble);
                        break;
                    case 30:case 31:case 32:case 33:case 34:
                        Thread.sleep(130-score);
                        aniNewBombBubble();
                        arrayListBubble.add(bubble);
                        break;
                    case 35:case 36:case 37:case 38:case 39:
                        Thread.sleep(120-score);
                        aniNewBombBubble();
                        arrayListBubble.add(bubble);
                        break;
                    case 40:case 41:case 42:case 43:case 44:
                        Thread.sleep(110-score);
                        aniNewBombBubble();
                        arrayListBubble.add(bubble);
                        break;
                    default:
                        Thread.sleep(100-score);
                        aniNewBombBubble();
                        arrayListBubble.add(bubble);
                }
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                gameView.invalidate(); // 핸들러를 통해서 화면 갱신
                if (arrayListBubble.size() == gameStageActivity.m_nBubbleMaxSize) { // 게임 실패시
                    gameStageActivity.m_bGameState = false;
                    gameOverDialog.musicStart();
                    gameOverDialog.show();
                }
                gameStageActivity.m_bBombBubbleState = gameStageActivity.m_nCombo > 10 && arrayListBubble.size() > 20;
            }
        });
    }
    void aniNewBubble(){
        gameStageActivity.m_bAnimationState[0] = true;
        for (int i = 0; i < 4; i++) {
            gameStageActivity.m_iAnimationCount[0] = i;
            try {
                switch (gameStageActivity.m_nCombo) {
                    case 0:case 1:case 2:case 3:case 4:
                        Thread.sleep(80); // 스레드가 0.5초 동안 잠든다 => 0.5초 간격으로 버블 생성
                        break;
                    case 5:case 6:case 7:case 8:case 9:
                        Thread.sleep(60);
                        break;
                    case 10:case 11:case 12:case 13:case 14:
                        Thread.sleep(40);
                        break;
                    case 15:case 16:case 17:case 18:case 19:
                        Thread.sleep(20);
                        break;
                    default:
                        Thread.sleep(15);
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    gameView.invalidate();
                }
            });

        }
        gameStageActivity.m_bAnimationState[0] = false;
    }
    void aniNewBombBubble(){
        gameStageActivity.m_bAnimationState[2] = true;
        for (int i = 0; i < 4; i++) {
            gameStageActivity.m_iAnimationCount[2] = i;
            try {
                switch (gameStageActivity.m_nCombo) {
                    case 0:case 1:case 2:case 3:case 4:
                        Thread.sleep(80); // 스레드가 0.5초 동안 잠든다 => 0.5초 간격으로 버블 생성
                        break;
                    case 5:case 6:case 7:case 8:case 9:
                        Thread.sleep(60);
                        break;
                    case 10:case 11:case 12:case 13:case 14:
                        Thread.sleep(40);
                        break;
                    case 15:case 16:case 17:case 18:case 19:
                        Thread.sleep(20);
                        break;
                    default:
                        Thread.sleep(15);
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    gameView.invalidate();
                }
            });

        }
        gameStageActivity.m_bAnimationState[2] = false;
    }

}
