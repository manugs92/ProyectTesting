package com.mygdx.model;

import com.badlogic.gdx.math.Vector2;

public class AvisosPartida {

    private enum avisos {DESCARTAR_CARTAS,ANTES_DEBES_ROBAR};
    private String texttoShow = "";

    private final float posXAvisos = 870;
    private final float posYAvisos = 180;

    private avisos avisos = null;
    private boolean showed = false;
    private Vector2 positionAviso = new Vector2();

    public AvisosPartida() {
        positionAviso.x = posXAvisos;
        positionAviso.y = posYAvisos;
    }

    public void setAvisos(int avisosValue,int size) {
        switch (avisosValue) {
                case 1:
                    showed = true;
                    avisos = avisos.DESCARTAR_CARTAS;
                    texttoShow = "Descarta "+(size - Partida.MAX_CARDS_IN_HAND)+" cartas\ncon click derecho.";
                    break;

                case 2:
                    showed = true;
                    avisos = avisos.ANTES_DEBES_ROBAR;
                    texttoShow = "Antes de pasar turno\ndebes robar\nuna carta.";
                    break;

                default:
                    avisos = null;
                    showed = false;
                    texttoShow ="";
                    break;
        }
    }

    public String getTexttoShow() { return texttoShow;}

    public Vector2 getPositionAviso() {return positionAviso;}

    public boolean isShowed() {
        return showed;
    }

    public avisos getAvisos() {return avisos;}

    public void setShowed(boolean state) { showed=state;}

}
