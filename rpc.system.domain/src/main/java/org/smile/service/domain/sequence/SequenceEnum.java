package org.smile.service.domain.sequence;

public enum SequenceEnum {
	
	USER_INFO("seq_user_info", "用户编号与主键"),
	
	ACCOUNT_BILL("seq_account_bill", "账单流水号"),
	
	ACCOUNT_INFO("seq_account_info", "账户编号与主键"),
	
	OPERATE_RECORD("seq_operate_record", "操作记录表主键");
	
	private final String name;
	
	private final String desc;
	
	private SequenceEnum(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}
}
