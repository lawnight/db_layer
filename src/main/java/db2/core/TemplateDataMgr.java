package db2.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db2.config.AppContext;
import db2.dao.MapUnitDAO;
import db2.entity.BaseEntity;
import db2.entity.MapUnitEntity;

/**
 * 所有策划配置都加载在这个类里面
 * 
 * @author near
 */
public class TemplateDataMgr {

	private static final TemplateDataMgr INSTANCE = new TemplateDataMgr();

	private TemplateDataMgr() {
	}

	public static TemplateDataMgr getInstance() {
		return TemplateDataMgr.INSTANCE;
	}

	public Map<Integer, MapUnitEntity> mapUnitTable = new HashMap<>();

	// 启服务器加载
	public void load() {
		mapUnitTable = translateMap(MapUnitDAO.getInstance().listAll());
	}

	/**
	 * 将list转化成主键为key的map
	 */
	@SuppressWarnings("unchecked")
	private <K, V extends BaseEntity> Map<K, V> translateMap(List<V> list) {
		Map<K, V> map = new HashMap<>();
		for (V v : list) {
			Integer key = v.getPrimaryKey();
			map.put((K) key, v);
		}
		return map;
	}

}
