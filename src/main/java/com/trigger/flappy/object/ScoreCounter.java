package com.trigger.flappy.object;

import java.awt.*;

/**
 * 得分计数器
 *
 * author: lfy
 */
public class ScoreCounter extends ObjectBase {

    private int scores; // 得分

    public ScoreCounter() {
        scores = 0;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    /**
     * 击败怪兽，得分增加
     */
    public void addScore(int value) {
        this.scores += value;
    }

    /**
     * 放跑怪兽，得分减少
     */
    public void subScore(int value) {
        this.scores -= value;
        if (this.scores < 0) {
            this.scores = 0;
        }
    }

    @Override
    public void drawSelf(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("微软雅黑", 1, 30));
        g.drawString("Scores: " + scores, 30, 95);
    }

    /**
     * 重新开始游戏
     */
    public void restart() {
        scores = 0;
    }
}
