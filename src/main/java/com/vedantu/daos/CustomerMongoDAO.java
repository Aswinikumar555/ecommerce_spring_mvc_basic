package com.vedantu.daos;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.vedantu.models.CustomerMongo;
import com.vedantu.utils.LogFactory;

@Service
public class CustomerMongoDAO extends AbstractMongoDAO{
	 @Autowired
	    private LogFactory logFactory;

	    @SuppressWarnings("static-access")
	    private Logger logger = logFactory.getLogger(CustomerMongoDAO.class);

	    @Autowired
	    private MongoClientFactory mongoClientFactory;

	    @Override
	    protected MongoOperations getMongoOperations() {
	        return mongoClientFactory.getMongoOperations();
	    }

	    public CustomerMongoDAO() {
	        super();
	    }

	    public void create(CustomerMongo customerMongo) {
	        try {
	        	logger.info("customer "+customerMongo);
	            saveEntity(customerMongo);
	        } catch (Exception ex) {
	            throw new RuntimeException(
	                    "ContentInfoUpdateError : Error updating the content info " + customerMongo, ex);
	        }
	    }

	    public CustomerMongo getById(String id) {
	    	CustomerMongo challenge = null;
	        try {
	            challenge = (CustomerMongo) getEntityById(id, CustomerMongo.class);
	        } catch (Exception ex) {
	            throw new RuntimeException("ContentInfoFetchError : Error fetch the content info with id " + id, ex);
	        }
	        return challenge;
	    }

	    public List<CustomerMongo> getStudyEntryItems(List<String> cuid) {
	        Query query;
	        query = new Query();
	        query.addCriteria(Criteria.where("cuid").in(cuid));
	        return runQuery(query, CustomerMongo.class);
	    }

	    public CustomerMongo getStudyEntryItem(String cuid) {
	        Query query = new Query();
	        query.addCriteria(Criteria.where(cuid).is(cuid));
	        logger.info(query);
	        return findOne(query, CustomerMongo.class);
	    }
	    
	    public void update(CustomerMongo p ,String callingUserId) {
      	  if (p != null) {
          logger.info("update: " + p.toString());
          saveEntity(p, callingUserId);
      	  }
      	  
	    }
	    
	    public void delete(CustomerMongo p,String callingUserId) {
	      	  if (p != null) {
	          logger.info("delete: " + p.toString());
	          deleteEntityById(p.getId(),CustomerMongo.class);
	      	  }
	      	  
		    }
	    
}
