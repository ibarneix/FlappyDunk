package com.mygdx.flappydunk;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Un cerceau horizontal (ellipse) que le joueur doit traverser.
 * (x, y) = centre du cerceau / centre de l'ouverture.
 *
 * Pour donner l'illusion que le joueur passe A TRAVERS l'anneau, on dessine
 * le cerceau en deux temps :
 *   - drawBack()  : la moitie arriere (le bord du fond), AVANT le joueur
 *   - drawFront() : la moitie avant (le bord de devant), APRES le joueur
 */
public class Hoop {
    // Dimensions (doivent correspondre a l'image hoop.png : 280 x 172).
    public static final float OUTER_RX = 140f;   // demi-largeur de l'ellipse exterieure
    public static final float OUTER_RY = 86f;    // demi-hauteur de l'ellipse exterieure
    public static final float OPENING_RY = 64f;  // demi-hauteur du trou (pour la collision)

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

    // Moitie arriere (haut de l'ellipse) -> dessinee derriere le joueur.
    public void drawBack(SpriteBatch batch, TextureRegion topHalf){
        batch.draw(topHalf, x - OUTER_RX, y, OUTER_RX * 2f, OUTER_RY);
    }

    // Moitie avant (bas de l'ellipse) -> dessinee devant le joueur.
    public void drawFront(SpriteBatch batch, TextureRegion bottomHalf){
        batch.draw(bottomHalf, x - OUTER_RX, y - OUTER_RY, OUTER_RX * 2f, OUTER_RY);
    }
}
