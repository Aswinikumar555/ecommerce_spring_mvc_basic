package com.vedantu.daos;

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.vedantu.models.ProductMongo;
import com.vedantu.utils.LogFactory;

@Service
public class ProductMongoDAO extends AbstractMongoDAO{
	
	@Autowired
    private LogFactory logFactory;

    @SuppressWarnings("static-access")
    private Logger logger = logFactory.getLogger(ProductMongoDAO.class);

    @Autowired
    private MongoClientFactory mongoClientFactory;

    @Override
    protected MongoOperations getMongoOperations() {
        return mongoClientFactory.getMongoOperations();
    }

    public ProductMongoDAO() {
        super();
    }

    public void create(ProductMongo productMongo) {
        try {
        	logger.info("customer "+productMongo);
            saveEntity(productMongo);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "ContentInfoUpdateError : Error updating the content info " + productMongo, ex);
        }
    }

    public ProductMongo getById(String id) {
    	ProductMongo challenge = null;
        try {
            challenge = (ProductMongo) getEntityById(id, ProductMongo.class);
        } catch (Exception ex) {
            throw new RuntimeException("ContentInfoFetchError : Error fetch the content info with id " + id, ex);
        }
        return challenge;
    }

    public List<ProductMongo> getStudyEntryItems(List<String> cuid) {
        Query query;
        query = new Query();
        query.addCriteria(Criteria.where("cuid").in(cuid));
        return runQuery(query, ProductMongo.class);
    }

    public ProductMongo getStudyEntryItem(String cuid) {
        Query query = new Query();
        query.addCriteria(Criteria.where(cuid).is(cuid));
        logger.info(query);
        return findOne(query, ProductMongo.class);
    }
    
    public void update(ProductMongo p ,String callingUserId) {
  	  if (p != null) {
      logger.info("update: " + p.toString());
      saveEntity(p, callingUserId);
  	  }
  	  
    }
    
    public void delete(ProductMongo p,String callingUserId) {
      	  if (p != null) {
          logger.info("delete: " + p.toString());
          deleteEntityById(p.getId(),ProductMongo.class);
      	  }
      	  
	    }
    // created by me
    public List<ProductMongo> getProductsFromIds(Set<String> ids) {
        Query query;
        query = new Query();
        query.addCriteria(Criteria.where("id").in(ids));
        return runQuery(query, ProductMongo.class);
    }
    

}
