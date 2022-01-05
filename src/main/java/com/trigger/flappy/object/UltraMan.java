package com.trigger.flappy.object;

import com.image.ImageUtil;
import com.trigger.flappy.util.Constant;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.trigger.flappy.util.GameEntities.manaCount;
import static com.trigger.flappy.util.GameEntities.monsterCount;

/**
 * 奥特曼类
 *
 * author: lfy
 */
public class UltraMan extends ObjectBase {

    // 奥特曼图片
    private BufferedImage[] images;

    // 图片数量
    private static final int ULTRAMAN_IMG_COUNT = 7;

    // 0初始 1上升 2下降 3向左 4向右 5静止
    private static final int STATE_UP = 1; // 上升
    private static final int STATE_DOWN = 2; // 下降
    private static final int STATE_LEFT = 3; // 向左
    private static final int STATE_RIGHT = 4; // 向右
    private static final int STATE_STILL = 5; // 静止
    private static final int STATE_SIMPLE_SHELL = 6; //发射光弹
    private static final int STATE_BEAM = 7; // 发射光线

    private static final int FLYING_SPEED = 8;

    // 奥特曼状态
    private int state = STATE_STILL;

    // 奥特曼生命值
    private final static int FULL_HEART = 5;
    private int heart = FULL_HEART; // 初始有5颗♥

    // 奥特曼蓝量
    private final static int FULL_MANA = 100;
    private int mana = FULL_MANA; //

    private boolean invincible = false;
    private double invincibleSeconds = 1; // 无敌时间 默认为1秒

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

    public int getMana() {
        return this.mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    /**
     * 使用技能，消耗MP
     */
    public void subMana(int value) {
        this.mana -= value;
    }

    public UltraMan() {
        images = new BufferedImage[ULTRAMAN_IMG_COUNT];
        for (int i=0; i<ULTRAMAN_IMG_COUNT; i++) {
            images[i] = ImageUtil.loadBufferedImage(Constant.ULTRAMAN_IMG[i]);
        }

        x = 200;
        y = 200;
        speed = FLYING_SPEED;

        int w = images[state-1].getWidth();
        int h = images[state-1].getHeight();
        rect = new Rectangle(w, h);
    }

    @Override
    public void drawSelf(Graphics g) {
        // 操作逻辑
        actionLogic();
        // 恢复MP逻辑
        recoverMana();
        g.drawImage(images[state-1], x, y, null);
//        g.drawRect(x, y, (int)rect.getWidth(), rect.height);
        rect.x = x;
        rect.y = y;
        showUltraManInfo(g);
    }

    /**
     * 根据状态，进行实际的位置变更
     */
    public void actionLogic() {
        switch (state) {
            case STATE_UP: // 向上
                y -= speed;
                if (y < 20) { // 保证小鸟不飞出屏幕
                    y = 20;
                }
                break;
            case STATE_DOWN: // 向下
                y += speed;
                if (y > Constant.FRAME_HEIGHT - images[0].getHeight()) { // 保证小鸟不落出屏幕
                    y = Constant.FRAME_HEIGHT- images[0].getHeight();
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
     * 接收键盘的操纵信息，进行状态的设置
     */
    public void action(int actionNum) {
        switch (actionNum) {
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
                state = STATE_SIMPLE_SHELL;
                break;
            case 7:
                state = STATE_BEAM;
                break;
        }
    }

    /**
     * 恢复MP的逻辑
     * 随时间自动恢复
     */
    private void recoverMana() {
        if (this.mana > 99) {
            return;
        }
        if (manaCount % 2 == 0) {
            this.mana += 1;
        }
    }

    /**
     * 显示奥特曼当前信息（血量和蓝量）
     */
    private void showUltraManInfo(Graphics g) {
        /**
         * 绘制血条
         */
        // 绘制血条信息
        g.setColor(Color.white);
        g.setFont(new Font("微软雅黑", 1, 25));
        g.drawString("HP ", 30, 130);
        // 绘制作为血条背景的矩形
        g.setColor(Color.white);
        g.fillRect(75, 115, 100, 15);
        // 绘制血量
        g.setColor(Color.red);
        // 血量矩形的宽度是血量比例
        g.fillRect(75, 115, this.heart * 100 / FULL_HEART, 15);
        /**
         * 绘制蓝条
         */
        g.setColor(Color.white);
        g.setFont(new Font("微软雅黑", 1, 25));
        g.drawString("MP ", 185, 130);
        g.setColor(Color.white);
        g.fillRect(238, 115, 100, 15);
        g.setColor(Color.blue);
        g.fillRect(238, 115, this.mana * 100 / FULL_MANA, 15);
    }

    /**
     * 重新开始游戏，重置奥特曼的位置，血量补满
     */
    public void restart() {
        setHeart(5);
        setMana(100);
        x = 200;
        y = 200;
    }
}
