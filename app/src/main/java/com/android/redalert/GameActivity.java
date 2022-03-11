package com.android.redalert;

import android.os.Bundle;

import org.newdawn.slick.SlickActivity;

import cr0s.javara.main.Main;

public class GameActivity extends SlickActivity {
    private GameSurfaceView gameSurfaceView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Main mainGame = new Main();
        mainGame.RegiserApp(getApplicationContext());
        mainGame.Init();

        start(mainGame);
    }
}
