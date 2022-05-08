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
    public static final String FRAME_TITLE = "Trigger AWSD";

    // 窗口初始化位置
    public static final int FRAME_X = 200;
    public static final int FRAME_Y = 200;

//    public static String picRoot = "./img/";
    public static String filesRoot = Constant.class.getResource("/").getPath() + "../../";
    public static String picRoot = filesRoot + "img/";
    public static String gameFileRoot = filesRoot + "game_file/";

    // 背景图片路径
    public static final String BK_IMG_OATH = picRoot + "/background1.png";

    // 游戏背景颜色
    public static final Color BK_COLOR = new Color(0xEA7642);

    public static final String DURATION_RECORD_FILE_PATH = gameFileRoot + "durationRecord.txt";

    public static final String SCORES_RECORD_FILE_PATH = gameFileRoot + "scoresRecord.txt";

    // 奥特曼图片
    public static final String[] ULTRAMAN_IMG = {
            picRoot + "trigger_sky.png", picRoot + "trigger_sky.png",
            picRoot + "trigger_power.png", picRoot + "trigger_power.png",
            picRoot + "trigger_normal.png", picRoot + "trigger_normal.png",
            picRoot + "ribut.png"
    };

    // 障碍物图片
    public static final String[] BARRIER_IMG = {
            picRoot + "barrier.png", picRoot + "barrier_up.png", picRoot + "barrier_down.png"
    };

    // 怪兽图片
    public static final String[] MONSTER_IMG = {
            picRoot + "monster1.png", picRoot + "monster2.png", picRoot + "monster3.png",
            picRoot + "monster4.png", picRoot + "monster5.png"
    };

    public static final String BOSS_IMG = picRoot + "monster1.png";

    // 简易光弹图片
    public static final String SIMPLE_SHELL_IMG = picRoot + "simple_shell.png";

    // 光线图片
    public static final String BEAM_IMG = picRoot + "beam.png";

    // 飞机图片
    public static final String[] FIGHTER_IMG = {
            picRoot + "cloud1.png", picRoot + "cloud2.png"
    };
}
