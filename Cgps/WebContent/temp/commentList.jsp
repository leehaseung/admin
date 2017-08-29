<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Insert title here</title>
<style>
	.comment{border:1px dotted blue;}
</style>
<script src="ajax.js"></script>
<script type="text/javascript" src="./chatting/ajax.js"></script>
	<script type="text/javascript" src="./chatting/chat.js"></script>

<script>
//페이지 로딩 후에 목록조회 ajax요청

window.onload = function(){
	loadCommentList();
	var chatting = new chat.Chat();
	
}
//목록요청
function loadCommentList(){
	new ajax.xhr.Request("CommentsServ?cmd=selectAll","",loadCommentResult,"get");	
}
	//목록요청 callback
function loadCommentResult(req){
		if(req.readyState == 4) {
			if(req.status == 200){
				var xmlDoc = req.responseXML; //서버의 실행 결과 XML
				var code = xmlDoc.getElementsByTagName("code") //code 태그내용
					.item(0).firstChild.nodeValue;
					console.log("==");
					
				if(code == 'success'){
					console.log("==");
					var data = xmlDoc.getElementsByTagName("data") //code 태그내용
					.item(0).firstChild.nodeValue;
					var commentList = eval( "("+ data +")" ); //string -> 객체
					var listDiv = document.getElementById("commentList");// 출력할 위치
					for(i=0;i<commentList.length;i++){  //댓글 수만큼 반복
						var div = makeCommentView(commentList[i]); //댓글 div 생성
						listDiv.appendChild(div);
					}
				}else{
					alert("댓글로딩 실패:" + req.status)
				}
			}
		}
}
function makeCommentView(comment){
	var commentDiv = document.createElement("div"); //태그 생성 <div>
	commentDiv.setAttribute("id","c"+comment.id);  //속성지정<div id ='c1'/>
	var html="<strong>"+comment.name+"</strong><br>"+ comment.content +
		"<input type=\"button\" value=\"수정\" onclick=\"viewUpdate('"+comment.id+"')\"/> "+
		"<input type=\"button\" value=\"삭제\" onclick=\"confirmDeletion('"+comment.id+"')\"/>";
	commentDiv.innerHTML = html; //태그내용 지정
	commentDiv.className = "comment";  //태그 스타일 속성 <div id ='c1' class="comment" />
	// <div id='c1' class="comment">태그내용 </div>
	commentDiv.comment = comment;
	return commentDiv;
}


//댓글 등록 요청
function addComment(){
	var name = document.addForm.name.value;      //작성자 텍스트필드 입력값
	var content = document.addForm.content.value;  //댓글
	var param = "cmd=insert&name="+ name + "&content=" + content ;
	new ajax.xhr.Request("CommentsServ",param,addResult,"get");	
}

//댓글 등록 콜백함수
function addResult(req){
	if(req.readyState == 4) {
			if(req.status == 200){
			var xmlDoc = req.responseXML; //서버의 실행 결과 XML
			var code = xmlDoc.getElementsByTagName("code") //code 태그내용
				.item(0).firstChild.nodeValue;
				console.log("==");
				
			if(code == 'success'){
				console.log("==");
				var data = xmlDoc.getElementsByTagName("data") //code 태그내용
				.item(0).firstChild.nodeValue;
				var comment = eval( "("+ data +")" ); //string -> 객체
				var listDiv = document.getElementById("commentList");// 출력할 위치
				
				var div = makeCommentView(comment); //댓글 div 생성
				listDiv.appendChild(div);
				
			}else{
				alert("댓글로딩 실패:" + req.status)
			}
		}
	}
	
}
//수정버튼 클릭 시 수정 폼 보이도록
function viewUpdate(commentId){
	var commentDiv = document.getElementById("c" + commentId);// 수정할 댓글
	var updateFormDiv = document.getElementById("commentUpdate");//수정폼이 있는 div	
	
	commentDiv.appendChild(updateFormDiv); //수정폼을 수정할 댓글로 이동
	updateFormDiv.style.display = '';    // 수정폼이 보이도록
	//수정할 값을 텍스트필드에 보이도록
	document.updateForm.id.value = commentDiv.comment.id;
	document.updateForm.name.value = commentDiv.comment.name;
	document.updateForm.content.value = commentDiv.comment.content;
}

//댓글 수정 요청
function updateComment(){
	var id = document.updateForm.id.value;
	var name = document.updateForm.name.value;      //작성자 텍스트필드 입력값
	var content = document.updateForm.content.value;  //댓글
	var param = "cmd=update&name="+ name + "&content=" + content + "&id="+id;
	new ajax.xhr.Request("CommentsServ",param,updateResult,"get");	
}

//댓글 수정 콜백
function updateResult(req){
	if(req.readyState == 4) {
		if(req.status == 200){
		var xmlDoc = req.responseXML; //서버의 실행 결과 XML
		var code = xmlDoc.getElementsByTagName("code") //code 태그내용
			.item(0).firstChild.nodeValue;
			console.log("==");
			
		if(code == 'success'){
			console.log("==");
			var data = xmlDoc.getElementsByTagName("data") //code 태그내용
			.item(0).firstChild.nodeValue;
			var comment = eval( "("+ data +")" ); //string -> 객체
			var listDiv = document.getElementById("commentList");// 출력할 위치
			//수정폼은 body 밑으로 이동
			
			var updateFormDiv = document.getElementById("commentUpdate");
			updateFormDiv.style.display = 'none';
			document.body.appendChild(updateFormDiv);
			
			//수정된 div로 교체
			var div = makeCommentView(comment); //댓글 div 생성
			var oldDiv = document.getElementById("c" + comment.id);
			//document.body.appendChild()
			listDiv.replaceChild(div,oldDiv);
			
		}else{
			alert("댓글로딩 실패:" + req.status)
		}
	}
}

}

function confirmDeletion(commentId){
	if(confirm("삭제하시겠습니까?")){
		var params = "cmd=delete&id="+commentId;
		new ajax.xhr.Request("CommentsServ",params,removeResult,'POST');
	}
}

function removeResult(req){
	if(req.readyState == 4) {
		if(req.status == 200){
		var xmlDoc = req.responseXML; //서버의 실행 결과 XML
		var code = xmlDoc.getElementsByTagName("code") //code 태그내용
			.item(0).firstChild.nodeValue;
			
		if(code == 'success'){
		
			var data = xmlDoc.getElementsByTagName("data") //code 태그내용
			.item(0).firstChild.nodeValue;
			var comment = eval( "("+ data +")" ); //string -> 객체
			var deleteId = comment.id;
			var commentDiv = document.getElementById("c"+deleteId);
			commentDiv.parentNode.removeChild(commentDiv);
			alert("삭제했습니다.");
		}else if(code == 'fail'){
			var message = xmlDoc.getElementsByTagName('message')
				.item(0).firstchild.nodeValue;
			alert("에러발생: "+ message);
		}
		}else{
			alert("서버 에러 발생:" + req.status)
		}
	}
}
</script>
</head>
<body>

<div id="commentList"></div>

<!-- 댓글등록시작 -->
<div id="commentAdd">
	<form action="" name="addForm">
	이름: <input type="text" name="name" size="10"><br/>
	내용: <textarea name="content" cols="20" rows="2"></textarea><br/>
	<input type="button" value="등록" onclick="addComment()"/>
	</form>
</div>
<!-- 댓글등록끝 -->

<!-- 댓글수정폼시작 -->
<div id="commentUpdate" style="display:none">
	<form action="" name="updateForm">
	<input type="hidden" name="id" value=""/>
	이름: <input type="text" name="name" size="10"><br/>
	내용: <textarea name="content" cols="20" rows="2"></textarea><br/>
	<input type="button" value="등록" onclick="updateComment()"/>
	<input type="button" value="취소" onclick="cancelUpdate()"/>
	</form>
</div>
<!-- 댓글수정폼끝 -->
	
</body>
</html>