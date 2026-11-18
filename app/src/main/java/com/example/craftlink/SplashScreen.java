package com.example.craftlink;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable goNext = () -> {
        boolean loggedIn = FirebaseAuth.getInstance().getCurrentUser() != null;
        Intent intent = new Intent(this, loggedIn ?
                MainActivity.class : Welcome.class);
        startActivity(intent);
        finish(); // ما يرجع للسبلاش بزر الرجوع
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        handler.postDelayed(goNext, 2500);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(goNext);
        super.onDestroy();
    }
}
