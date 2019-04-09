package com.mygdx.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Casilla extends Actor {


    private static int WIDTH=48;
    private static int HEIGHT=48;

    private Image texture;
    private Criatura criatura;
    private Trampa trampa;
    private int posX;
    private int posY;
    private boolean isInvocation;

    public Casilla(int posY, int posX){
        this.posX=posX;
        this.posY=posY;
        positionChanged();

    }




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

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public boolean isInvocation() {
        return isInvocation;
    }

    public void setInvocation(boolean invocation) {
        isInvocation = invocation;
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static void setWIDTH(int WIDTH) {
        Casilla.WIDTH = WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public static void setHEIGHT(int HEIGHT) {
        Casilla.HEIGHT = HEIGHT;
    }

    public void setTexture(Image image) {
        this.texture=image;
    }
}
