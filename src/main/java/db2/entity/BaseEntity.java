/**
 * Copyright(c) 2014 ShenZhen jingqi Network Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-26  下午9:18:13
 */
package db2.entity;

import db2.core.BaseDAO;
import db2.core.CacheDirtyData;

/**
 * @author near
 */
public abstract class BaseEntity implements Cloneable {

	private EntityOperation operation = EntityOperation.NONE;

	// copy出来的临时数据，不存入数据库
	protected boolean isTemp = false;

	// 初始化完之前，不参加dirty的判断
	protected boolean isInited = false;

	public boolean isInited() {
		return isInited;
	}

	public void setInited(boolean isInited) {
		this.isInited = isInited;
	}

	public String getClassName() {
		return "BaseEntity";
	}

	/**
	 * 
	 */
	public BaseEntity() {
	}

	/**
	 * @return the operation
	 */
	public EntityOperation getOperation() {
		return operation;
	}

	/**
	 * @param operation
	 *            the operation to set
	 */
	public void setOperation(final EntityOperation operation) {
		// 初始化的时候会调用set
		if (isInited && !isTemp) {

			if (this.operation == operation) {
				return;
			}

			// update 不能更新insert
			if ((this.operation == EntityOperation.INSERT) && (operation == EntityOperation.UPDATE)) {
				return;
			}

			this.operation = operation;

			if (this.operation != EntityOperation.NONE) {
				CacheDirtyData.getInstance().add(this);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public abstract BaseDAO getDAO();

	public abstract <K> K getPrimaryKey();

	/**
	 * 
	 * @author near
	 */
	public static enum EntityOperation {
		/** */
		NONE,

		/** */
		INSERT,

		/** */
		UPDATE,
		//
		// /** */
		// DELETE
	}

}
