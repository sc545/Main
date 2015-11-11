package com.example.k.alltogether.alltogether;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.k.alltogether.R;

import java.text.Format;
import java.util.ArrayList;

public class GameStageActivity extends Activity {
    GameStageActivity gameStageActivity;
    boolean gameState; // 게임 진행 상태를 담을 변수
    boolean threadState; // 스레드 상태를 담을 변수
    boolean feverState; // 피버 상태를 담을 변수
    boolean comboState; // 게임 중 콤보상태를 담을 변수 선언
    boolean bombBubbleState; // 폭탄 버블 생성 여부를 담을 변수 선언
    boolean animationState;
    boolean animationTempState;
    boolean comboDraw;
    int animaitionCount;
    int combo; // 게임 중 몇 콤보 수를 담을 변수 선언
    int score; // 게임 중 몇 점수를 담을 변수 선언
    GameView.Bubble oldBubble;
    ArrayList<GameView.Bubble> arrayList; // 게임 속에서 생성되는 버블 객체를 가지고 있을 ArrayList
    ArrayList<Thread> threadArrayList;
    int screenWidth, screenHeight; // 스마트폰 화면 사이즈를 담을 변수

    GameView.BubbleThread bubbleThread; // 버블 객체를 생성해 줄 스레드
    GameView.BumbBubbleThread bumbBubbleThread; // 버블 객체를 생성해 줄 스레드
    GameView.AnimationThread animationThread;
    GameView.AnimationTempThread animationTempThread;
    GameView.ComboThread comboThread;
    GameOverDialog gameOverDialog;
    GamePauseDialog gamePauseDialog;
    Rect rectBubbleArea;
    Point point[];
    Vibrator vibrator;
    ProgressBar pbLife;
    Button btnPause;
    TextView tvScore, tvCombo;
    LinearLayout layoutGameArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameStageActivity=this;
        gameState=true; // GameStageActivity 가 생성 될 때 게임 진행 상태를 true 로 변경
        arrayList = new ArrayList<GameView.Bubble>(); // arrayList 객체 생성
        threadArrayList = new ArrayList<Thread>();
        screenWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels; // 스마트폰 화면 사이즈 가져오는 함수
        screenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
        gameOverDialog = new GameOverDialog(this, gameStageActivity);
        gamePauseDialog = new GamePauseDialog(this, gameStageActivity);
        rectBubbleArea = new Rect();
        point = new Point[4];
        for(int i=0; i<4; i++)
            point[i] = new Point();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        setContentView(R.layout.stage_background); // GameStageActivity 에 화면(View)를 담당해줄 GameView 등록

        int startX = (int) (screenWidth*0.01);
        int startY = (int) (screenHeight*0.125);
        int endX = (int) (screenWidth*0.85);
        int endY = (int) (screenHeight*0.9775);

        point[0].x = startX; point[0].y = startY;
        point[1].x = endX; point[1].y = startY;
        point[2].x = startX; point[2].y = endY;
        point[3].x = endX; point[3].y = endY;
        rectBubbleArea.set(point[0].x, point[0].y, point[3].x, point[3].y);


/*

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // xml 레이아웃을 자바로 가져와서 객체화 시켜 줄 수 있는 인플레이터 변수 선언
        LinearLayout activity_main = (LinearLayout)inflater.inflate(R.layout.stage_background, null);
        // 인플레이터 변수를 사용해서 xml 레이아웃 객체화

*/
        GameView gameView = new GameView(this);




        addContentView(gameView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = (View)inflater.inflate(R.layout.game_stage, null);



        layout.setFocusable(true);
        addContentView(layout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        pbLife = (ProgressBar) findViewById(R.id.pbLife);

        btnPause = (Button) findViewById(R.id.btnPause);

        tvScore = (TextView) findViewById(R.id.tvScore);
        tvCombo = (TextView) findViewById(R.id.tvCombo);


        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameState = false;
                gamePauseDialog.show();
            }
        });

        threadState = true;
        bubbleThread.start(); // 버블을 생성시켜 줄 스레드 시작
        bumbBubbleThread.start(); // 폭탄 버블을 생성시켜 줄 스레드 시작
        animationThread.start();
        comboThread.start();
    }

    /*
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View stage_background = (LinearLayout)inflater.inflate(R.layout.stage_background, null);
        layout = (LinearLayout) stage_background.findViewById(R.id.layout_stage);

        LinearLayout layout_main = (LinearLayout) stage_background.findViewById(R.id.layout_main);
    `   */
    // inflater로 xml로 만든 레이아웃을 가져와서 적용시킬순 없을까?


    /*
        GameStageActivity 에 화면(View)을 담당해 줄 GameView 클래스
    */

    public void resetState(){
        arrayList.clear();
        score=0;
        combo=0;
        feverState = false;
        comboState = false;
        bombBubbleState = false;
    }

    class GameView extends View {
        Bitmap m_BackGroundImage, m_StatusBar, m_LifeGauge, m_FeverGauge, m_GamePauseIcon; // GameStageActivity 에 배경화면 변수 선언
        Bitmap m_BubbleAnimation[];
        int img[] = {R.drawable.bubble0, R.drawable.bubble1, R.drawable.bubble2, R.drawable.bubble3};

        Paint paint; // 페인트 변수 선언
        Paint txtPaint;

        public GameView(Context context) {
            super(context);

//            m_BackGroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.stage_background); // 배경화면 등록
//            m_BackGroundImage = Bitmap.createScaledBitmap(m_BackGroundImage, screenWidth, screenHeight, false); // 스마트폰 화면 사이즈에 맞게 배경화면 조정

            m_BubbleAnimation = new Bitmap[4];
            for(int i=0; i<4; i++){
                m_BubbleAnimation[i] = BitmapFactory.decodeResource(getResources(), img[i]);
                m_BubbleAnimation[i] = m_BubbleAnimation[i].createScaledBitmap(m_BubbleAnimation[i], screenWidth / 10*2, screenWidth / 10*2, false);
            }


            m_LifeGauge = BitmapFactory.decodeResource(getResources(), R.drawable.life_gauge);
            m_LifeGauge = Bitmap.createScaledBitmap(m_LifeGauge, (int) (screenWidth * 0.8), (int) (screenHeight * 0.04), false);

            m_StatusBar = BitmapFactory.decodeResource(getResources(), R.drawable.status_bar);
            m_StatusBar = Bitmap.createScaledBitmap(m_StatusBar, (int) (screenWidth * 0.9), (int) (screenHeight * 0.03), false);

            m_FeverGauge = BitmapFactory.decodeResource(getResources(), R.drawable.fever_gauge);
            m_FeverGauge = Bitmap.createScaledBitmap(m_FeverGauge, (int) (screenWidth * 0.125), (int) (screenHeight * 0.85), false);

            m_GamePauseIcon = BitmapFactory.decodeResource(getResources(), R.drawable.game_pause_icon);
            m_GamePauseIcon = Bitmap.createScaledBitmap(m_GamePauseIcon, (int) (screenWidth * 0.15), (int) (screenHeight * 0.075), false);

            combo=0;
            comboState=false;
            paint = new Paint();
            txtPaint = new Paint();
            txtPaint.setTextSize(50);
            bubbleThread = new BubbleThread();
            bumbBubbleThread = new BumbBubbleThread();
            animationThread = new AnimationThread();
            comboThread = new ComboThread();

        }

        public GameView(Context context, AttributeSet attrs){
            super(context, attrs);
        }

        /*
            버블 객체를 생성해 줄 스레드
        */
        class BubbleThread extends Thread {
            Handler handler; // 핸들러 사용 이유 : 메인스레드를 제외하고는 View 를 건드릴 수 없기 때문에 핸들러를 통해서 화면 갱신 => invalidate()
            public BubbleThread(){
                handler = new Handler();
            }
            public void run(){
                while(threadState) {
                    if(gameState) {
                        try {
                            int x = (int) ((Math.random() * rectBubbleArea.width()+ rectBubbleArea.left));
                            int y = (int) ((Math.random() * rectBubbleArea.height()+ rectBubbleArea.top));
                            Bubble bubble = new Bubble(x, y, screenWidth / 10);
                            if(point[0].x <  (bubble.x - bubble.r) && point[0].y < (bubble.y - bubble.r) && point[3].x >  (bubble.x + bubble.r) && point[3].y > (bubble.y + bubble.r)) {
                                switch (combo){
                                    case 0:case 1:case 2:case 3:case 4:
                                        Thread.sleep(500); // 스레드가 0.5초 동안 잠든다 => 0.5초 간격으로 버블 생성
                                        arrayList.add(bubble); break;
                                    case 5:case 6:case 7:case 8:case 9:
                                        Thread.sleep(400);
                                        arrayList.add(bubble); break;
                                    case 10:case 11:case 12:case 13:case 14:
                                        Thread.sleep(300);
                                        arrayList.add(bubble); break;
                                    case 15:case 16:case 17:case 18:case 19:
                                        Thread.sleep(200);
                                        arrayList.add(bubble); break;
                                    case 20:case 21:case 22:case 23:case 24:case 25:case 26:case 27:case 28:case 29:
                                        Thread.sleep(150);
                                        arrayList.add(bubble); break;
                                    default:
                                        Thread.sleep(150);
                                        arrayList.add(bubble); break;
                                }

                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                invalidate(); // 핸들러를 통해서 화면 갱신
                                if (arrayList.size() == 50) { // 게임 실패시
                                    resetState();
                                    gameState = false;
                                    gameOverDialog.show();
                                }

                                bombBubbleState = combo > 10 && arrayList.size() > 20;

                            }
                        });
                    }
                }
            }
        }
        /*
            폭탄 버블 객체를 생성해 줄 스레드
        */
        class BumbBubbleThread extends Thread {
            Handler handler; // 핸들러 사용 이유 : 메인스레드를 제외하고는 View 를 건드릴 수 없기 때문에 핸들러를 통해서 화면 갱신 => invalidate()
            public BumbBubbleThread(){
                handler = new Handler();
            }
            public void run(){
                while(threadState) {
                    if(gameState) {
                        if(bombBubbleState) {
                            int x = (int) ((Math.random() * rectBubbleArea.width() + rectBubbleArea.left));
                            int y = (int) ((Math.random() * rectBubbleArea.height() + rectBubbleArea.top));
                            try {
                                Bubble bubble = new Bubble(x, y, screenWidth / 10, true);
                                if(point[0].x <  (bubble.x - bubble.r) && point[0].y < (bubble.y - bubble.r) && point[3].x >  (bubble.x + bubble.r) && point[3].y > (bubble.y + bubble.r)) {
                                    Thread.sleep(2000);
                                    arrayList.add(bubble);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    invalidate(); // 핸들러를 통해서 화면 갱신
                                    if (arrayList.size() == 50) { // 게임 실패시
                                        resetState();
                                        gameState = false;
                                        gameOverDialog.show();
                                    }
                                    bombBubbleState = combo > 10 && arrayList.size() > 20;
                                }
                            });
                        }
                    }
                }

            }
        }

        /*
            피버 타임을 생성해 줄 스레드
        */
        class FeverThread extends Thread {
            Handler handler; // 핸들러 사용 이유 : 메인스레드를 제외하고는 View 를 건드릴 수 없기 때문에 핸들러를 통해서 화면 갱신 => invalidate()
            public FeverThread(){
                handler = new Handler();
            }
            public void run(){

                feverState = true;
                try {
                    Thread.sleep(10000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                feverState = false;
            }
        }

        class AnimationThread extends Thread{
            Handler handler; // 핸들러 사용 이유 : 메인스레드를 제외하고는 View 를 건드릴 수 없기 때문에 핸들러를 통해서 화면 갱신 => invalidate()
            int x, y, r;
            public AnimationThread() {
                handler = new Handler();
            }
            public void setLocation(int x, int y, int r) {
                this.x = x;
                this.y = y;
                this.r = r;
            }
            @Override
            public void run() {
                while(threadState) {
                    if(gameState) {
                        if(animationState) {
                            for (int i = 0; i < 4; i++) {
                                animaitionCount = i;
                                try {
                                    Thread.sleep(80);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invalidate();
                                    }
                                });
                            }
                            animationState = false;
                        }
                    }

                }
            }
        }

        class AnimationTempThread extends Thread{
            Handler handler; // 핸들러 사용 이유 : 메인스레드를 제외하고는 View 를 건드릴 수 없기 때문에 핸들러를 통해서 화면 갱신 => invalidate()
            int x, y, r;
            public AnimationTempThread() {
                handler = new Handler();
            }
            public void setLocation(int x, int y, int r) {
                this.x = x;
                this.y = y;
                this.r = r;
            }
            @Override
            public void run() {
                            for (int i = 0; i < 4; i++) {
                                animaitionCount = i;
                                try {
                                    Thread.sleep(80);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invalidate();
                                    }
                                });
                            }
            }
        }
        class ComboThread extends Thread{
            Handler handler; // 핸들러 사용 이유 : 메인스레드를 제외하고는 View 를 건드릴 수 없기 때문에 핸들러를 통해서 화면 갱신 => invalidate()
            public ComboThread() {
                handler = new Handler();
            }
            @Override
            public void run() {
                while(threadState) {
                    if(gameState) {
                        if(comboState) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    invalidate();
                                }
                            });
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            comboDraw = false;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    invalidate();
                                }
                            });
                        }
                    }

                }
            }
        }


        /*
            버블 클래스
        */
        class Bubble{
            int x;
            int y;
            int r;
            boolean isBombBubble;
            Bitmap imgBubble, imgBombBubble; // 버블 객체에 이미지 변수 선언

            public Bubble(int x, int y, int r){ // 일반 버블 생성자
                this.x = x;
                this.y = y;
                this.r = r;

                imgBubble = BitmapFactory.decodeResource(getResources(), R.drawable.bubble); // 버블 이미지 등록
                imgBubble = Bitmap.createScaledBitmap(imgBubble, r * 2, r * 2, false); // 버블 반지름에 맞게 버블 이미지 조정
            }

            public Bubble(int x, int y, int r, boolean isBombBubble){ // 폭탄 버블 생성자
                this.x = x;
                this.y = y;
                this.r = r;
                this.isBombBubble = isBombBubble;

                imgBombBubble = BitmapFactory.decodeResource(getResources(), R.drawable.bomb_bubble); // 버블 이미지 등록
                imgBombBubble = Bitmap.createScaledBitmap(imgBombBubble, r * 2, r * 2, false); // 버블 반지름에 맞게 버블 이미지 조정
            }

            /*
                화면 터치시 터치한 좌표가 버블 안인지 밖인지 판별해주는 함수
            */
            public boolean contains(int x, int y){
                return Math.pow(this.x-x, 2)+Math.pow(this.y-y, 2) <= Math.pow(r, 2);
            }

            /*
                폭탄 버블 터치시 주변 버블 위치를 판별해주는 함수
            */
            public boolean bombContains(int x, int y){
                return Math.pow(this.x-x, 2)+Math.pow(this.y-y, 2) <= Math.pow(r*3, 2);
            }


        }

        @Override
        protected void onDraw(Canvas canvas) {


//            canvas.drawBitmap(m_BackGroundImage, 0, 0, paint); // GameStageActivity 배경화면 그리기
//            canvas.drawBitmap(m_LifeGauge, point[0].x, point[0].y - (int) (point[0].y * 0.9), paint);
//            canvas.drawBitmap(m_StatusBar, point[0].x, point[0].y - (int) (point[0].y * 0.3), paint);


//            canvas.drawText(Integer.toString(score), point[0].x + (point[0].x * 25), point[0].y - (int) (point[0].y * 0.05), txtPaint);
//            canvas.drawText(Integer.toString(combo), point[0].x + (point[0].x * 75), point[0].y - (int) (point[0].y * 0.05), txtPaint);
            String strScore = ""+score;
            String strCombo = ""+combo;
            tvScore.setText(strScore);
            tvCombo.setText(strCombo);
            if(feverState) paint.setColor(Color.RED);
            else paint.setColor(Color.BLACK);
            canvas.drawBitmap(m_FeverGauge, point[1].x + (int) (point[1].x * 0.025), point[1].y - (int) (point[1].y * 0.05), paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(point[0].x, point[0].y, point[1].x, point[1].y, paint);
            canvas.drawLine(point[0].x, point[0].y, point[2].x, point[2].y, paint);
            canvas.drawLine(point[1].x, point[1].y, point[3].x, point[3].y, paint);
            canvas.drawLine(point[2].x, point[2].y, point[3].x, point[3].y, paint);
            int currentSize = arrayList.size();
            pbLife.setProgress(currentSize);
            if(gameState){
                for(int i=0; i<currentSize; i++) {
                    Bubble bubble = arrayList.get(i);
                    if(bubble.isBombBubble)
                        canvas.drawBitmap(bubble.imgBombBubble, bubble.x - bubble.r, bubble.y-bubble.r, paint);
                    else
                        canvas.drawBitmap(bubble.imgBubble, bubble.x - bubble.r, bubble.y-bubble.r, paint);
                }
            }
            if(animationState) {
                canvas.drawBitmap(m_BubbleAnimation[animaitionCount], animationThread.x - animationThread.r, animationThread.y - animationThread.r, paint);
            }
            if(animationTempState){
                canvas.drawBitmap(m_BubbleAnimation[animaitionCount], animationTempThread.x - animationThread.r, animationThread.y - animationThread.r, paint);
            }
            if(comboDraw){
                String tmp = combo+"COMOBO!!";
                txtPaint.setColor(Color.RED);
                txtPaint.setTextSize(50);
                canvas.drawText(tmp, oldBubble.x + oldBubble.r/2, oldBubble.y - oldBubble.r/2, txtPaint);
            }

        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int px = (int) event.getX(); // 터치이벤트가 발생 했을때 x좌표값
            int py = (int) event.getY(); // 터치이벤트가 발생 했을때 y좌표값

            if (feverState) {
                for (int i = arrayList.size() - 1; 0 <= i; i--) { // 현재 존재하는 버블 객체 수 만큼 반복
                    Bubble bubble = arrayList.get(i);

                    if (bubble.contains(px, py)) {
                        animationThread.setLocation(bubble.x, bubble.y, bubble.r);
                        oldBubble = bubble;
                        arrayList.remove(bubble);
                        score += 10;
                        ++combo;
                        comboState = true;
                        comboDraw = true;
                        animationState = true;

                        if(bubble.isBombBubble){
                            vibrator.vibrate(500);
                            animationTempState = true;
                            for(int j=arrayList.size() - 1; 0 <= j; j--){
                                Bubble tmpBubble = arrayList.get(j);
                                if(bubble.bombContains(tmpBubble.x, tmpBubble.y)) {
                                    arrayList.remove(j);
                                    score+=10;
                                    ++combo;

//                                    animationTempThread = new AnimationTempThread();
//                                    animationTempThread.setLocation(tmpBubble.x, tmpBubble.y, tmpBubble.r);
//                                    animationTempThread.start();
//                                    try {
//                                        animationTempThread.join();
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                    threadArrayList.add(animationTempThread);
                                }
                            }
//                            for (int k=0; k<threadArrayList.size(); k++){
//                                try {
//                                    threadArrayList.get(k).join();
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
                            animationTempState = false;
                        }
                        break;
                    }
                }
                invalidate();
            } else {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    for (int i = arrayList.size() - 1; 0 <= i; i--) { // 현재 존재하는 버블 객체수 만큼 반복
                        Bubble bubble = arrayList.get(i);

                        if (bubble.contains(px, py)) {
                            animationThread.setLocation(bubble.x, bubble.y, bubble.r);
                            oldBubble = bubble;
                            arrayList.remove(bubble);
                            score += 10;
                            ++combo;
                            comboState = true;
                            comboDraw = true;
                            animationState = true;
                            if(combo >= 30){
                                Toast.makeText(getApplicationContext(), "Fever Time!!", Toast.LENGTH_SHORT).show();
                                new FeverThread().start();
                            }
                            if(bubble.isBombBubble){
                                vibrator.vibrate(500);
                                animationTempState = true;
                                for(int j=arrayList.size() - 1; 0 <= j; j--){
                                    Bubble tmpBubble = arrayList.get(j);
                                    if(bubble.bombContains(tmpBubble.x, tmpBubble.y)) {
                                        arrayList.remove(j);
                                        score+=10;
                                        ++combo;
//                                        animationTempThread = new AnimationTempThread();
//                                        animationTempThread.setLocation(tmpBubble.x, tmpBubble.y, tmpBubble.r);
//                                        animationTempThread.start();
//                                        try {
//                                            animationTempThread.join();
//                                        } catch (InterruptedException e) {
//                                            e.printStackTrace();
//                                        }
//                                        threadArrayList.add(animationTempThread);
                                    }
                                }
//                                for (int k=0; k<threadArrayList.size(); k++){
//                                    try {
//                                        threadArrayList.get(k).join();
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
                                animationTempState = false;
                            }
                            break;

                        } else {
                            comboState = false;
                        }
                    }
                    if (!comboState) combo = 0;
                    invalidate();
                }
            }


            return true;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {// 뒤로가기 누를시
            showGamePauseDialog();
        }
        return false;
    }

    public void showGamePauseDialog(){
        gameState=false;
        gamePauseDialog.show();
    }

}

