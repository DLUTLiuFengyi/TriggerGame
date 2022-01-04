package com.trigger.flappy.object;

import com.trigger.flappy.util.Constant;
import com.trigger.flappy.util.GameUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.trigger.flappy.util.GameEntities.beams;

/**
 * 光线类
 *
 * author: lfy
 */
public class Beam extends ObjectBase {

    @Override
    public BufferedImage getImg() {
        return super.getImg();
    }

    public Beam() {
        super();
    }

    public Beam(BufferedImage img, int x, int y, int width, int height, int speed) {
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

//        // 绘制矩形
//        g.setColor(Color.blue);
//        g.drawRect(x, y, width, height);

        // 注意：此处需要更新光线对象的矩形坐标！之前射击有bug就是因为这里！
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
