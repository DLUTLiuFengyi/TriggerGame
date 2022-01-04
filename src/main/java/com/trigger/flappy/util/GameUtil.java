package com.trigger.flappy.util;

import com.trigger.flappy.object.*;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 提供游戏相关工具函数
 */
public class GameUtil {

    /**
     * 获取时长记录或得分记录
     */
    public static int getRecordFromFile(String filePath) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(new File(filePath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int recordValue = 0;
        try {
            recordValue = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recordValue;
    }

    /**
     * 新纪录存储到文件中
     */
    public static void setRecordIntoFile(String recordValue, String filePath) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileWriter.write(recordValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示最高得分记录
     */
    public static void showBestDuration(long currentDuration, Record record, Graphics g) {
        record.setCurrentDuration((int) currentDuration);
        record.drawSelf(g);
    }

    /**
     * 产生两个100-350之间的随机高度
     * 分别赋值给上方障碍物和下方障碍物
     */
    public static int[] generateRandomHeights() {
        Random random = new Random();
        int numberTop = random.nextInt(200) + 100;
        int numberDown = random.nextInt(200) + 100;

        // 如果障碍物重合，则重新随机
        if (numberTop + numberDown > (int)(Constant.FRAME_HEIGHT) / 1.2) {
            generateRandomHeights();
        }

        return new int[]{numberTop, numberDown};
    }

    /**
     * 产生一个100-350之间的随机高度
     */
    public static int generateRandomHeight() {
        Random random = new Random();
        int randomHeight = random.nextInt(350) + 200;
        // 障碍物高度不太低
        if (randomHeight < 300) {
            generateRandomHeight();
        }
        return randomHeight;
    }

    /**
     * 产生一个随机y坐标
     */
    public static int generateRandomY(int typeSelected) {
        Random random = new Random();
        int[] randomY = new int[]{100, 200, 300, 400, 500, 600};
        return randomY[random.nextInt(6)];
    }
}
