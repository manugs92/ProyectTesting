package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class MyGdxGameAssetManager {

    public final AssetManager manager = new AssetManager();


    //Images from general purposes
    public final String backgroundScroll = "backgrounds/bg_scroll.png";

    //Images from ConfigurationScreen
    public final String back_arrow= "icons/arrow_back.png";
    public final String backgroundBlue = "backgrounds/bg_blue.png";

    //Images from DuelScreen
    public final String imageSquare = "icons/casilla48.png";
    public final String imageSquare2 = "icons/casilla48_2.png";
    public final String imageSquare3 = "icons/casilla48_3.png";
    public final String imageSquare4 = "icons/casilla48_4.png";
    public final String passTurnIcon = "icons/pasarturno_0.png";
    public final String whiteFlagIcon = "icons/whiteflag.png";
    public final String imageBackCard = "icons/back_card.png";
    public final String imageBackCardToDraw = "icons/back_card_to_draw.png";
    public final String handSelection = "icons/hand_selection_0.png";
    public final String handSelection2 = "icons/hand_selection_1.png";
    public final String avoidToInvoqueBg = "icons/bg_avoid_to_invoque.png";
    public final String arrowUp = "icons/arrow_up.png";

    //Images from PlayersHud
    public final String manaOrb= "icons/mana_orb.png";
    public final String lives = "icons/lives.png";
    public final String myAvatar = "icons/mugshot2.png";
    public final String rivalAvatar = "icons/mugshot.png";
    public final String myAvatar2 = "icons/mugshot2_1.png";
    public final String rivalAvatar2 = "icons/mugshot_1.png";
    public final String rivalAvatar3 = "icons/mugshot_2.png";


   //Images from cards info
    public final String textureCardInfo_LavaGolem = "icons/lava_golem_carta_info.png";
    public final String textureCardInfo_Spyro = "icons/spyro_carta_info.png";
    public final String textureCardInfo_RockElemental = "icons/elemental_rock_golem_carta_info.png";

    //Little images from cards
    public final String textureCard_RockElemental = "icons/rock_elemental_card.png";
    public final String textureCard_Spyro = "icons/spyro_card.png";
    public final String textureCard_LavaGolem = "icons/lava_golem_card.png";

    //FrontSprite from Creatures
    public final String textureSpriteRockElementalFront = "icons/rock_elemental_front.png" ;
    public final String textureSpriteSpyroFront = "icons/spyro_front.png";
    public final String textureSpriteLavaGolemFront = "icons/lava_golem_front.png";

    //BackSprite from Creatures
    public final String textureSpriteRockElemental = "icons/rock_elemental.png";
    public final String textureSpriteSpyro = "icons/spyro.png";
    public final String textureSpriteLavaGolem = "icons/lava_golem.png";



    public void loadConfigurationScreenImages() {
        manager.load(backgroundBlue,Texture.class);
        manager.load(back_arrow,Texture.class);
    }

    public void loadDuelScreenImages() {
        manager.load(imageSquare, Texture.class);
        manager.load(imageSquare2, Texture.class);
        manager.load(imageSquare3, Texture.class);
        manager.load(imageSquare4, Texture.class);
        manager.load(passTurnIcon,Texture.class);
        manager.load(whiteFlagIcon,Texture.class);
        manager.load(avoidToInvoqueBg,Texture.class);
        manager.load(arrowUp,Texture.class);
    }

    public void loadGraveyardImages() {
        manager.load(imageSquare, Texture.class); }

    public void loadDeckImages() {
        manager.load(imageBackCard,Texture.class);
        manager.load(imageBackCardToDraw,Texture.class);
        manager.load(handSelection,Texture.class);
        manager.load(handSelection2,Texture.class);
    }

    public void loadScrollLogImage() { manager.load(backgroundScroll,Texture.class);}

    public void loadMyAvatarImage() {
        manager.load(myAvatar,Texture.class);
    }

    public void loadOtherAvatarsImages() {
        manager.load(rivalAvatar,Texture.class);
        manager.load(myAvatar2,Texture.class);
        manager.load(rivalAvatar2,Texture.class);
        manager.load(rivalAvatar3,Texture.class);
        manager.load(lives, Texture.class);
        manager.load(manaOrb,Texture.class);
    }

    public void loadCardsInfoImages(){
        //Texture Cards
        manager.load(textureCard_RockElemental,Texture.class);
        manager.load(textureCard_Spyro,Texture.class);
        manager.load(textureCard_LavaGolem, Texture.class);

        //Texture Card information
        manager.load(textureCardInfo_RockElemental,Texture.class);
        manager.load(textureCardInfo_Spyro,Texture.class);
        manager.load(textureCardInfo_LavaGolem,Texture.class);

        //Texture FrontSprites
        manager.load(textureSpriteRockElementalFront,Texture.class);
        manager.load(textureSpriteSpyroFront,Texture.class);
        manager.load(textureSpriteLavaGolemFront,Texture.class);

        //Texture BackSprites
        manager.load(textureSpriteRockElemental,Texture.class);
        manager.load(textureSpriteSpyro,Texture.class);
        manager.load(textureSpriteLavaGolem,Texture.class);

    }
}
