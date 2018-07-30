package org.jleopard.mall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j;
import org.jleopard.Msg;
import org.jleopard.PageTable;
import org.jleopard.mall.model.Order;
import org.jleopard.mall.model.OrderKey;
import org.jleopard.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

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
public class OrderController extends BaseController{

    @Autowired
    OrderService orderService;

    /**
     *  获取订单信息 分页显示
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/a/order")
    public PageTable getOrder(@RequestParam(value = "page", defaultValue = "1") Integer page,
                              @RequestParam(value = "limit", defaultValue = "20") Integer pageSize){
        PageHelper.startPage(page, pageSize);
        List<Order> orders =orderService.select();
        PageInfo<Order> orderPageInfo = new PageInfo<>(orders);
        return PageTable.success().count(orderPageInfo.getTotal()).put(orders);
    }

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
}
