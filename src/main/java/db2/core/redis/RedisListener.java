package db2.core.redis;

import redis.clients.jedis.JedisPubSub;

public class RedisListener extends JedisPubSub {

	// 取得按表达式的方式订阅的消息后的处理
	public void onPMessage(String pattern, String channel, String message) {
		System.out.println(pattern + "=" + channel + "=" + message);
	}
}
