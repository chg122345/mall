package org.jleopard.mall.service.impl;

import org.jleopard.Primarys;
import org.jleopard.mall.dao.UserMapper;
import org.jleopard.mall.model.User;
import org.jleopard.mall.service.UserService;
import org.jleopard.util.RandomKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

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

    @Autowired
    UserMapper userMapper;

    @Override
    public int deleteById(String id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteBySelective(User user) {
        return userMapper.deleteBySelective(user);
    }

    @Override
    public User insert(User user) {
        user.setId(RandomKeyUtils.generateString(Primarys.USER_ID));  // 生成的用户id
        if (userMapper.insertSelective(user) > 0){
            return user;
        }
        return null;
    }

    @Override
    public User selectById(String id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<User> selectBySelective(User user) {
        return userMapper.selectBySelective(user);
    }

    @Override
    public User updateById(User user) {
        if (userMapper.updateByPrimaryKeySelective(user) > 0){
            return user;
        }
        return null;
    }

    @Override
    public boolean checkEmail(String email) {
        User user = new User();
        user.setEmail(email);
        return !CollectionUtils.isEmpty(selectBySelective(user));
    }
}
