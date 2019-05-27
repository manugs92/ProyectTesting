package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class MyGdxGameAssetManager {

    public final AssetManager manager = new AssetManager();

    public final String imageSquare = "icons/casilla48.png";
    public final String imageSquare2 = "icons/casilla48_2.png";
    public final String imageBackCard = "icons/back_card.png";
    public final String backgroundScroll = "backgrounds/bg_scroll.png";

    public final String myAvatar = "icons/mugshot3.png";
    public final String rivalAvatar = "icons/mugshot3.png";

    public void loadImagesDuelScreen() {
        manager.load(imageSquare, Texture.class);
        manager.load(imageSquare2, Texture.class);
    }

    public void loadImageDeck() {
        manager.load(imageSquare, Texture.class);
    }

    public void loadBackCard() {manager.load(imageBackCard,Texture.class);}

    public void loadScrollLog() { manager.load(backgroundScroll,Texture.class);}

    public void loadAvatars() {
        manager.load(myAvatar,Texture.class);
        manager.load(rivalAvatar,Texture.class);
    }

}
