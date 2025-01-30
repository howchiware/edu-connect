package com.springboot.reservationsite.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lessonrequests")
public class LessonrequestsDo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String teacherId;

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
        PENDING, ACCEPTED, REJECTED
    }

    // 기본 생성자
    public LessonrequestsDo() {
        this.requestDate = LocalDateTime.now();
        this.requestsStatus = RequestsStatus.PENDING;
    }

    // 모든 필드를 초기화하는 생성자
    public LessonrequestsDo(int id, String userId, String userName, String teacherId, String lessonName, int lessonId, RequestsStatus requestsStatus, LocalDateTime requestDate, String selectedTime) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.teacherId = teacherId;
        this.lessonName = lessonName;
        this.lessonId = lessonId;
        this.requestsStatus = requestsStatus;
        this.requestDate = requestDate;
        this.selectedTime = selectedTime;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }

    public String getLessonName() { return lessonName; }
    public void setLessonName(String lessonName) { this.lessonName = lessonName; }

    public int getLessonId() { return lessonId; }
    public void setLessonId(int lessonId) { this.lessonId = lessonId; }

    public RequestsStatus getRequestsStatus() { return requestsStatus; }
    public void setRequestsStatus(RequestsStatus requestsStatus) { this.requestsStatus = requestsStatus; }

    public LocalDateTime getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDateTime requestDate) { this.requestDate = requestDate; }

    public String getSelectedTime() { return selectedTime; }
    public void setSelectedTime(String selectedTime) { this.selectedTime = selectedTime; }

    @Override
    public String toString() {
        return "LessonrequestsDo{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", teacherId='" + teacherId + '\'' +
                ", lessonName='" + lessonName + '\'' +
                ", lessonId=" + lessonId +
                ", requestsStatus=" + requestsStatus +
                ", requestDate=" + requestDate +
                ", selectedTime='" + selectedTime + '\'' +
                '}';
    }
}
