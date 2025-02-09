package com.springboot.reservationsite.model;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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
				udo.getRole().name()
		);
	}

	// 로그인 메소드
	public UserBoardDo loginBoard(String id, String pwd) {
		System.out.println("loginBoard() start");

		String sql = "SELECT * FROM usertable WHERE id = ? AND pwd = ?";
		
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { id, pwd }, new BeanPropertyRowMapper<>(UserBoardDo.class));
		
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

		String sql = "INSERT INTO lessontable (photo, photoPath, title, description, time, people, teacherId, teacherName, lessonId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		String generatedKeyQuery = "SELECT LAST_INSERT_ID()";

		try {
			jdbcTemplate.update(sql, 
					ldo.getPhoto() != null ? ldo.getPhoto() : new byte[0],  
					ldo.getPhotoPath(),
					ldo.getTitle(), 
					ldo.getDescription() != null ? ldo.getDescription() : "", 
					ldo.getTime(), 
					ldo.getPeople() != null ? ldo.getPeople() : 0, 
					ldo.getTeacherId(), 
					ldo.getTeacherName(), 
					ldo.getLessonId()
					
			);
			
			System.out.println("addLessonBoard() - 수업 추가 완료");

			int lessonId = jdbcTemplate.queryForObject(generatedKeyQuery, Integer.class);
			System.out.println("Generated lessonId: " + lessonId);

			return lessonId;
		} catch (Exception e) {
			System.err.println("Failed to add lesson: " + e.getMessage());
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

		String sql = "SELECT num, photo, photoPath, title, description, time, people, teacherId, teacherName, lessonId FROM lessontable WHERE teacherId = ?";

		try {
			List<LessonDo> lessonList = jdbcTemplate.query(sql, new Object[] { teacherId }, new RowMapper<LessonDo>() {
				@Override
				public LessonDo mapRow(ResultSet rs, int rowNum) throws SQLException {
					LessonDo lesson = new LessonDo();
					lesson.setNum(rs.getInt("num"));
					lesson.setPhoto(rs.getBytes("photo"));
					lesson.setTitle(rs.getString("title"));
					lesson.setDescription(rs.getString("description"));
					lesson.setTime(rs.getString("time"));
					lesson.setPeople(rs.getObject("people") != null ? rs.getInt("people") : null); // null 처리
					lesson.setTeacherId(rs.getString("teacherId"));
					lesson.setTeacherName(rs.getString("teacherName"));
					
	                try {
	                    int people = rs.getInt("people");
	                    lesson.setPeople(people);
	                } catch (SQLException e) {
	                    lesson.setPeople(null);
	                }
	                
	                lesson.setTeacherId(rs.getString("teacherId"));
	                lesson.setTeacherName(rs.getString("teacherName"));
	                return lesson;
				}
			});

			// 조회된 결과 로그로 출력
			System.out.println("Fetched lessons: " + lessonList);

			return lessonList;
		} catch (Exception e) {
			System.err.println("Error fetching lessons by teacherId: " + e.getMessage());
			e.printStackTrace();
			throw e;
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
			throw e;
		}
	}
	
	// 수업 정보 조회
	public LessonDo getLessonById(int num) {
	    System.out.println("🔍 Fetching lesson from DB (lessontable), num: " + num);
	    String sql = "SELECT num, title, teacherId, teacherName, description, lessonId FROM lessontable WHERE num = ?";

	    try {
	        return jdbcTemplate.queryForObject(sql, new Object[]{num}, new RowMapper<LessonDo>() {
	            @Override
	            public LessonDo mapRow(ResultSet rs, int rowNum) throws SQLException {
	                LessonDo lesson = new LessonDo();
	                lesson.setNum(rs.getInt("num"));
	                lesson.setTitle(rs.getString("title"));
	                lesson.setTeacherId(rs.getString("teacherId"));
	                lesson.setTeacherName(rs.getString("teacherName"));
	                lesson.setDescription(rs.getString("description"));

	                System.out.println("Lesson retrieved: " + lesson.getTitle());
	                return lesson;
	            }
	        });
	    } catch (EmptyResultDataAccessException e) {
	        System.out.println("[DB 조회 실패] lessontable에서 num = " + num + "인 강의를 찾을 수 없습니다.");
	        return null;
	    }
	}
	
	// 사용자 id 얻기
	public UserBoardDo getUserById(String id) {
	    String sql = "SELECT * FROM usertable WHERE id = ?";
	    try {
	        return jdbcTemplate.queryForObject(sql, new Object[]{id},
	                new BeanPropertyRowMapper<>(UserBoardDo.class));
	    } catch (EmptyResultDataAccessException e) {
	        return null;
	    }
	}

	// 사용자 정보 수정
	public void usermodifyBoard(UserBoardDo udo) {
		System.out.println("usermodifyBoard() start");
		
		String sql = "update usertable set pwd=?, name=? where num=?";
		
		jdbcTemplate.update(sql, udo.getPwd(), udo.getName(), udo.getNum());
	}
	
	// 강사 메시지 전달
	public void teachermessageBoard(EnquirytableBoardDo edo) {
		System.out.println("teachermessageBoard");
		
		String sql = "insert into enquirytable (title_teacher, content_teacher, userId, teacherId, lessonId) values (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, edo.getTitle_teacher(), edo.getContent_teacher(), edo.getUserId(), edo.getTeacherId(), edo.getLessonId());
	}

	// 수강생 메시지 전달
	public void insertenquiryBoard(EnquirytableBoardDo edo) {
		System.out.println("insertenquiryBoard");
		
		String sql = "INSERT INTO enquirytable (title, content, userId, teacherId, lessonId) VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, edo.getTitle(), edo.getContent(), edo.getUserId(), edo.getTeacherId(), edo.getLessonId());

	}

	// 특정 강사의 teacherId에 해당하는 모든 문의사항 조회
	public List<EnquirytableBoardDo> getEnquiriesByTeacherId(String teacherId) {
	    String sql = "SELECT * FROM enquirytable WHERE teacherId = ?";
	    return jdbcTemplate.query(sql, new Object[] { teacherId }, new BeanPropertyRowMapper<>(EnquirytableBoardDo.class));
	}
	
	// 특정 사용자의 userId에 해당하는 모든 문의사항 조회
	public List<EnquirytableBoardDo> getEnquiriesByUserId(String userId) {
	    String sql = "SELECT * FROM enquirytable WHERE userId = ?";
	    return jdbcTemplate.query(sql, new Object[] { userId }, new BeanPropertyRowMapper<>(EnquirytableBoardDo.class));
	}
	
	// 특정 문의사항 조회. enquirytable에서 조회
	public EnquirytableBoardDo getEnquiryByNum(int num) {
	    String sql = "SELECT * FROM enquirytable WHERE num = ?";
	    
	    try {
	        return jdbcTemplate.queryForObject(sql, new Object[]{num}, new BeanPropertyRowMapper<>(EnquirytableBoardDo.class));
	    } catch (EmptyResultDataAccessException e) {
	        System.out.println("[DB 조회 실패] enquirytable에서 num = " + num + "인 문의를 찾을 수 없습니다.");
	        return null;
	    }
	}

	// 수업 수정하는 메소드
	public void lessonmodifyBoard(LessonDo ldo) {
	    System.out.println("lessonmodifyBoard()");

	    String sql = "UPDATE lessontable SET photo=?, photoPath=?, title=?, description=?, time=?, people=? WHERE num=?";
	    System.out.println("SQL Query: " + sql);
	    System.out.println("Parameters: " + ldo.getPhoto() + ", " + ldo.getPhotoPath() + ", " + ldo.getTitle() + ", " + ldo.getDescription() + ", " + ldo.getTime() + ", " + ldo.getPeople() + ", " + ldo.getNum());
	    
	    int rowsAffected = jdbcTemplate.update(sql, ldo.getPhoto(), ldo.getPhotoPath(), ldo.getTitle(), ldo.getDescription(), ldo.getTime(), ldo.getPeople(), ldo.getNum());
	    
	    System.out.println("Rows Affected: " + rowsAffected);
	}
	
	// 수업의 사진과 경로 조회
	public LessonDo getLessonByNum(int num) {
	    String sql = "SELECT photo, photoPath FROM lessontable WHERE num = ?";
	    return jdbcTemplate.queryForObject(sql, new Object[]{num}, (rs, rowNum) -> {
	        LessonDo lesson = new LessonDo();
	        lesson.setPhoto(rs.getBytes("photo"));
	        lesson.setPhotoPath(rs.getString("photoPath"));
	        return lesson;
	    });
	}

	// 전체 레코드
	public List<LessonDo> getBoardList() {
	    System.out.println("getBoardList()");

	    String sql = "SELECT num, photoPath, title, description, time, people, teacherId, teacherName, lessonId FROM lessontable"; // 🔥 num 추가 확인
	    return jdbcTemplate.query(sql, (rs, rowNum) -> {
	        LessonDo lesson = new LessonDo();
	        lesson.setNum(rs.getInt("num"));
	        lesson.setPhotoPath(rs.getString("photoPath"));
	        lesson.setTitle(rs.getString("title"));
	        lesson.setDescription(rs.getString("description"));
	        lesson.setTime(rs.getString("time"));
	        lesson.setPeople(rs.getInt("people"));
	        lesson.setTeacherId(rs.getString("teacherId"));
	        lesson.setTeacherName(rs.getString("teacherName"));
	        lesson.setLessonId(rs.getInt("lessonId"));

	        return lesson;
	    });
	}
	
	// 한 개의 사용자 레코드 가져오는 메소드
	public UserBoardDo getUserBoard(UserBoardDo temp) {
		System.out.println("getUserBoard()");
		
		String sql = "select * from usertable where num=?";
		
		Object[] args = {temp.getNum()};
		return jdbcTemplate.queryForObject(sql, args, new UserRowMapper() );
	}
	
	/*
	// 한 개의 수업 레코드 가져오는 메소드
	public LessonDo getBoard(LessonDo temp) {
		System.out.println("getBoard()");

		String sql = "select * from lessontable where num=?";
		
		Object[] args = {temp.getNum()};
		return jdbcTemplate.queryForObject(sql, args, new LessonRowMapper() );
		

	}
	*/
	
	// 특정 수업 정보 조회
	public LessonDo getBoard(LessonDo temp) {
	    System.out.println("getBoard()");

	    String sql = "SELECT * FROM lessontable WHERE num=?";
	    
	    Object[] args = {temp.getNum()};
	    
	    System.out.println("Fetching lesson with num: " + temp.getNum());

	    return jdbcTemplate.queryForObject(sql, args, new LessonRowMapper());
	}

	



}

class UserRowMapper implements RowMapper<UserBoardDo> {
	@Override
	public UserBoardDo mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserBoardDo udo = new UserBoardDo();
		udo.setNum(rs.getInt("num"));
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
		//photo type 확인하고 추가하기
		lesson.setPhotoPath(rs.getString("photoPath"));
		lesson.setTitle(rs.getString("title"));
		lesson.setDescription(rs.getString("description"));
		lesson.setTime(rs.getString("time"));
		lesson.setPeople(rs.getInt("people"));
		lesson.setTeacherId(rs.getString("teacherId"));
		lesson.setTeacherName(rs.getString("teacherName"));
		lesson.setLessonId(rs.getInt("lessonId"));
		return lesson;
	}
}

class EnquiryRowMapper implements RowMapper<EnquirytableBoardDo> {
	@Override
	public EnquirytableBoardDo mapRow(ResultSet rs, int rowNum) throws SQLException {
		EnquirytableBoardDo enquiry = new EnquirytableBoardDo();
		enquiry.setNum(rs.getInt("num"));
		enquiry.setLessonId(rs.getInt("lessonId"));
		enquiry.setTitle(rs.getString("title"));
		enquiry.setTitle_teacher(rs.getString("title_teacher"));
		enquiry.setContent(rs.getString("content"));
		enquiry.setContent_teacher(rs.getString("content_teacher"));
		enquiry.setUserId(rs.getString("userId"));
		enquiry.setTeacherId(rs.getString("teacherId"));
		
		return enquiry;
	}
}