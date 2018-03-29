package db2.core.mongo;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoMgr {

	private static final MongoMgr INSTANCE = new MongoMgr();

	private MongoMgr() {
	}

	public static MongoMgr getInstance() {
		return MongoMgr.INSTANCE;
	}

	MongoDatabase mongoDatabase;

	public void init() {
		// 连接到 mongodb 服务
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		// 连接到数据库
		mongoDatabase = mongoClient.getDatabase("mycol");
		System.out.println("Connect to database successfully");
	}

	public MongoCollection<Document> getCollection(String name) {
		return mongoDatabase.getCollection(name);
	}

	public FindIterable<Document> find(String name, BasicDBObject object) {
		return getCollection(name).find(object);
	}

	public void del(String name, Bson key) {
		getCollection(name).deleteOne(key);
	}
}
