package com.c1ph3r.zomatocloneuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    private static final int LOADING_TIME = 6000;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            intent = new Intent(this, MainActivity.class);
        else
            intent = new Intent(this, Dashboard.class);

        new Handler().postDelayed(() -> {
            startActivity(intent);
            finish();
        }, LOADING_TIME);
    }
}