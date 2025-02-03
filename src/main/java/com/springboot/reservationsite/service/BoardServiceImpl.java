package com.springboot.reservationsite.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.reservationsite.model.BoardDao;
import com.springboot.reservationsite.model.EnquirytableBoardDo;
import com.springboot.reservationsite.model.LessonDo;
import com.springboot.reservationsite.model.UserBoardDo;



@Service("boardService")
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	private BoardDao bdao;
	
	@Override
	public void joinBoard(UserBoardDo udo) {
		bdao.joinBoard(udo);
	}
	
	@Override
	public void deleteLesson(int lessonId) {
		bdao.deleteLesson(lessonId);
	}
	
	@Override
	public List<LessonDo> getBoardList() {
		return bdao.getBoardList();
	}
	
	@Override
	public List<EnquirytableBoardDo> getEnquiriesByTeacherId(String teacherId) {
	    return bdao.getEnquiriesByTeacherId(teacherId);
	}


}
