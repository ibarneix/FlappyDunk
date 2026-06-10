package com.mygdx.flappydunk;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private OrthographicCamera cam;


    @Override
    public void create() {
        batch = new SpriteBatch();
        player = new Player(new Texture("flappy.png"));
        player.setVelocity(200,200);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        cam = new OrthographicCamera();
        cam.setToOrtho(true, w, h);


    }

    @Override
    public void render() {
        ScreenUtils.clear(1f, 1f, 1f, 1f);

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            player.setVelocity(200.0f, 300.0f);
        }

        cam.position.x = player.getX();
        cam.position.y = player.getY();
        cam.update();

        batch.begin();
        player.update(Gdx.graphics.getDeltaTime(), batch);




        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        //image.dispose();
    }
}

