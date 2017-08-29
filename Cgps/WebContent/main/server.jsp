<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Insert title here</title>
<link href="../jqGrid/css/ui.jqgrid.css" rel="stylesheet">
<link rel="stylesheet" href="../scripts/jquery-ui.min.css">
<script src="../scripts/jquery-3.2.1.min.js"></script>
<script src="../scripts/jquery-ui.min.js"></script>
<script src="../jqGrid/js/jquery.jqGrid.js"></script>
<script src="../jqGrid/js/i18n/grid.locale-kr.js"></script>


<style>
</style>
<script>
$(function(){
	var grid= $("#list").jqGrid(
			{
				
				url:"../DeptListAllJsonServ",
				editurl: "../DeptDMLServ.do",
				datatype: "json",
				pager:"#pager",
				colModel: [
					{ label: '부서명', 
						name: 'departmentName', 
						width: 75,
						key: true,
						editable: true
					},
					{ label: '지역코드', 
						name: 'locationId', 
						width: 90,
						key: true,
						editable: true
					},
					{ label: '부서id', 
						name: 'departmentId',
						width: 100,	
						key: true,
						editable: true
						},
					
					{ label: '매니저id',
						name: 'managerId',
						width: 80,
						key: true,
						editable: true
					}
					             
				],
				loadonce: true,
				page:1,
			viewrecords: true, // show the current page, data rang and total records on the toolbar
			width: 780,
			height: 200,
			rownumbers: true, // show row numbers
			 rownumWidth: 25, // the width of the row numbers columns
			rowNum: 30,
			loadonce: true, // this is just for the demo
		
			});
	  $('#list').navGrid('#pager',
       { edit: true, 
		  add: true, 
		  del: true 
              },// options for the Edit Dialog
      {
          editCaption: "부서 등록",
          recreateForm: true,
			//checkOnUpdate : true,
			//checkOnSubmit : true,
			beforeSubmit : function( postdata, form , oper) {
				if( confirm('Are you sure you want to update this row?') ) {
					// do something
					return [true,''];
				} else {
					return [false, 'You can not submit!'];
				}
			},
          closeAfterEdit: true,
          errorTextFormat: function (data) {
              return 'Error: ' + data.responseText
          }
      },
      // options for the Add Dialog
      {
          closeAfterAdd: true,
          recreateForm: true,
          errorTextFormat: function (data) {
              return 'Error: ' + data.responseText
          }
      },
      // options for the Delete Dailog
      {
          errorTextFormat: function (data) {
              return 'Error: ' + data.responseText
          }
      }  );
         
});
</script>
</head>
<body>
<!-- ui/dept_grid.jsp -->
	<table id="list"></table>
	<div id="pager"></div>
</body>
</html>