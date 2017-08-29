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
				//editurl: "../DeptDMLServ.do",
				datatype: "json",
				pager:"#pager",
				colModel: [
					{ label: '서버', 
						name: 'svrName', 
						width: 45,
						key: true,
						editable: true
					},
					{ label: '시', 
						name: 'sdate', 
						width: 100,
						key: true,
						editable: true
					},
					{ label: '일일in', 
						name: 'ifhcin',
						width: 50,	
						key: true,
						editable: true
						},
					
						{ label: '일일out',
						name: 'ifhcout',
						width: 50,
						key: true,
						editable: true
						},
						{ label: 'cpu1load',
							name: 'cpu1load',
							width: 30,
							key: true,
							editable: true
						},
						{ label: 'cpu5load',
							name: 'cpu5load',
							width: 30,
							key: true,
					     	editable: true
							},
							{ label: 'cpu15load',
							name: 'cpu15load',
							width: 30,
							key: true,
					     	editable: true
			  			},
			  			{ label: 'usercpu',
							name: 'usercpu',
							width: 30,
							key: true,
					     	editable: true
			  			},
			  			{ label: 'syscpu',
							name: 'syscpu',
							width: 30,
							key: true,
					     	editable: true
			  			},
			  			{ label: 'idlecpu',
							name: 'idlecpu',
							width: 30,
							key: true,
					     	editable: true
			  			},
			  			{ label: 'availswap',
							name: 'availswap',
							width: 50,
							key: true,
					     	editable: true
			  			},
					           
			  			{ label: 'totalram',
							name: 'totalram',
							width: 50,
							key: true,
					     	editable: true
			  			},
					           
			  			{ label: 'usedram',
							name: 'usedram',
							width: 50,
							key: true,
					     	editable: true
			  			},
			  			{ label: 'freeram',
							name: 'freeram',
							width: 50,
							key: true,
					     	editable: true
			  			},
			  			{ label: 'availdsik',
							name: 'availdsik',
							width: 50,
							key: true,
					     	editable: true
			  			},
			  			{ label: 'useddisk',
							name: 'useddisk',
							width: 50,
							key: true,
					     	editable: true
			  			},
					           
					             
				],
				loadonce: true,
				page:1,
			viewrecords: true, // show the current page, data rang and total records on the toolbar
			width: 1200,
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
   <div class="v-page-wrap no-bottom-spacing">

         	<table id="list"></table>
	<div id="pager"></div>
     

          

        
        </div>


          

</body>
</html>