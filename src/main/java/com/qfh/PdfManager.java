package com.qfh;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfManager {
    public static File Pdf(String path,int count,String fileType,String mOutputPdfFileName) {


        String TAG = "PdfManager";
        Document doc = new Document(PageSize.A4,0,0,0,0);
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(mOutputPdfFileName));
            doc.open();
            for (int i = 1; i <= count; i++) {
                doc.newPage();
                String  src = path+i+fileType;
                Image img = Image.getInstance(src);

                float heigth = img.getHeight();
                float width = img.getWidth();
                int percent = getPercent2(heigth, width);
                System.out.println(src+" 比例:"+percent*3);
                img.setAlignment(Image.MIDDLE);
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

        File mOutputPdfFile = new File(mOutputPdfFileName);
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

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //String path01 = "/Users/QiuFeihu/Documents/Word/统计学汇总合并图片/";
        String path01 = "/Users/QiuFeihu/Documents/Word/1he/";
        String path02 = "/Users/QiuFeihu/Documents/Word/";
        List<String> srcList = new ArrayList<String>();
        File file01=new File(path01);
        File[] tempList = file01.listFiles();
        List<String> imageUrllist = new ArrayList<String>();
        for (int i=1;i<tempList.length;i++){
            File f = tempList[i];
            if(f.getPath().endsWith(".jpg")||f.getPath().endsWith(".png")){
                imageUrllist.add(f.getPath());
            }

        }

        String pdfUrl = path02+"统计学汇总15页.pdf";
        File file = PdfManager.Pdf(path01,imageUrllist.size(),".jpg",pdfUrl);
        try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
