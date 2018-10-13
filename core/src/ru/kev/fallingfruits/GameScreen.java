package ru.kev.fallingfruits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
    Texture background;

    Vector3 touchPos;
    Array<Rectangle> fallingFruits;
    Array<Texture> imgFruits;

    long lastDropTime;
    int dropGatchered;
    int lives = 5;


    public GameScreen(final FallingFruits gam) {

        background = new Texture("background.png");
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

        fallingFruits = new Array<Rectangle>();
        imgFruits = new Array<Texture>();

        imgFruits.add(dropBananas);
        imgFruits.add(dropCherry);

        spawnFruits();

    }

    @Override
    public void render(float delta) {
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(background, 0, 0);
        game.font.draw(game.batch, "Score: " + dropGatchered, 0, 480);
        game.font.draw(game.batch, "Lives: " + lives, 740, 480);
        game.batch.draw(basketImage, basket.x, basket.y);

        for (Rectangle fruit : fallingFruits) {
            int i = (int) (1 + Math.random() * 3);
            switch (i) {
                case 1:
                    game.batch.draw(dropApple, fruit.x, fruit.y);
                    break;
                case 2:
                    game.batch.draw(dropCherry, fruit.x, fruit.y);
                    break;
                case 3:
                    game.batch.draw(dropBananas, fruit.x, fruit.y);
            }

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

        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
            spawnFruits();
        }

        Iterator<Rectangle> iter = fallingFruits.iterator();
        while (iter.hasNext()) {
            Rectangle fruit = iter.next();
            fruit.y -= 150 * Gdx.graphics.getDeltaTime();
            if (fruit.y + 64 < 0) {
                iter.remove();
                Gdx.input.vibrate(80);
                if (lives > 0) {
                    lives--;
                } else {
                    game.setScreen(new GameOver(game));
                }
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
        fallingFruits.add(fruit);
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
