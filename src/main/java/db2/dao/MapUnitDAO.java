package db2.dao;

import db2.config.AppContext;
import db2.core.BaseDAO;
import db2.core.mongo.MongoMgr;
import db2.core.redis.RedisMgr;
import db2.entity.MapUnitEntity;

import java.util.List;

public class MapUnitDAO implements BaseDAO<MapUnitEntity> {

    private static final MapUnitDAO INSTANCE = new MapUnitDAO();

    private MapUnitDAO() {
    }

    public static MapUnitDAO getInstance() {
        return MapUnitDAO.INSTANCE;
    }

//    RedisMgr redisMgr = RedisMgr.getInstance();
//
//    MongoMgr mongoMgr = MongoMgr.getInstance();

    MapUnitSqlDAO mapUnitSqlDAO = (MapUnitSqlDAO) AppContext.getBean("mapUnitSqlDAO");

    String entityName = "MapUnitEntity";

    //////////////////////////////////// reids ///////////////////////////////
//	public void saveRedis(MapUnitEntity entity) {
//		Map<String, String> infos = new HashMap<>();
//		infos.put("id", entity.getId() + "");
//		infos.put("x", entity.getX() + "");
//		infos.put("y", entity.getY() + "");
//		infos.put("type", entity.getType() + "");
//		redisMgr.hmset(entityName + entity.getPrimaryKey(), infos);
//	}
//
//	public MapUnitEntity getRedis(int key) {
//		MapUnitEntity entity = new MapUnitEntity();
//		Map<String, String> info = redisMgr.hgetAll(entityName + key);
//		entity.setId(Integer.parseInt(info.get("id")));
//		entity.setX(Integer.parseInt(info.get("x")));
//		entity.setY(Integer.parseInt(info.get("y")));
//		entity.setType(Integer.parseInt(info.get("type")));
//		return entity;
//	}
//
//	public void delRedis(int key) {
//		redisMgr.del(key + "");
//	}
//
//	public List<MapUnitEntity> getRedisAll() {
//		return getRedisByKeys("_*");
//	}
//
//	public List<MapUnitEntity> getRedisByUserId(int userId) {
//		return getRedisByKeys("_" + userId + "_" + "*");
//	}
//
//	public List<MapUnitEntity> getRedisByKeys(String key) {
//		List<MapUnitEntity> entities = new ArrayList<>();
//		MapUnitEntity entity;
//		Collection<String> Ids = redisMgr.getKeys(entityName + key);
//		for (String id : Ids) {
//			Map<String, String> info = redisMgr.hgetAll(id);
//			entity = new MapUnitEntity();
//			entity.setId(Integer.parseInt(info.get("id")));
//			entity.setX(Integer.parseInt(info.get("x")));
//			entity.setY(Integer.parseInt(info.get("y")));
//			entity.setType(Integer.parseInt(info.get("type")));
//			entities.add(entity);
//		}
//		return entities;
//	}
//
//	//////////////////////////////////// mongo ///////////////////////////////
//
//	public void saveMongo(MapUnitEntity entity) {
//		Document document = new Document();
//		document.append("id", entity.getId());
//		document.append("x", entity.getX());
//		document.append("y", entity.getY());
//		document.append("type", entity.getType());
//		mongoMgr.getCollection(entityName).insertOne(document);
//	}
//
//	public MapUnitEntity getMongo(int key) {
//		BasicDBObject query = new BasicDBObject();
//		query.put("id", key);
//		List<MapUnitEntity> entities = getMongoByKeys(query);
//		if (entities.size() > 0) {
//			return entities.get(0);
//		}
//		return null;
//	}
//
//	public void delMongo(int key) {
//		BasicDBObject query = new BasicDBObject();
//		query.put("id", key);
//		mongoMgr.del(entityName, query);
//	}
//
//	public List<MapUnitEntity> getMongoAll() {
//		return getMongoByKeys(new BasicDBObject());
//	}
//
//	public List<MapUnitEntity> getMongoByKeys(BasicDBObject query) {
//		List<MapUnitEntity> entities = new ArrayList<>();
//		MapUnitEntity entity;
//		for (Document document : mongoMgr.find(entityName, query)) {
//			entity = new MapUnitEntity();
//			entity.setId((Integer) document.get("id"));
//			entity.setX((Integer) (document.get("x")));
//			entity.setY((Integer) (document.get("y")));
//			entity.setType((Integer) (document.get("type")));
//			entities.add(entity);
//		}
//		return entities;
//	}

    //////////////////////////////////// mysql ///////////////////////////////

    public void insert(MapUnitEntity entity) {
        mapUnitSqlDAO.insert(entity);
    }

    public void delete(int id) {
        mapUnitSqlDAO.delete(id);
    }

    public void update(MapUnitEntity mapUnitEntity) {
        mapUnitSqlDAO.update(mapUnitEntity);
    }

    public MapUnitEntity get(int id) {
        return mapUnitSqlDAO.get(id);
    }

    public List<MapUnitEntity> listAll() {
        return mapUnitSqlDAO.listAll();
    }

}
