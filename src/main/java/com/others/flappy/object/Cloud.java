package com.others.flappy.object;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 云彩类
 */
public class Cloud {

    // 云彩图片
    private BufferedImage img;
    // 云彩速度
    private int speed;
    // 云彩位置
    private int x, y;

    public Cloud(BufferedImage img, int speed, int x, int y) {
        this.img = img;
        this.speed = speed;
        this.x = x;
        this.y = y;
    }

    /**
     * 画出云彩
     */
    public void draw(Graphics g) {
        x -= speed;
        g.drawImage(img, x, y, null);
    }

    /**
     * 用于判断云彩是否飞出屏幕外
     * 如果飞出，则需要清除容器中的云彩
     */
    public boolean isOutOfFrame() {
        if (x < -200) {
            return true;
        }
        return false;
    }
}
