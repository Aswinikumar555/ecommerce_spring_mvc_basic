package com.vedantu.lists;

import org.springframework.beans.factory.annotation.Autowired;

import com.vedantu.daos.ProductMongoDAO;
import com.vedantu.models.ProductMongo;

public class OrderItem {


	private String productid;
	private int quantity;
	private float price;
	
	
	public OrderItem(){
		
	}
	
	public OrderItem(String productid, int quantity, float price) {
		super();
		this.productid = productid;
		this.quantity = quantity;
		this.price = price;
	}
	public String getProductid() {
		return productid;
	}
	public void setProductid(String productid) {
		this.productid = productid;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
		
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	
}
