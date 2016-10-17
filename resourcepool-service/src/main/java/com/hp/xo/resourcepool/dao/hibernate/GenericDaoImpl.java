package com.hp.xo.resourcepool.dao.hibernate;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.util.Version;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.IdentifierLoadAccess;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.hp.xo.resourcepool.dao.GenericDao; 
import com.hp.xo.resourcepool.exception.SearchException;
import com.hp.xo.resourcepool.model.PhysicsInstances;

/**
 * This class serves as the Base class for all other DAOs - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 * <p/>
 * <p>To register this class in your Spring context file, use the following XML.
 * <pre>
 *      &lt;bean id="fooDao" class="com.hp.dao.hibernate.GenericDaoHibernate"&gt;
 *          &lt;constructor-arg value="com.hp.model.Foo"/&gt;
 *      &lt;/bean&gt;
 * </pre>
 *
 * @author Zhefang Chen
 * 
 * @param <T> a type variable
 * @param <PK> the primary key for that type
 */
public class GenericDaoImpl<T, PK extends Serializable> implements GenericDao<T, PK> {
    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    private Class<T> persistentClass;
    @Resource
    private SessionFactory sessionFactory;
    private Analyzer defaultAnalyzer;

    /**
     * Constructor that takes in a class to see which type of entity to persist.
     * Use this constructor when subclassing.
     *
     * @param persistentClass the class type you'd like to persist
     */
    public GenericDaoImpl(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;
        defaultAnalyzer = new StandardAnalyzer(Version.LUCENE_35);
    }

    /**
     * Constructor that takes in a class and sessionFactory for easy creation of DAO.
     *
     * @param persistentClass the class type you'd like to persist
     * @param sessionFactory  the pre-configured Hibernate SessionFactory
     */
    public GenericDaoImpl(final Class<T> persistentClass, SessionFactory sessionFactory) {
        this.persistentClass = persistentClass;
        this.sessionFactory = sessionFactory;
        defaultAnalyzer = new StandardAnalyzer(Version.LUCENE_35);
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    public Session getSession() throws HibernateException {
        Session sess = getSessionFactory().getCurrentSession();
        if (sess == null) {
            sess = getSessionFactory().openSession();
        }
        return sess;
    }

    @Autowired
    @Required
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        Session sess = getSession();
        return sess.createCriteria(persistentClass).list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> getAllDistinct() {
        Collection<T> result = new LinkedHashSet<T>(getAll());
        return new ArrayList<T>(result);
    }

    /**
     * {@inheritDoc}
     */
    public List<T> search(String searchTerm) throws SearchException {
        Session sess = getSession();
        FullTextSession txtSession = Search.getFullTextSession(sess);

        org.apache.lucene.search.Query qry;
        try {
            qry = HibernateSearchTools.generateQuery(searchTerm, this.persistentClass, sess, defaultAnalyzer);
        } catch (ParseException ex) {
            throw new SearchException(ex);
        }
        org.hibernate.search.FullTextQuery hibQuery = txtSession.createFullTextQuery(qry,
                this.persistentClass);
        return hibQuery.list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T get(PK id) {
        Session sess = getSession();
        IdentifierLoadAccess byId = sess.byId(persistentClass);
        T entity = (T) byId.load(id);

        if (entity == null) {
            log.warn("Uh oh, '" + this.persistentClass + "' object with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(this.persistentClass, id);
        }

        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public boolean exists(PK id) {
        Session sess = getSession();
        IdentifierLoadAccess byId = sess.byId(persistentClass);
        T entity = (T) byId.load(id);
        return entity != null;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T save(T object) {
        Session sess = getSession();
        return (T) sess.merge(object);
    }

    /**
     * {@inheritDoc}
     */
    public void remove(T object) {
        this.removeEntity(object);
    }
    protected void removeEntity(Object object) {
        Session sess = getSession();
        sess.delete(object);
    }

    /**
     * {@inheritDoc}
     */
    public void remove(PK id) {
        Session sess = getSession();
        IdentifierLoadAccess byId = sess.byId(persistentClass);
        T entity = (T) byId.load(id);
        sess.delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    public List<T> findByNamedQuery(String queryName, Map<String, Object> queryParams) {
    	return this.findByNamedQuery(queryName, queryParams, -1, -1);
    }    
    @SuppressWarnings("unchecked")
	public List<T> findByNamedQuery(final String queryName, final Map<String, Object> queryParams, final int pageNumber, final int pageSize) {
    	List<T> results = null;
        Session sess = getSession();
        Query namedQuery = sess.getNamedQuery(queryName);

        for (String s : queryParams.keySet()) {
            namedQuery.setParameter(s, queryParams.get(s));
        }
		if (pageNumber >= 0) {
		    final int firstResult = pageSize * (pageNumber - 1);
			namedQuery.setFirstResult(firstResult);
		}
		if (pageSize > 0) {
			namedQuery.setMaxResults(pageSize);
		}
		results = namedQuery.list();
		if (null == results) {
			results = Collections.emptyList();
		}
		return results;
    }
    @SuppressWarnings("unused")
	public List<?> findByExample(final String entityName, final Object exampleEntity, final int pageNumber, final int pageSize) {
		Assert.notNull(exampleEntity, "Example entity must not be null");
		List<?> results = null;
		Criteria executableCriteria = (entityName != null ? getSession().createCriteria(entityName) : getSession().createCriteria(exampleEntity.getClass()));
		executableCriteria.add(Example.create(exampleEntity).excludeZeroes().enableLike());
		if (pageNumber >= 0) {
			final int firstResult = pageSize * (pageNumber - 1);
			executableCriteria.setFirstResult(firstResult);
		}
		if (pageSize > 0) {
			executableCriteria.setMaxResults(pageSize);
		}
		results = executableCriteria.list();
		if (null == results) {
			results = Collections.emptyList();
		}
		return results; 
	}
    @Override
    public List<?> findByExample(final Object exampleEntity) {
		return this.findByExample(null, exampleEntity, -1, -1);
	}
	protected List<?> findByCriteria(final DetachedCriteria criteria) {
		return findByCriteria(criteria, -1, -1);
	}
	protected List<?> findByCriteria(final DetachedCriteria criteria, final int pageNumber, final int pageSize) {
		List<?> results = null;
		Assert.notNull(criteria, "DetachedCriteria must not be null");
		Criteria executableCriteria = criteria.getExecutableCriteria(this.getSession());
		if (pageNumber >= 0) {
			final int firstResult = pageSize * (pageNumber - 1);
			executableCriteria.setFirstResult(firstResult);
		}
		if (pageSize > 0) {
			executableCriteria.setMaxResults(pageSize);
		}
		results = executableCriteria.list();
		if (null == results) {
			results = Collections.emptyList();
		}
		return results; 
    }

    /**
     * {@inheritDoc}
     */
    public void reindex() {
        HibernateSearchTools.reindex(persistentClass, getSessionFactory().getCurrentSession());
    }


    /**
     * {@inheritDoc}
     */
    public void reindexAll(boolean async) {
        HibernateSearchTools.reindexAll(async, getSessionFactory().getCurrentSession());
    }
	protected Integer countByCriteria(final DetachedCriteria criteria) {
		Integer result = null;
		Criteria executableCriteria = criteria.getExecutableCriteria(this.getSession());
		executableCriteria.setProjection(Projections.rowCount()); 
		Object ur = executableCriteria.uniqueResult();
		if (null == ur) {
			result = new Integer(-1);
		} else {
			result = Integer.parseInt(ur.toString());  
		}
		return result; 
	}
	@Override
	public Integer removeByExample(Object exampleEntity) {
		Integer result = this.countByExample(exampleEntity);
		System.out.println("result : " + result);
		if (result > 10000) {
			throw new RuntimeException("The row count more than 10000, Please using the other way to remove records.");
		} else {
			List<?> entityList = this.findByExample(exampleEntity);
			for (Object obj : entityList) {
				this.removeEntity(obj);
			}
			entityList.clear();
		}
		return result; 
	}
	@Override
	public Integer countByNamedQuery(String queryName, Map<String, Object> queryParams) {
		Integer result = null;
        Session sess = getSession();
        Query namedQuery = sess.getNamedQuery(queryName);
        for (String s : queryParams.keySet()) {
            namedQuery.setParameter(s, queryParams.get(s));
        }
		result = namedQuery.list().size();
		return result;
	}
	@Override
	public Integer countByExample(Object exampleEntity) {		
		return this.countByExample(null, exampleEntity);
	}
	@Override
	public Integer countByExample(String entityName, Object exampleEntity) {
		Integer result = null;
		Criteria executableCriteria = (entityName != null ? getSession().createCriteria(entityName) : getSession().createCriteria(exampleEntity.getClass()));
//		executableCriteria.add(Example.create(exampleEntity));
		executableCriteria.add(Example.create(exampleEntity).excludeZeroes().enableLike());
		executableCriteria.setProjection(Projections.rowCount()); 
		Object ur = executableCriteria.uniqueResult();
		if (null == ur) {
			result = new Integer(-1);
		} else {
			result = Integer.parseInt(ur.toString());  
		}
		return result; 
	}
	
	public List<T> findListByHql(String hql,Object[] paramters, int start, int max) {
		Query query = getSessionFactory().getCurrentSession().createQuery(hql);
		int i=0;
		for(Object obj:paramters){
			query.setParameter(i, obj);
			i++;
		}
		query.setFirstResult(start);
		query.setMaxResults(max);
		return  (List<T>)query.list(); 
	}
	
	public List<T> findListByHql(String hql,Object[] paramters){
		Query query = getSessionFactory().getCurrentSession().createQuery(hql);
		int i=0;
		for(Object obj:paramters){
			query.setParameter(i, obj);
			i++;
		}
		return (List<T>)query.list();
	}
	
}

