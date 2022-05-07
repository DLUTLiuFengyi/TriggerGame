package com.trigger.awsd.method;

import com.trigger.awsd.object.NirvanaBeam;

import static com.trigger.awsd.util.GameEntities.beams;

/**
 * 光线类技能击中BOSS后
 * 需要将该光线对象的长度停止增大
 * 达到”光线击中BOSS“的视觉效果
 *
 * author: lfy
 */
public class BeamHitTheBossHook {

    public BeamHitTheBossHook() {}

    /**
     * 不断减小该光线对象的长度，减小到0以下时删除光线对象
     */
    public void subWidthOfBeam(NirvanaBeam nirvanaBeam) {
        // 需要另开线程执行这项操作，否则会产生画面卡顿现象
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
