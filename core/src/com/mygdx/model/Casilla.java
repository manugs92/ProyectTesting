package com.mygdx.model;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Casilla {

    private Criatura criatura;
    private Trampa trampa;
    private Vector2 positionGUI = new Vector2();
    private Texture textureCasilla;
    private Image imageCasilla;
    private int state;

    public static final int MEDIDA_CASILLA = 48;

    public void setCriatura(Criatura criatura) {
        this.criatura=criatura;
    }

    public void setTrampa(Trampa trampa) {
        this.trampa=trampa;
    }

    public Criatura getCriatura(){
        return this.criatura;
    }

    public Trampa getTrampa() {
        return this.trampa;
    }

    public Vector2 getPositionGUI() {
        return positionGUI;
    }

    public void setPositionGUI(float x,float y) {

        this.positionGUI.x = x;
        this.positionGUI.y = y;
    }

    public Texture getTextureCasilla() {
        return textureCasilla;
    }

    public void setTextureCasilla(Texture textureCasilla) {
        this.textureCasilla = textureCasilla;
    }

    public Image getImageCasilla() {
        return imageCasilla;
    }

    public void setImageCasilla(Image imageCasilla) {
        this.imageCasilla = imageCasilla;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state= state;
    }

    public void addListenerToBoard(Tablero tablero,Partida partida,int x2, int y2) {
        this.imageCasilla.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Listener cuando tenemos una carta escogida.
                Carta selectedCard = partida.getSelectedCard();

                if(selectedCard !=null && Casilla.this.getCriatura()== null && Casilla.this.getState() != 0) {
                    if(selectedCard.getLastPosition().x != -1 && selectedCard.getLastPosition().y != -1) {
                        tablero.getCasilla((int)selectedCard.getPosition().x,(int)selectedCard.getPosition().y).setCriatura(null);
                    }
                    selectedCard.setPosition(x2,y2);
                    selectedCard.setLastPosition(x2,y2);
                    Casilla.this.setCriatura((Criatura) selectedCard);
                    partida.addNewInvoquedMonster((Criatura) selectedCard);
                    selectedCard=null;
                    partida.setSelectedCard(selectedCard);

                    for(int i=0;i<=tablero.getCasillas().length-1;i++) {
                        tablero.getCasilla(i,0).getImageCasilla().setColor(255,255,255,255);
                        tablero.getCasilla(i,0).setState(0);
                    }

                }else {
                    //sin carta selecionada
                    if(Casilla.this.getCriatura() != null){
                        for(int i=0;i<=tablero.getCasillas().length-1;i++) {
                            if(tablero.getCasilla(i,0).getCriatura()==null) {
                                tablero.getCasilla(i,0).getImageCasilla().setColor(255,0,255,255);
                                tablero.getCasilla(i,0).setState(1);
                            }
                            selectedCard=tablero.getCasilla(x2,y2).getCriatura();
                            partida.setSelectedCard(selectedCard);
                        }
                    }
                }
            }});
    }
}
