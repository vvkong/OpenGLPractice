package com.godot.mooc.opengl;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.godot.mooc.R;
import com.godot.mooc.opengl.widget.BoyGLSurfaceView;

public class BasicOpenGLActivity extends AppCompatActivity {

    private BoyGLSurfaceView gdSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_open_g_l);
        gdSurfaceView = findViewById(R.id.gd_surface_view);
    }
}