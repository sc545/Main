package com.example.k.alltogether.alltogether;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.k.alltogether.R;

import java.util.ArrayList;

public class MainActivity extends Activity {
    static Activity main;
    static boolean gameState=false;
    static ArrayList<GameView.Bubble> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arrayList = new ArrayList<GameView.Bubble>();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout activity_main = (LinearLayout)inflater.inflate(R.layout.activity_main, null);

        setContentView(new GameView(this));
//        setContentView(activity_main);
//        setContentView(R.layout.activity_main);
        main = this;
    }

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

}
