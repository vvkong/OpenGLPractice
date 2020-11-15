package com.godot.mooc.opengl.widget;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by AllenWang on 2020/11/15.
 */
public class BoyGLSurfaceView extends GLSurfaceView {


    public BoyGLSurfaceView(Context context) {
        this(context, null);
    }

    public BoyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setEGLContextClientVersion(2);
        setRenderer(new BoyGLRenderer());
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }
}
