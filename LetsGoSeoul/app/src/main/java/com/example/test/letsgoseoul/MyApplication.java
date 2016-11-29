package com.example.test.letsgoseoul;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

/**
 * Created by jiny on 2016. 11. 25..
 */

//폰트 지정
public class MyApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/NanumBarunGothic.ttf"))
                .addBold(Typekit.createFromAsset(this, "fonts/NanumBarunGothicBold.ttf"));



    }
}
