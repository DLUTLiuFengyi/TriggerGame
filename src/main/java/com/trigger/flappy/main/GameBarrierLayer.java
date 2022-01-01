package com.trigger.flappy.main;

import com.trigger.flappy.object.Bird;
import com.trigger.flappy.opponent.Barrier;
import com.trigger.flappy.opponent.BarrierPool;
import com.trigger.flappy.util.Constant;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 障碍物层类
 */
public class GameBarrierLayer {

    private List<Barrier> barriers;

    private GameTime gameTime;

    public GameBarrierLayer() {
        barriers = new ArrayList<Barrier>();
        gameTime = new GameTime();
    }

    /**
     * 绘制障碍物
     */
    public void draw(Graphics g, Bird bird) {
        for (int i=0; i<barriers.size(); i++) {
            Barrier barrier = barriers.get(i);
            if (barrier.isVisible()) {
                barrier.draw(g);
            } else {
                // 否则 从barriers中删除，并返回给对象池
                Barrier remove = barriers.remove(i);
                BarrierPool.setPool(remove);
                i -= 1;
            }
            barrier.draw(g);
        }
        collideBird(bird);
        genericLogic(g);
    }

    /**
     * 障碍物从左向右随机生成的逻辑，计时器逻辑
     * 为了设置计时器的字体，传入Graphics对象参数
     */
    public void genericLogic(Graphics g) {
        if (barriers.size() == 0) {
            generateRandomHeight();
            gameTime.begin();
//            generateOneGroup(); // 普通方法，直接创建内存对象
            getBarrierFromPool(Constant.FRAME_WIDTH, 0, numberTop, 0);
            getBarrierFromPool(Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT-numberDown, numberDown, 2);
        } else {
            // 计算时间差
            long differ = gameTime.differ();
            g.setColor(Color.white);
            g.setFont(new Font("微软雅黑", 1, 30));
            g.drawString("Duration: " + differ + "s", 30, 95);

            showBestScores(differ, g);

            // 判断最后一个障碍物是否完全进入屏幕内（什么时候绘制下一组障碍物）
            Barrier last = barriers.get(barriers.size()-1);
            // 以此障碍物的横坐标为判断依据
            if (last.isIntoFrame()) {
                generateRandomHeight();
//                generateOneGroup(); // 普通方法，直接创建内存对象
                getBarrierFromPool(Constant.FRAME_WIDTH, 0, numberTop, 0);
                getBarrierFromPool(Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT-numberDown, numberDown, 2);
            }
        }
    }

    private int record; // 游戏数据
    // 记录游戏记录的数据
    File recordFile = new File("game_file/record.txt");

    /**
     * 获取文件中的数据
     */
    public int getText() {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(recordFile));
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
    public void setText(String str) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(recordFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileWriter.write(str);
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
    private void showBestScores(long differ, Graphics g) {
        // 先获取最高记录
        record = getText();
        if (differ <= record) {
            g.setColor(Color.white);
            g.setFont(new Font("微软雅黑", 1, 25));
            g.drawString("Best scores: " + record, 250, 95);
        } else {
            setText(String.valueOf(differ));
            g.setColor(Color.white);
            g.setFont(new Font("微软雅黑", 1, 25));
            g.drawString("Best scores: " + getText(), 250, 95);
        }
    }

    /**
     * 绘制一组障碍物（一组包括一个从上往下和一个从下往上的障碍物）
     */
    private void generateOneGroup() {
        // 从屏幕的最右侧生成
        Barrier top = new Barrier(Constant.FRAME_WIDTH, 0, numberTop, 0);
        barriers.add(top);
        Barrier down = new Barrier(Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT-numberDown, numberDown, 2);
        barriers.add(down);
    }

    // 上方障碍物高度
    private int numberTop;
    // 下方障碍物高度
    private int numberDown;
    private Random random = new Random();

    /**
     * 产生两个100-350之间的随机高度
     */
    public void generateRandomHeight() {
        numberTop = random.nextInt(250) + 100;
        numberDown = random.nextInt(250) + 100;
        // 如果障碍物重合，则重新随机
        if (numberTop + numberDown > (int)(Constant.FRAME_HEIGHT) / 1.2) {
            generateRandomHeight();
        }
    }

    /**
     * 从障碍物对象池中获取对象
     */
    public void getBarrierFromPool(int x, int y, int num, int type) {
        Barrier top = BarrierPool.getPool();
        top.setX(x);
        top.setY(y);
        top.setHeight(num);
        top.setType(type);
        top.setVisible(true);
        barriers.add(top);
    }

    /**
     * 判断小鸟与障碍物发生碰撞
     */
    public void collideBird(Bird bird) {
        for (int i=0; i<barriers.size(); i++) {
            Barrier barrier = barriers.get(i);
            if (!bird.isInvincible()) {
                // 判断小鸟矩形与障碍物矩形是否相交
                if (barrier.getRect().intersects(bird.getRect())) {
                    bird.setHeart(bird.getHeart() - 1);
                    System.out.println("撞上啦");
                    if (bird.getHeart() < 0) {
                        System.out.println("生命值耗尽");
                        return ;
                    }
                    // 碰到障碍物后，小鸟短时间内无敌，以避免一直卡在一个障碍物上
//                    bird.setInvincible(true);
                }
            }
        }
    }

    /**
     * 重新开始游戏，清空障碍物的池子
     */
    public void restart() {
        System.out.println("重新开始，清空障碍物列表");
        barriers.clear();
    }
}
