package com.trigger.flappy.game;

import com.image.GameUtil;
import com.others.flappy.game.GameBackground;
import com.others.flappy.game.GameFrontGround;
import com.others.flappy.method.BirdInvincibleHook;
import com.trigger.flappy.method.InvincibleHook;
import com.trigger.flappy.object.UltraMan;
import com.trigger.flappy.util.Constant;
import com.trigger.flappy.object.Beam;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class GameFrame extends Frame {

    private Beam beam;
    private UltraMan ultraMan;

    private GameBackground gameBackground;
    private GameFrontGround gameFrontGround;
    private GameBarrierLayer gameBarrierLayer;

    private BufferedImage bufferedImage = new BufferedImage(
            Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR
    );

    public GameFrame() {
        // 窗口是否可见
        setVisible(true);
        // 窗口大小
        setSize(Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT);
        // 窗口标题
        setTitle(Constant.FRAME_TITLE);
        // 初始化位置
        setLocation(Constant.FRAME_X, Constant.FRAME_Y);
        // 窗口大小不可变
        setResizable(false);

        // 窗口关闭事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 正常关闭窗口 结束程序
                System.exit(0);
            }
        });

        initGame(new InvincibleHook());

        /**
         * 用于刷新图片的线程
         */
        GameRun gameRun = new GameRun();
        gameRun.start();

        /**
         * 添加按键监听器，监听小鸟
         */
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                add(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                minu(e);
            }
        });
    }

    /**
     * 按键
     */
    private void add(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP: // 如果按下的是方向键上键
                ultraMan.fly(1);
                break;
            case KeyEvent.VK_DOWN:
                ultraMan.fly(2);
                break;
            case KeyEvent.VK_LEFT:
                ultraMan.fly(3);
                break;
            case KeyEvent.VK_RIGHT:
                ultraMan.fly(4);
                break;
            case KeyEvent.VK_SPACE: // 如果按下的是空格键
                if (ultraMan.getHeart() < 1) { // 只有小鸟死亡时才生效
                    restart();
                }
                break;
        }
    }

    /**
     * 松键
     */
    private void minu(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP: // 如果松开的是方向键上键
                ultraMan.fly(5);
                break;
            case KeyEvent.VK_DOWN:
                ultraMan.fly(6);
                break;
            case KeyEvent.VK_LEFT:
                ultraMan.fly(7);
                break;
            case KeyEvent.VK_RIGHT:
                ultraMan.fly(8);
                break;
        }
    }

    /**
     * 重新开始游戏
     */
    public void restart() {
        gameBarrierLayer.restart(); // 清空障碍物对象列表
        ultraMan.restart(); // 将小鸟位置初始化
    }

    /**
     * 对游戏中的对象进行实例化
     */
    public void initGame(InvincibleHook invincibleHook) {
        ultraMan = new UltraMan();
        beam = new Beam(GameUtil.loadBufferedImage(Constant.BEAM_IMG),
                ultraMan.getX()+3, ultraMan.getY(), 30, 10, 5);
        gameBackground = new GameBackground();
        gameFrontGround = new GameFrontGround();
        gameBarrierLayer = new GameBarrierLayer(invincibleHook);
    }

    class GameRun extends Thread {
        @Override
        public void run() {
            // 不断地绘制
            while (true) {
                // 通过调用repaint() 让JVM去执行update方法，进行重新的绘制
                repaint();
                try {
                    // 每30ms调用一次
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 所有需要绘制的内容都在此方法上进行调用绘制
     * @param g
     */
    @Override
    public void update(Graphics g) {

        if (ultraMan.getHeart() > 0) {
            /**
             * 缓存思想解决屏幕闪烁问题 双缓冲技术
             * 思路：先创建一个空的图片，把所有组件先绘制在空的图片上
             * 然后把绘制好的图片一次性绘制到主窗口中
             */
            // 得到缓存图片的画笔
            Graphics graphics = bufferedImage.getGraphics();
            gameBackground.draw(graphics);
            gameFrontGround.draw(graphics);
            gameBarrierLayer.draw(graphics, ultraMan);
            ultraMan.drawSelf(graphics);
            // 一次性将图片绘制到屏幕中
            g.drawImage(bufferedImage, 0, 0, null);
        } else {
            // 否则 游戏结束
            String over = "Game Over";
            // 设置画笔
            g.setColor(Color.red);
            g.setFont(new Font("微软雅黑", 1, 45));
            // 画出
            g.drawString(over, (int) (com.others.flappy.util.Constant.FRAME_WIDTH / 2), (int)(com.others.flappy.util.Constant.FRAME_HEIGHT / 2));

            String reset = "Press Space to Restart";
            g.setColor(Color.orange);
            g.setFont(new Font("微软雅黑", 1, 30));
            g.drawString(reset, (int) (com.others.flappy.util.Constant.FRAME_WIDTH / 2), (int)(com.others.flappy.util.Constant.FRAME_HEIGHT / 2) + 50);
        }
    }
}
