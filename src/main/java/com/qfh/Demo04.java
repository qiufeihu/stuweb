package com.qfh;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.*;

/**
 * Created by QiuFeihu on 16/9/3.
 */
public class Demo04 {

    public static void main(String[] args) throws  Exception {
        String path01 = "/Users/QiuFeihu/Documents/Word/统计学汇总/";
        String path02 = "/Users/QiuFeihu/Documents/Word/统计学汇总合并图片/";
        List<String> srcList = new ArrayList<String>();
        String path = path01;
        File file=new File(path);
        File[] tempList = file.listFiles();

        //3*6
        int count = tempList.length/3*6+1;
        int index = 1;
        int end = 3*6;
        for(int j=1;j<=count;j++){
        //A4是2480*3508象素 210*297毫米
        BufferedImage tag = new BufferedImage(2130,3000, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = tag.createGraphics();
        int row = 0;
        int col = 0;

        for(int i=index;i<=end;i++){
            File f = tempList[i];
            System.out.println(f.getPath());
            File bgfile = new File(f.getPath());
            Image img = ImageIO.read(bgfile);
            //720 540
            System.out.println();


            int x =  710*col;
            int y = 500*row;


            System.out.println(img.getWidth(null)+","+img.getHeight(null));
            g2d.drawImage(img, x, y,img.getWidth(null),img.getHeight(null), null);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,1.0f)); //透明度设置开始

            if(i%3 == 0){
                row++;col = 0;
            }else{
                col++;
            }


        }
            index += 3*6;
            end = index + 3*6;
            index = index > tempList.length-1 ? tempList.length-1 : index;
            end = end > tempList.length-1 ? tempList.length-1 : end;
        FileOutputStream out = new FileOutputStream(path02+j+".jpg");

        ImageIO.write(tag,"jpg", out);//写图片

        out.close();

        }
    }
}
