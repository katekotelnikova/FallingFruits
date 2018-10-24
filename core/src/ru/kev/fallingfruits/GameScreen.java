package ru.kev.fallingfruits;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;


import java.util.Iterator;


public class GameScreen implements Screen {
    final FallingFruits game;
    OrthographicCamera camera;
    private Rectangle basket;

    private Texture basketImage;
    private Texture background;

    private Vector3 touchPos;
    private Array<Fruit> fallingFruits;
    private Array<Danger> fallingDangerous;

    private long lastDropTime;
    private long lastDropDangTime;
    private static int dropGatchered;
    private int lives = 5;


    public GameScreen(final FallingFruits gam) {
        background = new Texture("bg_menu.jpg");

        this.game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        touchPos = new Vector3();
        dropGatchered = 0;

        basketImage = new Texture("basket.png");

        basket = new Rectangle();
        basket.x = 800 / 2 - 64 / 2;
        basket.y = 20;
        basket.width = 64;
        basket.height = 64;

        fallingFruits = new Array<Fruit>();
        fallingDangerous = new Array<Danger>();

        spawnFruits();
        spawnDanger();

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

        for (Fruit fruit : fallingFruits) {
            game.batch.draw(fruit.getFruit(), fruit.x, fruit.y);
        }

        for (Danger danger : fallingDangerous) {
            game.batch.draw(danger.getDanger(), danger.x, danger.y);
        }

        game.batch.end();

        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            basket.x = (int) (touchPos.x - 64 / 2);
        }

        if (basket.x < 0) basket.x = 0;
        if (basket.x > 800 - 64) basket.x = 800 - 64;

        if (TimeUtils.millis() - lastDropTime > 1500) {
            spawnFruits();
        }

        if (TimeUtils.millis() - lastDropDangTime > 5000) {
            spawnDanger();
        }

        Iterator<Fruit> iter = fallingFruits.iterator();
        while (iter.hasNext()) {
            Fruit fruit = iter.next();
            if (dropGatchered <= 10) {
                fruit.y -= 200 * Gdx.graphics.getDeltaTime();
            } else {
                fruit.y -= 250 * Gdx.graphics.getDeltaTime();
            }
            if (fruit.y + 64 < 0) {
                iter.remove();
                Gdx.input.vibrate(80);
                if (lives >= 1) {
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

        Iterator<Danger> iter2 = fallingDangerous.iterator();
        while (iter2.hasNext()) {
            Danger danger = iter2.next();
            if (dropGatchered <= 10) {
                danger.y -= 150 * Gdx.graphics.getDeltaTime();
            } else {
                danger.y -= 200 * Gdx.graphics.getDeltaTime();
            }
            if (danger.y + 64 < 0) {
                iter2.remove();
            }
            if (danger.overlaps(basket)) {
                Gdx.input.vibrate(80);
                if (lives >= 1) {
                    lives--;
                } else {
                    game.setScreen(new GameOver(game));
                }
                iter2.remove();
            }
        }
    }

    private void spawnFruits() {
        Fruit fruit = new Fruit();
        fallingFruits.add(fruit);
        lastDropTime = TimeUtils.millis();
    }

    private void spawnDanger() {
        Danger danger = new Danger();
        fallingDangerous.add(danger);
        lastDropDangTime = TimeUtils.millis();
    }

    public static int getDropGatchered() {
        return dropGatchered;
    }

    @Override
    public void dispose() {
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
