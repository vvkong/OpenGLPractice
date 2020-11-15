package com.godot.mooc.opengl;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.godot.mooc.BuildConfig;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by AllenWang on 2020/11/15.
 */
public class BoyGLUtils {
    private static final String TAG = "BoyGLUtils";

    public static int createProgram(String vertexShaderCode, String fragmentShaderCode) {
        int vertexShader = createShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = createShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
        checkGLError("linkProgram");
//        GLES20.glDeleteShader(vertexShader);
//        GLES20.glDeleteShader(fragmentShader);
        return program;
    }

    public static int createShader(int type, String shaderSourceCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderSourceCode);
        GLES20.glCompileShader(shader);
        if(BuildConfig.DEBUG) {
            String log = GLES20.glGetShaderInfoLog(shader);
            Log.e(TAG, "glGetShaderInfoLog: " + log);
        }
        checkGLError("createShader, type: "+type);
        return shader;
    }

    public static void checkGLError(String op) {
        int error = GLES20.glGetError();
        if( error != GLES20.GL_NO_ERROR ) {
            String errMsg = GLUtils.getEGLErrorString(error);
            Log.e(TAG, String.format("op\n%s", errMsg));
        }
    }

    public static String readAssetsFile(Context context, String name) {
        StringBuilder sb = new StringBuilder();
        AssetManager am = context.getResources().getAssets();
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            is = am.open(name);
            isr = new InputStreamReader(is, "utf-8");
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }catch (Exception e) {
            Log.e(TAG, "readAssetsFile fail. error: " + e.getMessage());
        }
        quietClose(isr);
        quietClose(is);
        return sb.toString();
    }

    private static void quietClose(Closeable closeable) {
        if( closeable != null ) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
