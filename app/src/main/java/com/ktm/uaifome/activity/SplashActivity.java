package com.ktm.uaifome.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ktm.uaifome.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        //definindo tempo de exibição
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                abrirAuthentication();
            }
        }, 2000);
    }

    private void abrirAuthentication(){
        startActivity(new Intent(getApplicationContext(), AuthenticationActivity.class));
        finish();
    }
}
