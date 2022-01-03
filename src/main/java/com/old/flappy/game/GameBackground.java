package com.old.flappy.game;

import com.image.ImageUtil;
import com.old.flappy.util.Constant;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 游戏背景类
 */
public class GameBackground {

    private BufferedImage bkimg;

    // 初始化资源
    public GameBackground() {
        bkimg = ImageUtil.loadBufferedImage(Constant.BK_IMG_OATH);
    }

    // 绘制方法，把图片画到窗口上
    public void draw(Graphics g) {

        // 填充背景
        g.setColor(Constant.BK_COLOR);
        // 画到窗口上
        g.fillRect(0, 0, Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT);
        // 画笔颜色
        g.setColor(Color.BLACK);

        // 得到图片高度和宽度
        int height = bkimg.getHeight();
        int width = bkimg.getWidth();
        // 所需要的图片的张数
        int count = Constant.FRAME_WIDTH/width + 1;
        for (int i=0; i<count; i++) {
            g.drawImage(bkimg, width*i, Constant.FRAME_HEIGHT-height, null);
        }
    }
}
