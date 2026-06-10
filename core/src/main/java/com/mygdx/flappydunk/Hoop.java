package com.mygdx.flappydunk;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Un cerceau que le joueur doit traverser.
 * (x, y) = centre du cerceau / centre de l'ouverture.
 */
public class Hoop {
    // Dimensions (doivent correspondre a l'image hoop.png : trou r=85, exterieur r=115).
    public static final float OPENING_RADIUS = 85f;  // demi-hauteur de l'ouverture (le trou)
    public static final float OUTER_RADIUS = 115f;   // rayon exterieur (pour le dessin)

    private final float x;
    private final float y;
    private boolean scored = false;   // deja compte (passe ou rate) -> on ne le rejuge pas

    public Hoop(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public boolean isScored(){
        return scored;
    }
    public void setScored(boolean scored){
        this.scored = scored;
    }

    public void draw(SpriteBatch batch, Texture texture){
        // On dessine l'anneau centre sur (x, y).
        float size = OUTER_RADIUS * 2f;
        batch.draw(texture, x - OUTER_RADIUS, y - OUTER_RADIUS, size, size);
    }
}
