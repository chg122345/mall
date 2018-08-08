package org.jleopard.mall.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jleopard.Msg;
import org.jleopard.PageTable;
import org.jleopard.mall.model.*;
import org.jleopard.mall.service.AddressService;
import org.jleopard.mall.service.OrderService;
import org.jleopard.mall.service.UserService;
import org.jleopard.mall.util.MD5Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.jleopard.AliPayConstant.*;
import static org.jleopard.ResultKeys.ADDRESS;
import static org.jleopard.ResultKeys.USER_INFO;

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
@RequiresAuthentication
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;

    @Autowired
    OrderService orderService;

    @GetMapping("/{page}")
    public ModelAndView userSkippage(@PathVariable("page") String page) {
        return  getModelAndView("user/" + page);
    }

    /**
     * 管理员查看用户列表
     * @param page
     * @param pageSize
     * @return
     */
    @RequiresRoles(value = {"admin"})
    @GetMapping("/getUserList")
    public PageTable userList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                              @RequestParam(value = "limit", defaultValue = "20") Integer pageSize){
        PageHelper.startPage(page,pageSize);
        List<User> users = userService.selectAll();
        PageInfo<User> userPageInfo = new PageInfo<>(users);
        return PageTable.success().put(users).count(userPageInfo.getTotal());
    }

    /**
     * 获取登录的用户信息
     * @return
     */
    @RequiresRoles(value = {"admin","user"},logical=Logical.OR)
    @GetMapping("/getUserInfo")
    public Msg userInfo(){
        User user = userService.selectById(getLoginUser().getId());
        if (user != null){
            return Msg.success().put(USER_INFO,user);
        }
        return Msg.fail();
    }
    /**
     * 新增 id为空则表示新增
     * 更新 --> 判断传过来的id是否非空
     * @param user
     * @return
     */
    @RequiresRoles(value = {"admin"})
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

    /**
     * 管理员修改用户
     * @param user
     * @return
     */
    @RequiresRoles(value = {"admin"})
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

    /**
     * 管理员删除用户
     * @param ids
     * @return
     */
    @RequiresRoles(value = {"admin"})
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

    /**
     * 根据id获取用户信息 必须登录账号
     * @param id
     * @return
     */
    @RequiresRoles(value = {"user","admin","VIP1"}, logical = Logical.OR)
    @GetMapping("/user/{id}")
    public Msg getUser(@PathVariable("id") String id){

        User user = userService.selectById(id);
        if (user != null){
            return Msg.success().put(USER_INFO,user);
        }
        return Msg.fail();
    }

    /**
     * 当前登录的用户修改信息
     * @param id
     * @return
     */
    @RequiresRoles(value = {"user","admin","VIP1"}, logical = Logical.OR)
    @PutMapping("/user/{id}")
    public Msg updateUser(@PathVariable("id") String id,User user){
        User u = getLoginUser();
        if (u != null){
            if (!id.equals(u.getId())){
                return Msg.msg("只能修改自己的信息哦..");
            }
            user.setId(id);
            user = userService.updateById(user);
            if (user != null){
                return Msg.success().put(USER_INFO,user);
            }
        }
        return Msg.fail();
    }

    /**
     * 新增收货地址
     * @param address
     * @return
     */
    @RequiresRoles(value = {"user","admin","VIP1"}, logical = Logical.OR)
    @PostMapping("/address")
    public Msg address(Address address){
        User u = getLoginUser();
        if (u != null){
           address.setMlUserId(u.getId());
            Address address1 = addressService.insert(address);
            if (address1 != null){
                return Msg.success().put(ADDRESS,address1);
            }
        }
        return Msg.fail();
    }

    /**
     * 获取收货地址
     * @return
     */
    @RequiresRoles(value = {"user","admin","VIP1"}, logical = Logical.OR)
    @GetMapping("/getAddress")
    public Msg address(){
        User u = getLoginUser();
        if (u != null){
            AddressKey key = new AddressKey();
            key.setMlUserId(u.getId());
            List<Address> address = addressService.selectByIds(key);
            return Msg.success().put(ADDRESS,address);
        }
        return Msg.fail();
    }

    /**
     * 修改密码
     * @param nickname
     * @param oldPass
     * @param newPass
     * @return
     */
    @RequiresRoles(value = {"user","admin","VIP1"}, logical = Logical.OR)
    @PostMapping("/change")
    public Msg changeUserInfo(@RequestParam("nickname") String nickname,
                              @RequestParam("oldPassword") String oldPass, @RequestParam("newPassword") String newPass){
        User user = getLoginUser();
        if (!user.getPassword().equals(MD5Helper.md5(oldPass, user.getEmail()))){
            return Msg.msg("旧密码不正确..");
        }
        User u = new User();
        u.setId(user.getId());
        u.setNickname(nickname);
        u.setPassword(MD5Helper.md5(newPass, user.getEmail()));
        if (userService.updateById(u) != null){
            SecurityUtils.getSubject().logout();
            return Msg.success();
        }
        return Msg.fail();
    }

    /**
     * 支付
     * @param id
     * @return
     * @throws AlipayApiException
     */
    @RequiresRoles(value = {"user","admin","VIP1"}, logical = Logical.OR)
    @GetMapping(value = "/pay/{id}",produces = "text/html; charset=UTF-8")
    public String pay(@PathVariable("id") String id) throws AlipayApiException {
        OrderKey key = new OrderKey();
        key.setId(id);
        List<Order> orders = orderService.selectByIds(key);
        if (!CollectionUtils.isEmpty(orders)){
            Order order = orders.get(0);
            //获得初始化的AlipayClient
            AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID,MERCHANT_PRIVATE_KEY, "json", CHAR_SET, PUBLIC_KEY, SING_TYPE);
            //设置请求参数
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            alipayRequest.setReturnUrl(RETURN_URL);
            alipayRequest.setNotifyUrl(NOTIFY_URL);

            //商户订单号，商户网站订单系统中唯一订单号，必填
            String out_trade_no = order.getSerial();
            //付款金额，必填
            String total_amount = order.getMoney().toString();
            //订单名称，必填
            String subject = "mall购物";
            //商品描述，可空
            String body = order.getId();

            // 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
            String timeout_express = "1c";

            alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                    + "\"total_amount\":\""+ total_amount +"\","
                    + "\"subject\":\""+ subject +"\","
                    + "\"body\":\""+ body +"\","
                    + "\"timeout_express\":\""+ timeout_express +"\","
                    + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
            //请求
            String result = alipayClient.pageExecute(alipayRequest).getBody();

            return result;
        }

        return "error";
    }

    @RequiresRoles(value = {"user","admin","VIP1"}, logical = Logical.OR)
    @RequestMapping(value = "/pay/success")
    public ModelAndView alipaySuccess(HttpServletRequest request) throws Exception {

        log.info("支付成功, 进入同步通知接口...");
        //获取支付宝GET过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
            params.put(name, valueStr);
        }
        boolean signVerified = AlipaySignature.rsaCheckV1(params, PUBLIC_KEY, CHAR_SET, SING_TYPE); //调用SDK验证签名

        if(signVerified) {
            //订单号
            String serial = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");

            //改变订单状态  改为成功付款
            int temp = orderService.updateStatusBySerial(serial ,Byte.valueOf("2"));
            log.info("********************** 支付成功(支付宝同步通知) **********************");
            log.info("* 订单号: {}" + serial);
            log.info("* 支付宝交易号: {}" + trade_no);
            log.info("* 实付金额: {}" + total_amount);
            log.info("***************************************************************");
            if (temp > 0){
                return getModelAndView("user/order");
            }
        }else {
            log.info("支付, 验签失败...");
        }

        return getModelAndView("404").addObject("err","服务器出故障了..");
    }

    @RequiresRoles(value = {"user","admin","VIP1"}, logical = Logical.OR)
    @RequestMapping(value = "/pay/notifySuccess")
    public Msg paySuccess(HttpServletRequest request) throws Exception {

        log.info("支付成功, 进入异步通知接口...");

        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params,PUBLIC_KEY, CHAR_SET, SING_TYPE); //调用SDK验证签名

        //——请在这里编写您的程序（以下代码仅作参考）——

		/* 实际验证过程建议商户务必添加以下校验：
		1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
		2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
		3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
		4、验证app_id是否为该商户本身。
		*/
        if(signVerified) {//验证成功
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");

            String subject = new String(request.getParameter("body").getBytes("ISO-8859-1"),"UTF-8");

            if(trade_status.equals("TRADE_FINISHED")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意： 尚自习的订单没有退款功能, 这个条件判断是进不来的, 所以此处不必写代码
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            }else if (trade_status.equals("TRADE_SUCCESS")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知

                Order order = new Order();
                order.setId(subject);
                order.setStatus(Byte.valueOf("2")); //已经付款 等待发货
            }
            log.info("支付成功...");
            return Msg.success();
        }else {//验证失败
            log.info("支付, 验签失败...");
            return Msg.msg(400,"支付宝校验失败..");
        }
    }
}
