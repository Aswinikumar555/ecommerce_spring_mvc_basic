package com.vedantu.daos;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.vedantu.models.CartMongo;
import com.vedantu.models.OrderMongo;
import com.vedantu.utils.LogFactory;

@Service
public class OrderMongoDAO extends AbstractMongoDAO{
	

	@Autowired
    private LogFactory logFactory;

    @SuppressWarnings("static-access")
    private Logger logger = logFactory.getLogger(OrderMongoDAO.class);

    @Autowired
    private MongoClientFactory mongoClientFactory;

    @Override
    protected MongoOperations getMongoOperations() {
        return mongoClientFactory.getMongoOperations();
    }

    public OrderMongoDAO() {
        super();
    }

    public void create(OrderMongo orderMongo) {
        try {
        	logger.info("customer "+orderMongo);
            saveEntity(orderMongo);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "ContentInfoUpdateError : Error updating the content info " + orderMongo, ex);
        }
    }

    public OrderMongo getById(String id) {
    	OrderMongo challenge = null;
        try {
            challenge = (OrderMongo) getEntityById(id, OrderMongo.class);
        } catch (Exception ex) {
            throw new RuntimeException("ContentInfoFetchError : Error fetch the content info with id " + id, ex);
        }
        return challenge;
    }

    public List<OrderMongo> getStudyEntryItems(List<String> cuid) {
        Query query;
        query = new Query();
        query.addCriteria(Criteria.where("cuid").in(cuid));
        return runQuery(query, OrderMongo.class);
    }

    public OrderMongo getStudyEntryItem(String cuid) {
        Query query = new Query();
        query.addCriteria(Criteria.where(cuid).is(cuid));
        logger.info(query);
        return findOne(query, OrderMongo.class);
    }
    
    public void update(OrderMongo p ,String callingUserId) {
  	  if (p != null) {
      logger.info("update: " + p.toString());
      saveEntity(p, callingUserId);
  	  }
  	  
    }
    
    public void delete(OrderMongo p,String callingUserId) {
      	  if (p != null) {
          logger.info("delete: " + p.toString());
          deleteEntityById(p.getId(),OrderMongo.class);
      	  }
      	  
	    }
    //custom
    public OrderMongo getByCustomerId(String id) {
    	OrderMongo challenge = null;
        try {
            challenge = (OrderMongo) getEntityByCustomerId(id, OrderMongo.class);
        } catch (Exception ex) {
            throw new RuntimeException("ContentInfoFetchError : Error fetch the content info with id " + id, ex);
        }
        return challenge;
    }
}
