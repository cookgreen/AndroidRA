package com.example.myapplication;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class OpenGLActivity extends AppCompatActivity {
    private MySurfaceView glSurfaceView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        glSurfaceView = new MySurfaceView(this);
        setContentView(glSurfaceView);
    }
}
