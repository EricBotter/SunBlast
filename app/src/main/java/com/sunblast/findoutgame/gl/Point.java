package com.sunblast.findoutgame.gl;

public class Point {
    public float x, y, z;

    public Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point translate(float x, float y, float z) {
        return new Point(this.x + x, this.y + y, this.z + z);
    }

    public static float[] toFloats(Point[] points) {
        float[] vertices = new float[points.length * 3];
        for (int i = 0; i < points.length; i++) {
            vertices[i * 3] = points[i].x;
            vertices[i * 3 + 1] = points[i].y;
            vertices[i * 3 + 2] = points[i].z;
        }
        return vertices;
    }

    public static float[] toFloats(Point[] points, short[] drawOrder) {
        float[] vertices = new float[drawOrder.length * 3];
        for (int i = 0; i < drawOrder.length; i++) {
            vertices[i * 3] = points[drawOrder[i]].x;
            vertices[i * 3 + 1] = points[drawOrder[i]].y;
            vertices[i * 3 + 2] = points[drawOrder[i]].z;
        }
        return vertices;
    }
}
