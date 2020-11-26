package com.godot.mooc.opengl.shape;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.godot.mooc.opengl.BoyGLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by AllenWang on 2020/11/15.
 */
public class Oval implements Shape {
    private final int COORDS_PER_VERTEX = 3;
    private final int VERTEX_STRIP = COORDS_PER_VERTEX * 4;
    private static final FloatBuffer vertexBuffer;
    private static final int vertexCount;
    static {
        int n = 1000;
        vertexBuffer = calcVertex(2, 1, n);
        vertexCount = n;
    }

    private int colorLocation;

    // (x/a)^2 + (y/b)^2 = 1
    // x = a *cos(beta) (a>b>0)；
    // y = a *sin(beta) *（b/a）
    private static FloatBuffer calcVertex(int a, int b, int n) {
        double start = 0;
        double end = Math.PI * 2;
        double diff = (end - start)/n;
        ByteBuffer bb = ByteBuffer.allocateDirect((n+1) * 3 * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.position(0);

        double beta = start;
        float x, y;
        while (beta < end ) {
            x = (float)(a * Math.cos(beta));
            y = (float)(a * Math.sin(beta))*b/a;
            fb.put(x);
            fb.put(y);
            fb.put(0.0f);
            beta += diff;
        }
        fb.position(0);
        return fb;
    }


    private int program;
    private final int vertexLocation;
    private final int mvpMatrixLocation;

    public Oval(Context context) {
        String vertexSource = BoyGLUtils.readAssetsFile(context, "shader/vertex.shader");
        String fragmentSource = BoyGLUtils.readAssetsFile(context, "shader/fragment.shader");
        program = BoyGLUtils.createProgram(vertexSource, fragmentSource);
        GLES20.glUseProgram(program);
        vertexLocation = GLES20.glGetAttribLocation(program, "aPosition");
        mvpMatrixLocation = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        colorLocation = GLES20.glGetUniformLocation(program, "uColor");
    }

    @Override
    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(program);

        GLES20.glEnableVertexAttribArray(vertexLocation);
        GLES20.glVertexAttribPointer(vertexLocation, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIP, vertexBuffer);
        GLES20.glUniformMatrix4fv(mvpMatrixLocation, 1, false, mvpMatrix, 0);
        GLES20.glUniform3f(colorLocation, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount);
        GLES20.glUniform3f(colorLocation, 1.0f, 1.0f, 0.0f);
        GLES20.glLineWidth(10f);
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, vertexCount);
    }
}
