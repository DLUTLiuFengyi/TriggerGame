package com.trigger.flappy.object;

import com.image.ImageUtil;
import com.trigger.flappy.method.InvincibleHook;
import com.trigger.flappy.util.Constant;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * BOSS类
 *
 * author: lfy
 */
public class Boss extends Monster {

    private static BufferedImage bossImg;

    private static int BOSS_FULL_HEART = 250;

    static {
        bossImg = ImageUtil.loadBufferedImage(Constant.BOSS_IMG);
    }

    public Boss(InvincibleHook invincibleHook) {
        // 新的怪兽对象固定从窗口最右端开始生成
        x = Constant.FRAME_WIDTH;
        y = 100;
        width = bossImg.getWidth() * 2;
        height = bossImg.getHeight() * 2;
        // 速度固定
        speed = 3;
        scoreReceived = 1000;
        heart = BOSS_FULL_HEART;
        // 生成矩形
        rect = new Rectangle();
        // 一个用于奥特曼与怪兽碰撞后无敌状态相关设置的回调函数
        this.invincibleHook = invincibleHook;
    }

    @Override
    public void drawSelf(Graphics g) {
        if (x < (int)(Constant.FRAME_WIDTH / 3) * 2) {
            // BOSS从右向左移动了屏幕的三分之一后，速度变为0
            speed = 0;
        }

        // 与光线的碰撞检测
        judgeCollideWithBeam();

        if (this.heart < 1) {
            return;
        }

        // 与奥特曼的碰撞检测
        judgeCollideWithUltraMan();

        // 绘制BOSS
        drawBoss(g);
        // 绘制BOSS血条
        drawBossInfo(g);
    }

    /**
     * 对BOSS进行绘制
     */
    private void drawBoss(Graphics g) {
        // 绘制
        g.drawImage(bossImg, x, y, width, height, null);

        // 处理障碍物的物理矩形
        drawRect(g);
        this.x -= speed;

        if (mobile) {
            y += 10;
            if (y > (int) Constant.FRAME_HEIGHT - height) {
                mobile = false;
            }
        } else if (!mobile) {
            y -= 10;
            if (y < (int) 100) {
                mobile = true;
            }
        }
    }

    /**
     * 绘制BOSS血条等信息
     */
    private void drawBossInfo(Graphics g) {
        // 绘制作为血条背景的矩形
        g.setColor(Color.white);
        g.fillRect(Constant.FRAME_WIDTH/4, Constant.FRAME_HEIGHT - 100,
                Constant.FRAME_WIDTH/2, 25);
        // 绘制血量
        g.setColor(Color.red);
        // 血量矩形的宽度是血量比例
        g.fillRect(Constant.FRAME_WIDTH/4, Constant.FRAME_HEIGHT - 100,
                this.heart * (Constant.FRAME_WIDTH/2) / BOSS_FULL_HEART, 25);
    }
}
