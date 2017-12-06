package com.sunblast.findoutgame.gl;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.sunblast.findoutgame.GameLogic;

public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

//        Set the renderer for this view
        mRenderer = new MyGLRenderer(context);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);

        GameLogic game = new GameLogic(context, mRenderer);
        game.startTimer(10);
    }
}
