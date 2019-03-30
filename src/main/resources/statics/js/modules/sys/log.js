$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/log/list',
        datatype: "json",
        colModel: [
			{ label: 'id', name: 'id', width: 30, key: true },
			{ label: '用户名', name: 'username', width: 50 },
			{ label: '用户操作', name: 'operation', width: 70 },
			{ label: '请求方法', name: 'method', width: 150 },
			{ label: '请求参数', name: 'params', width: 80 },
            { label: '执行时长(毫秒)', name: 'time', width: 80 },
			{ label: 'IP地址', name: 'ip', width: 70 },
			{ label: '创建时间', name: 'createDate', width: 90 }
        ],
		viewrecords: true,
        height: 585,
        rowNum: 30,
		rowList : [10,30,50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth:true,
        multiselect: false,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page",
            rows:"limit",
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			key: null
		},
	},
	methods: {
		query: function () {
			vm.reload();
		},
		reload: function (event) {
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
				postData:{'key': vm.q.key},
                page:page
            }).trigger("reloadGrid");
		},
        clean: function () {
            confirm('确定要清除系统日志吗？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/log/delete",
                    contentType: "application/json",
                    success: function(r){
                        if(r.code == 0){
                            alert('操作成功', function(){
                                vm.reload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        }
	}
});
