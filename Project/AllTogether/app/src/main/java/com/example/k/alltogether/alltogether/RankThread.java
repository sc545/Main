package com.example.k.alltogether.alltogether;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by K on 2015-11-17.
 */
public class RankThread extends Thread {
    static final String IP = "192.168.51.137"; // 192.168.51.141
    static int PORT = 1113;
    String name;
    int score;
    Handler handler;
    Context co;
    RankThread(Context co, String name, int score){
        handler = new Handler();
        this.co = co;
        this.name = name;
        this.score = score;
    }
    @Override
    public void run() {
        try {
            Socket s = new Socket(IP, PORT);
            InputStream is = s.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);

            OutputStream os = s.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);

            oos.writeObject("write");

            oos.writeObject(name);
            oos.writeObject(score);

            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(co, "등록 완료", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
