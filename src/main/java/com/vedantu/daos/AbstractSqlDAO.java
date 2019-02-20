package com.vedantu.daos;

import com.vedantu.enums.ErrorCode;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.vedantu.models.AbstractSqlEntity;
import com.vedantu.requests.AbstractFrontEndListReq;
import com.vedantu.utils.LogFactory;
import org.hibernate.LockMode;

/**
 * Created by somil on 31/01/17.
 */
public abstract class AbstractSqlDAO {

	protected abstract SessionFactory getSessionFactory();

	@Autowired
	private LogFactory logFactory;

	@SuppressWarnings("static-access")
	private Logger logger = logFactory.getLogger(AbstractSqlDAO.class);
	
	public static final int DEFAULT_FETCH_SIZE = 20;

	public AbstractSqlDAO() {
		super();
	}

	protected <E extends AbstractSqlEntity> void saveEntity(E entity, String callingUserId)
			throws RuntimeException {
		SessionFactory sessionFactory = getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction transaction = session.getTransaction();
		try {
			transaction.begin();
			saveEntity(entity, session, callingUserId);
			transaction.commit();
		} catch (Exception e) {
			logger.error(e);
			if (transaction.getStatus().equals(TransactionStatus.ACTIVE)) {
				logger.error("saveEntity Rollback: " + e.getMessage());
				session.getTransaction().rollback();
			}
			throw new RuntimeException(e.getMessage());

		} finally {
			session.close();
		}
	}

	protected <E extends AbstractSqlEntity> void saveEntity(E entity, Session session, String callingUserId) {
		entity.setEntityDefaultProperties(callingUserId);
		session.saveOrUpdate(entity);
	}

	protected <E extends AbstractSqlEntity> void saveAllEntities(Collection<E> entities, String callingUserId) {
		SessionFactory sessionFactory = getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction transaction = session.getTransaction();

		try {
			transaction.begin();
			saveAllEntities(entities, session, callingUserId);
			transaction.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	protected <E extends AbstractSqlEntity> void saveAllEntities(Collection<E> entities, Session session,
			String callingUserId) {
		setEntityDefaultProperties(entities, callingUserId);
		if (entities != null && !entities.isEmpty()) {
			for (E entity : entities) {
				session.save(entity);
			}
		}
	}

	protected <E extends AbstractSqlEntity> void updateEntity(E entity, String callingUserId) {
		SessionFactory sessionFactory = getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction transaction = session.getTransaction();
		try {
			transaction.begin();
			updateEntity(entity, session, callingUserId);
			transaction.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	protected <E extends AbstractSqlEntity> void updateEntity(E entity, Session session, String callingUserId) {
		entity.setEntityDefaultProperties(callingUserId);
		session.update(entity);
	}

	protected <E extends AbstractSqlEntity> void updateAllEntities(Collection<E> entities, String callingUserId) {
		SessionFactory sessionFactory = getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction transaction = session.getTransaction();
		try {
			transaction.begin();
			updateAllEntities(entities, session, callingUserId);
			transaction.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	protected <E extends AbstractSqlEntity> void updateAllEntities(Collection<E> entities, Session session,
			String callingUserId) {
		setEntityDefaultProperties(entities, callingUserId);
		if (entities != null && !entities.isEmpty()) {
			for (E entity : entities) {
				session.saveOrUpdate(entity);
			}
		}
	}

	public <T extends AbstractSqlEntity> List<T> getEntities(Class<T> calzz, AbstractFrontEndListReq req,
			Boolean enabled) {
		SessionFactory sessionFactory = getSessionFactory();
		Session session = sessionFactory.openSession();
		List<T> results = null;
		try {
			results = getEntities(calzz, req, enabled, session);
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	public <T extends AbstractSqlEntity> List<T> getEntities(Class<T> calzz, AbstractFrontEndListReq req,
			Boolean enabled, Session session) {
		List<T> results = null;
		Criteria cr = session.createCriteria(calzz);
		if (enabled != null) {
			if (enabled.equals(true)) {
				cr.add(Restrictions.eq("enabled", true));
			} else {
				cr.add(Restrictions.eq("enabled", false));
			}
		}
		Integer start = req.getStart();
		Integer size = req.getSize();
		if (start == null || start < 0) {
			start = 0;
		}
		if (size == null || size <= 0) {
			size = 20;
		}
		cr.setFirstResult(start);
		cr.setMaxResults(size);
		results = cr.list();
		return results;
	}

	public <T extends AbstractSqlEntity> T getEntityById(Object id, Boolean enabled, Class<T> calzz)
			throws RuntimeException {
		SessionFactory sessionFactory = getSessionFactory();
		Session session = sessionFactory.openSession();
		T result = null;
		try {
			result = getEntityById(id, enabled, calzz, session);
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public <T extends AbstractSqlEntity> T getEntityById(Object id, Boolean enabled, Class<T> calzz, Session session) {
		List<T> results = null;
		Criteria cr = session.createCriteria(calzz);
		cr.add(Restrictions.eq("id", id));
		if (enabled != null) {
			if (enabled.equals(true)) {
				cr.add(Restrictions.eq("enabled", true));
			} else {
				cr.add(Restrictions.eq("enabled", false));
			}
		}
		results = cr.list();
		if (results != null && !results.isEmpty()) {
			return results.get(0);
		}
		return null;
	}

        @SuppressWarnings("unchecked")
        public <T extends AbstractSqlEntity> List<T> runQuery(Session session, Criteria cr, Class<T> calzz) {
            return runQuery(session, cr, calzz, null);
        }

        @SuppressWarnings("unchecked")
        public <T extends AbstractSqlEntity> List<T> runQuery(Session session, Criteria cr,
                Class<T> calzz, LockMode lockMode) {
            if (lockMode != null) {
                cr.setLockMode(lockMode);
            }
            List<T> results = cr.list();
            return results;
        }   

	public <T extends AbstractSqlEntity> Long queryCount(Class<T> calzz) {
		SessionFactory sessionFactory = getSessionFactory();
		Session session = sessionFactory.openSession();
		Long count;
		try {
			count = queryCount(calzz, session);
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
		return count;
	}

	public <T extends AbstractSqlEntity> Long queryCount(Class<T> calzz, Session session) {
		Long count;
		Criteria cr = session.createCriteria(calzz);
		count = (Long) cr.setProjection(Projections.rowCount()).uniqueResult();
		return count;
	}

	public static <E extends AbstractSqlEntity> void setEntityDefaultProperties(Collection<E> entities,
			String callingUserId) {
		if (entities != null && !entities.isEmpty()) {
			for (E entity : entities) {
				entity.setEntityDefaultProperties(callingUserId);
			}
		}
	}

	public void setFetchParameters(Criteria cr, Integer start, Integer limit) {
		int fetchStart = 0;
		if (start != null && start >= 0) {
			fetchStart = start;
		}
		int fetchSize = DEFAULT_FETCH_SIZE;
		if (limit != null && limit >= 0) {
			fetchSize = limit;
		}
		cr.setFirstResult(fetchStart);
		cr.setMaxResults(fetchSize);
	}
	
//	public static <E extends AbstractSqlEntity> void setEntityDefaultProperties(E entity, String callingUserId) {
//		if (StringUtils.isEmpty(callingUserId))
//			callingUserId = DBUtils.getCallingUserId();
//		if (entity.getId() == null) {
//			if (entity.getCreationTime() == null) {
//				entity.setCreationTime(System.currentTimeMillis());
//				entity.setLastUpdatedTime(System.currentTimeMillis());
//			}
//			entity.setCreatedBy(callingUserId);
//			entity.setLastUpdatedby(callingUserId);
//		} else {
//			entity.setLastUpdatedby(callingUserId);
//			entity.setLastUpdatedTime(System.currentTimeMillis());
//		}
//		if (entity.getEnabled() == null) {
//			entity.setEnabled(true);
//		}
//	}
}
