package com.trigger.flappy.game;

import com.image.ImageUtil;
import com.trigger.flappy.method.InvincibleHook;
import com.trigger.flappy.object.*;
import com.trigger.flappy.util.Constant;
import com.trigger.flappy.util.GameUtil;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import static com.trigger.flappy.util.GameUtil.*;

public class GameFrame extends Frame {

    private Background background;
    private Timer timer;
    private Record record;

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
         * 添加按键监听器，监听飞行对象
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
            case KeyEvent.VK_UP: // 按下方向键上键
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
            case KeyEvent.VK_A: // 按下A键，发射简易光弹
                ultraMan.fly(6);
                createSimpleShell();
                break;
            case KeyEvent.VK_S: // 按下S键，发射光线
                ultraMan.fly(7);
                createBeam();
                break;
            case KeyEvent.VK_SPACE: // 按下空格键
                if (ultraMan.getHeart() < 1) { // 只有生命值归零时才生效
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
                ultraMan.fly(5);
                break;
            case KeyEvent.VK_LEFT:
                ultraMan.fly(5);
                break;
            case KeyEvent.VK_RIGHT:
                ultraMan.fly(5);
                break;
        }
    }

    /**
     * 重新开始游戏
     */
    public void restart() {
        simpleShells.clear();
        beams.clear();
        monsters.clear();
//        barriers.clear();
        ultraMan.restart(); // 将奥特曼位置初始化
        timer.restart();
    }

    /**
     * 对游戏中的对象进行实例化
     */
    public void initGame(InvincibleHook invincibleHook) {
        ultraMan = new UltraMan();
        background = new Background();
        timer = new Timer();
        record = new Record();
    }

    class GameRun extends Thread {
        @Override
        public void run() {
            int monsterCount = 0;
            // 不断地绘制
            while (true) {
                monsterCount += 1;
                if (monsterCount % 30 == 0) {
                    // 不断创建怪兽对象
                    createMonsters();
                    if (monsterCount > 3000) {
                        monsterCount = 1;
                    }
                }

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
            background.drawSelf(graphics);
            timer.drawSelf(graphics);
            record.setCurrentDuration((int)timer.computeDuration());
            record.drawSelf(graphics);
            for (int i=0; i<monsters.size(); i++) {
                monsters.get(i).drawSelf(graphics);
            }
            ultraMan.drawSelf(graphics);
            for (int i=0; i<simpleShells.size(); i++) {
                simpleShells.get(i).drawSelf(graphics);
            }
            for (int i=0; i<beams.size(); i++) {
                beams.get(i).drawSelf(graphics);
            }
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
        }
    }

    /**
     * 创建简易光弹对象
     */
    public void createSimpleShell() {
        SimpleShell simpleShell = new SimpleShell(ImageUtil.loadBufferedImage(Constant.SIMPLE_SHELL_IMG),
                ultraMan.getX()+80, ultraMan.getY()+30, 45, 45, 15);
        simpleShells.add(simpleShell);
    }

    /**
     * 创建光线对象
     */
    public void createBeam() {
        Beam beam = new Beam(ImageUtil.loadBufferedImage(Constant.BEAM_IMG),
                ultraMan.getX()+80, ultraMan.getY()+30, 750, 50, 10);
        beams.add(beam);
    }

    /**
     * 创建障碍物
     */
    public void createBarriers() {
        if (barriers.size() < 1) {
            Barrier barrier = new Barrier();
            barriers.add(barrier);
        } else {
            // 判断最后一个障碍物是否完全进入屏幕内（什么时候绘制下一组障碍物）
            Barrier last = barriers.get(GameUtil.barriers.size()-1);
            if (last.hasBeenIntoFrame()) {
                // 最后一个障碍物已进入屏幕内，则创建新的障碍物
                Barrier barrier = new Barrier();
                barriers.add(barrier);
            }
        }
    }

    /**
     * 创建怪兽对象
     */
    public void createMonsters() {
//        if (monsters.size() < 1) {
//            Monster monster = new Monster();
//            monsters.add(monster);
//        } else {
//            // 判断最后一个怪兽是否完全进入屏幕内（什么时候绘制下一个怪兽）
//            Monster last = monsters.get(monsters.size()-1);
//            if (last.hasBeenIntoFrame()) {
//                // 最后一个怪兽已进入屏幕内，则创建新的怪兽
//                Monster monster = new Monster();
//                monsters.add(monster);
//            }
//        }
        Monster monster = new Monster();
        monsters.add(monster);
    }
}
