package com.example.test.letsgoseoul;
import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by jiny on 2016. 11. 28..
 *
 * 안드로이드 시작화면
 */
public class Splash extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Handler hd = new Handler(){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                startActivity(new Intent(Splash.this, MainActivity.class));
                finish();
            }
            };
        hd.sendEmptyMessageDelayed(0,2000);
                    }
    }
