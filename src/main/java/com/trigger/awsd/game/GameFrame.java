package com.trigger.awsd.game;

import com.image.ImageUtil;
import com.trigger.awsd.method.BeamHitTheBossHook;
import com.trigger.awsd.method.CollideInvincibleHook;
import com.trigger.awsd.object.*;
import com.trigger.awsd.object.NirvanaBeam;
import com.trigger.awsd.object.SimpleShell;
import com.trigger.awsd.util.Constant;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import static com.trigger.awsd.util.GameEntities.*;

/**
 * 游戏的主界面类
 */
public class GameFrame extends Frame {

    private Background background;
//    private Timer timer;
    private Record record;

    // 游戏状态（普通战斗，boss战，游戏结束）
    private static int gameStatus = 1;
    private final static int NORMAL_BATTLE = 1;
    private final static int BOSS_BATTLE = 2;
    private final static int GAME_OVER = 3;

    // BOSS状态（未创建，正在战斗，已被打败）
    private int bossStatus = 0;
    private static int BOSS_NO_INIT = 0;
    private static int BOSS_IN_BATTLE = 1;
    private static int BOSS_BEAT = 2;

    // 普通怪兽状态（正常生成，停止生成）
    private static int createMonsterStatus = 1;
    private final static int CONTINUOUS_CREATE = 1;
    private final static int STOP_CREATE = 2;

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

        /**
         * 将游戏对象进行初始化
         */
        initGame();

        /**
         * 用于刷新图片的线程
         */
        GameRun gameRun = new GameRun();
        gameRun.start();

        /**
         * 按键监听器，监听玩家的按键操作
         */
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                pressKey(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                releaseKey(e);
            }
        });
    }

    /**
     * 按键
     */
    private void pressKey(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP: // 按下方向键上键
                ultraMan.action(1);
                break;
            case KeyEvent.VK_DOWN:
                ultraMan.action(2);
                break;
            case KeyEvent.VK_LEFT:
                ultraMan.action(3);
                break;
            case KeyEvent.VK_RIGHT:
                ultraMan.action(4);
                break;
            case KeyEvent.VK_A: // 按下A键，发射简易光弹
                ultraMan.action(6);
                createSimpleShell();
                break;
            case KeyEvent.VK_F: // 按下S键，发射超-必杀技
                ultraMan.action(7);
                createNirvanaBeam();
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
    private void releaseKey(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP: // 如果松开的是方向键上键
                ultraMan.action(5);
                break;
            case KeyEvent.VK_DOWN:
                ultraMan.action(5);
                break;
            case KeyEvent.VK_LEFT:
                ultraMan.action(5);
                break;
            case KeyEvent.VK_RIGHT:
                ultraMan.action(5);
                break;
        }
    }

    /**
     * 对游戏中的对象进行实例化
     */
    public void initGame() {
        ultraMan = new UltraMan();
        background = new Background();
//        timer = new Timer();
        scoreCounter = new ScoreCounter();
        record = new Record();
        monsterCount = 0;
        manaCount = 0;
    }

    /**
     * 执行游戏的线程，不断地将游戏对象绘制到屏幕画布上
     * 这个通过调用repaint()让jvm去执行update()方法，不断地进行绘制刷新
     * 因此，具体的绘制方法写在重载的update()方法中
     */
    class GameRun extends Thread {
        @Override
        public void run() {
            // 不断地绘制
            while (true) {
                manaCount += 1;
                if (manaCount > 3000) {
                    manaCount = 0;
                }
                if (ultraMan.getHeart() < 1) { // 生命值归0，切换到游戏结束模式
                    gameStatus = GAME_OVER;
                }
                if (gameStatus == NORMAL_BATTLE) { // 普通战斗模式
                    bossStatus = BOSS_NO_INIT;
                    // 此处加这个判断条件是为了修正从普通怪兽清空后到BOSS生成这段时间过长的bug
                    if (createMonsterStatus != STOP_CREATE) {
                        monsterCount += 1;
                    }
                    // 控制怪兽对象生成速度
                    if (monsterCount % 30 == 0 && createMonsterStatus == CONTINUOUS_CREATE) {
                        // 不断创建怪兽对象
                        createMonsters();
                    }
                    if (monsterCount % 1000 == 0) { // 准备切换到BOSS战模式
                        monsterCount = 1000;
                        System.out.println("准备切换到BOSS战");
                        // 先停止生成普通怪兽
                        createMonsterStatus = STOP_CREATE;
                        // 切换为boss战模式，停止自动生成普通怪兽
                        if (monsters.size() < 1) {
                            System.out.println("切换为BOSS战");
                            // 普通怪兽列表清空后，才转换为BOSS战模式
                            gameStatus = BOSS_BATTLE;
                        }
                    }
//                }
                } else if (gameStatus == BOSS_BATTLE) {
                    createBoss();
                    if (boss == null) { // BOSS被打败，切换到普通战斗模式
                        hasBeatBoss();
                    }
                }
                // 通过调用repaint() 让jvm去执行update方法，进行重新的绘制
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
        if (gameStatus == NORMAL_BATTLE) { // 普通战斗状态
            /**
             * 缓存思想解决屏幕闪烁问题 双缓冲技术
             * 思路：先创建一个空的图片，把所有组件先绘制在空的图片上
             * 然后把绘制好的图片一次性绘制到主窗口中
             */
            // 得到缓存画布的画笔
            Graphics graphics = bufferedImage.getGraphics();
            // 将相关游戏对象先画到缓存画布上
            background.drawSelf(graphics);
            scoreCounter.drawSelf(graphics);
            record.setCurrentScores(scoreCounter.getScores());
            record.drawSelf(graphics);
            // 怪兽绘制
            for (int i=0; i<monsters.size(); i++) {
                monsters.get(i).drawSelf(graphics);
            }
            // 奥特曼绘制
            ultraMan.drawSelf(graphics);
            // 光弹绘制
            for (int i=0; i<simpleShells.size(); i++) {
                simpleShells.get(i).drawSelf(graphics);
            }
            // 光线绘制
            for (int i=0; i<beams.size(); i++) {
                beams.get(i).drawSelf(graphics);
            }
            // 一次性将所有游戏对象绘制到屏幕中
            g.drawImage(bufferedImage, 0, 0, null);

        } else if (gameStatus == GAME_OVER) { // 游戏结束状态
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
        } else if (gameStatus == BOSS_BATTLE) { // BOSS战状态
            // 得到缓存画布的画笔
            Graphics graphics = bufferedImage.getGraphics();
            // 将相关游戏对象先画到缓存画布上
            background.drawSelf(graphics);
            scoreCounter.drawSelf(graphics);
            record.setCurrentScores(scoreCounter.getScores());
            record.drawSelf(graphics);
            // BOSS绘制
            if (boss != null) {
                boss.drawSelf(graphics);
            }
            // 奥特曼绘制
            ultraMan.drawSelf(graphics);
            // 光弹绘制
            for (int i=0; i<simpleShells.size(); i++) {
                simpleShells.get(i).drawSelf(graphics);
            }
            // 光线绘制
            for (int i=0; i<beams.size(); i++) {
                beams.get(i).drawSelf(graphics);
            }
            // 一次性将所有游戏对象绘制到屏幕中
            g.drawImage(bufferedImage, 0, 0, null);
        }
    }

    /**
     * 创建简易光弹对象
     */
    public void createSimpleShell() {
        // MP不足，无法发射
        if (ultraMan.getMana() - SimpleShell.getManaValue() < 0) {
            return;
        }
        // 扣除所需MP
        ultraMan.subMana(SimpleShell.getManaValue());
        SimpleShell simpleShell = new SimpleShell(ImageUtil.loadBufferedImage(Constant.SIMPLE_SHELL_IMG),
                ultraMan.getX()+80, ultraMan.getY()+30, 45, 45, 15);
        simpleShells.add(simpleShell);
    }

    /**
     * 创建光线对象
     */
    public void createNirvanaBeam() {
        // MP不足，无法发射
        if (ultraMan.getMana() - NirvanaBeam.getManaValue() < 0) {
            return;
        }
        // 扣除所需MP
        ultraMan.subMana(NirvanaBeam.getManaValue());
        NirvanaBeam beam = new NirvanaBeam(ImageUtil.loadBufferedImage(Constant.BEAM_IMG),
                ultraMan.getX()+50, ultraMan.getY()+30, 50, 10);
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
            Barrier last = barriers.get(barriers.size()-1);
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
        Monster monster = new Monster(new CollideInvincibleHook());
        monsters.add(monster);
    }

    /**
     * 创建BOSS对象
     */
    private void createBoss() {
        if (boss == null && bossStatus == BOSS_NO_INIT) {
            boss = new Boss(new CollideInvincibleHook(), new BeamHitTheBossHook());
            bossStatus = BOSS_IN_BATTLE;
        }
    }

    /**
     * 打败BOSS，处理收尾逻辑
     */
    private void hasBeatBoss() {
        bossStatus = BOSS_BEAT;
        // 为了保证打败BOSS之后，普通怪兽能从屏幕右边界开始出现
        // 而不是突然出现在之前消失的位置，需要将原来留在怪兽列表中的怪兽删除
        monsters.clear();
        System.out.println("打败BOSS");
        gameStatus = NORMAL_BATTLE;
        createMonsterStatus = CONTINUOUS_CREATE;
        monsterCount += 1;
    }

    /**
     * 重新开始游戏
     */
    public void restart() {
        simpleShells.clear();
        beams.clear();
        monsters.clear();
        boss = null;
        ultraMan.restart(); // 将奥特曼位置初始化
        scoreCounter.restart();
        monsterCount = 0;
        gameStatus = NORMAL_BATTLE;
        bossStatus = BOSS_NO_INIT;
        createMonsterStatus = CONTINUOUS_CREATE;
    }
}
