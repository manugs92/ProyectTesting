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
import gui.BackgroundScroll;

import java.util.ArrayList;

public class DuelLog {

    ArrayList<String> duelLog = new ArrayList<>();
    boolean newMsg;
    Texture textureBgScroll;
    Skin skin;
    ScrollPane scrollPane;

    public DuelLog(Skin skin) {
        this.skin=skin;
        newMsg=true;
        duelLog.add("Duelo empezado.");
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

    public void addMsgToLog(String msg){
        duelLog.add(msg);
    }

    public ScrollPane writeLog(MyGdxGameAssetManager assetManager, Batch batch) {
        assetManager.loadScrollLog();
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
        scrollPane.setHeight(MyGdxGame.SCREEN_HEIGHT);
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
}
