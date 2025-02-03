package com.springboot.reservationsite.service;

import com.springboot.reservationsite.model.LessonrequestsDao;
import com.springboot.reservationsite.model.LessonrequestsDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LessonrequestsService {

    @Autowired
    private LessonrequestsDao lessonrequestsDao;

    // 🔹 1. 예약 요청 추가
    public void saveLessonRequest(LessonrequestsDo request) {
        lessonrequestsDao.addLessonRequest(request);
    }

    // 🔹 2. 예약 상태 변경
    public void updateLessonRequestStatus(int requestId, LessonrequestsDo.RequestsStatus status) {
        lessonrequestsDao.updateRequestStatus(requestId, status);
    }

    // 🔹 3. 특정 사용자의 예약 목록 조회
    public List<LessonrequestsDo> getUserLessonRequests(String userId) {
        return lessonrequestsDao.getUserLessonRequests(userId);
    }

    // 🔹 4. 특정 강사의 예약 목록 조회
    public List<LessonrequestsDo> getTeacherLessonRequests(String teacherId) {
        return lessonrequestsDao.getTeacherLessonRequests(teacherId);
    }
}
