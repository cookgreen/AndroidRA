package com.example.myapplication;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class MySurfaceView extends GLSurfaceView {
    private final MyRenderer renderer;

    public MySurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(2);

        renderer = new MyRenderer();

        setRenderer(renderer);
    }
}
