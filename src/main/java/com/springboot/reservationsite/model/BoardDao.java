package com.springboot.reservationsite.model;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository("boardDao")
public class BoardDao {

	private final JdbcTemplate jdbcTemplate;

	public BoardDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// 회원가입 메소드
	public void joinBoard(UserBoardDo udo) {
		System.out.println("joinBoard() start");

		String sql = "INSERT INTO usertable (id, pwd, name, lesson, level, role) VALUES (?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, udo.getId(), udo.getPwd(), udo.getName(), udo.getLesson(), udo.getLevel(),
				udo.getRole().name() // Enum을 문자열로 변환
		);
	}

	// 로그인 메소드
	public UserBoardDo loginBoard(String id, String pwd) {
		System.out.println("loginBoard() start");

		String sql = "SELECT id, pwd, name, lesson, level, role FROM usertable WHERE id = ? AND pwd = ?";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { id, pwd }, new UserRowMapper());
		} catch (EmptyResultDataAccessException e) {
			System.out.println("Login Fail: No user found with id=" + id);
			return null;
		} catch (DataAccessException e) {
			System.err.println("Database Error: " + e.getMessage());
			throw e;
		}
	}

	// 수업 추가 메소드
	public int addLessonBoard(LessonDo ldo) {
		System.out.println("addLessonBoard() start");

		String sql = "INSERT INTO lessontable (photo, title, description, time, people, teacherId) VALUES (?, ?, ?, ?, ?, ?)";
		String generatedKeyQuery = "SELECT LAST_INSERT_ID()"; // MySQL에서 자동 생성된 PK(lessonId)) 가져오기

		try {
			// 데이터 삽입
			jdbcTemplate.update(sql, ldo.getPhoto() != null ? ldo.getPhoto() : new byte[0], // NULL 방지
					ldo.getTitle(), ldo.getDescription() != null ? ldo.getDescription() : "", // NULL 방지
					ldo.getTime().name(), ldo.getPeople() != null ? ldo.getPeople() : 0, // NULL 방지
					ldo.getTeacherId() // 강사 ID
			);

			System.out.println("addLessonBoard() - 수업 추가 완료");

			// 생성된 PK(lessonId) 가져오기
			int lessonId = jdbcTemplate.queryForObject(generatedKeyQuery, Integer.class);
			System.out.println("Generated lessonId: " + lessonId);

			return lessonId; // 생성된 lessonId 반환
		} catch (Exception e) {
			System.err.println("Failed to add lesson: " + e.getMessage());
			throw e; // 예외를 다시 던짐
		}
	}

	// 사용자가 강의 신청할 때의 정보 DB에 저장
	public void addLessonRequest(LessonrequestsDo lessonRequest) {
	    System.out.println("addLessonRequest() start");

	    String sql = "INSERT INTO lessonrequests (userId, userName, teacherId, lessonName, lessonId, requestsStatus, requestDate, selectedTime) " +
	            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	    try {
	        jdbcTemplate.update(sql,
	            lessonRequest.getUserId(),
	            lessonRequest.getUserName(),
	            lessonRequest.getTeacherId(),
	            lessonRequest.getLessonName(),
	            lessonRequest.getLessonId(),
	            lessonRequest.getRequestsStatus().name(),
	            Timestamp.valueOf(lessonRequest.getRequestDate()),
	            lessonRequest.getSelectedTime()
	        );
	        System.out.println("✅ addLessonRequest() - 요청 추가 완료");
	    } catch (Exception e) {
	        System.err.println("❌ Failed to add lesson request: " + e.getMessage());
	        e.printStackTrace();
	        throw e;
	    }
	}

	// 특정 lessonID 존재 여부 확인
	public boolean checkLessonExists(int lessonId) {
		String sql = "SELECT COUNT(*) FROM lessontable WHERE num = ?";
		Integer count = jdbcTemplate.queryForObject(sql, new Object[] { lessonId }, Integer.class);
		return count != null && count > 0;
	}

	// 강사 ID로 수업 목록 조회 메서드
	public List<LessonDo> getLessonListByTeacherId(String teacherId) {
		System.out.println("getLessonListByTeacherId() start");
		System.out.println("Fetching lessons for teacherId: " + teacherId);

		String sql = "SELECT num, photo, title, description, time, people, teacherId FROM lessontable WHERE teacherId = ?";

		try {
			List<LessonDo> lessonList = jdbcTemplate.query(sql, new Object[] { teacherId }, new RowMapper<LessonDo>() {
				@Override
				public LessonDo mapRow(ResultSet rs, int rowNum) throws SQLException {
					LessonDo lesson = new LessonDo();
					lesson.setNum(rs.getInt("num"));
					lesson.setPhoto(rs.getBytes("photo"));
					lesson.setTitle(rs.getString("title"));
					lesson.setDescription(rs.getString("description"));
					lesson.setTime(LessonDo.TimeType.valueOf(rs.getString("time")));
					lesson.setPeople(rs.getObject("people") != null ? rs.getInt("people") : null); // null 처리
					lesson.setTeacherId(rs.getString("teacherId"));
					return lesson;
				}
			});

			// 조회된 결과 로그로 출력
			System.out.println("Fetched lessons: " + lessonList);

			return lessonList;
		} catch (Exception e) {
			System.err.println("Error fetching lessons by teacherId: " + e.getMessage());
			e.printStackTrace();
			throw e; // 예외를 다시 던져 호출한 곳에서 처리할 수 있도록 설정
		}
	}

	// 강의 삭제
	public void deleteLesson(int lessonId) {
		System.out.println("deleteLesson() start");

		String sql = "DELETE FROM lessontable WHERE num = ?";
		try {
			jdbcTemplate.update(sql, lessonId);
			System.out.println("deleteLesson() - 수업 삭제 완료");
		} catch (Exception e) {
			System.err.println("Failed to delete lesson: " + e.getMessage());
			throw e; // 예외를 다시 던짐
		}
	}

	
	// 강의 신청 기록 삭제
	public void deleteLessonRequestsByLessonId(int lessonId) {
		String sql = "DELETE FROM lessonrequests WHERE lessonId = ?";
		jdbcTemplate.update(sql, lessonId);
	}
	
	
	public List<LessonrequestsDo> getLessonRequestsByTeacherId(String teacherId) {
	    String sql = "SELECT lessonId, lessonName, userId, userName, requestsStatus, requestDate, selectedTime " +
	                 "FROM lessonrequests WHERE teacherId = ?";

	    System.out.println("📋 실행할 SQL: " + sql);
	    System.out.println("📋 teacherId 값: " + teacherId);

	    try {
	        List<LessonrequestsDo> results = jdbcTemplate.query(sql, new Object[]{teacherId}, new RowMapper<LessonrequestsDo>() {
	            @Override
	            public LessonrequestsDo mapRow(ResultSet rs, int rowNum) throws SQLException {
	                LessonrequestsDo request = new LessonrequestsDo();
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

	        System.out.println("📋 조회된 수강 요청 목록: " + results);
	        return results;
	    } catch (Exception e) {
	        System.err.println("❌ SQL 실행 중 오류 발생: " + e.getMessage());
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}





	

	public List<LessonDo> getBoardList() {
	    System.out.println("getBoardList() 실행");

	    String sql = "SELECT num, title, teacherId, description FROM lessontable"; // 🔥 num 추가 확인
	    return jdbcTemplate.query(sql, (rs, rowNum) -> {
	        LessonDo lesson = new LessonDo();
	        lesson.setNum(rs.getInt("num"));  // 🔥 반드시 추가
	        lesson.setTitle(rs.getString("title"));
	        lesson.setTeacherId(rs.getString("teacherId"));
	        lesson.setDescription(rs.getString("description"));
	        return lesson;
	    });
	}


}

class UserRowMapper implements RowMapper<UserBoardDo> {

	@Override
	public UserBoardDo mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserBoardDo udo = new UserBoardDo();
		udo.setId(rs.getString("id"));
		udo.setPwd(rs.getString("pwd"));
		udo.setName(rs.getString("name"));
		udo.setLesson(rs.getString("lesson"));
		udo.setLevel(rs.getInt("level"));

		String role = rs.getString("role");
		if (role != null) {
			udo.setRole(UserBoardDo.Role.valueOf(role));
		}

		return udo;
	}
}

class LessonRowMapper implements RowMapper<LessonDo> {
	@Override
	public LessonDo mapRow(ResultSet rs, int rowNum) throws SQLException {
		LessonDo lesson = new LessonDo();
		lesson.setNum(rs.getInt("num"));
		lesson.setTitle(rs.getString("title"));
		lesson.setDescription(rs.getString("description"));
		lesson.setTeacherId(rs.getString("teacherId"));
		return lesson;
	}
}
