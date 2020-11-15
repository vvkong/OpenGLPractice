package com.godot.mooc.opengl.widget;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.godot.mooc.App;
import com.godot.mooc.opengl.shape.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by AllenWang on 2020/11/15.
 */
class BoyGLRenderer implements GLSurfaceView.Renderer {
    private Triangle triangle;
    private float[] modelMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] projectionMatrix = new float[16];
    private float[] mvMatrix = new float[16];
    private float[] mvpMatrix = new float[16];

    public BoyGLRenderer() {
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // 在这初始化，因为构造函数里用到了OpenGL上下文，切记
        triangle = new Triangle(App.getApp());
        GLES20.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        // 椎体 两个截面的边比例相同
        // (right - left) / width = (bottom-top)/height
        // left = -right; bottom = -1; top = 1
        float right = (float) width/(float)height;
        float left = -right;
        Matrix.frustumM(projectionMatrix, 0, left, right, -1, 1, 1.0f, 8.0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClearDepthf(1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0.0f, 0.0f, -5.0f);

        Matrix.setLookAtM(viewMatrix, 0,
                0, 0, 1,
                0, 0, 0,
                0, 1, 0);


        Matrix.multiplyMM(mvMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvMatrix, 0);

        triangle.draw(mvpMatrix);
    }
}
