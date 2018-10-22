package ru.kev.fallingfruits;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Danger extends Rectangle {
    private Texture danger;


    public Danger() {
        this.x = MathUtils.random(0, 800 - 64);
        this.y = 480;
        this.width = 64;
        this.height = 64;
        this.danger = setDanger();
    }

    public Texture getDanger() {
        return danger;
    }

    public Texture setDanger() {
        int i = (int) (1 + Math.random() * 2);
        switch (i) {
            case 1:
                danger = new Texture("boot.png");
                break;
            case 2:
                danger = new Texture("beetle.png");
                break;
        }
        return danger;
    }

}
