package com.trigger.flappy.object;

import com.trigger.flappy.object.ObjectBase;
import com.trigger.flappy.util.Constant;
import com.trigger.flappy.util.GameUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.trigger.flappy.util.GameEntities.beams;

/**
 * 超-必杀技，点击 F键 发射
 *
 * author: lfy
 */
public class NirvanaBeam extends ObjectBase {

    private static int manaValue = 50; // 消耗的MP量

    private int damageAmount = 100; // 对怪兽造成的伤害量

    public static int getManaValue() {
        return manaValue;
    }

    public int getDamageAmount() {
        return damageAmount;
    }

    @Override
    public BufferedImage getImg() {
        return super.getImg();
    }

    public NirvanaBeam() {
        super();
    }

    public NirvanaBeam(BufferedImage img, int x, int y, int width, int height, int speed) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        rect = new Rectangle(width, height);
    }

    /**
     * 画出光线
     */
    @Override
    public void drawSelf(Graphics g) {
        g.drawImage(img, x, y, width, height, null);

        // 注意：此处需要更新光线对象的碰撞矩形坐标！之前射击有bug就是因为这里！
        rect.x = x;
        rect.y = y;

        // 实现光线的移动
        x += speed;
        if (this.x > Constant.FRAME_WIDTH + this.width) {
            System.out.println("光线出界，删除");
            beams.remove(this);
        }
    }
}
