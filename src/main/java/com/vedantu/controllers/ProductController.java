package com.vedantu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vedantu.daos.ProductMongoDAO;
import com.vedantu.models.CustomerMongo;
import com.vedantu.models.ProductMongo;
import com.vedantu.requests.CustomerReq;
import com.vedantu.requests.ProductReq;

@RestController
@RequestMapping("test1")
public class ProductController {

	@Autowired
	private ProductMongoDAO productMongoDAO;

//Product APIS	
	@RequestMapping(value = "/addProduct", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ProductMongo addParam(@RequestBody ProductReq param) throws Exception {

		ProductMongo e2 = new ProductMongo();

		e2.setName(param.getName());
		e2.setQuantity(param.getQuantity());
		e2.setWid(param.getWid());
		e2.setPrice(param.getPrice());
		e2.setProductType(param.getProductType());
		// e2.setCartitems(param.getCartitems());
		productMongoDAO.create(e2);

		return e2;
	}

	// update
	@RequestMapping(value = "/updateProduct", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String updateParam(@RequestBody ProductReq param) throws Exception {

		ProductMongo e2 = productMongoDAO.getById(param.getId());
		e2.setName(param.getName());
		e2.setQuantity(param.getQuantity());
		e2.setWid(param.getWid());
		e2.setPrice(param.getPrice());

		productMongoDAO.update(e2, null);

		return "Update Success";

	}

	// delete
	@RequestMapping(value = "/deleteProduct", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteParam(@RequestBody ProductReq param) throws Exception {

		ProductMongo e2 = productMongoDAO.getEntityById(param.getId(), ProductMongo.class);

		productMongoDAO.delete(e2, null);

		return "Delete Success";
	}

}
