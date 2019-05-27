package com.mygdx.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyGdxGameAssetManager;

public class Jugador {

    //oh shit.
    private String nombre;
    private Mazo mazo;
    private Mano mano;
    private Cementerio cementerio;
    private int id;
    private boolean avoidToDrawCard;
    private Image avatar;
    private Image avatar2;
    private Vector2 posAvatar = new Vector2();
    private Vector2 posName = new Vector2();
    private int lives = Partida.INITIAL_LIVES;
    private int invocationOrbs = Partida.INITIAL_INVOCATION_ORBS;
    private Image livesGUI,manaOrb;
    private Image invocationOrbsGUI;
    private Vector2 poslives = new Vector2();
    private Vector2 posInvocationOrbs = new Vector2();

    public Jugador(String nombre, int id, MyGdxGameAssetManager assetManager, Skin skin) {
        this.nombre=nombre;
        this.id=id;
        this.mazo = new Mazo(assetManager,this);
        this.mano=new Mano(this.mazo);
        this.cementerio=new Cementerio(assetManager,this);
        assetManager.loadMyAvatars();
        assetManager.loadOtherAvatars();
        assetManager.manager.finishLoading();
        livesGUI=new Image(new Texture(assetManager.lives));
        manaOrb= new Image(new Texture(assetManager.manaOrb));

        if(id==0) {
            avatar = new Image(assetManager.manager.get(assetManager.myAvatar,Texture.class));
            avatar2 = new Image(assetManager.manager.get(assetManager.myAvatar2,Texture.class));
            posAvatar.x = MyGdxGame.SCREEN_WIDTH - 208;
            posAvatar.y = 80;
            posName.x = MyGdxGame.SCREEN_WIDTH - 208;
            posName.y= 60;
            livesGUI.setPosition(1010, 160);
            manaOrb.setPosition(livesGUI.getX(),livesGUI.getY()-80);
        }else {
            avatar = new Image(assetManager.manager.get(assetManager.rivalAvatar,Texture.class));
            avatar2 = new Image(assetManager.manager.get(assetManager.rivalAvatar2,Texture.class));
            posAvatar.x = MyGdxGame.SCREEN_WIDTH-208;
            posAvatar.y = MyGdxGame.SCREEN_HEIGHT-208;
            posName.x = MyGdxGame.SCREEN_WIDTH - 208;
            posName.y=MyGdxGame.SCREEN_HEIGHT-228;
            livesGUI.setPosition(1010, MyGdxGame.SCREEN_HEIGHT-125);
            manaOrb.setPosition(livesGUI.getX(),livesGUI.getY()-80);
        }
        avatar.setPosition(posAvatar.x,posAvatar.y);
        avatar2.setPosition(posAvatar.x,posAvatar.y);
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

    public Vector2 getPosName() { return posName; }

    public Image getLivesGUI() {
        return livesGUI;
    }

    public void setLivesGUI(Image livesGUI) {
        this.livesGUI = livesGUI;
    }

    public Image getManaOrb() {
        return manaOrb;
    }

    public void setManaOrb(Image manaOrb) {
        this.manaOrb = manaOrb;
    }
}
