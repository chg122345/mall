package org.jleopard.mall.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ToString
public class Order extends OrderKey implements Serializable {


    private static final long serialVersionUID = 3634156239618639671L;

    private String serial; // 18位订单编号

    private Byte status;  // 1->待付款 2-> 待发货 3-> 已发货 4-> 交易成功

    private Integer number;

    private Double money;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date created;

    private List<OrderItem> orderItem;

    private User user;

    private Address address;

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial == null ? null : serial.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<OrderItem> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(List<OrderItem> orderItem) {
        this.orderItem = orderItem;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}