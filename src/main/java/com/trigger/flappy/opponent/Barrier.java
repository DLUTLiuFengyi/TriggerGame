package com.trigger.flappy.opponent;

import com.trigger.flappy.util.Constant;
import com.trigger.flappy.util.GameUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 障碍物类
 */
public class Barrier {

    // 障碍物图片
    private static BufferedImage[] imgs;

    /**
     * 静态代码块，让类加载的时候将三个图片初始化
     */
    static {
        final int COUNT = 3; // 图片张数
        imgs = new BufferedImage[COUNT];
        for (int i=0; i<COUNT; i++) {
            imgs[i] = GameUtil.loadBufferedImage(Constant.BARRIER_IMG[i]);
        }
    }

    // 位置
    private int x, y;
    // 宽度和高度
    private int width, height;
    // 障碍物类型
    private int type;
    // 从上向下
    public static final int TYPE_TOP_NORMAL = 0;
    // 从下向上
    public static final int TYPE_BOTTOM_NORMAL = 2;
    // 悬浮
    public static final int TYPE_HOVER_NORMAL = 4;

    // 障碍物速度
    private int speed = 3;

    // 障碍物存活状态
    private boolean visible;

    // 障碍物矩形
    private Rectangle rect;

    // 获得障碍物的宽度和高度
    public static final int BARRIER_WIDTH = imgs[0].getWidth(); // 悬浮的宽度
    public static final int BARRIER_HEIGHT = imgs[0].getHeight(); // 悬浮的高度
    public static final int BARRIER_HEAD_WIDTH = imgs[1].getWidth(); // 向下或向上的宽度
    public static final int BARRIER_HEAD_HEIGHT = imgs[1].getHeight(); // 向下或向上的高度

    public Barrier() {
        rect = new Rectangle();
    }

    public Barrier(int x, int y, int height, int type) {
        this.x = x;
        this.y = y;
        this.width = BARRIER_WIDTH; // 宽度固定
        this.height = height;
        this.type = type;
    }

    /**
     * 根据不同的类型绘制障碍物
     */
    public void draw(Graphics g) {
        switch (type) {
            case TYPE_TOP_NORMAL:
                drawTopNormal(g);
                break;
            case TYPE_BOTTOM_NORMAL:
                drawNormalTop(g);
                break;
        }
    }

    /**
     * 绘制从上向下的障碍物
     */
    private void drawTopNormal(Graphics g) {
        // 求出所需要的障碍物的块数
        int count = (height - BARRIER_HEAD_HEIGHT) / BARRIER_HEIGHT + 1;
        // 绘制障碍物
        for (int i=0; i<count; i++) {
            g.drawImage(imgs[0],x, y+i*BARRIER_HEIGHT, null);
        }
        // 绘制障碍物的头
        int x = this.x - (BARRIER_HEAD_WIDTH-BARRIER_WIDTH)/2;
        int y = height - BARRIER_HEAD_HEIGHT;
        g.drawImage(imgs[2], x, y, null);

        this.x -= speed;
        if (x < -50) { // 出了屏幕外
            visible = false;
        }

        rect(g); // 绘制障碍物矩形
    }

    /**
     * 绘制从下往上的障碍物
     */
    private void drawNormalTop(Graphics g) {
        // 求出所需要的障碍物的块数
        int count = height / BARRIER_HEAD_HEIGHT + 1;
        // 绘制障碍物
        for (int i=0; i<count; i++) {
            // frame的像素点分布为x y从左上角到右下角 递增
            g.drawImage(imgs[0],x, Constant.FRAME_HEIGHT - i*BARRIER_HEIGHT, null);
        }
        // 绘制障碍物的头
        int x = this.x - (BARRIER_HEAD_WIDTH-BARRIER_WIDTH)/2;
        int y = Constant.FRAME_HEIGHT - height;
        // imgs[1]是从下往上的头的图片
        g.drawImage(imgs[1], x, y, null);

        this.x -= speed;
        if (x < -50) { // 出了屏幕外
            visible = false;
        }

        rect(g); // 绘制障碍物矩形
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
    public void rect(Graphics g) {
        int x1 = this.x;
        int y1 = this.y;
        int w1 = imgs[0].getWidth();
        g.setColor(Color.blue);
        g.drawRect(x1, y1, w1, height);
        setRectangle(x1, y1, w1, height);
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
