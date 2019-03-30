$(function () {
    /*$("#jqGrid").jqGrid({
        url: baseURL + 'front/follow/list',
        datatype: "json",
        colModel: [
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '用户ID', name: 'fromUserId', index: 'from_user_id', width: 80 },
			{ label: '被关注用户ID', name: 'toUserId', index: 'to_user_id', width: 80 },
			{ label: '是否有效', name: 'isValid', index: 'is_valid', width: 80 },
			{ label: '登录者', name: 'createUser', index: 'create_user', width: 80 },
			{ label: '登录时间', name: 'createTime', index: 'create_time', width: 80 },
			{ label: '更新者', name: 'opUser', index: 'op_user', width: 80 },
			{ label: '更新时间', name: 'opTime', index: 'op_time', width: 80 },
			{ label: '版本号', name: 'lastVer', index: 'last_ver', width: 80 }
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
    });*/
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
        username: null,
		followeds: [],
		follow: []
	},
	created(){
        this.initData();
	},
	methods: {
        initData: function(){
            $.get(baseURL + "front/follow/list/", function(r){
                vm.follow = r.follow;
                vm.followeds = r.followeds;
            });
        },
		query: function () {
            $.ajax({
                type: "GET",
                url: baseURL + "front/follow/list",
                contentType: "application/json",
                data: JSON.stringify(vm.username),
                success: function(r) {
                    if (r.code === 0) {
                        this.followeds = r.data.followeds;
                        this.follow = r.data.follow;
                        console.log(this.followeds)
                        console.log(this.follow)
                    }else{
                    	alert(r.msg);
					}
                }
            });
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.follow = {};
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
			var url = vm.follow.id == null ? "front/follow/save" : "front/follow/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.follow),
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
				    url: baseURL + "front/follow/delete",
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
			$.get(baseURL + "front/follow/info/"+id, function(r){
                vm.follow = r.follow;
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
