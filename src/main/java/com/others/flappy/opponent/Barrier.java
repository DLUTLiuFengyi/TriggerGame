package com.others.flappy.opponent;

import com.others.flappy.util.Constant;
import com.image.GameUtil;

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
    public static final int TYPE_TOP = 0;
    // 从下向上
    public static final int TYPE_BOTTOM = 2;
    // 悬浮
    public static final int TYPE_HOVER = 4;
    // 可动
    public static final int TYPE_MOBILE = 6;

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
            case TYPE_TOP:
                drawTop(g);
                break;
            case TYPE_BOTTOM:
                drawBottom(g);
                break;
            case TYPE_HOVER:
                drawHover(g);
                break;
            case TYPE_MOBILE:
                drawMobile(g);
                break;
        }
    }

    /**
     * 绘制从上向下的障碍物
     */
    private void drawTop(Graphics g) {
        // 求出所需要的障碍物的块数
        // height是想设定的障碍物的长度，其减去头部长度，再除以身体每节长度，得到需要的身体节个数
        int count = (height - BARRIER_HEAD_HEIGHT) / BARRIER_HEIGHT + 1;
        // 绘制障碍物
        for (int i=0; i<count; i++) {
            // 根据高度关系计算y轴坐标即可
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
    private void drawBottom(Graphics g) {
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
     * 绘制悬空障碍物
     */
    private void drawHover(Graphics g) {
        int count = (height - BARRIER_HEAD_HEIGHT) / BARRIER_HEIGHT + 1;
        // 绘制头部
        g.drawImage(imgs[1], x, y, null);
        // 绘制身躯
        for (int i=0; i<count; i++) {
            g.drawImage(imgs[0], x, y + BARRIER_HEAD_HEIGHT + i*BARRIER_HEIGHT, null);
        }
        // 处理障碍物的物理矩形
        rect(g);
        // 绘制尾部
        // tailY表示尾部开始绘制的y轴高度
        int tailY = y + height - BARRIER_HEAD_HEIGHT;
        g.drawImage(imgs[2], x, y, null);

        this.x -= speed;
        if (x < -50) { // 出了屏幕外
            visible = false;
        }
    }

    // 是否可动的状态
    private boolean mobile = true;

    /**
     * 绘制可动的障碍物
     */
    private void drawMobile(Graphics g) {
        int count = (height - BARRIER_HEAD_HEIGHT) / BARRIER_HEIGHT;
        // 绘制头部
        g.drawImage(imgs[1], x, y, null);
        // 绘制身躯
        for (int i=0; i<count; i++) {
            g.drawImage(imgs[0], x, y + BARRIER_HEAD_HEIGHT + i*BARRIER_HEIGHT, null);
        }
        // 处理障碍物的物理矩形
        rect(g);
        // 绘制尾部
        // tailY表示尾部开始绘制的y轴高度
        int tailY = y + height - BARRIER_HEAD_HEIGHT;
        g.drawImage(imgs[2], x, y, null);

        this.x -= speed;
        if (x < -50) { // 出了屏幕外
            visible = false;
        }

        if (mobile) {
            y += 5;
            if (y > (int) Constant.FRAME_HEIGHT/3) {
                mobile = false;
            }
        } else if (!mobile) {
            y -= 5;
            if (y < (int) Constant.FRAME_HEIGHT/4) {
                mobile = true;
            }
        }
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
        // 绘制障碍物矩形
//        g.setColor(Color.blue);
//        g.drawRect(x1, y1, w1, height);
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
