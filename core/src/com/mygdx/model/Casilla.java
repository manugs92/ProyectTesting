package com.mygdx.model;

public class Casilla {

    private Criatura criatura;
    private Trampa trampa;

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
}
