<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
 
<!DOCTYPE html>
<html>
<head>
    
    <title>지도 생성하기</title>
<script src="../scripts/jquery-3.2.1.min.js"></script> 
<script type="text/javascript" 
src="https://openapi.map.naver.com/openapi/v3/maps.js?clientId=YsYH9nE0qsa_VSAWAP24&submodules=geocoder">
</script>
<script src="ajax.js"></script>
</head>
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
	 width:80%;  
	height:600px;
	  float: left;
     position: relative;
     
}
#right {
	border: 1px solid #F6F6F6;	
   // background-color: #09C;
   float: left;
    height: 100%;
  /*  width: 250px; */
    position: relative;
    
}
#commentList{
		/* width: 1100px; */
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
	/*	width: 900px; */
	
		 color: black;
		 border: 1px solid #F6F6F6;
		   float: left;
     position: relative;
      clear: both;
		 
}

</style>

<script >
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
		
	$("#starttime").val(today +"T12:50:00");
	$("#endtime").val(today +"T13:50:00");
	
	
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
	new ajax.xhr.Request("../GpsServ",param,loadCommentResult,"get");	
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
<body>

<!-- 지도를 표시할 div 입니다 -->
 <div id="map" ></div> 
 
<div id="right">1<br>2<br>3<br>4<br>5<br>6<br>7<br>8<br>9<br>10<br>11<br>12<br>13<br>14<br>
15<br>16<br>17<br>18<br>19<br>20<br>21<br>22<br>23<br>24</div>

<script>
//지도 생성시에 옵션을 지정할 수 있습니다.
var map = new naver.maps.Map('map', {
		//size: new naver.maps.Size(800, 400),
        center:new naver.maps.LatLng(35.8700614655724,128.591220388309),
       zoom: 11, //지도의 초기 줌 레벨
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
/* var contentString = [
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
}); */

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


</body>
</html>