package ru.kev.fallingfruits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;


import java.util.Iterator;


public class GameScreen implements Screen {
    final FallingFruits game;
    OrthographicCamera camera;
    Rectangle basket;

    Texture dropBananas;
    Texture dropCherry;
    Texture dropApple;
    Texture basketImage;

    Vector3 touchPos;
    Array<Rectangle> fallingObjects;
    long lastDropTime;
    int dropGatchered;


    public GameScreen(final FallingFruits gam) {
        this.game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        touchPos = new Vector3();

        dropBananas = new Texture("bananas.png");
        dropCherry = new Texture("cherry.png");
        dropApple = new Texture("apple.png");

        basketImage = new Texture("basket.png");

        basket = new Rectangle();
        basket.x = 800 / 2 - 64 / 2;
        basket.y = 20;
        basket.width = 64;
        basket.height = 64;

        fallingObjects = new Array<Rectangle>();
        spawnFruits();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.draw(game.batch, "Score: " + dropGatchered, 0, 480);
        game.batch.draw(basketImage, basket.x, basket.y);

        Texture fall = new Texture("apple.png");

        for (Rectangle fruit : fallingObjects) {
            game.batch.draw(fall, fruit.x, fruit.y);
        }

        game.batch.end();

        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            basket.x = (int) (touchPos.x - 64 / 2);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) basket.x -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) basket.x += 200 * Gdx.graphics.getDeltaTime();

        if (basket.x < 0) basket.x = 0;
        if (basket.x > 800 - 64) basket.x = 800 - 64;

        if (TimeUtils.nanoTime() - lastDropTime > 1000000000 ) {
            spawnFruits();
        }

        Iterator<Rectangle> iter = fallingObjects.iterator();
        while (iter.hasNext()) {
            Rectangle fruit = iter.next();
            fruit.y -= 200 * Gdx.graphics.getDeltaTime();
            if (fruit.y + 64 < 0) {
                iter.remove();
                Gdx.input.vibrate(80);
            }
            if (fruit.overlaps(basket)) {
                dropGatchered++;
                iter.remove();
            }
        }
    }

    private void spawnFruits() {
        Rectangle fruit = new Rectangle();
        fruit.x = MathUtils.random(0, 800 - 64);
        fruit.y = 480;
        fruit.width = 64;
        fruit.height = 64;
        fallingObjects.add(fruit);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void dispose() {
        dropBananas.dispose();
        dropCherry.dispose();
        dropApple.dispose();
        basketImage.dispose();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }
}
