package com.example.loader.realization;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loader.R;
import com.example.loader.primary.Start;

public class Splash extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(() -> {
            TextView view=findViewById(R.id.tagline);
            Animation m= AnimationUtils.loadAnimation(this,R.anim.emergence);
            view.startAnimation(m);
        },1000);

        new Handler().postDelayed(() -> {
            Intent intent=new Intent(this, Start.class);
            startActivity(intent);
            finish();
        },2000);
    }
}
