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

    // 消耗的MP量
    private static int manaValue = 50;
    // 对怪兽造成的伤害量
    private int damageAmount = 100;

    // 光线类技能发射出去时，在没有达到最大长度时，长度会不断增加
    // 而当击中BOSS时，需要关闭长度增加的状态，并不断减少长度，达到光线击中BOSS的视觉效果
    private boolean widthMoreLonger = true;

    public static int getManaValue() {
        return manaValue;
    }

    public int getDamageAmount() {
        return damageAmount;
    }

    public boolean isWidthMoreLonger() {
        return widthMoreLonger;
    }

    public void setWidthMoreLonger(boolean widthMoreLonger) {
        this.widthMoreLonger = widthMoreLonger;
    }

    public void subWidth(int subValue) {
        this.width -= subValue;
    }

    @Override
    public BufferedImage getImg() {
        return super.getImg();
    }

    public NirvanaBeam() {
        super();
    }

    public NirvanaBeam(BufferedImage img, int x, int y, int height, int speed) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.width = 30; // 光线的初始长度是30，随后不断增大，达到750时停止增大
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

        // 实现光线的移动
        x += speed;

        // 光线长度不断增大，达到”发射光线“的视觉效果
        if (width <= 750 && widthMoreLonger == true) {
            width += 20;
        }
        // 更新碰撞矩形的x y坐标
        rect.x = x;
        rect.y = y;
        // 对于光线类型对象，还需要更新碰撞矩形的长度和高度（主要是长度）
        rect.width = width;
        rect.height = height;

        if (this.x > Constant.FRAME_WIDTH + this.width) {
            System.out.println("光线出界，删除");
            beams.remove(this);
        }
    }
}
