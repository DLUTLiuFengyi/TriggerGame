package com.trigger.flappy.object;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 飞机对象
 *
 * author: lfy
 */
public class Fighter extends ObjectBase {

    public Fighter(BufferedImage img, int speed, int x, int y) {
        this.img = img;
        this.speed = speed;
        this.x = x;
        this.y = y;
    }

    /**
     * 画出飞机
     */
    @Override
    public void drawSelf(Graphics g) {
        x -= speed;
        g.drawImage(img, x, y, null);
    }

    /**
     * 用于判断飞机是否飞出屏幕外
     * 如果飞出，则需要清除容器中的对象
     */
    public boolean isOutOfFrame() {
        if (x < -200) {
            return true;
        }
        return false;
    }
}
