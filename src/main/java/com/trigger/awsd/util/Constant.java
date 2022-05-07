package com.trigger.awsd.util;

import java.awt.*;

/**
 * 定义常规的游戏常量
 */
public class Constant {

    // 窗口大小
    public static final int FRAME_WIDTH = 1000;
    public static final int FRAME_HEIGHT = 800;

    // 窗口标题
    public static final String FRAME_TITLE = "Trigger Flappy";

    // 窗口初始化位置
    public static final int FRAME_X = 200;
    public static final int FRAME_Y = 200;

    // 背景图片路径
    public static final String BK_IMG_OATH = "img/background1.png";

    // 游戏背景颜色
    public static final Color BK_COLOR = new Color(0xEA7642);

    public static final String DURATION_RECORD_FILE_PATH = "game_file/durationRecord.txt";

    public static final String SCORES_RECORD_FILE_PATH = "game_file/scoresRecord.txt";

    // 奥特曼图片
    public static final String[] ULTRAMAN_IMG = {"img/trigger_sky.png", "img/trigger_sky.png",
            "img/trigger_power.png", "img/trigger_power.png",
            "img/trigger_normal.png", "img/trigger_normal.png",
            "img/ribut.png"
    };

    // 障碍物图片
    public static final String[] BARRIER_IMG = {"img/barrier.png", "img/barrier_up.png", "img/barrier_down.png"};

    // 怪兽图片
    public static final String[] MONSTER_IMG = {"img/monster1.png", "img/monster2.png", "img/monster3.png",
            "img/monster4.png", "img/monster5.png"};

    public static final String BOSS_IMG = "img/monster1.png";

    // 简易光弹图片
    public static final String SIMPLE_SHELL_IMG = "img/simple_shell.png";

    // 光线图片
    public static final String BEAM_IMG = "img/beam.png";

    // 飞机图片
    public static final String[] FIGHTER_IMG = {"img/cloud1.png", "img/cloud2.png"};
}
