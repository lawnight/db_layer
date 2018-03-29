/**
 * Copyright(c) 2014 ShenZhen jingqi Network Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-20  下午2:56:15
 */
package db2.core.redis;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Resource;

import db2.config.AppContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedisPool;

/**
 * REDIS管理器. 命令参考:http://redisdoc.com/
 * 
 * Jedis连接池操作步骤如下： a.获取Jedis实例需要从JedisPool中获取； b.用完Jedis实例需要返还给JedisPool；
 * c.如果Jedis在使用过程中出错，则也需要还给JedisPool；
 * 
 * @author near
 */

public class RedisMgr {

	private static final RedisMgr INSTANCE = new RedisMgr();

	private RedisMgr() {
	}

	public static RedisMgr getInstance() {
		return RedisMgr.INSTANCE;
	}

	/** redis 连接池 */
	JedisPool jedisPool = (JedisPool) AppContext.getBean("jedisPool");

	@Resource
	private ShardedJedisPool shardedJedisPool;

	public void stop() throws Exception {
		if (jedisPool != null) {
			jedisPool.destroy();
		}
	}

	/**
	 * 同步获取Jedis实例
	 * 
	 * @return Jedis
	 */
	public synchronized Jedis getJedis() {
		Jedis jedis = null;
		try {
			if (jedisPool != null) {
				jedis = jedisPool.getResource();
			}
		} finally {
			returnResource(jedis);
		}
		return jedis;
	}

	/**
	 * 释放jedis资源
	 * 
	 * @param jedis
	 */
	private void returnResource(final Jedis jedis) {
		if (jedis != null && jedisPool != null) {
			jedisPool.returnResourceObject(jedis);
		}
	}

	/**
	 * 测试连接
	 * 
	 * @return
	 */
	public void pingTest() {
		execute(new RedisExecuteCallBack<Void>() {
			@Override
			public Void execute(Jedis jedis) {
				jedis.ping();
				return null;
			}
		});
	}

	/**
	 * 把对象存到RediS的hash中。
	 */
	public void hmset(String key, Map<String, String> map) {
		hmset(key, map, -1);
	}

	/**
	 * 把对象存到RediS的hash中。
	 */
	public void hmset(final String key, final Map<String, String> map, final int expire) {
		execute(new RedisExecuteCallBack<Void>() {
			@Override
			public Void execute(Jedis jedis) {
				jedis.hmset(key, map);
				if (expire != -1) {
					jedis.expire(key, expire);
				}
				return null;
			}
		});
	}

	/**
	 * 获得hash
	 */
	public Map<String, String> hgetAll(final String key) {
		return execute(new RedisExecuteCallBack<Map<String, String>>() {
			@Override
			public Map<String, String> execute(Jedis jedis) {
				// TODO Auto-generated method stub
				return jedis.hgetAll(key);
			}
		});
	}

	public void del(final String key) {
		execute(new RedisExecuteCallBack<Void>() {
			@Override
			public Void execute(Jedis jedis) {
				// TODO Auto-generated method stub
				jedis.del(key);
				return null;
			}
		});
	}

	/**
	 * 查询key
	 * 
	 * @param key
	 * @return
	 */
	public Collection<String> getKeys(final String key) {
		return execute(new RedisExecuteCallBack<Collection<String>>() {
			@Override
			public Collection<String> execute(Jedis jedis) {
				// TODO Auto-generated method stub
				return jedis.keys(key);
			}
		});
	}

	public void sub() {
		execute(new RedisExecuteCallBack<Void>() {

			@Override
			public Void execute(Jedis jedis) {
				RedisListener listener = new RedisListener();
				jedis.subscribe(listener, new String[] { "123" });
				return null;
			}
		});
	}

	public Long publish(final String channle, final String message) {
		return execute(new RedisExecuteCallBack<Long>() {
			@Override
			public Long execute(Jedis jedis) {
				return jedis.publish(channle, message);
			}
		});

	}

	private <T> T execute(RedisExecuteCallBack<T> action) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return action.execute(jedis);
		} catch (Exception e) {
			throw (e);
		} finally {
			returnResource(jedis);
		}
	}

	///////////////////////////////////// sub/////////////////////////
}
