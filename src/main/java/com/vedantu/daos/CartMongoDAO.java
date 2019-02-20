package com.vedantu.daos;

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import com.vedantu.models.CartMongo;
import com.vedantu.models.ProductMongo;
import com.vedantu.utils.LogFactory;

@Service
public class CartMongoDAO extends AbstractMongoDAO{
	

	@Autowired
    private LogFactory logFactory;

    @SuppressWarnings("static-access")
    private Logger logger = logFactory.getLogger(CartMongoDAO.class);

    @Autowired
    private MongoClientFactory mongoClientFactory;

    @Override
    protected MongoOperations getMongoOperations() {
        return mongoClientFactory.getMongoOperations();
    }

    public CartMongoDAO() {
        super();
    }

    public void create(CartMongo cartMongo) {
        try {
        	logger.info("cart "+cartMongo);
            saveEntity(cartMongo);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "ContentInfoUpdateError : Error updating the content info " + cartMongo, ex);
        }
    }

    public CartMongo getById(String id) {
    	CartMongo challenge = null;
        try {
            challenge = (CartMongo) getEntityById(id, CartMongo.class);
        } catch (Exception ex) {
            throw new RuntimeException("ContentInfoFetchError : Error fetch the content info with id " + id, ex);
        }
        return challenge;
    }

    public List<CartMongo> getStudyEntryItems(List<String> cuid) {
        Query query;
        query = new Query();
        query.addCriteria(Criteria.where("cuid").in(cuid));
        return runQuery(query, CartMongo.class);
    }

    public CartMongo getStudyEntryItem(String cuid) {
        Query query = new Query();
        query.addCriteria(Criteria.where(cuid).is(cuid));
        logger.info(query);
        return findOne(query, CartMongo.class);
    }
    
    public void update(CartMongo p ,String callingUserId) {
  	  if (p != null) {
      logger.info("update: " + p.toString());
      saveEntity(p, callingUserId);
  	  }
  	  
    }
    
    public void delete(CartMongo p,String callingUserId) {
      	  if (p != null) {
          logger.info("delete: " + p.toString());
          deleteEntityById(p.getId(),CartMongo.class);
      	  }
      	  
	    }
    public List<CartMongo> getCartFromIds(Set<String> ids) {
        Query query;
        query = new Query();
        query.addCriteria(Criteria.where("id").in(ids));
        return runQuery(query, CartMongo.class);
    }
    //custom
    public CartMongo getByCustomerId(String id) {
    	CartMongo challenge = null;
        try {
            challenge = (CartMongo) getEntityByCustomerId(id, CartMongo.class);
        } catch (Exception ex) {
            throw new RuntimeException("ContentInfoFetchError : Error fetch the content info with id " + id, ex);
        }
        return challenge;
    }

}
