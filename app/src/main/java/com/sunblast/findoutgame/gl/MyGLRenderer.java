package com.sunblast.findoutgame.gl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class MyGLRenderer implements GLSurfaceView.Renderer {

    public ArrayList<Shape> shapes = new ArrayList<>();

    public static int loadShader(int type, String shaderCode) {
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    @Override
    public void onSurfaceCreated(GL10 _, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        Shape s = new Cube(new Point(0, 0, -3), 1);
        s.prepareBuffers();
        s.compileShaders();

        shapes.add(s);
    }

    @Override
    public void onSurfaceChanged(GL10 _, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 _) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        for (Shape shape: shapes) {
            shape.draw();
        }
    }
}
