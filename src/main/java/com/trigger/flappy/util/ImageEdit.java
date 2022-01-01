package com.trigger.flappy.util;

import static com.trigger.flappy.util.ImageUtil.*;

public class ImageEdit {

    public static void main(String[] args) {
        String inputPath = "img/monster3_origin.png";
        String savePath = inputPath.replace("_origin", "");
//        writeImg(removeBackground(changeScale(readImage(inputPath), 0.2)), savePath);
//        writeImg(rotate(180, removeBackground(changeScale(readImage(inputPath), 0.3))), savePath);
        writeImg(flip(changeScale(removeBackground(readImage(inputPath)), 0.3)), savePath);
    }
}
