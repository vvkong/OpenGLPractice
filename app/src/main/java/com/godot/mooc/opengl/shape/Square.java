package com.godot.mooc.opengl.shape;

import android.content.Context;
import android.opengl.GLES20;

import com.godot.mooc.opengl.BoyGLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by AllenWang on 2020/11/15.
 */
public class Square {
    private static final int COORDS_PER_VERTEX = 3;
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;
    private int program;
    private int vertexLocation;
    private int mvpMatrixLocation;
    private int colorLocation;
    private IntBuffer indicesBuffer;
    private FloatBuffer vertexBuffer;
    private static float[] vertex = new float[] {
      1.0f, 1.0f, 0.0f,
      1.0f, -1.0f, 0.0f,
      -1.0f, -1.0f, 0.0f,
      -1.0f, 1.0f, 0.0f,
       1.0f, 1.0f, 0.0f,
        -1.0f, -1.0f, 0.0f,
    };
    private static int[] indices = new int[] {
            0, 1, 2,
            2, 3, 0
    };

    public Square(Context context) {
        ByteBuffer bb = ByteBuffer.allocateDirect(vertex.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertex);
        vertexBuffer.position(0);

        indicesBuffer = IntBuffer.allocate(indices.length);
        indicesBuffer.put(indices);
        indicesBuffer.position(0);

        String vertexSource = BoyGLUtils.readAssetsFile(context, "shader/vertex.shader");
        String fragmentSource = BoyGLUtils.readAssetsFile(context, "shader/fragment.shader");
        program = BoyGLUtils.createProgram(vertexSource, fragmentSource);
        GLES20.glUseProgram(program);
        vertexLocation = GLES20.glGetAttribLocation(program, "aPosition");
        mvpMatrixLocation = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        colorLocation = GLES20.glGetUniformLocation(program, "uColor");
    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(program);
        GLES20.glEnableVertexAttribArray(vertexLocation);
        GLES20.glVertexAttribPointer(vertexLocation, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);
        GLES20.glUniform3f(colorLocation, 0.0f, 0.0f, 1.0f);
        GLES20.glUniformMatrix4fv(mvpMatrixLocation, 1, false, mvpMatrix, 0);

//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertex.length/COORDS_PER_VERTEX);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_INT, indicesBuffer);
    }
}
