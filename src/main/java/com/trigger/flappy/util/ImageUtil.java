package com.trigger.flappy.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 调整图片相关属性工具类
 */
public class ImageUtil {

    /**
     * 调整图片尺寸
     * @param scaling 调整比例
     */
    public static BufferedImage changeScale(BufferedImage originImg, Double scaling) {
        if (!(scaling instanceof Double)) {
            throw new IllegalArgumentException("scaling参数需要是Double类型，例如0.5");
        }
        int wid = originImg.getWidth();
        int hei = originImg.getHeight();
        int newWid = (int) Math.round(originImg.getWidth() * scaling);
        int newHei = (int) Math.round(originImg.getHeight() * scaling);
        BufferedImage newImg = new BufferedImage(newWid, newHei, originImg.getType());
        Graphics2D g = newImg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(originImg, 0, 0, newWid, newHei, 0, 0, wid, hei, null);
        g.dispose();
        return newImg;
    }

    /**
     * 去除图片背景
     * https://blog.csdn.net/lkh1992/article/details/109010328
     *
     * TODO: 抠图逻辑太简单粗暴，只适用于纯色背景
     */
    public static BufferedImage removeBackground(BufferedImage img) {
        int alpha = 255; // 透明度
        // 取图片边缘颜色作为对比对象
        String removeRGB = convertRGB(img.getRGB(img.getMinX(), img.getMinY()));
        // 遍历y轴像素
        for (int y = img.getMinY(); y<img.getHeight(); y++) {
            // 遍历x轴像素
            for (int x = img.getMinX(); x<img.getWidth(); x++) {
                // 该像素点颜色
                int rgb = img.getRGB(x, y);
                // 设置为透明背景
                if (convertRGB(rgb).equals(removeRGB)) {
                    alpha = 0;
                } else {
                    alpha = 255;
                }
                rgb = (alpha << 24) | (rgb & 0x00ffffff);
                img.setRGB(x, y, rgb);
            }
        }
        return img;
    }

    /**
     * 去除图片背景，宽松版
     */
    public static BufferedImage removeBackgroundRange(BufferedImage img) {
        int alpha = 255; // 透明度
        // 取图片边缘颜色作为对比对象
        String removeRGB = convertRGB(img.getRGB(img.getMinX(), img.getMinY()));
        // 遍历y轴像素
        for (int y = img.getMinY(); y<img.getHeight(); y++) {
            // 遍历x轴像素
            for (int x = img.getMinX(); x<img.getWidth(); x++) {
                // 该像素点颜色
                int rgb = img.getRGB(x, y);
                // 设置为透明背景
                if (similarColor(rgb, img.getRGB(img.getMinX(), img.getMinY()))) {
                    alpha = 0;
                } else {
                    alpha = 255;
                }
                rgb = (alpha << 24) | (rgb & 0x00ffffff);
                img.setRGB(x, y, rgb);
            }
        }
        return img;
    }

    /**
     * 获取RGB参数，改为字符串格式表示
     */
    public static String convertRGB(int color) {
        int red = (color & 0xff0000) >> 16; // 获取R位
        int green = (color & 0x00ff00) >> 8; // 获取G位
        int blue = (color & 0x0000ff); // 获取B位
        return red + "," + green + "," + blue;
    }

    /**
     * 两个颜色相近
     */
    private static boolean similarColor(int rgb1, int rgb2) {
        if (rgb1 == rgb2) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 旋转图片
     */
    public static BufferedImage rotate(double degree, BufferedImage originImg) {
        int wid = originImg.getWidth();
        int hei = originImg.getHeight();
        BufferedImage newImg = new BufferedImage(wid, hei, originImg.getType());
        Graphics2D g = newImg.createGraphics();
        g.rotate(Math.toRadians(degree), wid/2, hei/2);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(originImg, 0, 0, wid, hei, 0, 0, wid, hei, null);
        g.dispose();
        return newImg;
    }

    /**
     * 水平翻转图片
     */
    public static BufferedImage flip(BufferedImage originImg) {
        int wid = originImg.getWidth();
        int hei = originImg.getHeight();
        BufferedImage newImg = new BufferedImage(wid, hei, originImg.getType());
        Graphics2D g = newImg.createGraphics();
        g.drawImage(originImg, 0, 0, wid, hei, wid, 0, 0, hei, null);
        g.dispose();
        return newImg;
    }

    /**
     * 读取图片
     */
    public static BufferedImage readImage(String imgPath) {
        try {
            BufferedImage img = ImageIO.read(new FileInputStream(imgPath));
            return img;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 写入图片
     */
    public static void writeImg(BufferedImage img, String savePath) {
        try {
            ImageIO.write(img, "png", new File(savePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
