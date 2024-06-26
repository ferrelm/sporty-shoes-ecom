package com.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;;

@Entity
public class Orders {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// pk with auto_increment
	private int oid;
	private Integer pid;				// FK can hold null value but int can't in java side
	private LocalDateTime ldt;

	@ManyToOne
	@JoinColumn(name = "loginId", nullable = true) // Login ID can be null
	private Login login; // Join to the Login entity

	public int getOid() {
		return oid;
	}
	public void setOid(int oid) {
		this.oid = oid;
	}

	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public LocalDateTime getLdt() {
		return ldt;
	}
	public void setLdt(LocalDateTime ldt) {
		this.ldt = ldt;
	}

	public Login getLogin() {
			return login;
		}
	public void setLogin(Login login) {
			this.login = login;
		}

	@Override
	public String toString() {
		return "Orders [oid=" + oid + ", pid=" + pid + ", login=" + (login != null ? login.getUsername() : "null") + ", ldt=" + ldt + "]";
	}

}
