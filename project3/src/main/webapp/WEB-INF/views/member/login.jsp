<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="../header.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<title>LOG IN</title>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="shortcut icon" href="img/favicon.ico">
	<link rel="stylesheet" type="text/css" href="css/login.css" />
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;500;600&display=swap" rel="stylesheet">
	
<title>로그인</title>
</head>
<body>



<div class="login_form">
        
    
    <form method="post" action="#">
        <h3>Sign In</h3>
		<br><br>
        
        <input type="text" placeholder="ID" id="id">
        <input type="password" placeholder="PASSWORD" id="pwd">
		<br>
		<div style="text-align: right;">
			<a class="submit" href="find_id_form()">ID/PW 찾기</a>
        </div>
        
        <button onclick="location='#'">Log In</button>
        <br><br>
        <div style="text-align: right;">
			아직 회원이 아니신가요?&nbsp;&nbsp;
			<a class="submit" href="signup">SIGN UP</a>
        </div>
        <!-- 
        <div class="social">
          <div class="go"><i class="fab fa-google"></i>Google</div>
          <div class="fb"><i class="fab fa-facebook"></i>Facebook</div>
        </div>
         -->
    </form>
</div>


<div class="footer">
<%@ include file="../footer2.jsp" %>
</div>
</body>

</html>