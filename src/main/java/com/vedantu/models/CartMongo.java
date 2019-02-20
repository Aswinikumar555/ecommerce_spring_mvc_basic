package com.vedantu.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vedantu.lists.CartItem;

public class CartMongo extends AbstractMongoStringIdEntity{
	
	private  String  customerid;
	private List<CartItem> cartitems;
	
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	public List<CartItem> getCartitems() {
		return cartitems;
	}
	public void setCartitems(List<CartItem> cartitems) {
		this.cartitems = cartitems;
	}
	public Set<String> getProductIds(){
		if(cartitems==null||cartitems.isEmpty()) {
		return new HashSet<>();
		}
		Set<String> prdIds=new HashSet<>();
		for(CartItem cartItem:cartitems) {
		prdIds.add(cartItem.getProductid());
		}
		return prdIds;
		}
	

}
