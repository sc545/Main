package com.example.k.alltogether.alltogether;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by K on 2015-11-17.
 */
public class RankReadThread extends Thread {
    static final String IP = "172.30.1.11"; // 192.168.51.141
    static int PORT = 1113;
    String name;
    int score;
    Handler handler;
    Context co;
    TextView tvRank1;
    RankReadThread(Context co, TextView tvRank1){
        handler = new Handler();
        this.co = co;
        this.tvRank1 = tvRank1;
    }
    @Override
    public void run() {
        try {
            Socket s = new Socket(IP, PORT);
            InputStream is = s.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);

            OutputStream os = s.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);

            oos.writeObject("read");

            name = (String) ois.readObject();
            score = (int) ois.readObject();

            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                tvRank1.setText(name+", "+score);
                Toast.makeText(co, "저장 완료", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
