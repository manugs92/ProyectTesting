package com.mygdx.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Criatura extends Carta {



    int ataque, defensa, movimiento, alcance;
    Carta.Tipo tipo = Carta.Tipo.CRIATURA;
    //  Habilidad especial(Habilidad Especial);
    TipoEspecie criatura;
    TipoElemental elemental;
    Texture spriteCriatura;
    ArrayList<Equipamiento> equipamientos = new ArrayList<Equipamiento>();
    private Vector2 lastPosition = new Vector2();


    @Override
    public Vector2 getLastPosition() {
        return lastPosition;
    }

    @Override
    public void setLastPosition(float x, float y) {

        this.lastPosition.x = x;
        this.lastPosition.y=y;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
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

    @Override
    public Texture getSpriteCriatura() {
        return spriteCriatura;
    }

    @Override
    public void setSpriteCriatura(Texture spriteCriatura) {
        this.spriteCriatura = spriteCriatura;
    }

    public ArrayList<Equipamiento> getEquipamientos() {
        return equipamientos;
    }

    public void setEquipamientos(ArrayList<Equipamiento> equipamientos) {
        this.equipamientos = equipamientos;
    }

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
        GOLEM;
    }

}
