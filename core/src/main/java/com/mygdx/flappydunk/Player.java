package com.mygdx.flappydunk;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {
    private float x;
    private float y;
    private float vx;
    private float vy;
    private float ax;
    private float ay;
    private Texture image;

    public Player(Texture image){
        //position
        this.x=0;
        this.y=0;
        //velocité
        this.vx=0;
        this.vy=0;
        //acceleration
        this.ax = 0.0f;
        this.ay = -9.8f * 100;
        //image
        this.image=image;
    }

    public void setVelocity(float vx, float vy){
        this.vx = vx;
        this.vy = vy;
    }
    public void addVelocity(float vx, float vy){
        this.vx += vx;
        this.vy += vy;
    }
    public float getX(){
       return x;
    }
    public float getY(){
        return y;
    }



    public void update(float dt, SpriteBatch batch){


        vx += ax * dt;
        vy += ay * dt;
        x+=vx*dt;
        y+=vy*dt;

        batch.draw(image, x, y);



    }


}
