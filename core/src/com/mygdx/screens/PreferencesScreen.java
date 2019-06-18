package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.MyGdxGameScreen;
import com.mygdx.managers.MyGdxGameScreenManager;


/*
* Screen que cargamos al hacer click en preferencias.
* */
public class PreferencesScreen extends MyGdxGameScreen {

    private Slider slider;
    private Image backButton;
    private CheckBox cbActiveMusic;
    private CheckBox cbactiveSounds;
    private TextField textFieldPlayerName;

    public PreferencesScreen(MyGdxGameScreenManager myGdxGameScreenManagerR) {
        super(myGdxGameScreenManagerR);
        showConfigurationMenu(skin);
        addListenersToButtons();
    }

    @Override
     public void show() {
        // TODO Auto-generated method stub
        Label label = new Label("Configuración", myGdxGameScreenManager.skin);
        label.setBounds(20,stage.getHeight()-100,stage.getWidth(),100);
        stage.addActor(label);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void dispose() {
       super.dispose();
    }

    private void addListenersToButtons() {
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                myGdxGameConfigurationManager.soundsConfiguration.setGLOBAL_SOUND(slider.getValue());
            }
        });

        cbActiveMusic.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!cbActiveMusic.isChecked()) {
                    myGdxGameConfigurationManager.soundsConfiguration.mute(false);
                    myGdxGameConfigurationManager.soundsConfiguration.setVolume( 0.5f);
                    slider.setValue(0.5f);
                }else {
                    myGdxGameConfigurationManager.soundsConfiguration.mute(true);
                    myGdxGameConfigurationManager. soundsConfiguration.setVolume(0);
                    slider.setValue(0);
                }

            }
        });

        cbactiveSounds.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                myGdxGameConfigurationManager.soundsConfiguration.mute(true);
            }
        });


        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float cx, float cy) {
                myGdxGameScreenManager.changeScreen(MyGdxGameScreenManager.MAIN_SCREEN);
            }
        });


    }

    //Se tiene que cargar todos los datos del jugador, si existe en la BD.(Si no, se pedirá crear una cuenta).
    private void showConfigurationMenu(Skin skin) {
        assetManager.loadConfigurationScreenImages();
        assetManager.manager.finishLoading();
        textureBgScreen = assetManager.manager.get(assetManager.backgroundBlue,Texture.class);
        textureBgScreen.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        stage.addActor(new Image(textureBgScreen));
        //Configuration of widgets

        Table table = new Table();

        Label playerName = new Label("Nombre de jugador: ",skin);
        playerName.getStyle().fontColor.set(255,255,255,255);

        Label volumeValue = new Label("Volumen musica: ",skin);

        textFieldPlayerName = new TextField("",skin);
        if(null==null) {
            textFieldPlayerName.setText("PLAYER");
       }

        Label activeMusic = new Label("Musica activa: ",skin);

        cbActiveMusic = new CheckBox("",skin);
        if(myGdxGameScreenManager.myGdxGameConfigurationManager.soundsConfiguration.getGLOBAL_SOUND()==0) {
            cbActiveMusic.setChecked(true);
        }else {
            cbActiveMusic.setChecked(false);
        }

        Label activeSounds = new Label("Sonidos activos: ",skin);

        cbactiveSounds = new CheckBox("",skin);
        if( myGdxGameConfigurationManager.soundsConfiguration.getGLOBAL_SOUND()==0) {
            cbactiveSounds.setChecked(true);
        }else {
            cbactiveSounds.setChecked(false);
        }


        slider = new Slider(0,1,0.05f,false,skin);
        slider.setValue(myGdxGameConfigurationManager.soundsConfiguration.getGLOBAL_SOUND());
        slider.setSize(450,50);

        Texture textureBack = assetManager.manager.get(assetManager.back_arrow,Texture.class);
        backButton = new Image(textureBack);

        table.setFillParent(true);
        table.add(playerName);
        table.add(textFieldPlayerName);
        table.row().pad(10);
        table.add(volumeValue).align(Align.left);
        table.add(slider);
        table.row().pad(10);
        table.add(activeMusic).align(Align.left);
        table.add(cbActiveMusic);
        table.row().pad(10);
        table.add(activeSounds).align(Align.left);
        table.add(cbactiveSounds);
        table.row().pad(10);
        table.add(backButton).colspan(2).align(Align.right);

        stage.addActor(table);
    }




}
