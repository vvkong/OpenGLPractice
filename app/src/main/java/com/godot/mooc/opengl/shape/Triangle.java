package com.godot.mooc.opengl.shape;

import android.content.Context;
import android.opengl.GLES20;

import androidx.annotation.NonNull;

import com.godot.mooc.opengl.BoyGLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by AllenWang on 2020/11/15.
 */
public class Triangle {
    private int program;
    private int mvpMatrixLocation;
    private int vertexLocation;
    private int colorLocation;

    private static final int COORDS_PER_VERTEX = 3;
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;
    private FloatBuffer vertexBuf;
    private float[] vertex = new float[]{
            -1.0f, -1.0f, 0.0f,
            1.0f, -1.0f, 0.0f,
            0.0f, 1.0f, 0.0f
    };
    private int vertexCount = vertex.length / 3;

    public Triangle(Context context) {
        ByteBuffer bb = ByteBuffer.allocateDirect(vertex.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuf = bb.asFloatBuffer();
        vertexBuf.put(vertex);
        // 切记压入数据后再调整position
        vertexBuf.position(0);

        String vertexSource = BoyGLUtils.readAssetsFile(context, "shader/vertex.shader");
        String fragmentSource = BoyGLUtils.readAssetsFile(context, "shader/fragment.shader");
        program = BoyGLUtils.createProgram(vertexSource, fragmentSource);
        GLES20.glUseProgram(program);
        vertexLocation = GLES20.glGetAttribLocation(program, "aPosition");
        BoyGLUtils.checkGLError("glGetAttribLocation");
        mvpMatrixLocation = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        BoyGLUtils.checkGLError("glGetUniformLocation");
//        colorLocation = GLES20.glGetUniformLocation(program, "uColor");

    }

    public void draw(@NonNull float[] mvpMatrix) {
        GLES20.glUseProgram(program);

        GLES20.glUniformMatrix4fv(mvpMatrixLocation, 1, false, mvpMatrix, 0);

        GLES20.glEnableVertexAttribArray(vertexLocation);
        GLES20.glVertexAttribPointer(vertexLocation, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuf);


        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
    }

    public void release() {
        GLES20.glDeleteProgram(program);
    }
}
