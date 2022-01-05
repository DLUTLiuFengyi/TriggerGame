package com.trigger.flappy.object;

import com.image.ImageUtil;
import com.trigger.flappy.method.InvincibleHook;
import com.trigger.flappy.util.Constant;
import com.trigger.flappy.util.GameUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static com.trigger.flappy.util.GameEntities.*;
import static com.trigger.flappy.util.GameUtil.*;

/**
 * 怪兽类
 *
 * author: lfy
 */
public class Monster extends ObjectBase {

    // 怪兽图片
    private static BufferedImage[] imgs;

    // 回调函数，用于与奥特曼碰撞后，对奥特曼无敌状态的设置
    public InvincibleHook invincibleHook;

    /**
     * 静态代码块，让类加载的时候将怪兽的图片加载到内存中
     */
    static {
        final int COUNT = 5; // 图片张数
        imgs = new BufferedImage[COUNT];
        for (int i=0; i<COUNT; i++) {
            imgs[i] = ImageUtil.loadBufferedImage(Constant.MONSTER_IMG[i]);
        }
    }

    // 怪兽类型
    public int type;
    // 小金牛
    private static final int ABSOLUTE_DIAVOLO = 1;
    // 小金人
    private static final int ABSOLUTE_TARTARUS = 2;
    // 塞古
    private static final int SEGMEGER = 3;
    // 布鲁顿
    private static final int BULLTON = 4;
    // 基三
    private static final int KYRIELOID = 5;

    // 指定速度
    private static final int BARRIER_SPEED = 3;

    // 怪兽血量
    public int heart;

    // 怪兽被击败所获得分数
    public int scoreReceived;

    // 如果是BOSS，也需要一个无敌状态
    private boolean beamInvincible = false;

    public Monster() {}

    public Monster(InvincibleHook invincibleHook) {
        // 新的怪兽对象固定从窗口最右端开始生成
        x = Constant.FRAME_WIDTH;
        // 速度固定
        speed = BARRIER_SPEED;
        // y坐标、宽度、高度、血量、类型不固定，需要随机生成
        generateRandomElement();
        // 生成矩形
        rect = new Rectangle();
        // 一个用于奥特曼与怪兽碰撞后无敌状态相关设置的回调函数
        this.invincibleHook = invincibleHook;
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

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    /**
     * 随机选取一种类型，然后确定每种类型的对应成员变量的取值
     */
    public void generateRandomElement() {
        // 五种类型的怪兽（小金牛，小金人，塞古，布鲁顿，基三）
        int[] randomType = new int[]{1, 2, 3, 4, 5};
        Random random = new Random();
        // 从四种类型中随机挑一种
        int typeIndex = random.nextInt(5);
        int typeSelected = randomType[typeIndex];

        y = generateRandomY(typeSelected);

        switch (typeSelected) {
            case 1: // 小金牛
                type = 1;
                heart = 14;
                width = imgs[0].getWidth();
                height = imgs[0].getHeight();
                scoreReceived = 10;
                break;
            case 2: // 小金人
                type = 2;
                heart = 14;
                width = imgs[1].getWidth();
                height = imgs[1].getHeight();
                scoreReceived = 12;
                break;
            case 3: // 塞古
                type = 3;
                heart = 6;
                width = imgs[2].getWidth();
                height = imgs[2].getHeight();
                scoreReceived = 5;
                break;
            case 4: // 布鲁顿
                type = 4;
                heart = 6;
                width = imgs[3].getWidth();
                height = imgs[3].getHeight();
                scoreReceived = 8;
                break;
            case 5: // 基三
                type = 5;
                heart = 12;
                width = imgs[4].getWidth();
                height = imgs[4].getHeight();
                scoreReceived = 10;
                break;
        }
    }

    /**
     * 根据不同的类型绘制怪兽
     */
    @Override
    public void drawSelf(Graphics g) {
        // 判断怪兽是否已移动出屏幕左边界
        if (judgeOutOfScreen()) {
            return;
        }

        // 与光线的碰撞检测
        judgeCollideWithBeam();

        if (this.heart < 1) {
            return;
        }

        // 与奥特曼的碰撞检测
        judgeCollideWithUltraMan();

        // 根据type确定需要绘制的怪兽类型，并进行绘制
        drawBasedOnType(g);
    }

    /**
     * 判断障碍物是否已移动出屏幕左边界（障碍物从右向左移动）
     */
    public boolean judgeOutOfScreen() {
        // 判断是否移除屏幕外
        if (x < -imgs[type-1].getWidth()) {
            System.out.println("屏幕外，得分-50");
            scoreCounter.subScore(50);
            monsters.remove(this);
            return true;
        }
        return false;
    }

    /**
     * 与光弹、光线的碰撞检测
     */
    public void judgeCollideWithBeam() {
        // 尚未进入屏幕中的障碍物，不进行碰撞检测
        if (this.x > Constant.FRAME_WIDTH) {
            return ;
        }
        for (int i=0; i<beams.size(); i++) {
            if (this.getRect().intersects(beams.get(i).getRect())) {
                this.heart -= beams.get(i).getDamageAmount();
                if (this instanceof Boss) {

                }
                if (this.height < 1) {
                    eliminateLogic();
                }
                return ;
            }
        }
        for (int i=0; i<simpleShells.size(); i++) {
            if (this.getRect().intersects(simpleShells.get(i).getRect())) {
                this.heart -= 2;
                // 同时这个光弹对象也需要被删除，避免线程刷新时保留效果，导致障碍物被同一光弹多次攻击
                simpleShells.remove(simpleShells.get(i));
                if (this.heart < 1) {
                    eliminateLogic();
                }
                return ;
            }
        }
    }

    /**
     * 处理普通怪兽或BOSS被击败的逻辑
     */
    public void eliminateLogic() {
        this.heart = 0;
        if (this instanceof Boss) {
            boss = null;
        } else {
            monsters.remove(this);
        }
        scoreCounter.addScore(this.scoreReceived);
    }

    /**
     * 与奥特曼的碰撞检测
     */
    public boolean judgeCollideWithUltraMan() {
        // 判断奥特曼矩形与障碍物矩形是否相交
        if (!ultraMan.isInvincible()) {
            // 如果奥特曼不是无敌状态
            if (this.getRect().intersects(ultraMan.getRect())) {
                // 生命值-1
                ultraMan.setHeart(ultraMan.getHeart() - 1);
                System.out.println("撞上啦");
                if (ultraMan.getHeart() < 0) {
                    System.out.println("生命值耗尽");
                }
                // 碰到障碍物后，奥特曼短时间内无敌，以避免一直卡在一个障碍物上
                ultraMan.setInvincible(true);
                // 回调函数，在新的线程中将无敌时间结束后的奥特曼设为不无敌状态
                invincibleHook.setNonInvincible(ultraMan);
                return true;
            }
        }
        return false;
    }

    /**
     * 根据type确定需要绘制的类型（从上往下，从下往上，悬空，悬空可动）
     * 并进行绘制
     */
    public void drawBasedOnType(Graphics g) {
        switch (type) {
            case ABSOLUTE_DIAVOLO:
                drawMobile(g, 8, false);
                break;
            case ABSOLUTE_TARTARUS:
                drawMobile(g, 8, false);
                break;
            case SEGMEGER:
                drawHover(g);
                break;
            case BULLTON:
                drawMobile(g, 25, true);
                break;
            case KYRIELOID:
                drawMobile(g, 10, false);
                break;
        }
    }

    /**
     * 绘制静止怪兽（相对较弱的不会动）
     */
    private void drawHover(Graphics g) {
        // 绘制
        g.drawImage(imgs[type-1], x, y, null);
        // 处理障碍物的物理矩形
        drawRect(g);
        this.x -= speed;
    }

    // 该状态用来控制可动对象的上下移动
    public boolean mobile = true;

    /**
     * 绘制可动的怪兽
     * （这里的可动指的不是基础的从屏幕划过去的移动，而是怪兽自身的移动，所以与speed变量区分开）
     * @param g
     * @param movingSpeed 移动速度
     * @param movingLAndR 是否能左右移动
     */
    public void drawMobile(Graphics g, int movingSpeed, boolean movingLAndR) {
        // 绘制
        g.drawImage(imgs[type-1], x, y, null);

        // 处理障碍物的物理矩形
        drawRect(g);
        this.x -= speed;

        if (mobile) {
            y += movingSpeed;
            if (y > (int) Constant.FRAME_HEIGHT - height) {
                mobile = false;
            }
        } else if (!mobile) {
            y -= movingSpeed;
            if (y < (int) 100) {
                mobile = true;
            }
        }

//        if (movingLAndR) {
//            boolean controlLAndR = true;
//            if (controlLAndR) {
//                x += 10;
//                if (x > (int) Constant.FRAME_WIDTH - width) {
//                    controlLAndR = false;
//                }
//            } else if (!controlLAndR) {
//                x -= 10;
//                if (x < (int) 300) {
//                    controlLAndR = true;
//                }
//            }
//        }
    }

    /**
     * 判断何时进行绘制下一组障碍物
     */
    public boolean hasBeenIntoFrame() {
        return Constant.FRAME_WIDTH - x > 50;
    }

    /**
     * 绘制障碍物的碰撞矩形
     */
    public void drawRect(Graphics g) {
//        // 绘制障碍物矩形
//        g.setColor(Color.blue);
//        g.drawRect(x1, y1, w1, height);
        setRectangle(x, y, width, height);
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
