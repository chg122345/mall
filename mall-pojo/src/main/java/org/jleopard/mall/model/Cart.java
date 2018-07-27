package org.jleopard.mall.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Cart implements Serializable {


	private static final long serialVersionUID = -7808052514664738346L;

	private Map<String,CartItem> items=new HashMap<String,CartItem>();
	private Integer totalNumber;
	private Double totalMoney;
	
	
	
	public Integer getTotalNumber() {
		   totalNumber=0;
		for(Map.Entry<String, CartItem> g:items.entrySet()) {
		    totalNumber +=g.getValue().getNumber();
			}
		return totalNumber;
	}
	
	
	public void setTotalNumber(Integer totalNumber) {
		
		this.totalNumber = totalNumber;
	}
	
	
	public Double getTotalMoney() {
		totalMoney=0.0;
   for(Map.Entry<String, CartItem> g:items.entrySet()) {
	    totalMoney +=g.getValue().getMoney();
		}
		return totalMoney;
	}
	
	
	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}
	
	
	public Map<String, CartItem> getItems() {
		return items;
	}
	
	

	//添加购物车
	public void addCart(Product product, Integer Number){
		
		if(items.containsKey(product.getId())) {
			//购物车有该商品
			CartItem item=items.get(product.getId());
			item.setNumber(item.getNumber() + Number);
		}else {
			
			CartItem item=new CartItem(product);
			item.setNumber(Number);
			items.put(product.getId(), item);
		}
		
	}
	
	
}
