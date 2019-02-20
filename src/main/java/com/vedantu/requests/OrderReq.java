package com.vedantu.requests;

import java.util.List;

import com.vedantu.enums.Orderstate;
import com.vedantu.lists.OrderItem;
public class OrderReq {
	private String id;
	private String customerid;
	private int totalprice;
	private Orderstate orderstate; //enum
	private List<OrderItem> orderitems; //list
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
		this.totalprice = totalprice;
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
	
	

}
