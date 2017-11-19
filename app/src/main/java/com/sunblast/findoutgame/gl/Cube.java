package com.sunblast.findoutgame.gl;

class Cube extends Shape {

    private final short[] facesDrawOrder = {
            0, 1, 2, // right face
            1, 3, 2,
            1, 5, 3, // front face
            5, 7, 3,
            5, 4, 7, // left face
            4, 6, 7,
            4, 0, 6, // rear face
            0, 2, 6,
            7, 2, 3, // bottom face
            7, 6, 2,
            4, 5, 1, // top face
            4, 1, 0
    };

    public Cube(Point center, float side) {
        float side_half = side / 2;
        Point vertices[] = {
                center.translate(side_half, side_half, side_half),
                center.translate(side_half, side_half, -side_half),
                center.translate(side_half, -side_half, side_half),
                center.translate(side_half, -side_half, -side_half),
                center.translate(-side_half, side_half, side_half),
                center.translate(-side_half, side_half, -side_half),
                center.translate(-side_half, -side_half, side_half),
                center.translate(-side_half, -side_half, -side_half)
        };

        drawOrder = facesDrawOrder;
        shapeCoords = Point.toFloats(vertices, drawOrder);
    }
}
