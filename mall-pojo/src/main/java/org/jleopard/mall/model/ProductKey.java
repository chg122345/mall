package org.jleopard.mall.model;

import java.io.Serializable;

public class ProductKey implements Serializable {


    private static final long serialVersionUID = -8673379801442553577L;

    private String id;

    private Integer mlCategoryId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Integer getMlCategoryId() {
        return mlCategoryId;
    }

    public void setMlCategoryId(Integer mlCategoryId) {
        this.mlCategoryId = mlCategoryId;
    }
}