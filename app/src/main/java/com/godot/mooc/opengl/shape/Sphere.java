package com.godot.mooc.opengl.shape;

import android.content.Context;
import android.opengl.GLES20;

import com.godot.mooc.opengl.BoyGLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

/**
 * Created by AllenWang on 2020/11/17.
 */
public class Sphere implements Shape {
    private static final int COORDS_PER_VERTEX = 3;
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;
    private static final int COLOR_SIZE = 3;
    private static final int COLOR_STRIDE = COLOR_SIZE * 4;
    private int program;
    private int vertexLocation;
    private int colorLocation;
    private int mvpMatrixLocation;
    private static final int n = 10;
    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private IntBuffer indicesBuffer;

    private static float[] sVertex;
    private static float[] sColors;
    private static int[] sIndices;
    static {
        createSphere(2, 30, 30);
    }

//    x = r*sin(theta)cos(fy)
//    y = r*sin(theta)sin(fy)
//    z = r*cos(theta)
//    0<=theta<=pi
//    0<=fy<=2*pi
    private static void createSphere(float radius, int nolatitude, int nolongitude) {
        final double diff = Math.PI / nolatitude;
        float vertices[] = new float[65535];
        int pindex[] = new int[65535];
        float pcolor[] = new float[65535];
        int vertexindex = 0;
        int colorindex = 0;
        for (int row = 0; row <= nolatitude; row++) {
            double theta = row * diff;
            double sinTheta = Math.sin(theta);
            double cosTheta = Math.cos(theta);
            float tcolor = -0.5f;
            float tcolorinc = 1f / (float) (nolongitude + 1);
            for (int col = 0; col <= nolongitude; col++) {
                double phi = col * 2 * diff;
                double sinPhi = Math.sin(phi);
                double cosPhi = Math.cos(phi);
                double x = cosPhi * sinTheta;
                double y = cosTheta;
                double z = sinPhi * sinTheta;

                vertices[vertexindex++] = (float) (radius * x);
                vertices[vertexindex++] = (float) (radius * y);
                vertices[vertexindex++] = (float) (radius * z);

                pcolor[colorindex++] = 1f;
                pcolor[colorindex++] = Math.abs(tcolor);
                pcolor[colorindex++] = 0f;
                tcolor += tcolorinc;
            }
        }
        int indx = 0;
        for (int row = 0; row < nolatitude; row++) {
            for (int col = 0; col < nolongitude; col++) {
                // S  *
                // F  *
                int first = (row * (nolongitude + 1)) + col; // F
                int second = first + nolongitude + 1;        // S
                pindex[indx++] = first;
                pindex[indx++] = second;
                pindex[indx++] = first + 1;

                pindex[indx++] = second;
                pindex[indx++] = second + 1;
                pindex[indx++] = first + 1;
            }
        }

        sVertex = Arrays.copyOf(vertices, vertexindex);
        sIndices = Arrays.copyOf(pindex, indx);
        sColors = Arrays.copyOf(pcolor, colorindex);
    }

    public Sphere(Context context) {
        ByteBuffer bb = ByteBuffer.allocateDirect(sVertex.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(sVertex);
        vertexBuffer.position(0);

        bb = ByteBuffer.allocateDirect(sColors.length * 4);
        bb.order(ByteOrder.nativeOrder());
        colorBuffer = bb.asFloatBuffer();
        colorBuffer.put(sColors);
        colorBuffer.position(0);

        indicesBuffer = IntBuffer.allocate(sIndices.length);
        indicesBuffer.put(sIndices);
        indicesBuffer.position(0);

        String vertexSource = BoyGLUtils.readAssetsFile(context, "shader/vertex_sphere.shader");
        String fragmentSource = BoyGLUtils.readAssetsFile(context, "shader/fragment.shader");
        program = BoyGLUtils.createProgram(vertexSource, fragmentSource);
        GLES20.glUseProgram(program);
        vertexLocation = GLES20.glGetAttribLocation(program, "aPosition");
        colorLocation = GLES20.glGetAttribLocation(program, "aColor");
        mvpMatrixLocation = GLES20.glGetUniformLocation(program, "uMVPMatrix");
    }

    @Override
    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(program);
        GLES20.glEnableVertexAttribArray(vertexLocation);
        GLES20.glVertexAttribPointer(vertexLocation, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);

        GLES20.glEnableVertexAttribArray(colorLocation);
        GLES20.glVertexAttribPointer(colorLocation, COLOR_SIZE, GLES20.GL_FLOAT, false, COLOR_STRIDE, colorBuffer);

        GLES20.glUniformMatrix4fv(mvpMatrixLocation, 1, false, mvpMatrix, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, sIndices.length, GLES20.GL_UNSIGNED_INT, indicesBuffer);
    }
}
