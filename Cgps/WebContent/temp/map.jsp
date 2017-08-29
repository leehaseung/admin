<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
 
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>지도 생성하기</title>
    
</head>
<style>


</style>
<script src="ajax.js"></script>
<script >
var gpsarr = [];
window.onload = function(){
	loadCommentList();
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
					gps();
				}else{
					alert("댓글로딩 실패:" + req.status)
				}
				
			}
		}
}
function makeCommentView(comment){
	var commentDiv = document.createElement("div"); //태그 생성 <div>
	commentDiv.setAttribute("no","c"+comment.no);  //속성지정<div id ='c1'/>
	var html=comment.no +" " +comment.x+" " + comment.y + " " + comment.addr +" " + comment.gdate ;
		
	commentDiv.innerHTML = html; //태그내용 지정
	commentDiv.className = "comment";  //태그 스타일 속성 <div id ='c1' class="comment" />
	// <div id='c1' class="comment">태그내용 </div>
	commentDiv.comment = comment;
	return commentDiv;
}




</script>
<body>
<!-- 지도를 표시할 div 입니다 -->
<div id="map" style="width:700px;height:550px;"></div>
<script type="text/javascript" 
src="https://openapi.map.naver.com/openapi/v3/maps.js?clientId=YsYH9nE0qsa_VSAWAP24&submodules=geocoder">
</script>
<script>
//지도 생성시에 옵션을 지정할 수 있습니다.
var map = new naver.maps.Map('map', {
       // center: new naver.maps.LatLng(35.8349615335798,128.718405207739), //지도의 초기 중심 좌표
        center: new naver.maps.LatLng(35.8713442703601,128.589150102048),
       zoom: 8, //지도의 초기 줌 레벨
        minZoom: 1, //지도의 최소 줌 레벨
        zoomControl: true, //줌 컨트롤의 표시 여부
        zoomControlOptions: { //줌 컨트롤의 옵션
            position: naver.maps.Position.TOP_RIGHT
        }
    });

//setOptions 메서드를 통해 옵션을 조정할 수도 있습니다.
function gps(){
map.setOptions("mapTypeControl", true); //지도 유형 컨트롤의 표시 여부
var polyline = new naver.maps.Polyline({
    map: map,
    path:gpsarr
	 
});

console.log(gpsarr);
}
</script>
<!-- 검색기능 -->


<div id="commentList" ></div>
</body>
</html>