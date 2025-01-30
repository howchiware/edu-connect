package com.springboot.reservationsite.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lessontable") 
public class LessonDo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    private int num;

    @Lob
    private byte[] photo; // BLOB 필드 매핑

    @Column(length = 50, nullable = false)
    private String title;
    
    @Column(length = 200)
    private String description;
    
    @Enumerated(EnumType.STRING) // ENUM 필드 매핑
    @Column(nullable = false)
    private TimeType time;

    private Integer people;

    // 강사 ID와 연관 설정 (teacherId 필드 추가)
    @Column(name = "teacherId", nullable = false)
    private String teacherId;

    // ENUM 정의
    public enum TimeType {
        A, B, C
    }

    // Getter, Setter 추가

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

    public TimeType getTime() {
        return time;
    }

    public void setTime(TimeType time) {
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

    @Override
    public String toString() {
        return "LessonDo{" +
                "num=" + num +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", time=" + time +
                ", people=" + people +
                ", teacherId='" + teacherId + '\'' +
                '}';
    }

}
