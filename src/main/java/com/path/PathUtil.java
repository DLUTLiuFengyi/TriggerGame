package com.path;

public class PathUtil {

    public static String classPath = PathUtil.class.getResource("/").getPath();

    public static void main(String[] args) {
        String imgsPath = classPath + "../../img/";
        System.out.println(classPath);
        System.out.println(imgsPath);
    }
}
