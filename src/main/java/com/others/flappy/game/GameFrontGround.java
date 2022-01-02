package com.others.flappy.game;

import com.others.flappy.object.Cloud;
import com.others.flappy.util.Constant;
import com.image.GameUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 游戏的前景类
 */
public class GameFrontGround {

    // 云彩个数
    private static final int CLOUD_COUNT = 2;
    // 存放云彩的容器
    private List<Cloud> clouds;
    // 云彩飞行速度
    private static final int CLOUD_SPEED = 5;

    private Random random;

    // 图片资源
    private BufferedImage[] img;

    public GameFrontGround() {
        clouds = new ArrayList<Cloud>();
        img = new BufferedImage[CLOUD_COUNT];

        for (int i=0; i<CLOUD_COUNT; i++) {
            img[i] = GameUtil.loadBufferedImage(Constant.CLOUD_IMG[i]);
        }

        random = new Random();
    }

    /**
     * 绘制云彩
     */
    public void draw(Graphics g) {
        logic();

        // 绘制
        for (int i=0; i<clouds.size(); i++) {
            clouds.get(i).draw(g);
        }
    }

    /**
     * 云彩的个数控制
     */
    private void logic() {
        // 产生0-500的随机数，如果它小于5，就生成一朵云
        if ((int) 500*Math.random() < 5) {
            Cloud cloud = new Cloud(img[random.nextInt(CLOUD_COUNT)], CLOUD_SPEED,
                    Constant.FRAME_WIDTH, random.nextInt(150));
            clouds.add(cloud);
        }
        for (int i=0; i<clouds.size(); i++) {
            Cloud cloud = clouds.get(i);
            if (cloud.isOutOfFrame()) {
                clouds.remove(i);
                i--;
                System.out.println("云朵实例" + cloud + "被移除");
            }
        }
    }
}
