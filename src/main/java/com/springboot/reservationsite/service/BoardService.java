package com.springboot.reservationsite.service;

import java.util.ArrayList;
import java.util.List;

import com.springboot.reservationsite.model.LessonDo;
import com.springboot.reservationsite.model.UserBoardDo;

public interface BoardService {
	
	void joinBoard(UserBoardDo udo);
	
	void deleteLesson(int lessonId);
	
	List<LessonDo> getBoardList();
	

	
	

}
