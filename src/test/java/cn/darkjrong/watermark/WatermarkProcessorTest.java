package cn.darkjrong.watermark;

import cn.darkjrong.watermark.domain.WatermarkParam;
import cn.darkjrong.watermark.exceptions.WatermarkException;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class WatermarkProcessorTest {

    @Test
    public void image() throws WatermarkException {

        File file = new File("C:\\Users\\Administrator\\Documents\\6.png");
        File imageFile = new File("D:\\1.png");

        WatermarkParam param = WatermarkParam.builder()
                .file(file)
//                .text("杭州定川信息技术有限公司")
                .degree(330F)
                .fontSize(20)
                .alpha(0.1F)

                .imageFile(imageFile)
//                .xMove(100)
//                .yMove(100)
                .bespread(Boolean.TRUE)
                .color(Color.GRAY)
                .build();

        WatermarkUtils.addWatermark(param, new File("C:\\Users\\Administrator\\Documents\\62.png"));

    }

    @Test
    public void pdf() throws Exception {

        File file = new File("C:\\Users\\Administrator\\Documents\\数字化案例要素填写指南.pdf");
        File imageFile = new File("D:\\package\\watermark.png");

        WatermarkParam param = WatermarkParam.builder()
                .file(file)
//                .text("杭州定川信息技术有限公司")
//                .degree(0.0F)
                .fontSize(20)
                .imageFile(imageFile)
                .degree(330F)
//                .xMove(100)
                .alpha(0.1F)
//                .yMove(70)
                .bespread(Boolean.TRUE)
                .color(Color.GRAY)
                .build();

        WatermarkUtils.addWatermark(param, new File("C:\\Users\\Administrator\\Documents\\数字化案例要素填写指南-3.pdf"));


    }

    @Test
    public void excel() throws WatermarkException {

        File file = new File("F:\\1.xlsx");

        WatermarkParam param = WatermarkParam.builder()
                .file(file)
                .text("杭州定川信息技术有限公司")
                .degree(40.0F)
                .fontSize(100)
//                .imageFile(imageFile)
//                .xMove(30)
                .alpha(0.4F)
//                .yMove(30)
                .bespread(Boolean.TRUE)
                .color(Color.red).build();


        WatermarkUtils.addWatermark(param, new File("F:\\dem1o.xlsx"));


    }

    @Test
    public void word() throws WatermarkException {

        File file = new File("D:\\组件使用申请单.docx");
        InputStream fileStream;
        try {
            fileStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        File imageFile = new File("D:\\package\\watermark.png");

        WatermarkParam param = WatermarkParam.builder()
//                .file(file)
                .fileType("docx")
                .fileStream(fileStream)
//                .text("杭州定川信息技术有限公司")
                .degree(330.0F)
                .fontSize(20)
                .imageFile(imageFile)
//                .xMove(200)
                .alpha(0.1F)
//                .yMove(200)
                .bespread(Boolean.TRUE)
//                .color(Color.GRAY)
                .build();

        WatermarkUtils.addWatermark(param, new File("D:\\组件使用申请单35.docx"));


    }

    @Test
    public void ppt() throws WatermarkException {

        File file = new File("F:\\1.pptx");

        WatermarkParam param = WatermarkParam.builder()
                .file(file)
                .text("杭州定川信息技术有限公司")
                .degree(40.0F)
                .fontSize(50)
//                .imageFile(imageFile)
//                .xMove(30)
                .alpha(0.5F)
//                .yMove(30)
                .bespread(Boolean.TRUE)
                .color(Color.lightGray).build();


        WatermarkUtils.addWatermark(param, new File("F:\\2.pptx"));


    }

    @Test
    public void html() throws Exception {

        File file = new File("F:\\test\\pdfhtml.html");
        File imageFile = new File("F:\\3 - 副本.jpeg");

        WatermarkParam param = WatermarkParam.builder()
                .file(file)
                .text("杭州定川信息技术有限公司")
//                .degree(40.0F)
                .fontSize(30)
//                .imageFile(imageFile)
                .degree(60F)
                .xMove(100)
                .alpha(0.7F)
                .yMove(70)
                .bespread(Boolean.TRUE)
                .color(Color.lightGray).build();

        WatermarkUtils.addWatermark(param, new File("F:\\test\\pdfhtml.pdf"));


    }

    @Test
    public void rtf() throws WatermarkException {

        File file = new File("F:\\test\\1.rtf");
        File imageFile = new File("F:\\4 - 副本.jpeg");

        WatermarkParam param = WatermarkParam.builder()
                .file(file)
                .text("杭州定川信息技术有限公司")
                .degree(20.0F)
                .fontSize(50)
//                .imageFile(imageFile)
//                .xMove(200)
                .alpha(0.5F)
//                .yMove(200)
                .bespread(Boolean.FALSE)
                .color(Color.lightGray).build();

        WatermarkUtils.addWatermark(param, new File("F:\\test\\111.rtf"));


    }



}
