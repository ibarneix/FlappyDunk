package com.mygdx.flappydunk;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
    private static final float REBOND         = 0.15f;  // elasticite du rebond (0=mou, 1=dur)
    private static final int   POINTS_NORMAL  = 1;      // dunk en touchant le bord
    private static final int   POINTS_PERFECT = 2;      // dunk sans toucher le bord

    private SpriteBatch batch;
    private Player player;
    private OrthographicCamera cam;

    private Hoop[] hoops;
    private int score;
    private boolean gameOver;
    private float perfectTimer;   // affiche "PARFAIT !" un court instant

    private float worldW;
    private float worldH;

    // arriere-plan (genere par code, defile en parallaxe)
    private Texture bgTexture;
    private TextureRegion bgRegion;
    private Matrix4 uiMatrix;   // projection fixee a l'ecran (fond + score)
    private BitmapFont font;
    private GlyphLayout layout;

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
        layout = new GlyphLayout();

        buildBackground();

        hoops = new Hoop[NB_ANNEAUX];
        resetGame();
    }

    /** (Re)place la balle et les anneaux au depart. */
    private void resetGame(){
        score = 0;
        gameOver = false;
        perfectTimer = 0;
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

        boolean action = Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.justTouched();
        if (perfectTimer > 0) perfectTimer -= dt;

        if (gameOver){
            // partie arretee : un appui relance une nouvelle partie
            if (action) resetGame();
        } else {
            // ---- entrees : saut au clavier (ESPACE) ou clic/tap ----
            if (action) player.flap(FORCE_SAUT);

            // ---- 1. physique (on garde la hauteur d'avant pour le dunk) ----
            float prevCenterY = player.getCenterY();
            player.update(dt);

            // ---- 2. la camera suit la balle sur l'axe x uniquement ----
            cam.position.x = player.getCenterX();
            cam.update();

            // ---- 3. rebonds / score / game over / recyclage des anneaux ----
            updateHoops(prevCenterY);
        }

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
        if (perfectTimer > 0 && !gameOver){
            drawCentre("PARFAIT !", worldH - 80);
        }
        if (gameOver){
            drawCentre("GAME OVER", worldH / 2f + 60);
            drawCentre("Score : " + score, worldH / 2f);
            drawCentre("ESPACE ou clic pour rejouer", worldH / 2f - 60);
        }
        batch.end();
    }

    /** Dessine un texte centre horizontalement a la hauteur donnee. */
    private void drawCentre(String texte, float y){
        layout.setText(font, texte);
        font.draw(batch, layout, (worldW - layout.width) / 2f, y);
    }

    /**
     * Rebonds, score et game over.
     * - La balle se cogne contre les bords des anneaux (et roule).
     * - Dunk reussi (descente alignee avec le trou) : +1 point, ou +2 en
     *   PERFECT si la balle n'a pas touche le bord de l'anneau.
     * - Rater un anneau (depasser le trou sans plonger) : game over.
     * - Toucher le sol ou le plafond : game over.
     */
    private void updateHoops(float prevCenterY){
        // rayon de collision plus petit que l'image (marges transparentes + hitbox indulgente)
        float rayonBalle = player.getWidth() / 2f * 0.55f;
        float camGauche = cam.position.x - worldW / 2f;

        // sol ou plafond touche -> game over
        float cy = player.getCenterY();
        if (cy - rayonBalle <= 0 || cy + rayonBalle >= worldH){
            gameOver = true;
            return;
        }

        for (Hoop h : hoops){
            // la balle se cogne contre les deux cotes de l'anneau (et roule)
            h.rebondir(player, rayonBalle, REBOND);

            // le dunk : on descend (avant au-dessus, maintenant au niveau ou en
            // dessous du centre du trou) en etant aligne avec le trou
            if (!h.isScored()
                    && prevCenterY > h.getCenterY()
                    && player.getCenterY() <= h.getCenterY()
                    && h.balleAuDessusDuTrou(player.getCenterX(), rayonBalle)){
                h.setScored(true);
                if (h.isTouched()){
                    score += POINTS_NORMAL;
                } else {
                    score += POINTS_PERFECT;   // dunk sans toucher le bord
                    perfectTimer = 0.9f;
                }
            }

            // la balle a depasse le trou sans plonger dedans -> rate -> game over
            if (!h.isScored() && h.trouDepasse(player.getCenterX())){
                gameOver = true;
                return;
            }

            // anneau sorti de l'ecran a gauche -> on le replace plus loin
            if (h.getRightX() < camGauche){
                h.respawn(plusLoinX() + ESPACEMENT, hauteurAleatoire());
            }
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
