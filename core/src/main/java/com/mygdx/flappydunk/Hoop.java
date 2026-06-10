package com.mygdx.flappydunk;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Un anneau rouge (style "donut" vu de face, ellipse horizontale).
 * La texture est generee par code avec un Pixmap : pas besoin d'image.
 *
 * La balle doit le traverser VERTICALEMENT, de haut en bas (le dunk).
 * Pour l'effet visuel "on passe a travers", l'anneau est coupe en deux :
 *   - la moitie haute (l'arriere du cerceau) est dessinee DERRIERE la balle
 *   - la moitie basse (l'avant du cerceau) est dessinee DEVANT la balle
 */
public class Hoop {

    // taille de l'anneau a l'ecran (en pixels du monde)
    public static final int WIDTH  = 240;
    public static final int HEIGHT = 110;

    // la texture est generee en 2x plus grand puis affichee reduite :
    // avec le filtre lineaire, ca lisse les bords (moins pixelise)
    private static final int SCALE = 2;

    // demi-taille du trou au centre (en pixels du monde)
    private static final float HOLE_RX = WIDTH  / 2f * 0.62f;
    private static final float HOLE_RY = HEIGHT / 2f * 0.62f;

    // texture partagee par tous les anneaux (on ne la genere qu'une fois)
    private static Texture ringTexture;
    private static TextureRegion backHalf;    // moitie haute -> derriere la balle
    private static TextureRegion frontHalf;   // moitie basse -> devant la balle

    private float x;   // coin bas-gauche
    private float y;
    private boolean scored;   // dunk deja compte ?

    public Hoop(float x, float y){
        if (ringTexture == null) buildTexture();
        this.x = x;
        this.y = y;
        this.scored = false;
    }

    /** Genere une fois pour toutes la texture de l'anneau rouge. */
    private static void buildTexture(){
        int tw = WIDTH * SCALE;
        int th = HEIGHT * SCALE;
        Pixmap pm = new Pixmap(tw, th, Pixmap.Format.RGBA8888);
        pm.setBlending(Pixmap.Blending.None);
        // tout transparent au depart
        pm.setColor(0f, 0f, 0f, 0f);
        pm.fill();

        float cx = tw / 2f;
        float cy = th / 2f;
        float outerRx = tw / 2f - 2f;
        float outerRy = th / 2f - 2f;
        float holeRx = HOLE_RX * SCALE;
        float holeRy = HOLE_RY * SCALE;

        for (int py = 0; py < th; py++){
            for (int px = 0; px < tw; px++){
                float ox = (px - cx) / outerRx;
                float oy = (py - cy) / outerRy;
                float outer = ox * ox + oy * oy;        // <=1 -> dans l'ellipse exterieure

                float ix = (px - cx) / holeRx;
                float iy = (py - cy) / holeRy;
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
        backHalf  = new TextureRegion(ringTexture, 0, 0,      tw, th / 2);
        frontHalf = new TextureRegion(ringTexture, 0, th / 2, tw, th / 2);
    }

    /** Moitie haute de l'anneau : a dessiner AVANT la balle. */
    public void drawBack(SpriteBatch batch){
        batch.draw(backHalf, x, y + HEIGHT / 2f, WIDTH, HEIGHT / 2f);
    }

    /** Moitie basse de l'anneau : a dessiner APRES la balle. */
    public void drawFront(SpriteBatch batch){
        batch.draw(frontHalf, x, y, WIDTH, HEIGHT / 2f);
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

    /**
     * La balle est-elle alignee avec le trou (horizontalement) ?
     * C'est la condition pour pouvoir plonger dedans.
     */
    public boolean balleAuDessusDuTrou(float ballCenterX, float rayonBalle){
        return Math.abs(ballCenterX - getCenterX()) <= HOLE_RX - rayonBalle * 0.4f;
    }

    /**
     * La balle touche-t-elle la matiere de l'anneau (les bords rouges) ?
     * On approxime les deux cotes du cerceau par deux rectangles :
     * tout l'anneau SAUF la bande centrale du trou.
     */
    public boolean toucheLeBord(float bx, float by, float rayonBalle){
        float bordGaucheFin = getCenterX() - HOLE_RX;   // fin du cote gauche
        float bordDroitDebut = getCenterX() + HOLE_RX;  // debut du cote droit
        return cercleToucheRectangle(bx, by, rayonBalle, x, y, bordGaucheFin - x, HEIGHT)
            || cercleToucheRectangle(bx, by, rayonBalle, bordDroitDebut, y, getRightX() - bordDroitDebut, HEIGHT);
    }

    /** Collision cercle / rectangle classique : on cherche le point du
     *  rectangle le plus proche du centre du cercle. */
    private static boolean cercleToucheRectangle(float cx, float cy, float r,
                                                 float rx, float ry, float rw, float rh){
        float closestX = Math.max(rx, Math.min(cx, rx + rw));
        float closestY = Math.max(ry, Math.min(cy, ry + rh));
        float dx = cx - closestX;
        float dy = cy - closestY;
        return dx * dx + dy * dy <= r * r;
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
