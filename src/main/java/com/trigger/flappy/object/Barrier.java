package com.trigger.flappy.object;

import com.image.ImageUtil;
import com.trigger.flappy.method.InvincibleHook;
import com.trigger.flappy.util.Constant;
import com.trigger.flappy.util.GameUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

import static com.trigger.flappy.util.GameUtil.*;

/**
 * 障碍物类
 */
public class Barrier extends ObjectBase {

    // 障碍物图片
    private static BufferedImage[] imgs;

    // 回调函数，用于与奥特曼碰撞后，对奥特曼无敌状态的设置
    private InvincibleHook invincibleHook;

    /**
     * 静态代码块，让类加载的时候将三个图片初始化
     */
    static {
        final int COUNT = 3; // 图片张数
        imgs = new BufferedImage[COUNT];
        for (int i=0; i<COUNT; i++) {
            imgs[i] = ImageUtil.loadBufferedImage(Constant.BARRIER_IMG[i]);
        }
    }

    // 障碍物类型
    private int type;
    // 从上向下
    public static final int TYPE_TOP = 0;
    // 从下向上
    public static final int TYPE_BOTTOM = 2;
    // 悬浮
    public static final int TYPE_HOVER = 4;
    // 可动
    public static final int TYPE_MOBILE = 6;

    public static final int BARRIER_SPEED = 3;

    // 障碍物存活状态
    private boolean visible;

    // 获得障碍物的宽度和高度
    public static final int BARRIER_WIDTH = imgs[0].getWidth(); // 悬浮的宽度
    public static final int BARRIER_HEIGHT = imgs[0].getHeight(); // 悬浮的高度
    public static final int BARRIER_HEAD_WIDTH = imgs[1].getWidth(); // 向下或向上的宽度
    public static final int BARRIER_HEAD_HEIGHT = imgs[1].getHeight(); // 向下或向上的高度

    public Barrier() {
        x = Constant.FRAME_WIDTH; // x坐标固定
        width = BARRIER_WIDTH; // 宽度固定
        speed = BARRIER_SPEED; // 速度固定
        // y坐标、高度、类型不固定，需要随机生成
        generateRandomElement();
        // 生成矩形
        rect = new Rectangle();
        invincibleHook = new InvincibleHook();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    /**
     * 随机选取一种类型，然后确定每种类型的对应成员变量的取值
     */
    private void generateRandomElement() {
        // 四种类型的障碍物（由上往下，由下往上，悬空，悬空可动）
        int[] randomType = new int[]{0, 2, 4, 6};
        Random random = new Random();
        // 从四种类型中随机挑一种
        int typeIndex = random.nextInt(4);
        int typeSelected = randomType[typeIndex];
        // 生成一个随机数，作为高度，注意此处的高度尚不是障碍物的最终高度
        int randomHeight = generateRandomHeight();
        switch (typeSelected) {
            case 0: // 从上往下
                y = 0;
                type = 0;
                height = randomHeight;
                break;
            case 2: // 从下往上
                y = Constant.FRAME_HEIGHT - randomHeight;
                type = 2;
                height = randomHeight;
                break;
            case 4: // 悬空
                y = 100;
                type = 4;
                height = 300;
                break;
            case 6: // 悬空可动
                y = 200;
                type = 6;
                height = 250;
                break;
        }
    }

    /**
     * 根据不同的类型绘制障碍物
     */
    @Override
    public void drawSelf(Graphics g) {
        // 判断障碍物是否已移动出屏幕左边界
        if (judgeOutOfScreen()) {
            return;
        }

        // 与光线的碰撞检测
        if (judgeCollideWithBeam()) {
            return;
        }

        // 与奥特曼的碰撞检测
        judgeCollideWithUltraMan();

        // 根据type确定需要绘制的类型（从上往下，从下往上，悬空，悬空可动）并进行绘制
        drawBasedOnType(g);
    }

    /**
     * 判断障碍物是否已移动出屏幕左边界（障碍物从右向左移动）
     */
    private boolean judgeOutOfScreen() {
        // 判断是否移除屏幕外
        if (x < -imgs[1].getWidth()) {
            System.out.println("屏幕外，删除障碍");
            barriers.remove(this);
            return true;
        }
        return false;
    }

    /**
     * 与光线的碰撞检测
     */
    private boolean judgeCollideWithBeam() {
        for (Beam beam : beams) {
            if (this.getRect().intersects(beam.getRect())) {
                System.out.println("被光线消灭");
                barriers.remove(this);
                return true;
            }
        }
        return false;
    }

    /**
     * 与奥特曼的碰撞检测
     */
    private boolean judgeCollideWithUltraMan() {
        // 判断奥特曼矩形与障碍物矩形是否相交
        if (!GameUtil.ultraMan.isInvincible()) {
            // 如果奥特曼不是无敌状态
            if (this.getRect().intersects(GameUtil.ultraMan.getRect())) {
                // 生命值-1
                GameUtil.ultraMan.setHeart(GameUtil.ultraMan.getHeart() - 1);
                System.out.println("撞上啦");
                if (GameUtil.ultraMan.getHeart() < 0) {
                    System.out.println("生命值耗尽");
                }
                // 碰到障碍物后，奥特曼短时间内无敌，以避免一直卡在一个障碍物上
                GameUtil.ultraMan.setInvincible(true);
                // 回调函数，在新的线程中将无敌时间结束后的奥特曼设为不无敌状态
                invincibleHook.setNonInvincible(GameUtil.ultraMan);
                return true;
            }
        }
        return false;
    }

    /**
     * 根据type确定需要绘制的类型（从上往下，从下往上，悬空，悬空可动）
     * 并进行绘制
     */
    private void drawBasedOnType(Graphics g) {
        switch (type) {
            case TYPE_TOP:
                drawTop(g);
                break;
            case TYPE_BOTTOM:
                drawBottom(g);
                break;
            case TYPE_HOVER:
                drawHover(g);
                break;
            case TYPE_MOBILE:
                drawMobile(g);
                break;
        }
    }

    /**
     * 绘制从上向下的障碍物
     */
    private void drawTop(Graphics g) {
        // 求出所需要的障碍物的块数
        // height是想设定的障碍物的长度，其减去头部长度，再除以身体每节长度，得到需要的身体节个数
        int count = (height - BARRIER_HEAD_HEIGHT) / BARRIER_HEIGHT + 1;
        // 绘制障碍物
        for (int i=0; i<count; i++) {
            // 根据高度关系计算y轴坐标即可
            g.drawImage(imgs[0],x, y+i*BARRIER_HEIGHT, null);
        }
        // 绘制障碍物的头
        int x = this.x - (BARRIER_HEAD_WIDTH-BARRIER_WIDTH)/2;
        int y = height - BARRIER_HEAD_HEIGHT;
        g.drawImage(imgs[2], x, y, null);

        this.x -= speed;
        if (x < -50) { // 出了屏幕外
            visible = false;
        }

        drawRect(g); // 绘制障碍物矩形
    }

    /**
     * 绘制从下往上的障碍物
     */
    private void drawBottom(Graphics g) {
        // 求出所需要的障碍物的块数
        int count = height / BARRIER_HEAD_HEIGHT + 1;
        // 绘制障碍物
        for (int i=0; i<count; i++) {
            // frame的像素点分布为x y从左上角到右下角 递增
            g.drawImage(imgs[0],x, Constant.FRAME_HEIGHT - i*BARRIER_HEIGHT, null);
        }
        // 绘制障碍物的头
        int x = this.x - (BARRIER_HEAD_WIDTH-BARRIER_WIDTH)/2;
        int y = Constant.FRAME_HEIGHT - height;
        // imgs[1]是从下往上的头的图片
        g.drawImage(imgs[1], x, y, null);

        this.x -= speed;
        if (x < -50) { // 出了屏幕外
            visible = false;
        }

        drawRect(g); // 绘制障碍物矩形
    }

    /**
     * 绘制悬空障碍物
     */
    private void drawHover(Graphics g) {
        int count = (height - BARRIER_HEAD_HEIGHT) / BARRIER_HEIGHT + 1;
        // 绘制头部
        g.drawImage(imgs[1], x, y, null);
        // 绘制身躯
        for (int i=0; i<count; i++) {
            g.drawImage(imgs[0], x, y + BARRIER_HEAD_HEIGHT + i*BARRIER_HEIGHT, null);
        }
        // 处理障碍物的物理矩形
        drawRect(g);
        // 绘制尾部
        // tailY表示尾部开始绘制的y轴高度
        int tailY = y + height - BARRIER_HEAD_HEIGHT;
        g.drawImage(imgs[2], x, y, null);

        this.x -= speed;
        if (x < -50) { // 出了屏幕外
            visible = false;
        }
    }

    // 是否可动的状态
    private boolean mobile = true;

    /**
     * 绘制可动的障碍物
     */
    private void drawMobile(Graphics g) {
        int count = (height - BARRIER_HEAD_HEIGHT) / BARRIER_HEIGHT;
        // 绘制头部
        g.drawImage(imgs[1], x, y, null);
        // 绘制身躯
        for (int i=0; i<count; i++) {
            g.drawImage(imgs[0], x, y + BARRIER_HEAD_HEIGHT + i*BARRIER_HEIGHT, null);
        }
        // 处理障碍物的物理矩形
        drawRect(g);
        // 绘制尾部
        // tailY表示尾部开始绘制的y轴高度
        int tailY = y + height - BARRIER_HEAD_HEIGHT;
        g.drawImage(imgs[2], x, y, null);

        this.x -= speed;
        if (x < -50) { // 出了屏幕外
            visible = false;
        }

        if (mobile) {
            y += 5;
            if (y > (int) Constant.FRAME_HEIGHT/3) {
                mobile = false;
            }
        } else if (!mobile) {
            y -= 5;
            if (y < (int) Constant.FRAME_HEIGHT/4) {
                mobile = true;
            }
        }
    }

    /**
     * 判断何时进行绘制下一组障碍物
     */
    public boolean hasBeenIntoFrame() {
        return Constant.FRAME_WIDTH - x > 150;
    }

    /**
     * 绘制障碍物的碰撞矩形
     */
    public void drawRect(Graphics g) {
        int x1 = this.x;
        int y1 = this.y;
        int w1 = imgs[0].getWidth();
//        // 绘制障碍物矩形
//        g.setColor(Color.blue);
//        g.drawRect(x1, y1, w1, height);
        setRectangle(x1, y1, w1, height);
    }

    /**
     * 障碍物碰撞矩形参数
     */
    public void setRectangle(int x, int y, int width, int height) {
        rect.x = x;
        rect.y = y;
        rect.width = width;
        rect.height = height;
    }
}
