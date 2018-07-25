package org.jleopard.mall.model;

import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@ToString
public class Order extends OrderKey implements Serializable {


    private static final long serialVersionUID = 3634156239618639671L;

    private String serial;

    private Byte status;

    private Integer number;

    private Double money;

    private Date created;

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

    public Double getmoney() {
        return money;
    }

    public void setmoney(Double money) {
        this.money = money;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}