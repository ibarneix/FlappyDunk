package com.mygdx.flappydunk;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Couche de rendu / entrees. Toute la logique est dans {@link GameWorld}.
 */
public class Main extends ApplicationAdapter {
    // La camera est legerement en avance sur le joueur pour voir venir les cerceaux.
    private static final float CAMERA_X_OFFSET = 120f;
    private static final float BG_PARALLAX = 0.3f;
    private static final float WATER_TOP = GameWorld.GROUND_Y;

    private SpriteBatch batch;
    private OrthographicCamera cam;     // suit le joueur
    private OrthographicCamera hudCam;  // fixe (interface)
    private BitmapFont font;
    private final GlyphLayout layout = new GlyphLayout();

    private GameWorld world;

    // Assets
    private Texture flappyTex;
    private TextureRegion flappyRegion;
    private Texture bgTex;
    private TextureRegion bgRegion;
    private Texture hoopTex;
    private TextureRegion hoopTop;
    private TextureRegion hoopBottom;
    private Texture pixel;              // 1x1 blanc, pour dessiner des rectangles

    private Preferences prefs;
    private int best;

    @Override
    public void create() {
        batch = new SpriteBatch();
        world = new GameWorld();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        cam = new OrthographicCamera();
        cam.setToOrtho(false, w, h);
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, w, h);

        font = new BitmapFont();
        font.getData().setScale(1.4f);

        flappyTex = new Texture("flappy.png");
        flappyRegion = new TextureRegion(flappyTex);

        bgTex = new Texture("background.png");
        bgTex.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        bgRegion = new TextureRegion(bgTex);

        // Cerceau decoupe en deux : moitie haute = bord arriere, moitie basse = bord avant.
        hoopTex = new Texture("hoop.png");
        int hw = hoopTex.getWidth();
        int hh = hoopTex.getHeight();
        hoopTop = new TextureRegion(hoopTex, 0, 0, hw, hh / 2);
        hoopBottom = new TextureRegion(hoopTex, 0, hh / 2, hw, hh / 2);

        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        pixel = new Texture(pm);
        pm.dispose();

        prefs = Gdx.app.getPreferences("flappydunk");
        best = prefs.getInteger("best", 0);
    }

    @Override
    public void render() {
        // --- Entrees ---
        boolean tap = Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.justTouched();
        if (tap) {
            world.flap();
        }

        // --- Logique ---
        GameWorld.State before = world.getState();
        world.update(Gdx.graphics.getDeltaTime());
        if (world.getState() == GameWorld.State.GAME_OVER && before != GameWorld.State.GAME_OVER) {
            if (world.getScore() > best) {
                best = world.getScore();
                prefs.putInteger("best", best);
                prefs.flush();
            }
        }

        // --- Camera suit le joueur (axe X), legerement en avance ---
        Player player = world.getPlayer();
        cam.position.x = player.getCenterX() + CAMERA_X_OFFSET;
        cam.update();

        ScreenUtils.clear(0.55f, 0.78f, 0.92f, 1f);

        float w = cam.viewportWidth;
        float h = cam.viewportHeight;
        float left = cam.position.x - w / 2f;
        float bottom = cam.position.y - h / 2f;

        // ===== Monde =====
        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        // Fond defilant (parallaxe via glissement des coordonnees de texture).
        float u = (cam.position.x * BG_PARALLAX) / bgTex.getWidth();
        float u2 = u + w / bgTex.getWidth();
        bgRegion.setRegion(u, 0f, u2, 1f);
        batch.draw(bgRegion, left, bottom, w, h);

        // Eau au fond.
        batch.setColor(0.25f, 0.6f, 0.85f, 1f);
        batch.draw(pixel, left, bottom, w, WATER_TOP - bottom);
        batch.setColor(0.8f, 0.92f, 1f, 1f);
        batch.draw(pixel, left, WATER_TOP - 4f, w, 4f);
        batch.setColor(Color.WHITE);

        // Bord arriere des cerceaux (derriere le joueur).
        for (Hoop hoop : world.getHoops()) {
            batch.draw(hoopTop, hoop.getX() - Hoop.OUTER_RX, hoop.getY(),
                    Hoop.OUTER_RX * 2f, Hoop.OUTER_RY);
        }

        // Joueur, incline selon sa vitesse verticale (juice).
        float angle = MathUtils.clamp(player.getVy() * 0.05f, -45f, 30f);
        batch.draw(flappyRegion,
                player.getX(), player.getY(),
                Player.WIDTH / 2f, Player.HEIGHT / 2f,
                Player.WIDTH, Player.HEIGHT,
                1f, 1f, angle);

        // Bord avant des cerceaux (devant le joueur -> effet "a travers").
        for (Hoop hoop : world.getHoops()) {
            batch.draw(hoopBottom, hoop.getX() - Hoop.OUTER_RX, hoop.getY() - Hoop.OUTER_RY,
                    Hoop.OUTER_RX * 2f, Hoop.OUTER_RY);
        }

        batch.end();

        // ===== Interface (HUD) =====
        batch.setProjectionMatrix(hudCam.combined);
        batch.begin();
        drawHud(w, h);
        batch.end();
    }

    private void drawHud(float w, float h) {
        // Score (et swish eventuel).
        font.setColor(0.12f, 0.16f, 0.22f, 1f);
        font.draw(batch, "Score: " + world.getScore(), 20f, h - 18f);
        font.draw(batch, "Best: " + best, 20f, h - 50f);
        if (world.isSwish()) {
            font.setColor(0.95f, 0.55f, 0.1f, 1f);
            drawCentered("SWISH ! +2", w / 2f, h - 60f);
        }

        switch (world.getState()) {
            case READY:
                font.setColor(0.12f, 0.16f, 0.22f, 1f);
                drawCentered("ESPACE / Clic pour jouer", w / 2f, h / 2f);
                break;
            case GAME_OVER:
                // Voile sombre.
                batch.setColor(0f, 0f, 0f, 0.45f);
                batch.draw(pixel, 0f, 0f, w, h);
                batch.setColor(Color.WHITE);
                font.setColor(Color.WHITE);
                drawCentered("GAME OVER", w / 2f, h / 2f + 50f);
                drawCentered("Score : " + world.getScore() + "    Best : " + best, w / 2f, h / 2f + 10f);
                drawCentered("ESPACE / Clic pour rejouer", w / 2f, h / 2f - 35f);
                break;
            default:
                break;
        }
    }

    private void drawCentered(String text, float cx, float cy) {
        layout.setText(font, text);
        font.draw(batch, layout, cx - layout.width / 2f, cy + layout.height / 2f);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        flappyTex.dispose();
        bgTex.dispose();
        hoopTex.dispose();
        pixel.dispose();
    }
}
