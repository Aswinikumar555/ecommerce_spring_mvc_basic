package com.vedantu.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vedantu.daos.AbstractSqlDAO;
import com.vedantu.daos.CartMongoDAO;
import com.vedantu.daos.CustomerMongoDAO;
import com.vedantu.daos.OrderMongoDAO;
import com.vedantu.daos.ProductMongoDAO;
import com.vedantu.enums.Orderstate;
import com.vedantu.lists.CartItem;
import com.vedantu.lists.OrderItem;
import com.vedantu.models.CartMongo;
import com.vedantu.models.CustomerMongo;
import com.vedantu.models.OrderMongo;
import com.vedantu.models.ProductMongo;
import com.vedantu.requests.OrderReq;
import com.vedantu.utils.LogFactory;

@RestController
@RequestMapping("test4")
public class PlaceOrder {
	
	@Autowired
	private LogFactory logFactory;

	@SuppressWarnings("static-access")
	private Logger logger = logFactory.getLogger(AbstractSqlDAO.class);
	 @Autowired
	    private CartMongoDAO cartMongoDAO;
	 @Autowired
	    private ProductMongoDAO productMongoDAO;
	 //
	 @Autowired
	    private OrderMongoDAO orderMongoDAO;
	 @Autowired
	    private CustomerMongoDAO customerMongoDAO;
	
	 @RequestMapping(value = "/placeOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	   @ResponseBody
	   public String addplaceOrder(@RequestBody OrderReq param) throws Exception {
		 
		 CartMongo c = cartMongoDAO.getByCustomerId(param.getCustomerid());		 
	     Set<String> ordIds = c.getProductIds();
	     
	     List<ProductMongo> prdts = productMongoDAO.getProductsFromIds(ordIds);
		 
		 Map<String, Integer> cartmap = new HashMap<>();
		   for (CartItem cartItem : c.getCartitems()) {
			   cartmap.put(cartItem.getProductid(), cartItem.getQuantity());
		   }
		   
		   for (ProductMongo prdt: prdts) {
			 if (prdt.getQuantity() < cartmap.get(prdt.getId())) {
				 	throw new RuntimeException("item not in stock for "+prdt.getName()); 
			 } else {
				 List<OrderItem> orderitem_list = new ArrayList<OrderItem>();
				 
				 for (ProductMongo prdt1 : prdts) {
					 //adding order items
					 int cart_quantity = cartmap.get(prdt1.getId());
					   orderitem_list.add(new OrderItem(prdt1.getId() ,cart_quantity ,prdt1.getPrice()));
				 }
				   // add Order
				   OrderMongo o =new OrderMongo();
				   o.setOrderitems(orderitem_list);
				   o.setCustomerid(param.getCustomerid());
				   o.setTotalprice(param.getTotalprice());
		
				   //checking amount in customer
				   CustomerMongo customer = customerMongoDAO.getById(o.getCustomerid());
				   if(o.getTotalprice() <= customer.getAmount()) {
					   
					   //subtract the customer amount
					 customer.setAmount(customer.getAmount()-o.getTotalprice());
					   customerMongoDAO.create(customer);
					   for (ProductMongo prdt1 : prdts) {
						   
						   // subtract the quantity
						   prdt1.setQuantity(prdt1.getQuantity() - cartmap.get(prdt1.getId()));
						  productMongoDAO.create(prdt1);
						 }
					   o.setOrderstate(Orderstate.PAID);
					  orderMongoDAO.create(o);
					 
					  // clear the cart
						c.setCartitems(null);
						cartMongoDAO.create(c);
				   }
				   else {
					   return "Less amount";
				   }
				 }
		  }
		   return "Order Placed Successfully";
	 }
	 
	 
	 @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	   @ResponseBody
	   public String cancelOrder(@RequestBody OrderReq param) throws Exception {
			OrderMongo order_obj = orderMongoDAO.getById(param.getId());	
			if (order_obj.getOrderstate().equals("PAID")) {
				Set<String> ordIds = order_obj.getProductIds();
			    List<ProductMongo> prdts = productMongoDAO.getProductsFromIds(ordIds);
			    
			  //Adding the customer amount
				CustomerMongo customer = customerMongoDAO.getById(order_obj.getCustomerid());
				customer.setAmount(customer.getAmount()+order_obj.getTotalprice());
				customerMongoDAO.create(customer);
				
				 // Adding the quantity
				Map<String, Integer> ordermap = new HashMap<>();
					for (OrderItem orderItem : order_obj.getOrderitems()) {
							ordermap.put(orderItem.getProductid(), orderItem.getQuantity());
					}
					for (ProductMongo prdt1 : prdts) {
						prdt1.setQuantity(prdt1.getQuantity() + ordermap.get(prdt1.getId()));
						productMongoDAO.create(prdt1);
					}
					
					// Change the status of order
						order_obj.setOrderstate(Orderstate.CANCELED);
						orderMongoDAO.create(order_obj);
				   return "Order Canceled Successfully";
			}
			else {
				return "order_state in canceled";
			}
		}
	 }
