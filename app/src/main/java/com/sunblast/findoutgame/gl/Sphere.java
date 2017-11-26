package com.sunblast.findoutgame.gl;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

class Sphere extends Shape {
    public Sphere(Point center, float radius) {
        vertexShaderCode = "attribute vec4 position;\n" +
                "attribute vec4 inputImpostorSpaceCoordinate;\n" +
                "\n" +
                "varying mediump vec2 impostorSpaceCoordinate;\n" +
                "varying mediump vec3 normalizedViewCoordinate;\n" +
                "\n" +
                "uniform mat4 modelViewProjMatrix;\n" +
                "uniform mediump mat4 orthographicMatrix;\n" +
                "uniform mediump float sphereRadius;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    vec4 transformedPosition;\n" +
                "    transformedPosition = modelViewProjMatrix * position;\n" +
                "    impostorSpaceCoordinate = inputImpostorSpaceCoordinate.xy;\n" +
                "\n" +
                "    transformedPosition.xy = transformedPosition.xy + inputImpostorSpaceCoordinate.xy * vec2(sphereRadius);\n" +
                "    transformedPosition = transformedPosition * orthographicMatrix;\n" +
                "\n" +
                "    normalizedViewCoordinate = (transformedPosition.xyz + 1.0) / 2.0;\n" +
                "    gl_Position = transformedPosition;\n" +
                "}";

        fragmentShaderCode = "precision mediump float;\n" +
                "\n" +
                "uniform vec3 lightPosition;\n" +
                "uniform vec3 sphereColor;\n" +
                "uniform mediump float sphereRadius;\n" +
                "\n" +
                "uniform sampler2D depthTexture;\n" +
                "\n" +
                "varying mediump vec2 impostorSpaceCoordinate;\n" +
                "varying mediump vec3 normalizedViewCoordinate;\n" +
                "\n" +
                "const mediump vec3 oneVector = vec3(1.0, 1.0, 1.0);\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    float distanceFromCenter = length(impostorSpaceCoordinate);\n" +
                "\n" +
                "    // Establish the visual bounds of the sphere\n" +
                "    if (distanceFromCenter > 1.0)\n" +
                "    {\n" +
                "        discard;\n" +
                "    }\n" +
                "\n" +
                "    float normalizedDepth = sqrt(1.0 - distanceFromCenter * distanceFromCenter);\n" +
                "\n" +
                "    // Current depth\n" +
                "    float depthOfFragment = sphereRadius * 0.5 * normalizedDepth;\n" +
                "    //        float currentDepthValue = normalizedViewCoordinate.z - depthOfFragment - 0.0025;\n" +
                "    float currentDepthValue = (normalizedViewCoordinate.z - depthOfFragment - 0.0025);\n" +
                "\n" +
                "    // Calculate the lighting normal for the sphere\n" +
                "    vec3 normal = vec3(impostorSpaceCoordinate, normalizedDepth);\n" +
                "\n" +
                "    vec3 finalSphereColor = sphereColor;\n" +
                "\n" +
                "    // ambient\n" +
                "    float lightingIntensity = 0.3 + 0.7 * clamp(dot(lightPosition, normal), 0.0, 1.0);\n" +
                "    finalSphereColor *= lightingIntensity;\n" +
                "\n" +
                "    // Per fragment specular lighting\n" +
                "    lightingIntensity  = clamp(dot(lightPosition, normal), 0.0, 1.0);\n" +
                "    lightingIntensity  = pow(lightingIntensity, 60.0);\n" +
                "    finalSphereColor += vec3(0.4, 0.4, 0.4) * lightingIntensity;\n" +
                "\n" +
                "    gl_FragColor = vec4(finalSphereColor, 1.0);" +
                "}";

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

