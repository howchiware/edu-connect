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
    private String content;
    private String userId;
    private String teacherId;


    public EnquirytableBoardDo() {
    }


    public EnquirytableBoardDo(int num, int lessonId, String title, String content, String userId, String teacherId) {
        this.num = num;
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.teacherId = teacherId;
    }

    // Getter & Setter
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    // toString 메서드
    @Override
    public String toString() {
        return "EnquirytableBoardDo{" +
                "num=" + num +
                ", lessonId=" + lessonId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", userId='" + userId + '\'' +
                ", teacherId='" + teacherId + '\'' +
                '}';
    }
}
