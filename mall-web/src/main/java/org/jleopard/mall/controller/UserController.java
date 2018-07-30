package org.jleopard.mall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jleopard.Msg;
import org.jleopard.PageTable;
import org.jleopard.mall.model.User;
import org.jleopard.mall.service.UserService;
import org.jleopard.mall.util.MD5Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-25  上午11:28
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
@RestController
@RequestMapping("/user")
@Log4j
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    @RequiresRoles(value = {"admin"})
    @RequiresAuthentication
    @RequiresPermissions(value = "admin:select")
    @GetMapping("/getUserList")
    public PageTable userList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                              @RequestParam(value = "limit", defaultValue = "20") Integer pageSize){
        PageHelper.startPage(page,pageSize);
        List<User> users = userService.selectAll();
        PageInfo<User> userPageInfo = new PageInfo<>(users);
        return PageTable.success().put(users).count(userPageInfo.getTotal());
    }

    /**
     * 新增 id为空则表示新增
     * 更新 --> 判断传过来的id是否非空
     * @param user
     * @return
     */
    @PostMapping("/user")
    public Msg userAdd(User user){
        log.info("用户信息-->" + user);
        if (!StringUtils.isEmpty(user.getPassword())){
            user.setPassword(MD5Helper.md5(user.getPassword(), user.getEmail()));
        }
        if (!StringUtils.isEmpty(user.getId())){
            user = userService.updateById(user);
            if (user != null){
                return Msg.msg(200,"更新成功.");
            }
        }else {
            if (userService.checkEmail(user.getEmail())){
                return Msg.msg(user.getEmail() + "该邮箱已被占用，请更换邮箱.");
            }
            user = userService.insert(user);
            if (user != null){
                return Msg.success();
            }
        }

        return Msg.fail();
    }

    @PutMapping("/user")
    public Msg userUpdate(@RequestBody User user){
        log.info("用户信息-->" + user);
        if (!StringUtils.isEmpty(user.getPassword())){
            user.setPassword(MD5Helper.md5(user.getPassword(), user.getEmail()));
        }
        if (!StringUtils.isEmpty(user.getId())){
            user = userService.updateById(user);
            if (user != null){
                return Msg.msg(200,"更新成功.");
            }
        }

        return Msg.fail();
    }

    @DeleteMapping("/user")
    public Msg userDelete(@RequestBody String ids){
        log.info("id信息-->" + ids);
        String[] id = ids.split("-");
        try {
            Arrays.stream(id).forEach(i -> {
                userService.deleteById(i);
            });
        } catch (Exception e){
            log.error("批量删除用户出错.", e);
            return Msg.fail();
        }

        return Msg.success();
    }
}
