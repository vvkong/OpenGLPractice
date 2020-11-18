package com.godot.mooc.opengl.shape;

import android.content.Context;
import android.opengl.GLES20;

import com.godot.mooc.opengl.BoyGLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by AllenWang on 2020/11/18.
 */
public class Pentagon implements Shape {
    private static final int COORDS_PER_VERTEX = 3;
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;
    private static final float[] vertex = new float[]{
        1.0f, 0.0f, 0.0f,
        1.618f, 1.902f, 0.0f,
        0.0f, 3.077f, 0.0f,
        -1.618f, 1.902f, 0.0f,
        -1.0f, 0.0f, 0.0f,

            1.0f, 0.0f, -2.0f,
            1.618f, 1.902f, -2.0f,
            0.0f, 3.077f, -2.0f,
            -1.618f, 1.902f, -2.0f,
            -1.0f, 0.0f, -2.0f,
    };

    private static final int[] indices = new int[]{
            0, 1, 2,
            2, 3, 4,
            0, 2, 4,

            5, 6, 7,
            7, 8, 9,
            5, 7, 9,

            0, 1, 5,
            1, 5, 6,

            1, 2, 6,
            2, 7, 6,

            2, 3, 8,
            2, 7, 8,

            3, 8, 9,
            3, 4, 9,

            4, 9, 5,
            4, 0, 5
    };
    private static final float[] colors = new float[] {
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,

        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,

    };
    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private IntBuffer indicesBuffer;
    private int program;
    private int colorLocation;
    private int mvpMatrixLocation;
    private int vertexLocation;

    public Pentagon(Context context) {
        ByteBuffer bb = ByteBuffer.allocateDirect(vertex.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertex);
        vertexBuffer.position(0);

        bb = ByteBuffer.allocateDirect(colors.length * 4);
        bb.order(ByteOrder.nativeOrder());
        colorBuffer = bb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);


        indicesBuffer = IntBuffer.allocate(indices.length);
        indicesBuffer.put(indices);
        indicesBuffer.position(0);

        String vertexSource = BoyGLUtils.readAssetsFile(context, "shader/vertex_pentagon.shader");
        String fragmentSource = BoyGLUtils.readAssetsFile(context, "shader/fragment.shader");
        program = BoyGLUtils.createProgram(vertexSource, fragmentSource);
        GLES20.glUseProgram(program);
        vertexLocation = GLES20.glGetAttribLocation(program, "aPosition");
        mvpMatrixLocation = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        colorLocation = GLES20.glGetAttribLocation(program, "aColor");
    }

    @Override
    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(program);
        GLES20.glEnableVertexAttribArray(vertexLocation);
        GLES20.glVertexAttribPointer(vertexLocation, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);
        GLES20.glEnableVertexAttribArray(colorLocation);
        GLES20.glVertexAttribPointer(colorLocation, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, colorBuffer);

        GLES20.glUniform3f(colorLocation, 0.0f, 1.0f, 0.0f);
        GLES20.glUniformMatrix4fv(mvpMatrixLocation, 1, false, mvpMatrix, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_INT, indicesBuffer);
    }
}
