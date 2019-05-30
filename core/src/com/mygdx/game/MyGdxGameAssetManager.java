package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class MyGdxGameAssetManager {

    public final AssetManager manager = new AssetManager();

    public final String imageSquare = "icons/casilla48.png";
    public final String manaOrb= "icons/mana_orb.png";
    public final String lives = "icons/lives.png";

    public final String imageSquare2 = "icons/casilla48_2.png";
    public final String imageBackCard = "icons/back_card.png";
    public final String imageBackCardToDraw = "icons/back_card_to_draw.png";
    public final String backgroundScroll = "backgrounds/bg_scroll.png";

    public final String myAvatar = "icons/mugshot2.png";
    public final String rivalAvatar = "icons/mugshot.png";
    public final String myAvatar2 = "icons/mugshot2_1.png";
    public final String rivalAvatar2 = "icons/mugshot_1.png";
    public final String passTurnIcon = "icons/pasarturno_0.png";
    public final String whiteFlagIcon = "icons/whiteflag.png";
    public final String cartaInfo = "icons/carta_info.png";


    public void loadImagesDuelScreen() {
        manager.load(imageSquare, Texture.class);
        manager.load(imageSquare2, Texture.class);
        manager.load(passTurnIcon,Texture.class);
        manager.load(whiteFlagIcon,Texture.class);
        manager.load(lives, Texture.class);
        manager.load(cartaInfo, Texture.class);
    }

    public void loadImageDeck() { manager.load(imageSquare, Texture.class); }

    public void loadBackCard() {
        manager.load(imageBackCard,Texture.class);
        manager.load(imageBackCardToDraw,Texture.class);
    }

    public void loadScrollLog() { manager.load(backgroundScroll,Texture.class);}

    public void loadMyAvatars() {
        manager.load(myAvatar,Texture.class);
        manager.load(rivalAvatar,Texture.class);
    }

    public void loadOtherAvatars() {
        manager.load(myAvatar2,Texture.class);
        manager.load(rivalAvatar2,Texture.class);
    }

}
