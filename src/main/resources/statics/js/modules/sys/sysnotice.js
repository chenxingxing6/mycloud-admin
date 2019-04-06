$(function () {
    $('.summernote').summernote({
        height : '220px',
        lang : 'zh-CN'
    });

    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/sysnotice/list',
        datatype: "json",
        colModel: [
			{ label: '编号', name: 'id', index: 'id', width: 50, key: true },
			{ label: '公告标题', name: 'noticeTitle', index: 'notice_title', width: 80 },
            { label: '公告类型', name: 'noticeType', width: 60, index: 'notice_type',formatter: function(value, options, row){
                    return value === 1 ?
                        '<span class="label label-danger">通知</span>' :
                        '<span class="label label-success">公告</span>';
                }},
            { label: '状态', name: 'status', width: 60, index: 'status',formatter: function(value, options, row){
                    return value === 0 ?
                        '<span class="label label-success">正常</span>' :
                        '<span class="label label-danger">关闭</span>';
            }},
			{ label: '创建者', name: 'createUser', index: 'create_user', width: 80 },
			{ label: '创建', name: 'createTime', index: 'create_time', width: 80 },
			{ label: '操作', name: '', index: 'remark', width: 80 ,
                formatter: function(value, row, index) {
                    var actions = [];
                    actions.push('<span class="label label-success">编辑</span>&nbsp;&nbsp;');
                    actions.push('<span class="label label-danger">删除</span>');
                    return actions.join('');
                }
			}
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth:true,
        multiselect: true,
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
		showList: true,
		title: null,
		sysNotice: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.sysNotice = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
            var comment=$(".summernote").code()
			this.sysNotice.noticeContent = comment;
			console.log("comment"+comment)
			//$(".summernote").code(data.comment)
			var url = vm.sysNotice.id == null ? "sys/sysnotice/save" : "sys/sysnotice/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.sysNotice),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}

			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "sys/sysnotice/delete",
                    contentType: "application/json",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(id){
			$.get(baseURL + "sys/sysnotice/info/"+id, function(r){
                vm.sysNotice = r.sysNotice;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                page:page
            }).trigger("reloadGrid");
		}
	}
});
