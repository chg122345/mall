package org.jleopard.mall.util;

import lombok.extern.log4j.Log4j;
import org.jleopard.Msg;
import org.jleopard.util.PathUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-08-01  上午10:47
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
@Log4j
public class UploadFileHelper {

    /***
     * 上唇图片
     * @param spath 上传路径 xx
     * @param file
     * @return
     * @throws IOException
     */
    public static String upload(String spath,MultipartFile file) throws IOException {
        String path = PathUtils.getUploadImgBasePath();
        String fileName = file.getOriginalFilename();
        String realPath = path + spath + fileName;
        File filePath = new File(realPath);
        if (!filePath.getParentFile().exists()) {
            filePath.getParentFile().mkdirs();
        }
        file.transferTo(filePath);
        String uri = "/icon/" + spath + fileName;
        return uri;
    }
}
