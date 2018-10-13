package ru.kev.fallingfruits;

import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Fruit extends Rectangle {
    Texture fruit;


    public Fruit() {
        this.x = MathUtils.random(0, 800 - 64);
        this.y = 480;
        this.width = 64;
        this.height = 64;
        this.fruit = fruit;

    }

    public Texture getFruit() {
        return fruit;
    }

    public void setFruit(Texture fruit) {
        this.fruit = fruit;
    }
}
