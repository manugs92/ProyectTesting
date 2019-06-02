package com.mygdx.game.desktop;


import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyGdxGameScreen;


/*
 * Clase que se encarga de ejecutar el juego en PC.
 * */
public class DesktopLauncher {

	/*
	 * Lo único que tiene que tener el main, es objeto de configuración de la aplicación.
	 * (Para setear título de la aplicación e icono)
	 * Y la llamada a la clase principal del juego, con la configuración establecida.
	 * */


	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width= MyGdxGame.SCREEN_WIDTH;
		config.height= MyGdxGame.SCREEN_HEIGHT;
		config.title = "The Cave";
		config.y = -2;
		config.addIcon("icons\\the_cave.png", Files.FileType.Internal);
		new LwjglApplication(new MyGdxGame(), config);
	}
}
