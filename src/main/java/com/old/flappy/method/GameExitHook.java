package com.old.flappy.method;

/**
 * 自动退出游戏（未完成）
 */
public class GameExitHook {

    private static int duration = 5000;

    public GameExitHook() {}

    /**
     * duration秒后自动退出游戏
     */
    public void autoExitGame(Boolean gameExit) {
        // 需要另开线程进行对是否自动结束游戏的判断，否则会出现卡顿现象
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(duration);
                    // 等待超过数秒后，若用户没有选择重新开始游戏，则自动退出
                    synchronized (gameExit) {
                        if (gameExit) {
                            System.out.println("自动退出游戏");
                            System.exit(0);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
