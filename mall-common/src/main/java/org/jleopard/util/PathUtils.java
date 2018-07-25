package org.jleopard.util;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-06-07 下午5:02
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */

/**
 * 图片路径
 */
public class PathUtils {

    private static String separator = System.getProperty("file.separator"); // 文件类型

    public static String getUploadImgBasePath(){
        String basePath = "";
        String os=System.getProperty("os.name");    // 获取系统 linux windows
        if (os.toLowerCase().startsWith("win")){
            basePath = "D:/mall/images/";
        } else {
            basePath = "/home/mall/images/";
        }
        basePath = basePath.replace("/",separator);
        return basePath;
    }
}
