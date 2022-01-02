package com.image;

import static com.image.ImageUtil.*;

public class ImageEdit {

    public static void main(String[] args) {
        String inputPath = "img/beam_origin.png";
        String savePath = inputPath.replace("_origin", "");
//        writeImg(removeBackground(changeScale(readImage(inputPath), 0.2)), savePath);
//        writeImg(rotate(180, removeBackground(changeScale(readImage(inputPath), 0.3))), savePath);
//        ImageUtil.writeImg(ImageUtil.flip(ImageUtil.changeScale(
//                ImageUtil.removeBackground(ImageUtil.readImage(inputPath)), 0.3)), savePath);
        writeImg(flip(changeScale(readImage(inputPath), 0.5)), savePath);
    }
}
