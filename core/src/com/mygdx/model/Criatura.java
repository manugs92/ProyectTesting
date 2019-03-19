package com.mygdx.model;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;

public class Criatura {
    int ataque, defensa, movimiento,alcance;
    Carta.Tipo tipo= Carta.Tipo.CRIATURA;
//    Habilidad especial(Habilidad Especial);
    TipoEspecie criatura ;
//    Tipo elemental(enum);
    ArrayList<Equipamiento> equipamientos = new ArrayList<Equipamiento>();
    Sprite spriteCriatura;


    public enum TipoEspecie {
        HUMANO,
        DRAGON,
        PLANTAS,
        GOLEM;
    }

}
