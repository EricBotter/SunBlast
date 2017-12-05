package com.sunblast.findoutgame.gl;


import android.opengl.Matrix;

public class TextObject {
    public String text;
    public float x;
    public float y;
    public float z;
    public float[] color;

    public TextObject()
    {
        text = "default";
        x = 0f;
        y = 0f;
        z = 0f;
        color = new float[] {1f, 1f, 1f, 1.0f};
    }

    public TextObject(String txt, float xcoord, float ycoord, float zcoord)
    {
        text = txt;
        x = xcoord;
        y = ycoord;
        z = zcoord;
        color = new float[] {1f, 1f, 1f, 1.0f};
    }
    public void rotateText(float[] rotation){
        float[] newCoords = new float[16];
        float[] tmp = {
                x, 0, 0, 0,
                y, 0, 0, 0,
                z, 0, 0, 0,
                0, 0, 0, 0
        };
        Matrix.multiplyMM(newCoords, 0, tmp, 0, rotation, 0);
        x = newCoords[0];
        y = newCoords[4];
        z = newCoords[8];
    }
}
