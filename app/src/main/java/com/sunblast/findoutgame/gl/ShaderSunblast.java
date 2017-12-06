package com.sunblast.findoutgame.gl;


import android.opengl.GLES20;

public class ShaderSunblast {

    // Program variables
    public static int spShape;
    public static int sp_Image;
    public static int spText;

    public static int loadShader(int type, String shaderCode) {
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }


    /* SHADER shape
     *
     * This shader is for rendering a colored primitive.
     *
     */
    public static final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    public static final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor ;" +
                    "}";

    /* SHADER Text
     *
     * This shader is for rendering 2D text textures straight from a texture
     * Color and alpha blended.
     *
     */
    public static final String vs_Text =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec4 a_Color;" +
                    "attribute vec2 a_texCoord;" +
                    "varying vec4 v_Color;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    "   gl_Position = " +
                    "       vec4(vPosition.x * 2.0 / 1920.0 - 1.0,\n" +
                    "                     vPosition.y  * -2.0 / 1080.0 + 1.0,\n" +
                    "                     vPosition.z, \n" +
                    "                     1.0);"+
                    "  v_texCoord = a_texCoord;" +
                    "  v_Color = a_Color;" +
                    "}";
    public static final String fs_Text =
            "precision mediump float;" +
                    "varying vec4 v_Color;" +
                    "varying vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    "vec2 flipped_texcoord = vec2(v_texCoord.x,  v_texCoord.y);"+
                    "  gl_FragColor = texture2D( s_texture, flipped_texcoord ) * v_Color;" +
                    "  gl_FragColor.rgb *= v_Color.a;" +
                    "}";

    /* SHADER Sphere
     *
     * This shader is for rendering Spheres. Eventually
     *
     */
    public static final String sphereVertexShaderCode = "attribute vec4 position;\n" +
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

    public static final String sphereFragmentShaderCode = "precision mediump float;\n" +
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

    /* SHADER Lines
     *
     * Even lines need shaders in OpenGL ES. Seriously.
     *
     */
    public static final String lineVertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    public static final String lineFragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";


}
