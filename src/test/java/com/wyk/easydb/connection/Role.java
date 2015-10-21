package com.wyk.easydb.connection;

import com.wyk.esaydb.annotation.Column;
import com.wyk.esaydb.annotation.Table;
import com.wyk.esaydb.interfaces.IEntity;

@Table(name="role")
public class Role implements IEntity{

	@Column(name="id")
	private String id;
	@Column(name="rolename")
	private String rolename;
	@Column(name="des")
	private String des;
	public Role() {
		super();
	}
	public Role(String id, String rolename, String des) {
		super();
		this.id = id;
		this.rolename = rolename;
		this.des = des;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	
	
}
