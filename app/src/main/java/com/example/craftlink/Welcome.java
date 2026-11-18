package com.example.craftlink;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        // declare elements

        // إنشاء حساب -> Register 1
        findViewById(R.id.btnCreateAccount).setOnClickListener(v ->
                startActivity(new Intent(this, Register1.class)));

        // تسجيل الدخول -> Login
        findViewById(R.id.tvGoLogin).setOnClickListener(v ->
                startActivity(new Intent(this, Login.class)));


    }
}




