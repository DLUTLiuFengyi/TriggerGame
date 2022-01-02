package com.others.flappy.util;

import java.awt.*;

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

    // 小鸟图片资源（初始，往上飞，往下飞，往左飞，往右飞，静止）
    public static final String[] BIRD_IMG = {"img/bird_normal.png", "img/bird_up.png",
            "img/bird_down.png", "img/bird_up.png", "img/bird_down.png", "img/bird_normal.png"};

    // 云彩图片
    public static final String[] CLOUD_IMG = {"img/cloud1.png", "img/cloud2.png"};

    // 障碍物图片
    public static final String[] BARRIER_IMG = {"img/barrier.png", "img/barrier_up.png", "img/barrier_down.png"};
}
