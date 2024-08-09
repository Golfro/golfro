package com.itwill.golfro.web;

import static com.itwill.gaebokchi.filter.AuthenticationFilter.SESSION_ATTR_USER;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itwill.gaebokchi.repository.Normal;
import com.itwill.gaebokchi.repository.Pro;
import com.itwill.gaebokchi.service.UserMypageService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserHomeController {
   public static final String SESSION_USER_GRADE = "signedInUserGrade";
   private final UserMypageService userService;
//   private String userid = "banggu";

   @GetMapping("/mypage")
   public void mypage(HttpSession session, Model model) {
      log.debug("mypage()");
      
      String userid = (String) session.getAttribute(SESSION_ATTR_USER);
      String grade = (String) session.getAttribute(SESSION_USER_GRADE);
      
      if (grade.equals("G10")) {
         Pro pro = userService.readPro(userid);
         model.addAttribute("user", pro);
         log.debug("user={}", pro);
      } else {
         Normal user = userService.read(userid);
         model.addAttribute("user", user);
         log.debug("user={}", user);
      }

//      Point point = userService.change(userid);
//      model.addAttribute("point", point);
   }
}
