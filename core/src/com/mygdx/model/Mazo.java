package com.mygdx.model;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyGdxGameAssetManager;


import java.util.*;

import static com.mygdx.model.Criatura.TipoElemental.FUEGO;
import static com.mygdx.model.Criatura.TipoElemental.ROCK;
import static com.mygdx.model.Criatura.TipoEspecie.DRAGON;
import static com.mygdx.model.Criatura.TipoEspecie.GOLEM;

public class Mazo {

    private final int MAX_CARDS = 40;
    private final int MIN_CARDS = 20;

    private final float POS_X_MAZO = Tablero.POS_X_TABLERO + (Casilla.MEDIDA_CASILLA*7);
    private final float POS_Y_MAZO_J1 = 87;
    private final float POS_Y_MAZO_J2 = MyGdxGame.SCREEN_HEIGHT-133;

    private ArrayList<Carta> cartasMazo = new ArrayList<>();
    private ArrayList<Carta> shuffleMazo = new ArrayList<>();
    private Texture textureMazoDefault,textureMazoAvoidToDraw;
    private Image mazoDefaultGUI,mazoAvoidToDrawGUI;
    private Vector2 positionGUI = new Vector2();

    public Mazo(MyGdxGameAssetManager assetManager, Jugador jugador) {
        assetManager.loadBackCard();
        assetManager.manager.finishLoading();
        textureMazoDefault = assetManager.manager.get(assetManager.imageBackCard, Texture.class);
        mazoDefaultGUI = new Image(textureMazoDefault);
        textureMazoAvoidToDraw = assetManager.manager.get(assetManager.imageBackCardToDraw,Texture.class);
        mazoAvoidToDrawGUI = new Image(textureMazoAvoidToDraw);
        if(jugador.getId()==0) {
            positionGUI.y= POS_Y_MAZO_J1;
        }else {
            positionGUI.y= POS_Y_MAZO_J2;
        }
        positionGUI.x= POS_X_MAZO;
        mazoDefaultGUI.setPosition(positionGUI.x,positionGUI.y);
        mazoAvoidToDrawGUI.setPosition(positionGUI.x,positionGUI.y);

        DefaultDeck();

        mazoAvoidToDrawGUI.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float cx, float cy) {
                drawCard(jugador);
            }
        });
        ShuffleMazo();
    }

    public void drawCard(Jugador jugador) {
        if(jugador.isAvoidToDrawCard() && cartasMazo.size()>0) {
            jugador.getMano().getCartasMano().add(shuffleMazo.get(0));
            shuffleMazo.remove(0);
            jugador.avoidToDrawCard(false);
        }
    }

    public ArrayList<Carta> getCartasMazo() {
        return cartasMazo;
    }

    public void addCardToDeck(Carta carta) {
        this.cartasMazo.add(carta);
    }

    public ArrayList<Carta> DefaultDeck() {

        MyGdxGameAssetManager assetManager=new MyGdxGameAssetManager();

        Texture textureCard_Spyro = new Texture("icons\\carta.png");
        Texture textureCardInfo_Spyro = new Texture( assetManager.spyroCartaInfo);
        Texture textureSpriteSpyro= new Texture("icons\\spyro.png");
        Texture textureSpriteSpyroFront= new Texture("icons\\spyro_front.png");

        Texture textureCard_LavaGolem = new Texture("icons\\lava_golem_card.png");
        Texture textureCardInfo_LavaGolem = new Texture( assetManager.lavaGolemCartaInfo);
        Texture textureSpriteLavaGolem= new Texture("icons\\lava_golem.png");
        Texture textureSpriteLavaGolemFront= new Texture("icons\\lava_golem_front.png");

        Texture textureCard_RockElemental = new Texture("icons\\rock_elemental_card.png");
        Texture textureCardInfo_RockElemental = new Texture( assetManager.rockElementalGolemCartaInfo);
        Texture textureSpriteRockElemental= new Texture("icons\\rock_elemental.png");
        Texture textureSpriteRockElementalFront= new Texture("icons\\rock_elemental_front.png");


        while (cartasMazo.size()<=MIN_CARDS-1) {
            Criatura spyro = new Criatura(DRAGON,FUEGO,
                    textureSpriteSpyro,textureSpriteSpyroFront,
                    textureCardInfo_Spyro,
                    3,2,7,0);
            instanceCreatureDetail(spyro,"spyro",
                    textureCard_Spyro,2);

            Criatura lavaGolem = new Criatura(GOLEM,FUEGO,
                    textureSpriteLavaGolem,
                    textureSpriteLavaGolemFront,
                    textureCardInfo_LavaGolem,
                    3,3,2,0);
            instanceCreatureDetail(lavaGolem,"GolemManu",
                    textureCard_LavaGolem,3 );

            Criatura rockElemental = new Criatura(GOLEM,ROCK,
                    textureSpriteRockElemental,
                    textureSpriteRockElementalFront,
                    textureCardInfo_RockElemental,
                    4,6,1,0);
            instanceCreatureDetail(rockElemental,"RockElemental",
                    textureCard_RockElemental,4 );
        }
        return cartasMazo;
    }

    private void instanceCreatureDetail(Criatura creature ,String name,Texture texture,int costInvocation) {
        creature.setNombre(name);
        creature.setImage(texture);
        creature.setLastPosition(-1,-1);
        creature.setFirstPosition(-1,-1);
        creature.setCostInvocation(costInvocation);
        cartasMazo.add(creature);
    }

    public Image getMazoDefaultGUI() { return mazoDefaultGUI; }

    public Image getMazoAvoidToDrawGUI() {return  mazoAvoidToDrawGUI;}

    public void setPositionGUI(float x, float y) {
        this.mazoDefaultGUI.setPosition(x,y);
        this.positionGUI.x = x;
        this.positionGUI.y =y;
    }

    public Vector2 getPositionGUI() {
        return positionGUI;
    }

    public void ShuffleMazo() {
        shuffleMazo = cartasMazo;
        Collections.shuffle(shuffleMazo);
    }

    public ArrayList<Carta> getShuffleMazo() {
        return shuffleMazo;
    }
}
