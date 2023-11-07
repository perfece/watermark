package cn.darkjrong.watermark;

import cn.darkjrong.watermark.domain.WatermarkParam;
import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.darkjrong.watermark.factory.*;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * 水印工具类
 *
 * @author Rong.Jia
 * @date 2021/08/13 13:50:07
 */
public class WatermarkUtils {

    private static final List<WatermarkProcessor> processors = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(WatermarkUtils.class);

    static {
        processors.add(new ExcelWatermarkProcessor());
        processors.add(new ImageWatermarkProcessor());
        processors.add(new PdfWatermarkProcessor());
        processors.add(new PowerPointWatermarkProcessor());
        processors.add(new WordWatermarkProcessor());
    }

    /**
     * 添加处理器
     *
     * @param processor 处理器
     */
    public static void addProcessor(WatermarkProcessor processor) {
        if (ObjectUtil.isNotNull(processor)) {
            processors.add(processor);
        }
    }

    /**
     * 添加水印
     *
     * @param watermarkParam 水印参数
     * @param outputFile     输出文件
     * @throws WatermarkException 水印异常
     */
    public static void addWatermark(WatermarkParam watermarkParam, File outputFile) throws WatermarkException {
        LicenseUtils.verificationLicense();
        FileUtil.writeBytes(addWatermark(watermarkParam), outputFile);
    }

    /**
     * 添加水印
     *
     * @param watermarkParam 水印参数
     * @throws WatermarkException 水印异常
     */
    public static byte[] addWatermark(WatermarkParam watermarkParam) throws WatermarkException {
        LicenseUtils.verificationLicense();
        File file = watermarkParam.getFile();
        try {
            if (null == file && null != watermarkParam.getFileStream()) {
                String tmpPath = FileUtil.file("").getPath() + File.separator + IdUtil.fastSimpleUUID() + "." + watermarkParam.getFileType();
//                String tmpPath =FileUtil.getUserHomePath()+"/"+ IdUtil.fastSimpleUUID()+"."+watermarkParam.getFileType();
                System.out.println("===tmpPath:" + tmpPath);
                File tmpFile = new File(tmpPath);
                file = FileUtil.writeFromStream(watermarkParam.getFileStream(), tmpFile);
                watermarkParam.setFile(file);
                //暂存文件，加完水印删除
                watermarkParam.setTmpFile(tmpFile);
            }
        } catch (Exception e) {
            throw new WatermarkException("文件解析异常,请确保文件流【fileStream】和文件类型【fileType】正确");
        }
        File finalFile = file;
        WatermarkProcessor processor = processors.stream().filter(a -> a.supportType(finalFile)).findAny().orElse(null);
        if (ObjectUtil.isNull(processor)) {
            logger.error("The watermark does not support the file format is: {}", FileTypeUtils.getFileType(file));
            throw new WatermarkException("不支持文件格式为 " + FileTypeUtils.getFileType(file) + " 的水印处理");
        }
        handlerWatermarkFile(watermarkParam);
        return processor.addWatermark(watermarkParam);
    }

    /**
     * 处理程序水印文件
     *
     * @param watermarkParam 水印参数
     * @throws WatermarkException 水印的例外
     */
    private static void handlerWatermarkFile(WatermarkParam watermarkParam) throws WatermarkException {

        File imageFile = watermarkParam.getImageFile();
        if (FileUtil.exist(imageFile)) {
//            String tmpPath =FileUtil.getUserHomePath()+"/watermarktmp/"+ IdUtil.fastSimpleUUID()+"."+watermarkParam.getFileType();
            String tmpPath = FileUtil.file("").getPath() +File.separator + IdUtil.fastSimpleUUID() + ".png";
            System.out.println("==tmp watermark:"+tmpPath);
            //todo 缓存处理后的水印图片，下次使用，若有直接使用
            File tempFile = FileUtil.copyFile(imageFile, new File(tmpPath));
            ImageUtils.createImage(tempFile, watermarkParam.getDegree(), watermarkParam.getAlpha());
            watermarkParam.imageFile(tempFile);
        } else if (StrUtil.isNotBlank(watermarkParam.getText())) {
            watermarkParam.imageFile(ImageUtils.createImage(watermarkParam.getText(),
                    watermarkParam.getColor(), watermarkParam.getFontSize(),
                    watermarkParam.getDegree(), watermarkParam.getAlpha()));
        }
    }


}
