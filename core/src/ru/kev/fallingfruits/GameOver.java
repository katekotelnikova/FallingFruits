package ru.kev.fallingfruits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public class GameOver implements Screen {
    final FallingFruits game;
    OrthographicCamera camera;
    Texture background;

    public GameOver(final FallingFruits gam){
        game = gam;
        background = new Texture("bg_menu.jpg");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(background, 0, 0);
        game.font.draw(game.batch, "Your score: " + GameScreen.getDropGatchered(),  100, 180);
        game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
        game.batch.end();

        if(Gdx.input.isTouched()){

            game.setScreen(new GameScreen(game));
            dispose();
        }

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
    public void dispose() {

    }
}
