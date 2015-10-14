package com.example.k.alltogether.alltogether;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.k.alltogether.R;

import java.util.ArrayList;

public class GameStageActivity extends Activity {
    GameStageActivity gameStageActivity;
    boolean gameState; // 게임 진행 상태를 담을 변수
    ArrayList<GameView.Bubble> arrayList; // 게임 속에서 생성되는 버블 객체를 가지고 있을 ArrayList
    int screenWidth, screenHeight; // 스마트폰 화면 사이즈를 담을 변수
    GameView.GameThread myThread; // 버블 객체를 생성해 줄 스레드
    GameOverDialog gameOverDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameStageActivity=this;
        gameState=true; // GameStageActivity 가 생성 될 때 게임 진행 상태를 true 로 변경
        arrayList = new ArrayList<GameView.Bubble>(); // arrayList 객체 생성
        screenWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels; // 스마트폰 화면 사이즈 가져오는 함수
        screenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
        gameOverDialog = new GameOverDialog(this, gameStageActivity);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // xml 레이아웃을 자바로 가져와서 객체화 시켜 줄 수 있는 인플레이터 변수 선언
        LinearLayout activity_main = (LinearLayout)inflater.inflate(R.layout.game_stage_activity, null);
        // 인플레이터 변수를 사용해서 xml 레이아웃 객체화

        setContentView(new GameView(this)); // GameStageActivity 에 화면(View)를 담당해줄 GameView 등록

        myThread.start(); // 버블을 생성시켜 줄 스레드 시작

    }

    /*
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View game_stage_activity = (LinearLayout)inflater.inflate(R.layout.game_stage_activity, null);
        layout = (LinearLayout) game_stage_activity.findViewById(R.id.layout_stage);

        LinearLayout layout_main = (LinearLayout) game_stage_activity.findViewById(R.id.layout_main);
    `   */
    // inflater로 xml로 만든 레이아웃을 가져와서 적용시킬순 없을까?

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
        GameStageActivity 에 화면(View)을 담당해 줄 GameView 클래스
    */
    class GameView extends View {
        Bitmap m_BackGroundImage; // GameStageActivity 에 배경화면 변수 선언
        int combo; // 게임 중 몇 콤보수를 담을 변수 선언
        boolean comboState; // 게임 중 콤보상태를 담을 변수 선언
        Paint paint; // 페인트 변수 선언

        public GameView(Context context) {
            super(context);
            m_BackGroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.stage_background); // 배경화면 등록
            m_BackGroundImage = Bitmap.createScaledBitmap(m_BackGroundImage, screenWidth, screenHeight, false); // 스마트폰 화면 사이즈에 맞게 배경화면 조정
            combo=0;
            comboState=false;
            paint = new Paint();
            myThread = new GameThread();
            /*
                첫 번째 버블 생성
            */
            int x = (int) (Math.random() * screenWidth);
            int y = (int) (Math.random() * screenHeight);
            arrayList.add(new Bubble(x, y, 100));
        }

        public GameView(Context context, AttributeSet attrs){
            super(context, attrs);
        }

        /*
            버블 객체를 생성해 줄 스레드
        */
        class GameThread extends Thread {
            Handler handler; // 핸들러 사용 이유 : 메인스레드를 제외하고는 View 를 건드릴 수 없기 때문에 핸들러를 통해서 화면 갱신 => invalidate()
            public GameThread(){
                handler = new Handler();
            }
            public void run(){
                while(gameState) {
                    try {
                        Thread.sleep(500); // 스레드가 0.5초 동안 잠든다 => 0.5초 간격으로 버블 생성
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int x = (int) (Math.random() * screenWidth);
                    int y = (int) (Math.random() * screenHeight);
                    arrayList.add(new Bubble(x, y, 100));

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            invalidate(); // 핸들러를 통해서 화면 갱신
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
            int r;
            Bitmap imgBubble; // 버블 객체에 이미지 변수 선언

            public Bubble(int x, int y, int r){
                this.x = x;
                this.y = y;
                this.r = r;

                imgBubble = BitmapFactory.decodeResource(getResources(), R.drawable.bubble); // 버블 이미지 등록
                imgBubble = Bitmap.createScaledBitmap(imgBubble, r * 2, r * 2, false); // 버블 반지름에 맞게 버블 이미지 조정
            }
            /*
                화면 터치시 터치한 좌표가 버블 안인지 밖인지 판별해주는 함수
            */
            public boolean contains(int x, int y){
                return Math.pow(this.x-x, 2)+Math.pow(this.y-y, 2) <= Math.pow(r, 2);
            }

        }

        @Override
        protected void onDraw(Canvas canvas) {

            canvas.drawBitmap(m_BackGroundImage, 0, 0, paint); // GameStageActivity 배경화면 그리기

            if(arrayList.size()==20) { // 게임 실패시
                gameState = false;
                gameOverDialog.show();
            }

            if(arrayList.size()==0){ // 게임 성공시
                gameState = false;
            }

            if(gameState){
                for(int i=0; i<arrayList.size(); i++) {

                    Bubble bubble = arrayList.get(i);

                    canvas.drawBitmap(bubble.imgBubble, bubble.x - bubble.r, bubble.y-bubble.r, paint);

                }
            }else{
                gameOverDialog.show();
            }

        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int px = (int) event.getX(); // 터치이벤트가 발생 했을때 x좌표값
            int py = (int) event.getY(); // 터치이벤트가 발생 했을때 y좌표값

            if(event.getAction() == MotionEvent.ACTION_DOWN){
                for(int i=arrayList.size()-1; 0<=i; i--){ // 현재 존재하는 버블 객체수 만큼 반복
                    if (arrayList.get(i).contains(px, py)) {
                        arrayList.remove(i);
                        comboState=true;
                        invalidate();
                        break;
                    }else {
                        comboState = false;
                    }
                }
                if(comboState){
                    String tmp = (++combo)+" COMBO!!";
                    Toast.makeText(getApplicationContext(), tmp, Toast.LENGTH_SHORT).show();
                }else{
                    combo=0;
                    String tmp = "콤보 실패!!";
                    Toast.makeText(getApplicationContext(), tmp, Toast.LENGTH_SHORT).show();
                }
            }

            return true;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {// 뒤로가기 누를시

        }
        return false;
    }

}
