package com.android.redalert;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    private GameSurfaceView gameSurfaceView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        gameSurfaceView = new GameSurfaceView(this);
        setContentView(gameSurfaceView);
    }
}
