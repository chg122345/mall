package org.jleopard.mall.util;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-27  下午12:05
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
public class MD5Helper {

    public static String md5(String str,String salt){
        return new Md5Hash(str,salt).toString();
    }
}
