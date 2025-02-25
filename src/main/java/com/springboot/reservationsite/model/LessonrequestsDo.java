package com.springboot.reservationsite.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lessonrequests")
public class LessonrequestsDo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String teacherId;
    
    @Column(nullable = false)
    private String teacherName;

    @Column(nullable = false)
    private String lessonName;

    @Column(nullable = false)
    private int lessonId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestsStatus requestsStatus;

    @Column(nullable = false)
    private LocalDateTime requestDate;

    @Column(length = 10, nullable = false) // 추가된 필드
    private String selectedTime; 

    public enum RequestsStatus {
        PENDING, ACCEPTED, REJECTED, CANCEL
    }

    // 기본 생성자
    public LessonrequestsDo() {
        this.requestDate = LocalDateTime.now();
        this.requestsStatus = RequestsStatus.PENDING;
    }

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getLessonName() {
		return lessonName;
	}

	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}

	public int getLessonId() {
		return lessonId;
	}

	public void setLessonId(int lessonId) {
		this.lessonId = lessonId;
	}

	public RequestsStatus getRequestsStatus() {
		return requestsStatus;
	}

	public void setRequestsStatus(RequestsStatus requestsStatus) {
		this.requestsStatus = requestsStatus;
	}

	public LocalDateTime getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(LocalDateTime requestDate) {
		this.requestDate = requestDate;
	}

	public String getSelectedTime() {
		return selectedTime;
	}

	public void setSelectedTime(String selectedTime) {
		this.selectedTime = selectedTime;
	}

	@Override
	public String toString() {
		return "LessonrequestsDo [num=" + num + ", userId=" + userId + ", userName=" + userName + ", teacherId="
				+ teacherId + ", teacherName=" + teacherName + ", lessonName=" + lessonName + ", lessonId=" + lessonId
				+ ", requestsStatus=" + requestsStatus + ", requestDate=" + requestDate + ", selectedTime="
				+ selectedTime + "]";
	}

	public LessonrequestsDo(int num, String userId, String userName, String teacherId, String teacherName,
			String lessonName, int lessonId, RequestsStatus requestsStatus, LocalDateTime requestDate,
			String selectedTime) {
		super();
		this.num = num;
		this.userId = userId;
		this.userName = userName;
		this.teacherId = teacherId;
		this.teacherName = teacherName;
		this.lessonName = lessonName;
		this.lessonId = lessonId;
		this.requestsStatus = requestsStatus;
		this.requestDate = requestDate;
		this.selectedTime = selectedTime;
	}

	

   
    
    
    
    
}
