package com.sunblast.findoutgame.gl;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class UserLine
{
    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    static final int COORDS_PER_VERTEX = 3;
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    private short[] pathDrawOrder = {0,1};
    private float[] color = {1.0f, 0.0f, 0.0f, 1.0f};

    public UserLine(Point from, Point to) {
        this(from, to, null);
    }

    public UserLine(Point from, Point to, float[] color)
    {
        float[] pathCords = Point.toFloats(new Point[]{from, to});
        if (color != null) {
            this.color = color;
        }

        ByteBuffer bb = ByteBuffer.allocateDirect(pathCords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(pathCords);
        vertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(pathDrawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(pathDrawOrder);
        drawListBuffer.position(0);
    }

    public static void compileShaders() {
        ShaderSunblast.spLine = GLES20.glCreateProgram();

        int vertexShader = ShaderSunblast.loadShader(GLES20.GL_VERTEX_SHADER,
                ShaderSunblast.lineVertexShaderCode);
        int fragmentShader = ShaderSunblast.loadShader(GLES20.GL_FRAGMENT_SHADER,
                ShaderSunblast.lineFragmentShaderCode);

        GLES20.glAttachShader(ShaderSunblast.spLine, vertexShader);
        GLES20.glAttachShader(ShaderSunblast.spLine, fragmentShader);
        GLES20.glLinkProgram(ShaderSunblast.spLine);
    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(ShaderSunblast.spLine);
        mPositionHandle = GLES20.glGetAttribLocation(ShaderSunblast.spLine, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        mColorHandle = GLES20.glGetUniformLocation(ShaderSunblast.spLine, "vColor");
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        mMVPMatrixHandle = GLES20.glGetUniformLocation(ShaderSunblast.spLine, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glDrawElements(GLES20.GL_LINES, pathDrawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisable(mColorHandle);
    }
}