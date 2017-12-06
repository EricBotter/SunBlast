package com.sunblast.findoutgame.gl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.sunblast.findoutgame.sensors.SensorWrapper;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class MyGLRenderer implements GLSurfaceView.Renderer {

//    Array with all shapes to be rendered
    public ArrayList<Shape> shapes = new ArrayList<>();

//    Text utilities
    public ArrayList<TextObject> text = new ArrayList<>();
    TextManager tm;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

//    Android context
    Context context;

//    Screen scaling
    float 	ssu = 1.0f;
    float 	ssx = 1.0f;
    float 	ssy = 1.0f;
    float 	swp = 320.0f;
    float 	shp = 480.0f;

    public MyGLRenderer(Context c) {
        context = c;
    }

    public void setupScaling()
    {
        // The screen resolutions
        swp = (int) (context.getResources().getDisplayMetrics().widthPixels);
        shp = (int) (context.getResources().getDisplayMetrics().heightPixels);

        // Orientation is assumed landscape
        ssx = swp / 480.0f;
        ssy = shp / 320.0f;

        // Get our uniform scaler
        if(ssx > ssy)
            ssu = ssy;
        else
            ssu = ssx;
    }

    @Override
    public void onSurfaceCreated(GL10 _, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        setupScaling();

//        SHAPES
        Shape s = new Cube(new Point(0, 0, -10), 1);
        s.prepareBuffers();
        s.compileShaders();

        shapes.add(s);


        //        TEXT
        // Create our text manager
        tm = new TextManager(context);
        tm.compileShaders();

        // Tell our text manager to use index 1 of textures loaded
        tm.setTextureID(1);

        // Pass the uniform scale
        tm.setUniformscale(ssu);

        // Create our new textobject
        TextObject txt = new TextObject("henlo", 100f, 100f, -1f);
        text.add(txt);
    }

    @Override
    public void onSurfaceChanged(GL10 _, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
//        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 1, 1000);
        Matrix.perspectiveM(mProjectionMatrix, 0, 45.0f, ratio, 1, 1000);
        setupScaling();
    }

    private float[] mRotationMatrix;

    @Override
    public void onDrawFrame(GL10 _) {
        float[] scratch = new float[16];

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 0f, 0f, 0f, -1f, 1.0f, 0.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // Create a rotation transformation for the Shape
        mRotationMatrix = SensorWrapper.getSingletonInstance().getRotationMatrix();
        if (mRotationMatrix != null) {
            // Combine the rotation matrix with the projection and camera view
            // Note that the mMVPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
        }

        for (Shape shape: shapes) {
            shape.draw(scratch);
        }

//        draw text
        for (TextObject txt: text){
//            if (mRotationMatrix != null) {
//                txt.rotateText(mRotationMatrix);
//            }
            tm.resetText();
            // Add text to our manager
            tm.addText(txt);

            // Prepare the text for rendering
            tm.PrepareDraw();
//            render text
            tm.Draw(mMVPMatrix);
        }
    }
}
