package org.jleopard.mall.service;

import org.jleopard.mall.model.User;

import java.util.List;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-21  上午11:56
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
public interface UserService {

    int deleteById(String id);

    int deleteBySelective(User user);

    User insert(User user);

    User selectById(String id);

    List<User> selectBySelective(User user);

    User updateById(User user);

    boolean checkEmail(String email);


    User selectByEmail(String email);

    List<User> selectAll();
}
