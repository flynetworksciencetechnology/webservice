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
        if ("HQL".equals(queryType))
            query = em.createQuery(queryStr);
        if ("HQL".equals(queryType)) {
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
}
