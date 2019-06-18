package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.managers.MyGdxGameConfigurationManager;
import com.mygdx.managers.MyGdxGameAssetManager;
import com.mygdx.managers.MyGdxGameScreenManager;

public class MyGdxGame extends Game {

	public final static int SCREEN_WIDTH = 1280;
	public final static int SCREEN_HEIGHT = 700;
	public com.mygdx.managers.MyGdxGameAssetManager assets;
	public MyGdxGameConfigurationManager myGdxGameConfigurationManager;
	public Skin skin ;
	public BitmapFont font;
	public FitViewport fitViewport;
	public OrthographicCamera camera;

	@Override
	public void create () {
		assets=new MyGdxGameAssetManager();
		myGdxGameConfigurationManager = new MyGdxGameConfigurationManager();
		camera = new OrthographicCamera();
		fitViewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
		camera.position.set(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, 0);
		fitViewport.apply();
		skin= new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));
		font = new BitmapFont();

		MyGdxGameScreenManager myGdxGameScreenManager = new MyGdxGameScreenManager(this);
		myGdxGameScreenManager.setDefaultScreen();
	}
}
