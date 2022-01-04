package com.trigger.flappy.util;

import com.trigger.flappy.object.*;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameUtil {

    // 奥特曼对象
    public static UltraMan ultraMan = new UltraMan();

    // 简易光弹列表
    public static List<SimpleShell> simpleShells = new ArrayList<>();

    // 发射的光线之所以是列表，主要是为了上一条光线还没飞出屏幕，角色能发射第二条光线
    public static List<Beam> beams = new ArrayList<>();

    // 障碍物列表
    public static List<Barrier> barriers = new ArrayList<>();
    // 怪兽列表
    public static List<Monster> monsters = new ArrayList<>();

    /**
     * 获取文件中的数据
     */
    public static int getDataFromFile(String filePath) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(new File(filePath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int read = 0;
        try {
            read = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return read;
    }

    /**
     * 存储数据
     */
    public static void setDataIntoFile(String data, String filePath) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileWriter.write(data);
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
