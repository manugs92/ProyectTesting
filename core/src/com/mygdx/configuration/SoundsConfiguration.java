package com.mygdx.configuration;

public class SoundsConfiguration {

    float GLOBAL_SOUND = 1f;
    float music = GLOBAL_SOUND;
    float efects = GLOBAL_SOUND;



    public SoundsConfiguration() {
    }


    public void update() {
        controlVolume();

    }

    private void controlVolume() {

        if (Controls.isMinusPressed()) {

            this.music = (music <= 0) ? 0 : music - 0.002f;
            this.efects = (efects <= 0) ? 0 : efects - 0.002f;


        } else if (Controls.isPluslePressed()) {
            this.music = (music >= 1) ? 1 : music + 0.002f;
            this.efects = (efects >= 1) ? 1 : efects + 0.002f;

        }


        //System.out.println(music + "aa");


    }

    public void mute(boolean mute) {

        if (mute) {
            this.GLOBAL_SOUND = 0;
            setVolume(GLOBAL_SOUND);
        } else {
            this.GLOBAL_SOUND = 1;
        }
    }

    public void setVolume(float volume) {
        music = volume;
        efects = volume;

    }


    public float getGLOBAL_SOUND() {
        return GLOBAL_SOUND;
    }

    public void setGLOBAL_SOUND(float GLOBAL_SOUND) {
        this.GLOBAL_SOUND = GLOBAL_SOUND;
    }

    public float getMusic() {
        return music;
    }

    public void setMusic(float music) {
        this.music = music;
    }

    public float getEfects() {
        return efects;
    }

    public void setEfects(float efects) {
        this.efects = efects;
    }




}
