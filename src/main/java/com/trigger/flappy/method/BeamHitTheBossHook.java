package com.trigger.flappy.method;

import com.trigger.flappy.object.NirvanaBeam;

import static com.trigger.flappy.util.GameEntities.beams;

/**
 * 光线类技能击中BOSS后
 * 需要将该光线对象的宽度停止增大
 * 达到”光线击中BOSS“的视觉效果
 *
 * author: lfy
 */
public class BeamHitTheBossHook {

    public BeamHitTheBossHook() {}

    /**
     * duration秒后无敌状态消失
     */
    public void subWidthOfBeam(NirvanaBeam nirvanaBeam) {
        // 需要另开线程对无敌状态进行计时，否则飞行物与障碍物相碰后，会出现卡顿现象
        new Thread(new Runnable() {
            @Override
            public void run() {
                nirvanaBeam.setWidthMoreLonger(false);
                while (nirvanaBeam.getWidth() > 0) {
                    nirvanaBeam.subWidth(20);
                    try {
                        // 延缓一下光线变短的速度，否则光线眨眼就消失了
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 光线长度减小到0以下时，删除该光线对象
                beams.remove(nirvanaBeam);
            }
        }).start();
    }
}
