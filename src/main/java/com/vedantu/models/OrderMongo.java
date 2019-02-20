package com.vedantu.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vedantu.enums.Orderstate;
import com.vedantu.lists.CartItem;
import com.vedantu.lists.OrderItem;

public class OrderMongo extends AbstractMongoStringIdEntity{
	private String customerid;
	private int totalprice;
	private Orderstate orderstate; //enum
	private List<OrderItem> orderitems; //list
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	
	public int getTotalprice() {
		return totalprice;
	}
	public void setTotalprice(int totalprice) {
		
			int Sum_totalPrice=0;
			if(orderitems != null && !orderitems.isEmpty()) {
			for(OrderItem orderItem:orderitems) {
				Sum_totalPrice += orderItem.getPrice()*orderItem.getQuantity();
			}
		this.totalprice = Sum_totalPrice;
			}
	}
	public Orderstate getOrderstate() {
		return orderstate;
	}
	public void setOrderstate(Orderstate orderstate) {
		this.orderstate = orderstate;
	}
	public List<OrderItem> getOrderitems() {
		return orderitems;
	}
	public void setOrderitems(List<OrderItem> orderitems) {
		this.orderitems = orderitems;
	}
	
	
	
	public Set<String> getProductIds(){
		if(orderitems==null||orderitems.isEmpty()) {
		return new HashSet<>();
		}
		Set<String> ordpIds=new HashSet<>();
		for(OrderItem orderItem:orderitems) {
		ordpIds.add(orderItem.getProductid());
		}
		return ordpIds;
		}
	

}
