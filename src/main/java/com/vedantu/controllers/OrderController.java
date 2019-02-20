package com.vedantu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.vedantu.daos.OrderMongoDAO;
import com.vedantu.models.OrderMongo;
import com.vedantu.requests.OrderReq;

@RestController
@RequestMapping("test2")
public class OrderController {
	
	@Autowired
    private OrderMongoDAO orderMongoDAO;
//Order APIS	
	@RequestMapping(value = "/addOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	   @ResponseBody
	   public OrderMongo addParam(@RequestBody OrderReq param) throws Exception {
	       
		OrderMongo e2 = new OrderMongo();
		 
	        e2.setCustomerid(param.getCustomerid());
	        e2.setOrderstate(param.getOrderstate());
	        e2.setOrderitems(param.getOrderitems());
	        e2.setTotalprice(param.getTotalprice());
	        orderMongoDAO.create(e2);

	       return e2;
	   }
	
	//update
	   @RequestMapping(value="/updateOrder", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	   @ResponseBody
	   public String updateParam(@RequestBody OrderReq param) throws Exception {
		   
		   OrderMongo e2 =orderMongoDAO.getById(param.getId());
		   e2.setCustomerid(param.getCustomerid());
	        e2.setOrderstate(param.getOrderstate());
	        e2.setOrderitems(param.getOrderitems());
	        e2.setTotalprice(param.getTotalprice());
	       
	        orderMongoDAO.update(e2,null);

	       return "UpdateSuccess";
		   
		  }
	 //delete
	   @RequestMapping(value="/deleteOrder", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	   @ResponseBody
	   public String deleteParam(@RequestBody OrderReq param) throws Exception {
		   
		   OrderMongo e2 =orderMongoDAO.getEntityById(param.getId(), OrderMongo.class);
		   
	       
		   orderMongoDAO.delete(e2,null);

	       return "Delete Success";
	   }


}
