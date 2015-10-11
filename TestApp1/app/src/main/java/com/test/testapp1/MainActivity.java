package com.test.testapp1;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class MainActivity extends Activity {

    //LinearLayout startMain;
    //LinearLayout gameMain;

    ImageButton btnStart;
    ImageButton btnHowTo;
    ImageButton btnSetting;

    static Activity main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new BubbleView(this));
        main = this;
        //setContentView(R.layout.activity_main);

        //startMain = (LinearLayout) findViewById (R.id.startMain);
        //startMain.setBackgroundResource(R.drawable.startmain);
        //main = (LinearLayout) findViewById (R.id.gameMain);
        //gameMain.setBackgroundResource (R.drawable.main);

        btnStart = (ImageButton) findViewById (R.id.btnStart);
        btnHowTo = (ImageButton) findViewById (R.id.btnHowTo);
        btnSetting = (ImageButton) findViewById (R.id.btnSetting);

    }

}