package com.example.k.alltogether.alltogether;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GameStageActivity extends Activity {
    GameStageActivity gameStageActivity;
    GameStageActivity.GameView gameView;

    boolean m_bGameState; // 게임 진행 상태를 담을 변수
    boolean m_bThreadState; // 스레드 상태를 담을 변수
    boolean m_bFeverState; // 피버 상태를 담을 변수
    boolean m_bComboState; // 게임 중 콤보상태를 담을 변수 선언
    boolean m_bBombBubbleState; // 폭탄 버블 생성 여부를 담을 변수 선언
    boolean m_bAnimationState[]; // 애니메이션 상태를 담을 변수 선언
    boolean m_bAnimationTempState;
    boolean m_bComboDraw; // 콤보 표시 상태를 담을 변수 선언

    int m_nScreenWidth, m_nScreenHeight; // 스마트폰 화면 사이즈를 담을 변수
    int m_nBubbleRadius; // 버블 반지름
    int m_nCombo; // 게임 중 몇 콤보 수를 담을 변수 선언
    int m_nScore; // 게임 중 몇 점수를 담을 변수 선언
    int m_iAnimationCount[]; // [0] bubble_in [1] bubble_out [2] bomb_bubble_in [3] bomb_bubble_out

    /*
        버블 관련 Bitmap 변수 선언
     */
    Bitmap m_btmpImgBubble, m_btmpImgBombBubble, m_btmpImgFeverBubble;
    Bitmap m_btmpBubbleAnimation[][], m_btmpBombBubbleInAnimation[], m_btmpBombBubbleOutAnimation[];


    ArrayList<Bubble> m_arraylistBubble; // 게임 속에서 생성되는 버블 객체를 가지고 있을 ArrayList
    Bubble m_NewBubble, m_OldBubble, m_NewBombBubble; // 추가, 삭제 될 버블을 가지고 있을 변수

    /*
        다이얼로그 변수
     */
    GameOverDialog m_dlgGameOverDialog;
    GamePauseDialog m_dlgGamePauseDialog;

    Rect m_rectBubbleArea; // 버블 생성 영역

    Vibrator vibrator; // 진동 클래스

    ProgressBar pbLife; // 체력바
    Button btnPause; // 일지정지 버튼
    TextView tvScore, tvCombo; // 점수, 콤보 TextView
    LinearLayout layoutGameArea;

    ThreadPoolExecutor m_ThreadPoolExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
            멤버 변수 초기화
         */
        gameStageActivity=this;

        m_bGameState =true; // GameStageActivity 가 생성 될 때 게임 진행 상태를 true 로 변경
        m_bThreadState = true;
        m_bAnimationState = new boolean[4];

        m_nScreenWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels; // 스마트폰 화면 사이즈 가져오는 함수
        m_nScreenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
        m_nBubbleRadius = m_nScreenWidth /10;
        m_iAnimationCount = new int[4];

        m_arraylistBubble = new ArrayList<Bubble>(); // m_arraylistBubble 객체 생성

        m_dlgGameOverDialog = new GameOverDialog(this, gameStageActivity);
        m_dlgGamePauseDialog = new GamePauseDialog(this, gameStageActivity);

        m_rectBubbleArea = new Rect();

        int startX = (int) (m_nScreenWidth *0.01);
        int startY = (int) (m_nScreenHeight *0.125);
        int endX = (int) (m_nScreenWidth *0.85);
        int endY = (int) (m_nScreenHeight *0.9775);

        m_rectBubbleArea.set(startX, startY, endX, endY);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        /*
            첫 번째 뷰
         */
        setContentView(R.layout.game_stage0);

        /*
            두 번째 뷰
         */
        gameView = new GameView(this);
        addContentView(gameView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        /*
            세 번째 뷰
         */
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.game_stage1, null);
        layout.setFocusable(true);
        addContentView(layout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        pbLife = (ProgressBar) findViewById(R.id.pbLife);

        btnPause = (Button) findViewById(R.id.btnPause);

        tvScore = (TextView) findViewById(R.id.tvScore);
        tvCombo = (TextView) findViewById(R.id.tvCombo);

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_bGameState = false;
                m_dlgGamePauseDialog.show();
            }
        });

        m_ThreadPoolExecutor = new ThreadPoolExecutor(5, 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        m_ThreadPoolExecutor.execute(new BubbleThread(gameStageActivity));
        m_ThreadPoolExecutor.execute(new AnimationThread(gameStageActivity));

    }

    /*
        새로하기를 할 경우 게임상태 초기화 함수
     */
    public void resetState(){
        m_arraylistBubble.clear();
        m_nScore =0;
        m_nCombo =0;
        m_bFeverState = false;
        m_bComboState = false;
        m_bBombBubbleState = false;
    }

    /*
        스마트폰에서 디코딩시 아웃오브메모리 줄이기위해서 비율 줄이는 메소드
     */
//    private Bitmap decodeFile(int minImageSize, InputStream is){
//        Bitmap b = null;
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeStream(is, null, options);
//        int scale = 1;
//        if(options.outHeight>minImageSize || options.outWidth>minImageSize){
//            scale = (int)Math.pow(  2,  (int)Math.round( Math.log( minImageSize / (double)Math.max( options.outHeight, options.outWidth ) ) / Math.log( 0.5 ) ) );
//        }
//        BitmapFactory.Options o2 = new BitmapFactory.Options();
//        o2.inSampleSize = scale;
//        b = BitmapFactory.decodeStream(is, null, o2);
//
//        return b;
//    }

    /*
        GameStageActivity 에 화면(View)을 담당해 줄 GameView 클래스
    */
    class GameView extends View {
        Paint paint; // 페인트 변수 선언
        Paint txtPaint;

        public GameView(Context context) {
            super(context);

//          여기서 이미지를 디코딩을 미리 시켜줘야 어플에 부담이 안간다.

//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 4;


            int imgBubbleInOut[][] = {{R.drawable.bubble_in0, R.drawable.bubble_in1, R.drawable.bubble_in2, R.drawable.bubble_in3}, {R.drawable.bubble_out0, R.drawable.bubble_out1, R.drawable.bubble_out2, R.drawable.bubble_out3}};
            int imgBombBubbleIn[] = {R.drawable.bomb_bubble_in0, R.drawable.bomb_bubble_in1, R.drawable.bomb_bubble_in2, R.drawable.bomb_bubble_in3};
            int imgBombBubbleOut[] = {R.drawable.bomb_bubble_out0, R.drawable.bomb_bubble_out1, R.drawable.bomb_bubble_out2, R.drawable.bomb_bubble_out3, R.drawable.bomb_bubble_out4};

            /*
                버블, 피버버블, 폭탄버블 이미지 가져오기
             */
            m_btmpImgBubble = BitmapFactory.decodeResource(getResources(), R.drawable.bubble); // 버블 이미지 등록
            m_btmpImgFeverBubble = BitmapFactory.decodeResource(getResources(), R.drawable.fever_bubble); // 피버 버블 이미지 등록
            m_btmpImgBombBubble = BitmapFactory.decodeResource(getResources(), R.drawable.bomb_bubble); // 폭탄 버블 이미지 등록

            /*
                버블 애니메이션 이미지 가져오고 크기 재조정
             */
            m_btmpBubbleAnimation = new Bitmap[2][4];
            m_btmpBombBubbleInAnimation = new Bitmap[4];
            m_btmpBombBubbleOutAnimation = new Bitmap[5];
            for(int i=0; i<2; i++){
                for(int j=0; j<4; j++) {
                    m_btmpBubbleAnimation[i][j] = BitmapFactory.decodeResource(getResources(), imgBubbleInOut[i][j]);
                    m_btmpBubbleAnimation[i][j] = m_btmpBubbleAnimation[i][j].createScaledBitmap(m_btmpBubbleAnimation[i][j], m_nBubbleRadius * 2, m_nBubbleRadius * 2, false);
                }
            }
            for(int i=0; i<4; i++){
                m_btmpBombBubbleInAnimation[i] = BitmapFactory.decodeResource(getResources(), imgBombBubbleIn[i]);
                m_btmpBombBubbleInAnimation[i] = m_btmpBombBubbleInAnimation[i].createScaledBitmap(m_btmpBombBubbleInAnimation[i], m_nBubbleRadius * 2, m_nBubbleRadius * 2, false);
            }
            for(int i=0; i<5; i++){
                m_btmpBombBubbleOutAnimation[i] = BitmapFactory.decodeResource(getResources(), imgBombBubbleOut[i]);
                m_btmpBombBubbleOutAnimation[i] = m_btmpBombBubbleOutAnimation[i].createScaledBitmap(m_btmpBombBubbleOutAnimation[i], m_nBubbleRadius * 2 * 3, m_nBubbleRadius * 2 * 3, false);
            }

            paint = new Paint();
            txtPaint = new Paint();
            txtPaint.setTextSize(50);
        }

        public GameView(Context context, AttributeSet attrs){
            super(context, attrs);
        }

//        class AnimationTempThread extends Thread{
//            Handler handler; // 핸들러 사용 이유 : 메인스레드를 제외하고는 View 를 건드릴 수 없기 때문에 핸들러를 통해서 화면 갱신 => invalidate()
//            public AnimationTempThread() {
//                handler = new Handler();
//            }
//            @Override
//            public void run() {
//                            for (int i = 0; i < 4; i++) {
//                                m_iAnimationCount[1]= i;
//                                try {
//                                    Thread.sleep(80);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                handler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        invalidate();
//                                    }
//                                });
//                            }
//            }
//        }

        @Override
        protected void onDraw(Canvas canvas) {

            String strScore = ""+ m_nScore;
            String strCombo = ""+ m_nCombo;
            tvScore.setText(strScore);
            tvCombo.setText(strCombo);

            if(m_bFeverState) paint.setColor(Color.RED);
            else paint.setColor(Color.BLACK);
            /*
                버블 생성 영역 그리기
             */
            paint.setStrokeWidth(5);
            canvas.drawLine(m_rectBubbleArea.left, m_rectBubbleArea.top, m_rectBubbleArea.right, m_rectBubbleArea.top, paint);
            canvas.drawLine(m_rectBubbleArea.left, m_rectBubbleArea.top, m_rectBubbleArea.left, m_rectBubbleArea.bottom, paint);
            canvas.drawLine(m_rectBubbleArea.right, m_rectBubbleArea.top, m_rectBubbleArea.right, m_rectBubbleArea.bottom, paint);
            canvas.drawLine(m_rectBubbleArea.left, m_rectBubbleArea.bottom, m_rectBubbleArea.right, m_rectBubbleArea.bottom, paint);
            /*
                현재 버블 개수 만큼 프로그레스바 갱신
             */
            int currentSize = m_arraylistBubble.size();
            pbLife.setProgress(currentSize);
            /*
                현재 m_arraylistBubble 에 존재하는 버블 그리기
             */
            if(m_bGameState){
                for(int i=0; i<currentSize; i++) {
                    Bubble bubble = m_arraylistBubble.get(i);
                    if(bubble.isBombBubble)
                        canvas.drawBitmap(bubble.imgBombBubble, bubble.x - bubble.bubbleR, bubble.y-bubble.bubbleR, paint);
                    else if(m_bFeverState)
                        canvas.drawBitmap(bubble.imgFeverBubble, bubble.x - bubble.bubbleR, bubble.y-bubble.bubbleR, paint);
                    else
                        canvas.drawBitmap(bubble.imgBubble, bubble.x - bubble.bubbleR, bubble.y-bubble.bubbleR, paint);
                }
            }
            /*
                m_bAnimationState[0] - 새로운 버블이 생성될 경우 애니메이션
                m_bAnimationState[1] - 버블이 터질 경우 애니메이션
                m_bAnimationState[2] - 새로운 폭탄 버블이 생성될 경우 애니메이션
                m_bAnimationState[3] - 폭탄 버블이 터질 경우 애니메이션
             */
            if(m_bAnimationState[0]){
                canvas.drawBitmap(m_btmpBubbleAnimation[0][m_iAnimationCount[0]], m_NewBubble.x - m_NewBubble.bubbleR, m_NewBubble.y - m_NewBubble.bubbleR, paint);
            }
            if(m_bAnimationState[1]) {
                canvas.drawBitmap(m_btmpBubbleAnimation[1][m_iAnimationCount[1]], m_OldBubble.x - m_OldBubble.bubbleR, m_OldBubble.y - m_OldBubble.bubbleR, paint);
            }
            if(m_bAnimationState[2]) {
                canvas.drawBitmap(m_btmpBombBubbleInAnimation[m_iAnimationCount[2]], m_NewBombBubble.x - m_NewBombBubble.bubbleR, m_NewBombBubble.y - m_NewBombBubble.bubbleR, paint);
            }
            if(m_bAnimationState[3]){
                canvas.drawBitmap(m_btmpBombBubbleOutAnimation[m_iAnimationCount[3]], m_OldBubble.x - m_OldBubble.bombBubbleR, m_OldBubble.y - m_OldBubble.bombBubbleR, paint);
            }
            if(m_bAnimationTempState){
                canvas.drawBitmap(m_btmpBubbleAnimation[1][m_iAnimationCount[1]], m_OldBubble.x - m_OldBubble.bubbleR, m_OldBubble.y - m_OldBubble.bubbleR, paint);
            }
            /*
                현재 콤보가 진행중 일 경우 콤보 표시
             */
            if(m_bComboDraw){
                String tmp = m_nCombo +"COMOBO!!";
                txtPaint.setColor(Color.RED);
                txtPaint.setTextSize(50);
                canvas.drawText(tmp, m_OldBubble.x + m_OldBubble.bubbleR /2, m_OldBubble.y - m_OldBubble.bubbleR /2, txtPaint);
            }

        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int px = (int) event.getX(); // 터치이벤트가 발생 했을때 x좌표값
            int py = (int) event.getY(); // 터치이벤트가 발생 했을때 y좌표값

            if (m_bFeverState) {
                for (int i = m_arraylistBubble.size() - 1; 0 <= i; i--) { // 현재 존재하는 버블 객체 수 만큼 반복
                    Bubble bubble = m_arraylistBubble.get(i);

                    if (bubble.contains(px, py)) {
                        m_OldBubble = bubble;
                        m_arraylistBubble.remove(bubble);
                        m_nScore += 10;
                        ++m_nCombo;
                        m_bComboState = true;
                        m_bComboDraw = true;

                        if(bubble.isBombBubble) {
                            m_bAnimationState[3] = true;
                            vibrator.vibrate(500);
                            m_bAnimationTempState = true;
                            for (int j = m_arraylistBubble.size() - 1; 0 <= j; j--) {
                                Bubble tmpBubble = m_arraylistBubble.get(j);
                                if (bubble.bombContains(tmpBubble.x, tmpBubble.y)) {
                                    m_arraylistBubble.remove(j);
                                    m_nScore += 10;
                                    ++m_nCombo;
                                }
                                m_bAnimationTempState = false;
                            }
                        }else{
                            m_bAnimationState[1] = true;
                        }
                        break;
                    }
                }
                invalidate();
            } else {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    for (int i = m_arraylistBubble.size() - 1; 0 <= i; i--) { // 현재 존재하는 버블 객체수 만큼 반복
                        Bubble bubble = m_arraylistBubble.get(i);

                        if (bubble.contains(px, py)) {
                            m_OldBubble = bubble;
                            m_arraylistBubble.remove(bubble);
                            m_nScore += 10;
                            ++m_nCombo;
                            m_bComboState = true;
                            m_bComboDraw = true;
                            if(bubble.isBombBubble) {
                                m_bAnimationState[3] = true;
                                vibrator.vibrate(500);
                                m_bAnimationTempState = true;
                                for (int j = m_arraylistBubble.size() - 1; 0 <= j; j--) {
                                    Bubble tmpBubble = m_arraylistBubble.get(j);
                                    if (bubble.bombContains(tmpBubble.x, tmpBubble.y)) {
                                        m_arraylistBubble.remove(j);
                                        m_nScore += 10;
                                        ++m_nCombo;
                                    }
                                    m_bAnimationTempState = false;
                                }
                            }else{
                                m_bAnimationState[1] = true;
                            }

                            if(m_nCombo >= 30){
                                Toast.makeText(getApplicationContext(), "Fever Time!!", Toast.LENGTH_SHORT).show();
                                m_ThreadPoolExecutor.execute(new FeverThread(gameStageActivity));
                            }

                            break;
                        } else {
                            m_bComboState = false;
                        }
                    }
                    if (!m_bComboState) m_nCombo = 0;
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
        m_bGameState =false;
        m_dlgGamePauseDialog.show();
    }

    @Override
    protected void onDestroy() {
        System.gc();
        super.onDestroy();
    }
}

