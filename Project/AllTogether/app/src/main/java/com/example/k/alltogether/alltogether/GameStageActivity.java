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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GameStageActivity extends Activity {
    GameStageActivity gameStageActivity;
    boolean gameState; // 게임 진행 상태를 담을 변수
    boolean threadState; // 스레드 상태를 담을 변수
    boolean feverState; // 피버 상태를 담을 변수
    boolean comboState; // 게임 중 콤보상태를 담을 변수 선언
    boolean bombBubbleState; // 폭탄 버블 생성 여부를 담을 변수 선언
    boolean animationState[];
    boolean animationTempState;
    boolean comboDraw;

    int combo; // 게임 중 몇 콤보 수를 담을 변수 선언
    int score; // 게임 중 몇 점수를 담을 변수 선언
    int bubbleRadius;
    int screenWidth, screenHeight; // 스마트폰 화면 사이즈를 담을 변수

    GameView.Bubble newBubble, oldBubble, newBombBubble;
    ArrayList<GameView.Bubble> arrayList; // 게임 속에서 생성되는 버블 객체를 가지고 있을 ArrayList


    ThreadPoolExecutor threadPoolExecutor;
    GameView.BubbleThread bubbleThread; // 버블 객체를 생성해 줄 스레드
    GameView.AnimationThread animationThread;
    GameView.AnimationTempThread animationTempThread;
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
        threadPoolExecutor = new ThreadPoolExecutor(5, 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        gameStageActivity=this;
        gameState=true; // GameStageActivity 가 생성 될 때 게임 진행 상태를 true 로 변경
        arrayList = new ArrayList<GameView.Bubble>(); // arrayList 객체 생성
        screenWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels; // 스마트폰 화면 사이즈 가져오는 함수
        screenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;

        animationState = new boolean[4];

        bubbleRadius = screenWidth/10;

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

        GameView gameView = new GameView(this);
//        setContentView(gameView);
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
                recycle();
                gameState = false;
                gamePauseDialog.show();
            }
        });

        threadState = true;
        threadPoolExecutor.execute(bubbleThread);
        threadPoolExecutor.execute(animationThread);
    }

    public void recycle(){
//        for(int i=0; i<2; i++){
//            for(int j=0; j<4; j++){
//                m_BubbleAnimation[i][j].recycle();
//            }
//        }
//        for(int i=0; i<4; i++){
//            m_BombBubbleInAnimation[i].recycle();
//        }
//        for(int i=0; i<5; i++){
//            m_BombBubbleOutAnimation[i].recycle();
//        }

    }



    public void resetState(){
        arrayList.clear();
        score=0;
        combo=0;
        feverState = false;
        comboState = false;
        bombBubbleState = false;
    }
    /*
        스마트폰에서 디코딩시 아웃오브메모리 줄이기위해서 비율 줄이는 메소드
     */
    private Bitmap decodeFile(int minImageSize, InputStream is){
        Bitmap b = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        int scale = 1;
        if(options.outHeight>minImageSize || options.outWidth>minImageSize){
            scale = (int)Math.pow(  2,  (int)Math.round( Math.log( minImageSize / (double)Math.max( options.outHeight, options.outWidth ) ) / Math.log( 0.5 ) ) );
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        b = BitmapFactory.decodeStream(is, null, o2);

        return b;
    }

    /*
        GameStageActivity 에 화면(View)을 담당해 줄 GameView 클래스
    */
    class GameView extends View {
        Bitmap m_BubbleAnimation[][], m_BombBubbleInAnimation[], m_BombBubbleOutAnimation[];
        Bitmap m_ImgBubble, m_ImgBombBubble, m_ImgFeverBubble;

        int imgBubbleInOut[][] = {{R.drawable.bubble_in0, R.drawable.bubble_in1, R.drawable.bubble_in2, R.drawable.bubble_in3}, {R.drawable.bubble_out0, R.drawable.bubble_out1, R.drawable.bubble_out2, R.drawable.bubble_out3}};
        int imgBombBubbleIn[] = {R.drawable.bomb_bubble_in0, R.drawable.bomb_bubble_in1, R.drawable.bomb_bubble_in2, R.drawable.bomb_bubble_in3};
        int imgBombBubbleOut[] = {R.drawable.bomb_bubble_out0, R.drawable.bomb_bubble_out1, R.drawable.bomb_bubble_out2, R.drawable.bomb_bubble_out3, R.drawable.bomb_bubble_out4};
        int animaitionCount[]; // [0] bubble_in [1] bubble_out [2] bomb_bubble_in [3] bomb_bubble_out
        Paint paint; // 페인트 변수 선언
        Paint txtPaint;

        public GameView(Context context) {
            super(context);
            animaitionCount = new int[4];
//          여기서 이미지를 디코딩을 미리 시켜줘야 어플에 부담이 안간다.

//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 4;

            m_ImgBubble = BitmapFactory.decodeResource(getResources(), R.drawable.bubble); // 버블 이미지 등록
            m_ImgFeverBubble = BitmapFactory.decodeResource(getResources(), R.drawable.fever_bubble); // 피버 버블 이미지 등록
            m_ImgBombBubble = BitmapFactory.decodeResource(getResources(), R.drawable.bomb_bubble); // 폭탄 버블 이미지 등록

            m_BubbleAnimation = new Bitmap[2][4];
            m_BombBubbleInAnimation = new Bitmap[4];
            m_BombBubbleOutAnimation = new Bitmap[5];
            for(int i=0; i<2; i++){
                for(int j=0; j<4; j++) {
                    m_BubbleAnimation[i][j] = BitmapFactory.decodeResource(getResources(), imgBubbleInOut[i][j]);
                    m_BubbleAnimation[i][j] = m_BubbleAnimation[i][j].createScaledBitmap(m_BubbleAnimation[i][j], bubbleRadius * 2, bubbleRadius * 2, false);
                }
            }
            for(int i=0; i<4; i++){
                m_BombBubbleInAnimation[i] = BitmapFactory.decodeResource(getResources(), imgBombBubbleIn[i]);
                m_BombBubbleInAnimation[i] = m_BombBubbleInAnimation[i].createScaledBitmap(m_BombBubbleInAnimation[i], bubbleRadius * 2, bubbleRadius * 2, false);
            }
            for(int i=0; i<5; i++){
                m_BombBubbleOutAnimation[i] = BitmapFactory.decodeResource(getResources(), imgBombBubbleOut[i]);
                m_BombBubbleOutAnimation[i] = m_BombBubbleOutAnimation[i].createScaledBitmap(m_BombBubbleOutAnimation[i], bubbleRadius * 2 * 3, bubbleRadius * 2 * 3, false);
            }

            combo=0;
            comboState=false;
            paint = new Paint();
            txtPaint = new Paint();
            txtPaint.setTextSize(50);
            bubbleThread = new BubbleThread();
            animationThread = new AnimationThread();
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
                        if(!bombBubbleState) {
                            createBubble();
                        }else{
                            int random = (int) (Math.random()*7);
                            if(random == 6) {
                                createBombBubble();
                            }else{
                                createBubble();
                            }
                        }
                    }
                }
            }
            void aniNewBubble(){
                animationState[0] = true;
                for (int i = 0; i < 4; i++) {
                    animaitionCount[0] = i;
                    try {
                        switch (combo) {
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
                            invalidate();
                        }
                    });

                }
                animationState[0] = false;
            }
            void aniNewBombBubble(){
                animationState[2] = true;
                for (int i = 0; i < 4; i++) {
                    animaitionCount[2] = i;
                    try {
                        switch (combo) {
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
                            invalidate();
                        }
                    });

                }
                animationState[2] = false;
            }
            void createBubble(){
                try {
                    int x = (int) ((Math.random() * rectBubbleArea.width() + rectBubbleArea.left));
                    int y = (int) ((Math.random() * rectBubbleArea.height() + rectBubbleArea.top));
                    Bubble bubble = new Bubble(x, y, bubbleRadius);
                    newBubble = bubble;
                    if (point[0].x < (bubble.x - bubble.bubbleR) && point[0].y < (bubble.y - bubble.bubbleR) && point[3].x > (bubble.x + bubble.bubbleR) && point[3].y > (bubble.y + bubble.bubbleR)) {
                        switch (combo) {
                            case 0:case 1:case 2:case 3:case 4:
                                Thread.sleep(500); // 스레드가 0.5초 동안 잠든다 => 0.5초 간격으로 버블 생성
                                aniNewBubble();
                                arrayList.add(bubble);
                                break;
                            case 5:case 6:case 7:case 8:case 9:
                                Thread.sleep(400);
                                aniNewBubble();
                                arrayList.add(bubble);
                                break;
                            case 10:case 11:case 12:case 13:case 14:
                                Thread.sleep(300);
                                aniNewBubble();
                                arrayList.add(bubble);
                                break;
                            case 15:case 16:case 17:case 18:case 19:
                                Thread.sleep(200);
                                aniNewBubble();
                                arrayList.add(bubble);
                                break;
                            default:
                                Thread.sleep(150);
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

            void createBombBubble(){
                try{
                    int x = (int) ((Math.random() * rectBubbleArea.width() + rectBubbleArea.left));
                    int y = (int) ((Math.random() * rectBubbleArea.height() + rectBubbleArea.top));
                    Bubble bubble = new Bubble(x, y, bubbleRadius, true);
                    newBombBubble = bubble;
                    if (point[0].x < (bubble.x - bubble.bubbleR) && point[0].y < (bubble.y - bubble.bubbleR) && point[3].x > (bubble.x + bubble.bubbleR) && point[3].y > (bubble.y + bubble.bubbleR)) {
                        Thread.sleep(500); // 스레드가 0.5초 동안 잠든다 => 0.5초 간격으로 버블 생성
                        aniNewBombBubble();
                        arrayList.add(bubble);
                        }
                }catch (InterruptedException e) {
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
            @Override
            public void run() {
                while (threadState) {
                    if (gameState) {
                        if (animationState[1]) {
                            for (int i = 0; i < 4; i++) {
                                animaitionCount[1] = i;
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
                            comboDraw = false;
                            animationState[1] = false;
                        }
                        if (animationState[3]) {
                            for (int i = 0; i < 5; i++) {
                                animaitionCount[3] = i;
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
                            comboDraw = false;
                            animationState[3] = false;
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
                                animaitionCount[1]= i;
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

        /*
            버블 클래스
        */
        class Bubble{
            int x;
            int y;
            int bubbleR, bombBubbleR;
            boolean isBombBubble;

            public Bubble(int x, int y, int r){ // 일반 버블 생성자
                this.x = x;
                this.y = y;
                this.bubbleR = r;

                m_ImgBubble = Bitmap.createScaledBitmap(m_ImgBubble, r * 2, r * 2, false); // 버블 반지름에 맞게 버블 이미지 조정
                m_ImgFeverBubble = Bitmap.createScaledBitmap(m_ImgFeverBubble, r * 2, r * 2, false); // 버블 반지름에 맞게 버블 이미지 조정
            }

            public Bubble(int x, int y, int r, boolean isBombBubble){ // 폭탄 버블 생성자
                this.x = x;
                this.y = y;
                this.bubbleR = r;
                bombBubbleR = r*3;
                this.isBombBubble = isBombBubble;

                m_ImgBombBubble = Bitmap.createScaledBitmap(m_ImgBombBubble, r * 2, r * 2, false); // 버블 반지름에 맞게 버블 이미지 조정
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

        @Override
        protected void onDraw(Canvas canvas) {

            String strScore = ""+score;
            String strCombo = ""+combo;
            tvScore.setText(strScore);
            tvCombo.setText(strCombo);

            if(feverState) paint.setColor(Color.RED);
            else paint.setColor(Color.BLACK);

//            canvas.drawBitmap(m_FeverGauge, point[1].x + (int) (point[1].x * 0.025), point[1].y - (int) (point[1].y * 0.05), paint);

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
                        canvas.drawBitmap(m_ImgBombBubble, bubble.x - bubble.bubbleR, bubble.y-bubble.bubbleR, paint);
                    else if(feverState)
                        canvas.drawBitmap(m_ImgFeverBubble, bubble.x - bubble.bubbleR, bubble.y-bubble.bubbleR, paint);
                    else
                        canvas.drawBitmap(m_ImgBubble, bubble.x - bubble.bubbleR, bubble.y-bubble.bubbleR, paint);
                }
            }
            if(animationState[0]){
                canvas.drawBitmap(m_BubbleAnimation[0][animaitionCount[0]], newBubble.x - newBubble.bubbleR, newBubble.y - newBubble.bubbleR, paint);
            }
            if(animationState[1]) {
                canvas.drawBitmap(m_BubbleAnimation[1][animaitionCount[1]], oldBubble.x - oldBubble.bubbleR, oldBubble.y - oldBubble.bubbleR, paint);
            }
            if(animationState[2]) {
                canvas.drawBitmap(m_BombBubbleInAnimation[animaitionCount[2]], newBombBubble.x - newBombBubble.bubbleR, newBombBubble.y - newBombBubble.bubbleR, paint);
            }
            if(animationState[3]){
                canvas.drawBitmap(m_BombBubbleOutAnimation[animaitionCount[3]], oldBubble.x - oldBubble.bombBubbleR, oldBubble.y - oldBubble.bombBubbleR, paint);
            }
            if(animationTempState){
                canvas.drawBitmap(m_BubbleAnimation[1][animaitionCount[1]], oldBubble.x - oldBubble.bubbleR, oldBubble.y - oldBubble.bubbleR, paint);
            }
            if(comboDraw){
                String tmp = combo+"COMOBO!!";
                txtPaint.setColor(Color.RED);
                txtPaint.setTextSize(50);
                canvas.drawText(tmp, oldBubble.x + oldBubble.bubbleR /2, oldBubble.y - oldBubble.bubbleR /2, txtPaint);
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
                        oldBubble = bubble;
                        arrayList.remove(bubble);
                        score += 10;
                        ++combo;
                        comboState = true;
                        comboDraw = true;

                        if(bubble.isBombBubble) {
                            animationState[3] = true;
                            vibrator.vibrate(500);
                            animationTempState = true;
                            for (int j = arrayList.size() - 1; 0 <= j; j--) {
                                Bubble tmpBubble = arrayList.get(j);
                                if (bubble.bombContains(tmpBubble.x, tmpBubble.y)) {
                                    arrayList.remove(j);
                                    score += 10;
                                    ++combo;
                                }
                                animationTempState = false;
                            }
                        }else{
                            animationState[1] = true;
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
                            oldBubble = bubble;
                            arrayList.remove(bubble);
                            score += 10;
                            ++combo;
                            comboState = true;
                            comboDraw = true;
                            if(bubble.isBombBubble) {
                                animationState[3] = true;
                                vibrator.vibrate(500);
                                animationTempState = true;
                                for (int j = arrayList.size() - 1; 0 <= j; j--) {
                                    Bubble tmpBubble = arrayList.get(j);
                                    if (bubble.bombContains(tmpBubble.x, tmpBubble.y)) {
                                        arrayList.remove(j);
                                        score += 10;
                                        ++combo;
                                    }
                                    animationTempState = false;
                                }
                            }else{
                                animationState[1] = true;
                            }

                            if(combo >= 30){
                                Toast.makeText(getApplicationContext(), "Fever Time!!", Toast.LENGTH_SHORT).show();
                                threadPoolExecutor.execute(new FeverThread());
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

    @Override
    protected void onDestroy() {
        System.gc();
        super.onDestroy();
    }
}

