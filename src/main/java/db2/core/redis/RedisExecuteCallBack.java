package db2.core.redis;

import redis.clients.jedis.Jedis;

public interface RedisExecuteCallBack<T> {
	public T execute(Jedis jedis);
}
