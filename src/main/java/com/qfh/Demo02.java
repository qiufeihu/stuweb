package com.qfh;


import java.io.*;

import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;

/**
 * Created by QiuFeihu on 16/9/3.
 */
public class Demo02 {


    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream(new File("/Users/QiuFeihu/Documents/Word/1.ppt"));
        String content = getDocument(fis);
        System.out.println(content.replaceAll("null",""));

    }


    public static String getDocument(InputStream is){
        StringBuffer content = new StringBuffer();
        try{
            SlideShow ss = new SlideShow(new HSLFSlideShow(is));//is 为文件的InputStream，建立SlideShow
            Slide[] slides = ss.getSlides();//获得每一张幻灯片
            for(int i=0;i<slides.length;i++){
                TextRun[] t = slides[i].getTextRuns();//为了取得幻灯片的文字内容，建立TextRun
                for(int j=0;j<t.length;j++){
                    String str = t[j].getText();
                    if(str.trim() == ""){
                        str = str.trim();
                    }
                    content.append(str);//这里会将文字内容加到content中去
                }
                content.append(slides[i].getTitle());
            }
            return content.toString();
        }catch(Exception ex){
            System.out.println(ex.toString());
        }
        return null;
    }
}



