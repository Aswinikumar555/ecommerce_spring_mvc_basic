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

import com.vedantu.daos.AbstractMongoClientFactory;
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
	private final Logger logger = logFactory.getLogger(AbstractMongoClientFactory.class);
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

		CartMongo cart_obj = cartMongoDAO.getByCustomerId(param.getCustomerid());
		if (cart_obj != null) {

			Set<String> cart_pids = cart_obj.getProductIds();

			List<ProductMongo> product_info = productMongoDAO.getProductsFromIds(cart_pids);

			Map<String, Integer> cart_map = new HashMap<>();
			for (CartItem cartItem : cart_obj.getCartitems()) {
				cart_map.put(cartItem.getProductid(), cartItem.getQuantity());
			}

			List<CartItem> citem_list = cart_obj.getCartitems();
			List<OrderItem> orderitem_list = new ArrayList<OrderItem>();
			logger.info("first products  " + product_info);

			for (ProductMongo prdt : product_info) {
				if (prdt.getQuantity() < cart_map.get(prdt.getId())) {

					// throw new RuntimeException("item not in stock for " + prdt.getName());
				} else {
					// adding order items
					int cart_quantity = cart_map.get(prdt.getId());
					orderitem_list.add(new OrderItem(prdt.getId(), cart_quantity, prdt.getPrice()));

					// remove from cartitem_list
					citem_list.remove(new CartItem(prdt.getId(), cart_quantity));

					prdt.setQuantity(prdt.getQuantity() - cart_map.get(prdt.getId()));
				}
			}

			if (orderitem_list != null && !orderitem_list.isEmpty()) {

				// add Order
				OrderMongo o = new OrderMongo();
				o.setOrderitems(orderitem_list);
				o.setCustomerid(param.getCustomerid());
				o.setTotalprice(param.getTotalprice());

				// checking amount in customer
				CustomerMongo customer = customerMongoDAO.getById(o.getCustomerid());
				if (o.getTotalprice() <= customer.getAmount()) {

					// subtract the customer amount
					customer.setAmount(customer.getAmount() - o.getTotalprice());
					customerMongoDAO.create(customer);
					for (ProductMongo prdt1 : product_info) {

						// subtract the quantity
						productMongoDAO.create(prdt1);
					}
					o.setOrderstate(Orderstate.PAID);
					orderMongoDAO.create(o);

					// clear the cart
					if (citem_list != null && !citem_list.isEmpty()) {
						cart_obj.setCartitems(citem_list);
					} else {
						cart_obj.setCartitems(null);
					}
					cartMongoDAO.create(cart_obj);
				} else {
					return "Less amount";
				}
				return "Order Placed Successfully";
			} else {
				return "quantity is high for all products";
			}

		} else {
			return "Cart does not exist for Customer";
		}
	}

	@RequestMapping(value = "/cancelOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String cancelOrder(@RequestBody OrderReq param) throws Exception {
		OrderMongo order_obj = orderMongoDAO.getById(param.getId());
		if (order_obj != null) {
			if (order_obj.getOrderstate().equals(Orderstate.PAID)) {
				Set<String> ordIds = order_obj.getProductIds();
				List<ProductMongo> prdts = productMongoDAO.getProductsFromIds(ordIds);

				// Adding the customer amount
				CustomerMongo customer = customerMongoDAO.getById(order_obj.getCustomerid());
				customer.setAmount(customer.getAmount() + order_obj.getTotalprice());
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
			} else {
				return "order_state in canceled";
			}
		} else {
			return "OrderNumber doesnot exist";
		}
	}
}
