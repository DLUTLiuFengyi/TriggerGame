package com.trigger.flappy.object;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 光线
 *
 * author: lfy
 */
public class Beam extends ObjectBase {

    @Override
    public BufferedImage getImg() {
        return super.getImg();
    }

    public Beam() {
        super();
    }

    public Beam(BufferedImage img, int x, int y, int width, int height, int speed) {
        super(img, x, y, width, height, speed);
    }

    @Override
    public void drawSelf(Graphics g) {
        super.drawSelf(g);
    }
}
