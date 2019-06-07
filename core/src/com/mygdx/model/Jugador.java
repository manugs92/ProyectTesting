package com.mygdx.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyGdxGameAssetManager;

import java.util.ArrayList;

public class Jugador {

    private String nombre;
    private Mazo mazo;
    private Mano mano;
    private Cementerio cementerio;
    private int id;
    private boolean avoidToDrawCard;
    private Image avatar;
    private Image avatar2;
    private Image avatar3;
    private Vector2 posAvatar = new Vector2();
    private Vector2 posName = new Vector2();
    private int lives = Partida.INITIAL_LIVES;
    private int invocationOrbs = Partida.INITIAL_INVOCATION_ORBS;
    private Image livesGUI, manaOrbGUI;
    private Vector2 poslives = new Vector2();
    private Vector2 posInvocationOrbs = new Vector2();
    private ArrayList<Criatura> criaturasInvocadas = new ArrayList<>();
    private ArrayList<Carta> cartasColocadas = new ArrayList<>();
    private boolean avoidToDamage = false;

    private int damageToLose;

    public Jugador(String nombre, int id, MyGdxGameAssetManager assetManager, Skin skin) {
        this.nombre=nombre;
        this.id=id;
        this.mazo = new Mazo(assetManager,this);
        this.mano=new Mano(this.mazo);
        this.cementerio=new Cementerio(assetManager,this);
        assetManager.loadMyAvatars();
        assetManager.loadOtherAvatars();
        assetManager.manager.finishLoading();
        livesGUI=new Image(assetManager.manager.get(assetManager.lives,Texture.class));
        manaOrbGUI = new Image(assetManager.manager.get(assetManager.manaOrb,Texture.class));

        if(id==0) {
            avatar = new Image(assetManager.manager.get(assetManager.myAvatar,Texture.class));
            avatar2 = new Image(assetManager.manager.get(assetManager.myAvatar2,Texture.class));
            avatar3 = new Image(assetManager.manager.get(assetManager.rivalAvatar3,Texture.class));
            posAvatar.y = 80;
            posName.y= 60;
            poslives.y = 160;
        }else {
            avatar = new Image(assetManager.manager.get(assetManager.rivalAvatar,Texture.class));
            avatar2 = new Image(assetManager.manager.get(assetManager.rivalAvatar2,Texture.class));
            avatar3 = new Image(assetManager.manager.get(assetManager.rivalAvatar3,Texture.class));
            posAvatar.y = MyGdxGame.SCREEN_HEIGHT-208;
            posName.y=MyGdxGame.SCREEN_HEIGHT-228;
            poslives.y = MyGdxGame.SCREEN_HEIGHT-125;
        }
        posAvatar.x = MyGdxGame.SCREEN_WIDTH - 208;
        posName.x = MyGdxGame.SCREEN_WIDTH - 208;
        poslives.x = 1010;
        posInvocationOrbs.x=poslives.x;
        posInvocationOrbs.y = poslives.y-80;

        avatar.setPosition(posAvatar.x,posAvatar.y);
        avatar2.setPosition(posAvatar.x,posAvatar.y);
        avatar3.setPosition(posAvatar.x,posAvatar.y);
        livesGUI.setPosition(poslives.x,poslives.y);
        manaOrbGUI.setPosition(posInvocationOrbs.x,posInvocationOrbs.y);
    }

    public void setMazo(Mazo mazo) {
        this.mazo=mazo;
    }

    public void setNombre(String nombre) {
        this.nombre=nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public Mazo getMazo() {
        return mazo;
    }

    public int getId() {
        return id;
    }

    public Mano getMano() {
        return mano;
    }

    public boolean isAvoidToDrawCard() { return avoidToDrawCard; }

    public void avoidToDrawCard(Boolean isAvoid) { avoidToDrawCard=isAvoid; }

    public Cementerio getCementerio() {
        return cementerio;
    }

    public void setCementerio(Cementerio cementerio) {
        this.cementerio = cementerio;
    }

    public Image getAvatar() { return avatar; }

    public Image getAvatar2() {return avatar2;}

    public Image getAvatar3() {return avatar3;}

    public Vector2 getPosName() { return posName; }

    public Image getLivesGUI() {
        return livesGUI;
    }

    public Image getManaOrbGUI() {
        return manaOrbGUI;
    }

    public int getLives() { return lives; }

    public void setLives(int lives) { this.lives = lives; }

    public int getInvocationOrbs() { return invocationOrbs; }

    public void setInvocationOrbs(int invocationOrbs) { this.invocationOrbs = invocationOrbs; }

    public void removeInvocationOrbs(int invocationOrbs) {this.invocationOrbs -= invocationOrbs;}

    public Vector2 getPoslives() { return poslives; }

    public Vector2 getPosInvocationOrbs() { return posInvocationOrbs; }

    public void addInvocationOrbs(int orbs) {
        invocationOrbs+=orbs;
    }

    public void addNewInvoquedMonster(Criatura criatura) { criaturasInvocadas.add(criatura); }

    public  ArrayList<Criatura> getCriaturasInvocadas() {
        return criaturasInvocadas;
    }

    public void addNewInvoquedCard(Carta carta) { cartasColocadas.add(carta); }

    public  ArrayList<Carta> getInvoquedCards() {
        return cartasColocadas;
    }

    public boolean isAvoidToDamage() { return avoidToDamage; }

    public void setAvoidToDamage(boolean avoidToDamage) { this.avoidToDamage = avoidToDamage; }

    public int getDamageToLose() { return damageToLose; }

    public void setDamageToLose(int damageToLose) { this.damageToLose = damageToLose; }
}
