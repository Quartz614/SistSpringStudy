package com.sist.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

import javax.servlet.http.HttpSession;

import com.sist.dao.*;
import com.sist.vo.*;
@Controller
public class ReplyController {
   @Autowired
   private FoodReplyDAO dao;
   
   @GetMapping("food/login.do")
   public String food_login()
   {
	   return "food/login";
   }
   
   @GetMapping("food/logout.do")
   public String food_logout(HttpSession session)
   {
	   session.invalidate();
	   return "redirect:login.do";
   }
}
