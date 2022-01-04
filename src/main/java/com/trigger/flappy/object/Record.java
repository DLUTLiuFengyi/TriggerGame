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

    private String recordFilePath = Constant.RECORD_FILE_PATH;

    public Record() {
        x = 280;
        y = 95;
        this.durationRecord = GameUtil.getDataFromFile(recordFilePath);
    }

    public int getDurationRecord() {
        return durationRecord;
    }

    public void setDurationRecord(int durationRecord) {
        this.durationRecord = durationRecord;
    }

    public String getRecordFilePath() {
        return recordFilePath;
    }

    public void setRecordFilePath(String recordFilePath) {
        this.recordFilePath = recordFilePath;
    }

    public int getCurrentDuration() {
        return currentDuration;
    }

    public void setCurrentDuration(int currentDuration) {
        this.currentDuration = currentDuration;
    }

    @Override
    public void drawSelf(Graphics g) {
        // 与存储在游戏文件record.txt中的最高时间记录作比较
        if (currentDuration > durationRecord) {
            durationRecord = currentDuration;
            GameUtil.setDataIntoFile(String.valueOf(durationRecord), recordFilePath);
        }
        g.setColor(Color.white);
        g.setFont(new Font("微软雅黑", 1, 25));
        g.drawString("Best scores: " + durationRecord, x, y);
//        if (currentDuration <= durationRecord) {
//            g.setColor(Color.white);
//            g.setFont(new Font("微软雅黑", 1, 25));
//            g.drawString("Best scores: " + durationRecord, x, y);
//        } else {
//            GameUtil.setDataIntoFile(String.valueOf(currentDuration), recordFilePath);
//            g.setColor(Color.white);
//            g.setFont(new Font("微软雅黑", 1, 25));
//            g.drawString("Best scores: " + GameUtil.getDataFromFile(recordFilePath), x, y);
//        }
    }
}
