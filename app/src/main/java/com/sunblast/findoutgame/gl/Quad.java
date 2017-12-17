package com.sunblast.findoutgame.gl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;

class Quad extends Shape {

    private int texturenr;

    private Context context;

    public Quad(Context c, Point center, float radius) {
        context = c;
        float side_half = radius / 2;
        Point vertices[] = {
                center.translate(side_half, side_half, 0),
                center.translate(-side_half, side_half, 0),
                center.translate(side_half, -side_half, 0),
                center.translate(-side_half, -side_half, 0)
        };

        drawOrder = new short[]{
                0, 1, 2, 1, 3, 2
        };
        shapeCoords = Point.toFloats(vertices, drawOrder);
    }

    @Override
    public void compileShaders() {
        int vertexShader = ShaderSunblast.loadShader(GLES20.GL_VERTEX_SHADER,
                ShaderSunblast.vs_Image);
        int fragmentShader = ShaderSunblast.loadShader(GLES20.GL_FRAGMENT_SHADER,
                ShaderSunblast.fs_Image);

        // create empty OpenGL ES Program
        ShaderSunblast.sp_Image = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(ShaderSunblast.sp_Image, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(ShaderSunblast.sp_Image, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(ShaderSunblast.sp_Image);
    }

    // Texture variables
    public static float uvs[];
    public FloatBuffer uvBuffer;

    public void setupImage() {
        // Create our UV coordinates.
        uvs = new float[]{
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
        };

        // The texture buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        uvBuffer = bb.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);

        // Generate Textures, if more needed, alter these numbers.
        int[] texturenames = new int[1];
        GLES20.glGenTextures(1, texturenames, 0);

        // Retrieve our image from resources.
        int id = context.getResources().getIdentifier("drawable/psycho", null,
                context.getPackageName());

        // Temporary create a bitmap
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id);

        // Bind texture to texturename
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);

        // Set wrapping mode
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GL_CLAMP_TO_EDGE);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

        // We are done using the bitmap so we should recycle it.
        bmp.recycle();
    }

    @Override
    public void draw(float[] mvpMatrix) {
        int vertexCount = shapeCoords.length / Shape.COORDS_PER_VERTEX;

        // Add program to OpenGL ES environment
        GLES20.glUseProgram(ShaderSunblast.sp_Image);

        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(ShaderSunblast.sp_Image, "vPosition");

        // Enable a handle to the shape vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Get handle to texture coordinates location
        int mTexCoordLoc = GLES20.glGetAttribLocation(ShaderSunblast.sp_Image, "a_texCoord");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mTexCoordLoc);

        // Prepare the texturecoordinates
        GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT,
                false,
                0, uvBuffer);

        // Prepare the shape coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, Shape.COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // Get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation(ShaderSunblast.sp_Image,
                "s_texture");

        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i(mSamplerLoc, 0);

        // get handle to shape's transformation matrix
        int mMVPMatrixHandle = GLES20.glGetUniformLocation(ShaderSunblast.sp_Image, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the shape
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}

