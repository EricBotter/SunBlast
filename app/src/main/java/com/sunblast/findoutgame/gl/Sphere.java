package com.sunblast.findoutgame.gl;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

class Sphere extends Shape {
    public Sphere(Point center, float radius) {

        float side_half = radius / 2;
        Point vertices[] = {
                center.translate(side_half, side_half, 0),
                center.translate(-side_half, side_half, 0),
                center.translate(side_half, -side_half, 0),
                center.translate(-side_half, -side_half, 0)
        };

        drawOrder = new short[] {
            0, 1, 2, 1, 3, 2
        };
        shapeCoords = Point.toFloats(vertices, drawOrder);
    }

    @Override
    public void draw(float[] mvpMatrix) {

    }
}

