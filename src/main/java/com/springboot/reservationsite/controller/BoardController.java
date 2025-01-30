package com.springboot.reservationsite.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.springboot.reservationsite.model.BoardDao;
import com.springboot.reservationsite.model.LessonDo;
import com.springboot.reservationsite.model.LessonrequestsDo;
import com.springboot.reservationsite.model.UserBoardDo;

import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController {

    @Autowired
    private BoardDao boardDao;

    // 강의 목록 가져와서 mainBoard 페이지에 전달
    @RequestMapping(value = "/mainBoard.do")
    public String mainBoard(Model model) {
        System.out.println("mainBoard()");

        List<LessonDo> lessonList = boardDao.getBoardList();
        model.addAttribute("lessonList", lessonList);
        
        return "mainBoard";
    }

    // 관리자 메인 페이지 이동
    @RequestMapping(value = "/adminmainBoard.do")
    public String adminmainBoard() {
        System.out.println("adminmainBoard()");
        return "adminmainBoard";
    }

    // 강사의 ID를 통해서 자신만의 강의 목록 조회
    @RequestMapping(value = "/teachermainBoard.do")
    public String teachermainBoard(HttpSession session, Model model) {
        try {
            String teacherId = (String) session.getAttribute("loginId");
            if (teacherId == null) {
                return "redirect:/loginBoard.do"; // 로그인 페이지로 리다이렉트
            }

            // DAO를 통해 강사 ID로 수업 목록 가져오기
            List<LessonDo> lessonList = boardDao.getLessonListByTeacherId(teacherId);
            model.addAttribute("lessonList", lessonList);

            return "teachermainBoard";
        } catch (Exception e) {
            System.err.println("Error in teachermainBoard: " + e.getMessage());
            model.addAttribute("error", "페이지를 불러오는 중 문제가 발생했습니다.");
            return "errorPage";
        }
    }

 //
    @RequestMapping(value = "/teacherMain.do")
    public String teacherMain(HttpSession session, Model model) {
        try {
            // 세션에서 로그인한 강사 ID 가져오기
            String teacherId = (String) session.getAttribute("loginId");
            if (teacherId == null) {
                System.out.println("❌ teacherId가 NULL입니다! 로그인 페이지로 리다이렉트");
                return "redirect:/loginBoard.do";
            }

            System.out.println("✅ teacherMain.do 실행됨! teacherId: " + teacherId);

            // ✅ 강사가 개설한 강의 목록 조회
            List<LessonDo> lessonList = boardDao.getLessonListByTeacherId(teacherId);
            System.out.println("📋 조회된 강의 목록: " + lessonList);

            // 🚀 `getLessonRequestsByTeacherId()` 실행 여부 확인
            System.out.println("🚀 getLessonRequestsByTeacherId() 실행 전 - teacherId: " + teacherId);
            List<LessonrequestsDo> responseList = boardDao.getLessonRequestsByTeacherId(teacherId);
            System.out.println("🚀 getLessonRequestsByTeacherId() 실행 완료");
            System.out.println("📋 조회된 수강 요청 목록: " + responseList);

            // JSP에 데이터 전달
            model.addAttribute("lessonList", lessonList);
            model.addAttribute("responseList", responseList);

            return "teacherMain";
        } catch (Exception e) {
            System.err.println("❌ teacherMain.do 요청 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "페이지를 불러오는 중 문제가 발생했습니다.");
            return "errorPage";
        }
    }





    
    // 회원가입 페이지
    @RequestMapping(value = "/joinBoard.do", method = RequestMethod.GET)
    public String joinBoard() {
        System.out.println("joinBoard()");
        return "joinBoard";
    }

    // 회원정보를 DB에 저장. 메인 페이지로 리다이렉트
    @RequestMapping(value = "/joinBoardProc.do", method = RequestMethod.POST)
    public String joinBoardProc(@ModelAttribute UserBoardDo udo) {
        System.out.println("joinBoardProc()");
        System.out.println("name: " + udo.getName());
        boardDao.joinBoard(udo);
        return "redirect:mainBoard.do";
    }

    // 로그인 페이지
    @RequestMapping(value = "/loginBoard.do")
    public String loginBoard() {
        System.out.println("loginBoard()");
        return "loginBoard";
    }

    // 로그인 검증 후 사용자 역할에 따라서 페이지 이동
    @RequestMapping(value = "/loginBoardProc.do", method = RequestMethod.POST)
    public String loginBoardProc(UserBoardDo udo, HttpSession session, Model model) {
        UserBoardDo udo1 = boardDao.loginBoard(udo.getId(), udo.getPwd());
        if (udo1 != null) {
            System.out.println("loginBoardProc() - Login Success");

            UserBoardDo.Role role = udo1.getRole();
            session.setAttribute("loginName", udo1.getName());
            session.setAttribute("loginId", udo1.getId());
            session.setAttribute("isLoggedIn", true);

            switch (role) {
                case ADMIN:
                    return "redirect:/adminmainBoard.do";
                case TEACHER:
                    return "redirect:/teachermainBoard.do";
                case USER:
                    return "redirect:/mainBoard.do";
                default:
                    model.addAttribute("error", "Unknown Role");
                    return "loginBoard";
            }
        } else {
            System.out.println("loginBoardProc() - Login Failed");
            model.addAttribute("error", "Invalid ID or Password");
            return "loginBoard";
        }
    }

    // 세션 무효화 후 메인 페이지 이동
    @RequestMapping(value = "/logout.do", method = RequestMethod.GET)
    public String logoutBoard(HttpSession session) {
        System.out.println("logoutBoard()");
        session.invalidate();
        return "redirect:/mainBoard.do";
    }

    // 강의 추가 페이지
    @RequestMapping(value = "/addlessonBoard.do")
    public String addlessonBoard() {
        System.out.println("addlessonBoard()");
        return "addlessonBoard";
    }

    // 강의 정보 DB에 저장
    @RequestMapping(value = "/addlessonBoardProc.do", method = RequestMethod.POST)
    public String addlessonBoardProc(
        @RequestParam(value = "photo", required = false) MultipartFile photo,
        @RequestParam(value = "title") String title,
        @RequestParam(value = "description") String description,
        @RequestParam(value = "time") LessonDo.TimeType time,
        @RequestParam(value = "people", defaultValue = "0") Integer people,
        HttpSession session,
        Model model
    ) {
        try {
            // 1. 세션에서 사용자 정보 가져오기
            String userId = (String) session.getAttribute("loginId");
            String userName = (String) session.getAttribute("loginName");

            if (userId == null || userName == null) {
                model.addAttribute("error", "로그인이 필요합니다.");
                return "loginBoard"; // 로그인 페이지로 이동
            }

            // 2. LessonDo 객체 생성 및 설정
            LessonDo ldo = new LessonDo();
            ldo.setTitle(title);
            ldo.setDescription(description);
            ldo.setTime(time);
            ldo.setPeople(people);

            if (photo != null && !photo.isEmpty()) {
                ldo.setPhoto(photo.getBytes());
            }

            ldo.setTeacherId(userId); // 세션에서 가져온 userId를 teacherId로 설정

            // 3. 수업 추가
            int lessonId = boardDao.addLessonBoard(ldo);

            // 4. LessonrequestsDo 객체 생성 및 설정
            LessonrequestsDo lessonRequest = new LessonrequestsDo();
            lessonRequest.setUserId(userId); // 세션에서 가져온 userId 설정
            lessonRequest.setUserName(userName);
            lessonRequest.setTeacherId(userId); // 수업 생성자의 ID 설정
            lessonRequest.setLessonName(title);
            lessonRequest.setLessonId(lessonId);
            lessonRequest.setRequestsStatus(LessonrequestsDo.RequestsStatus.PENDING);

            // 5. 수업 요청 추가
            boardDao.addLessonRequest(lessonRequest);

            System.out.println("Lesson added successfully: " + title);

            return "redirect:/teachermainBoard.do";
        } catch (IOException e) {
            model.addAttribute("error", "사진 업로드에 실패했습니다.");
            return "addlessonBoard";
        } catch (Exception e) {
            model.addAttribute("error", "수업 추가 중 문제가 발생했습니다.");
            return "addlessonBoard";
        }
    }

    // 강의 삭제. 관련 정보도 함께 삭제
    @RequestMapping(value = "/deleteLesson.do", method = RequestMethod.GET)
    public String deleteLesson(@RequestParam(value = "num", required = false, defaultValue = "0") int lessonId, Model model) {
        if (lessonId == 0) {
            model.addAttribute("error", "유효하지 않은 수업 번호입니다.");
            return "teachermainBoard"; // 에러 페이지로 리다이렉트
        }

        try {
            boardDao.deleteLessonRequestsByLessonId(lessonId);
            
            boardDao.deleteLesson(lessonId);

            System.out.println("수업 삭제 완료: " + lessonId);
            return "redirect:/teachermainBoard.do";
        } catch (Exception e) {
            System.out.println("삭제 중 오류 발생: " + e.getMessage());
            model.addAttribute("error", "수업 삭제 중 문제가 발생했습니다.");
            return "teachermainBoard";
        }
    }
    
    // 사용자가 특정 강의에 신청할 때. 정보를 DB에 저장
    @PostMapping("/applyLesson.do")
    @ResponseBody
    public Map<String, Object> applyLesson(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> response = new HashMap<>();
        try {
            // ✅ NULL 값 체크 및 기본값 설정
            if (!requestData.containsKey("lessonId") || requestData.get("lessonId") == null) {
                throw new IllegalArgumentException("❌ lessonId 값이 없습니다.");
            }
            if (!requestData.containsKey("userId") || requestData.get("userId") == null) {
                throw new IllegalArgumentException("❌ userId 값이 없습니다.");
            }
            if (!requestData.containsKey("teacherId") || requestData.get("teacherId") == null) {
                throw new IllegalArgumentException("❌ teacherId 값이 없습니다.");
            }
            if (!requestData.containsKey("lessonTitle") || requestData.get("lessonTitle") == null) {
                requestData.put("lessonTitle", "제목 없음"); // 기본값 설정
            }
            if (!requestData.containsKey("selectedTime") || requestData.get("selectedTime") == null) {
                requestData.put("selectedTime", "기본 시간"); // ✅ 기본 시간 설정
            }

            // ✅ `lessonId`를 안전하게 변환 (예외 방지)
            Object lessonIdObj = requestData.get("lessonId");
            int lessonId;
            if (lessonIdObj instanceof Integer) {
                lessonId = (Integer) lessonIdObj;
            } else {
                lessonId = Integer.parseInt(lessonIdObj.toString());
            }

            String userId = requestData.get("userId").toString();
            String userName = requestData.get("userName").toString();
            String teacherId = requestData.get("teacherId").toString();
            String lessonName = requestData.get("lessonTitle").toString();
            String selectedTime = requestData.get("selectedTime").toString();

            System.out.println("📥 수업 신청 데이터: " + requestData);

            if (!boardDao.checkLessonExists(lessonId)) {
                response.put("success", false);
                response.put("error", "❌ 해당 lessonId가 존재하지 않습니다.");
                return response;
            }

            LessonrequestsDo lessonRequest = new LessonrequestsDo();
            lessonRequest.setUserId(userId);
            lessonRequest.setUserName(userName);
            lessonRequest.setTeacherId(teacherId);
            lessonRequest.setLessonName(lessonName);
            lessonRequest.setLessonId(lessonId);
            lessonRequest.setRequestsStatus(LessonrequestsDo.RequestsStatus.PENDING);
            lessonRequest.setRequestDate(LocalDateTime.now());
            lessonRequest.setSelectedTime(selectedTime);

            boardDao.addLessonRequest(lessonRequest);

            response.put("success", true);
        } catch (NumberFormatException e) {
            response.put("success", false);
            response.put("error", "❌ lessonId가 올바른 숫자가 아닙니다.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            System.err.println("❌ 신청 중 오류 발생: " + e.getMessage());
        }
        return response;
    }















    
    

}
