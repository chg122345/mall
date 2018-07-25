package org.jleopard.util;



import java.util.Random;

import static org.jleopard.Primarys.PRIMARY_LENGTH;
import static org.jleopard.Primarys.RANDOM_SOURCE;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-06-06 下午5:46
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 * 产生随机字符串
 */
public class RandomKeyUtils {

    /**
     * 产生随机字符串
     * @param prefix
     * @return
     */
    public static String generateString(String prefix) {
        Random random = new Random();
        int length = PRIMARY_LENGTH;
        String source = RANDOM_SOURCE;
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = source.charAt(random.nextInt(source.length()));
        }
        String result = prefix + new String(text);
        return result;
    }
}
