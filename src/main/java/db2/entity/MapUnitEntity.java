/**
 *created by tool DAOGenerate
 */
package db2.entity;

import db2.dao.MapUnitDAO;

/**
 * created by tool
 */
public class MapUnitEntity extends BaseEntity {

	public MapUnitDAO getDAO() {
		return MapUnitDAO.getInstance();
	}

	public String getClassName() {
		return "MapUnitEntity";
	}

	public Integer getPrimaryKey() {
		return id;
	}

	/**
	* 
	*/
	private int id;

	/**
	 * 
	 */
	private int type;

	/**
	 * 
	 */
	private int x;

	/**
	 * 
	 */
	private int y;

	/**
	 * 获得
	 */
	public int getId() {
		return id;
	}

	/**
	 * 设置
	 */
	public void setId(int id) {
		if (id != this.id) {
			this.id = id;
			setOperation(EntityOperation.UPDATE);
		}
	}

	/**
	 * 获得
	 */
	public int getType() {
		return type;
	}

	/**
	 * 设置
	 */
	public void setType(int type) {
		if (type != this.type) {
			this.type = type;
			setOperation(EntityOperation.UPDATE);
		}
	}

	/**
	 * 获得
	 */
	public int getX() {
		return x;
	}

	/**
	 * 设置
	 */
	public void setX(int x) {
		if (x != this.x) {
			this.x = x;
			setOperation(EntityOperation.UPDATE);
		}
	}

	/**
	 * 获得
	 */
	public int getY() {
		return y;
	}

	/**
	 * 设置
	 */
	public void setY(int y) {
		if (y != this.y) {
			this.y = y;
			setOperation(EntityOperation.UPDATE);
		}
	}

	/**
	 * 克隆
	 */
	public MapUnitEntity clone() {
		MapUnitEntity clone = new MapUnitEntity();
		clone.id = this.id;
		clone.type = this.type;
		clone.x = this.x;
		clone.y = this.y;
		return clone;
	}
}