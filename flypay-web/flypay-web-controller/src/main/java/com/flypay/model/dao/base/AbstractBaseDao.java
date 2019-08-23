package com.flypay.model.dao.base;

import com.flypay.utils.Page;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class AbstractBaseDao<T> implements BaseDao<T> {
 
    @PersistenceContext
    private EntityManager em;
 
    private Class<T> clazz = null;
 
    public AbstractBaseDao(){
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.clazz = (Class<T>) pt.getActualTypeArguments()[0];
    }
 
 
    @Override
    public T add(T t) {
        em.persist(t);
        return t;
    }
 
    @Override
    public T update(T t) {
        return em.merge(t);
    }
 
 
    @Override
    public void delete(Serializable id) {
        T t = em.getReference(clazz, id);
        em.remove(t);
    }
 
    @Override
    public int executeUpdate(String jpql, Object... obj) {
        Query query = em.createQuery(jpql);
        if(obj.length > 0){
            for (int i = 0; i < obj.length; i++) {
                query.setParameter((i+1),obj[i]);
            }
        }
        return query.executeUpdate();
    }
 
    @Override
    public T load(Serializable id) {
        return em.find(clazz, id);
    }
 
    @Override
    public T load(String jpql, Object... obj) {
        try{
            Query query = em.createQuery(jpql);
            if(obj.length > 0){
                for (int i = 0; i < obj.length; i++) {
                    query.setParameter((i+1),obj[i]);
                }
            }
            return (T) query.getSingleResult();
        }catch (Exception e){
          return null;
        }
    }
 
    @Override
    public List<T> findAll() {
        return em.createQuery("from "+clazz.getSimpleName()).getResultList();
    }
 
    @Override
    public List<T> find(String jpql, Object... obj) {
        try{
            Query query = em.createQuery(jpql);
            if(obj.length > 0){
                for (int i = 0; i < obj.length; i++) {
                    query.setParameter((i+1),obj[i]);
                }
            }
           return query.getResultList();
        }catch (Exception e){
            return null;
        }
    }
 
    @Override
    public Object findByAggregate(String jpql, Object... obj) {
        Query query = em.createQuery(jpql);
        if(obj.length > 0){
            for (int i = 0; i < obj.length; i++) {
                query.setParameter((i+1),obj[i]);
            }
        }
        Object result = query.getSingleResult();
        return result;
    }
 
    @Override
    public int count() {
        Long num = (Long) em.createQuery("select count(*) from "+clazz.getSimpleName()).getSingleResult();
        return num.intValue();
    }
 
    @Override
    public int count(String jpql,Object... obj) {
        try{
            Query query = em.createQuery(jpql);
            if(obj.length > 0){
                for (int i = 0; i < obj.length; i++) {
                    query.setParameter((i+1),obj[i]);
                }
            }
            Long num = (Long)query.getSingleResult();
            return num.intValue();
        }catch (Exception e){
            return 0;
        }
    }
 
    @Override
    public List<T> findPage(Integer firstIndex, Integer maxResults) {
        return em.createQuery("from "+clazz.getSimpleName()).setFirstResult(firstIndex).setMaxResults(maxResults).getResultList();
    }
 
    @Override
    public List<T> findPage(Integer firstIndex, Integer maxResults,String jpql,Object... obj) {
        try{
            Query query = em.createQuery(jpql);
            if(obj.length > 0){
                for (int i = 0; i < obj.length; i++) {
                    query.setParameter((i+1),obj[i]);
                }
            }
            query.setFirstResult(firstIndex).setMaxResults(maxResults);
            return query.getResultList();
        }catch (Exception e){
            return null;
        }
 
    }
 
    @Override
    public Page<T> findPage(Page page, String jpql, Object... obj) {
        Query query = em.createQuery(jpql);
        if(obj.length > 0){
            for (int i = 0; i < obj.length; i++) {
                query.setParameter((i+1),obj[i]);
            }
        }
        int total = ((Long)query.getSingleResult()).intValue();
        page.setSumCounts(total);
        query.setFirstResult(page.getCur()).setMaxResults(page.getRows());
        List<T> list = query.getResultList();
        page.setItems(list);
        return page;
    }
 
}