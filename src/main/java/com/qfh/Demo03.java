package com.qfh;

/**
 * Created by QiuFeihu on 16/9/3.
 */
import java.awt.AlphaComposite;

import java.awt.Graphics2D;

import java.awt.Image;

import java.awt.image.BufferedImage;

import java.io.File;

import java.io.FileOutputStream;

import javax.imageio.ImageIO;

public class Demo03 {

    /**

     *

     * @param filesrc

     * @param logosrc

     * @param outsrc

     * @param x 位置

     * @param y 位置

     */

    public void composePic(String filesrc,String logosrc,String outsrc,int x,int y) {

        try {

            File bgfile = new File(filesrc);

            Image bg_src = javax.imageio.ImageIO.read(bgfile);

            File logofile = new File(logosrc);

            Image logo_src = javax.imageio.ImageIO.read(logofile);

            int bg_width = bg_src.getWidth(null);

            int bg_height = bg_src.getHeight(null);

            int logo_width = logo_src.getWidth(null);;

            int logo_height = logo_src.getHeight(null);

            BufferedImage tag = new BufferedImage(2480,3508, BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = tag.createGraphics();

            g2d.drawImage(bg_src, 0, 0, bg_width, bg_height-20, null);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,1.0f)); //透明度设置开始

            g2d.drawImage(logo_src,x,y,logo_width,logo_height, null);

            //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER)); //透明度设置 结束

            FileOutputStream out = new FileOutputStream(outsrc);

            ImageIO.write(tag, "jpg", out);//写图片

/* JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);

encoder.encode(tag);*/

            out.close();

        }catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static void main(String args[]) {
        String path01 = "/Users/QiuFeihu/Documents/Word/统计学/统计学汇总/";
        String path02 = "/Users/QiuFeihu/Documents/Word/统计学/";
        Long star = System.currentTimeMillis();

        Demo03 pic = new Demo03();

        pic.composePic(path01+"幻灯片001.jpg",path01+"幻灯片002.jpg",path02+"out.jpg",0,340);

        Long end =System.currentTimeMillis();

        System.out.print("time====:"+(end-star));

    }

}
