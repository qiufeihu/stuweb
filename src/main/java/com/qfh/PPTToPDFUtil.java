package com.qfh;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * PPT转PDF工具类
 * 作者:邱飞虎
 * 时间:2016年09月04日12:09:01
 */
public class PPTToPDFUtil {


    /**
     * 生成PDF
     * @param path01 导出PPT图片地址
     * @return
     */
    public static Boolean createPDF(String path01){

        try{
            String path02 = path01+"合并后/";
            File file = new File(path02);
            if(!file.exists()){
                file.mkdir();
            }
            String pdfPath = path02+"合并后.pdf";
            List<String> srcList = imgJoint(path01,path02);  //合并图片
            imgsToPdf(srcList,pdfPath); //生成PDF
            //删除图片
            for(File f : file.listFiles()){
                if(f.getPath().endsWith(".jpg") || f.getPath().endsWith(".png")){
                    f.delete();
                }
            }
            System.out.print("运行完毕创建成功!pdf地址:"+pdfPath);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 多张图片合并为一张图片
     * @param path01 图片目录
     * @param path02 合并后的图片目录
     * @return
     * @throws Exception
     */
    public static List<String> imgJoint(String path01,String path02) throws Exception{


        File file=new File(path01);
        File[] tempList = file.listFiles();

        List<String> imgList = new ArrayList<String>();
        for(File f : tempList){
            if(f.getPath().endsWith(".jpg")||f.getPath().endsWith(".png")){
                imgList.add(f.getPath());
            }
        }
        if(imgList.size() == 0){
            return null;
        }
        Image firstImg = ImageIO.read(new File(imgList.get(0)));
        int imgWidth = firstImg.getWidth(null);
        int imgHeight = firstImg.getHeight(null);
        System.out.println("imgWidth:"+imgWidth+",imgHeight:"+imgHeight);
        //A4是2480*3508象素 210*297毫米
        int pageWidth = 2480;
        int pageHeigth = 3508;
        int count = imgList.size();

        int rowSize = pageWidth/imgWidth;
        int colSize = pageHeigth/imgHeight;

        System.out.println("rowSize:"+rowSize+",colSize:"+colSize);
        int pageSize = rowSize*colSize;
        int pageTotal = count%pageSize == 0 ? count/pageSize : count/pageSize+1;
        System.out.println("pageSize:"+pageSize+",pageTotal:"+pageTotal);
        int index = 0;
        int end = pageSize;

        List<String> srcImgList = new ArrayList<String>();
        for(int j=1;j<=pageTotal;j++){

            BufferedImage tag = new BufferedImage(pageWidth,pageHeigth,BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = tag.createGraphics();
            g2d.setColor(Color.white);
            g2d.fillRect(0, 0,pageWidth,pageHeigth);
            System.out.println("第:"+j+"张");
            System.out.println("index:"+index+",end:"+end);

            int row = 0;
            int col = 0;

            for(int i=index;i<end;i++){
                System.out.println("行:"+row+",列:"+col);

                Image img = ImageIO.read(new File(imgList.get(i)));
                int x =  img.getWidth(null)*col;
                int y = img.getHeight(null)*row;

                g2d.drawImage(img, x, y,img.getWidth(null),img.getHeight(null),null);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,1.0f)); //透明度设置开始

                if((i+1)%rowSize == 0){
                    row++;col = 0;
                }else {
                    col++;
                }
                System.out.println(imgList.get(i));

            }
            index += pageSize;
            end = index + pageSize;
            end = end > count ? count : end;

            String imgSrc = path02+j+".jpg";
            srcImgList.add(imgSrc);
            FileOutputStream out = new FileOutputStream(imgSrc);

            ImageIO.write(tag,"jpg", out);//写图片

            out.close();

        }
        System.out.println("PPT总数:"+count);
        System.out.println("图片数:"+pageTotal);

        return srcImgList;
    }

    /**
     * 图片批量转pdf
     * @param imagList 图片地址数组
     * @param pdfPath pdf地址
     * @return
     */
    public static File imgsToPdf(List<String> imagList,String pdfPath) {

        Document doc = new Document(PageSize.A4,0,0,0,0);
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(pdfPath));
            doc.open();
            for (int i = 0; i < imagList.size(); i++) {
                doc.newPage();
                String  src = imagList.get(i);
                com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(src);

                float heigth = img.getHeight();
                float width = img.getWidth();
                int percent = getPercent2(heigth, width);
                System.out.println(src+" 比例:"+percent*3);
                img.setAlignment(com.itextpdf.text.Image.MIDDLE);
                img.scalePercent(percent+3);// 表示是原来图像的比例;
                doc.add(img);
            }
            doc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File mOutputPdfFile = new File(pdfPath);
        if (!mOutputPdfFile.exists()) {
            mOutputPdfFile.deleteOnExit();
            return null;
        }
        return mOutputPdfFile;
    }

    /**
     * 第一种解决方案 在不改变图片形状的同时，判断，如果h>w，则按h压缩，否则在w>h或w=h的情况下，按宽度压缩
     *
     * @param h
     * @param w
     * @return
     */

    public static int getPercent(float h, float w) {
        int p = 0;
        float p2 = 0.0f;
        if (h > w) {
            p2 = 297 / h * 100;
        } else {
            p2 = 210 / w * 100;
        }
        p = Math.round(p2);
        return p;
    }

    /**
     * 第二种解决方案，统一按照宽度压缩 这样来的效果是，所有图片的宽度是相等的，自我认为给客户的效果是最好的
     *
     *
     */
    public static int getPercent2(float h, float w) {
        int p = 0;
        float p2 = 0.0f;
        p2 = 530 / w * 100;
        p = Math.round(p2);
        return p;
    }

    /**
     * 测试
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        String path01 = "/Users/QiuFeihu/Documents/Word/2/";

        createPDF(path01);

    }

}
