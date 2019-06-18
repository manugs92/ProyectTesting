package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.managers.MyGdxGameAssetManager;
import com.mygdx.game.MyGdxGameScreen;
import com.mygdx.managers.MyGdxGameScreenManager;
import com.mygdx.model.Partida;


public class SummaryScreen extends MyGdxGameScreen {

    private final float POSITION_X_MYAVATAR = 172;
    private final float POSITION_Y_MYAVATAR = MyGdxGame.SCREEN_HEIGHT-250;
    private final float POSITION_X_RIVALAVATAR = MyGdxGame.SCREEN_WIDTH-300;
    private final float POSITION_Y_RIVALAVATAR = MyGdxGame.SCREEN_HEIGHT-250;
    private final String WINNER_TEXT = "GANADOR";
    private final String LOOSER_TEXT = "PERDEDOR";

    private SpriteBatch batch;
    private Skin skin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));
    private Partida partida;
    private Image myAvatar,rivalAvatar;

    public SummaryScreen(MyGdxGameScreenManager myGdxGameScreenManager, Partida partida) {
        super(myGdxGameScreenManager);
        this.partida=partida;
        stage.getActors().get(0).remove(); //Removemos el background del juego para poner otro nuevo.
        textureBgScreen = new Texture(Gdx.files.internal("backgrounds\\bg-resumeScreen.png"));
        textureBgScreen.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        stage.addActor(new Image(textureBgScreen));
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        font.setColor(255,255,255,255);

        if(partida.getWinnerId()==0) {
            myAvatar = partida.getJugador(0).getAvatar();
            rivalAvatar = partida.getJugador(1).getAvatar2();
            myAvatar.setPosition(POSITION_X_MYAVATAR,POSITION_Y_MYAVATAR);
            rivalAvatar.setPosition(POSITION_X_RIVALAVATAR,POSITION_Y_RIVALAVATAR);
        }else {
            myAvatar = partida.getJugador(0).getAvatar2();
            rivalAvatar = partida.getJugador(1).getAvatar();
            myAvatar.setPosition(POSITION_X_MYAVATAR,POSITION_Y_MYAVATAR);
            rivalAvatar.setPosition(POSITION_X_RIVALAVATAR,POSITION_Y_RIVALAVATAR);
        }
        TextButton backButton = new TextButton("Continuar",skin);
        backButton.setPosition(MyGdxGame.SCREEN_WIDTH-200,50);
        backButton.setColor(Color.BLUE);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float cx, float cy) {
                myGdxGameScreenManager.changeScreen(MyGdxGameScreenManager.MAIN_SCREEN);
            }
        });

        stage.addActor(myAvatar);
        stage.addActor(rivalAvatar);
        stage.addActor(backButton);
        stage.addActor(partida.getDuelLog().writeLogSumaryScreen(assetManager,batch));

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(cam.combined);
        stage.act(Math.min(delta, 1 / 30f));

        stage.draw();
        batch.begin();

        font.draw(batch,"RESUMEN DE PARTIDA",560,MyGdxGame.SCREEN_HEIGHT-50);
        font.draw(batch,"VIDAS RESTANTES",560,POSITION_Y_MYAVATAR+128);
        font.draw(batch,String.valueOf(partida.getJugador(0).getLives()),400,POSITION_Y_MYAVATAR+128);
        font.draw(batch,String.valueOf(partida.getJugador(1).getLives()),820,POSITION_Y_MYAVATAR+128);
        font.draw(batch,"MANA RESTANTE",560,POSITION_Y_MYAVATAR+88);
        font.draw(batch,String.valueOf(partida.getJugador(0).getInvocationOrbs()),400,POSITION_Y_MYAVATAR+88);
        font.draw(batch,String.valueOf(partida.getJugador(1).getInvocationOrbs()),820,POSITION_Y_MYAVATAR+88);

        font.draw(batch,"TURNOS REALIZADOS",560,POSITION_Y_MYAVATAR+48);
        font.draw(batch,String.valueOf(partida.getNumTurn()),400,POSITION_Y_MYAVATAR+48);
        font.draw(batch,String.valueOf(partida.getNumTurn()),820,POSITION_Y_MYAVATAR+48);

        font.draw(batch,"LOG DEL DUELO",560,MyGdxGame.SCREEN_HEIGHT-300);


        font.draw(batch,partida.getJugador(0).getNombre(),POSITION_X_MYAVATAR,POSITION_Y_MYAVATAR+150);
        font.draw(batch,partida.getJugador(1).getNombre(),POSITION_X_RIVALAVATAR,POSITION_Y_RIVALAVATAR+150);

        if(partida.getWinnerId()==0) {
            font.draw(batch, WINNER_TEXT, POSITION_X_MYAVATAR, POSITION_Y_MYAVATAR - 20);
            font.draw(batch, LOOSER_TEXT, POSITION_X_RIVALAVATAR, POSITION_Y_RIVALAVATAR - 20);
        }else {
            font.draw(batch,LOOSER_TEXT,POSITION_X_MYAVATAR,POSITION_Y_MYAVATAR-20);
            font.draw(batch,WINNER_TEXT,POSITION_X_RIVALAVATAR,POSITION_Y_RIVALAVATAR-20);
        }

        batch.end();
    }

    @Override
    public void dispose() { super.dispose(); }

}
