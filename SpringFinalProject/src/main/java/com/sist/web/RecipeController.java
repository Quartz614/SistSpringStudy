package com.sist.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sist.vo.*;
import com.sist.dao.*;
@Controller
public class RecipeController {
  @Autowired
  private RecipeDAO dao;
  @GetMapping("recipe/list.do")
  // 메소드는 리턴형 => 특별한 경우가 아니면 => String , void(화면 변경이 없는 경우, 다운로드)
  /*
   *    매개변수 => 객체 (DispatcherServlet)
   *    1) 커맨드 객체 : ~VO (회원가입 , 클쓰기...)
   *    2) Model : request로 변환 => JSP 데이터(요청결과) 전송 => 전송 객체
   *       forward에서만 사용 ==> return "경로/JSP명";
   *    3) RedirectAttributes : 재전송 
   *       sendRedirect : return "redirect:~.do"
   *    4) HttpSetvletRequest : 1) Cookie읽기 
   *    5) HttpServletResponse : 1) Cookie전송 , 2) 파일다운로드 
   *    6) HttpSession 
   *    7) List : 같은 이름의 데이터가 여러개 있는 경우 (checkbox)
   *    8) 일반 변수 => page , 검색어 , 비밀번호 , 번호 ...
   *    -------------------------------- 400(bad request) => 받는 데이터형이 틀릴 경우
   *    ?no=10  ==> (boolean no) 
   *    => 일반 데이터형은 1개 통합 => String ==> 필요시마다 변환(Wrapper)
   *       => int변환 (Integer.parseInt()) 
   *       => boolean (Boolean.parseBoolean())
   *       => double (Double.parseDouble())
   *       => 일반 데이터형 기능을 사용하기 쉽게 만든 클래스 (기술면접) 
   */
  public String recipe_list(String page,Model model,HttpServletRequest request)
  {
	  //Model => request,response사용을 권장하지 않는다 => 전송객체 (Model)
	  //사용자가 보내준 값 , 내장객체 => DispatcherServlet을 통해서 받아 온다 
	  //매개변수를 통해서 받는다 (순서는 상관없다)
	  // 리턴값은 2개중에 한개 선택 
	  // model값을 전송 => forward  ==> return "경로/파일명"
	  // 재전송 => sendRedirect()  ==> return "redirect:~~.do"
	  // 대부분은 해당 데이터형으로 받는다 
	  // 처음 실행시에 사용자가 page를 선택할 수 없다 
	  if(page==null)
		  page="1";
	  int curpage=Integer.parseInt(page);
	  Map map=new HashMap();
	  int rowSize=12;
	  int start=(rowSize*curpage)-(rowSize-1);
	  int end=rowSize*curpage;
	  
	  map.put("start", start);
	  map.put("end", end);
	  
	  List<RecipeVO> rList=dao.recipeListData(map);
	  int totalpage=dao.recipeTotalPage();
	  
	  final int BLOCK=10;
	  int startPage=((curpage-1)/BLOCK*BLOCK)+1;
	  //                (10-1)/10*10 => 0+1  ==> 1
	  // 1(curpage=1,10) ,  11(curpage=11~20) , 21 , 31
	  int endPage=((curpage-1)/BLOCK*BLOCK)+BLOCK; //10,20....
	  
	  if(endPage>totalpage)
		  endPage=totalpage;
	  
	  // list.jsp에서 출력에 필요한 데이터 전송
	  model.addAttribute("curpage", curpage);
	  model.addAttribute("totalpage", totalpage);
	  model.addAttribute("rList", rList);
	  model.addAttribute("startPage", startPage);
	  model.addAttribute("endPage", endPage);
	  // Cookie값 전송 
	  Cookie[] cookies=request.getCookies();
	  List<RecipeDetailVO> cList=new ArrayList<RecipeDetailVO>();
	  if(cookies!=null)
	  {
		  for(int i=cookies.length-1;i>=0;i--)
		  {
			  if(cookies[i].getName().startsWith("r"))
			  {
				  cookies[i].setPath("/");
				  String no=cookies[i].getValue();
				  RecipeDetailVO vo=dao.recipeDetailData(Integer.parseInt(no));
			      cList.add(vo);
			  }
		  }
		  model.addAttribute("cList", cList);
	  }
	  return "recipe/list";
  }
  // 조건 ==> 라이브러리 => 안에 코딩이 불가능 (컴파일된 파일만 보내준다) 
  // 읽어 갈 수 있는 소스 코딩 => 형식 => String
  @GetMapping("recipe/detail_before.do")
  public String rescipe_detail_before(int no,HttpServletResponse response,RedirectAttributes ra)
  {
	  Cookie cookie=new Cookie("r"+no, String.valueOf(no));
	  // 쿠키 단점 => 문자열만 저장이 가능 
	  cookie.setPath("/");//저장 위치
	  cookie.setMaxAge(60*60*24);//기간 
	  // 클라이언트로 전송 
	  response.addCookie(cookie);
	  
	  ra.addAttribute("no", no);
	  return "redirect:../recipe/detail.do";
  }
  @GetMapping("recipe/detail.do")
  public String recipe_detail(int no,Model model)
  {
	  // */* => /WEB-INF/recipe/detail.jsp => include (tiles는 include를 포함한다)
	  RecipeDetailVO vo=dao.recipeDetailData(no);
	  List<String> mList=new ArrayList<String>();
	  List<String> pList=new ArrayList<String>();
	  String[] data=vo.getFoodmake().split("\n");
	  for(String s:data)
	  {
		  StringTokenizer st=new StringTokenizer(s,"^");
		  mList.add(st.nextToken());
		  pList.add(st.nextToken());
	  }
	  
	  model.addAttribute("mList", mList);
	  model.addAttribute("pList", pList);
	  model.addAttribute("vo", vo);
	  return "recipe/detail";
  }
  @GetMapping("recipe/chef.do")
  public String recipe_chef(String page,Model model)
  {
	  if(page==null)
		  page="1";
	  int curpage=Integer.parseInt(page);
	  Map map=new HashMap();
	  int rowSize=12;
	  int start=(rowSize*curpage)-(rowSize-1);
	  int end=rowSize*curpage;
	  
	  map.put("start", start);
	  map.put("end", end);
	  
	  List<ChefVO> rList=dao.chefListData(map);
	  int totalpage=dao.chefTotalPage();
	  
	  final int BLOCK=10;
	  int startPage=((curpage-1)/BLOCK*BLOCK)+1;
	  //                (10-1)/10*10 => 0+1  ==> 1
	  // 1(curpage=1,10) ,  11(curpage=11~20) , 21 , 31
	  int endPage=((curpage-1)/BLOCK*BLOCK)+BLOCK; //10,20....
	  
	  if(endPage>totalpage)
		  endPage=totalpage;
	  
	  // list.jsp에서 출력에 필요한 데이터 전송
	  model.addAttribute("curpage", curpage);
	  model.addAttribute("totalpage", totalpage);
	  model.addAttribute("rList", rList);
	  model.addAttribute("startPage", startPage);
	  model.addAttribute("endPage", endPage);
	  return "recipe/chef";
  }
  /*
   *   스프링 : 클래스 메모리할당 (등록) => 저장 => 찾기  => DL
   *                ===================
   *                     |
   *                   필요한 데이터 (멤버변수의 초기화) => DI
   *   => 기능 추가 : MVC
   *              ----- Model찾기 , JSP로 데이터 전송 
   *                    HandlerMapping  ViewResolver
   *                    ---------------------------- Web
   *   => 소스 간결화 
   *      => 가장 긴 소스 : request로 값을 받고 VO에 값을 채운다 => 매개변수
   */
  @GetMapping("recipe/chef_detail.do")
  public String recipe_chef_detail(String page,String chef,Model model)
  {
	  // DAO연결 => 데이터 읽기 => chef_detail.jsp 전송 
	  if(page==null)
		  page="1";
	  int curpage=Integer.parseInt(page);
	  Map map=new HashMap();
	  int rowSize=12;
	  int start=(rowSize*curpage)-(rowSize-1);
	  int end=rowSize*curpage;
	  
	  map.put("start", start);
	  map.put("end", end);
	  map.put("chef",chef);
	  
	  List<RecipeVO> rList=dao.chefMakeRecipeData(map);
	  int totalpage=dao.chefMakeRecipeTotalpage(chef);
	  
	  final int BLOCK=10;
	  int startPage=((curpage-1)/BLOCK*BLOCK)+1;
	  //                (10-1)/10*10 => 0+1  ==> 1
	  // 1(curpage=1,10) ,  11(curpage=11~20) , 21 , 31
	  int endPage=((curpage-1)/BLOCK*BLOCK)+BLOCK; //10,20....
	  
	  if(endPage>totalpage)
		  endPage=totalpage;
	  
	  // list.jsp에서 출력에 필요한 데이터 전송
	  model.addAttribute("curpage", curpage);
	  model.addAttribute("totalpage", totalpage);
	  model.addAttribute("rList", rList);
	  model.addAttribute("startPage", startPage);
	  model.addAttribute("endPage", endPage);
	  model.addAttribute("chef", chef);
	  return "recipe/chef_detail";
  }
  
  @GetMapping("recipe/recipe_find.do")
  public String recipe_find(String fd,Model model)
  {
	  return "recipe/recipe_find";
  }
  /*
   *  종류별
상황별
재료별
방법별
전체밑반찬메인반찬국/탕찌개디저트면/만두밥/죽/떡퓨전김치/젓갈/장류양념/소스/잼양식샐러드스프빵과자차/음료/술기타
전체일상초스피드손님접대술안주다이어트도시락영양식간식야식푸드스타일링해장명절이유식기타
전체소고기돼지고기닭고기육류채소류해물류달걀/유제품가공식품류쌀밀가루건어물류버섯류과일류콩/견과류곡류기타
전체볶음끓이기부침조림무침비빔찜절임튀김삶기굽기데치기회기타
테마별
여성/뷰티 엄마/아기 건강/질병 제철요리 추천
   */
  @GetMapping("recipe/recipe_recommand.do")
  public String recipe_recommand()
  {
	  return "recipe/recipe_recommand";
  }
  
  @RequestMapping("recipe/priceCompare.do")
  public String recipe_price(String[] recipe,String fd,Model model)
  {
	  if(recipe!=null)
	  {
		  Map map=new HashMap();
		  map.put("fsArr", recipe);
		  map.put("ss", fd);
		  List<RecipeVO> list=dao.recipeSearchData(map);
		  model.addAttribute("rList", list);
	  }
	  return "recipe/priceCompare";
  }
}








