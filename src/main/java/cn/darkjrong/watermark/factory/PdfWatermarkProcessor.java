package cn.darkjrong.watermark.factory;

import cn.darkjrong.watermark.FileTypeUtils;
import cn.darkjrong.watermark.domain.WatermarkParam;
import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.aspose.pdf.Document;
import com.aspose.pdf.ImageStamp;
import com.aspose.pdf.MarginInfo;
import com.aspose.pdf.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

/**
 * pdf处理器
 *
 * @author Rong.Jia
 * @date 2021/08/13 10:06:40
 */
public class PdfWatermarkProcessor extends AbstractWatermarkProcessor {

    private static final Logger logger = LoggerFactory.getLogger(PdfWatermarkProcessor.class);

    @Override
    public Boolean supportType(File file) {
        return FileTypeUtils.isPdf(file) || FileTypeUtils.isHtml(file);
    }

    @Override
    public void addWatermark(WatermarkParam watermarkParam, File target) throws WatermarkException {
        FileUtil.writeBytes(this.addWatermark(watermarkParam), target);
    }

    @Override
    public byte[] addWatermark(WatermarkParam watermarkParam) throws WatermarkException {

        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        InputStream imageInput = null;
        try {
            inputStream = getInputStream(watermarkParam.getFile());
            Document pdfDocument = new Document(inputStream);
            imageInput = FileUtil.getInputStream(watermarkParam.getImageFile());
            ImageStamp imageStamp = new ImageStamp(imageInput);

            Image srcImage = ImageIO.read(watermarkParam.getImageFile());

            //设置水印背景的宽高，还有透明度
            imageStamp.setHeight(srcImage.getWidth(null));
            imageStamp.setWidth(srcImage.getHeight(null));
            imageStamp.setOpacity(1 - watermarkParam.getAlpha());

            for (int i = 1; i <= pdfDocument.getPages().size(); i++) {
                Page page = pdfDocument.getPages().get_Item(i);
                if (!watermarkParam.getBespread()) {
                    page.addStamp(imageStamp);
                } else {
                    //让每一页的边距为0，也可以自定义设置
                    page.getPageInfo().setMargin(new MarginInfo(5, 5, 5, 5));
                    for (double y = 0; y < page.getPageInfo().getHeight(); y = y + imageStamp.getHeight() + watermarkParam.getYMove()) {
                        for (double x = 0; x < page.getPageInfo().getWidth(); x = x + imageStamp.getWidth() + watermarkParam.getXMove()) {
                            //设置图片位置的x,y轴，通过双重循环来达到铺满背景的目的
                            imageStamp.setXIndent(x);
                            imageStamp.setYIndent(y);
                            //添加水印
                            page.addStamp(imageStamp);
                        }
                    }
                }
            }
            outputStream = new ByteArrayOutputStream();
            pdfDocument.save(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            logger.error("Description Failed to add watermark to PDF :  {}", e.getMessage());
            throw new WatermarkException(e.getMessage());
        } finally {
            IoUtil.close(inputStream);
            IoUtil.close(outputStream);
            IoUtil.close(imageInput);
            delete(watermarkParam.getImageFile());
        }
    }
}
