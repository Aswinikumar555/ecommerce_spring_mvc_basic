package com.vedantu.requests;

import java.util.List;

import com.vedantu.lists.CartItem;

public class CartReq {
	private String id;
	private  String  customerid;
	//private List<CartItem> cartitems;
	private CartItem cartitems;
	
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
	public CartItem getCartitems() {
		return cartitems;
	}
	public void setCartitems(CartItem cartitems) {
		this.cartitems = cartitems;
	}
	
	
	
	
	
	 

}
