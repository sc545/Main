package com.example.k.alltogether.alltogether;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.k.alltogether.R;

/**
 * Created by 5-3COM- on 2015-11-18.
 */
public class RankDialog extends Dialog {
    Context co;
    ImageButton btnExit;
    TextView tvRank1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.rank_dialog);

        btnExit = (ImageButton) findViewById(R.id.btnExit3);
        tvRank1 = (TextView) findViewById(R.id.tvRank1);

        RankReadThread rankReadThread = new RankReadThread(co, tvRank1);
        rankReadThread.start();


        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    RankDialog(Context context){
        super(context);
        this.co = context;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {// 뒤로가기 누를시
            dismiss();
        }
        return false;
    }
}
