package org.jleopard.mall.model;

import java.io.Serializable;


public class CartItem implements Serializable {


	private static final long serialVersionUID = 2905331945510215047L;
	
	private Product product;
	private Integer number;
	private Double money;
	public CartItem(Product product) {
		this.product=product;
	}
	
	
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public Double getMoney() {
		return product.getPrice()*number;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Product getProduct() {
		return product;
	}

}
