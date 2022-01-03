package com.trigger.flappy.object;

import com.image.ImageUtil;
import com.trigger.flappy.util.Constant;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 游戏的背景类
 * 背景包括背景图片、背景颜色、战斗机等
 *
 * author: lfy
 */
public class Background extends ObjectBase {

    // 飞机种类数
    private static final int FIGHTER_COUNT = 2;
    // 存放飞机的容器
    private List<Fighter> fighters;
    // 飞机速度
    private static final int FIGHTER_SPEED = 5;
    // 随机生成不同种类的飞机
    private Random fighterNumRan;

    // 飞机的图片资源
    private BufferedImage[] fighterImgs;

    public Background() {
        /**
         * 背景图片
         */
        img = ImageUtil.loadBufferedImage(Constant.BK_IMG_OATH);

        /**
         * 飞机
         */
        fighters = new ArrayList<Fighter>();
        fighterImgs = new BufferedImage[FIGHTER_COUNT];
        for (int i=0; i<FIGHTER_COUNT; i++) {
            fighterImgs[i] = ImageUtil.loadBufferedImage(Constant.FIGHTER_IMG[i]);
        }
        fighterNumRan = new Random();
    }

    /**
     * 绘制背景
     */
    @Override
    public void drawSelf(Graphics g) {
        drawBackImg(g);
        drawFighters(g);
    }

    /**
     * 负责背景绘制
     */
    private void drawBackImg(Graphics g) {
        // 填充背景
        g.setColor(Constant.BK_COLOR);
        // 画到窗口上
        g.fillRect(0, 0, Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT);
        // 画笔颜色
        g.setColor(Color.BLACK);

        // 得到图片高度和宽度
        int height = img.getHeight();
        int width = img.getWidth();
        // 所需要的底部图片的张数
        int count = Constant.FRAME_WIDTH/width + 1;
        // 保证底部图片能将底部区域填充满
        for (int i=0; i<count; i++) {
            g.drawImage(img, width*i, Constant.FRAME_HEIGHT-height, null);
        }
    }

    /**
     * 负责飞机绘制
     */
    private void drawFighters(Graphics g) {
        // 产生0-500的随机数，如果它小于5，就生成一架飞机
        if ((int) 500*Math.random() < 5) {
            Fighter fighter = new Fighter(
                    fighterImgs[fighterNumRan.nextInt(FIGHTER_COUNT)],
                    FIGHTER_SPEED,
                    Constant.FRAME_WIDTH,
                    fighterNumRan.nextInt(150)
            );
            fighters.add(fighter);
        }
        for (int i=0; i<fighters.size(); i++) {
            Fighter fighter = fighters.get(i);
            if (fighter.isOutOfFrame()) {
                fighters.remove(i);
                i--;
                System.out.println("飞机实例" + fighter + "被移除");
            }
        }
        // 绘制
        for (int i=0; i<fighters.size(); i++) {
            fighters.get(i).drawSelf(g);
        }
    }
}
