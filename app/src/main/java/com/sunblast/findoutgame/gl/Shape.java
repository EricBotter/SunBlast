package com.sunblast.findoutgame.gl;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

abstract public class Shape {

    protected static final int COORDS_PER_VERTEX = 3;
    protected final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    protected FloatBuffer vertexBuffer;
    protected ShortBuffer drawListBuffer;

    protected float shapeCoords[] = null;
    protected short drawOrder[] = null;

    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    private float[] color = new float[]{0, 0, 1, 1};

    public void prepareBuffers() {
        ByteBuffer bb = ByteBuffer.allocateDirect(shapeCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(shapeCoords);
        vertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
    }

    public void compileShaders() {
        int vertexShader = ShaderSunblast.loadShader(GLES20.GL_VERTEX_SHADER,
                ShaderSunblast.vertexShaderCode);
        int fragmentShader = ShaderSunblast.loadShader(GLES20.GL_FRAGMENT_SHADER,
                ShaderSunblast.fragmentShaderCode);

        // create empty OpenGL ES Program
        ShaderSunblast.spShape = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(ShaderSunblast.spShape, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(ShaderSunblast.spShape, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(ShaderSunblast.spShape);
    }

    public void draw(float[] mvpMatrix) {
        int vertexCount = shapeCoords.length / COORDS_PER_VERTEX;

        // Add program to OpenGL ES environment
        GLES20.glUseProgram(ShaderSunblast.spShape);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(ShaderSunblast.spShape, "vPosition");

        // Enable a handle to the shape vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the shape coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(ShaderSunblast.spShape, "vColor");

        // Set color for drawing the shapes
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(ShaderSunblast.spShape, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the shape
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public float[] getColor() {
        return color;
    }

    public void setColor(float[] color) {
        this.color = color;
    }
}
