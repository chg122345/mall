package org.jleopard.mall.model;

import java.io.Serializable;

public class OrderItemKey implements Serializable {


    private static final long serialVersionUID = 7501083647144081535L;

    private Integer id;

    private String mlOrderId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMlOrderId() {
        return mlOrderId;
    }

    public void setMlOrderId(String mlOrderId) {
        this.mlOrderId = mlOrderId == null ? null : mlOrderId.trim();
    }
}