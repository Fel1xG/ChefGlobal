package com.example.chefglobal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Confirmacion1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacion1);
    }

    public void inicio2(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}