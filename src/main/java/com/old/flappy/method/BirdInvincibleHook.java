package com.old.flappy.method;

import com.old.flappy.object.Bird;

/**
 * 回调函数
 * 用于在飞行物与障碍物碰撞后，保证其在短时间内无敌
 * 避免卡在一个障碍物上、导致生命值快速降为0的不好的游戏体验
 *
 * author: lfy
 */
public class BirdInvincibleHook {

    // 无敌时间，默认为1000ms
    private static int duration = 1000;

    public BirdInvincibleHook() {}

    /**
     * duration秒后无敌状态消失
     */
    public void setNonInvincible(Bird bird) {
        // 需要另开线程对无敌状态进行计时，否则飞行物与障碍物相碰后，会出现卡顿现象
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(duration);
                    bird.setInvincible(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * duration秒后无敌状态消失
     * @param bird
     * @param setDuration
     */
    public void setNonInvincible(Bird bird, int setDuration) {
        // 需要另开线程对无敌状态进行计时，否则飞行物与障碍物相碰后，会出现卡顿现象
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(setDuration);
                    bird.setInvincible(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
