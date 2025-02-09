package com.springboot.reservationsite.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.springboot.reservationsite.model.BoardDao;
import com.springboot.reservationsite.model.EnquirytableBoardDo;
import com.springboot.reservationsite.model.LessonDo;
import com.springboot.reservationsite.model.LessonrequestsDao;
import com.springboot.reservationsite.model.LessonrequestsDo;
import com.springboot.reservationsite.model.UserBoardDo;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

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
    
    // 사용자 메인 페이지 이동
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

    // 강사 메인 페이지 이동
    @RequestMapping(value = "/teachermainBoard.do")
    public String teachermainBoard(HttpSession session, Model model) {
        try {
            String teacherId = (String) session.getAttribute("loginId");
            if (teacherId == null) {
                return "redirect:/mainBoard.do";
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
                System.out.println("teacherId가 NULL입니다. 로그인이 필요합니다.");
                return "redirect:/loginBoard.do";
            }

            System.out.println("teacherMain.do 실행됨! teacherId: " + teacherId);

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

    // 로그인
    @RequestMapping(value = "/loginBoard.do")
    public String loginBoard() {
        System.out.println("loginBoard()");
        return "loginBoard";
    }
/*
    
	@RequestMapping(value = "/loginBoardProc.do", method = RequestMethod.POST)
    public String loginBoardProc(UserBoardDo udo, HttpSession session, Model model) {
        UserBoardDo udo1 = boardDao.loginBoard(udo.getId(), udo.getPwd());
        
        if (udo1 != null) {
            System.out.println("loginBoardProc() - Login Success");
            
            model.addAttribute("udo1", udo1);
            
            session.setAttribute("role", udo1.getRole().toString());
            session.setAttribute("loginUser", udo1);
            session.setAttribute("loginName", udo1.getName());
            session.setAttribute("loginId", udo1.getId());
            session.setAttribute("isLoggedIn", true);
            
            UserBoardDo sessionUser = (UserBoardDo) session.getAttribute("loginUser");
            if (sessionUser != null) {
                System.out.println("User stored in session: " + sessionUser);
                System.out.println("User num from session: " + sessionUser.getNum());  // num 확인
            } else {
                System.out.println("Session does not contain loginUser.");
            }
            
            UserBoardDo.Role role = udo1.getRole();
            switch (role) {
                case ADMIN:
                    return "redirect:/mainBoard.do";
                case TEACHER:
                    return "redirect:/mainBoard.do";
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
*/
    
    @RequestMapping(value = "/loginBoardProc.do", method = RequestMethod.POST)
    public String loginBoardProc(UserBoardDo udo, HttpSession session, Model model) {
        UserBoardDo udo1 = boardDao.loginBoard(udo.getId(), udo.getPwd());

        if (udo1 != null) {
            System.out.println("loginBoardProc() - Login Success");

            model.addAttribute("udo1", udo1);
            session.setAttribute("role", udo1.getRole().toString());  // role 세션에 저장
            session.setAttribute("loginUser", udo1);
            session.setAttribute("loginName", udo1.getName());
            session.setAttribute("loginId", udo1.getId());
            session.setAttribute("isLoggedIn", true);

            return "redirect:/mainBoard.do"; // 로그인 후 mainBoard.jsp로 이동
        } else {
            System.out.println("loginBoardProc() - Login Failed");
            model.addAttribute("error", "Invalid ID or Password");
            return "loginBoard";
        }
    }
    
    // 페이지 이동
    @RequestMapping(value = "/managePage.do", method = RequestMethod.GET)
    public String managePage(HttpSession session) {
        String role = (String) session.getAttribute("role");

        if (role != null) {
            switch (role) {
                case "ADMIN":
                    return "redirect:/adminmainBoard.do";
                case "TEACHER":
                    return "redirect:/teachermainBoard.do";
                case "USER":
                    return "redirect:/usermainBoard.do";
                default:
                    return "redirect:/mainBoard.do";
            }
        } else {
            return "redirect:/loginBoard.do";
        }
    }

    // 로그아웃
    @RequestMapping(value = "/logout.do", method = RequestMethod.GET)
    public String logoutBoard(HttpSession session) {
        System.out.println("logoutBoard()");
        session.invalidate();
        return "redirect:/mainBoard.do";
    }

    //사진 저장
    private static final String UPLOAD_DIR = "C:/haeun_java_workspace/spring/workspace/reservationsite/src/main/resources/static/images";

    // 수업 추가
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

            // 사진 업로드 처리
            if (photo != null && !photo.isEmpty()) {
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                    System.out.println("업로드 폴더 생성: " + UPLOAD_DIR);
                }

                String fileName = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
                File saveFile = new File(UPLOAD_DIR + File.separator + fileName);

                photo.transferTo(saveFile);

                byte[] photoBytes = Files.readAllBytes(saveFile.toPath());

                ldo.setPhoto(photoBytes);
                ldo.setPhotoPath("/images/" + fileName);

                if (saveFile.exists()) {
                    System.out.println("파일 업로드 성공: " + saveFile.getPath());
                } else {
                    System.out.println("파일 업로드 실패");
                }

            } else {
                // 사진이 없을 경우 기본 이미지 경로 설정
                ldo.setPhotoPath("/images/lesson_1.jpg");
                System.out.println("사진이 없어 기본 이미지 사용");
            }

            int lessonId = boardDao.addLessonBoard(ldo);

            System.out.println("Lesson 추가 완료: " + title);
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

    //수업 신청 상태인가?
    @RequestMapping(value = "/applyForLesson.do", method = RequestMethod.POST)
    public String applyForLesson(@RequestParam("lessonId") int lessonId, HttpSession session, Model model) {
        try {
            String userId = (String) session.getAttribute("loginId");
            String userName = (String) session.getAttribute("loginName");

            if (userId == null || userName == null) {
                model.addAttribute("error", "로그인이 필요합니다.");
                return "loginBoard";
            }

            LessonrequestsDo lessonRequest = new LessonrequestsDo();
            lessonRequest.setUserId(userId);
            lessonRequest.setUserName(userName);
            
            LessonDo lesson = boardDao.getLessonById(lessonId);
            lessonRequest.setTeacherId(lesson.getTeacherId());
            lessonRequest.setTeacherName(lesson.getTeacherName());
            lessonRequest.setLessonId(lessonId);
            lessonRequest.setLessonName(lesson.getTitle());
            lessonRequest.setRequestsStatus(LessonrequestsDo.RequestsStatus.PENDING);

            // 신청 추가
            lessonrequestsDao.addLessonRequest(lessonRequest);

            System.out.println("수업 신청 완료");
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
        	System.out.println("applyLesson()");
        	System.out.println("requestData: " + requestData);

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
            lessonRequest.setTeacherName(requestData.get("teacherName").toString());
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

    // 강의 상태 변경
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
        
        UserBoardDo loginUser = (UserBoardDo) session.getAttribute("loginUser");
        
        if (loginUser == null) {
            mav.setViewName("redirect:mainBoard.do");
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
        System.out.println("Received num: " + num); // 디버깅 로그

        try {
            LessonDo lesson = boardDao.getLessonById(num);
            if (lesson == null) {
                System.out.println("Lesson not found for num: " + num);
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
    @RequestMapping(value="/lessonmodifyBoard.do")
    public ModelAndView lessonmodifyBoard(LessonDo ldo, ModelAndView mav) {
    	System.out.println("lessonmodifyBoard()");
    	
    	LessonDo lessontable = boardDao.getBoard(ldo);
    	
    	mav.addObject("lessontable", lessontable);
    	mav.setViewName("lessonmodifyBoard");
    	
    	
    	return mav;
    }

    /*    
    @RequestMapping(value = "/lessonmodifyProcBoard.do", method = RequestMethod.POST)
    public ModelAndView lessonmodifyProcBoard(@Valid LessonDo lessonDo, BindingResult result, ModelAndView mav) {

        if (lessonDo.getPhotoFile() != null && !lessonDo.getPhotoFile().isEmpty()) {
            try {
                byte[] photoBytes = lessonDo.getPhotoFile().getBytes();
                lessonDo.setPhoto(photoBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("photo: " + lessonDo.getPhoto());
        System.out.println("photoPath: " + lessonDo.getPhotoPath());
        System.out.println("title: " + lessonDo.getTitle());
        System.out.println("description: " + lessonDo.getDescription());
        System.out.println("time: " + lessonDo.getTime());
        System.out.println("people: " + lessonDo.getPeople());

        boardDao.lessonmodifyBoard(lessonDo);

        mav.setViewName("redirect:teachermainBoard.do");
        return mav;
    }
*/

    /*
    @RequestMapping(value = "/lessonmodifyProcBoard.do", method = RequestMethod.POST)
    public ModelAndView lessonmodifyProcBoard(@Valid LessonDo lessonDo, BindingResult result, ModelAndView mav) {
        if (result.hasErrors()) {
            mav.setViewName("lessonmodifyBoard");
            return mav;
        }

        if (lessonDo.getPhotoFile() != null && !lessonDo.getPhotoFile().isEmpty()) {
            try {
                byte[] photoBytes = lessonDo.getPhotoFile().getBytes();
                lessonDo.setPhoto(photoBytes);
                String fileName = lessonDo.getPhotoFile().getOriginalFilename();
                String photoPath = "/images/" + fileName;
                lessonDo.setPhotoPath(photoPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            LessonDo existingLesson = boardDao.getLessonByNum(lessonDo.getNum());
            lessonDo.setPhoto(existingLesson.getPhoto());
            lessonDo.setPhotoPath(existingLesson.getPhotoPath());
        }

        boardDao.lessonmodifyBoard(lessonDo);
        mav.setViewName("redirect:teachermainBoard.do");
        return mav;
    }
*/

    @RequestMapping(value = "/lessonmodifyProcBoard.do", method = RequestMethod.POST)
    public ModelAndView lessonmodifyProcBoard(@Valid LessonDo lessonDo, BindingResult result, ModelAndView mav) {
        if (result.hasErrors()) {
            mav.setViewName("lessonmodifyBoard");
            return mav;
        }

        if (lessonDo.getPhotoFile() != null && !lessonDo.getPhotoFile().isEmpty()) {
            try {
                byte[] photoBytes = lessonDo.getPhotoFile().getBytes();
                lessonDo.setPhoto(photoBytes);

                // 저장할 디렉토리 지정
                String uploadDir = "C:/haeun_java_workspace/spring/workspace/reservationsite/src/main/resources/static/images/";
                File uploadFolder = new File(uploadDir);
                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs();
                }

                // 파일 저장
                String fileName = lessonDo.getPhotoFile().getOriginalFilename();
                String filePath = uploadDir + fileName;
                File file = new File(filePath);
                lessonDo.getPhotoFile().transferTo(file);

                // DB에 저장될 경로 설정
                String photoPath = "/images/" + fileName;
                lessonDo.setPhotoPath(photoPath);

                System.out.println("파일 저장 성공: " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 기존 데이터 유지
            LessonDo existingLesson = boardDao.getLessonByNum(lessonDo.getNum());
            lessonDo.setPhoto(existingLesson.getPhoto());
            lessonDo.setPhotoPath(existingLesson.getPhotoPath());
        }

        boardDao.lessonmodifyBoard(lessonDo);
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

        UserBoardDo loginUser = (UserBoardDo) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:mainBoard.do";
        }

        edo.setUserId(loginUser.getId());


        boardDao.insertenquiryBoard(edo);
        System.out.println("LessonId: " + edo.getLessonId());

        return "redirect:mainBoard.do";
    }
    
    // 문의 답변
    @RequestMapping(value = "/teachermessageBoard.do")
    public String teachermessageBoard(@RequestParam("num") int num, Model model) {
        try {
            EnquirytableBoardDo enquiry = boardDao.getEnquiryByNum(num);
            model.addAttribute("enquiry", enquiry);
            return "teachermessageBoard";
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