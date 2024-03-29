package com.mygdx.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;

public class Criatura extends Carta {

    public enum TipoEspecie {
        HUMANO,
        DRAGON,
        PLANTAS,
        GOLEM;
    }

    public enum TipoElemental {
        NORMAL,
        FUEGO,
        PLANTA,
        ROCK;
    }

    int ataque, vida, bufferVida, movimiento, alcance;
    Carta.Tipo tipo = Carta.Tipo.CRIATURA;
    //  Habilidad especial(Habilidad Especial);
    TipoEspecie criatura;
    TipoElemental elemental;
    Texture spriteCriatura;
    Texture spriteCriaturaFront;
    ArrayList<Equipamiento> equipamientos = new ArrayList<Equipamiento>();
    Boolean moved;

    public Criatura(TipoEspecie criatura, TipoElemental elemental, Texture spriteCriatura, Texture spriteCriaturaFront, Texture cardDetailInfo, int ataque, int vida, int movimiento, int alcance, int owner){
        this.criatura=criatura;
        this.elemental=elemental;
        this.spriteCriatura=spriteCriatura;
        this.spriteCriaturaFront=spriteCriaturaFront;
        this.ataque=ataque;
        this.vida = vida;
        this.bufferVida = vida;
        this.movimiento=movimiento;
        this.alcance=alcance;
        tipo = Tipo.CRIATURA;
        this.cardDetailInfo=new Image(cardDetailInfo);
        this.moved=false;
        setOwnerId(owner);
    }



    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(int movimiento) {
        this.movimiento = movimiento;
    }

    public int getAlcance() {
        return alcance;
    }

    public void setAlcance(int alcance) {
        this.alcance = alcance;
    }

    public Carta.Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Carta.Tipo tipo) {
        this.tipo = tipo;
    }

    public TipoEspecie getCriatura() {
        return criatura;
    }

    public void setCriatura(TipoEspecie criatura) {
        this.criatura = criatura;
    }

    public TipoElemental getElemental() {
        return elemental;
    }

    public void setElemental(TipoElemental elemental) {
        this.elemental = elemental;
    }

    public Texture getSprite() {
        return spriteCriatura;
    }

    public Texture getSpriteFront() { return spriteCriaturaFront; }

    public void setSprite(Texture spriteCriatura) {
        this.spriteCriatura = spriteCriatura;
    }

    public ArrayList<Equipamiento> getEquipamientos() {
        return equipamientos;
    }

    public void setEquipamientos(ArrayList<Equipamiento> equipamientos) {
        this.equipamientos = equipamientos;
    }

    public Boolean isMoved() {
        return moved;
    }

    public void setMoved(Boolean moved) {
        this.moved = moved;
    }

    public void setBufferVida(int vida) {this.bufferVida=vida;}

    public int getBufferVida() { return bufferVida; }
}
