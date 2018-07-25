package org.jleopard.mall.model;

import java.io.Serializable;

public class OrderKey implements Serializable {


    private static final long serialVersionUID = 5080772182114051815L;

    private String id;

    private String mlUserId;

    private Integer mlAddressId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getMlUserId() {
        return mlUserId;
    }

    public void setMlUserId(String mlUserId) {
        this.mlUserId = mlUserId == null ? null : mlUserId.trim();
    }

    public Integer getMlAddressId() {
        return mlAddressId;
    }

    public void setMlAddressId(Integer mlAddressId) {
        this.mlAddressId = mlAddressId;
    }
}