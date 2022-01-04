package com.trigger.flappy.object;

import com.trigger.flappy.util.Constant;
import com.trigger.flappy.util.GameUtil;

import java.awt.*;

/**
 * 游戏记录
 *
 * author: lfy
 */
public class Record extends ObjectBase {

    // 游戏时长记录
    private int durationRecord;
    // 当前游戏时长
    private int currentDuration;

    // 游戏得分记录
    private int scoresRecord;
    // 当前游戏得分
    private int currentScores;

    private String durationRecordFilePath = Constant.DURATION_RECORD_FILE_PATH;

    private String scoresRecordFilePath = Constant.SCORES_RECORD_FILE_PATH;

    public Record() {
        x = 280;
        y = 95;
//        this.durationRecord = GameUtil.getRecordFromFile(durationRecordFilePath);
        this.scoresRecord = GameUtil.getRecordFromFile(scoresRecordFilePath);
    }

    public int getCurrentDuration() {
        return currentDuration;
    }

    public void setCurrentDuration(int currentDuration) {
        this.currentDuration = currentDuration;
    }

    public int getCurrentScores() {
        return currentScores;
    }

    public void setCurrentScores(int currentScores) {
        this.currentScores = currentScores;
    }

    @Override
    public void drawSelf(Graphics g) {
        // 与存储在游戏文件durationRecord.txt中的最高记录作比较
//        if (currentDuration > durationRecord) {
//            durationRecord = currentDuration;
//            GameUtil.setRecordIntoFile(String.valueOf(durationRecord), durationRecordFilePath);
//        }
//        g.setColor(Color.white);
//        g.setFont(new Font("微软雅黑", 1, 25));
//        g.drawString("Best duration: " + durationRecord + "s", x, y);

        // 与存储在游戏文件scoresRecord.txt中的最高记录作比较
        if (currentScores > scoresRecord) {
            scoresRecord = currentScores;
            GameUtil.setRecordIntoFile(String.valueOf(scoresRecord), scoresRecordFilePath);
        }
        g.setColor(Color.white);
        g.setFont(new Font("微软雅黑", 1, 25));
        g.drawString("Best scores: " + scoresRecord, x, y);
    }
}
