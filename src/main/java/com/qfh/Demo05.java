package com.qfh;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by QiuFeihu on 16/9/4.
 */
public class Demo05 {

    public static void main(String[] args) throws Exception {
        String path01 = "/Users/QiuFeihu/Documents/Word/统计学汇总/";
        String path02 = "/Users/QiuFeihu/Documents/Word/统计学汇总合并图片/";
        File file=new File(path01);
        File[] tempList = file.listFiles();

        List<String> imgList = new ArrayList<String>();
        for(File f : tempList){
            if(f.getPath().endsWith(".jpg")||f.getPath().endsWith(".png")){
                imgList.add(f.getPath());
            }
        }


        //3*6
        int count = imgList.size()/(3*6);
        int index = 0;
        int end = 3*6;

        for(int j=1;j<=count;j++){

            BufferedImage tag = new BufferedImage(2130,3000, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = tag.createGraphics();

            System.out.println("第:"+j+"页");
            System.out.println("index:"+index+",end:"+end);
            int row = 0;
            int col = 0;

            for(int i=index;i<end;i++){
                System.out.println("行:"+row+",列:"+col);
                //720 540
                int x =  710*col;
                int y = 500*row;
                Image img = ImageIO.read(new File(imgList.get(i)));
                g2d.drawImage(img, x, y,img.getWidth(null),img.getHeight(null), null);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,1.0f)); //透明度设置开始

                if((i+1)%3 == 0){
                    row++;col = 0;
                }else {
                    col++;
                }

            }
            index += 3*6;
            end = end > tempList.length-1 ? tempList.length-1 : index + 3*6;

            FileOutputStream out = new FileOutputStream(path02+j+".jpg");

            ImageIO.write(tag,"jpg", out);//写图片

            out.close();

        }
    }
}
