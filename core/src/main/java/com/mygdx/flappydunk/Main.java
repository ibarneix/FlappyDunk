package com.mygdx.flappydunk;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    // Rayon de collision du joueur (un peu plus petit que son image pour etre indulgent).
    private static final float PLAYER_RADIUS = 32f;
    // Espacement horizontal entre deux cerceaux et position du premier.
    private static final float HOOP_SPACING = 450f;
    private static final float FIRST_HOOP_X = 500f;
    // Bornes verticales ou peut apparaitre le centre d'un cerceau.
    private static final float HOOP_MIN_Y = 120f;
    private static final float HOOP_MAX_Y = 360f;

    private SpriteBatch batch;
    private Player player;
    private OrthographicCamera cam;

    // Fond qui defile derriere le joueur.
    private Texture background;
    private TextureRegion bgRegion;

    // Cerceaux. La texture est decoupee en deux moitiees (arriere = haut, avant = bas)
    // pour que le joueur passe visuellement A TRAVERS l'anneau.
    private Texture hoopTexture;
    private TextureRegion hoopTop;
    private TextureRegion hoopBottom;
    private Array<Hoop> hoops;
    private float nextHoopX;

    // Score / etat de jeu / interface (HUD).
    private int score;
    private boolean gameOver;
    private OrthographicCamera hudCam;
    private BitmapFont font;


    @Override
    public void create() {
        batch = new SpriteBatch();
        player = new Player(new Texture("flappy.png"));
        player.reset();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        cam = new OrthographicCamera();
        // Y vers le haut : la gravité (ay negatif) fait tomber le joueur,
        // et un saut correspond a une velocite Y positive.
        cam.setToOrtho(false, w, h);

        // Camera fixe pour le HUD (le score reste a l'ecran, ne suit pas le monde).
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, w, h);
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(1.5f);

        // Fond : wrap en Repeat pour pouvoir le faire defiler a l'infini
        // (on fera glisser les coordonnees de texture, voir render()).
        background = new Texture("background.png");
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        bgRegion = new TextureRegion(background);

        // Cerceaux : on coupe l'image en deux (moitie haute = arriere, moitie basse = avant).
        hoopTexture = new Texture("hoop.png");
        int hw = hoopTexture.getWidth();
        int hh = hoopTexture.getHeight();
        hoopTop = new TextureRegion(hoopTexture, 0, 0, hw, hh / 2);
        hoopBottom = new TextureRegion(hoopTexture, 0, hh / 2, hw, hh / 2);
        hoops = new Array<Hoop>();
        nextHoopX = FIRST_HOOP_X;
    }

    @Override
    public void render() {
        ScreenUtils.clear(1f, 1f, 1f, 1f);

        float dt = Gdx.graphics.getDeltaTime();
        float prevCenterX = player.getCenterX();

        if (gameOver) {
            // En game over : SPACE relance une partie.
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                restart();
            }
        } else {
            // Saut : impulsion au moment ou on appuie (pas tant qu'on maintient).
            // On garde vx = 200 -> le joueur continue d'avancer vers la droite.
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                player.setVelocity(200.0f, 400.0f);
            }
            // 1) On met a jour la physique du joueur AVANT de placer la camera
            //    (sinon l'ecart variable vx*dt ferait vibrer le joueur).
            player.update(dt);
        }

        // 2) La camera suit le joueur sur l'axe X uniquement (figee si game over).
        cam.position.x = player.getCenterX();
        cam.update();

        if (!gameOver) {
            spawnHoopsAhead();
            handleHoops(prevCenterX);
            // Tombe sous l'ecran -> perdu.
            if (player.getCenterY() < 0f) {
                gameOver = true;
            }
        }

        // ===== Rendu du monde (avec la camera qui suit le joueur) =====
        batch.setProjectionMatrix(cam.combined);

        float w = cam.viewportWidth;
        float h = cam.viewportHeight;
        float left = cam.position.x - w / 2f;
        float bottom = cam.position.y - h / 2f;

        // Defilement du fond : on fait glisser les coordonnees de texture (u)
        // selon la position de la camera. parallax < 1 => le fond defile plus
        // lentement que le joueur -> effet de profondeur.
        float parallax = 0.3f;
        float u = (cam.position.x * parallax) / background.getWidth();
        float u2 = u + w / background.getWidth();
        bgRegion.setRegion(u, 0f, u2, 1f);

        batch.begin();
        batch.draw(bgRegion, left, bottom, w, h);      // le fond, derriere tout
        for (Hoop hoop : hoops) {
            hoop.drawBack(batch, hoopTop);             // bord arriere des cerceaux
        }
        player.draw(batch);                            // le joueur (passe a travers)
        for (Hoop hoop : hoops) {
            hoop.drawFront(batch, hoopBottom);         // bord avant des cerceaux, par-dessus
        }
        batch.end();

        // ===== Rendu du HUD (camera fixe) =====
        batch.setProjectionMatrix(hudCam.combined);
        batch.begin();
        font.draw(batch, "Score: " + score, 20f, h - 20f);
        if (gameOver) {
            font.draw(batch, "GAME OVER - SPACE pour rejouer", 20f, h / 2f);
        }
        batch.end();
    }

    /** Cree les cerceaux a venir devant la camera et supprime ceux laisses loin derriere. */
    private void spawnHoopsAhead() {
        while (nextHoopX < cam.position.x + 700f) {
            float y = MathUtils.random(HOOP_MIN_Y, HOOP_MAX_Y);
            hoops.add(new Hoop(nextHoopX, y));
            nextHoopX += HOOP_SPACING;
        }
        for (int i = hoops.size - 1; i >= 0; i--) {
            if (hoops.get(i).getX() < cam.position.x - 400f) {
                hoops.removeIndex(i);
            }
        }
    }

    /** Detecte le passage du joueur a travers les cerceaux : +1 si centre, game over sinon. */
    private void handleHoops(float prevCenterX) {
        float curCenterX = player.getCenterX();
        float pcy = player.getCenterY();
        for (Hoop hoop : hoops) {
            // Le joueur vient de franchir le plan du cerceau pendant cette frame ?
            if (!hoop.isScored() && prevCenterX < hoop.getX() && curCenterX >= hoop.getX()) {
                hoop.setScored(true);
                // Passe-t-il dans l'ouverture (assez centre verticalement) ?
                if (Math.abs(pcy - hoop.getY()) <= Hoop.OPENING_RY - PLAYER_RADIUS) {
                    score++;
                } else {
                    gameOver = true;   // a touche le bord du cerceau
                }
            }
        }
    }

    /** Reinitialise la partie. */
    private void restart() {
        player.reset();
        hoops.clear();
        nextHoopX = FIRST_HOOP_X;
        score = 0;
        gameOver = false;
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        hoopTexture.dispose();
        font.dispose();
    }
}
