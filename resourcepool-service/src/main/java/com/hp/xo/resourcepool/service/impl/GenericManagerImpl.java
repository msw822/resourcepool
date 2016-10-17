package com.hp.xo.resourcepool.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hp.xo.resourcepool.dao.GenericDao;
import com.hp.xo.resourcepool.service.GenericManager;

/**
 * This class serves as the Base class for all other Managers - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 *
 * <p>To register this class in your Spring context file, use the following XML.
 * <pre>
 *     &lt;bean id="userManager" class="com.hp.service.impl.GenericManagerImpl"&gt;
 *         &lt;constructor-arg&gt;
 *             &lt;bean class="com.hp.dao.hibernate.GenericDaoHibernate"&gt;
 *                 &lt;constructor-arg value="com.hp.model.User"/&gt;
 *                 &lt;property name="sessionFactory" ref="sessionFactory"/&gt;
 *             &lt;/bean&gt;
 *         &lt;/constructor-arg&gt;
 *     &lt;/bean&gt;
 * </pre>
 *
 * <p>If you're using iBATIS instead of Hibernate, use:
 * <pre>
 *     &lt;bean id="userManager" class="com.hp.service.impl.GenericManagerImpl"&gt;
 *         &lt;constructor-arg&gt;
 *             &lt;bean class="com.hp.dao.ibatis.GenericDaoiBatis"&gt;
 *                 &lt;constructor-arg value="com.hp.model.User"/&gt;
 *                 &lt;property name="dataSource" ref="dataSource"/&gt;
 *                 &lt;property name="sqlMapClient" ref="sqlMapClient"/&gt;
 *             &lt;/bean&gt;
 *         &lt;/constructor-arg&gt;
 *     &lt;/bean&gt;
 * </pre>
 *
 * @author Zhefang Chen
 * @param <T> a type variable
 * @param <PK> the primary key for that type
 */
@Transactional(propagation=Propagation.REQUIRED)
public class GenericManagerImpl<T, PK extends Serializable> implements GenericManager<T, PK> {
    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
	protected final Logger log = Logger.getLogger(this.getClass());

    /**
     * GenericDao instance, set by constructor of child classes
     */
    protected GenericDao<T, PK> dao;

    public GenericManagerImpl() {}

    public GenericManagerImpl(GenericDao<T, PK> genericDao) {
        this.dao = genericDao;
    }

    /**
     * {@inheritDoc}
     */
    public List<T> getAll() {
        return dao.getAll();
    }

    /**
     * {@inheritDoc}
     */
    public T get(PK id) {
        return dao.get(id);
    }

    /**
     * {@inheritDoc}
     */
    public boolean exists(PK id) {
        return dao.exists(id);
    }

    /**
     * {@inheritDoc}
     */
    public T save(T object) {
        return dao.save(object);
    }

    /**
     * {@inheritDoc}
     */
    public void remove(T object) {
        dao.remove(object);
    }

    /**
     * {@inheritDoc}
     */
    public void remove(PK id) {
        dao.remove(id);
    }


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> search(String q, Class clazz) {
        if (q == null || "".equals(q.trim())) {
            return getAll();
        }
        return dao.search(q);
    }

    /**
     * {@inheritDoc}
     */
    public void reindex() {
        dao.reindex();
	}

    /**
     * {@inheritDoc}
     */
    public void reindexAll(boolean async) {
        dao.reindexAll(async);
    }
	@Transactional(readOnly = true)
	public List<?> findByExample(Object exampleEntity) {
		return dao.findByExample(exampleEntity);
	}
	@Override
	public List<?> findByExample(String entityName, Object exampleEntity, int pageNumber, int pageSize) {
		return dao.findByExample(entityName, exampleEntity, pageNumber, pageSize);
	}
	@Override
	public List<?> findByNamedQuery(String queryName, Map<String, Object> queryParams, int pageNumber, int pageSize) {
		return dao.findByNamedQuery(queryName, queryParams, pageNumber, pageSize);
	}
	@Override
	public Integer removeByExample(Object exampleEntity) {
		return dao.removeByExample(exampleEntity);
	}
	@Override
	public Integer countByNamedQuery(String queryName, Map<String, Object> queryParams) {
		return dao.countByNamedQuery(queryName, queryParams);
	}
	@Override
	public Integer countByExample(Object exampleEntity) {
		return dao.countByExample(exampleEntity);
	}
	@Override
	public Integer countByExample(String entityName, Object exampleEntity) {
		return dao.countByExample(entityName, exampleEntity);
	}
}
