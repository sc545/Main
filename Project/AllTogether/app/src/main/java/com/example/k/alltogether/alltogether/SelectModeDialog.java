package com.example.k.alltogether.alltogether;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.k.alltogether.R;

/**
 * Created by K on 2015-11-19.
 */
public class SelectModeDialog extends Dialog {
    MainActivity mainActivity;
    Button btnMode1, btnMode2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.select_mode_dailog);

        btnMode1 = (Button) findViewById(R.id.btn1);
        btnMode2 = (Button) findViewById(R.id.btn2);

        btnMode1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mainActivity.getApplicationContext(), GameStageActivity.class);
                mainActivity.startActivity(i);
                mainActivity.finish();
            }
        });

        btnMode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    SelectModeDialog(Context context, MainActivity mainActivity){
        super(context);
        this.mainActivity = mainActivity;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {// 뒤로가기 누를시
            dismiss();
        }
        return false;
    }
}
