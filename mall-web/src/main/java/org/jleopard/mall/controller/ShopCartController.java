package org.jleopard.mall.controller;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.jleopard.Msg;
import org.jleopard.mall.model.Cart;
import org.jleopard.mall.model.Product;
import org.jleopard.mall.model.ProductKey;
import org.jleopard.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-26  下午6:57
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
@RestController
@RequestMapping("/cart")
@RequiresAuthentication
public class ShopCartController extends BaseController{

    @Autowired
    private ProductService productService;

    //处理新增购物项
    @RequestMapping("/add")
    public Msg addCart(HttpSession session, @RequestParam("id") String id, @RequestParam(value = "number",defaultValue = "1") Integer number) {
        ProductKey pk = new ProductKey();
        pk.setId(id);
        List<Product> products = productService.selectByIds(pk);
        Cart cart = (Cart)session.getAttribute("cart");
        if(cart==null) {
            cart=new Cart();
            session.setAttribute("cart", cart);
        }
        if (!CollectionUtils.isEmpty(products)){
            cart.addCart(products.get(0), number);
        }

        return Msg.msg("添加成功").put("cart",cart);

    }

    //处理移除购物项
    @RequestMapping("/del")
    public Msg delCart(HttpSession session, @RequestParam("id") String id) {
        Cart cart = (Cart)session.getAttribute("cart");
        if (cart != null){
            cart.getItems().remove(id);
            return Msg.msg("移除" + id + "商品成功").put("cart",cart);
        }
        return Msg.fail();
    }

    //清空购物车
    @RequestMapping("/clear")
    public Msg clearCart( HttpSession session) {
        Cart cart = (Cart)session.getAttribute("cart");
        if (cart != null){
            cart.getItems().clear();
            return Msg.success();
        }
        return Msg.fail();

    }

    @RequestMapping("/get")
    public Msg getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null){
            return Msg.success().put("cart", cart);
        }
       return Msg.fail();
    }
}
