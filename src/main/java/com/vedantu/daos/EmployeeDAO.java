/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vedantu.daos;

import com.vedantu.models.Employee;
import com.vedantu.utils.LogFactory;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author ajith
 */
@Service
public class EmployeeDAO extends AbstractSqlDAO {

    @Autowired
    private LogFactory logFactory;

    @SuppressWarnings("static-access")
    private Logger logger = logFactory.getLogger(EmployeeDAO.class);

    public EmployeeDAO() {
        super();
    }

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    protected SessionFactory getSessionFactory() {
        return sqlSessionFactory.getSessionFactory();
    }

    public void create(Employee e) {
        try {
            if (e != null) {
                saveEntity(e, e.getName());
            }
        } catch (Exception ex) {
            logger.error("Create error: " + ex.getMessage());
        }
    }

    public Employee getByUserId(Long userId) {
        SessionFactory sessionFactory = sqlSessionFactory.getSessionFactory();
        Session session = sessionFactory.openSession();
        Employee userChallengeStats = null;
        try {
            userChallengeStats = getByUserId(userId, session);
        } catch (Exception ex) {
            logger.error("getByUserId userChallengeStats: " + ex.getMessage());
            userChallengeStats = null;
        } finally {
            session.close();
        }
        return userChallengeStats;
    }

    public Employee getByUserId(Long userId, Session session) {
        Employee userChallengeStats = null;
        if (userId != null) {
            Criteria cr = session.createCriteria(Employee.class);
            cr.add(Restrictions.eq("userId", userId));
            List<Employee> results = runQuery(session, cr, Employee.class);
            if (!CollectionUtils.isEmpty(results)) {
                userChallengeStats = results.get(0);
            } else {
                logger.info("getByUserId Employee : no Employee found " + userId);
            }
        }
        return userChallengeStats;
    }

   /* public void update(Employee p, Session session, String callingUserId) {
        if (p != null) {
            logger.info("update: " + p.toString());
            updateEntity(p, session, callingUserId);
        }
        */
        
          public void update(Employee p ,String callingUserId) {
        	  if (p != null) {
            logger.info("update: " + p.toString());
            saveEntity(p, callingUserId);
        }
         
    }

}
