package org.jleopard.mall.model;

import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@ToString
public class OrderItem extends OrderItemKey implements Serializable {


    private static final long serialVersionUID = -8393034271582113589L;

    private Integer number;

    private Double money;

    private String mlProductId;

    private Product product;

    public OrderItem() {
    }

    public OrderItem(Integer number, Double money, String mlProductId) {
        this.number = number;
        this.money = money;
        this.mlProductId = mlProductId;
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

    public String getMlProductId() {
        return mlProductId;
    }

    public void setMlProductId(String mlProductId) {
        this.mlProductId = mlProductId == null ? null : mlProductId.trim();
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}