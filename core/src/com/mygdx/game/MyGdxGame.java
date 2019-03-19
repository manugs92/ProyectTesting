package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.view.ScreenManager;

public class MyGdxGame extends Game {

	@Override
	public void create () {
		ScreenManager screenManager = new ScreenManager(this);
		screenManager.setDefaultScreen();
	}
}
