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

    public void saveLessonRequest(LessonrequestsDo request) {
        lessonrequestsDao.addLessonRequest(request);
    }

    public void updateLessonRequestStatus(int requestId, LessonrequestsDo.RequestsStatus status) {
        lessonrequestsDao.updateRequestStatus(requestId, status);
    }

    public List<LessonrequestsDo> getUserLessonRequests(String userId) {
        return lessonrequestsDao.getUserLessonRequests(userId);
    }

    public List<LessonrequestsDo> getTeacherLessonRequests(String teacherId) {
        return lessonrequestsDao.getTeacherLessonRequests(teacherId);
    }
}
