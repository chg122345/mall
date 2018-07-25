package org.jleopard.mall.model;

import java.io.Serializable;

public class AddressKey implements Serializable {

    private static final long serialVersionUID = -173817406916523865L;

    private Integer id;

    private String mlUserId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMlUserId() {
        return mlUserId;
    }

    public void setMlUserId(String mlUserId) {
        this.mlUserId = mlUserId == null ? null : mlUserId.trim();
    }
}