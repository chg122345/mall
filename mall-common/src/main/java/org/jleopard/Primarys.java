package org.jleopard;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-06-06 下午5:53
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */

/**
 * 生成主键的前缀及资源
 */
public interface Primarys {

    String RANDOM_SOURCE="abcdefghijklmnopqrstuvwxyz0123456789";
    String USER_ID="uu";
    String POST_PRIMARY_PREFIX="pt";
    String USER_ACTION_PRIMARY_PREFIX="ua";
    String USER_COLLECTION_PRIMARY_PREFIX="uc";
    String USER_MESSAGE_PRIMARY_PREFIX="um";
    int PRIMARY_LENGTH=30;  // 30 + 2


}
