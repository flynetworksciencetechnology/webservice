package com.flypay.model.dao.base;

import com.flypay.utils.Page;

import java.io.Serializable;
import java.util.List;

public interface BaseDao<T> {
    /**
     * 添加实体类
     * @param t
     * @return
     */
    public T add(T t);
 
    /**
     * 更新实体类
     * @param t
     * @return
     */
    public T update(T t);
 
    /**
     * 根据主键ID删除实体类
     * @param id
     */
    public void delete(Serializable id);
 
    /**
     * 根据JPQL语句更新或删除操作
     * @param jpql
     * @param obj
     */
    public int executeUpdate(String jpql,Object... obj);
 
    /**
     * 根据主键ID查找单个实体类
     * @param id
     * @return
     */
    public T load(Serializable id);
 
    /**
     * 根据JPQL语句查询单个实体类
     * @param jpql
     * @param obj（参数可有可无）
     * @return
     */
    public T load(String jpql,Object... obj);
 
    /**
     * 查找所有的实体类
     * @return
     */
    public List<T> findAll();
 
    /**
     * 根据JPQL语句查询集合实体类
     * @param jpql
     * @param obj（参数可有可无）
     * @return
     */
    public List<T> find(String jpql,Object... obj);
 
    /**
     * 聚合查询
     * @param jpql
     * @param obj
     * @return
     */
    public Object findByAggregate(String jpql,Object... obj);
 
    /**
     * 查找总记录数
     * @return
     */
    public int count();
 
    /**
     * 根据JPQL语句查询记录数
     * @param jpql
     * @param obj
     * @return
     */
    public int count(String jpql,Object... obj);
 
    /**
     * 分页查询集合实体类
     * @param firstIndex
     * @param maxResults
     * @return
     */
    public List<T> findPage(Integer firstIndex, Integer maxResults);
 
    /**
     * 根据JPQL语句查询集合实体类
     * @param firstIndex
     * @param maxResults
     * @param jpql
     * @param obj
     * @return
     */
    public List<T> findPage(Integer firstIndex, Integer maxResults,String jpql,Object... obj);
 
 
    public Page<T> findPage(Page page, String jpql, Object... obj);
 
}