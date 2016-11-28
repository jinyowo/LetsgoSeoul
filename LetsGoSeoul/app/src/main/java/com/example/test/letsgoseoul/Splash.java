package com.example.test.letsgoseoul;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
/**
 * Created by jiny on 2016. 11. 28..
 */


public class Splash extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();       // 3 초후 이미지를 닫아버림
            }
        }, 3000);
    }


}