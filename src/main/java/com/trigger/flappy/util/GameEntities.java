package com.trigger.flappy.util;

import com.trigger.flappy.object.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 游戏主窗口所用的静态实体
 */
public class GameEntities {

    // 奥特曼对象
    public static UltraMan ultraMan = new UltraMan();

    public static ScoreCounter scoreCounter;

    // 简易光弹列表
    public static List<SimpleShell> simpleShells = new ArrayList<>();

    // 发射的光线之所以是列表，主要是为了上一条光线还没飞出屏幕，角色能发射第二条光线
    public static List<Beam> beams = new ArrayList<>();

    // 障碍物列表
    public static List<Barrier> barriers = new ArrayList<>();
    // 怪兽列表
    public static List<Monster> monsters = new ArrayList<>();
}
