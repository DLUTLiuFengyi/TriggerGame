package com.trigger.flappy.util;

import com.trigger.flappy.object.Barrier;
import com.trigger.flappy.object.Beam;
import com.trigger.flappy.object.ObjectBase;

import java.util.ArrayList;
import java.util.List;

public class GameUtil {

    // 发射的光线之所以是列表，主要是为了上一条光线还没飞出屏幕，角色能发射第二条光线
    public static List<Beam> beams = new ArrayList<>();
}
