package com.springboot.reservationsite.model;

import jakarta.persistence.*;

@Entity
@Table(name = "enquirytable")
public class EnquirytableBoardDo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;

    private int lessonId;
    private String title;
    private String title_teacher;
    private String content;
    private String content_teacher;
    private String userId;
    private String teacherId;


    public EnquirytableBoardDo() {
    }


	public int getNum() {
		return num;
	}


	public void setNum(int num) {
		this.num = num;
	}


	public int getLessonId() {
		return lessonId;
	}


	public void setLessonId(int lessonId) {
		this.lessonId = lessonId;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getTitle_teacher() {
		return title_teacher;
	}


	public void setTitle_teacher(String title_teacher) {
		this.title_teacher = title_teacher;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getContent_teacher() {
		return content_teacher;
	}


	public void setContent_teacher(String content_teacher) {
		this.content_teacher = content_teacher;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getTeacherId() {
		return teacherId;
	}


	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}


	public EnquirytableBoardDo(int num, int lessonId, String title, String title_teacher, String content,
			String content_teacher, String userId, String teacherId) {
		super();
		this.num = num;
		this.lessonId = lessonId;
		this.title = title;
		this.title_teacher = title_teacher;
		this.content = content;
		this.content_teacher = content_teacher;
		this.userId = userId;
		this.teacherId = teacherId;
	}


	@Override
	public String toString() {
		return "EnquirytableBoardDo [num=" + num + ", lessonId=" + lessonId + ", title=" + title + ", title_teacher="
				+ title_teacher + ", content=" + content + ", content_teacher=" + content_teacher + ", userId=" + userId
				+ ", teacherId=" + teacherId + "]";
	}


	



    
    
    
}
