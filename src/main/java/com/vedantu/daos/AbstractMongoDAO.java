/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vedantu.daos;

import com.mongodb.WriteResult;
import com.vedantu.models.AbstractMongoEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 *
 * @author ajith
 */
public abstract class AbstractMongoDAO {

    public static final String ORDER_DESC = "desc";
    public static final String ORDER_ASC = "asc";
    public static final String QUERY_AND_OPERATOR = "&&";

    public final long NO_START = 0;
    public final static long NO_LIMIT = 0;
    public static final long UNINITIALIZED = -1L;

    public static final int DEFAULT_FETCH_SIZE = 20;
    public static final int MAX_ALLOWED_FETCH_SIZE = 2000;

    public AbstractMongoDAO() {
        super();
    }

    protected abstract MongoOperations getMongoOperations();

    protected <E extends AbstractMongoEntity> void saveEntity(E p, String callingUserId) {
        p.setEntityDefaultProperties(callingUserId);
        getMongoOperations().save(p, p.getClass().getSimpleName());
    }

    protected <E extends AbstractMongoEntity> void saveEntity(E p) {
        saveEntity(p, null);
    }

    protected <E extends AbstractMongoEntity> void insertAllEntities(Collection<E> entities, String collectionName, String callingUserId) {
        setEntityDefaultProperties(entities, callingUserId);
        getMongoOperations().insert(entities, collectionName);
    }

    public <E extends AbstractMongoEntity> void insertAllEntities(Collection<E> entities, String collectionName) {
        insertAllEntities(entities, collectionName, null);
    }

    public <T extends AbstractMongoEntity> void upsertEntity(Query q, Update u, Class<T> clazz) {
        getMongoOperations().upsert(q, u, clazz.getSimpleName());
    }

    public <E extends AbstractMongoEntity> void upsertEntity(E entity, Query q, Update u, String callingUserId) {
        entity.setEntityDefaultProperties(callingUserId);
        getMongoOperations().upsert(q, u, entity.getClass().getSimpleName());
    }

    public <T extends AbstractMongoEntity> int updateFirst(Query q, Update u, Class<T> clazz) {
        if (u != null) {
            u.set(AbstractMongoEntity.Constants.LAST_UPDATED, System.currentTimeMillis());
        }
        WriteResult result = getMongoOperations().updateFirst(q, u, clazz.getSimpleName());
        long modifiedCount = result.getN();
        return Long.valueOf(modifiedCount).intValue();
    }

    protected <T extends AbstractMongoEntity> void updateMulti(Query q, Update u, Class<T> clazz) {
        if (u != null) {
            u.set(AbstractMongoEntity.Constants.LAST_UPDATED, System.currentTimeMillis());
        }
        getMongoOperations().updateMulti(q, u, clazz.getSimpleName());
    }

    protected <T extends AbstractMongoEntity> T findAndModifyEntity(Query q, Update u, FindAndModifyOptions o, Class<T> clazz) {
        if (u != null) {
            u.set(AbstractMongoEntity.Constants.LAST_UPDATED, System.currentTimeMillis());
        }
        return getMongoOperations().findAndModify(q, u, o, clazz, clazz.getSimpleName());
    }

    public <T extends AbstractMongoEntity> T getEntityById(String id, Class<T> clazz) {
        Query query = new Query(Criteria.where("_id").is(id));
        return getMongoOperations().findOne(query, clazz, clazz.getSimpleName());
    }

    public <T extends AbstractMongoEntity> T getEntityById(String id, Class<T> clazz,
            List<String> includeFields) {
        Query query = new Query(Criteria.where("_id").is(id));
        if (includeFields != null && !includeFields.isEmpty()) {
            includeFields.forEach((fieldName) -> {
                query.fields().include(fieldName);
            });
        }
        return getMongoOperations().findOne(query, clazz, clazz.getSimpleName());
    }

    public <T extends AbstractMongoEntity> T getEntityById(Long id, Class<T> clazz) {
        Query query = new Query(Criteria.where("_id").is(id));
        return getMongoOperations().findOne(query, clazz, clazz.getSimpleName());
    }

    public <T extends AbstractMongoEntity> T getEntityById(Long id, Class<T> clazz,
            List<String> includeFields) {
        Query query = new Query(Criteria.where("_id").is(id));
        if (includeFields != null && !includeFields.isEmpty()) {
            includeFields.forEach((fieldName) -> {
                query.fields().include(fieldName);
            });
        }
        return getMongoOperations().findOne(query, clazz, clazz.getSimpleName());
    }

    public <T extends AbstractMongoEntity> List<T> runQuery(Query query, Class<T> clazz) {
        if (query != null && query.getLimit() > 1000) {
            query.limit(MAX_ALLOWED_FETCH_SIZE);
        }
        return getMongoOperations().find(query, clazz, clazz.getSimpleName());
    }

    public <T extends AbstractMongoEntity> List<T> runQuery(Query query, Class<T> clazz, String collectionName) {
        if (query != null && query.getLimit() > 1000) {
            query.limit(MAX_ALLOWED_FETCH_SIZE);
        }
        return getMongoOperations().find(query, clazz, collectionName);
    }

    public <T extends AbstractMongoEntity> List<T> runQuery(Query query, Class<T> clazz, List<String> fields) {
        if (query != null && query.getLimit() > 1000) {
            query.limit(MAX_ALLOWED_FETCH_SIZE);
        }
        if (fields != null && query != null) {
            for (String field : fields) {
                query.fields().include(field);
            }
        }
        return getMongoOperations().find(query, clazz, clazz.getSimpleName());
    }

    public <T extends AbstractMongoEntity> T findOne(Query query, Class<T> clazz) {
        return getMongoOperations().findOne(query, clazz, clazz.getSimpleName());
    }

    public <T extends AbstractMongoEntity> T findOne(Query query, Class<T> clazz, String collectionName) {
        return getMongoOperations().findOne(query, clazz, collectionName);
    }

    public <T extends AbstractMongoEntity> long queryCount(Query query, Class<T> clazz) {
        return getMongoOperations().count(query, clazz, clazz.getSimpleName());
    }

    public <T extends AbstractMongoEntity> int deleteEntityById(String id, Class<T> clazz) {
        Query query = new Query(Criteria.where("_id").is(id));
        WriteResult result = getMongoOperations().remove(query, clazz,
                clazz.getSimpleName());
        return Long.valueOf(result.getN()).intValue();
    }

    public <T extends AbstractMongoEntity> int deleteEntityById(Long id, Class<T> clazz) {
        Query query = new Query(Criteria.where("_id").is(id));
        WriteResult result = getMongoOperations().remove(query, clazz,
                clazz.getSimpleName());
        return Long.valueOf(result.getN()).intValue();
    }

    public <T extends AbstractMongoEntity> int deleteEntities(Query query, Class<T> clazz) {
        WriteResult result = getMongoOperations().remove(query, clazz,
                clazz.getSimpleName());
        return Long.valueOf(result.getN()).intValue();
    }

    // aggregation queries
    public <T extends AbstractMongoEntity> List<T> getEntitiesByUpdationTimeAndLimit(Long fromTime, Long tillTime, Integer limit, Class<T> clazz) {
        Query query = new Query();
        if ((fromTime != null && fromTime > 0)
                || (tillTime != null && tillTime > 0)) {

            Criteria criteria = null;
            if (fromTime != null && fromTime > 0) {
                criteria = Criteria.where(AbstractMongoEntity.Constants.LAST_UPDATED).gte(fromTime);
            }

            if (tillTime != null && tillTime > 0) {
                if (criteria != null) {
                    criteria = criteria.andOperator(Criteria.where(AbstractMongoEntity.Constants.LAST_UPDATED).lt(tillTime));
                } else {
                    criteria = Criteria.where(AbstractMongoEntity.Constants.LAST_UPDATED).lt(tillTime);
                }
            }
            query.addCriteria(criteria);
        }

        if (limit != null) {
            query.limit(limit);
        } else {
            query.limit(MAX_ALLOWED_FETCH_SIZE);
        }
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.DESC, AbstractMongoEntity.Constants.LAST_UPDATED));
        query.with(new Sort(orderList));
        return runQuery(query, clazz);
    }

    public <E extends AbstractMongoEntity> void setEntityDefaultProperties(Collection<E> entities, String callingUserId) {
        if (entities != null && !entities.isEmpty()) {
            for (E entity : entities) {
                entity.setEntityDefaultProperties(callingUserId);
            }
        }
    }

    public void setFetchParameters(Query query, Integer start, Integer limit) {
        int fetchStart = 0;
        if (start != null && start >= 0) {
            fetchStart = start;
        }
        int fetchSize = DEFAULT_FETCH_SIZE;
        if (limit != null && limit >= 0) {
            fetchSize = limit;
        }
        query.skip(fetchStart);
        query.limit(fetchSize);
    }

    //custom
    public <T extends AbstractMongoEntity> T getEntityByCustomerId(String id, Class<T> clazz) {
        Query query = new Query(Criteria.where("customerid").is(id));
        return getMongoOperations().findOne(query, clazz, clazz.getSimpleName());
    }
    
    
    public void setQueryFieldsFromAbstractEntity(Query query) {
        query.fields().include(AbstractMongoEntity.Constants.CREATION_TIME);
        query.fields().include(AbstractMongoEntity.Constants.LAST_UPDATED);
        query.fields().include(AbstractMongoEntity.Constants.LAST_UPDATED_BY);
        query.fields().include(AbstractMongoEntity.Constants.CREATED_BY);
    }
}
