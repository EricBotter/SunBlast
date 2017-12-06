package com.sunblast.findoutgame;


import android.content.Context;
import android.os.CountDownTimer;
import com.sunblast.findoutgame.gl.*;

public class GameLogic {

    private MyGLRenderer renderer;
    private int timeLeft;

    private CountDownTimer timer;

    private Context context;

    public GameLogic(Context context, MyGLRenderer renderer){
        this.context = context;
        this.renderer = renderer;
    }

    public void startTimer(int seconds){
        if(timer != null){
            timer.cancel();
            timer = null;
        }
        timer = new CountDownTimer(seconds*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                if(renderer.timeAdded && renderer.addTime != 0){
                    startTimer(timeLeft + renderer.addTime);
                    renderer.timeAdded = false;
                }
                timeLeft = (int) millisUntilFinished / 1000;
                renderer.setTime(timeLeft);
            }

            public void onFinish() {
                if(renderer.timeAdded && renderer.addTime != 0){
                    startTimer(timeLeft + renderer.addTime);
                    renderer.timeAdded = false;
                }
                else{
                    renderer.setTime(timeLeft);
                }
            }
        }.start();

    }

}
