package com.android.redalert;

import android.os.Bundle;

import org.newdawn.slick.SlickActivity;

import cr0s.javara.main.Main;

public class GameActivity extends SlickActivity {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Main mainGame = Main.getInstance();
        mainGame.Init(getApplicationContext());
        start(mainGame);
        mainGame.setAndroidInput(input);
    }
}
