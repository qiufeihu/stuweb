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
public class Demo07 {

    public static void main(String[] args) throws Exception {
        String path01 = "/Users/QiuFeihu/Documents/Word/1/";
        String path02 = "/Users/QiuFeihu/Documents/Word/1he/";
        File file=new File(path01);
        File[] tempList = file.listFiles();

        List<String> imgList = new ArrayList<String>();
        for(File f : tempList){
            if(f.getPath().endsWith(".jpg")||f.getPath().endsWith(".png")){
                imgList.add(f.getPath());
            }
        }


        //3*6
        int count = imgList.size();
        int pageSize = 5*10;
        int pageTotal = count%pageSize == 0 ? count/pageSize : count/pageSize+1;

        int index = 0;
        int end = pageSize;

        for(int j=1;j<=pageTotal;j++){
             //A4是2480*3508象素 210*297毫米
            //620 465   438
            BufferedImage tag = new BufferedImage(2480,3508,BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = tag.createGraphics();

            System.out.println("第:"+j+"页");
            System.out.println("index:"+index+",end:"+end);

            int row = 0;
            int col = 0;

            for(int i=index;i<end;i++){
                System.out.println("行:"+row+",列:"+col);

                int x =  496*col;
                int y = 355*row;
                Image img = ImageIO.read(new File(imgList.get(i)));
                Color color = new Color(255, 255, 255, 255);

                g2d.drawImage(img, x, y,img.getWidth(null),img.getHeight(null),null);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,1.0f)); //透明度设置开始

                if((i+1)%5 == 0){
                    row++;col = 0;
                }else {
                    col++;
                }
                System.out.println(imgList.get(i));

            }
            index += pageSize;
            end = index + pageSize;
            end = end > count ? count : end;

            FileOutputStream out = new FileOutputStream(path02+j+".jpg");

            ImageIO.write(tag,"jpg", out);//写图片

            out.close();

        }
        System.out.println("PPT总数:"+count);
        System.out.println("总页数:"+pageTotal);
    }
}
