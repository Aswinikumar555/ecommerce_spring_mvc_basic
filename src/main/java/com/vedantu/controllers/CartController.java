package com.vedantu.controllers;

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
import com.vedantu.lists.CartItem;
import com.vedantu.models.CartMongo;
import com.vedantu.models.CustomerMongo;
import com.vedantu.models.OrderMongo;
import com.vedantu.models.ProductMongo;
import com.vedantu.requests.CartReq;
import com.vedantu.requests.OrderReq;
import com.vedantu.requests.ProductReq;
import com.vedantu.utils.LogFactory;

@RestController
@RequestMapping("test3")
public class CartController {
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

//Cart APIS		
	@RequestMapping(value = "/addCart", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String addParam(@RequestBody CartReq param) throws Exception {

		CustomerMongo customer_details = customerMongoDAO.getById(param.getCustomerid());
		if (customer_details != null) {
			CartMongo cust_cart = cartMongoDAO.getByCustomerId(param.getCustomerid());
			if (cust_cart == null) {

				List<CartItem> cItems = new ArrayList<CartItem>();
				cItems.add(param.getCartitems());
				CartMongo cart_obj = new CartMongo();
				cart_obj.setCustomerid(param.getCustomerid());
				cart_obj.setCartitems(cItems);
				cartMongoDAO.create(cart_obj);
				return "added";
			} else {

				// CartMongo cart = cartMongoDAO.getByCustomerId(param.getCustomerid());
				List<CartItem> citem_list = cust_cart.getCartitems();

				if (citem_list != null && !citem_list.isEmpty()) {
					if (citem_list.contains(param.getCartitems())) {
						for (CartItem cartItem : citem_list) {
							if (cartItem.equals(param.getCartitems())) {
								cartItem.setQuantity(cartItem.getQuantity() + param.getCartitems().getQuantity());
								break;
							}
						}
					} else {
						citem_list.add(param.getCartitems());
						cust_cart.setCartitems(citem_list);
					}

				} else {
					List<CartItem> cItems = new ArrayList<CartItem>();
					cItems.add(param.getCartitems());
					cust_cart.setCartitems(cItems);
				}
				cartMongoDAO.create(cust_cart);
				return "Cart updated";
			}

		} else {
			return "Check CustomerId once";
		}
	}

	// update
	@RequestMapping(value = "/updateCart", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String updateParam(@RequestBody CartReq param) throws Exception {

		CartMongo cart = cartMongoDAO.getByCustomerId(param.getCustomerid());

		List<CartItem> citem = cart.getCartitems();

		if (citem.contains(param.getCartitems())) {
			for (CartItem cartItem : citem) {
				if (cartItem.equals(param.getCartitems())) {
					cartItem.setQuantity(param.getCartitems().getQuantity());
					cartItem.setProductid(param.getCartitems().getProductid());
					break;

				}
			}
		} else {
			citem.add(param.getCartitems());
		}
		cart.setCartitems(citem);

		cartMongoDAO.update(cart, null);

		return "Success";

	}

	// delete
	@RequestMapping(value = "/deleteCart", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteParam(@RequestBody CartReq param) throws Exception {
		CustomerMongo customer_details = customerMongoDAO.getById(param.getCustomerid());
		if (customer_details != null) {
			CartMongo cust_cart = cartMongoDAO.getByCustomerId(param.getCustomerid());
			List<CartItem> citem_list = cust_cart.getCartitems();
			if (citem_list != null && !citem_list.isEmpty()) {
				if (citem_list.contains(param.getCartitems())) {
					for (CartItem cartItem : citem_list) {
						if (cartItem.equals(param.getCartitems())) {
							citem_list.remove(param.getCartitems());
							cust_cart.setCartitems(citem_list);
							cartMongoDAO.create(cust_cart);
							break;
						}
					}
				}

			} else {
				return "Customer Cart is Empty";
			}
		} else {
			return "Customer doesn't exist";
		}
		return "Delete Success";
	}

}
