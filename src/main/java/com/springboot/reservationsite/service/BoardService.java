package com.springboot.reservationsite.service;

import java.util.List;

import com.springboot.reservationsite.model.EnquirytableBoardDo;
import com.springboot.reservationsite.model.LessonDo;
import com.springboot.reservationsite.model.UserBoardDo;

public interface BoardService {
	
	void joinBoard(UserBoardDo udo);
	
	void deleteLesson(int lessonId);
	
	List<LessonDo> getBoardList();
	
	List<EnquirytableBoardDo> getEnquiriesByTeacherId(String teacherId);
	

	
	

}
