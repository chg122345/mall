package org.jleopard.util;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-25  下午6:15
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */

import java.util.Date;
import java.util.Random;

/**
 * 生成18位订单号
 */
public class RandomOrderSerialKeyHelper {

    private static RandomOrderSerialKeyHelper Instance=new RandomOrderSerialKeyHelper();

    public static RandomOrderSerialKeyHelper getInstance(){
        return Instance;
    }

    private RandomOrderSerialKeyHelper(){

    }

    public String getSerialKey(String args) {
        Random random=new Random();
        String temp="";
        for (int i=0;i<6;++i){
            temp+= String.valueOf(random.nextInt(10));
        }
        if ("".equals(args) || null == args){
            args="0000";
        }
        if (args.length() > 4){
            args=args.substring(args.length()-4);
        }
        StringBuilder str = new StringBuilder(18);

        str.append("16").append(temp).append(DateUtils.formatTime(new Date())).append(args);
        return str.toString();
    }

    public String getSerialKey() {
        Random random=new Random();
        String temp="";
        String subfix = "";
        for (int i=0;i<6;++i){
            temp+= String.valueOf(random.nextInt(10));
        }
        for (int i=0;i<2;++i){
            subfix+= String.valueOf(random.nextInt(10));
        }
        StringBuilder str = new StringBuilder(18);

        str.append("A1").append(temp).append(DateUtils.formatTime(new Date())).append("18").append(subfix);
        return str.toString();
    }

}
