package com.others.flappy.game;

import com.others.flappy.method.BirdInvincibleHook;
import com.others.flappy.method.GameExitHook;
import com.others.flappy.util.Constant;
import com.others.flappy.object.Bird;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

/**
 * 游戏主窗口
 */
public class GameFrame extends Frame {

    private GameBackground gameBackground;
    private GameFrontGround gameFrontGround;
    private GameBarrierLayer gameBarrierLayer;

    private Bird bird;

    private BufferedImage bufferedImage = new BufferedImage(
            Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR
    );

    private volatile Boolean gameExit = false; // 游戏结束状态，用于控制程序安全退出

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

        initGamg(new BirdInvincibleHook());

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

//        /**
//         * 新开一个线程，当游戏结束后用户在一定时间内没有选择重新开始时
//         * 自动将游戏程序终止退出
//         */
//        GameExit gameExit = new GameExit();
//        gameExit.start();
    }

    /**
     * 按键
     */
    private void add(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP: // 如果按下的是方向键上键
                bird.fly(1);
                break;
            case KeyEvent.VK_DOWN:
                bird.fly(2);
                break;
            case KeyEvent.VK_LEFT:
                bird.fly(3);
                break;
            case KeyEvent.VK_RIGHT:
                bird.fly(4);
                break;
            case KeyEvent.VK_SPACE: // 如果按下的是空格键
                if (bird.getHeart() < 1) { // 只有小鸟死亡时才生效
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
                bird.fly(5);
                break;
            case KeyEvent.VK_DOWN:
                bird.fly(6);
                break;
            case KeyEvent.VK_LEFT:
                bird.fly(7);
                break;
            case KeyEvent.VK_RIGHT:
                bird.fly(8);
                break;
        }
    }

    /**
     * 重新开始游戏
     */
    public void restart() {
//        synchronized (gameExit) {
//            gameExit = false;
//        }
        gameBarrierLayer.restart(); // 清空障碍物对象列表
        bird.restart(); // 将小鸟位置初始化
    }

    /**
     * 对游戏中的对象进行实例化
     */
    public void initGamg(BirdInvincibleHook birdInvincibleHook) {
        gameBackground = new GameBackground();
        bird = new Bird();
        gameFrontGround = new GameFrontGround();
        gameBarrierLayer = new GameBarrierLayer(birdInvincibleHook);
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

        if (bird.getHeart() > 0) {
            // 得到缓存图片的画笔
            Graphics graphics = bufferedImage.getGraphics();

            /**
             * 把所有要绘制的东西绘制在画板上，再一次性绘制出来，解决屏幕闪烁问题
             */
            gameBackground.draw(graphics);
            gameFrontGround.draw(graphics);
            gameBarrierLayer.draw(graphics, bird);
            bird.draw(graphics);
            // 一次性将图片绘制到屏幕中
            g.drawImage(bufferedImage, 0, 0, null);
        } else {
            // 否则 游戏结束
            String over = "Game Over";
            // 设置画笔
            g.setColor(Color.red);
            g.setFont(new Font("微软雅黑", 1, 45));
            // 画出
            g.drawString(over, (int) (Constant.FRAME_WIDTH / 2), (int)(Constant.FRAME_HEIGHT / 2));

            String reset = "Press Space to Restart";
            g.setColor(Color.orange);
            g.setFont(new Font("微软雅黑", 1, 30));
            g.drawString(reset, (int) (Constant.FRAME_WIDTH / 2), (int)(Constant.FRAME_HEIGHT / 2) + 50);

//            gameExit = true;
//            new GameExitHook().autoExitGame(gameExit);
        }
    }

    /**
     * 游戏结束后，在一定时间内没有选择重新开始游戏的话
     * 则退出程序
     */
    class GameExit extends Thread {
        @Override
        public void run() {
            while (true) {
                if (gameExit) {
                    // 等待5s后退出程序
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (gameExit) {
                        System.exit(0);
                    }
                }
            }
        }
    }

}
