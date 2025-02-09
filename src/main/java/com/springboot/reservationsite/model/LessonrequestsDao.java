package com.springboot.reservationsite.model;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository("lessonrequestsDao")
public class LessonrequestsDao {

    private final JdbcTemplate jdbcTemplate;

    public LessonrequestsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 강의 신청 요청 추가
    public void addLessonRequest(LessonrequestsDo lessonRequest) {
        System.out.println("addLessonRequest() start");

        String sql = "INSERT INTO lessonrequests (userId, userName, teacherId, teacherName, lessonName, lessonId, requestsStatus, requestDate, selectedTime) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql,
                lessonRequest.getUserId(),
                lessonRequest.getUserName(),
                lessonRequest.getTeacherId(),
                lessonRequest.getTeacherName(),
                lessonRequest.getLessonName(),
                lessonRequest.getLessonId(),
                lessonRequest.getRequestsStatus().name(),
                Timestamp.valueOf(lessonRequest.getRequestDate()),
                lessonRequest.getSelectedTime()
            );
            
            System.out.println("addLessonRequest() - 요청 추가 완료");
        } catch (Exception e) {
            System.err.println("Failed to add lesson request: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // 예약 상태 변경
    public boolean updateRequestStatus(int num, LessonrequestsDo.RequestsStatus status) {
        String sql = "UPDATE lessonrequests SET requestsStatus = ? WHERE num = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, status.toString(), num);
            return rowsAffected > 0;
        } catch (Exception e) {
            System.err.println("SQL 오류: " + e.getMessage());
            return false;
        }
    }

    // 사용자의 예약 목록 조회 (PENDING, REJECTED)
    public List<LessonrequestsDo> getPendingOrRejectedLessonsByUserId(String userId) {
        String sql = "SELECT * FROM lessonrequests WHERE userId = ? AND requestsStatus IN ('PENDING', 'REJECTED')";

        return jdbcTemplate.query(sql, new Object[]{userId}, new RowMapper<LessonrequestsDo>() {
            @Override
            public LessonrequestsDo mapRow(ResultSet rs, int rowNum) throws SQLException {
                LessonrequestsDo lesson = new LessonrequestsDo();
                lesson.setNum(rs.getInt("num"));
                lesson.setLessonName(rs.getString("lessonName"));
                lesson.setTeacherName(rs.getString("teacherName"));
                lesson.setSelectedTime(rs.getString("selectedTime"));
                lesson.setRequestsStatus(LessonrequestsDo.RequestsStatus.valueOf(rs.getString("requestsStatus")));
                return lesson;
            }
        });
    }

    // 사용자의 승인된 강의 조회 (ACCEPTED)
    public List<LessonrequestsDo> getAcceptedLessonsByUserId(String userId) {
        String sql = "SELECT * FROM lessonrequests WHERE userId = ? AND requestsStatus = 'ACCEPTED'";

        return jdbcTemplate.query(sql, new Object[]{userId}, new RowMapper<LessonrequestsDo>() {
            @Override
            public LessonrequestsDo mapRow(ResultSet rs, int rowNum) throws SQLException {
                LessonrequestsDo lesson = new LessonrequestsDo();
                lesson.setNum(rs.getInt("num"));
                lesson.setLessonName(rs.getString("lessonName"));
                lesson.setTeacherName(rs.getString("teacherName"));
                lesson.setSelectedTime(rs.getString("selectedTime"));
                lesson.setLessonId(rs.getInt("lessonId"));
                return lesson;
            }
        });
    }

    // 특정 강사의 예약 목록 조회
    public List<LessonrequestsDo> getLessonRequestsByTeacherId(String teacherId) {
        String sql = "SELECT num, lessonId, lessonName, userId, userName, requestsStatus, requestDate, selectedTime FROM lessonrequests WHERE teacherId = ?";
        System.out.println("실행할 SQL: " + sql);
        System.out.println("teacherId 값: " + teacherId);

        try {
            return jdbcTemplate.query(sql, new Object[]{teacherId}, new RowMapper<LessonrequestsDo>() {
                @Override
                public LessonrequestsDo mapRow(ResultSet rs, int rowNum) throws SQLException {
                    LessonrequestsDo request = new LessonrequestsDo();
                    request.setNum(rs.getInt("num"));
                    request.setLessonId(rs.getInt("lessonId"));
                    request.setLessonName(rs.getString("lessonName"));
                    request.setUserId(rs.getString("userId"));
                    request.setUserName(rs.getString("userName"));
                    request.setRequestsStatus(LessonrequestsDo.RequestsStatus.valueOf(rs.getString("requestsStatus")));
                    request.setRequestDate(rs.getTimestamp("requestDate").toLocalDateTime());
                    request.setSelectedTime(rs.getString("selectedTime"));
                    return request;
                }
            });
        } catch (Exception e) {
            System.err.println("수강 요청 목록 조회 중 오류 발생: " + e.getMessage());
            return List.of();
        }
    }

    // 강의 신청 취소
    public void cancelLessonRequest(int num) {
        String sql = "UPDATE lessonrequests SET requestsStatus = 'CANCEL' WHERE num = ?";
        jdbcTemplate.update(sql, num);
    }

    // 강의 신청 기록 삭제 (강의 삭제 시 함께 제거)
    public void deleteLessonRequestsByLessonId(int lessonId) {
        String sql = "DELETE FROM lessonrequests WHERE lessonId = ?";
        jdbcTemplate.update(sql, lessonId);
    }
    
    // 특정 유저의 예약 목록 조회
    public List<LessonrequestsDo> getUserLessonRequests(String userId) {
        String sql = "SELECT * FROM lessonrequests WHERE userId = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, new RowMapper<LessonrequestsDo>() {
            @Override
            public LessonrequestsDo mapRow(ResultSet rs, int rowNum) throws SQLException {
                LessonrequestsDo lessonRequest = new LessonrequestsDo();
                lessonRequest.setNum(rs.getInt("num"));
                lessonRequest.setUserId(rs.getString("userId"));
                lessonRequest.setUserName(rs.getString("userName"));
                lessonRequest.setTeacherId(rs.getString("teacherId"));
                lessonRequest.setTeacherName(rs.getString("teacherName"));
                lessonRequest.setLessonName(rs.getString("lessonName"));
                lessonRequest.setLessonId(rs.getInt("lessonId"));
                lessonRequest.setRequestsStatus(LessonrequestsDo.RequestsStatus.valueOf(rs.getString("requestsStatus")));
                lessonRequest.setRequestDate(rs.getTimestamp("requestDate").toLocalDateTime());
                lessonRequest.setSelectedTime(rs.getString("selectedTime"));
                return lessonRequest;
            }
        });
    }
    
    // 특정 강사의 예약 목록 조회
    public List<LessonrequestsDo> getTeacherLessonRequests(String teacherId) {
        String sql = "SELECT * FROM lessonrequests WHERE teacherId = ?";
        return jdbcTemplate.query(sql, new Object[]{teacherId}, new RowMapper<LessonrequestsDo>() {
            @Override
            public LessonrequestsDo mapRow(ResultSet rs, int rowNum) throws SQLException {
                LessonrequestsDo lessonRequest = new LessonrequestsDo();
                lessonRequest.setNum(rs.getInt("num"));
                lessonRequest.setUserId(rs.getString("userId"));
                lessonRequest.setUserName(rs.getString("userName"));
                lessonRequest.setTeacherId(rs.getString("teacherId"));
                lessonRequest.setTeacherName(rs.getString("teacherName"));
                lessonRequest.setLessonName(rs.getString("lessonName"));
                lessonRequest.setLessonId(rs.getInt("lessonId"));
                lessonRequest.setRequestsStatus(LessonrequestsDo.RequestsStatus.valueOf(rs.getString("requestsStatus")));
                lessonRequest.setRequestDate(rs.getTimestamp("requestDate").toLocalDateTime());
                lessonRequest.setSelectedTime(rs.getString("selectedTime"));
                return lessonRequest;
            }
        });
    }


    
}
