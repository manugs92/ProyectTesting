package com.mygdx.game.desktop;


import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;


/*
 * Clase que se encarga de ejecutar el juego en PC.
 * */
public class DesktopLauncher {

	/*
	 * Lo único que tiene que tener el main, es objeto de configuración de la aplicación.
	 * (Para setear título de la aplicación e icono)
	 * Y la llamada a la clase principal del juego, con la configuración establecida.
	 * */

	private final static int SCREEN_WIDTH = 1280;
	private final static int SCREEN_HEIGHT = 700;

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width= SCREEN_WIDTH;
		config.height= SCREEN_HEIGHT;
		config.x=-1;
		config.y=-5;
		config.resizable=false;
		config.title = "xD";
		config.addIcon("icons\\xd.png", Files.FileType.Internal);
		new LwjglApplication(new MyGdxGame(), config);
	}
}
