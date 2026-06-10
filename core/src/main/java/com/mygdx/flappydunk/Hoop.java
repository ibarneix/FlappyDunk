package com.mygdx.flappydunk;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Un anneau rouge (style "donut" vu de face, ellipse horizontale).
 * La texture est generee par code avec un Pixmap : pas besoin d'image.
 * Pour l'effet "on passe a travers", l'anneau est coupe en deux :
 *   - la moitie haute est dessinee DERRIERE la balle
 *   - la moitie basse est dessinee DEVANT la balle
 */
public class Hoop {

    // taille de l'anneau (en pixels du monde)
    public static final int WIDTH  = 240;
    public static final int HEIGHT = 110;

    // taille du trou au centre (en proportion de l'anneau)
    private static final float HOLE_RX = WIDTH  / 2f * 0.62f;
    private static final float HOLE_RY = HEIGHT / 2f * 0.62f;

    // texture partagee par tous les anneaux (on ne la genere qu'une fois)
    private static Texture ringTexture;
    private static TextureRegion backHalf;   // moitie haute -> derriere la balle
    private static TextureRegion frontHalf;   // moitie basse -> devant la balle

    private float x;   // coin bas-gauche
    private float y;
    private boolean scored;   // deja compte / evalue ?

    public Hoop(float x, float y){
        if (ringTexture == null) buildTexture();
        this.x = x;
        this.y = y;
        this.scored = false;
    }

    /** Genere une fois pour toutes la texture de l'anneau rouge. */
    private static void buildTexture(){
        Pixmap pm = new Pixmap(WIDTH, HEIGHT, Pixmap.Format.RGBA8888);
        pm.setBlending(Pixmap.Blending.None);
        // tout transparent au depart
        pm.setColor(0f, 0f, 0f, 0f);
        pm.fill();

        float cx = WIDTH  / 2f;
        float cy = HEIGHT / 2f;
        float outerRx = WIDTH  / 2f - 2f;
        float outerRy = HEIGHT / 2f - 2f;

        for (int py = 0; py < HEIGHT; py++){
            for (int px = 0; px < WIDTH; px++){
                float ox = (px - cx) / outerRx;
                float oy = (py - cy) / outerRy;
                float outer = ox * ox + oy * oy;        // <=1 -> dans l'ellipse exterieure

                float ix = (px - cx) / HOLE_RX;
                float iy = (py - cy) / HOLE_RY;
                float inner = ix * ix + iy * iy;        // >=1 -> hors du trou

                if (outer <= 1f && inner >= 1f){
                    // c'est un pixel de l'anneau -> on choisit la teinte de rouge
                    float r, g, b;
                    if (outer > 0.80f){
                        // bord exterieur : rouge fonce (donne du relief)
                        r = 0.55f; g = 0.04f; b = 0.04f;
                    } else if (inner < 1.35f){
                        // bord interieur (autour du trou) : rouge clair (reflet)
                        r = 0.98f; g = 0.32f; b = 0.30f;
                    } else {
                        // corps de l'anneau : rouge vif
                        r = 0.86f; g = 0.11f; b = 0.11f;
                    }
                    pm.setColor(r, g, b, 1f);
                    pm.drawPixel(px, py);
                }
            }
        }

        ringTexture = new Texture(pm);
        ringTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pm.dispose();

        // on decoupe la texture en deux moities (haut / bas).
        // Attention : dans un Pixmap, la ligne 0 est EN HAUT de l'image.
        backHalf  = new TextureRegion(ringTexture, 0, 0,          WIDTH, HEIGHT / 2);
        frontHalf = new TextureRegion(ringTexture, 0, HEIGHT / 2, WIDTH, HEIGHT / 2);
    }

    /** Moitie haute de l'anneau : a dessiner AVANT la balle. */
    public void drawBack(SpriteBatch batch){
        batch.draw(backHalf, x, y + HEIGHT / 2f);
    }

    /** Moitie basse de l'anneau : a dessiner APRES la balle. */
    public void drawFront(SpriteBatch batch){
        batch.draw(frontHalf, x, y);
    }

    /** Le centre du trou (la ou il faut viser). */
    public float getCenterX(){
        return x + WIDTH / 2f;
    }
    public float getCenterY(){
        return y + HEIGHT / 2f;
    }

    /** Bord droit de l'anneau (pour savoir s'il est sorti de l'ecran). */
    public float getRightX(){
        return x + WIDTH;
    }

    /** La balle (a la hauteur py) est-elle dans le trou ? */
    public boolean ballPasseDansLeTrou(float py, float rayonBalle){
        float hauteurTrou = HOLE_RY - rayonBalle * 0.5f;   // petite marge
        return Math.abs(py - getCenterY()) <= hauteurTrou;
    }

    public boolean isScored(){ return scored; }
    public void setScored(boolean v){ this.scored = v; }

    /** Replace l'anneau plus loin avec une nouvelle hauteur (recyclage). */
    public void respawn(float newX, float newY){
        this.x = newX;
        this.y = newY;
        this.scored = false;
    }

    public static void disposeTexture(){
        if (ringTexture != null){
            ringTexture.dispose();
            ringTexture = null;
        }
    }
}
