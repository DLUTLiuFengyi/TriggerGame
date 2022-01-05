package com.trigger.flappy.object;

import com.image.ImageUtil;
import com.trigger.flappy.method.BeamHitTheBossHook;
import com.trigger.flappy.method.CollideInvincibleHook;
import com.trigger.flappy.util.Constant;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.trigger.flappy.util.GameEntities.*;

/**
 * BOSS类
 *
 * author: lfy
 */
public class Boss extends Monster {

    private static BufferedImage bossImg;

    private static int BOSS_FULL_HEART = 500;

    // 该回调函数实现“光线击中BOSS”的视觉效果
    private BeamHitTheBossHook beamHitTheBossHook;

    static {
        bossImg = ImageUtil.loadBufferedImage(Constant.BOSS_IMG);
    }

    public Boss(CollideInvincibleHook collideHook, BeamHitTheBossHook beamHitTheBossHook) {
        // BOSS对象固定从窗口最右端开始生成
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
        this.collideHook = collideHook;
        // 实现”光线击中BOSS“的视觉效果
        this.beamHitTheBossHook = beamHitTheBossHook;
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
     * 与光弹、光线的碰撞检测
     */
    @Override
    public void judgeCollideWithBeam() {
        // 尚未进入屏幕中的障碍物，不进行碰撞检测
        if (this.x > Constant.FRAME_WIDTH) {
            return ;
        }
        for (int i=0; i<beams.size(); i++) {
            if (this.getRect().intersects(beams.get(i).getRect())
                    && !bossIgnoreBeams.contains(beams.get(i))) {
                // 击中敌人，扣除血量
                this.heart -= beams.get(i).getDamageAmount();
                // 将此光线添加进之后会被BOSS无视的列表中，避免同一条光线多次对BOSS造成伤害的情况
                bossIgnoreBeams.add(beams.get(i));
                // 营造“光线击中BOSS”的视觉效果
                beamHitTheBossHook.subWidthOfBeam(beams.get(i));
                if (this.heart < 1) {
                    eliminateLogic();
                }
                return ;
            }
        }
        for (int i=0; i<simpleShells.size(); i++) {
            if (this.getRect().intersects(simpleShells.get(i).getRect())) {
                // 击中敌人，扣除血量
                this.heart -= simpleShells.get(i).getDamageAmount();
                // 同时这个光弹对象也需要被删除，避免线程刷新时保留效果，导致障碍物被同一光弹多次攻击
                simpleShells.remove(simpleShells.get(i));
                if (this.heart < 1) {
                    eliminateLogic();
                }
                return ;
            }
        }
    }

    /**
     * 绘制BOSS血条等信息
     */
    private void drawBossInfo(Graphics g) {
        // 绘制作为血条背景的矩形
        g.setColor(Color.white);
        g.fillRect(Constant.FRAME_WIDTH/4 - 50, Constant.FRAME_HEIGHT - 60,
                Constant.FRAME_WIDTH/2 + 100,
                20);
        // 绘制血量
        g.setColor(Color.red);
        // 血量矩形的宽度是血量比例
        g.fillRect(Constant.FRAME_WIDTH/4 - 50, Constant.FRAME_HEIGHT - 60,
                this.heart * (Constant.FRAME_WIDTH/2) / BOSS_FULL_HEART + 100,
                20);
    }
}
