package com.others.flappy.object;

import com.others.flappy.util.Constant;
import com.image.GameUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bird{

    // 小鸟图片
    private BufferedImage[] images;

    // 图片数量
    private static final int BIRD_IMG_COUNT = 6;
    // 鸟的状态
    // 0初始 1上升 2下降 3向左 4向右 5静止
    private static final int STATE_NORMAL = 0; // 初始
    private static final int STATE_UP = 1; // 上升
    private static final int STATE_DOWN = 2; // 下降
    private static final int STATE_LEFT = 3; // 向左
    private static final int STATE_RIGHT = 4; // 向右
    private static final int STATE_STILL = 5; // 静止

    // 小鸟状态
    private int state;

    // 小鸟位置
    private int x = 200, y = 200;

    // 小鸟移动方向
    private boolean up = false, down = false;

    // 小鸟初始移动速度
    private int speed = 7;

    // 小鸟的矩形，用于碰撞处理
    private Rectangle rect;

    // 小鸟生命值
    private int heart = 5; // 默认有5颗♥
    private boolean invincible = false;
    private double invincibleSeconds = 1; // 无敌时间 默认为1秒

    // 小鸟加速度
    private int acceleration;

    public Bird() {
        images = new BufferedImage[BIRD_IMG_COUNT];
        for (int i=0; i<BIRD_IMG_COUNT; i++) {
            images[i] = GameUtil.loadBufferedImage(Constant.BIRD_IMG[i]);
        }

        int w = images[0].getWidth();
        int h = images[0].getHeight();
        rect = new Rectangle(w, h);
    }

    public Rectangle getRect() {
        return rect;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public double getInvincibleSeconds() {
        return invincibleSeconds;
    }

    public void setInvincibleSeconds(double invincibleSeconds) {
        this.invincibleSeconds = invincibleSeconds;
    }

    /**
     * 绘制小鸟
     */
    public void draw(Graphics g) {
        flyLogic();
        g.drawImage(images[state], x, y, null);

        // 绘制出小鸟的矩形
//        g.drawRect(x, y, (int)rect.getWidth(), rect.height);
        rect.x = x;
        rect.y = y;

        showBirdStatus(g);
    }

    /**
     * 控制小鸟方向
     */
    public void flyLogic() {
//        if (up) {
//            y -= speed;
//            if (y < 20) { // 保证小鸟不飞出屏幕
//                y = 20;
//            }
//        }
//        if (!up) { // 自由落体
//            y += speed;
//            if (y > Constant.FRAME_HEIGHT - 182) { // 保证小鸟不落出屏幕
//                y = Constant.FRAME_HEIGHT - 182;
//            }
//        }
        switch (state) {
            case STATE_UP: // 向上
//                // 加速度逻辑
//                acceleration -= 1;
//                y += acceleration;
//                // 最高速度限制
//                if (acceleration < -10) {
//                    acceleration = 10;
//                }

                y -= speed;
                if (y < 20) { // 保证小鸟不飞出屏幕
                    y = 20;
                }
                break;
            case STATE_DOWN: // 向下
//                // 加速度逻辑
//                acceleration += 1;
//                y += acceleration;
//                // 最高速度限制
//                if (acceleration > 10) {
//                    acceleration = 10;
//                }

                y += speed;
                if (y > Constant.FRAME_HEIGHT - 182) { // 保证小鸟不落出屏幕
                    y = Constant.FRAME_HEIGHT - 182;
                    acceleration = 0;
                }
                break;
            case STATE_LEFT: // 向左
                x -= speed * 2;
                break;
            case STATE_RIGHT: // 向右
                x += speed;
                break;
            case STATE_STILL: // 静止
                break;
        }
    }

    /**
     * 操纵
     */
    public void fly(int fly) {
        switch (fly) {
            case 1:
                state = STATE_UP;
                break;
            case 2:
                state = STATE_DOWN;
                break;
            case 3:
                state = STATE_LEFT;
                break;
            case 4:
                state = STATE_RIGHT;
                break;
            case 5:
                state = STATE_STILL;
                break;
            case 6:
                state = STATE_STILL;
                break;
            case 7:
                state = STATE_STILL;
                break;
            case 8:
                state = STATE_STILL;
                break;
        }
    }

    /**
     * 显示小鸟当前信息（血量，速度）
     */
    private void showBirdStatus(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("微软雅黑", 1, 25));
        g.drawString("Blood: " + showHeart(), 30, 130);
    }

    /**
     * 显示小鸟血量当前是几颗♥
     */
    private String showHeart() {
        switch (getHeart()) {
            case 5:
                return "HHHHH";
            case 4:
                return "HHHH";
            case 3:
                return "HHH";
            case 2:
                return "HH";
            case 1:
                return "H";
            default:
                return "DEAD";
        }
    }

    /**
     * 重新开始游戏，重新绘制小鸟位置
     */
    public void restart() {
        setHeart(5);
        x = 200;
        y = 200;
    }
}
