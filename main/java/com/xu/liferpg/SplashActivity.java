package com.xu.liferpg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SplashActivity extends AppCompatActivity {
    Button buttoneng;
    Button buttonmalay;
    Button buttonhind;
    Button buttonchn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_splash);


        buttonchn=(Button)findViewById(R.id.buttonchn);
        buttoneng=(Button)findViewById(R.id.buttoneng);
        buttonhind=(Button)findViewById(R.id.buttonhind);
        buttonmalay=(Button)findViewById(R.id.buttonmalay);
        buttonchn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, MapActivity.class));
                SplashActivity.this.finish();
                SplashActivity.this.overridePendingTransition(0, R.anim.custom_fadeout);
            }
        });
        buttonhind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, MapActivity.class));
                SplashActivity.this.finish();
                SplashActivity.this.overridePendingTransition(0, R.anim.custom_fadeout);
            }
        });
        buttonmalay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, MapActivity.class));
                SplashActivity.this.finish();
                SplashActivity.this.overridePendingTransition(0, R.anim.custom_fadeout);
            }
        });
        buttoneng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, MapActivity.class));
                SplashActivity.this.finish();
                SplashActivity.this.overridePendingTransition(0, R.anim.custom_fadeout);
            }
        });
    }
}
