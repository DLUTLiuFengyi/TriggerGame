package com.trigger.flappy.object;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 游戏中需要绘制的物体的基类
 *
 * author: lfy
 */
public class ObjectBase {

    BufferedImage img;
    int x, y;
    int width, height;
    int speed;
    Rectangle rect;

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public ObjectBase() {
        rect = new Rectangle(width, height);
    }

    public ObjectBase(BufferedImage img, int x, int y, int width, int height, int speed) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        rect = new Rectangle(width, height);
    }

    public void drawSelf(Graphics g) {
        g.drawImage(img, x, y, width, height, null);
    }

}
