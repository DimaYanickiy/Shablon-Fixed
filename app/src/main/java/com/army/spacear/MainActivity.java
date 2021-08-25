package com.army.spacear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.army.spacear.utils.MainThread;
import com.army.spacear.utils.PrefsSaver;

public class MainActivity extends AppCompatActivity {

    MainThread main;
    PrefsSaver prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = new PrefsSaver(this);

        main = new MainThread(this);
        main.start();

        waitor();
    }

    public void waitor(){
        Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                if(prefs.getGo() == 1){
                    startActivity(new Intent(MainActivity.this, Web.class));
                    finish();
                }
                else if(prefs.getGo() == 2){
                    startActivity(new Intent(MainActivity.this, Zaglushka.class));
                    finish();
                } else {
                    h.postDelayed(this, 2000);
                }
            }
        });
    }
}