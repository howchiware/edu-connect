package com.springboot.reservationsite.model;


public class UserBoardDo {

	private int num;
	private String id;
	private String pwd;
	private String name;
	private String lesson;
	private int level;
	private Role role = Role.USER;

	public enum Role {
		USER, TEACHER, ADMIN // 이용자, 강사, 관리자
	}

	@Override
	public String toString() {
		return "UserBoardDo [num=" + num + ", id=" + id + ", pwd=" + pwd + ", name=" + name + ", lesson=" + lesson
				+ ", level=" + level + ", role=" + role + "]";
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLesson() {
		return lesson;
	}

	public void setLesson(String lesson) {
		this.lesson = lesson;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	

	
}
