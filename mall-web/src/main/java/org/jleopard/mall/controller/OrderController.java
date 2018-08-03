package org.jleopard.mall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jleopard.Msg;
import org.jleopard.PageTable;
import org.jleopard.mall.model.*;
import org.jleopard.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    @RequiresRoles(value = {"user","admin"}, logical = Logical.OR)
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
    @GetMapping("/user/getOrder")
    public PageTable getOrderByUser(@RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "limit", defaultValue = "20") Integer pageSize){
        User user = getLoginUser();
        if (user != null){
            OrderKey key = new OrderKey();
            key.setMlUserId(user.getId());
            PageHelper.startPage(page, pageSize);
            List<Order> orders = orderService.selectByIds(key);
            PageInfo<Order> orderPageInfo = new PageInfo<>(orders);
            return PageTable.success().count(orderPageInfo.getTotal()).put(orders);
        }
        return PageTable.fail();
    }

    /**
     * 用户生成订单
     * @param aid
     * @return
     */
    @RequiresRoles(value = {"user","admin"}, logical = Logical.OR)
    @PostMapping(value = "/user/order")
    public Msg order(@RequestParam("mlAddressId") Integer aid){
        Order order = new Order();
        order.setMlUserId(getLoginUser().getId());
        order.setMlAddressId(aid);
        List<OrderItem> list = new ArrayList<>();
        Cart cart = (Cart)getSession().getAttribute("cart");
        order.setNumber(cart.getTotalNumber());
        order.setMoney(cart.getTotalMoney());
        Map<String,CartItem> items = cart.getItems();
        items.forEach((k,v)-> list.add(new OrderItem(v.getNumber(),v.getMoney(),k)));
        order.setOrderItem(list);
        Order o = orderService.insert(order);
        if (o != null){
            getSession().removeAttribute("cart");
            return Msg.success().put("order",o);
        }
        return Msg.fail();
    }

    @RequiresRoles(value = {"user","admin","VIP1"}, logical = Logical.OR)
    @RequestMapping("/user/sureOrder")
    public Msg sureOrder(@RequestParam("id") String id){
        if (!StringUtils.isEmpty(id)){
            OrderKey key = new OrderKey();
            key.setId(id);
            key.setMlUserId(getLoginUser().getId());
            List<Order> orders = orderService.selectByIds(key);
            if (!CollectionUtils.isEmpty(orders)){
                Order o = new Order();
                o.setId(id);
                o.setStatus(Byte.valueOf("4"));
                o = orderService.updateByIdSelective(o);
                if (o != null){
                    return Msg.success().put(ORDER,o);
                }
            }
        }
        return Msg.fail();
    }
}
