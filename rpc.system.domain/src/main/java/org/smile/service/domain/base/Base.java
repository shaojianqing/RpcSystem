package org.smile.service.domain.base;

import java.io.Serializable;
import java.sql.Timestamp;

public class Base implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final short TRUE = 1;
	
	public static final short FALSE = 0;
	
	private String id;
	
	private Short isValid;
	
	private Timestamp createTime;
	
	private Timestamp operateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Short getIsValid() {
		return isValid;
	}

	public void setIsValid(Short isValid) {
		this.isValid = isValid;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Timestamp operateTime) {
		this.operateTime = operateTime;
	}
}
