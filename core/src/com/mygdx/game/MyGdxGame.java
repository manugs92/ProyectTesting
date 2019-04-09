package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.view.ScreenManager;

public class MyGdxGame extends Game {

	public final static int SCREEN_WIDTH = 1920;
	public final static int SCREEN_HEIGHT = 1080;

	FitViewport fitViewport;
	OrthographicCamera camera;

	@Override
	public void create () {
		camera = new OrthographicCamera();
		fitViewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
		camera.position.set(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, 0);
		fitViewport.apply();

		ScreenManager screenManager = new ScreenManager(this);
		screenManager.setDefaultScreen();
	}
}
