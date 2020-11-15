package com.godot.mooc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.godot.mooc.opengl.BasicOpenGLActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findAndSetListener(R.id.btn_opengl_basic);
    }

    private void findAndSetListener(int resId) {
        findViewById(resId).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.btn_opengl_basic:
                intent = new Intent(this, BasicOpenGLActivity.class);
                break;
        }
        if( intent != null ) {
            startActivity(intent);
        }
    }
}