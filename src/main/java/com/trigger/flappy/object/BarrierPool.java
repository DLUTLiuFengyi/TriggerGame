package com.trigger.flappy.object;

import java.util.ArrayList;
import java.util.List;

/**
 * 障碍物对象池，避免频繁创建对象导致的内存溢出
 */
public class BarrierPool {

    // 管理池中所有对象的容器
    private static List<Barrier> pool = new ArrayList<Barrier>();
    // 池中初始化对象个数
    public static final int initCount = 16;
    // 池中最大对象个数
    public static final int maxCount = 20;

    static {
        for (int i=0; i<initCount; i++) {
            pool.add(new Barrier());
        }
    }

    /**
     * 从池中获取对象
     */
    public static Barrier getPool() {
        int size = pool.size();
        // 池不为空时
        if (size > 0) {
            // 从池中移除，并返回一个数据
            System.out.println("从对象池中拿走一个障碍物对象");
            return pool.remove(size-1);
        } else {
            System.out.println("new一个障碍物对象");
            return new Barrier();
//            Barrier barrier = new Barrier();
//            pool.add(barrier);
//            return pool.remove(size-1);
        }
    }

    /**
     * 把对象归还容器中
     */
    public static void setPool(Barrier barrier) {
        if (pool.size() < maxCount) {
            pool.add(barrier);
            System.out.println("障碍物对象已归还容器");
        } else {

        }
    }
}
