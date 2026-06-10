package com.mygdx.flappydunk;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    // ---- reglages du jeu (a ajuster librement) ----
    private static final float VITESSE_AVANCE = 170f;   // vitesse horizontale de la balle
    private static final float FORCE_SAUT     = 380f;   // poussee vers le haut a chaque saut
    private static final int   NB_ANNEAUX     = 6;      // nombre d'anneaux recycles
    private static final float ESPACEMENT     = 600f;   // distance en x entre deux anneaux
    private static final float PREMIER_ANNEAU = 500f;   // x du premier anneau
    private static final float REBOND         = 0.6f;   // elasticite du rebond (0=mou, 1=dur)

    private SpriteBatch batch;
    private Player player;
    private OrthographicCamera cam;

    private Hoop[] hoops;
    private int score;

    private float worldW;
    private float worldH;

    // arriere-plan (genere par code, defile en parallaxe)
    private Texture bgTexture;
    private TextureRegion bgRegion;
    private Matrix4 uiMatrix;   // projection fixee a l'ecran (fond + score)
    private BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        player = new Player(new Texture("flappy.png"));

        worldW = Gdx.graphics.getWidth();
        worldH = Gdx.graphics.getHeight();

        cam = new OrthographicCamera();
        cam.setToOrtho(false, worldW, worldH);

        // projection ecran pour le fond et le texte (origine en bas a gauche)
        uiMatrix = new Matrix4().setToOrtho2D(0, 0, worldW, worldH);
        font = new BitmapFont();
        font.getData().setScale(2f);

        buildBackground();

        hoops = new Hoop[NB_ANNEAUX];
        resetGame();
    }

    /** (Re)place la balle et les anneaux au depart. */
    private void resetGame(){
        score = 0;
        player.reset(0, worldH / 2f, VITESSE_AVANCE);
        for (int i = 0; i < NB_ANNEAUX; i++){
            float x = PREMIER_ANNEAU + i * ESPACEMENT;
            float y = hauteurAleatoire();
            if (hoops[i] == null) hoops[i] = new Hoop(x, y);
            else hoops[i].respawn(x, y);
        }
    }

    /** Hauteur (coin bas-gauche) aleatoire en gardant l'anneau a l'ecran. */
    private float hauteurAleatoire(){
        float marge = 90f;
        float min = marge;
        float max = worldH - Hoop.HEIGHT - marge;
        return min + (float) Math.random() * (max - min);
    }

    /** Genere un fond beige facon mur de briques, repetable. */
    private void buildBackground(){
        int tw = 128, th = 128;
        Pixmap pm = new Pixmap(tw, th, Pixmap.Format.RGBA8888);
        // fond beige
        pm.setColor(0.78f, 0.72f, 0.62f, 1f);
        pm.fill();
        // lignes de mortier (un peu plus foncees)
        pm.setColor(0.70f, 0.64f, 0.54f, 1f);
        for (int y = 0; y < th; y += 32){
            pm.drawLine(0, y, tw, y);
            // briques decalees une rangee sur deux
            int offset = ((y / 32) % 2 == 0) ? 0 : 32;
            for (int x = offset; x <= tw; x += 64){
                pm.drawLine(x, y, x, y + 32);
            }
        }
        bgTexture = new Texture(pm);
        bgTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        pm.dispose();
        bgRegion = new TextureRegion(bgTexture);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0.78f, 0.72f, 0.62f, 1f);

        // ---- entrees : saut au clavier (ESPACE) ou clic/tap ----
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.justTouched()){
            player.flap(FORCE_SAUT);
        }

        // ---- 1. physique (on garde la hauteur d'avant pour le dunk) ----
        float prevCenterY = player.getCenterY();
        player.update(dt);

        // ---- 2. la camera suit la balle sur l'axe x uniquement ----
        cam.position.x = player.getCenterX();
        cam.update();

        // ---- 3. rebonds / score / recyclage des anneaux ----
        updateHoops(prevCenterY);

        // ---- 4. fond (fixe a l'ecran, defile en parallaxe) ----
        batch.setProjectionMatrix(uiMatrix);
        batch.begin();
        float u  = (cam.position.x * 0.3f) / bgTexture.getWidth();
        float u2 = u + worldW / bgTexture.getWidth();
        float v2 = worldH / bgTexture.getHeight();
        bgRegion.setRegion(u, 0f, u2, v2);
        batch.draw(bgRegion, 0, 0, worldW, worldH);
        batch.end();

        // ---- 5. monde (anneaux + balle) avec l'effet de passage ----
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        // a) moities hautes des anneaux -> derriere la balle
        for (Hoop h : hoops) h.drawBack(batch);
        // b) la balle
        player.draw(batch);
        // c) moities basses des anneaux -> devant la balle
        for (Hoop h : hoops) h.drawFront(batch);
        batch.end();

        // ---- 6. interface (fixe a l'ecran) ----
        batch.setProjectionMatrix(uiMatrix);
        batch.begin();
        font.draw(batch, "Score : " + score, 20, worldH - 20);
        batch.end();
    }

    /**
     * Rebonds et score.
     * - La balle rebondit contre les bords des anneaux et contre le
     *   haut / bas de l'ecran (la partie ne s'arrete jamais).
     * - Le score augmente quand la balle plonge dans un anneau PAR LE HAUT
     *   (elle franchit la hauteur du trou en descendant, alignee avec lui).
     */
    private void updateHoops(float prevCenterY){
        // rayon de collision un peu plus petit que l'image (marges transparentes)
        float rayonBalle = player.getWidth() / 2f * 0.7f;
        float camGauche = cam.position.x - worldW / 2f;

        for (Hoop h : hoops){
            // rebond contre les deux cotes de l'anneau
            h.rebondir(player, rayonBalle, REBOND);

            // le dunk : on descend (avant au-dessus, maintenant au niveau ou en
            // dessous du centre du trou) en etant aligne avec le trou
            if (!h.isScored()
                    && prevCenterY > h.getCenterY()
                    && player.getCenterY() <= h.getCenterY()
                    && h.balleAuDessusDuTrou(player.getCenterX(), rayonBalle)){
                score++;
                h.setScored(true);
            }

            // anneau sorti de l'ecran a gauche -> on le replace plus loin
            if (h.getRightX() < camGauche){
                h.respawn(plusLoinX() + ESPACEMENT, hauteurAleatoire());
            }
        }

        // rebond contre le bas et le haut de l'ecran
        rebondBords(rayonBalle);
    }

    /** Empeche la balle de sortir par le haut ou le bas : elle rebondit. */
    private void rebondBords(float rayonBalle){
        float cy = player.getCenterY();
        if (cy - rayonBalle < 0){
            player.setCenter(player.getCenterX(), rayonBalle);
            if (player.getVy() < 0) player.setVy(-player.getVy() * REBOND);
        } else if (cy + rayonBalle > worldH){
            player.setCenter(player.getCenterX(), worldH - rayonBalle);
            if (player.getVy() > 0) player.setVy(-player.getVy() * REBOND);
        }
    }

    /** x du coin gauche de l'anneau le plus a droite. */
    private float plusLoinX(){
        float max = 0f;
        for (Hoop h : hoops){
            float gx = h.getCenterX() - Hoop.WIDTH / 2f;
            if (gx > max) max = gx;
        }
        return max;
    }

    @Override
    public void dispose() {
        batch.dispose();
        bgTexture.dispose();
        font.dispose();
        Hoop.disposeTexture();
    }
}
