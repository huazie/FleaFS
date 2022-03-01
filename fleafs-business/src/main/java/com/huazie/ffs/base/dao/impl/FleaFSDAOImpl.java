package com.huazie.ffs.base.dao.impl;

import com.huazie.fleaframework.common.exception.CommonException;
import com.huazie.fleaframework.db.jpa.dao.impl.AbstractFleaJPADAOImpl;
import com.huazie.fleaframework.db.jpa.transaction.FleaTransactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * FleaFS数据源DAO层父类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FleaFSDAOImpl<T> extends AbstractFleaJPADAOImpl<T> {

    @PersistenceContext(unitName = "fleafs")
    protected EntityManager entityManager;

    @Override
    @FleaTransactional("fleaFSTransactionManager")
    public Number getFleaNextValue(T entity) throws CommonException {
        return super.getFleaNextValue(entity);
    }

    @Override
    @FleaTransactional("fleaFSTransactionManager")
    public boolean remove(long entityId) throws CommonException {
        return super.remove(entityId);
    }

    @Override
    @FleaTransactional("fleaFSTransactionManager")
    public boolean remove(T entity) throws CommonException {
        return super.remove(entity);
    }

    @Override
    @FleaTransactional("fleaFSTransactionManager")
    public boolean remove(String entityId) throws CommonException {
        return super.remove(entityId);
    }

    @Override
    @FleaTransactional("fleaFSTransactionManager")
    public boolean remove(long entityId, T entity) throws CommonException {
        return super.remove(entityId, entity);
    }

    @Override
    @FleaTransactional("fleaFSTransactionManager")
    public boolean remove(String entityId, T entity) throws CommonException {
        return super.remove(entityId, entity);
    }

    @Override
    @FleaTransactional("fleaFSTransactionManager")
    public T update(T entity) throws CommonException {
        return super.update(entity);
    }

    @Override
    @FleaTransactional("fleaFSTransactionManager")
    public List<T> batchUpdate(List<T> entities) throws CommonException {
        return super.batchUpdate(entities);
    }

    @Override
    @FleaTransactional("fleaFSTransactionManager")
    public void save(T entity) throws CommonException {
        super.save(entity);
    }

    @Override
    @FleaTransactional("fleaFSTransactionManager")
    public void batchSave(List<T> entities) throws CommonException {
        super.batchSave(entities);
    }

    @Override
    @FleaTransactional("fleaFSTransactionManager")
    public int insert(String relationId, T entity) throws CommonException {
        return super.insert(relationId, entity);
    }

    @Override
    @FleaTransactional("fleaFSTransactionManager")
    public int update(String relationId, T entity) throws CommonException {
        return super.update(relationId, entity);
    }

    @Override
    @FleaTransactional("fleaFSTransactionManager")
    public int delete(String relationId, T entity) throws CommonException {
        return super.delete(relationId, entity);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

}