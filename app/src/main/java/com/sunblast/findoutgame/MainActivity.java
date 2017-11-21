package com.sunblast.findoutgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sunblast.findoutgame.sensors.SensorWrapper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SensorWrapper.getSingletonInstance().init(this);
    }

    public void startGameButtonClick(View view) {
        Intent myIntent = new Intent(MainActivity.this, GameActivity.class);
        startActivity(myIntent);
    }
}
