package com.flypay.utils;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author chunhui.tan
 * @version 创建时间：2018年10月18日 下午2:31:35
 *
 */
@Component
public class RedisUtils {
	private static final Logger logger = Logger.getLogger(RedisUtils.class);
	@Autowired
	private JedisPool jedisPool;
	@Value("${spring.redis.open}")
	private Boolean isOpen;
	/**
	 * 不设置过期时长
	 */
	public final static int NOT_EXPIRE = -1;


	/**
	 * 插入值-对象,指定数据库 指定过期时间
	 *
	 * @param key     键
	 * @param value   值
	 * @param dbIndex 数据库索引 范围 0-15 默认0
	 * @param expire  过期时间 单位：秒
	 */
	public void set(String key, Object value, RedisDBIndex dbIndex, int expire) {
		if( !isOpen) {
			logger.error("缓存未开启");
			return;
		}
		Jedis jedis = getJedis(dbIndex);
		jedis.set(key, toJson(value));
		if (expire != NOT_EXPIRE) {
			jedis.expire(key, expire);
		}
	}

	/**
	 * 插入值-对象,指定数据库索引
	 *
	 * @param key     键
	 * @param value   值
	 * @param dbIndex 数据库索引 范围 0-15,默认0
	 */
	public void set(String key, Object value, RedisDBIndex dbIndex) {
		if( !isOpen) {
			logger.error("缓存未开启");
			return;
		}
		set(key, value, dbIndex, NOT_EXPIRE);
	}

	/**
	 * 插入值-对象 ,默认0 index数据库
	 *
	 * @param key   键
	 * @param value 值
	 */
	public void set(String key, Object value) {
		if( !isOpen) {
			logger.error("缓存未开启");
			return;
		}
		set(key, value, RedisDBIndex.base, NOT_EXPIRE);
	}

	/**
	 * 获取值-对象,指定数据库索引,并设置过期时间
	 *
	 * @param key     键
	 * @param clazz   字节码对象
	 * @param dbIndex 数据库索引 范围 0-15 默认0
	 * @param expire  过期时间 单位：秒
	 * @return
	 */
	public <T> T get(String key, Class<T> clazz, RedisDBIndex dbIndex, int expire) {
		if( !isOpen) {
			logger.error("缓存未开启");
			return null;
		}
		Jedis jedis = getJedis(dbIndex);
		try {
			String value = jedis.get(key);
			if (expire != NOT_EXPIRE) {
				jedis.expire(key, expire);
			}
			return value == null ? null : fromJson(value, clazz);
		} finally {
			jedis.close();
		}
	}

	/**
	 * 取值-对象 指定数据库索引
	 *
	 * @param key     键
	 * @param clazz   字节码对象
	 * @param dbIndex 数据库索引 范围 0-15 默认0
	 * @return
	 */
	public <T> T get(String key, Class<T> clazz, RedisDBIndex dbIndex) {
		if( !isOpen) {
			logger.error("缓存未开启");
			return null;
		}
		return get(key, clazz, dbIndex, NOT_EXPIRE);
	}

	/**
	 * 取值-对象
	 *
	 * @param key   键
	 * @param clazz 字节码对象
	 * @return
	 */
	public <T> T get(String key, Class<T> clazz) {
		if( !isOpen) {
			logger.error("缓存未开启");
			return null;
		}
		return get(key, clazz, RedisDBIndex.base, NOT_EXPIRE);
	}

	/**
	 * 取值-字符串,指定数据库索引,设置过期时间
	 *
	 * @param key     键
	 * @param dbIndex 数据库索引 范围 0-15 默认0
	 * @param expire  过期时间 单位：秒
	 * @return
	 */
	public String get(String key, RedisDBIndex dbIndex, int expire) {
		if( !isOpen) {
			logger.error("缓存未开启");
			return null;
		}
		Jedis jedis = getJedis(dbIndex);
		try {
			String value = jedis.get(key);
			if (expire != NOT_EXPIRE) {
				jedis.expire(key, expire);
			}
			return value;
		} finally {
			jedis.close();
		}
	}

	/**
	 * 取值-字符串,指定数据库索引
	 *
	 * @param key     键
	 * @param dbIndex 数据库索引 范围 0-15 默认0
	 * @return
	 */
	public String get(String key, RedisDBIndex dbIndex) {
		if( !isOpen) {
			logger.error("缓存未开启");
			return null;
		}
		return get(key, dbIndex, NOT_EXPIRE);
	}

	/**
	 * 取值-字符串
	 *
	 * @param key 键
	 * @return
	 */
	public String get(String key) {
		if( !isOpen) {
			logger.error("缓存未开启");
			return null;
		}
		return get(key, RedisDBIndex.base, NOT_EXPIRE);
	}

	/**
	 * 删除 指定数据库索引
	 *
	 * @param key     键
	 * @param dbIndex 数据库索引 范围 0-15 默认0
	 */
	public void delete(String key, RedisDBIndex dbIndex) {
		if( !isOpen) {
			logger.error("缓存未开启");
			return;
		}
		Jedis jedis = getJedis(dbIndex);
		try {
			jedis.del(key);
		} finally {
			jedis.close();
		}
	}

	/**
	 * 删除
	 *
	 * @param key 键
	 */
	public void delete(String key) {
		if( !isOpen) {
			logger.error("缓存未开启");
			return;
		}
		delete(key, RedisDBIndex.base);
	}

	/**
	 * 设置过期时间
	 *
	 * @param key     键
	 * @param dbIndex 数据库索引 范围 0-15 默认0
	 * @param expire  过期时间 单位：秒
	 */
	public void expire(String key, RedisDBIndex dbIndex, int expire) {
		if( !isOpen) {
			logger.error("缓存未开启");
			return;
		}
		Jedis jedis = getJedis(dbIndex);
		try {
			if (expire != NOT_EXPIRE) {
				jedis.expire(key, expire);
			}
		} finally {
			jedis.close();
		}
	}

	/**
	 * 设置过期时间
	 *
	 * @param key    键
	 * @param expire 过期时间 单位：秒
	 */
	public void expire(String key, int expire) {
		if( !isOpen) {
			logger.error("缓存未开启");
			return;
		}
		expire(key, RedisDBIndex.base, expire);
	}

	/**
	 * 获取jedis对象，并指定dbIndex
	 *
	 * @param dbIndex
	 */
	private Jedis getJedis(RedisDBIndex dbIndex) {

		Jedis jedis = jedisPool.getResource();
		Integer index = dbIndex.ordinal();
		if (index == null || index > 15 || index < 0) {
			index = 0;
		}
		jedis.select(index);
		return jedis;
	}

	/**
	 * Object转成JSON数据
	 */
	private String toJson(Object object) {
		if (object instanceof Integer || object instanceof Long || object instanceof Float || object instanceof Double
				|| object instanceof Boolean || object instanceof String) {
			return String.valueOf(object);
		}

		return JSON.toJSONString(object);

	}

	/**
	 * JSON数据，转成Object
	 */
	private <T> T fromJson(String json, Class<T> clazz) {
		return JSON.parseObject(json, clazz);
	}

	public Long ttl(String key) {
		if( !isOpen) {
			logger.error("缓存未开启");
			return null;
		}
		return ttl(key,null);
	}
	public Long ttl(String key, RedisDBIndex index){
		if( !isOpen) {
			logger.error("缓存未开启");
			return null;
		}
		Jedis jedis = getJedis(index);
		return jedis.ttl(key);
	}
	public enum RedisDBIndex{
		base;
	}

}
