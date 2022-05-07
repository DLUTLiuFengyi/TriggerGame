package com.trigger.awsd.object;

import java.awt.*;

/**
 * 计数器，记录游戏时长
 *
 * author: lfy
 */
public class Timer extends ObjectBase {

    // 开始
    private long beginTime;
    // 结束
    private long endTime;

    public Timer() {
        beginTime = System.currentTimeMillis();
    }

    public void begin() {
        beginTime = System.currentTimeMillis();
    }

    public long computeDuration() {
        endTime = System.currentTimeMillis();
        return (endTime - beginTime) / 1000;
    }

    @Override
    public void drawSelf(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("微软雅黑", 1, 30));
        g.drawString("Duration: " + computeDuration() + "s", 30, 95);
    }

    /**
     * 重新开始游戏
     */
    public void restart() {
        beginTime = System.currentTimeMillis();
    }
}
