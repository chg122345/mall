package org.jleopard.mall.service.impl;

import org.springframework.stereotype.Service;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-21  上午11:58
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
@Service
public class UserServiceImpl implements UserService {
    @Override
    public String say(String name) {
        return name+ ": 这是我说的话..";
    }
}
