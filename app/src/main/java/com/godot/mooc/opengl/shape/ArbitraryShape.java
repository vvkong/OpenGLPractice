package com.godot.mooc.opengl.shape;

import android.content.Context;
import android.opengl.GLES20;

import com.godot.mooc.opengl.BoyGLUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

/**
 * Created by AllenWang on 2020/11/26.
 */
public class ArbitraryShape implements Shape {
    private static final int COORDS_PER_VERTEX = 3;
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;
    private static final int COLOR_SIZE = 3;
    private static final int COLOR_STRIDE = COLOR_SIZE * 4;

    private static boolean hasCreateVertex;
    private static float[] sSphereVertex;
    private static int[] sSphereIndex;
    private static float[] sSphereColor;
    private static float[] sSphere2Vertex;
    private static int[] sSphere2Index;
    private static float[] sSphere2Color;
    private static float[] sRingVertex;
    private static float[] sRingColor;
    private static int[] sRingIndex;

    private final int vertexLocation;
    private final int colorLocation;
    private final int mvpMatrixLocation;
    private int program;

    private FloatBuffer vertexBuffer1 = null;
    private FloatBuffer colorBuffer1 = null;
    private IntBuffer indicesBuffer1 = null;

    private FloatBuffer vertexBuffer2 = null;
    private FloatBuffer colorBuffer2 = null;
    private IntBuffer indicesBuffer2 = null;

    private FloatBuffer ringVertexBuffer = null;
    private FloatBuffer ringColorBuffer = null;
    private IntBuffer ringIndicesBuffer = null;

    public ArbitraryShape(Context context) {
        String vertexSource = BoyGLUtils.readAssetsFile(context, "shader/vertex_sphere.shader");
        String fragmentSource = BoyGLUtils.readAssetsFile(context, "shader/fragment.shader");
        program = BoyGLUtils.createProgram(vertexSource, fragmentSource);
        GLES20.glUseProgram(program);
        vertexLocation = GLES20.glGetAttribLocation(program, "aPosition");
        colorLocation = GLES20.glGetAttribLocation(program, "aColor");
        mvpMatrixLocation = GLES20.glGetUniformLocation(program, "uMVPMatrix");

        if( !hasCreateVertex ) {
            createArbitraryShape(2, 30, 30);
            hasCreateVertex = true;
        }
        vertexBuffer1 = BoyGLUtils.createFloatBuffer(sSphereVertex);
        indicesBuffer1 = BoyGLUtils.createIntBuffer(sSphereIndex);
        colorBuffer1 = BoyGLUtils.createFloatBuffer(sSphereColor);

        vertexBuffer2 = BoyGLUtils.createFloatBuffer(sSphere2Vertex);
        indicesBuffer2 = BoyGLUtils.createIntBuffer(sSphere2Index);
        colorBuffer2 = BoyGLUtils.createFloatBuffer(sSphere2Color);

        ringVertexBuffer = BoyGLUtils.createFloatBuffer(sRingVertex);
        ringIndicesBuffer = BoyGLUtils.createIntBuffer(sRingIndex);
        ringColorBuffer = BoyGLUtils.createFloatBuffer(sRingColor);
    }

    private void createArbitraryShape(double radius, int nolongitude, int nolatitude) {
        float vertices[] = new float[65535];
        int index[] = new int[65535];
        float color[] = new float[65535];
        int pnormlen = (nolongitude + 1) * 3 * 3;
        int vertexindex = 0;
        int colorindex = 0;
        int indx = 0;
        float vertices2[] = new float[65535];
        int index2[] = new int[65535];
        float color2[] = new float[65525];
        int vertex2index = 0;
        int color2index = 0;
        int indx2 = 0;
        float ring_vertices[] = new float[65535];
        int ring_index[] = new int[65535];
        float ring_color[] = new float[65525];
        int rvindx = 0;
        int rcindex = 0;
        int rindx = 0;
        float dist = 3;
        int plen = (nolongitude + 1) * 3 * 3;
        int pcolorlen = (nolongitude + 1) * 4 * 3;
        final double diff = Math.PI / nolatitude;
        for (int row = 0; row < nolatitude + 1; row++) {
            double theta = row * diff;
            double sinTheta = Math.sin(theta);
            double cosTheta = Math.cos(theta);
            float tcolor = -0.5f;
            float tcolorinc = 1 / (float) (nolongitude + 1);
            for (int col = 0; col < nolongitude + 1; col++) {
                double phi = col * 2 * diff;
                double sinPhi = Math.sin(phi);
                double cosPhi = Math.cos(phi);
                double x = cosPhi * sinTheta;
                double y = cosTheta;
                double z = sinPhi * sinTheta;
                vertices[vertexindex++] = (float) (radius * x);
                vertices[vertexindex++] = (float) (radius * y) + dist;
                vertices[vertexindex++] = (float) (radius * z);

                vertices2[vertex2index++] = (float) (radius * x);
                vertices2[vertex2index++] = (float) (radius * y) - dist;
                vertices2[vertex2index++] = (float) (radius * z);

                color[colorindex++] = 1;
                color[colorindex++] = Math.abs(tcolor);
                color[colorindex++] = 0;

                color2[color2index++] = 0;
                color2[color2index++] = 1;
                color2[color2index++] = Math.abs(tcolor);

                if (row == 20) {
                    ring_vertices[rvindx++] = (float) (radius * x);
                    ring_vertices[rvindx++] = (float) (radius * y) + dist;
                    ring_vertices[rvindx++] = (float) (radius * z);
                    ring_color[rcindex++] = 1;
                    ring_color[rcindex++] = Math.abs(tcolor);
                    ring_color[rcindex++] = 0;
                }
                if (row == 15) {
                    ring_vertices[rvindx++] = (float) (radius * x) / 2;
                    ring_vertices[rvindx++] = (float) (radius * y) / 2 + 0.2f * dist;
                    ring_vertices[rvindx++] = (float) (radius * z) / 2;
                    ring_color[rcindex++] = 1;
                    ring_color[rcindex++] = Math.abs(tcolor);
                    ring_color[rcindex++] = 0;
                }
                if (row == 10) {
                    ring_vertices[rvindx++] = (float) (radius * x) / 2;
                    ring_vertices[rvindx++] = (float) (radius * y) / 2 - 0.1f * dist;
                    ring_vertices[rvindx++] = (float) (radius * z) / 2;
                    ring_color[rcindex++] = 0;
                    ring_color[rcindex++] = 1;
                    ring_color[rcindex++] = Math.abs(tcolor);
                }
                if (row == 20) {
                    ring_vertices[plen++] = (float) (radius * x);
                    ring_vertices[plen++] = (float) (-radius * y) - dist;
                    ring_vertices[plen++] = (float) (radius * z);
                    ring_color[pcolorlen++] = 0;
                    ring_color[pcolorlen++] = 1;
                    ring_color[pcolorlen++] = Math.abs(tcolor);
                    //-------
                }
                tcolor += tcolorinc;
            }
        }
        //index buffer
        for (int row = 0; row < nolatitude; row++) {
            for (int col = 0; col < nolongitude; col++) {
                int P0 = (row * (nolongitude + 1)) + col;
                int P1 = P0 + nolongitude + 1;
                index[indx++] = P1;
                index[indx++] = P0;
                index[indx++] = P0 + 1;
                index[indx++] = P1 + 1;
                index[indx++] = P1;
                index[indx++] = P0 + 1;

                index2[indx2++] = P1;
                index2[indx2++] = P0;
                index2[indx2++] = P0 + 1;
                index2[indx2++] = P1 + 1;
                index2[indx2++] = P1;
                index2[indx2++] = P0 + 1;
            }
        }
        rvindx = (nolongitude + 1) * 3 * 4;
        rcindex = (nolongitude + 1) * 4 * 4;
        plen = nolongitude + 1;
        for (int j = 0; j < plen - 1; j++) {
            ring_index[rindx++] = j;
            ring_index[rindx++] = j + plen;
            ring_index[rindx++] = j + 1;
            ring_index[rindx++] = j + plen + 1;
            ring_index[rindx++] = j + 1;
            ring_index[rindx++] = j + plen;

            ring_index[rindx++] = j + plen;
            ring_index[rindx++] = j + plen * 2;
            ring_index[rindx++] = j + plen + 1;
            ring_index[rindx++] = j + plen * 2 + 1;
            ring_index[rindx++] = j + plen + 1;
            ring_index[rindx++] = j + plen * 2;

            ring_index[rindx++] = j + plen * 3;
            ring_index[rindx++] = j;
            ring_index[rindx++] = j + 1;
            ring_index[rindx++] = j + 1;
            ring_index[rindx++] = j + plen * 3 + 1;
            ring_index[rindx++] = j + plen * 3;
        }

        //set the buffers
        sSphereVertex = Arrays.copyOf(vertices, vertexindex);
        sSphereIndex = Arrays.copyOf(index, indx);
        sSphereColor = Arrays.copyOf(color, colorindex);
        sSphere2Vertex = Arrays.copyOf(vertices2, vertex2index);
        sSphere2Index = Arrays.copyOf(index2, indx2);
        sSphere2Color = Arrays.copyOf(color2, color2index);
        sRingVertex = Arrays.copyOf(ring_vertices, rvindx);
        sRingColor = Arrays.copyOf(ring_color, rcindex);
        sRingIndex = Arrays.copyOf(ring_index, rindx);
    }

    @Override
    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(program);
        GLES20.glUniformMatrix4fv(mvpMatrixLocation, 1, false, mvpMatrix, 0);

        GLES20.glEnableVertexAttribArray(vertexLocation);
        GLES20.glVertexAttribPointer(vertexLocation, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer1);
        GLES20.glEnableVertexAttribArray(colorLocation);
        GLES20.glVertexAttribPointer(colorLocation, COLOR_SIZE, GLES20.GL_FLOAT, false, COLOR_STRIDE, colorBuffer1);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, sSphereIndex.length, GLES20.GL_UNSIGNED_INT, indicesBuffer1);

        GLES20.glEnableVertexAttribArray(vertexLocation);
        GLES20.glVertexAttribPointer(vertexLocation, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer2);
        GLES20.glEnableVertexAttribArray(colorLocation);
        GLES20.glVertexAttribPointer(colorLocation, COLOR_SIZE, GLES20.GL_FLOAT, false, COLOR_STRIDE, colorBuffer2);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, sSphere2Index.length, GLES20.GL_UNSIGNED_INT, indicesBuffer2);

        GLES20.glEnableVertexAttribArray(vertexLocation);
        GLES20.glVertexAttribPointer(vertexLocation, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, ringVertexBuffer);
        GLES20.glEnableVertexAttribArray(colorLocation);
        GLES20.glVertexAttribPointer(colorLocation, COLOR_SIZE, GLES20.GL_FLOAT, false, COLOR_STRIDE, ringColorBuffer);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, sRingIndex.length, GLES20.GL_UNSIGNED_INT, ringIndicesBuffer);

    }
}
