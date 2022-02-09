package com.example.witsmarketplace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.witsmarketplace.LandingPage.LandingPage;
import com.example.witsmarketplace.Login.LoginActivity;

public class SplashScreen extends AppCompatActivity
{
    private SharedPreference sharedPreference;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mContext = this;

        sharedPreference = new SharedPreference(this);
        String email = sharedPreference.getSH("email");

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent;
                if(email != null){
                    intent = new Intent(mContext, LandingPage.class);
                }
                else {
                    intent = new Intent(mContext, LoginActivity.class);
                }
                mContext.startActivity(intent);
            }
        }, 4000);
    }
}