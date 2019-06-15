package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.configuration.SoundsConfiguration;
import com.mygdx.game.MyGdxGameAssetManager;
import com.mygdx.game.MyGdxGameScreen;


/*
* Screen que cargamos al hacer click en preferencias.
* */
public class PreferencesScreen extends MyGdxGameScreen {


    private Stage stage;

    private Slider slider;
    private Image backButton;
    private CheckBox cbActiveMusic;
    private CheckBox cbactiveSounds;
    private TextField textFieldPlayerName;
    SpriteBatch spriteBatch;
    MyGdxGameAssetManager assetManager ;
    SoundsConfiguration soundsConfiguration;


    public PreferencesScreen(ScreenManager screenManagerR) {
        super(screenManagerR);

        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        assetManager=screenManagerR.asset;
        assetManager.loadConfigurationScreenImages();
        assetManager.manager.finishLoading();

        showConfigurationMenu(screenManagerR.skin);
        addListenersToButtons();
        this.soundsConfiguration=screenManagerR.sounds;
    }

    @Override
     public void show() {

        stage.setViewport(screenManager.fitViewport);
        // TODO Auto-generated method stub
        Label label = new Label("Configuración",screenManager.skin);
        label.setBounds(20,stage.getHeight()-100,stage.getWidth(),100);
        stage.addActor(label);


        spriteBatch = new SpriteBatch();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        stage.getViewport().update(width, height, true);

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
    }

    private void addListenersToButtons() {
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundsConfiguration.setGLOBAL_SOUND(slider.getValue());
            }
        });

        cbActiveMusic.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!cbActiveMusic.isChecked()) {
                    soundsConfiguration.mute(false);
                    soundsConfiguration.setVolume( 0.5f);
                    slider.setValue(0.5f);
                }else {
                    soundsConfiguration.mute(true);
                    soundsConfiguration.setVolume(0);
                    slider.setValue(0);
                }

            }
        });

        cbactiveSounds.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundsConfiguration.mute(true);
            }
        });


        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float cx, float cy) {
                screenManager.changeScreen(ScreenManager.MAIN_SCREEN);
            }
        });


    }

    private void showConfigurationMenu(Skin skin) {
        textureBgScreen = assetManager.manager.get(assetManager.backgroundBlue,Texture.class);
        textureBgScreen.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        stage.addActor(new Image(textureBgScreen));
        //Configuration of widgets

        Table table = new Table();
        //BackgroundTable backgroundTable = new BackgroundTable("black.png");

        Label playerName = new Label("Nombre de jugador: ",skin);
        playerName.getStyle().fontColor.set(255,255,255,255);

        Label volumeValue = new Label("Volumen musica: ",skin);

//        textFieldPlayerName = new TextField("",skin);
//        if(.getPlayerName()!=null) {
//            textFieldPlayerName.setText(.getPlayerName());
//        }

        Label activeMusic = new Label("Musica activa: ",skin);

        cbActiveMusic = new CheckBox("",skin);
        if(screenManager.sounds.getGLOBAL_SOUND()==0) {
            cbActiveMusic.setChecked(true);
        }else {
            cbActiveMusic.setChecked(false);
        }

        Label activeSounds = new Label("Sonidos activos: ",skin);

        cbactiveSounds = new CheckBox("",skin);
        if(screenManager.sounds.getGLOBAL_SOUND()==0) {
            cbactiveSounds.setChecked(true);
        }else {
            cbactiveSounds.setChecked(false);
        }


        slider = new Slider(0,1,0.05f,false,skin);
        slider.setValue(screenManager.sounds.getGLOBAL_SOUND());
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
