package com.example.k.alltogether.alltogether;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.k.alltogether.R;

/**
 * Created by K on 2015-10-14.
 */

public class MainActivity extends Activity {
    ImageButton btnHowTo, btnSetting, btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        btnHowTo = (ImageButton) findViewById(R.id.btnHowTo);
        btnSetting = (ImageButton) findViewById(R.id.btnSetting);
        btnStart = (ImageButton) findViewById(R.id.btnStart);

        btnHowTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), GameStageActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
