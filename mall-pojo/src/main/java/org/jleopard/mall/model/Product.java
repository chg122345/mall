package org.jleopard.mall.model;

import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@ToString
public class Product extends ProductKey implements Serializable {

    private static final long serialVersionUID = 9090632509994310769L;

    private String name;

    private String details;

    private Double price;

    private Integer stock;

    private String imgs;

    private Date created;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details == null ? null : details.trim();
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs == null ? null : imgs.trim();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}