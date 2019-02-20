package com.vedantu.models;

import java.util.ArrayList;
import java.util.List;

import com.vedantu.enums.ProductType;


public class ProductMongo extends AbstractMongoStringIdEntity {
	
	private String name;
	private int quantity;
	private String wid;
	private float price;
	private ProductType productType;
	
	/*private List<Cartitems> cartitems;
	
	
	
	public List<Cartitems> getCartitems() {
		return cartitems;
	}
	public void setCartitems(List<Cartitems> cartitems) {
		this.cartitems = cartitems;
	}
	*/
	public ProductType getProductType() {
		return productType;
	}
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getWid() {
		return wid;
	}
	public void setWid(String wid) {
		this.wid = wid;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	
	

}
