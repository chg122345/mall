package org.jleopard.mall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jleopard.Msg;
import org.jleopard.PageTable;
import org.jleopard.mall.model.Order;
import org.jleopard.mall.model.OrderItem;
import org.jleopard.mall.model.OrderKey;
import org.jleopard.mall.model.User;
import org.jleopard.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.jleopard.ResultKeys.ORDER;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-30  下午1:25
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
@RestController
@Log4j
@RequiresAuthentication
public class OrderController extends BaseController{

    @Autowired
    OrderService orderService;

    /**
     *  获取订单信息 分页显示
     * @param page
     * @param pageSize
     * @return
     */
    @RequiresRoles(value = {"admin"})
    @GetMapping("/a/order")
    public PageTable getOrder(@RequestParam(value = "page", defaultValue = "1") Integer page,
                              @RequestParam(value = "limit", defaultValue = "20") Integer pageSize){
        PageHelper.startPage(page, pageSize);
        List<Order> orders =orderService.select();
        PageInfo<Order> orderPageInfo = new PageInfo<>(orders);
        return PageTable.success().count(orderPageInfo.getTotal()).put(orders);
    }

    /**
     * 修改订单信息
     * @param order
     * @return
     */
    @RequiresRoles(value = {"admin"})
    @PutMapping("/a/order")
    public Msg updateOrder(@RequestBody Order order){
        log.info("订单信息-->" + order);
        if (!StringUtils.isEmpty(order.getId())){
            order = orderService.updateByIdSelective(order);
            if (order != null){
                return Msg.msg(200,"更新成功.");
            }
        }
        return Msg.fail();
    }

    /**
     * 批量删除订单信息
     * @param ids
     * @return
     */
    @RequiresRoles(value = {"admin"})
    @DeleteMapping("/a/order")
    @Transactional
    public Msg deleteProduct(@RequestBody String ids){
        log.info("id信息-->" + ids);
        String[] id = ids.split("-");
        try {
            Arrays.stream(id).forEach(i -> {
                OrderKey key = new OrderKey();
                key.setId(i);
                orderService.deleteByIds(key);
            });
        } catch (Exception e){
            log.error("批量删除订单出错.", e);
            return Msg.fail();
        }
        return Msg.success();
    }

    /**
     * 当前用户获取订单信息
     * @return
     */
    @RequiresRoles(value = {"user","admin"}, logical = Logical.OR)
    @GetMapping("/order")
    public Msg getOrder(){
        User user = getLoginUser();
        if (user != null){
            OrderKey key = new OrderKey();
            key.setMlUserId(user.getId());
            List<Order> orders = orderService.selectByIds(key);
            return Msg.success().put(ORDER,orders);
        }
        return Msg.fail();
    }

    /**
     * 用户生成订单
     * @param uid
     * @param aid
     * @return
     */
    @RequiresRoles(value = {"user","admin"}, logical = Logical.OR)
    @PostMapping(value = "/order")
    public Msg order(@RequestParam("mlUserId") String uid, @RequestParam("mlAddressId") Integer aid){
        Order order = new Order();
        order.setMlUserId(uid);
        order.setMlAddressId(aid);
        order.setNumber(5);
        order.setMoney(245.5);
        OrderItem o1 = new OrderItem();
        o1.setMlProductId("pdqsa9s8ecusv0cift4xdbndjargtaqs");
        o1.setMoney(56.2);
        o1.setNumber(2);
        OrderItem o2 = new OrderItem();
        o2.setMlProductId("pd0v3ytxuvvnh91ejgwweqhqxxa0ts2z");
        o2.setMoney(99.9);
        o2.setNumber(3);
        List<OrderItem> list = new ArrayList<>();
        list.add(o1);
        list.add(o2);
        order.setOrderItem(list);
        Order u = orderService.insert(order);
        if (u != null){
            return Msg.success().put("order",u);
        }
        return Msg.fail();
    }
}
