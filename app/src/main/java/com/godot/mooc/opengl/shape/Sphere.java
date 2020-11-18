package com.godot.mooc.opengl.shape;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.godot.mooc.opengl.BoyGLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by AllenWang on 2020/11/17.
 */
public class Sphere implements Shape {
    private static final int COORDS_PER_VERTEX = 3;
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;
    private int program;
    private int vertexLocation;
    private int mvpMatrixLocation;
    private int colorLocation;
    private IntBuffer indicesBuffer;
    private static final int n = 10;
    private static final int VERTEX_COUNT = n * n;
    private static FloatBuffer vertexBuffer;
    static {
//        x = r*sin(theta)cos(fy)
//        y = r*sin(theta)sin(fy)
//        z = r*cos(theta)
//        0<=theta<=pi
//        0<=fy<=2*pi

        calcVertex(n);
    }

    private static void calcVertex(int n) {
        ByteBuffer bb = ByteBuffer.allocateDirect((n+1)*(n+1)*3*4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        float x, y, z;
        float r = 1f;
        double diff = Math.PI / n;
        int k = 0;
        for( float theta=0f; theta<Math.PI; theta+=2*diff ) {
            for( float fy=0f; fy<2*Math.PI; fy+=2*diff ) {
                x = (float) (r * Math.sin(theta)*Math.cos(fy));
                y = (float) (r * Math.sin(theta)*Math.sin(fy));
                z = (float) (r * Math.cos(theta));
                vertexBuffer.put(x);
                vertexBuffer.put(y);
                vertexBuffer.put(z);

                x = (float) (r * Math.sin(diff+theta)*Math.cos(fy));
                y = (float) (r * Math.sin(diff+theta)*Math.sin(fy));
                z = (float) (r * Math.cos(theta));
                vertexBuffer.put(x);
                vertexBuffer.put(y);
                vertexBuffer.put(z);
                k += 6;
            }
        }
        Log.d("Sphere", "size: "+vertexBuffer.position() + ", k: "+ k);
        vertexBuffer.position(0);
    }

    public Sphere(Context context) {
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
        GLES20.glVertexAttribPointer(vertexLocation, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);
        GLES20.glUniform3f(colorLocation, 0.0f, 0.0f, 1.0f);
        GLES20.glUniformMatrix4fv(mvpMatrixLocation, 1, false, mvpMatrix, 0);

//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, VERTEX_COUNT);
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, VERTEX_COUNT);
    }
}
