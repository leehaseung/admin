<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Gps</title>
    <meta name="keywords" content="HTML5 Template" />
    <meta name="description" content="Volvox - Responsive HTML5 Template">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <link rel="shortcut icon" type="image/png" href="img/favicon.png" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Web Fonts  -->
    <link href="http://fonts.googleapis.com/css?family=Open+Sans:300,400,500,600,700,800" rel="stylesheet" type="text/css">
    <link href="http://fonts.googleapis.com/css?family=Raleway:100,200,300,400,500,700,800,900" rel="stylesheet" type="text/css">

    <!-- Libs CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet" />
    <link href="css/style.css" rel="stylesheet" />
    <link href="css/font-awesome.min.css" rel="stylesheet" />
    <link href="css/streamline-icon.css" rel="stylesheet" />
    <link href="css/v-nav-menu.css" rel="stylesheet" />
    <link href="css/v-portfolio.css" rel="stylesheet" />
    <link href="css/v-blog.css" rel="stylesheet" />
    <link href="css/v-animation.css" rel="stylesheet" />
    <link href="css/v-bg-stylish.css" rel="stylesheet" />
    <link href="css/v-shortcodes.css" rel="stylesheet" />
    <link href="css/theme-responsive.css" rel="stylesheet" />
    <link href="plugins/owl-carousel/owl.theme.css" rel="stylesheet" />
    <link href="plugins/owl-carousel/owl.carousel.css" rel="stylesheet" />

    <!-- Custom CSS -->
    <link rel="stylesheet" href="css/custom.css">
    <!-- naver map -->
    
    <style>
.comment:hover { background: #D9E5FF; }
.gpsno{
   border: 0px solid red;	
	display:inline-block;
	width:80px;
}
.geocodex{

	display:inline-block;
	width:150px;
}
.geocodey{

	display:inline-block;
	width:150px;
}
.time{
	
	display:inline-block;
	width:170px;
}
.garo{
display:inline-block;
	width:370px;
}
#map{
	width:60%;
	height:500px;
	  float: left;
     position: relative;
     
}
#right {
	border: 1px solid #F6F6F6;	
   // background-color: #09C;
   float: left;
    height: 100%;
    width: 250px;
    position: relative;
    
}
#commentList{
		width: 1100px;
		height: 300px;
		 color: black;
		 overflow:scroll;
		 border: 1px solid #F6F6F6;
		  
		     position: relative;
		      clear: both;

}

.comment{
 border: 1px solid #F6F6F6;
}

#find {
		width: 900px;
		 color: black;
		 border: 1px solid #F6F6F6;
		   float: left;
     position: relative;
      clear: both;
		 
}

</style>
    <script type="text/javascript" 
			src="https://openapi.map.naver.com/openapi/v3/maps.js?clientId=YsYH9nE0qsa_VSAWAP24&submodules=geocoder">
		</script>
	<script src="ajax.js"></script>
	<script src="./scripts/jquery-3.2.1.min.js"></script>
	
<script>
$(function(){
	//////////////////////////////////오늘 시간 구하기
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0!
	var yyyy = today.getFullYear();
	
	
	if (("" + mm).length == 1) { mm = "0" + mm; }
    if (("" + dd).length   == 1) { dd   = "0" + dd;   }
    
    today = yyyy+'-'+mm+'-'+dd;
	//alert(today);  
	/////////////////////////////////////오늘시간 구하기 끝
	
	$("#starttime").val(today +"T13:00:00");
	$("#endtime").val(today +"T13:10:00");
	
	
	$("#send").click(function(){
		//id 입력여부 에크 
		if( $("#starttime").val() == ""){
			alert("시작시간 입력");
			$("#starttime").focus();
			return false;  //기본기능 중지(preventDefault())
		}else if( $("#endtime").val() == ""){
			alert("엔드시간 입력");
			$("#endtime").focus();
			return false;  //기본기능 중지(preventDefault())
		}
	});
	
	
});

</script>

<script >
var position=null ;
var marker = null;
var markers =[];
var gpsarr = [];
var polyline = null;

window.onload = function(){
	//loadCommentList();


}
//댓글 등록 요청
// 검색 기능  시작 
function findtimes(){
	var starttime = document.findtime.starttime.value;      //작성자 텍스트필드 입력값

	starttime =starttime.replace(/\T/g,' ');
	
	var endtime = document.findtime.endtime.value;  //댓글
	endtime=endtime.replace('T',' ');
	
	var param = "cmd=find&starttime="+ starttime + "&endtime=" + endtime ;
	new ajax.xhr.Request("GpsServ",param,loadCommentResult,"get");	
}

//목록요청
function loadCommentList(){
	new ajax.xhr.Request("GpsServ?cmd=selectAll","",loadCommentResult,"get");	
}
	//목록요청 callback
function loadCommentResult(req){
		if(req.readyState == 4) {
			if(req.status == 200){
				var xmlDoc = req.responseXML; //서버의 실행 결과 XML
				var code = xmlDoc.getElementsByTagName("code") //code 태그내용
					.item(0).firstChild.nodeValue;
				
					
				if(code == 'success'){
					
					var data = xmlDoc.getElementsByTagName("data") //code 태그내용
					.item(0).firstChild.nodeValue;
					var commentList = eval( "("+ data +")" ); //string -> 객체
					var listDiv = document.getElementById("commentList");// 출력할 위치
					for(i=0;i<commentList.length;i++){  //댓글 수만큼 반복
						
						gpsarr.push(new naver.maps.LatLng(commentList[i].x,commentList[i].y));
						
						var div = makeCommentView(commentList[i]); //댓글 div 생성
						listDiv.appendChild(div);
					}
				//	polylines();
				//markers();
			//	info();
				}else{
					alert("지도로딩 실패:" + req.status)
				}
				
			}
		}
}




function makeCommentView(comment){
	var commentDiv = document.createElement("div"); //태그 생성 <div>
	commentDiv.setAttribute("id",comment.no);  //속성지정<div id ='c1'/> "c"+comment.no
	var html="<span class='gpsno'>"+comment.no + "</span>" + 
			 "<span class='time'>" + comment.gdate + "</span>" + "<span class='garo' >" +
			 "<a href='javascript:getmarker("+comment.x +','+ comment.y+")'>"+
			 comment.addr+"</a></span>" +"<span class='geocodex'>" + comment.x + "</span>" +" " +
			 "<span class='geocodey'>" + comment.y + "</span>";
		
	commentDiv.innerHTML = html; //태그내용 지정
	commentDiv.className = "comment";  
	//태그 스타일 속성 <div id ='c1' class="comment" />
	// <div id='c1' class="comment">태그내용 </div>
	commentDiv.comment = comment;
	return commentDiv;
}
//검색 기능 끝 

</script>

</head>

<body>

    <!--Header-->
    <div class="header-container">

        <header class="header fixed clearfix">

            <div class="container">

                <!--Site Logo-->
                <div class="logo">
                    <a href="index.html">
                        <img alt="Volvox" src="img/logo.png" data-logo-height="35">
                    </a>
                </div>
                <!--End Site Logo-->

                <div class="navbar-collapse nav-main-collapse collapse">

                    <!--Header Search-->
                    <div class="search" id="headerSearch">
                        <a href="#" id="headerSearchOpen"><i class="fa fa-search"></i></a>
                        <div class="search-input">
                            <form id="headerSearchForm" action="#" method="get">
                                <div class="input-group">
                                    <input type="text" class="form-control search" name="q" id="q" placeholder="Search...">
                                    <span class="input-group-btn">
                                        <button class="btn btn-primary" type="button"><i class="fa fa-search"></i></button>
                                    </span>
                                </div>
                            </form>
                            <span class="v-arrow-wrap"><span class="v-arrow-inner"></span></span>
                        </div>
                    </div>
                    <!--End Header Search-->
                    <!--Main Menu-->
                    <nav class="nav-main mega-menu">
                        <ul class="nav nav-pills nav-main" id="mainMenu">
                            <li class="dropdown">
                                <a class="dropdown-toggle" href="#">Home <i class="fa fa-caret-down"></i></a>
                                <ul class="dropdown-menu">
                                    <li><a href="index.html">Home - Variation 1</a></li>
                                    <li><a href="index-2.html">Home - Variation 2</a></li>
                                    <li><a href="index-3.html">Home - Variation 3</a></li>
                                    <li><a href="index-4.html">Home - Variation 4</a></li>
                                    <li><a href="index-5.html">Home - Variation 5 <span class="v-menu-item-info bg-warning">Boxed</span></a></li>
                                    <li><a href="index-6.html">Home - Variation 6</a></li>
                                    <li><a href="index-7.html">Home - Variation 7</a></li>
                                    <li><a href="index-8.html">Home - Variation 8</a></li>
                                    <li><a href="index-9.html">Home - Variation 9 <span class="v-menu-item-info bg-info">Flat</span></a></li>
                                    <li><a href="index-10.html">Home - Variation 10</a></li>
                                    <li><a href="index-11.html">Home - Variation 11</a></li>
                                    <li><a href="index-12.html">Home - Variation 12</a></li>
                                    <li><a href="index-13.html">Home - Variation 13</a></li>
                                    <li><a href="index-14.html">Home - Variation 14</a></li>
                                    <li><a href="page-landing.html">Home - Variation 15 <span class="v-menu-item-info bg-success">Landing</span></a></li>
                                </ul>
                            </li>
                            <li class="dropdown mega-menu-item mega-menu-fullwidth">
                                <a class="dropdown-toggle" href="#">Pages <i class="fa fa-caret-down"></i></a>
                                <ul class="dropdown-menu three-columns">
                                    <li>
                                        <div class="mega-menu-content">
                                            <div class="row">
                                                <div class="menu-logo-wrap">
                                                    <img class="menu-logo" src="img/menu-logo.png" />
                                                </div>

                                                <div class="col-md-3">
                                                    <ul class="sub-menu">
                                                        <li>
                                                            <span class="mega-menu-sub-title">Example Pages</span>
                                                            <ul class="sub-menu">
                                                                <li><a href="page-about-us.html">About Us</a></li>
                                                                <li><a href="page-about-us-2.html">About Us - v2</a></li>
                                                                <li><a href="page-about-us-3.html">About Us - v3</a></li>
                                                                <li><a href="page-about-me.html">About Me</a></li>
                                                                <li><a href="page-about-me-2.html">About Me - v2</a></li>
                                                                <li><a href="page-about-me-3.html">About Me - v3</a></li>
                                                                <li><a href="page-services.html">Service Page</a></li>
                                                                <li><a href="page-services.html">Service Page - v2</a></li>

                                                            </ul>
                                                        </li>
                                                    </ul>
                                                </div>
                                                <div class="col-md-3">
                                                    <ul class="sub-menu">
                                                        <li>
                                                            <span class="mega-menu-sub-title">Special Pages</span>
                                                            <ul class="sub-menu">
                                                                <li><a href="page-careers.html">Careers Page</a></li>
                                                                <li><a href="page-search-result.html">Search Result Page</a></li>
                                                                <li><a href="page-landing.html">Landing Page <span class="v-menu-item-info bg-success">Hot</span></a></li>
                                                                <li><a href="page-privacy.html">Privacy Page</a></li>
                                                                <li><a href="page-parallax.html">Parallax Page</a></li>
                                                                <li><a href="page-help.html">Help Page</a></li>
                                                                <li><a href="page-author-page.html">Author Page</a></li>
                                                                <li><a href="page-about-us-3.html">Custom Header Page</a></li>
                                                            </ul>
                                                        </li>
                                                    </ul>
                                                </div>
                                                <div class="col-md-3">
                                                    <ul class="sub-menu">
                                                        <li>
                                                            <span class="mega-menu-sub-title">Common Pages</span>
                                                            <ul class="sub-menu">
                                                                <li><a href="page-faq.html">FAQ Page</a></li>
                                                                <li><a href="page-meet-team.html">Meet The Team</a></li>
                                                                <li><a href="page-right-sidebar.html">Right Sidebar Page</a></li>
                                                                <li><a href="page-left-sidebar.html">Left Sidebar Page</a></li>
                                                                <li><a href="page-full-width.html">Full width Page</a></li>
                                                                <li><a href="page-login.html">Login Page</a></li>
                                                                <li><a href="page-register.html">Registration Page</a></li>
                                                            </ul>
                                                        </li>
                                                    </ul>
                                                </div>
                                                <div class="col-md-3">
                                                    <ul class="sub-menu">
                                                        <li>
                                                            <span class="mega-menu-sub-title">Other Pages</span>
                                                            <ul class="sub-menu">
                                                                <li><a href="contact-us.html">Contact Us</a></li>
                                                                <li><a href="contact-us-2.html">Contact Us - v2</a></li>
                                                                <li><a href="page-pricing.html">Page Pricing</a></li>
                                                                <li><a href="page-pricing.html">Page Pricing v2</a></li>
                                                                <li><a href="page-404.html">404 Error</a></li>
                                                            </ul>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </li>
                                </ul>
                            </li>
                            <li class="dropdown active">
                                <a class="dropdown-toggle" href="#">Features <i class="fa fa-caret-down"></i></a>
                                <ul class="dropdown-menu">
                                    <li class="dropdown-submenu">
                                        <a href="#">Sliders</a>
                                        <ul class="dropdown-menu">
                                            <li class="dropdown-submenu">
                                                <a href="portfolio-single.html">Master Slider</a>
                                                <ul class="dropdown-menu">
                                                    <li><a href="index-8.html">Full Width Slider</a></li>
                                                    <li><a href="index-12.html">Full Screen Slider</a></li>
                                                    <!--<li><a href="indexx-10.html">Boxed Slider</a></li>-->
                                                </ul>
                                            </li>
                                            <li class="dropdown-submenu">
                                                <a href="#">Revolution Slider</a>
                                                <ul class="dropdown-menu">
                                                    <li><a href="index-12.html">Full Width Slider</a></li>
                                                    <li><a href="index-6.html">Full Screen Slider</a></li>
                                                    <li><a href="index-10.html">Boxed Slider</a></li>
                                                </ul>
                                            </li>
                                        </ul>
                                    </li>
                                    <li class="dropdown-submenu">
                                        <a href="#">Footers</a>
                                        <ul class="dropdown-menu">
                                            <li><a href="page-404.html#footer">Footer Variation - v1</a></li>
                                            <li><a href="page-faq.html#footer">Footer Variation - v2</a></li>
                                            <li><a href="page-services.html#footer">Footer Variation - v3</a></li>
                                            <li><a href="index-11.html#footer">Footer Variation - v4</a></li>
                                        </ul>
                                    </li>
                                    <li class="dropdown-submenu">
                                        <a href="#">Headers</a>
                                        <ul class="dropdown-menu">
                                            <li><a href="index-9.html">Transparent Header</a></li>
                                            <li><a href="index-11.html">Semi Transparent Header</a></li>
                                            <!--<li><a href="index-7.html">Transparent With Top Header</a></li>-->
                                            <li><a href="index-4.html">Header With Top Header</a></li>
                                            <li><a href="index-6.html">Floating Header <span class="v-menu-item-info">Hot</span></a></li>
                                        </ul>
                                    </li>
                                    <li><a class="current" href="element-maps.html">Google Maps</a></li>
                                </ul>
                            </li>
                            <li class="dropdown mega-menu-item mega-menu-fullwidth">
                                <a class="dropdown-toggle" href="#">Elements <i class="fa fa-caret-down"></i></a>
                                <ul class="dropdown-menu three-columns">
                                    <li>
                                        <div class="mega-menu-content no-smx">
                                            <div class="row">
                                                <div class="col-md-4">
                                                    <ul class="sub-menu">
                                                        <li>
                                                            <span class="mega-menu-sub-title">Common Elements</span>
                                                            <ul class="sub-menu">
                                                                <li><a href="element-common.html#gridsystem"><i class="fa fa-star-o"></i>Grid System</a></li>
                                                                <li><a href="element-common.html#typograpy"><i class="fa fa-text-height"></i>Typograpy</a></li>
                                                                <li><a href="element-forms.html"><i class="fa fa-briefcase"></i>Form Elements <span class="v-menu-item-info">Hot</span></a></li>
                                                                <li><a href="element-common.html#lists"><i class="fa fa-list"></i>Lists</a></li>
                                                                <li class="dropdown-submenu">
                                                                    <a href="#"><i class="fa fa-book"></i>Glyphicons Icons</a>
                                                                    <ul class="dropdown-menu">
                                                                        <li><a href="element-icons.html">Font Awesome Icons</a></li>
                                                                        <li><a href="element-icons-2.html">Streamline Icons</a></li>
                                                                    </ul>
                                                                </li>
                                                                <li><a href="element-common.html#info-messages"><i class="fa fa-info-circle"></i>Info Messages</a></li>
                                                                <li><a href="element-common.html#heading-options"><i class="fa fa-magic"></i>Heading Options</a></li>
                                                                <li><a href="element-common.html#tagline"><i class="fa fa-info"></i>Tagline & Info Boxes</a></li>
                                                                <li><a href="element-common.html#pagination"><i class="fa fa-ellipsis-h"></i>Pagination</a></li>
                                                                <li><a href="element-common.html#separator-divider"><i class="fa fa-cut"></i>Separator / Divider</a></li>
                                                            </ul>
                                                        </li>
                                                    </ul>
                                                </div>
                                                <div class="col-md-4">
                                                    <ul class="sub-menu">
                                                        <li>
                                                            <span class="mega-menu-sub-title">Featured Elements</span>
                                                            <ul class="sub-menu">
                                                                <li><a href="index-3.html"><i class="fa fa-youtube-play"></i>Video Section</a></li>
                                                                <li><a href="page-parallax.html"><i class="fa fa-leaf"></i>Paralax Section</a></li>
                                                                <li><a href="element-pricing-tables.html"><i class="fa fa-th"></i>Pricing Tables</a></li>
                                                                <li><a href="element-charts.html"><i class="fa fa-bar-chart-o"></i>Charts <span class="v-menu-item-info">Hot</span></a></li>
                                                                <li><a href="element-progress-bar.html"><i class="fa fa-tasks"></i>Progress Bars</a></li>
                                                                <li><a href="element-tabs.html"><i class="fa fa-sort"></i><span>Tab Control</span></a></li>
                                                                <li><a href="page-meet-team.html"><i class="fa fa-user"></i>Team Elements</a></li>
                                                                <li><a href="element-accordion.html"><i class="fa fa-bars"></i><span>Accordion & Toggles</span></a></li>
                                                                <li><a href="element-call-to-actions.html"><i class="fa fa-thumb-tack"></i>Call To Actions <span class="v-menu-item-info bg-warning">Important</span></a></li>
                                                                <li><a href="element-process-steps.html"><i class="fa fa-sort-amount-asc"></i>Process Steps</a></li>
                                                            </ul>
                                                        </li>
                                                    </ul>
                                                </div>
                                                <div class="col-md-4">
                                                    <ul class="sub-menu">
                                                        <li>
                                                            <span class="mega-menu-sub-title">Interactive Elements</span>
                                                            <ul class="sub-menu">
                                                                <li><a href="element-content-carousel.html"><i class="fa fa-laptop"></i>Content Carousel</a></li>
                                                                <li><a href="element-common2.html#modal-windows"><i class="fa fa-share-square-o"></i>Modal Windows</a></li>
                                                                <li><a href="element-testimonials.html"><i class="fa fa-quote-left"></i>Testimonials</a></li>
                                                                <li><a href="element-brands-clients.html"><i class="fa fa-fire"></i>Brands & Clients</a></li>
                                                                <li><a href="element-common2.html#image-frames"><i class="fa fa-crop"></i>Image Frames</a></li>
                                                                <li><a href="element-buttons.html"><i class="fa fa-twitter"></i>Buttons & Social Icons <span class="v-menu-item-info bg-warning">Wow</span></a></li>
                                                                <li><a href="element-iconboxes.html"><i class="fa fa-location-arrow"></i>Animation & Feature Boxes</a></li>
                                                                <li><a href="element-common2.html#circle-counters"><i class="fa fa-clock-o"></i>Circle Counters</a></li>
                                                                <li><a href="page-about-us-2.html"><i class="fa fa-umbrella"></i>Fancy Heading</a></li>
                                                                <li><a href="element-common2.html#recent-posts"><i class="fa fa-calendar"></i>Recent Posts & News</a></li>
                                                            </ul>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </li>
                                </ul>
                            </li>

                            <li class="dropdown">
                                <a class="dropdown-toggle menu-icon" href="#">Portfolio <i class="fa fa-caret-down"></i></a>
                                <ul class="dropdown-menu">
                                    <li class="dropdown-submenu">
                                        <a href="#">Portfolio Gallery</a>
                                        <ul class="dropdown-menu">
                                            <li><a href="portfolio-two-gallery.html">Gallery - 2 Columns</a></li>
                                            <li><a href="portfolio-three-gallery.html">Gallery - 3 Columns</a></li>
                                            <li><a href="portfolio-four-gallery.html">Gallery - 4 Columns</a></li>
                                        </ul>
                                    </li>
                                    <li class="dropdown-submenu">
                                        <a href="#">Portfolio Standard</a>
                                        <ul class="dropdown-menu">
                                            <li><a href="portfolio-two-standard.html">Standard - 2 Columns</a></li>
                                            <li><a href="portfolio-three-standard.html">Standard - 3 Columns</a></li>
                                            <li><a href="portfolio-four-standard.html">Standard - 4 Columns</a></li>
                                        </ul>
                                    </li>
                                    <li class="dropdown-submenu">
                                        <a href="#">Portfolio Masonry</a>
                                        <ul class="dropdown-menu">
                                            <li><a href="portfolio-masonry-gallery.html">Masonry Gallery</a></li>
                                            <li><a href="portfolio-masonry-standard.html">Masonry Standard</a></li>
                                            <li><a href="portfolio-masonry-fw.html">Masonry Fullwidth Standard</a></li>
                                            <li><a href="portfolio-masonry-fw-2.html">Masonry Fullwidth Gallery</a></li>
                                        </ul>
                                    </li>
                                    <li class="dropdown-submenu">
                                        <a href="#">Portfolio Single</a>
                                        <ul class="dropdown-menu">
                                            <li><a href="portfolio-single.html">Single Portfolio</a></li>
                                            <li><a href="portfolio-single-slider.html">Single Slider Portfolio</a></li>
                                            <li><a href="portfolio-single-2.html">Single Custom Two</a></li>
                                            <li><a href="portfolio-single-gallery.html">Single Gallery Portfolio</a></li>
                                            <li><a href="portfolio-single-fw.html">Single Full Width Portfolio</a></li>
                                            <li><a href="portfolio-single-fw-slides.html">Single Full Width Slides</a></li>
                                            <li><a href="portfolio-single-extended.html">Single Extended Portfolio <span class="v-menu-item-info">Hot</span></a></li>
                                        </ul>
                                    </li>
                                    <li class="dropdown-submenu">
                                        <a href="#">Portfolio Variations</a>
                                        <ul class="dropdown-menu">
                                            <li><a href="portfolio-right-sidebar.html">Right Sidebar Portfolio</a></li>
                                            <li><a href="portfolio-left-sidebar.html">Left Sidebar Portfolio</a></li>
                                        </ul>
                                    </li>
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a class="dropdown-toggle" href="#">Blog <i class="fa fa-caret-down"></i></a>
                                <ul class="dropdown-menu">
                                    <!-- <li><a href="blog-standard-sidebar.html">Blog Timeline</a></li> -->
                                    <li><a href="blog-standard-sidebar.html">Blog Standard</a></li>
                                    <li><a href="blog-mini-sidebar.html">Blog Small</a></li>
                                    <li><a href="blog-masonry.html">Blog Masonry</a></li>
                                    <li><a href="blog-masonry-fw.html">Blog Fullwidth Masonry</a></li>
                                    <li class="dropdown-submenu">
                                        <a href="#">Blog Posts</a>
                                        <ul class="dropdown-menu">
                                            <li><a href="blog-standard-post.html">Standard Post</a></li>
                                            <li><a href="blog-full-width-post.html">Full Width Post</a></li>
                                        </ul>
                                    </li>
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a class="dropdown-toggle" href="contact-us.html">Contact</a>
                                <ul class="dropdown-menu">
                                    <li><a href="contact-us.html"><i class="fa fa-send-o"></i>Contact Us - v1</a></li>
                                    <li><a href="contact-us-2.html"><i class="fa fa-send-o"></i>Contact Us - v2</a></li>
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a class="dropdown-toggle" href="#">Support <i class="fa fa-caret-down"></i></a>
                                <ul class="dropdown-menu">
                                    <li><a href="changelog.html"><i class="fa fa-terminal"></i>Changelog</a></li>
                                    <li><a href="theme-plugins.html"><i class="fa fa-star-o"></i>Theme Plugins</a></li>
                                </ul>
                            </li>

                        </ul>
                    </nav>
                    <!--End Main Menu-->
                </div>
                <button class="btn btn-responsive-nav btn-inverse" data-toggle="collapse" data-target=".nav-main-collapse">
                    <i class="fa fa-bars"></i>
                </button>
            </div>
        </header>

    </div>
    <!--End Header-->

    <div id="container">

 
        <div class="v-page-heading v-bg-stylish v-bg-stylish-v6">
            <div class="container">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="heading-text">
                            <h1 class="entry-title">Google Maps</h1>
                        </div>

                        <ol class="breadcrumb">
                            <li><a href="#">Element</a></li>
                            <li class="active">Google Maps</li>
                        </ol>
                    </div>
                </div>
            </div>
        </div>


        <div class="v-page-wrap no-bottom-spacing">
          
        <div class="container">
 
            <div class="row">
                <div class="col-md-12">
                    <h3 class="no-margin-top">Full Width (Inside Container)</h3>
                    <div id="googlemapsFullWidthInside" class="google-map mt-none mb-none" style="height: 280px;"></div>
                </div>
            </div>

            <div class="row">
                <div class="v-spacer col-sm-12 v-height-small"></div>
            </div>

            <div class="row">
                <div class="col-md-6">
                    <h3>Markers</h3>
                    <div id="googlemapsMarkers" class="google-map mt-none mb-lg" style="height: 280px;"></div>
                </div>
                <div class="col-md-6">
                    <h3>Custom Controls</h3>
                    <div id="googlemapsControls" class="google-map mt-none mb-none" style="height: 280px;"></div>
                </div>
            </div>

            <div class="row">
                <div class="v-spacer col-sm-12 v-height-small"></div>
            </div>

            <div class="row">
                <div class="v-spacer col-sm-12 v-height-small"></div>
            </div>
		<!-- 지도를 표시할 div 입니다 -->
 <div id="map" ></div> 
 
<div id="right">1<br>2<br>3<br>4<br>5<br>6<br>7<br>8<br>9<br>10<br>11<br>12<br>13<br>14<br>
15<br>16<br>17<br>18<br>19<br>20<br>21<br>22<br>23<br>24</div>

<script>
//지도 생성시에 옵션을 지정할 수 있습니다.
var map = new naver.maps.Map('map', {
		//size: new naver.maps.Size(800, 400),
        center:new naver.maps.LatLng(35.8700614655724,128.591220388309),
       zoom: 8, //지도의 초기 줌 레벨
        minZoom: 1, //지도의 최소 줌 레벨
        zoomControl: true, //줌 컨트롤의 표시 여부
        zoomControlOptions: { //줌 컨트롤의 옵션
            position: naver.maps.Position.TOP_RIGHT
        }
    });
map.setOptions("mapTypeControl", true); //지도 유형 컨트롤의 표시 여부
//setOptions 메서드를 통해 옵션을 조정할 수도 있습니다.


/////////////////경로 그리기 ////////////////
function polylines(){

polyline = new naver.maps.Polyline({
    
    path:gpsarr,
    strokeColor: 'red',
    endIcon: naver.maps.PointingIcon.BLOCK_ARROW,
    endIconSize: 10,
    strokeOpacity: 0.5,
    strokeWeight: 2
		});
	polyline.setMap(map);
}
////////////////경로 그리기 끝////////////////

function polylineremove(){

		polyline.setMap(null);
	}

//마커찍기 
function markers(){
	for(i=0;i<gpsarr.length;i++){
 	markers[i] = new naver.maps.Marker({
   	 position: gpsarr[i],
 	icon: {
        url: 'http://static.naver.net/maps/v3/pin_default.png',
        size: new naver.maps.Size(22, 35),
        anchor: new naver.maps.Point(11, 35)
    }
		});
 	markers[i].setMap(map);
	}
}	
/////////////////////////////끝 
/////마커 1개 찍기 
function getmarker(x,y){
	position = new naver.maps.LatLng(x, y);
 	marker = new naver.maps.Marker({
   	 position:position,
 	icon: {
        url: 'http://static.naver.net/maps/v3/pin_default.png',
        size: new naver.maps.Size(22, 35),
        anchor: new naver.maps.Point(11, 35)
  		  }
	});
 	marker.setMap(map);

}	

///////////////////////단일 마커 삭제 /////////
function getmarkerremove(){


 marker.setMap(null);
	}




/////////////////////단일마커 삭제 끝/////////
///////////////다중 마커 삭제 
function markerromve(){
	for(i=0;i<gpsarr.length;i++){
 	markers[i].setMap(null);
	}
}

///////////////////////////
var contentString = [
        '<div class="iw_inner">',
        '   <h3>서울특별시청</h3>',
        '   <p>서울특별시 중구 태평로1가 31 | 서울특별시 중구 세종대로 110 서울특별시청<br />',
        '   </p>',
        '</div>'
    ].join('');

var infowindow = new naver.maps.InfoWindow({
    content: contentString,
    maxWidth: 140,
    backgroundColor: "#eee",
    borderColor: "#2db400",
    borderWidth: 5,
    anchorSize: new naver.maps.Size(30, 30),
    anchorSkew: true,
    anchorColor: "#eee",
    pixelOffset: new naver.maps.Point(20, -20)
});

naver.maps.Event.addListener(marker, "click", function(e) {
    if (infowindow.getMap()) {
        infowindow.close();
    } else {
        infowindow.open(map, marker);
    }
});

</script>



	
<div id="find">
<form  action="" name="findtime">
<table>
<tr>
<td>start:</td>
<td>
<input type="datetime-local"   id="starttime" name="starttime" /></td>
<td>end:</td>
<td>
<input type="datetime-local" id="endtime" name="endtime" /></td>
<td><button  type="button" id="send" onclick="findtimes();">조회</button></td>

</form>
<td><button  type="button" onclick="getmarkerremove();">개별마커삭제</button></td>
<td><button  type="button" onclick="markerromve();">마커삭제</button></td>
<td><button  type="button" onclick="markers();">마커찍기</button></td>
<td><button  type="button" onclick="polylines();">라인생성</button></td>
<td><button  type="button" onclick="polylineremove();">라인제거</button></td>
<td><button  type="button" onClick="history.go(0)">초기화</button></td>
</tr>
</table>


</div>


<div id="commentList" ></div>
		
        

        </div>

       

            <!-- Full Width -->
            <!-- <div id="googlemapsFullWidth" class="google-map mt-none mb-none"></div> -->
        </div>
 

        <!--Footer-Wrap-->
        <div class="footer-wrap">
            <footer>
                <div class="container">
                    <div class="row">
                        <div class="col-sm-3">
                            <section class="widget">
                                <img alt="Volvox" src="img/logo-white.png" style="height: 40px; margin-bottom: 20px;">
                                <p class="pull-bottom-small">
                                    Donec quam felis, ultricies nec, pellen tesqueeu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel aliquet nec, vulputate eget aliquet nec, arcu.
                                </p>
                                <p>
                                    <a href="page-about-us-2.html">Read More →</a>
                                </p>
                            </section>
                        </div>
                        <div class="col-sm-3">
                            <section class="widget v-twitter-widget">
                                <div class="widget-heading">
                                    <h4>Latest Tweets</h4>
                                    <div class="horizontal-break"></div>
                                </div>
                                <ul class="v-twitter-widget">
                                    <li>
                                        <div class="tweet-text">
                                            <a href="#" target="_blank">@Volvox</a>
                                            Lorem ipsum dolor sit amet, consec adipiscing elit onvallis dignissim.
                                        </div>
                                        <div class="twitter_intents">
                                            <a class="timestamp" href="#" target="_blank">3 hours ago</a>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="tweet-text">
                                            <a href="#" target="_blank">@Volvox</a>
                                            Sed blandit conval dignissim. pharetra velit eu velit et erat pharetra.
                                        </div>
                                        <div class="twitter_intents">
                                            <a class="timestamp" href="#" target="_blank">5 days ago</a>
                                        </div>
                                    </li>
                                </ul>
                            </section>
                        </div>
                        <div class="col-sm-3">
                            <section class="widget v-recent-entry-widget">
                                <div class="widget-heading">
                                    <h4>Recent Posts</h4>
                                    <div class="horizontal-break"></div>
                                </div>
                                <ul>
                                    <li>
                                        <a href="blog-standard-post.html">Amazing Standard Post</a>
                                    </li>
                                    <li>
                                        <a href="blog-full-width-post.html">Full Width Media Post</a>
                                    </li>
                                    <li>
                                        <a href="blog-video-post.html">Perfect Video Post</a>
                                    </li>
                                    <li>
                                        <a href="blog-slideshow-post.html">Amazing Slideshow post</a>
                                    </li>
                                </ul>
                            </section>
                        </div>
                        <div class="col-sm-3">
                            <section class="widget">
                                <div class="widget-heading">
                                    <h4>Recent Works</h4>
                                    <div class="horizontal-break"></div>
                                </div>
                                <ul class="portfolio-grid">
                                    <li>
                                        <a href="portfolio-single.html" class="grid-img-wrap">
                                            <img src="img/thumbs/project-1.jpg" />
                                            <span class="tooltip">Phasellus enim libero<span class="arrow"></span></span>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="portfolio-single.html" class="grid-img-wrap">
                                            <img src="img/thumbs/project-2.jpg" />
                                            <span class="tooltip">Phasellus enim libero<span class="arrow"></span></span>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="portfolio-single.html" class="grid-img-wrap">
                                            <img src="img/thumbs/project-3.jpg" />
                                            <span class="tooltip">Phasellus enim<span class="arrow"></span></span>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="portfolio-single.html" class="grid-img-wrap">
                                            <img src="img/thumbs/project-4.png" />
                                            <span class="tooltip">Lorem Imput<span class="arrow"></span></span>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="portfolio-single.html" class="grid-img-wrap">
                                            <img src="img/thumbs/project-5.jpg" />
                                            <span class="tooltip">Phasellus Enim libero<span class="arrow"></span></span>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="portfolio-single.html" class="grid-img-wrap">
                                            <img src="img/thumbs/project-6.jpg" />
                                            <span class="tooltip">Phasellus Enim<span class="arrow"></span></span>
                                        </a>
                                    </li>
                                </ul>
                            </section>
                        </div>
                    </div>
                </div>
            </footer>

            <div class="copyright">
                <div class="container">
                    <p>© Copyright 2016 by Volvox. All Rights Reserved.</p>

                

                </div>
            </div>
        </div>
        <!--End Footer-Wrap-->
    </div>

    <!--// BACK TO TOP //-->
    <div id="back-to-top" class="animate-top"><i class="fa fa-angle-up"></i></div>

    <!-- Libs -->
    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/jquery.flexslider-min.js"></script>
    <script src="js/jquery.easing.js"></script>
    <script src="js/jquery.fitvids.js"></script>
    <script src="js/jquery.carouFredSel.min.js"></script>
    <script src="js/jquery.validate.js"></script>
    <script src="js/theme-plugins.js"></script>
    <script src="js/jquery.isotope.min.js"></script>
    <script src="js/imagesloaded.js"></script>
    <script src="js/jquery.gmap.js"></script>

    <script src="js/view.min.js?auto"></script>
    <script src="js/theme-core.js"></script>

	<!-- Examples --> 
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCGouGLyXYr9GnbZrcH9FHVx9wbNa3Gcss"></script>



</body>
</html>
