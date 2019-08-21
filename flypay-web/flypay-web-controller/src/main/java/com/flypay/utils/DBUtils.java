package com.flypay.utils;
import com.flypay.conf.ConfigBean;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
@Component
public class DBUtils {
    private static final Logger LOGGER = Logger.getLogger(DBUtils.class);
    @Autowired
    ConfigBean cb;
    @Autowired
    @PersistenceContext
    private EntityManager em;

    /**
     * 根据id删除表数据
     * @param queryStr
     * @param id
     * @param queryType
     * @return
     * @throws Exception
     */
    public Integer delete(final String queryStr, final Integer id,
                          final String queryType) throws Exception {
        if (null == id) {
            throw new NullPointerException(
                    "param:id is not allowed null when you delete by id");
        }
        Query query = createQuery(em, queryStr, queryType, false,null);
        query.setParameter(1, id);
        return query.executeUpdate();
    }
    /**
     * @Description: 更新操作，适用于 delete ,update 操作，批量的，多条件的。
     * @param updateStr
     *            要执行的语句
     * @param params
     *            传入的参数
     * @param queryType
     *            HQL/SQl
     * @return
     * @author Li Weidong
     * @date 2015-12-25
     */
    public Integer executeUpdate(final String updateStr,
                                        final List<Object> params, final String queryType) {
        checkQueryType(queryType);
        Query query = createQuery(em, updateStr, queryType, false, null);

        setParamters(params, query);
        return query.executeUpdate();
    }
    /**
     * @Description: 这是一个泛型方法，把查询结果映射成一个指定的对象，适合select 本地sql 查询
     * @param entityClass
     *            你要转换成的对对象。
     * @param page
     * @param queryStr
     * @param params
     * @return
     * @throws Exception
     * @author Li Weidong
     * @date 2015-12-25
     */
    public <T> List<T> queryBySQL(final Class<T> entityClass, final Page page,final String queryStr, final List<Object> params) throws Exception {

        Query query = em.createNativeQuery(queryStr);
        query.unwrap(SQLQuery.class).setResultTransformer(
                Transformers.aliasToBean(entityClass));

        setParamters(params, query);
        if (null != page) {
            query.setFirstResult(page.getStartRow());
            query.setMaxResults(page.getRows());
        }
        return query.getResultList();
    }
    /**
     *
     * 描述: 这是一个泛型方法，把查询结果映射成一个指定的对象，适合select 本地sql 查询 返回带泛型集合
     * @author:  Liu Yufan
     * @date:2018年8月3日 下午4:33:33
     * @param entityClass   要转的对象
     * @param page          分页对象
     * @param queryStr      sql
     * @param params        参数
     * @return
     * @throws Exception
     */
    public <T> List<T> queryBySQL(final String queryStr, final Page page,final Class<T> entityClass, final Object... params) throws Exception {

        Query query = em.createNativeQuery(queryStr);
        query.unwrap(SQLQuery.class).setResultTransformer(
                Transformers.aliasToBean(entityClass));

        setParamters(params, query);
        return query.getResultList();
    }
    /**
     * @Description: 查询总条数，通用的，可以是hql/sql
     * @param queryStr
     * @param params
     * @param queryType
     *            查询类型 HQL/SQL
     * @return
     * @throws Exception
     * @author Li Weidong
     * @date 2015-12-25
     */
    public Integer queryTotleRows(final String queryStr,
                                         final List<Object> params, final String queryType) throws Exception {

        checkQueryType(queryType);

        Query query = null;
        if (queryType.equals(QUERY_TYPE_HQL))
            query = em.createQuery(queryStr);
        if (queryType.equals(QUERY_TYPE_SQL))
            query = em.createNativeQuery(queryStr);

        setParamters(params, query);
        List list = query.getResultList();
        Object count = 0;
        if( list != null && !list.isEmpty()) {
            count = list.get(0);
        }
        Long l = Long.parseLong(count.toString());
        return l.intValue();
    }
    /**
     *
     * 描述: 这是一个泛型方法，把查询结果映射成一个指定的对象，适合select 本地sql 查询 返回单个对象
     * @author:  Liu Yufan
     * @date:2018年8月3日 下午4:33:33
     * @param entityClass   要转的对象
     * @param queryStr      sql
     * @param params        参数
     * @return
     * @throws Exception
     */
    public <T> T queryBySQL(final Class<T> entityClass,final String queryStr, final Object... params) throws Exception {

        Query query = em.createNativeQuery(queryStr);
        query.unwrap(SQLQuery.class).setResultTransformer(
                Transformers.aliasToBean(entityClass));

        setParamters(params, query);
        List<T> res = query.getResultList();
        if( res == null || res.isEmpty() || res.size() != 1)
            return null;

        return res.get(0);
    }
    /**
     * @Description: sql查询，返回Map类型的结果集
     * @param queryStr
     * @param params
     * @return
     * @throws Exception
     * @author Li Weidong
     * @date 2016-5-11
     */
    public Map<String,Object> queryBySQL(final String queryStr, final Object... params) throws Exception {

        Query query = em.createNativeQuery(queryStr);
        query.unwrap(SQLQuery.class).setResultTransformer(
                Transformers.ALIAS_TO_ENTITY_MAP);

        setParamters(params, query);
        List<Map<String,Object>> resultList = query.getResultList();
        if( resultList != null && resultList.size() == 1)
            return resultList.get(0);
        return null;
    }
    /**
     * 分页查询返回list<map>
     * @param page
     * @param queryStr
     * @param params
     * @return
     */
    public List<Map<String,Object>> queryBySql2Map(final Page page, final String queryStr, final List<Object> params){
        Query query = em.createNativeQuery(queryStr);
        query.unwrap(SQLQuery.class).setResultTransformer(
                Transformers.ALIAS_TO_ENTITY_MAP);

        setParamters(params, query);
        if (null != page) {
            query.setFirstResult(page.getStartRow());
            query.setMaxResults(page.getRows());
        }
        return query.getResultList();
    }
    /**
     * @Description: 检查参数合法性
     * @param queryType
     * @author Li Weidong
     * @date 2015-12-25
     */
    private static void checkQueryType(final String queryType) {

        if (!StringUtil.hasText(queryType)
                || (!queryType.equals(QUERY_TYPE_HQL) && !queryType
                .equals(QUERY_TYPE_SQL)))
            throw new IllegalStateException(
                    "param queryParam is error,allowed:HQL/SQL");

    }
    /**
     * @Description: 根据查询类型创建query对象
     * @param em
     * @param queryStr
     * @param queryType
     *            HQL/SQL
     * @param mapClass
     *            是否需要也class类进行映射，一般是通过select 查询属性集合时用到<br/>
     *            把一个对象数组的集合映射为对象的集合。
     * @return
     * @author Li Weidong
     * @date 2015-12-25
     */
    private Query createQuery(EntityManager em, final String queryStr,
                                     final String queryType, boolean mapClass,Class entityClass) {
        Query query = null;
        if (QUERY_TYPE_HQL.equals(queryType))
            query = em.createQuery(queryStr);
        if (QUERY_TYPE_SQL.equals(queryType)) {
            if (mapClass) {
                query = em.createNativeQuery(queryStr);
                query.unwrap(SQLQuery.class).setResultTransformer(
                        Transformers.aliasToBean(entityClass));
            } else
                query = em.createNativeQuery(queryStr);
        }
        return query;
    }
    /**
     * 生成表id
     * @param dbNo
     * @param tableIndex
     * @return
     */
    public static final Long id_bit = 100000L;
    public static final Long order_bit = 1000L;
    @Transactional
    public Long creatId(Long bit ,String dbNo,TableIndex tableIndex){
        if((StringUtil.hasText(dbNo) && !StringUtil.isNum(dbNo)) || dbNo.length() > 2){
            dbNo = getDistributeDBNo(dbNo);
        }
        long PKID = 0,id = 0;
        //EntityTransaction transaction = em.getTransaction();
        //if( !transaction.isActive())
        //    transaction.begin();
        try {
            Query query = em.createNativeQuery("UPDATE id_build SET current_no = current_no + 1 , update_time = NOW() WHERE table_name = ?");
            query.setParameter(1,tableIndex.name());
            int result = query.executeUpdate();
            PKID = (100 + tableIndex.ordinal());
            if( result > 0 ){
                query = em.createNativeQuery("SELECT current_no FROM id_build WHERE table_name = ?");
                query.setParameter(1,tableIndex.name());
                BigInteger rs = (BigInteger) query.getSingleResult();
                id=rs.longValue();
                PKID = PKID * bit + id;
            }else{
                //生产一条
                String insert = "INSERT INTO id_build (table_name,current_no,add_time,update_time)VALUES ('"+tableIndex.name()+"','1',now(),now())";
                query = em.createNativeQuery(insert);
                query.executeUpdate();
                PKID = PKID * bit + 1;
            }
            //transaction.commit();
            String temp = dbNo + PKID;
            if( !id_bit.equals(bit)){
                temp = cb.areaCode + temp;
            }
            return Long.valueOf(temp);
        }catch (Exception e){
            //transaction.rollback();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            LOGGER.error("生成id失败使用默认id",e);
            throw e;
        }

    }

    public String getDistributeDBNo(String biName){
        try {
            if(!StringUtil.hasText(biName)) {
                biName = "飞付科技";
            }
            ConsistentHash.hash(biName);
            String virtualkey = ConsistentHash.getShardInfo(biName);
            if(Integer.parseInt(virtualkey)<10){
                virtualkey = ("0"+virtualkey);
            }
            return virtualkey;
        } catch (Exception e) {
            try {
                throw new Exception("getDistributeDBNo_哈希分组失败");
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return "00";
    }
    public enum TableIndex{
        business_info;
    }
    /**
     * @Description: 处理查询参数
     * @param params
     * @param query
     * @author Li Weidong
     * @date 2015-12-25
     */
    private void setParamters(final List<Object> params, Query query) {

        if (null != params) {
            for (int i = 0; i < params.size(); i++) {
                query.setParameter(i + 1, params.get(i));
            }
        }
    }
    private static void setParamters(Object[] params, Query query) {
        if( params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
    }
    private static final String QUERY_TYPE_SQL = "SQL";
    private static final String QUERY_TYPE_HQL = "HQL";
}
