package com.nxt.ynt.entity;

import java.io.Serializable;

public class DetailsHYData implements Serializable {
	private String industryName;
	private String industryId;
	private String ColumnName;//��Ŀ���ƣ���Ϣͨ��Ŀ���ƣ�
	
	@Override
	public String toString() {
		return "DetailsHYData [industryName=" + industryName + ", industryId="
				+ industryId + ", ColumnName=" + ColumnName + "]";
	}
	public String getIndustryName() {
		return industryName;
	}
	public String getColumnName() {
		return ColumnName;
	}
	public void setColumnName(String columnName) {
		ColumnName = columnName;
	}
	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}
	public String getIndustryId() {
		return industryId;
	}
	public void setIndustryId(String industryId) {
		this.industryId = industryId;
	}


}
