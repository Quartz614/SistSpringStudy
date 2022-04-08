<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<div class="wrapper row2">
  <div id="breadcrumb" class="clear"> 
    <!-- ################################################################################################ -->
    <ul>
      <li><a href="#">Home</a></li>
      <li><a href="#">레시피</a></li>
      <li><a href="#">레시피 검색</a></li>
    </ul>
    <!-- ################################################################################################ -->
  </div>
</div>
<!-- ################################################################################################ -->
<!-- ################################################################################################ -->
<!-- ################################################################################################ -->
<div class="wrapper row3">
  <main class="container clear"> 
    <!-- main body -->
    <!-- ################################################################################################ -->
    <div class="content"> 
      <!-- ################################################################################################ -->
      <div id="gallery">
        <figure>
          <header class="heading" style="display: inline-block">
           <form method="post" action="../recipe/priceCompare.do">
	           <input type="checkbox" name="recipe" value="T" style="display: inline-block">레시피명
	           <input type="checkbox" name="recipe" value="C" style="display: inline-block">쉐프명
	           <!-- <input type="checkbox" name="recipe" value="S" style="display: inline-block">레시피설명 -->
	           <input type="text" name="fd" size=20 class="input-sm" style="display: inline-block">
	           <input type="submit" value="전송" style="display: inline-block">
           </form>
          </header>
          <ul class="nospace clear">
           <c:forEach var="vo" items="${rList }" varStatus="s">
            <c:if test="${s.index%4==0 }">
              <li class="one_quarter first"><a href="../recipe/detail_before.do?no=${vo.no }"><img src="${vo.poster }" title="${vo.title }"></a></li>
            </c:if>
            <c:if test="${s.index%4!=0 }">
              <li class="one_quarter"><a href="../recipe/detail_before.do?no=${vo.no }"><img src="${vo.poster }" title="${vo.title }"></a></li>
            </c:if>
           </c:forEach>
            
          </ul>
          
        </figure>
      </div>
      
    <!-- / main body -->
    <div class="clear"></div>
  </main>
</div>
</body>
</html>