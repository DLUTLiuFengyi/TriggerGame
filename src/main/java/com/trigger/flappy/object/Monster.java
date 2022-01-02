package com.trigger.flappy.object;

import com.others.flappy.util.Constant;
import com.image.GameUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 障碍物类（怪兽）
 */
public class Monster {

    private static BufferedImage[] imgs;

    static {
        final int COUNT = 3;
        imgs = new BufferedImage[COUNT];
        for (int i=0; i<COUNT; i++) {
            imgs[i] = GameUtil.loadBufferedImage(Constant.MONSTER_IMG[i]);
        }
    }

    private int x, y;
    private int width, height;
    private int type;
    // 从上向下
    public static final int TYPE_TOP_NORMAL = 0;
    // 从下向上
    public static final int TYPE_BOTTOM_NORMAL = 2;
    // 悬浮
    public static final int TYPE_HOVER_NORMAL = 4;

    private int speed = 2;

    private boolean visible;

    private Rectangle rect;

    // 随机决定当前生成哪个怪兽
    private Random monsterRan = new Random();

    // 获得障碍物的宽度和高度
    public static final int MONSTER1_WIDTH = imgs[0].getWidth();
    public static final int MONSTER1_HEIGHT = imgs[0].getHeight();
    public static final int MONSTER2_WIDTH = imgs[1].getWidth();
    public static final int MONSTER2_HEIGHT = imgs[1].getHeight();
    public static final int MONSTER3_WIDTH = imgs[2].getWidth();
    public static final int MONSTER3_HEIGHT = imgs[2].getHeight();

    public Monster() {
        rect = new Rectangle();
    }

    public Monster(int x, int y, int width, int height, int type) {
        this.x = x;
        this.y = y;
        this.width = width; // 宽度固定
        this.height = height;
        this.type = type;
    }

    /**
     * 根据不同的类型绘制障碍物（怪兽）
     */
    public void draw(Graphics g) {
        switch (type) {
            case TYPE_TOP_NORMAL:
                drawTop(g);
                break;
            case TYPE_BOTTOM_NORMAL:
                drawBottom(g);
                break;
        }
    }

    /**
     * 绘制从上向下的怪兽
     */
    private void drawTop(Graphics g) {
        int monsterId = monsterRan.nextInt(3);

        // 绘制怪兽
        g.drawImage(imgs[monsterId], x, y, null);

        // 赋予速度
        this.x -= speed;
        if (x < -50) { // 出了屏幕外
            visible = false;
        }

        rect(g, monsterId); // 绘制怪物矩形
    }

    /**
     * 绘制从下往上的怪兽
     */
    private void drawBottom(Graphics g) {
        int monsterId = monsterRan.nextInt(3);

        // 绘制怪兽
        g.drawImage(imgs[monsterId], x, Constant.FRAME_HEIGHT - imgs[monsterId].getHeight(), null);

        // 赋予速度
        this.x -= speed;
        if (x < -50) { // 出了屏幕外
            visible = false;
        }

        rect(g, monsterId); // 绘制障碍物矩形
    }

    /**
     * 判断何时进行绘制下一组障碍物
     */
    public boolean isIntoFrame() {
        return Constant.FRAME_WIDTH - x > 150;
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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    /**
     * 绘制障碍物的碰撞矩形
     */
    public void rect(Graphics g, int monsterId) {
        int x1 = this.x;
        int y1 = this.y;
        int w1 = imgs[monsterId].getWidth();
        int h1 = imgs[monsterId].getHeight();
        g.setColor(Color.blue);
        g.drawRect(x1, y1, w1, h1);
        setRectangle(x1, y1, w1, h1);
    }

    /**
     * 障碍物碰撞矩形参数
     */
    public void setRectangle(int x, int y, int width, int height) {
        rect.x = x;
        rect.y = y;
        rect.width = width;
        rect.height = height;
    }
}
