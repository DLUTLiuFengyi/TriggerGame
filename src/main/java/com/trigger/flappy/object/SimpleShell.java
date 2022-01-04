package com.trigger.flappy.object;

import com.trigger.flappy.util.Constant;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.trigger.flappy.util.GameEntities.simpleShells;

/**
 * 简易光弹类，平A技能
 *
 * author: lfy
 */
public class SimpleShell extends ObjectBase {

    private static int manaValue = 4; // 消耗的MP量

    public static int getManaValue() {
        return manaValue;
    }

    @Override
    public BufferedImage getImg() {
        return super.getImg();
    }

    public SimpleShell() {
        super();
    }

    public SimpleShell(BufferedImage img, int x, int y, int width, int height, int speed) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        rect = new Rectangle(width, height);
    }

    @Override
    public void drawSelf(Graphics g) {
        g.drawImage(img, x, y, width, height, null);

        // 注意：此处需要更新光线对象的矩形坐标！之前射击有bug就是因为这里！
        rect.x = x;
        rect.y = y;

        // 实现光线的移动
        x += speed;
        if (this.x > Constant.FRAME_WIDTH + this.width) {
            System.out.println("光弹出界，删除");
            simpleShells.remove(this);
        }
    }
}
