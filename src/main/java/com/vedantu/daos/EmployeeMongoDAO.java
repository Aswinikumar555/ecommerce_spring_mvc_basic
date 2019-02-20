/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vedantu.daos;

import com.vedantu.models.EmployeeMongo;

import java.util.List;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import com.vedantu.utils.LogFactory;

/**
 *
 * @author ajith
 */
@Service
public class EmployeeMongoDAO extends AbstractMongoDAO {

    @Autowired
    private LogFactory logFactory;

    @SuppressWarnings("static-access")
    private Logger logger = logFactory.getLogger(EmployeeMongoDAO.class);

    @Autowired
    private MongoClientFactory mongoClientFactory;

    @Override
    protected MongoOperations getMongoOperations() {
        return mongoClientFactory.getMongoOperations();
    }

    public EmployeeMongoDAO() {
        super();
    }

    public void create(EmployeeMongo employeeMongo) {
        try {
            saveEntity(employeeMongo);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "ContentInfoUpdateError : Error updating the content info " + employeeMongo, ex);
        }
    }

    public EmployeeMongo getById(String id) {
        EmployeeMongo challenge = null;
        try {
            challenge = (EmployeeMongo) getEntityById(id, EmployeeMongo.class);
        } catch (Exception ex) {
            throw new RuntimeException("ContentInfoFetchError : Error fetch the content info with id " + id, ex);
        }
        return challenge;
    }

    public List<EmployeeMongo> getStudyEntryItems(List<String> userIds) {
        Query query;
        query = new Query();
        query.addCriteria(Criteria.where("userIds").in(userIds));
        return runQuery(query, EmployeeMongo.class);
    }

    public EmployeeMongo getStudyEntryItem(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(userId).is(userId));
        logger.info(query);
        return findOne(query, EmployeeMongo.class);
    }
}
