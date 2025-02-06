package com.springboot.reservationsite.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.springboot.reservationsite.model.BoardDao;
import com.springboot.reservationsite.model.EnquirytableBoardDo;
import com.springboot.reservationsite.model.LessonDo;
import com.springboot.reservationsite.model.LessonrequestsDao;
import com.springboot.reservationsite.model.LessonrequestsDo;
import com.springboot.reservationsite.model.UserBoardDo;

import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController {

    @Autowired
    private BoardDao boardDao;
    
    @Autowired
    private LessonrequestsDao lessonrequestsDao;

    // 강의 목록 가져와서 mainBoard 페이지에 전달
    @RequestMapping(value = "/mainBoard.do")
    public String mainBoard(HttpSession session, Model model) {
        System.out.println("mainBoard()");

        UserBoardDo loginUser = (UserBoardDo) session.getAttribute("loginUser");

        if (loginUser != null) {
            System.out.println("User in session at mainBoard: " + loginUser);
        } else {
            System.out.println("No user found in session!");
        }

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
    
    @RequestMapping(value = "/usermainBoard.do")
    public String usermainBoard(HttpSession session, Model model) {
        try {
            String userId = (String) session.getAttribute("loginId");
            if (userId == null) {
                return "redirect:/mainBoard.do";
            }

            // 수락된 강의 목록
            List<LessonrequestsDo> enrolledLessons = lessonrequestsDao.getAcceptedLessonsByUserId(userId);
            model.addAttribute("enrolledLessons", enrolledLessons);

            // 대기중(Pending) 및 거절된(Rejected) 강의 목록
            List<LessonrequestsDo> pendingAndRejectedLessons = lessonrequestsDao.getPendingOrRejectedLessonsByUserId(userId);
            model.addAttribute("pendingAndRejectedLessons", pendingAndRejectedLessons);
            
            // 사용자가 작성한 문의사항에 대한 정보
            List<EnquirytableBoardDo> enquiryList = boardDao.getEnquiriesByTeacherId(userId);
            model.addAttribute("enquiryList", enquiryList);
            
            return "usermainBoard";
        } catch (Exception e) {
            System.err.println("Error in usermainBoard: " + e.getMessage());
            model.addAttribute("error", "페이지를 불러오는 중 문제가 발생했습니다.");
            return "errorPage";
        }
    }

    // 강사의 ID를 통해서 자신만의 강의 목록 조회
    @RequestMapping(value = "/teachermainBoard.do")
    public String teachermainBoard(HttpSession session, Model model) {
        try {
            String teacherId = (String) session.getAttribute("loginId");
            if (teacherId == null) {
                return "redirect:/mainBoard.do"; // 로그인 페이지로 리다이렉트
            }

            // 강사가 개설한 수업 목록 조회
            List<LessonDo> lessonList = boardDao.getLessonListByTeacherId(teacherId);
            model.addAttribute("lessonList", lessonList);

            // 강사의 강의에 신청한 학생 목록 조회
            List<LessonrequestsDo> responseList = lessonrequestsDao.getLessonRequestsByTeacherId(teacherId);
            model.addAttribute("responseList", responseList);
            
            // 강사가 담당하는 수업에 대한 문의사항 목록 조회
            List<EnquirytableBoardDo> enquiryList = boardDao.getEnquiriesByTeacherId(teacherId);
            model.addAttribute("enquiryList", enquiryList);
            System.out.println("Enquiry List: " + enquiryList); //확인용 로그


            return "teachermainBoard";
        } catch (Exception e) {
            System.err.println("Error in teachermainBoard: " + e.getMessage());
            model.addAttribute("error", "페이지를 불러오는 중 문제가 발생했습니다.");
            return "errorPage";
        }
    }
    
    

    

    // 수정 보완 필요함
    @RequestMapping(value = "/teacherMain.do")
    public String teacherMain(HttpSession session, Model model) {
        try {
            String teacherId = (String) session.getAttribute("loginId");

            if (teacherId == null) {
                System.out.println("❌ teacherId가 NULL입니다! 로그인 필요.");
                return "redirect:/loginBoard.do";
            }

            System.out.println("✅ teacherMain.do 실행됨! teacherId: " + teacherId);

            // 강사가 개설한 수업 목록 조회
            List<LessonDo> lessonList = boardDao.getLessonListByTeacherId(teacherId);
            System.out.println("조회된 강의 목록: " + lessonList);

            System.out.println("getLessonRequestsByTeacherId() 실행 전 - teacherId: " + teacherId);
            
            List<LessonrequestsDo> responseList = lessonrequestsDao.getLessonRequestsByTeacherId(teacherId);
            
            System.out.println("getLessonRequestsByTeacherId() 실행 완료");

            if (responseList == null) {
                System.out.println("responseList NULL!");
            } else if (responseList.isEmpty()) {
                System.out.println("조회된 수강 요청 목록이 없습니다!");
            } else {
                System.out.println("조회된 수강 요청 목록:");
                for (LessonrequestsDo request : responseList) {
                    System.out.println("요청 ID: " + request.getUserId() +
                            " | 수업명: " + request.getLessonName() +
                            " | 신청자: " + request.getUserName() + " (" + request.getUserId() + ")" +
                            " | 선택 시간: " + request.getSelectedTime() +
                            " | 상태: " + request.getRequestsStatus());
                }
            }

            // 데이터를 JSP로 전달
            model.addAttribute("lessonList", lessonList);
            model.addAttribute("responseList", responseList);

            return "teacherMain";
        } catch (Exception e) {
            System.err.println("teacherMain.do 요청 처리 중 오류 발생: " + e.getMessage());
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

    @RequestMapping(value = "/loginBoardProc.do", method = RequestMethod.POST)
    public String loginBoardProc(UserBoardDo udo, HttpSession session, Model model) {
        UserBoardDo udo1 = boardDao.loginBoard(udo.getId(), udo.getPwd());
        
        if (udo1 != null) {
            System.out.println("loginBoardProc() - Login Success");
            
            model.addAttribute("udo1", udo1);
            
            session.setAttribute("loginUser", udo1);
            
            session.setAttribute("loginName", udo1.getName());
            session.setAttribute("loginId", udo1.getId());
            session.setAttribute("isLoggedIn", true);
            
            UserBoardDo sessionUser = (UserBoardDo) session.getAttribute("loginUser");
            if (sessionUser != null) {
                System.out.println("User stored in session: " + sessionUser);
                System.out.println("User num from session: " + sessionUser.getNum());  // num 값 확인
            } else {
                System.out.println("Session does not contain loginUser.");
            }
            
            UserBoardDo.Role role = udo1.getRole();
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
    
    // 로그아웃
    @RequestMapping(value = "/logout.do", method = RequestMethod.GET)
    public String logoutBoard(HttpSession session) {
        System.out.println("logoutBoard()");
        session.invalidate();
        return "redirect:/mainBoard.do";
    }

   
    private static final String UPLOAD_DIR = "C:/haeun_java_workspace/spring/workspace/reservationsite/src/main/resources/static/images";

    @RequestMapping(value = "/addlessonBoard.do")
    public String addlessonBoard() {
        System.out.println("addlessonBoard()");
        return "addlessonBoard";
    }

    @RequestMapping(value = "/addlessonBoardProc.do", method = RequestMethod.POST)
    public String addlessonBoardProc(
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "time") String time,
            @RequestParam(value = "people", defaultValue = "0") Integer people,
            HttpSession session,
            Model model
    ) {
        try {
            String userId = (String) session.getAttribute("loginId");
            String userName = (String) session.getAttribute("loginName");

            if (userId == null || userName == null) {
                model.addAttribute("error", "로그인이 필요합니다.");
                return "loginBoard";
            }

            // 수업 등록 로직
            LessonDo ldo = new LessonDo();
            ldo.setTitle(title);
            ldo.setDescription(description);
            ldo.setTime(time);
            ldo.setPeople(people);
            ldo.setTeacherId(userId);
            ldo.setTeacherName(userName);

            // ✅ 사진 업로드 처리
            if (photo != null && !photo.isEmpty()) {
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs(); // 폴더 생성
                    System.out.println("📂 업로드 폴더 생성: " + UPLOAD_DIR);
                }

                // 파일명 생성 (UUID + 원래 파일명)
                String fileName = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
                File saveFile = new File(UPLOAD_DIR + File.separator + fileName);

                // 파일 저장
                photo.transferTo(saveFile);

                // DB에 저장할 웹 경로 설정
                ldo.setPhotoPath(fileName);
                System.out.println("✅ 사진 저장 완료: " + ldo.getPhotoPath());
            } else {
                // 사진이 없을 경우 기본 이미지 경로 설정
                ldo.setPhotoPath("/images/lesson_1.jpg");
                System.out.println("⚠️ 사진이 없어 기본 이미지 사용");
            }

            // DB 저장 (수업 등록)
            int lessonId = boardDao.addLessonBoard(ldo);

            System.out.println("✅ Lesson 추가 완료: " + title);
            return "redirect:/teachermainBoard.do";

        } catch (IOException e) {
            model.addAttribute("error", "사진 업로드에 실패했습니다.");
            e.printStackTrace();
            return "addlessonBoard";
        } catch (Exception e) {
            model.addAttribute("error", "수업 추가 중 문제가 발생했습니다.");
            e.printStackTrace();
            return "addlessonBoard";
        }
    }

    @RequestMapping(value = "/applyForLesson.do", method = RequestMethod.POST)
    public String applyForLesson(@RequestParam("lessonId") int lessonId, HttpSession session, Model model) {
        try {
            String userId = (String) session.getAttribute("loginId");
            String userName = (String) session.getAttribute("loginName");

            if (userId == null || userName == null) {
                model.addAttribute("error", "로그인이 필요합니다.");
                return "loginBoard";
            }

            // 수업 신청 정보 생성
            LessonrequestsDo lessonRequest = new LessonrequestsDo();
            lessonRequest.setUserId(userId);
            lessonRequest.setUserName(userName);
            
            // teacherId와 teacherName은 수업 정보에 맞게 설정해야 합니다.
            // teacherId를 lessonId에 해당하는 수업의 정보를 조회하여 설정
            LessonDo lesson = boardDao.getLessonById(lessonId); // 수업 정보를 가져옵니다.
            lessonRequest.setTeacherId(lesson.getTeacherId());
            lessonRequest.setTeacherName(lesson.getTeacherName());
            lessonRequest.setLessonId(lessonId); // 신청할 수업 ID 설정
            lessonRequest.setLessonName(lesson.getTitle()); // 수업 이름 설정
            lessonRequest.setRequestsStatus(LessonrequestsDo.RequestsStatus.PENDING);

            // 신청 추가
            lessonrequestsDao.addLessonRequest(lessonRequest);

            System.out.println("✅ 수업 신청 완료");
            return "redirect:/studentDashboard.do";

        } catch (Exception e) {
            model.addAttribute("error", "수업 신청 중 문제가 발생했습니다.");
            e.printStackTrace();
            return "applyForLesson";
        }
    }



    // 수업 삭제
    @RequestMapping(value = "/deleteLesson.do", method = RequestMethod.GET)
    public String deleteLesson(@RequestParam(value = "num", required = false, defaultValue = "0") int lessonId, Model model) {
        if (lessonId == 0) {
            model.addAttribute("error", "유효하지 않은 수업 번호입니다.");
            return "teachermainBoard";
        }

        try {
        	lessonrequestsDao.deleteLessonRequestsByLessonId(lessonId);
            
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

            if (!requestData.containsKey("lessonId") || requestData.get("lessonId") == null) {
                throw new IllegalArgumentException("lessonId 값이 없습니다.");
            }
            if (!requestData.containsKey("userId") || requestData.get("userId") == null) {
                throw new IllegalArgumentException("userId 값이 없습니다.");
            }
            if (!requestData.containsKey("teacherId") || requestData.get("teacherId") == null) {
                throw new IllegalArgumentException("teacherId 값이 없습니다.");
            }

            Object lessonIdObj = requestData.get("lessonId");
            int lessonId = 0;
            if (lessonIdObj instanceof Integer) {
                lessonId = (Integer) lessonIdObj;
            } else {
                try {
                    lessonId = Integer.parseInt(lessonIdObj.toString());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("lessonId는 숫자여야 합니다.");
                }
            }
            
            LessonrequestsDo lessonRequest = new LessonrequestsDo();
            lessonRequest.setUserId(requestData.get("userId").toString());
            lessonRequest.setTeacherId(requestData.get("teacherId").toString());
            lessonRequest.setLessonName(requestData.get("lessonTitle").toString());
            lessonRequest.setUserName(requestData.get("userName").toString());
            lessonRequest.setLessonId(lessonId);
            lessonRequest.setRequestsStatus(LessonrequestsDo.RequestsStatus.PENDING);
            lessonRequest.setSelectedTime(requestData.get("selectedTime").toString());

            lessonrequestsDao.addLessonRequest(lessonRequest);

            response.put("success", true);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }

    @PostMapping("/updateRequestStatus")
    @ResponseBody
    public Map<String, Object> updateRequestStatus(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> response = new HashMap<>();
        try {
            int num = (int) requestData.get("num");  
            if (num == 0) {
                throw new IllegalArgumentException("잘못된 요청: num 값이 0입니다.");
            }

            String status = (String) requestData.get("status");
            System.out.println("상태 변경 요청 - num: " + num + ", status: " + status);

            boolean isUpdated = lessonrequestsDao.updateRequestStatus(num, LessonrequestsDo.RequestsStatus.valueOf(status));

            if (isUpdated) {
                response.put("success", true);
            } else {
                response.put("success", false);
                response.put("error", "상태 변경에 실패했습니다.");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            System.err.println("상태 변경 중 오류 발생: " + e.getMessage());
        }
        return response;
    }

    // 사용자 수정 페이지
    @RequestMapping(value="/usermodifyBoard.do")
    public ModelAndView usermodifyBoard(HttpSession session, ModelAndView mav) {
        
        System.out.println("usermodifyBoard()");
        
        UserBoardDo loginUser = (UserBoardDo) session.getAttribute("loginUser"); // ✅ loginUser로 변경
        
        if (loginUser == null) {
            mav.setViewName("redirect:mainBoard.do"); // 로그인 페이지로 이동
            return mav;
        }
        
        UserBoardDo usertable = boardDao.getUserBoard(loginUser);
        
        if (usertable == null) {
        	System.out.println("usertable NULL");
        } else {
        	System.out.println("usertable.num 값: " + usertable.getNum());
        }
        
        mav.addObject("usertable", usertable);
        mav.setViewName("usermodifyBoard");
        
        return mav;
    }

    @RequestMapping(value = "/usermodifyBoardroc.do")
    public String usermodifyBoardroc(UserBoardDo udo, HttpSession session) {
        System.out.println("usermodifyBoardroc()");

        UserBoardDo loginUser = (UserBoardDo) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:mainBoard.do";
        }

        boardDao.usermodifyBoard(udo);
        System.out.println("usermodifyBoardroc() complete");

        UserBoardDo updatedUser = boardDao.getUserById(udo.getId());

        session.setAttribute("loginUser", updatedUser);

        return "redirect:mainBoard.do";
    }
     
    // 사용자 - 예약 확인 페이지
    @RequestMapping(value = "/detaillessonBoard.do", method = RequestMethod.GET)
    public String detailLessonBoard(@RequestParam("num") int num, Model model) {
        System.out.println("✅ Received num: " + num); // 디버깅 로그

        try {
            LessonDo lesson = boardDao.getLessonById(num);
            if (lesson == null) {
                System.out.println("❌ Lesson not found for num: " + num);
                model.addAttribute("error", "해당 강의를 찾을 수 없습니다.");
                return "errorPage";
            }

            model.addAttribute("lesson", lesson);
            return "detaillessonBoard";
        } catch (Exception e) {
            System.err.println("Error in detailLessonBoard: " + e.getMessage());
            model.addAttribute("error", "강의 정보를 가져오는 중 오류가 발생했습니다.");
            return "errorPage";
        }
    }
    
    
    // 수업 수정
    @RequestMapping(value="/0Board.do")
    public ModelAndView lessonmodifyBoard(LessonDo ldo, BoardDao bdao, ModelAndView mav) {
    	System.out.println("lessonmodifyBoard()");
    	
    	LessonDo lessontable = boardDao.getBoard(ldo);
    	
    	mav.addObject("lessontable", lessontable);
    	mav.setViewName("lessonmodifyBoard");
    	
    	return mav;
    }
    
    @RequestMapping(value="/lessonmodifyProcBoard.do")
    public ModelAndView lessonmodifyProcBoard(LessonDo ldo, BoardDao bdao, ModelAndView mav) {
    	System.out.println("lessonmodifyProcBoard()");
    	
    	boardDao.lessonmodifyBoard(ldo);
    	mav.setViewName("redirect:teachermainBoard.do");
    	
    	return mav;
    }
    
    
 
    
    
    // 게시판 글 작성 페이지
    @RequestMapping(value = "/insertenquiryBoard.do")
    public String insertenquiryBoard() {
        System.out.println("insertenquiryBoard()");
        return "insertenquiryBoard";
    }

    // 게시판 문의 등록 처리
    @RequestMapping(value = "/insertenquiryProcBoard.do")
    public String insertenquiryProcBoard(EnquirytableBoardDo edo, HttpSession session) {
        System.out.println("insertenquiryProcBoard()");

        // 세션에서 로그인한 사용자 정보 가져오기
        UserBoardDo loginUser = (UserBoardDo) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:mainBoard.do"; // 로그인 안 되어 있으면 로그인 페이지로 이동
        }

        // 사용자 정보 설정 (문의 작성자 ID)
        edo.setUserId(loginUser.getId());

        // DB에 저장
        boardDao.insertenquiryBoard(edo);

        return "redirect:mainBoard.do"; // 문의 등록 후 메인 페이지로 이동
    }
    
    // 문의 답변
    @RequestMapping(value = "/teachermessageBoard.do")
    public String teachermessageBoard(@RequestParam("num") int num, Model model) {
        try {
            EnquirytableBoardDo enquiry = boardDao.getEnquiryByNum(num);
            model.addAttribute("enquiry", enquiry);
            return "teachermessageBoard"; // 답변 작성 화면
        } catch (Exception e) {
            System.err.println("Error in teachermessageBoard: " + e.getMessage());
            model.addAttribute("error", "답변 페이지를 불러오는 중 문제가 발생했습니다.");
            return "errorPage";
        }
    }

    
    // 문의 답변 처리
    @PostMapping(value = "/teachermessageProcBoard.do")
    public String teachermessageProcBoard(EnquirytableBoardDo edo, HttpSession session) {
    	System.out.println("teachermessageProcBoard()");
    	
    	UserBoardDo loginUser = (UserBoardDo) session.getAttribute("loginUser");
    	if(loginUser == null) {
    		return "redirect:mainBoard.do";
    	}
    	
    	edo.setTeacherId(loginUser.getId());
    	    	
    	boardDao.teachermessageBoard(edo);
    	
    	return "redirect:teachermainBoard.do";
    	
    }
    











    
    

}