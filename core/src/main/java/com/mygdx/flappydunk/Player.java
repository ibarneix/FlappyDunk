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

    // Taille du joueur = taille de son image (dessine a sa taille native).
    public float getWidth(){
        return image.getWidth();
    }
    public float getHeight(){
        return image.getHeight();
    }
    // Centre du joueur (utile pour les collisions et le suivi camera).
    public float getCenterX(){
        return x + image.getWidth() / 2f;
    }
    public float getCenterY(){
        return y + image.getHeight() / 2f;
    }

    // Remet le joueur a son etat de depart (pour rejouer apres un game over).
    public void reset(){
        x = 0;
        y = 200;
        vx = 200;
        vy = 200;
    }



    // Logique uniquement : on met a jour la vitesse puis la position.
    public void update(float dt){
        vx += ax * dt;
        vy += ay * dt;
        x += vx * dt;
        y += vy * dt;
    }

    // Rendu uniquement : on dessine le joueur a sa position courante.
    // Separe de update() pour pouvoir placer la camera entre les deux
    // (joueur deplace -> camera recalee -> dessin) et eviter le tremblement.
    public void draw(SpriteBatch batch){
        batch.draw(image, x, y);
    }


}
