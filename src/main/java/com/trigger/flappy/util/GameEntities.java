package com.trigger.flappy.util;

import com.trigger.flappy.object.*;
import com.trigger.flappy.object.NirvanaBeam;
import com.trigger.flappy.object.SimpleShell;

import java.util.ArrayList;
import java.util.List;

/**
 * 游戏主窗口所用的静态实体
 */
public class GameEntities {

    // 奥特曼对象
    public static UltraMan ultraMan = new UltraMan();

    // 得分计数器
    public static ScoreCounter scoreCounter;

    // 用于控制怪兽生成速度的计数器
    public static int monsterCount;
    // 用于控制MP恢复速度的计数器
    public static int manaCount;

    // 简易光弹列表
    public static List<SimpleShell> simpleShells = new ArrayList<>();

    // 超-必杀技光线列表
    public static List<NirvanaBeam> beams = new ArrayList<>();

    // 障碍物列表
    public static List<Barrier> barriers = new ArrayList<>();
    // 怪兽列表
    public static List<Monster> monsters = new ArrayList<>();
    // BOSS
    public static Boss boss = null;

    // 击中BOSS一次之后的光线，会被添加进此队列中，避免同一条光线持续对BOSS造成多次伤害
    public static List<ObjectBase> bossIgnoreBeams = new ArrayList<>();
}
