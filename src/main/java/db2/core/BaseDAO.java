package db2.core;

import db2.entity.BaseEntity;

public interface BaseDAO<T extends BaseEntity> {

	public void insert(T mapUnitEntity);

	public void delete(int id);

	public void update(T mapUnitEntity);

	public T get(int id);

	// public int insertAll(List<T> entities);
	//
	// public void updateAll(List<T> entities);

}
