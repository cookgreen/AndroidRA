package com.android.redalert;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class GameSurfaceView extends GLSurfaceView {

    private final GameRenderer gameRenderer;

    public GameSurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(2);

        gameRenderer = new GameRenderer();
        setRenderer(gameRenderer);
    }
}
