package com.godot.mooc.opengl;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

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
        int[] status = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, status, 0);
        if( status[0] == GLES20.GL_FALSE ) {
            String error = GLES20.glGetProgramInfoLog(program);
            checkGLError("linkProgram fail: " + error);
            GLES20.glDeleteProgram(program);
        }
        GLES20.glDeleteShader(vertexShader);
        GLES20.glDeleteShader(fragmentShader);
        return program;
    }

    public static int createShader(int type, String shaderSourceCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderSourceCode);
        GLES20.glCompileShader(shader);
        int[] status = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0);
        if( status[0] == GLES20.GL_FALSE ) {
            String log = GLES20.glGetShaderInfoLog(shader);
            Log.e(TAG, "glGetShaderInfoLog: " + log);
            GLES20.glDeleteShader(shader);
        }
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

    public static IntBuffer createIntBuffer(int[] array) {
        if( array != null ) {
            IntBuffer intBuffer = IntBuffer.allocate(array.length);
            intBuffer.put(array);
            intBuffer.position(0);
            return intBuffer;
        }
        return null;
    }

    public static FloatBuffer createFloatBuffer(float[] array) {
        if( array != null ) {
            ByteBuffer bb = ByteBuffer.allocateDirect(array.length*4);
            bb.order(ByteOrder.nativeOrder());
            FloatBuffer floatBuffer = bb.asFloatBuffer();
            floatBuffer.put(array);
            floatBuffer.position(0);
            return floatBuffer;
        }
        return null;
    }


}
