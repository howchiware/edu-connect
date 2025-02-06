package com.springboot.reservationsite.model;

import java.util.Arrays;

import jakarta.persistence.*;

@Entity
@Table(name = "lessontable") 
public class LessonDo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    private int num;

    @Lob
    private byte[] photo;

    @Column(name = "photoPath")
    private String photoPath;    

    @Column(length = 255, nullable = false)
    private String title;
    
    @Column(length = 500)
    private String description;
    
    @Column(length = 200)
    private String time;

    private Integer people;

    // 강사 ID와 연관 설정 (teacherId 필드 추가)
    @Column(name = "teacherId", nullable = false)
    private String teacherId;
    
    @Column(name = "teacherName", nullable = false)
    private String teacherName;
    
    private int lessonId;


	public LessonDo() {
		
	}


	public int getNum() {
		return num;
	}


	public void setNum(int num) {
		this.num = num;
	}


	public byte[] getPhoto() {
		return photo;
	}


	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}


	public String getPhotoPath() {
		return photoPath;
	}


	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public Integer getPeople() {
		return people;
	}


	public void setPeople(Integer people) {
		this.people = people;
	}


	public String getTeacherId() {
		return teacherId;
	}


	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}


	public String getTeacherName() {
		return teacherName;
	}


	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}


	public int getLessonId() {
		return lessonId;
	}


	public void setLessonId(int lessonId) {
		this.lessonId = lessonId;
	}


	@Override
	public String toString() {
		return "LessonDo [num=" + num + ", photo=" + Arrays.toString(photo) + ", photoPath=" + photoPath + ", title="
				+ title + ", description=" + description + ", time=" + time + ", people=" + people + ", teacherId="
				+ teacherId + ", teacherName=" + teacherName + ", lessonId=" + lessonId + "]";
	}


	public LessonDo(int num, byte[] photo, String photoPath, String title, String description, String time,
			Integer people, String teacherId, String teacherName, int lessonId) {
		super();
		this.num = num;
		this.photo = photo;
		this.photoPath = photoPath;
		this.title = title;
		this.description = description;
		this.time = time;
		this.people = people;
		this.teacherId = teacherId;
		this.teacherName = teacherName;
		this.lessonId = lessonId;
	}

}
