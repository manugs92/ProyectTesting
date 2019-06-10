package com.mygdx.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyGdxGameAssetManager;
import com.mygdx.gui.BackgroundScroll;

import java.util.ArrayList;

public class DuelLog {

    private ArrayList<String> duelLog = new ArrayList<>();
    private boolean newMsg;
    private Texture textureBgScroll;
    private Skin skin;
    private ScrollPane scrollPane;

    public DuelLog(Skin skin) {
        this.skin=skin;
        newMsg=true;
        duelLog.add("Duelo empezado");
    }


    public ScrollPane writeLog(MyGdxGameAssetManager assetManager, Batch batch) {
        assetManager.loadScrollLogImage();
        assetManager.manager.finishLoading();
        textureBgScroll = assetManager.manager.get(assetManager.backgroundScroll, Texture .class);
        textureBgScroll.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        Table scrollTable = new Table();
        scrollTable.setPosition(0,0);
        BackgroundScroll backgroundScroll = new BackgroundScroll(batch,textureBgScroll,0,0,0,0, MyGdxGame.SCREEN_WIDTH,MyGdxGame.SCREEN_HEIGHT);
        scrollTable.setBackground(backgroundScroll);

        this.scrollPane = new ScrollPane(scrollTable,skin);
        scrollPane.setColor(Color.BLUE);
        scrollPane.setPosition(0,MyGdxGame.SCREEN_HEIGHT-(MyGdxGame.SCREEN_HEIGHT/2));
        scrollPane.setWidth(400);
        scrollPane.setHeight(MyGdxGame.SCREEN_HEIGHT/2);
        scrollPane.setScrollbarsVisible(true);
        scrollPane.setForceScroll(false,true);
        scrollPane.setFadeScrollBars(true);
        scrollPane.layout ();

        for(int i=0;i<duelLog.size();i++) {
            //Mensaje a añadir del log.
            Label label = new Label(duelLog.get(i),skin);
            label.getStyle().fontColor.set(Color.WHITE);
            scrollTable.add(label).expandX().expandY().fillX().pad(5,5,5,5);
            scrollTable.row();
        }

        scrollPane.layout ();
        scrollPane.setScrollPercentY (100);

        newMsg=false;
        return scrollPane;
    }

    public ScrollPane writeLogSumaryScreen(MyGdxGameAssetManager assetManager, Batch batch) {
        assetManager.loadScrollLogImage();
        assetManager.manager.finishLoading();
        textureBgScroll = assetManager.manager.get(assetManager.backgroundScroll, Texture .class);
        textureBgScroll.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        Table scrollTable = new Table();
        scrollTable.setPosition(0,0);
        BackgroundScroll backgroundScroll = new BackgroundScroll(batch,textureBgScroll,0,0,0,0, MyGdxGame.SCREEN_WIDTH,MyGdxGame.SCREEN_HEIGHT);
        scrollTable.setBackground(backgroundScroll);

        this.scrollPane = new ScrollPane(scrollTable,skin);
        scrollPane.setColor(Color.BLUE);
        scrollPane.setPosition(400,20);
        scrollPane.setWidth(430);
        scrollPane.setHeight(MyGdxGame.SCREEN_HEIGHT/2);
        scrollPane.setScrollbarsVisible(true);
        scrollPane.setForceScroll(false,true);
        scrollPane.setFadeScrollBars(true);

        for(int i=0;i<duelLog.size();i++) {
            //Mensaje a añadir del log.
            Label label = new Label(duelLog.get(i),skin);
            label.getStyle().fontColor.set(Color.WHITE);
            scrollTable.add(label).expandX().expandY().fillX().pad(5,5,5,5);
            scrollTable.row();
        }

        newMsg=false;
        return scrollPane;
    }


    public void setNewMsgTrue() {
        this.newMsg=true;
    }

    public void setNewMsgFalse() {
        this.newMsg = false;
    }

    public boolean isNewMsg() {
        return this.newMsg;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public void addMsgToLog(String msg){ duelLog.add(msg); }

    //Mostramos el primer mensaje del log de inicio de partida.
    public void announceStartMsgLog(Partida partida) {
        addMsgToLog(partida.getJugador(0).getNombre().toUpperCase()+" VS "+partida.getJugador(1).getNombre().toUpperCase());
        if (partida.getOwnerTurn() == 0) {
            addMsgToLog("Empieza " + partida.getJugador(0).getNombre().toUpperCase());
        } else {
            addMsgToLog("Empieza " + partida.getJugador(1).getNombre().toUpperCase());
        }
        setNewMsgTrue();
    }

    public void announceCardInvoqued(Partida partida, Carta selectedCard) {
        addMsgToLog(partida.getJugador(0).getNombre().toUpperCase()+" ha invocado a "+selectedCard.getNombre().toUpperCase()+" en la CASILLA "+(int)selectedCard.getLastPosition().x+","+(int)selectedCard.getLastPosition().y);
        setNewMsgTrue();
        getScrollPane().remove();
    }

    public void announcePlayerAtackToMonster(Carta myCard, Carta herCard) {
        addMsgToLog(myCard.getNombre().toUpperCase()+" ha LUCHADO contra "+herCard.getNombre().toUpperCase());
        setNewMsgTrue();
        getScrollPane().remove();
    }

    public void announceResultOfAttack(Carta myCard,Carta herCard) {
        addMsgToLog(herCard.getNombre().toUpperCase()+" ha PERDIDO "+ ((Criatura) myCard).getAtaque()+ "HP");
        addMsgToLog("A "+(herCard.getNombre().toUpperCase()+" le QUEDAN "+ ((Criatura) herCard).getBufferVida()+ " HP"));
        addMsgToLog(myCard.getNombre().toUpperCase()+" ha PERDIDO "+ ((Criatura) herCard).getAtaque()+ " HP");
        addMsgToLog("A "+(myCard.getNombre().toUpperCase()+" le QUEDAN "+ ((Criatura) myCard).getBufferVida()+ " HP"));
        setNewMsgTrue();
        getScrollPane().remove();
    }

    public void announceCardDead(Carta card) {
        addMsgToLog(card.getNombre().toUpperCase()+" ha MUERTO y se ha ido al CEMENTERIO");
        setNewMsgTrue();
        getScrollPane().remove();
    }

    public void announceInvoquedCardDead(Carta myCard, Carta herCard) {
        addMsgToLog(myCard.getNombre().toUpperCase()+" ha DESTRUIDO la CARTA "+herCard.getNombre().toUpperCase());
        setNewMsgTrue();
        getScrollPane().remove();
    }

    public void announceCardMoved(Partida partida, Carta selectedCard) {
        addMsgToLog(partida.getJugador(0).getNombre().toUpperCase()+" ha movido a "+selectedCard.getNombre().toUpperCase()+" a la CASILLA "+(int)selectedCard.getLastPosition().x+","+(int)selectedCard.getLastPosition().y);
        setNewMsgTrue();
        getScrollPane().remove();
    }

    public void announceGiveUp(Partida partida) {
        addMsgToLog(partida.getJugador(0).getNombre().toUpperCase()+" se ha rendido.");
        addMsgToLog(partida.getJugador(0).getNombre().toUpperCase()+" ha perdido el duelo.");
        setNewMsgTrue();
        getScrollPane().remove();
    }

    public void announcePlayerPassedHisTurn(Partida partida) {
        addMsgToLog(partida.getJugador(0).getNombre().toUpperCase()+" ha finalizado su turno.");
        setNewMsgTrue();
        getScrollPane().remove();
    }
}
