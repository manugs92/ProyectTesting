package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class MyGdxGameAssetManager {
    public final AssetManager manager = new AssetManager();

    public final String imageSquare = "icons/casilla48.png";

    public void loadImagesDuelScreen() {
        manager.load(imageSquare, Texture.class);
    }
}
